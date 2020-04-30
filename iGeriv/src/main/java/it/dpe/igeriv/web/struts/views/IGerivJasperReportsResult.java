package it.dpe.igeriv.web.struts.views;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.jasperreports.JasperReportsResult;
import org.apache.struts2.views.jasperreports.ValueStackShadowMap;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import ch.lambdaj.group.Group;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivConstants.SQLType;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.vo.FatturaClienteEdicolaVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiDocumentiEmessiVo;
import it.dpe.igeriv.vo.pk.ParametriEdicolaPk;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Sottclasse di JasperReportsResult che permette il salvataggio in locale
 * dei reports delle fatture.
 * 
 * N.B. 
 * Essendo la variabile byte[] output, contente i bytes del report, inaccessibile alla sottoclasse 
 * (i metodi che la usano sono private) è stato necessario copiare tutti i metodi della 
 * classe padre JasperReportsResult.
 * 
 * @author mromano
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class IGerivJasperReportsResult extends JasperReportsResult {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = LoggerFactory.getLogger(IGerivJasperReportsResult.class);
	
	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		// Will throw a runtime exception if no "datasource" property. TODO Best place for that is...?
        initializeProperties(invocation);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating JasperReport for dataSource = " + dataSource + ", format = " + format);
        }

        HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) invocation.getInvocationContext().get(ServletActionContext.HTTP_RESPONSE);

        // Handle IE special case: it sends a "contype" request first.
        // TODO Set content type to config settings?
        if ("contype".equals(request.getHeader("User-Agent"))) {
            try {
                response.setContentType("application/pdf");
                response.setContentLength(0);

                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.close();
            } catch (IOException e) {
                LOG.error("Error writing report output", e);
                throw new ServletException(e.getMessage(), e);
            }
            return;
        }

        // Construct the data source for the report.
        ValueStack stack = invocation.getStack();
        IGerivValueStackDataSource stackDataSource = null;

        Connection conn = (Connection) stack.findValue(connection);
        if (conn == null) {
            stackDataSource = new IGerivValueStackDataSource(stack, dataSource);
        }
        
        // Determine the directory that the report file is in and set the reportDirectory parameter
        // For WW 2.1.7:
        //  ServletContext servletContext = ((ServletConfig) invocation.getInvocationContext().get(ServletActionContext.SERVLET_CONFIG)).getServletContext();
        ServletContext servletContext = (ServletContext) invocation.getInvocationContext().get(ServletActionContext.SERVLET_CONTEXT);
        String systemId = servletContext.getRealPath(finalLocation);
        Map parameters = new ValueStackShadowMap(stack);
        File directory = new File(systemId.substring(0, systemId.lastIndexOf(File.separator)));
        parameters.put("reportDirectory", directory);
        parameters.put(JRParameter.REPORT_LOCALE, invocation.getInvocationContext().getLocale());

        // put timezone in jasper report parameter
        if (timeZone != null) {
            timeZone = conditionalParse(timeZone, invocation);
            final TimeZone tz = TimeZone.getTimeZone(timeZone);
            if (tz != null) {
                // put the report time zone
                parameters.put(JRParameter.REPORT_TIME_ZONE, tz);
            }
        }

        // Add any report parameters from action to param map.
        Map reportParams = (Map) stack.findValue(reportParameters);
        if (reportParams != null) {
            LOG.debug("Found report parameters; adding to parameters...");
            parameters.putAll(reportParams);
        }

        byte[] output;
        JasperPrint jasperPrint;
        JasperReport jasperReport = null;
        
        // Fill the report and produce a print object
        try {
            jasperReport = (JasperReport) JRLoader.loadObjectFromFile(systemId);
            if (conn == null)
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, stackDataSource);
            else
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
        } catch (JRException e) {
            LOG.error("Error building report for uri " + systemId, e);
            throw new ServletException(e.getMessage(), e);
        }

        // Export the print object to the desired output format
        try {
            if (contentDisposition != null || documentName != null) {
                final StringBuffer tmp = new StringBuffer();
                tmp.append((contentDisposition == null) ? "inline" : contentDisposition);

                if (documentName != null) {
                    tmp.append("; filename=");
                    tmp.append(documentName);
                    tmp.append(".");
                    tmp.append(format.toLowerCase());
                }

                response.setHeader("Content-disposition", tmp.toString());
            }

            JRExporter exporter = buildExporter(request, response, stack, jasperPrint);

            output = exportReportToBytes(jasperPrint, exporter);
            
			saveReportToFile(invocation, parameters, output, stackDataSource, stack, jasperReport, request, response);
        } catch (JRException e) {
            String message = "Error producing " + format + " report for uri " + systemId;
            LOG.error(message, e);
            throw new ServletException(e.getMessage(), e);
        }

        response.setContentLength(output.length);

        // Will throw ServletException on IOException.
        writeReport(response, output);
	}

	private JRExporter buildExporter(HttpServletRequest request, HttpServletResponse response, ValueStack stack, JasperPrint jasperPrint) throws ServletException {
		JRExporter exporter;

		if (format.equals(FORMAT_PDF)) {
		    response.setContentType("application/pdf");
		    exporter = new JRPdfExporter();
		} else if (format.equals(FORMAT_CSV)) {
		    response.setContentType("text/plain");
		    exporter = new JRCsvExporter();
		} else if (format.equals(FORMAT_HTML)) {
		    response.setContentType("text/html");

		    // IMAGES_MAPS seems to be only supported as "backward compatible" from JasperReports 1.1.0

		    Map imagesMap = new HashMap();
		    request.getSession(true).setAttribute("IMAGES_MAP", imagesMap);

		    exporter = new JRHtmlExporter();
		    exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
		    exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath() + imageServletUrl);

		    // Needed to support chart images:
		    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		    request.getSession().setAttribute("net.sf.jasperreports.j2ee.jasper_print", jasperPrint);
		} else if (format.equals(FORMAT_XLS)) {
		    response.setContentType("application/vnd.ms-excel");
		    exporter = new JRXlsExporter();
		} else if (format.equals(FORMAT_XML)) {
		    response.setContentType("text/xml");
		    exporter = new JRXmlExporter();
		} else if (format.equals(FORMAT_RTF)) {
		    response.setContentType("application/rtf");
		    exporter = new JRRtfExporter();
		} else {
		    throw new ServletException("Unknown report format: " + format);
		}

		Map exportParams = (Map) stack.findValue(exportParameters);
		if (exportParams != null) {
		    LOG.debug("Found export parameters; adding to exporter parameters...");
		    exporter.getParameters().putAll(exportParams);
		}
		return exporter;
	}

	
	/**
	 * Scrive il report pdf su disco e inserisce nel DB
	 * 
	 * @param invocation
	 * @param parameters
	 * @param output
	 * @param stackDataSource
	 * @param stack
	 * @param jasperReport
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ParseException
	 * @throws JRException
	 * @throws ServletException
	 */
	private void saveReportToFile(ActionInvocation invocation, Map parameters, byte[] output, IGerivValueStackDataSource stackDataSource, ValueStack stack, JasperReport jasperReport, HttpServletRequest request, HttpServletResponse response) throws IOException, FileNotFoundException, ParseException, JRException, ServletException {
		if (invocation.getInvocationContext().getParameters() != null) {
			Object tipDoc = invocation.getInvocationContext().getParameters().get("tipoDocumento");
			boolean isFatturaOrStorno = tipDoc != null
					&& tipDoc instanceof String[] 
					&& NumberUtils.isNumber(((String[]) tipDoc)[0])
					&& (new Integer(((String[]) tipDoc)[0]).equals(IGerivConstants.FATTURA)
						|| new Integer(((String[])tipDoc)[0]).equals(IGerivConstants.STORNO_FATTURA));
			boolean isDocumentoResaProdottiVari = tipDoc != null
					&& tipDoc instanceof String[] 
					&& NumberUtils.isNumber(((String[]) tipDoc)[0])
					&& (new Integer(((String[]) tipDoc)[0]).equals(IGerivConstants.BOLLA_RESA_PRODOTTI_VARI));
			if (isFatturaOrStorno) {
				Map<Long, Long> mapClienteNumFattura = new HashMap<Long, Long>();
				String fileName = contentDisposition.replace(".pdf", "").split("=")[1].replaceAll("\"", "");
				String[] split = fileName.split("_");
				String ccli = split[1];
				String numFatture = split[4];
				String[] arrCli = ccli.split("-");
				String[] arrFatt = numFatture.split("-");
				String dtComp = split[6];
				for (int i = 0; i < arrCli.length; i++) {
					mapClienteNumFattura.put(new Long(arrCli[i]), new Long(arrFatt[i]));
				}
				Group<VenditeParamDto> group = group(((List<VenditeParamDto>) stackDataSource.getDataSourceList()), by(on(VenditeParamDto.class).getCodCliente()));
				for (Group<VenditeParamDto> subgroup : group.subgroups()) {
					List<VenditeParamDto> subGroupList = subgroup.findAll();
					Long codCliente = subGroupList.get(0).getCodCliente();
					stack.setValue("vendite", subGroupList);
					IGerivValueStackDataSource vsds = new IGerivValueStackDataSource(stack, "vendite");
			        JasperPrint jp = JasperFillManager.fillReport(jasperReport, parameters, vsds);
			        JRExporter exp = buildExporter(request, response, stack, jp);
			        byte[] bytes = exportReportToBytes(jp, exp);
			        String coddl = parameters.get("coddl").toString();
					String codEdicola = parameters.get("codedicola").toString();
					String codUtente = parameters.get("codutente").toString();
					String pathFattureEdicole = parameters.get("pathFattureEdicole").toString();
					String name = MessageFormat.format(IGerivMessageBundle.get("igeriv.fattura.file.name"), new Object[]{codCliente.toString(), DateUtilities.getTimestampAsStringExport(new Date(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS), mapClienteNumFattura.get(codCliente).toString(), IGerivConstants.FATTURA, dtComp})+ ".pdf";
					Integer tipoDocumento = new Integer(((String[]) tipDoc)[0]);
					writeToDisk(bytes, coddl, codEdicola, pathFattureEdicole, name);
					save(tipoDocumento, bytes, codEdicola, codUtente, name);
					setParamNumerazioneInizialeFatture(request, codEdicola, name);
				}
			} else if (isDocumentoResaProdottiVari) {
				String fileName = contentDisposition.replace(".pdf", "").replaceAll("/", "").split("=")[1].replaceAll("\"", "");
				IGerivValueStackDataSource vsds = new IGerivValueStackDataSource(stack, "dettagli");
		        JasperPrint jp = JasperFillManager.fillReport(jasperReport, parameters, vsds);
		        JRExporter exp = buildExporter(request, response, stack, jp);
		        byte[] bytes = exportReportToBytes(jp, exp);
		        String coddl = parameters.get("coddl").toString();
		        Integer codEdicola = (Integer) parameters.get("codedicola");
				String codUtente = parameters.get("codutente").toString();
				Integer codFornitore = (Integer) parameters.get("codFornitore");
				String dataDocumento = parameters.get("dataDocumento").toString();
				String numeroDocumento = parameters.get("numeroDocumento").toString();
				String pathBolleResaProdottiVariEdicole = parameters.get("pathBolleResaProdottiVariEdicole").toString();
				String name = fileName + ".pdf";
				writeToDisk(bytes, coddl, codEdicola.toString(), pathBolleResaProdottiVariEdicole, name);
				saveDocumentoBollaResa(parameters, bytes, codEdicola, codUtente, codFornitore, dataDocumento, numeroDocumento, name);
			}
		}
	}

	/**
	 * Setta la numerazione iniziale delle fatture nella session e nella tabella parametri edicola
	 * 
	 * @param request
	 * @param codEdicola
	 * @param name 
	 */
	private void setParamNumerazioneInizialeFatture(HttpServletRequest request, String codEdicola, String name) {
		final EdicoleService edicoleService = SpringContextManager.getSpringContext().getBean(EdicoleService.class);
		ParametriEdicolaVo vo = edicoleService.getParametroEdicola(new Integer(codEdicola), IGerivConstants.COD_PARAMETRO_NUMERAZIONE_INIZIALE_FATTURE);
		if (vo == null) {
			vo = new ParametriEdicolaVo();
			ParametriEdicolaPk pk = new ParametriEdicolaPk();
			pk.setCodEdicola(new Integer(codEdicola));
			pk.setCodParametro(IGerivConstants.COD_PARAMETRO_NUMERAZIONE_INIZIALE_FATTURE);
			vo.setPk(pk);
		}
		String codFattura = name.substring(0, name.lastIndexOf(".")).split("_")[4];
		vo.setValue(codFattura);
		edicoleService.saveBaseVo(vo);
		Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) request.getSession().getAttribute(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
		ParametriEdicolaDto dto = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_NUMERAZIONE_INIZIALE_FATTURE) ? mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_NUMERAZIONE_INIZIALE_FATTURE) : null;
		if (dto == null) {
			dto = new ParametriEdicolaDto();
			dto.setCodEdicola(new Integer(codEdicola));
			dto.setCodParametro(IGerivConstants.COD_PARAMETRO_NUMERAZIONE_INIZIALE_FATTURE);
			dto.setSqlType(SQLType.INTEGER);
		}
		dto.setValue(codFattura);
		mapParam.put(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_NUMERAZIONE_INIZIALE_FATTURE, dto);
	}

	/**
	 * Scrive la fattura pdf su disco
	 * 
	 * @param byte[] output
	 * @param String coddl
	 * @param String codedicola
	 * @param String pathFattureEdicole
	 * @param String name
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void writeToDisk(byte[] output, String coddl, String codedicola, String path, String name) throws IOException, FileNotFoundException {
		ByteArrayOutputStream baos = null;
		OutputStream outputStream = null;
		try {
			baos = new ByteArrayOutputStream();
			baos.write(output);
			File dirEstrattoConto = new File(path + System.getProperty("file.separator") + coddl + System.getProperty("file.separator") + codedicola);
			if (!dirEstrattoConto.isDirectory()) {
				dirEstrattoConto.mkdirs();
			}
			outputStream = new FileOutputStream(new File(dirEstrattoConto + System.getProperty("file.separator") + name)); 
			baos.writeTo(outputStream);
		} finally {
			if (baos != null) {
				baos.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
	
	/**
	 * Inserisce la fattura nel db
	 * 
	 * @param Integer tipoDocumento
	 * @param byte[] output
	 * @param String codEdicola
	 * @param String codUtente
	 * @param String  name
	 * @throws ParseException
	 */
	private void save(Integer tipoDocumento, byte[] bytes, String codEdicola, String codUtente, String name) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat(DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
		String fileName = name.substring(0, name.lastIndexOf("."));
		String[] split  = fileName.split("_");
		String codCliente = split[1];
		FatturaClienteEdicolaVo vo = new FatturaClienteEdicolaVo();
		vo.setCodEdicola(new Integer(codEdicola));
		vo.setCodUtente(codUtente);
		vo.setCodCliente(new Long(codCliente));
		vo.setDataFattura(new Timestamp(sdf.parse(split[2]).getTime()));
		vo.setDataCompetenzaFattura(new Timestamp(sdf.parse(split[6]).getTime()));
		vo.setNomeFileFattura(name);
		vo.setNumeroFattura(new Integer(split[4]));
		vo.setTipoDocumento(tipoDocumento);
		vo.setFattura(bytes);
		final ClientiService<FatturaClienteEdicolaVo> clientiService = SpringContextManager.getSpringContext().getBean(ClientiService.class);
		clientiService.saveBaseVo(vo);
	}
	
	/**
	 * Inserisce il documento bolla resa nel db
	 * 
	 * @param parameters
	 * @param output1
	 * @param codEdicola
	 * @param codUtente
	 * @param codFornitore
	 * @param dataDocumento
	 * @param numeroDocumento
	 * @param name
	 * @throws ParseException
	 */
	public void saveDocumentoBollaResa(Map parameters, byte[] output1, Integer codEdicola, String codUtente, Integer codFornitore, String dataDocumento, String numeroDocumento, String name) throws ParseException {
		final ProdottiService prodottiService = SpringContextManager.getSpringContext().getBean(ProdottiService.class);
		Long idDoc = (Long) parameters.get("idDocumento");
		Long codForn = new Long(codFornitore);
		ProdottiNonEditorialiDocumentiEmessiVo vo = prodottiService.getProdottiNonEditorialiDocumentiEmessi(codForn, idDoc);
		if (vo == null) {
			vo = new ProdottiNonEditorialiDocumentiEmessiVo();
		}
		vo.setCodEdicola(new Integer(codEdicola));
		vo.setCodFornitore(codForn);
		vo.setCodUtente(codUtente);
		vo.setDataDocumento(DateUtilities.parseDate(dataDocumento, DateUtilities.FORMATO_DATA_SLASH));
		vo.setDocumento(output1);
		vo.setIdDocumento(idDoc);
		vo.setNomeFile(name);
		vo.setNumeroDocumento(numeroDocumento != null && NumberUtils.isNumber(numeroDocumento) ? new Integer(numeroDocumento) : null);
		vo.setTipoDocumento(IGerivConstants.BOLLA_RESA_PRODOTTI_VARI);
		prodottiService.saveBaseVo(vo);
	}
	
	/**
     * Writes report bytes to response output stream.
     *
     * @param response Current response.
     * @param output   Report bytes to write.
     * @throws ServletException on stream IOException.
     */
    private void writeReport(HttpServletResponse response, byte[] output) throws ServletException {
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(output);
            outputStream.flush();
        } catch (IOException e) {
            LOG.error("Error writing report output", e);
            throw new ServletException(e.getMessage(), e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LOG.error("Error closing report output stream", e);
                throw new ServletException(e.getMessage(), e);
            }
        }
    }

    /**
     * Sets up result properties, parsing etc.
     *
     * @param invocation Current invocation.
     * @throws Exception on initialization error.
     */
    private void initializeProperties(ActionInvocation invocation) throws Exception {
        if (dataSource == null && connection == null) {
            String message = "No dataSource specified...";
            LOG.error(message);
            throw new RuntimeException(message);
        }
        if (dataSource != null)
            dataSource = conditionalParse(dataSource, invocation);

        format = conditionalParse(format, invocation);
        if (StringUtils.isEmpty(format)) {
            format = FORMAT_PDF;
        }

        if (contentDisposition != null) {
            contentDisposition = conditionalParse(contentDisposition, invocation);
        }

        if (documentName != null) {
            documentName = conditionalParse(documentName, invocation);
        }

        reportParameters = conditionalParse(reportParameters, invocation);
        exportParameters = conditionalParse(exportParameters, invocation);
    }

    /**
     * Run a Jasper report to CSV format and put the results in a byte array
     *
     * @param jasperPrint The Print object to render as CSV
     * @param exporter    The exporter to use to export the report
     * @return A CSV formatted report
     * @throws net.sf.jasperreports.engine.JRException
     *          If there is a problem running the report
     */
    private byte[] exportReportToBytes(JasperPrint jasperPrint, JRExporter exporter) throws JRException {
        byte[] output;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        if (delimiter != null) {
            exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, delimiter);
        }

        exporter.exportReport();

        output = baos.toByteArray();

        return output;
    }
}
