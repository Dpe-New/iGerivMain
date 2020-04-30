package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.dto.EstrattoContoClienti;
import it.dpe.igeriv.dto.EstrattoContoClientiXmlDto;
import it.dpe.igeriv.dto.EstrattoContoHeaderFooterXmlDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.XmlUtils;

import java.io.File;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Action per l'esecuzione dei reports.
 * I reports compilati (.jasper) devono essere presenti nel direttorio "/WEB-INF/jasper/" 
 * (configurato nel parametro "location" nella action corrente in struts.xml).
 * La List dataSourceList contiene i beans con i dati da essere inseriti nel form.
 * La Map reportParams contiene i parametri del report.
 * La conversione in xml dei dati è gestita dal framework.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked"})
@Scope("prototype")
@Component("reportAction")
public class ReportAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private List<?> dataSourceList;
	private String downloadToken;
	private HashMap reportParams = new HashMap();
	private Long codCliente;
	private String fileName;
	@Value("${path.fatture.edicole}")
	private String pathFattureEdicole;
	private String dataEstrattoConto;
	@Value("${img.miniature.edicola.path.dir}")
	private String imgMiniatureEdicolaPathDir;
	@Value("${igeriv.valore.euro.marca.da.bollo.estratto.conto}")
	private String valoreMarcaBollo;
	@Value("${path.estratti.conto.clienti}")
	private String pathEstrattiContoClienti;
	
	/**
	 * Produce il report pdf dell'estratto conto del cliente edicola andando 
	 * a prendere i dati dal file .xml prodotto dal metodo emettiEstrattoContoClientiEdicola()
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String emettiEstrattoContoSingoloClienteEdicola() throws Exception {
		if (!Strings.isNullOrEmpty(this.dataEstrattoConto) && codCliente != null) {
			String[] split = this.dataEstrattoConto.split("\\|");
			String strDataCompetenza = this.dataEstrattoConto.contains("|") ? split[2] : this.dataEstrattoConto;
			Timestamp dataCompetenza = DateUtilities.parseDate(strDataCompetenza, DateUtilities.FORMATO_DATA_SLASH);
			File xmlFileDir = new File(pathEstrattiContoClienti + System.getProperty("file.separator") + "xml" + System.getProperty("file.separator") + getAuthUser().getCodFiegDlMaster() + System.getProperty("file.separator") + getAuthUser().getCodEdicolaMaster());
			File file = new File(xmlFileDir, MessageFormat.format(IGerivMessageBundle.get("igeriv.estratto_conto_cliente.file.name"), new Object[]{codCliente.toString(), DateUtilities.getTimestampAsString(dataCompetenza, DateUtilities.FORMATO_DATA)}) + ".xml");
			if (file != null && file.isFile()) {
				EstrattoContoClientiXmlDto unmarshall = (EstrattoContoClientiXmlDto) XmlUtils.unmarshall(EstrattoContoClientiXmlDto.class, file);
				List<EstrattoContoClienti> listEstrattoContoDettaglio = new ArrayList<EstrattoContoClienti>();
				if (unmarshall.getListConsegnePubblicazioni() != null) {
					listEstrattoContoDettaglio.addAll(unmarshall.getListConsegnePubblicazioni());
				}
				if (unmarshall.getListConsegneProdotti() != null) { 
					listEstrattoContoDettaglio.addAll(unmarshall.getListConsegneProdotti());
				}
				dataSourceList = listEstrattoContoDettaglio;
				EstrattoContoHeaderFooterXmlDto header = unmarshall.getHeader();
				reportParams.put("logoFileName", header.getLogoFileName());
				reportParams.put("title", header.getTitle());
				reportParams.put("title1", header.getTitle1());
				reportParams.put("titoloLabel", header.getTitoloLabel());
				reportParams.put("prezzoLabel", header.getPrezzoLabel());
				reportParams.put("copieLabel", header.getCopieLabel());
				reportParams.put("edicola", header.getEdicola());
				reportParams.put("datiEdicola", header.getDatiEdicola());
				reportParams.put("datiEdicolaInt", header.getDatiEdicolaInt());
				reportParams.put("importoLabel", header.getImportoLabel());
				reportParams.put("totaleLabel", getText("igeriv.pne.report.magazzino.importo"));
				reportParams.put("importoScontatoLabel", getText("igeriv.pne.report.magazzino.importo")  + " " + getText("column.calc.total"));
				reportParams.put("ggDataDocLabel", getText("igeriv.giorni.data.documento"));
				reportParams.put("dataScadenzaPagamentoLabel", getText("igeriv.scadenza"));
				reportParams.put("controllareDatiLabel", header.getControllareDatiLabel());
				reportParams.put("scontoLabel", getText("igeriv.sconto"));
				reportParams.put("pagamentoLabel", header.getPagamentoLabel());
				reportParams.put("intestazioneCliente", header.getIntestazioneCliente());
				reportParams.put("pieDiPagina", header.getPieDiPagina().replaceAll("<br>","\\\n"));
				reportParams.put("valoreMarcaBollo", valoreMarcaBollo);
				reportParams.put("marcaBolloLabel", getText("igeriv.marca.bollo"));
				reportParams.put("numeroEstrattoContoLabel", header.getNumeroEstrattoContoLabel());
				reportParams.put("fileName", fileName);
				fileName = header.getFileName();
			}
		}
		setCookie();
		return IGerivConstants.ACTION_ESTRATTO_CONTO_CLIENTI_EDICOLA_PDF;
	}
	
	private void setCookie() {
		if (!Strings.isNullOrEmpty(downloadToken)) {
			downloadToken = downloadToken.replaceAll(",", "").trim();
			response.addCookie(new Cookie("fileDownloadToken", request.getParameter("downloadToken").toString()));
		}
	}

	public List<?> getDataSourceList() {
		return dataSourceList;
	}

	public void setDataSourceList(List<?> dataSourceList) {
		this.dataSourceList = dataSourceList;
	}

	public String getDownloadToken() {
		return downloadToken;
	}

	public void setDownloadToken(String downloadToken) {
		this.downloadToken = downloadToken;
	}

	public HashMap getReportParams() {
		return reportParams;
	}

	public void setReportParams(HashMap reportParams) {
		this.reportParams = reportParams;
	}

	public Long getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Long codCliente) {
		this.codCliente = codCliente;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPathFattureEdicole() {
		return pathFattureEdicole;
	}

	public void setPathFattureEdicole(String pathFattureEdicole) {
		this.pathFattureEdicole = pathFattureEdicole;
	}

	public String getDataEstrattoConto() {
		return dataEstrattoConto;
	}

	public void setDataEstrattoConto(String dataEstrattoConto) {
		this.dataEstrattoConto = dataEstrattoConto;
	}

	public String getImgMiniatureEdicolaPathDir() {
		return imgMiniatureEdicolaPathDir;
	}

	public void setImgMiniatureEdicolaPathDir(String imgMiniatureEdicolaPathDir) {
		this.imgMiniatureEdicolaPathDir = imgMiniatureEdicolaPathDir;
	}

	public String getValoreMarcaBollo() {
		return valoreMarcaBollo;
	}

	public void setValoreMarcaBollo(String valoreMarcaBollo) {
		this.valoreMarcaBollo = valoreMarcaBollo;
	}

	public String getPathEstrattiContoClienti() {
		return pathEstrattiContoClienti;
	}

	public void setPathEstrattiContoClienti(String pathEstrattiContoClienti) {
		this.pathEstrattiContoClienti = pathEstrattiContoClienti;
	}
	
}	
