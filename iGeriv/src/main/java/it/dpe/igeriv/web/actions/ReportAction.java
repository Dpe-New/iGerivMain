package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.sort;
import static ch.lambdaj.Lambda.sum;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import ch.lambdaj.group.Group;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.contabilita.ContabilitaService;
import it.dpe.igeriv.bo.inventario.InventarioService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.bo.scolastica.ScolasticaService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.bo.ws.epipoli.EpipoliWebServices;
import it.dpe.igeriv.dto.EstrattoContoClienti;
import it.dpe.igeriv.dto.EstrattoContoClientiProdottiDto;
import it.dpe.igeriv.dto.EstrattoContoClientiPubblicazioniDto;
import it.dpe.igeriv.dto.EstrattoContoClientiXmlDto;
import it.dpe.igeriv.dto.EstrattoContoHeaderFooterXmlDto;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.dto.RichiestaClienteReportEtichetteDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.XmlUtils;
import it.dpe.igeriv.visitor.ActionVisitor;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.FatturaClienteEdicolaVo;
import it.dpe.igeriv.vo.InventarioDettaglioVo;
import it.dpe.igeriv.vo.InventarioVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiProgressiviFatturazioneVo;
import it.dpe.igeriv.vo.RegistrazioneRisposteWSEpipoliVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiProgressiviFatturazionePk;
import it.dpe.igeriv.vo.pk.RichiestaClientePk;
import lombok.Getter;
import lombok.Setter;

/**
 * Action per l'esecuzione dei reports. I reports compilati (.jasper) devono
 * essere presenti nel direttorio "/WEB-INF/jasper/" (configurato nel parametro
 * "location" nella action corrente in struts.xml). La List dataSourceList
 * contiene i beans con i dati da essere inseriti nel form. La Map reportParams
 * contiene i parametri del report. La conversione in xml dei dati è gestita dal
 * framework.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("reportAction")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(getClass());
	private final ClientiService<ClienteEdicolaVo> clientiService;
	private final InventarioService inventarioService;
	private final RifornimentiService rifornimentiService;
	private final ContabilitaService contabilitaService;
	private final VenditeService venditeService;
	private final ScolasticaService scolasticaService;
	private final EpipoliWebServices epipoliWebServices;

	private final String pathFattureEdicole;
	private final String imgMiniatureEdicolaPathDir;
	private final String valoreMarcaBollo;
	private final String pathEstrattiContoClienti;

	private List<VenditeParamDto> vendite;
	private List<OrdineLibriDettVo> ordiniClienti;
	private List<RegistrazioneRisposteWSEpipoliVo> prodottiDigitali;
	private List<?> dataSourceList;
	private String ragSocCliente;
	private String downloadToken;
	private HashMap reportParams = new HashMap();
	private String idtnOrdini;
	private String dataTipoBolla;
	private Long codCliente;
	private Integer tipoDocumento;
	private String strDataA;
	private String strDataDocumento;
	private String onlyClosedEstratti;
	private String tipoReportOrdiniClienti;
	private Boolean esportaClientiDiTutteLeBolle;
	private Boolean hasProdottiMisti;
	private String fileName;
	private String spunte;
	private String strDataCompetenza;
	private String strDataComp;
	private String tipiEstrattoConto;
	private Integer tipoProdotti;
	private String dataEstrattoConto;
	private String exportDirEstrattiContoXml;
	private Integer scontoPerc;
	private Integer scontoPercPne;
	private Map<Long, Integer> sconto;
	private Map<Long, Integer> scontoPne;
	private Integer tipoProdottiInEstrattoConto;
	private Integer numFattura;
	private Long idInventario;
	private InputStream excelStream;
	private Integer tipo;
	private String ordiniSelezionati;
	private String idRichiestaWS;

	public ReportAction() {
		this.clientiService = null;
		this.inventarioService = null;
		this.rifornimentiService = null;
		this.contabilitaService = null;
		this.venditeService = null;
		this.scolasticaService = null;
		this.pathFattureEdicole = null;
		this.imgMiniatureEdicolaPathDir = null;
		this.valoreMarcaBollo = null;
		this.pathEstrattiContoClienti = null;
		this.epipoliWebServices = null;
	}

	@Autowired
	public ReportAction(ClientiService<ClienteEdicolaVo> clientiService, InventarioService inventarioService,
			RifornimentiService rifornimentiService, ContabilitaService contabilitaService,
			VenditeService venditeService, ScolasticaService scolasticaService,
			@Value("${path.fatture.edicole}") String pathFattureEdicole,
			@Value("${img.miniature.edicola.path.dir}") String imgMiniatureEdicolaPathDir,
			@Value("${igeriv.valore.euro.marca.da.bollo.estratto.conto}") String valoreMarcaBollo,
			@Value("${path.estratti.conto.clienti}") String pathEstrattiContoClienti,
			EpipoliWebServices epipoliWebServices) {
		this.clientiService = clientiService;
		this.inventarioService = inventarioService;
		this.rifornimentiService = rifornimentiService;
		this.contabilitaService = contabilitaService;
		this.scolasticaService = scolasticaService;
		this.venditeService = venditeService;
		this.pathFattureEdicole = pathFattureEdicole;
		this.imgMiniatureEdicolaPathDir = imgMiniatureEdicolaPathDir;
		this.valoreMarcaBollo = valoreMarcaBollo;
		this.pathEstrattiContoClienti = pathEstrattiContoClienti;
		this.epipoliWebServices = epipoliWebServices;
	}

	/**
	 * Produce il report pdf di: 1. bollettina di consegna 2. Fattura 3. Storno
	 * Fattura (Nota Credito) Nei casi 2 e 3 i report possono essere per clienti
	 * multipli (unico pdf per più clienti) e i file pdf vengono salvati
	 * separatamente su disco (al path pathFattureEdicole) e nel db (tbl_9309).
	 * <see>it.dpe.igeriv.web.struts.views.IGerivJasperReportsResult</see>
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String reportVenditePdf() throws Exception {
		vendite = select(vendite, notNullValue());
		if (!Strings.isNullOrEmpty(this.strDataDocumento) && this.strDataDocumento.contains(",")) {
			this.strDataDocumento = this.strDataDocumento.substring(0, this.strDataDocumento.indexOf(",")).trim();
		}
		Timestamp dataDocumento = !Strings.isNullOrEmpty(this.strDataDocumento)
				? DateUtilities.parseDate(this.strDataDocumento, DateUtilities.FORMATO_DATA_SLASH)
				: new Timestamp(new Date().getTime());
		if (tipoDocumento != null && (tipoDocumento.equals(IGerivConstants.FATTURA)
				|| tipoDocumento.equals(IGerivConstants.STORNO_FATTURA))) {
			if (tipoDocumento.equals(IGerivConstants.FATTURA)) {
				reportParams.put("tipoDocumento", getText("igeriv.fattura").toUpperCase());
			} else if (tipoDocumento.equals(IGerivConstants.STORNO_FATTURA)) {
				reportParams.put("tipoDocumento", getText("igeriv.nota.credito").toUpperCase());
			}
			reportParams.put("dataDocumento", this.strDataDocumento);
			reportParams.put("tipoDocumentoLabel", getText("igeriv.tipo.documento"));
			reportParams.put("numeroDocumentoLabel", getText("igeriv.numero.documento"));
			reportParams.put("dataDocumentoLabel", getText("igeriv.data.documento"));
			reportParams.put("descrizione", getText("igeriv.descrizione"));
			reportParams.put("qta", getText("igeriv.quantita"));
			reportParams.put("prezzoListino", getText("igeriv.prezzo.listino.short"));
			reportParams.put("sconto", getText("igeriv.sconto.perc"));
			reportParams.put("prezzoNetto", getText("igeriv.prezzo.netto.short"));
			reportParams.put("prezzoVendita", getText("igeriv.prezzo.vendita.short"));
			reportParams.put("importoNetto", getText("igeriv.importo.netto"));
			reportParams.put("importoIva", getText("igeriv.importo.iva.short"));
			reportParams.put("imponibile", getText("igeriv.imponibile"));
			reportParams.put("imponibileNetto", getText("igeriv.imponibile.netto"));
			reportParams.put("imponibileLordo", getText("igeriv.imponibile.lordo"));
			reportParams.put("importoIvaLabel", getText("igeriv.importo.iva"));
			reportParams.put("paginaLabel", getText("label.print.Page"));
			reportParams.put("coddl", getAuthUser().getCodFiegDlMaster());
			reportParams.put("codedicola", getAuthUser().getCodEdicolaMaster());
			reportParams.put("codutente", getAuthUser().getCodUtente());
			reportParams.put("pathFattureEdicole", pathFattureEdicole);
		} else {
			vendite = sort(vendite, on(VenditeParamDto.class).getTipoProdotto());
			reportParams.put("tipoProdottoEditoriale", IGerivConstants.TIPO_PRODOTTO_EDITORIALE);
			reportParams.put("tipoProdottoNonEditorialeConIVA", IGerivConstants.TIPO_PRODOTTO_NON_EDITORIALE_CON_IVA);
			reportParams.put("tipoProdottoNonEditorialeIVAEsente",
					IGerivConstants.TIPO_PRODOTTO_NON_EDITORIALE_IVA_ESENTE);
			fileName = MessageFormat.format(IGerivMessageBundle.get("igeriv.consegne.file.name"), new Object[] {
					DateUtilities.getTimestampAsStringExport(new Date(), DateUtilities.FORMATO_DATA_SECONDI) });
		}
		for (VenditeParamDto dto : vendite) {
			dto.setTitolo(dto.getTitolo().replaceAll(IGerivConstants.EURO_SIGN_TEXT, "€"));
		}
		Group<VenditeParamDto> group = group(vendite, by(on(VenditeParamDto.class).getCodCliente()));
		List<Long> listCodClienti = new ArrayList<Long>();
		List<Integer> listNumeroFatture = new ArrayList<Integer>();
		Timestamp dataCompetenza = !Strings.isNullOrEmpty(this.strDataCompetenza)
				? DateUtilities.parseDate(this.strDataCompetenza, DateUtilities.FORMATO_DATA_SLASH) : null;
		if (!group.subgroups().isEmpty()) {
			vendite = new ArrayList<VenditeParamDto>();
			Map<Long, BigDecimal> mapClienteImporto = new HashMap<Long, BigDecimal>();
			Map<Long, Set<Timestamp>> mapClienteDateCompetezaEc = new HashMap<Long, Set<Timestamp>>();
			Map<Long, Integer> mapClienteNumeroFattura = new HashMap<Long, Integer>();
			for (Group<VenditeParamDto> subgroup : group.subgroups()) {
				List<VenditeParamDto> subGroupList = subgroup.findAll();
				List<VenditeParamDto> groupedSubList = groupListVenditeParamDto(subGroupList);
				VenditeParamDto venditeParamDto = groupedSubList.get(0);
				Long codCli = venditeParamDto.getCodCliente();
				Integer scontoCliente = sconto != null && sconto.get(codCli) != null && sconto.get(codCli) >= 0
						? sconto.get(codCli) : scontoPerc;
				Integer scontoPneCliente = scontoPne != null && scontoPne.get(codCli) != null
						&& scontoPne.get(codCli) >= 0 ? scontoPne.get(codCli) : scontoPercPne;
				forEach(groupedSubList, VenditeParamDto.class).setSconto(scontoCliente);
				forEach(groupedSubList, VenditeParamDto.class).setScontoPne(scontoPneCliente);
				List<VenditeParamDto> list10 = select(groupedSubList,
						having(on(VenditeParamDto.class).getAliquota(), equalTo(new BigDecimal(10)))
								.and(having(on(VenditeParamDto.class).getProdottoNonEditoriale(), equalTo(true))));
				BigDecimal importoIva10 = sum(list10, on(VenditeParamDto.class).getImportoIvaCalc()).setScale(2,
						BigDecimal.ROUND_HALF_EVEN);
				BigDecimal totaleNetto10 = sum(list10, on(VenditeParamDto.class).getImportoTotaleNettoPerRigaScontato())
						.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				List<VenditeParamDto> list21 = select(groupedSubList,
						having(on(VenditeParamDto.class).getAliquota(), equalTo(new BigDecimal(21)))
								.and(having(on(VenditeParamDto.class).getProdottoNonEditoriale(), equalTo(true))));
				BigDecimal importoIva21 = sum(list21, on(VenditeParamDto.class).getImportoIvaCalc()).setScale(2,
						BigDecimal.ROUND_HALF_EVEN);
				BigDecimal totaleNetto21 = sum(list21, on(VenditeParamDto.class).getImportoTotaleNettoPerRigaScontato())
						.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				List<VenditeParamDto> list4 = select(groupedSubList,
						having(on(VenditeParamDto.class).getAliquota(), equalTo(new BigDecimal(4)))
								.and(having(on(VenditeParamDto.class).getProdottoNonEditoriale(), equalTo(true))));
				BigDecimal importoIva4 = sum(list4, on(VenditeParamDto.class).getImportoIvaCalc()).setScale(2,
						BigDecimal.ROUND_HALF_EVEN);
				BigDecimal totaleNetto4 = sum(list4, on(VenditeParamDto.class).getImportoTotaleNettoPerRigaScontato())
						.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				List<VenditeParamDto> list0 = select(groupedSubList,
						having(on(VenditeParamDto.class).getAliquota(), equalTo(BigDecimal.ZERO))
								.and(having(on(VenditeParamDto.class).getProdottoNonEditoriale(), equalTo(true))));
				BigDecimal importoIva0 = sum(list0, on(VenditeParamDto.class).getImportoIvaCalc()).setScale(2,
						BigDecimal.ROUND_HALF_EVEN);
				BigDecimal totaleNetto0 = sum(list0, on(VenditeParamDto.class).getImportoTotaleNettoPerRigaScontato())
						.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				List<VenditeParamDto> list0_74 = select(groupedSubList,
						having(on(VenditeParamDto.class).getAliquota(), equalTo(BigDecimal.ZERO))
								.and(having(on(VenditeParamDto.class).getProdottoNonEditoriale(), equalTo(false))));
				BigDecimal importoIva0_74 = sum(list0_74, on(VenditeParamDto.class).getImportoIvaCalc()).setScale(2,
						BigDecimal.ROUND_HALF_EVEN);
				BigDecimal totaleNetto0_74 = sum(list0_74,
						on(VenditeParamDto.class).getImportoTotaleNettoPerRigaScontato()).setScale(2,
								BigDecimal.ROUND_HALF_EVEN);
				listCodClienti.add(codCli);
				Integer numFatt = 0;
				if (tipoDocumento != null) {
					numFatt = getNumeroFattura(tipoDocumento);
					listNumeroFatture.add(numFatt);
				}
				BigDecimal totaleGenerale = totaleNetto0_74.add(importoIva0_74).add(totaleNetto0).add(importoIva0)
						.add(totaleNetto4).add(importoIva4).add(totaleNetto10).add(importoIva10).add(totaleNetto21)
						.add(importoIva21).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				forEach(groupedSubList, VenditeParamDto.class).setImportoIva0(importoIva0);
				forEach(groupedSubList, VenditeParamDto.class).setImportoIva0_74(importoIva0_74);
				forEach(groupedSubList, VenditeParamDto.class).setImportoIva4(importoIva4);
				forEach(groupedSubList, VenditeParamDto.class).setImportoIva10(importoIva10);
				forEach(groupedSubList, VenditeParamDto.class).setImportoIva21(importoIva21);
				forEach(groupedSubList, VenditeParamDto.class).setTotaleNetto0(totaleNetto0);
				forEach(groupedSubList, VenditeParamDto.class).setTotaleNetto0_74(totaleNetto0_74);
				forEach(groupedSubList, VenditeParamDto.class).setTotaleNetto4(totaleNetto4);
				forEach(groupedSubList, VenditeParamDto.class).setTotaleNetto10(totaleNetto10);
				forEach(groupedSubList, VenditeParamDto.class).setTotaleNetto21(totaleNetto21);
				forEach(groupedSubList, VenditeParamDto.class).setNumeroDocumento(numFatt.toString());
				forEach(groupedSubList, VenditeParamDto.class).setDataDocumento(dataDocumento);
				forEach(groupedSubList, VenditeParamDto.class).setTotaleGenerale(totaleGenerale);
				if (codCli != null && (tipoProdottiInEstrattoConto == null
						|| !tipoProdottiInEstrattoConto.equals(IGerivConstants.TIPO_PRODOTTO_MISTO))) {
					ClienteEdicolaVo cliente = clientiService.getClienteEdicola(getAuthUser().getArrId(), codCli);
					forEach(groupedSubList, VenditeParamDto.class).setDatiCliente(cliente.getDatiCliente());
				} else if (!Strings.isNullOrEmpty(ragSocCliente)) {
					forEach(groupedSubList, VenditeParamDto.class).setDatiCliente(ragSocCliente);
				}
				vendite.addAll(groupedSubList);
				mapClienteImporto.put(codCli, totaleGenerale);
				mapClienteDateCompetezaEc.put(codCli, new HashSet(
						extract(subGroupList, on(VenditeParamDto.class).getDataCompetenzaEstrattoContoClienti())));
				mapClienteNumeroFattura.put(codCli, numFatt);
			}
			if (!mapClienteImporto.isEmpty() && dataCompetenza != null) {
				clientiService.saveFatturaEmessaAndPagamento(clientiService.getSysdate(), dataCompetenza, tipoProdotti,
						mapClienteImporto, mapClienteDateCompetezaEc, mapClienteNumeroFattura, tipoDocumento);
			}
		}
		if (tipoDocumento != null
				&& (tipoDocumento.equals(IGerivConstants.FATTURA)
						|| tipoDocumento.equals(IGerivConstants.STORNO_FATTURA))
				&& listNumeroFatture != null && !listNumeroFatture.isEmpty()) {
			if (tipoDocumento.equals(IGerivConstants.FATTURA)) {
				fileName = MessageFormat.format(IGerivMessageBundle.get("igeriv.fatture.clienti.file.name"),
						new Object[] { Joiner.on("-").join(listCodClienti),
								DateUtilities.getTimestampAsStringExport(new Date(),
										DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS),
								Joiner.on("-").join(listNumeroFatture), IGerivConstants.FATTURA,
								DateUtilities.getTimestampAsStringExport(dataCompetenza,
										DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) });
			}
		}
		if (tipoDocumento != null && tipoDocumento.equals(IGerivConstants.STORNO_FATTURA) && numFattura != null) {
			FatturaClienteEdicolaVo vo = clientiService.getFatturaClienteEdicola(codCliente,
					getAuthUser().getCodEdicolaMaster(), numFattura);
			if (vo != null) {
				vo.setTipoDocumento(IGerivConstants.FATTURA_STORNATA);
				clientiService.saveBaseVo(vo);
			}
			venditeService.updateVenditePerStornoFattura(codCliente, numFattura);
			fileName = MessageFormat.format(IGerivMessageBundle.get("igeriv.nota.credito.file.name"),
					new Object[] { Joiner.on("-").join(listCodClienti),
							DateUtilities.getTimestampAsStringExport(new Date(),
									DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS),
							Joiner.on("-").join(listNumeroFatture), IGerivConstants.STORNO_FATTURA,
							DateUtilities.getTimestampAsStringExport(vo.getDataCompetenzaFattura(),
									DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) });
		}
		Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap
				.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
		String logoFileName = mapParam
				.containsKey(
						IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
								+ IGerivConstants.COD_PARAMETRO_LOGOMARCA_EDICOLA)
										? mapParam
												.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
														+ IGerivConstants.COD_PARAMETRO_LOGOMARCA_EDICOLA)
												.getParamValue()
										: "";
		reportParams.put("logoFileName", Strings.isNullOrEmpty(logoFileName.trim()) ? null
				: imgMiniatureEdicolaPathDir + System.getProperty("file.separator") + logoFileName);
		reportParams.put("datiEdicola",
				((getAuthUser().getRagioneSocialeEdicola() != null)
						? getAuthUser().getRagioneSocialeEdicola().replaceAll("&nbsp;", " ") : "")
						+ "    "
						+ ((getAuthUser().getIndirizzoEdicolaPrimaRiga() != null)
								? getAuthUser().getIndirizzoEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
						+ " - "
						+ ((getAuthUser().getLocalitaEdicolaPrimaRiga() != null)
								? getAuthUser().getLocalitaEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
						+ "            "
						+ (getAuthUser().getPiva() != null ? getText("dpe.partita.iva")
								: getText("dpe.codice.fiscale.2"))
						+ ": " + ((getAuthUser().getPiva() != null) ? getAuthUser().getPiva().replaceAll("&nbsp;", " ")
								: getAuthUser().getCodiceFiscale()));
		reportParams.put("datiEdicolaInt",
				((getAuthUser().getRagioneSocialeEdicola() != null)
						? getAuthUser().getRagioneSocialeEdicola().replaceAll("&nbsp;", " ") : "")
						+ "\n"
						+ ((getAuthUser().getIndirizzoEdicolaPrimaRiga() != null)
								? getAuthUser().getIndirizzoEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
						+ " - "
						+ ((getAuthUser().getLocalitaEdicolaPrimaRiga() != null)
								? getAuthUser().getLocalitaEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
						+ "\n"
						+ (getAuthUser().getPiva() != null ? getText("dpe.partita.iva")
								: getText("dpe.codice.fiscale.2"))
						+ ": " + ((getAuthUser().getPiva() != null) ? getAuthUser().getPiva().replaceAll("&nbsp;", " ")
								: getAuthUser().getCodiceFiscale()));
		reportParams.put("intestazioneCliente", getText("igeriv.spett"));
		reportParams.put("ragSocEdicola", getAuthUser().getRagioneSocialeEdicola().toUpperCase());
		reportParams.put("indirizzo",
				(getAuthUser().getIndirizzoEdicolaPrimaRiga() != null
						? getAuthUser().getIndirizzoEdicolaPrimaRiga().toUpperCase() : "")
						+ (getAuthUser().getLocalitaEdicolaPrimaRiga() != null
								? " - " + getAuthUser().getLocalitaEdicolaPrimaRiga().toUpperCase() : ""));
		reportParams.put("pieDiPagina",
				(mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
						+ IGerivConstants.COD_PARAMETRO_TESTO_FONDO_RICEVUTE))
								? mapParam
										.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
												+ IGerivConstants.COD_PARAMETRO_TESTO_FONDO_RICEVUTE)
										.getValue().replaceAll("<br>", "\\\n")
								: "");
		reportParams.put("codiceFiscale", getAuthUser().getCodiceFiscale());
		reportParams.put("piva", getAuthUser().getPiva());
		reportParams.put("edicola", getText("igeriv.provenienza.evasione.edicola") + ":");
		reportParams.put("cliente", getText("igeriv.provenienza.evasione.cliente") + ":");
		reportParams.put("pivaCodFiscale", getText("dpe.piva.cod.fiscale") + ":");
		reportParams.put("titolo", getText("label.print.Table.Title"));
		reportParams.put("sottotitolo", getText("label.print.Table.Subtitle"));
		reportParams.put("prezzo", getText("label.print.Table.Price"));
		reportParams.put("copie", getText("igeriv.copie.lavorazione.resa"));
		reportParams.put("totale", getText("column.calc.total"));
		reportParams.put("iva", getText("dpe.iva"));
		reportParams.put("totaleGenerale", getText("column.calc.grand.total"));
		reportParams.put("dataUscitaTitle", getText("igeriv.data.uscita"));
		reportParams.put("prodottiEditoriali", getText("igeriv.prodotti.editoriali"));
		reportParams.put("prodottiNonEditoriali", getText("igeriv.prodotti.non.editoriali"));
		reportParams.put("prodottiNonEditorialiIvaEsente", getText("igeriv.prodotti.non.editoriali.iva.esente"));
		reportParams.put("datiPerRicevutaFiscale", getText("igeriv.dati.per.ricevuta.fiscale.scontrino"));
		reportParams.put("inclusoIva", getText("igeriv.incluso.iva"));
		reportParams.put("ggDataDocLabel", getText("igeriv.giorni.data.documento"));
		reportParams.put("dataScadenzaPagamentoLabel", getText("igeriv.scadenza"));
		reportParams.put("controllareDatiLabel", getText("igeriv.msg.controllare.dati.fiscali"));
		reportParams.put("scontoLabel", getText("igeriv.sconto.pubb"));
		reportParams.put("scontoPneLabel", getText("igeriv.sconto.prodotti"));
		reportParams.put("pagamentoLabel", getText("igeriv.pagamento"));
		reportParams.put("hasProdottiMisti", hasProdottiMisti);

		setCookie();
		if (tipoDocumento != null && (tipoDocumento.equals(IGerivConstants.FATTURA)
				|| tipoDocumento.equals(IGerivConstants.STORNO_FATTURA))) {
			return IGerivConstants.ACTION_VENDITE_REPORT_FATTURA_PDF;
		} else {
			return IGerivConstants.ACTION_VENDITE_REPORT_PDF;
		}
	}

	public String reportVenditeLibriPdf() throws Exception {
		float totale = 0f;
		String[] strIds = ordiniSelezionati.split(",");
		Long[] ids = new Long[strIds.length];
		for (int i = 0; i < strIds.length; ++i) {
			ids[i] = Long.parseLong(strIds[i]);
		}
		ordiniClienti = scolasticaService.getOrdiniFornitore(ids);

		String edicola_codice = null;
		String edicola_ragione_sociale = null;
		String edicola_indirizzo = null;
		String edicola_contatti = null;

		String cliete_codice = null;
		String cliete_nome = null;
		String cliete_cognome = null;

		if (ordiniClienti != null) {
			for (OrdineLibriDettVo ordine : ordiniClienti) {
				BigDecimal prezzoTotaleLibro = new BigDecimal(0);

				BigDecimal p = ordine.getPrezzoCopertina();
				if (p != null) {
					totale += p.floatValue();
					prezzoTotaleLibro = prezzoTotaleLibro.add(p);
				}
				BigDecimal pCopertinatura = ordine.getPrezzoCopertinaCliente();
				if (pCopertinatura != null) {
					totale += pCopertinatura.floatValue();
					prezzoTotaleLibro = prezzoTotaleLibro.add(pCopertinatura);
				}
				ordine.setPrezzoTotaleLibro(prezzoTotaleLibro);

			}
			// Vittorio 27/08/15
			// edicola_codice = getAuthUser().getCodDpeWebEdicola().toString();
			edicola_codice = getAuthUser().getCodEdicolaDl().toString();
			edicola_ragione_sociale = getAuthUser().getRagioneSocialeEdicola();
			//edicola_indirizzo = (getAuthUser().getIndirizzoAgenziaPrimaRiga() != null)
			//		? getAuthUser().getIndirizzoAgenziaPrimaRiga() : null;
			edicola_indirizzo = getAuthUser().getIndirizzoEdicolaPrimaRiga() != null
					? getAuthUser().getIndirizzoEdicolaPrimaRiga()
					: " ";

			ClienteEdicolaVo clienteVo = clientiService.getClienteEdicola(getAuthUser().getArrId(),
					ordiniClienti.get(0).getOrdine().getCodCliente());

			cliete_codice = clienteVo.getCodCliente().toString();
			cliete_nome = (clienteVo.getNome() != null) ? clienteVo.getNome() : null;
			cliete_cognome = (clienteVo.getCognome() != null) ? clienteVo.getCognome() : null;

		}

		reportParams.put("TitoloDocumento",
				"Rivendita: " + getAuthUser().getRagioneSocialeEdicola() + " \nRicevuta non fiscale");

		reportParams.put("edicola_codice", edicola_codice);
		reportParams.put("edicola_ragione_sociale", edicola_ragione_sociale);
		reportParams.put("edicola_indirizzo", edicola_indirizzo);
		reportParams.put("edicola_contatti", edicola_contatti);
		reportParams.put("cliete_codice", cliete_codice);
		reportParams.put("cliete_nome", cliete_nome);
		reportParams.put("cliete_cognome", cliete_cognome);

		reportParams.put("Totale", String.format("%,.02f", totale));
		fileName = "VenditaCliente";
		setCookie();
		return IGerivConstants.ACTION_VENDITE_LIBRI_REPORT_PDF;
	}

	/**
	 * Crea il report pdf dell'inventario
	 * 
	 * @throws IOException
	 */
	public String reportAttivazioneProdottiDigitaliGiftCardEpipoli() throws IOException {

		idRichiestaWS = idRichiestaWS.trim();
		log.info("ReportAttivazioneProdottiDigitaliGiftCardEpipoli : " + idRichiestaWS);

		int occurance = StringUtils.countOccurrencesOf(idRichiestaWS, "|");
		Integer[] ids = new Integer[occurance];
		int i = 0;

		StringTokenizer stringTokenizer = new StringTokenizer(idRichiestaWS, "|");
		while (stringTokenizer.hasMoreElements()) {
			ids[i] = new Integer(stringTokenizer.nextElement().toString());
			i++;
		}

		String edicola_codice = null;
		String edicola_ragione_sociale = null;
		String edicola_indirizzo = null;
		String idRichiesta = idRichiestaWS;
		String serialNumber = null;
		String pin = null;
		String valore = null;
		String nomeProdotto = null;
		String istruzioniAttivazione = null;
		String scadenzaAttivazione = null;

		// DATI EDICOLA
		edicola_codice = getAuthUser().getCodEdicolaDl().toString();
		edicola_ragione_sociale = getAuthUser().getRagioneSocialeEdicola();
		edicola_indirizzo = getAuthUser().getIndirizzoEdicolaPrimaRiga() + " - "
				+ getAuthUser().getLocalitaEdicolaPrimaRiga();

		log.info("ReportAttivazioneProdottiDigitaliGiftCardEpipoli : CodiceEdicole - " + edicola_codice
				+ " | Rag.Sociale - " + edicola_ragione_sociale + " | Indirizzo - " + edicola_indirizzo + " |");

		prodottiDigitali = epipoliWebServices.findRispostaByIdRichiesta(ids, getAuthUser().getCodFiegDl(),getAuthUser().getCodEdicolaDl());

		log.info("ReportAttivazioneProdottiDigitaliGiftCardEpipoli : ProdottiDigitali size = "
				+ prodottiDigitali.size());

		
		reportParams.put("TitoloDocumento", " ATTIVAZIONE PRODOTTI DIGITALI");
		reportParams.put("edicola_codice", edicola_codice);
		reportParams.put("edicola_ragione_sociale", edicola_ragione_sociale);
		reportParams.put("edicola_indirizzo", edicola_indirizzo);
		
//		if (prodottiDigitali != null) {
//
//			reportParams.put("TitoloDocumento", " ATTIVAZIONE PRODOTTI DIGITALI");
//			reportParams.put("edicola_codice", edicola_codice);
//			reportParams.put("edicola_ragione_sociale", edicola_ragione_sociale);
//			reportParams.put("edicola_indirizzo", edicola_indirizzo);

			// if(risposta.getEsito().equals("OK")){
			// serialNumber = risposta.getSerialNumber();
			// pin = risposta.getPin();
			// valore = risposta.getValore().toString();
			// nomeProdotto = risposta.getNomeProdotto();
			// istruzioniAttivazione = risposta.getIstruzioniAttivazione();
			// scadenzaAttivazione = new
			// SimpleDateFormat("dd/MM/yyyy").format(risposta.getDtScadenzaAttivazione());
			// }else{
			// serialNumber = "";
			// pin = "";
			// valore = "";
			// nomeProdotto = "";
			// istruzioniAttivazione =
			// risposta.getErrCodice()+"\n"+risposta.getErrDescrizione();
			// scadenzaAttivazione = "";
			// }

//		}

		// reportParams.put("TitoloDocumento", " ATTIVAZIONE PRODOTTI
		// DIGITALI");
		// reportParams.put("edicola_codice", edicola_codice);
		// reportParams.put("edicola_ragione_sociale", edicola_ragione_sociale);
		// reportParams.put("edicola_indirizzo", edicola_indirizzo);
		// reportParams.put("idRichiesta", idRichiesta);
		// reportParams.put("serialNumber", serialNumber);
		// reportParams.put("pin", pin);
		// reportParams.put("valore",valore);
		// reportParams.put("nomeProdotto",nomeProdotto);
		// reportParams.put("istruzioniAttivazione", istruzioniAttivazione);
		// reportParams.put("scadenzaAttivazione", scadenzaAttivazione);

		fileName = "AttivazioneProdottoDigitali";
		setCookie();
		return IGerivConstants.ACTION_ATTIVAZIONE_PRODOTTI_DIGITALI_GIFT_CARD_EPIPOLI;
	}

	/**
	 * Produce il report pdf dell'estratto conto del cliente edicola e salva i
	 * dati del report in formato .xml
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String emettiEstrattoContoClientiEdicola() throws Exception {
		if (!Strings.isNullOrEmpty(this.strDataDocumento) && !Strings.isNullOrEmpty(this.tipiEstrattoConto)
				&& !Strings.isNullOrEmpty(this.strDataComp) && this.spunte != null) {
			if (this.strDataDocumento.contains(",")) {
				this.strDataDocumento = this.strDataDocumento.substring(0, this.strDataDocumento.indexOf(",")).trim();
			}
			if (this.strDataComp.contains(",")) {
				this.strDataComp = this.strDataComp.substring(0, this.strDataComp.indexOf(",")).trim();
			}
			if (this.tipiEstrattoConto.contains(",")) {
				this.tipiEstrattoConto = this.tipiEstrattoConto.substring(0, this.tipiEstrattoConto.indexOf(","))
						.trim();
			}
			Timestamp dataDocumento = DateUtilities.parseDate(this.strDataDocumento, DateUtilities.FORMATO_DATA_SLASH);
			Integer[] arrCodEdicola = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv())
					? getAuthUser().getArrId() : new Integer[] { getAuthUser().getId() };
			Timestamp dataCompetenza = DateUtilities.parseDate(this.strDataComp, DateUtilities.FORMATO_DATA_SLASH);
			List<Integer> tipiEstrattoConto = null;
			if (!Strings.isNullOrEmpty(this.tipiEstrattoConto)) {
				tipiEstrattoConto = new ArrayList<Integer>();
				String[] split = this.tipiEstrattoConto.split("\\|");
				for (String tec : split) {
					String tipoEC = tec.trim();
					if (!Strings.isNullOrEmpty(tipoEC)) {
						tipiEstrattoConto.add(new Integer(tipoEC));
					}
				}
			}
			List<Long> listClienti = new ArrayList<Long>();
			if (!Strings.isNullOrEmpty(this.spunte)) {
				String[] split = this.spunte.split(",");
				for (String tec : split) {
					String codCli = tec.trim();
					if (!Strings.isNullOrEmpty(codCli)) {
						listClienti.add(new Long(codCli));
					}
				}
			}
			final Timestamp now = clientiService.getSysdate();
			File outDir = new File(pathEstrattiContoClienti + System.getProperty("file.separator") + "xml"
					+ System.getProperty("file.separator") + getAuthUser().getCodFiegDlMaster()
					+ System.getProperty("file.separator") + getAuthUser().getCodEdicolaMaster());
			if (!outDir.isDirectory()) {
				outDir.mkdirs();
			}
			tipoProdotti = IGerivConstants.TIPO_PRODOTTO_TUTTI;
			dataSourceList = clientiService.getListEstrattoContoClienti(arrCodEdicola, listClienti, dataCompetenza,
					tipiEstrattoConto, tipoProdotti);
			if (tipoProdottiInEstrattoConto != null
					&& tipoProdottiInEstrattoConto.equals(IGerivConstants.TIPO_PRODOTTO_MISTO)) {
				return emettiFattura();
			}
			Timestamp ultimaDataCompetenza = clientiService.getUltimaDataCompetenza(arrCodEdicola, listClienti,
					dataCompetenza, tipiEstrattoConto);
			String ultDtComp = null;
			if (ultimaDataCompetenza != null) {
				ultimaDataCompetenza = new Timestamp(DateUtilities.aggiungiGiorni(ultimaDataCompetenza, 1).getTime());
				ultDtComp = DateUtilities.getTimestampAsString(ultimaDataCompetenza, DateUtilities.FORMATO_DATA_SLASH);
			}
			Map<Long, BigDecimal> mapClienteImporto = new HashMap<Long, BigDecimal>();
			Map<Long, Set<Timestamp>> mapClienteDateCompetezaEc = new HashMap<Long, Set<Timestamp>>();
			Map<Long, byte[]> mapClienteEstrattContoXml = new HashMap<Long, byte[]>();
			Map<Long, Integer> mapClienteNumeroEC = new HashMap<Long, Integer>();
			Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap
					.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
			ParametriEdicolaDto paramMarcaBollo = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
					+ IGerivConstants.COD_PARAMETRO_VALORE_ESTRATTO_CONTO_MARCA_BOLLO)
							? mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
									+ IGerivConstants.COD_PARAMETRO_VALORE_ESTRATTO_CONTO_MARCA_BOLLO)
							: null;
			BigDecimal maxEcPerMarcaBollo = new BigDecimal(
					paramMarcaBollo != null && !Strings.isNullOrEmpty(paramMarcaBollo.getParamValue())
							&& !new BigDecimal(paramMarcaBollo.getParamValue()).equals(new BigDecimal(0))
									? paramMarcaBollo.getParamValue() : "0");
			String logoFileName = mapParam
					.containsKey(
							IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
									+ IGerivConstants.COD_PARAMETRO_LOGOMARCA_EDICOLA)
											? mapParam
													.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
															+ IGerivConstants.COD_PARAMETRO_LOGOMARCA_EDICOLA)
													.getParamValue()
											: "";
			fileName = MessageFormat.format(IGerivMessageBundle.get("igeriv.estratto_conto_clienti.file.name"),
					new Object[] { DateUtilities.getTimestampAsString(dataCompetenza, DateUtilities.FORMATO_DATA) });
			reportParams.put("logoFileName", Strings.isNullOrEmpty(logoFileName.trim()) ? null
					: imgMiniatureEdicolaPathDir + System.getProperty("file.separator") + logoFileName);
			reportParams.put("datiEdicola",
					((getAuthUser().getRagioneSocialeEdicola() != null)
							? getAuthUser().getRagioneSocialeEdicola().replaceAll("&nbsp;", " ") : "")
							+ "    "
							+ ((getAuthUser().getIndirizzoEdicolaPrimaRiga() != null)
									? getAuthUser().getIndirizzoEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
							+ " - "
							+ ((getAuthUser().getLocalitaEdicolaPrimaRiga() != null)
									? getAuthUser().getLocalitaEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
							+ "            "
							+ (getAuthUser().getPiva() != null ? getText("dpe.partita.iva")
									: getText("dpe.codice.fiscale.2"))
							+ ": "
							+ ((getAuthUser().getPiva() != null) ? getAuthUser().getPiva().replaceAll("&nbsp;", " ")
									: getAuthUser().getCodiceFiscale()));
			reportParams.put("datiEdicolaInt",
					((getAuthUser().getRagioneSocialeEdicola() != null)
							? getAuthUser().getRagioneSocialeEdicola().replaceAll("&nbsp;", " ") : "")
							+ "\n"
							+ ((getAuthUser().getIndirizzoEdicolaPrimaRiga() != null)
									? getAuthUser().getIndirizzoEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
							+ " - "
							+ ((getAuthUser().getLocalitaEdicolaPrimaRiga() != null)
									? getAuthUser().getLocalitaEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
							+ "\n"
							+ (getAuthUser().getPiva() != null ? getText("dpe.partita.iva")
									: getText("dpe.codice.fiscale.2"))
							+ ": "
							+ ((getAuthUser().getPiva() != null) ? getAuthUser().getPiva().replaceAll("&nbsp;", " ")
									: getAuthUser().getCodiceFiscale()));
			reportParams
					.put("title",
							getText("igeriv.periodo.riferimento") + " "
									+ ((ultDtComp == null)
											? (getText("igeriv.fino.a").toLowerCase() + " " + strDataComp)
											: MessageFormat.format(getText("igeriv.dal.al"), ultDtComp, strDataComp)));
			reportParams.put("title1", getText("igeriv.data.documento") + ": " + this.strDataDocumento);
			reportParams.put("titoloLabel", getText("label.print.Table.Title") + "/" + getText("igeriv.prodotto"));
			reportParams.put("prezzoLabel", getText("label.print.Table.Price"));
			reportParams.put("copieLabel", getText("igeriv.quantita.normal"));
			reportParams.put("importoLabel", getText("igeriv.importo"));
			reportParams.put("totaleLabel", getText("igeriv.pne.report.magazzino.importo"));
			reportParams.put("importoScontatoLabel",
					getText("igeriv.pne.report.magazzino.importo") + " " + getText("column.calc.total"));
			reportParams.put("ggDataDocLabel", getText("igeriv.giorni.data.documento"));
			reportParams.put("dataScadenzaPagamentoLabel", getText("igeriv.scadenza"));
			reportParams.put("controllareDatiLabel", getText("igeriv.msg.controllare.dati.fiscali"));
			reportParams.put("scontoLabel", getText("igeriv.sconto.pubb"));
			reportParams.put("scontoPneLabel", getText("igeriv.sconto.prodotti"));
			reportParams.put("pagamentoLabel", getText("igeriv.pagamento"));
			reportParams.put("edicola",
					((getAuthUser().getRagioneSocialeEdicola() != null)
							? getAuthUser().getRagioneSocialeEdicola().replaceAll("&nbsp;", " ") : "")
							+ "    "
							+ ((getAuthUser().getIndirizzoEdicolaPrimaRiga() != null)
									? getAuthUser().getIndirizzoEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
							+ " - "
							+ ((getAuthUser().getLocalitaEdicolaPrimaRiga() != null)
									? getAuthUser().getLocalitaEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
							+ "            "
							+ (getAuthUser().getPiva() != null ? getText("dpe.partita.iva")
									: getText("dpe.codice.fiscale.2"))
							+ ": "
							+ ((getAuthUser().getPiva() != null) ? getAuthUser().getPiva().replaceAll("&nbsp;", " ")
									: getAuthUser().getCodiceFiscale()));
			reportParams.put("intestazioneCliente", getText("igeriv.spett"));
			reportParams.put("dicituraEsenteArt74", getText("igeriv.dicitura.esente.art.74"));
			reportParams.put("pieDiPagina", (sessionMap.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
					+ IGerivConstants.COD_PARAMETRO_TESTO_FONDO_RICEVUTE) != null)
							? ((ParametriEdicolaDto) sessionMap.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
									+ IGerivConstants.COD_PARAMETRO_TESTO_FONDO_RICEVUTE)).getValue().replaceAll("<br>",
											"\\\n")
							: "");
			reportParams.put("valoreMarcaBollo", valoreMarcaBollo);
			reportParams.put("marcaBolloLabel", getText("igeriv.marca.bollo"));
			reportParams.put("numeroEstrattoContoLabel", getText("igeriv.numero.documento"));
			reportParams.put("fileName", fileName);
			Group<EstrattoContoClienti> group = group(((List<EstrattoContoClienti>) dataSourceList),
					by(on(EstrattoContoClienti.class).getCodCliente()));
			dataSourceList = new ArrayList<EstrattoContoClienti>();
			BigDecimal valMarcaBollo = Strings.isNullOrEmpty(valoreMarcaBollo) ? new BigDecimal(0)
					: new BigDecimal(valoreMarcaBollo.replace(",", "."));
			for (Group<EstrattoContoClienti> subgroup : group.subgroups()) {
				List<EstrattoContoClienti> subGroupList = subgroup.findAll();
				List<EstrattoContoClienti> groupedSubList = groupListEstrattoContoClienti(subGroupList);
				BigDecimal totale = sum(groupedSubList, on(EstrattoContoClienti.class).getImporto())
						.add(sum(groupedSubList, on(EstrattoContoClienti.class).getImportoLordoPne()));
				boolean hasMarcaBollo = totale.compareTo(maxEcPerMarcaBollo) == 1;
				if (maxEcPerMarcaBollo.intValue() > 0 && hasMarcaBollo) {
					totale = totale.add(valMarcaBollo);
					forEach(groupedSubList, EstrattoContoClienti.class).setMarcaBollo(valMarcaBollo);
				}
				Long codCliente = groupedSubList.get(0).getCodCliente();
				Integer scontoCliente = (sconto != null && sconto.get(codCliente) != null
						&& sconto.get(codCliente) >= 0) ? sconto.get(codCliente) : scontoPerc;
				Integer scontoPneCliente = (scontoPne != null && scontoPne.get(codCliente) != null
						&& scontoPne.get(codCliente) >= 0) ? scontoPne.get(codCliente) : scontoPercPne;
				Integer numEC = getNumeroEstrattoConto();
				forEach(groupedSubList, EstrattoContoClienti.class).setSconto(scontoCliente);
				forEach(groupedSubList, EstrattoContoClienti.class).setScontoPne(scontoPneCliente);
				forEach(groupedSubList, EstrattoContoClienti.class).setNumeroEstrattoConto(numEC);
				forEach(groupedSubList, EstrattoContoClienti.class).setDataDocumento(dataDocumento);
				((List<EstrattoContoClienti>) dataSourceList).addAll(groupedSubList);
				String fileName1 = MessageFormat.format(
						IGerivMessageBundle.get("igeriv.estratto_conto_cliente.file.name"),
						new Object[] { codCliente.toString(),
								DateUtilities.getTimestampAsString(dataCompetenza, DateUtilities.FORMATO_DATA) })
						+ ".xml";
				File outFile = new File(outDir, fileName1);
				reportParams.put("fileName", fileName1);
				exportXmlDatiEstrattoConto((List<EstrattoContoClienti>) groupedSubList, reportParams, outFile, now,
						scontoCliente);
				mapClienteImporto.put(codCliente, totale);
				mapClienteDateCompetezaEc.put(codCliente, new HashSet(
						extract(subGroupList, on(EstrattoContoClienti.class).getDataCompetenzaEstrattoContoClienti())));
				mapClienteEstrattContoXml.put(codCliente, FileUtils.getBytesFromFile(outFile));
				mapClienteNumeroEC.put(codCliente, numEC);
			}
			if (!mapClienteImporto.isEmpty()) {
				clientiService.saveDataEstrattoAndPagamento(dataDocumento, dataCompetenza, tipoProdotti,
						mapClienteImporto, mapClienteDateCompetezaEc, mapClienteEstrattContoXml, mapClienteNumeroEC,
						IGerivConstants.ESTRATTO_CONTO);
			}
		}
		setCookie();
		return IGerivConstants.ACTION_ESTRATTO_CONTO_CLIENTI_EDICOLA_PDF;
	}

	/**
	 * Crea il report pdf dell'inventario
	 * 
	 * @throws IOException
	 */
	public String reportInventario() throws IOException {
		if (idInventario != null) {
			InventarioVo inventarioVo = inventarioService.getInventarioVo(getAuthUser().getCodEdicolaMaster(),
					idInventario);
			List<InventarioDettaglioVo> dettagli = inventarioVo.getDettagli();
			dettagli = sort(dettagli, on(InventarioDettaglioVo.class).getSortCriteria());
			dataSourceList = dettagli;
			Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap
					.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
			String logoFileName = mapParam
					.containsKey(
							IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
									+ IGerivConstants.COD_PARAMETRO_LOGOMARCA_EDICOLA)
											? mapParam
													.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA
															+ IGerivConstants.COD_PARAMETRO_LOGOMARCA_EDICOLA)
													.getParamValue()
											: "";
			String dataInventario = DateUtilities.getTimestampAsStringExport(inventarioVo.getDataInventario(),
					DateUtilities.FORMATO_DATA_SLASH);
			String dataInventario1 = DateUtilities.getTimestampAsStringExport(inventarioVo.getDataInventario(),
					DateUtilities.FORMATO_DATA);
			reportParams.put("logoFileName", Strings.isNullOrEmpty(logoFileName.trim()) ? null
					: imgMiniatureEdicolaPathDir + System.getProperty("file.separator") + logoFileName);
			reportParams.put("titoloReport", MessageFormat.format(getText("igeriv.inventario.del"), dataInventario));
			String titolo = getText("label.print.Table.Title");
			String sottotitolo = getText("label.print.Table.Subtitle");
			String prezzoNetto = getText("igeriv.prezzo.netto.short");
			String prezzoLordo = getText("igeriv.prezzo.lordo.short");
			String copie = getText("igeriv.copie.lavorazione.resa");
			String numero = getText("igeriv.numero");
			String dataUscita = getText("igeriv.data.uscita");
			String totale = getText("igeriv.importo");
			String totalePubb = getText("column.calc.total.pubblicazioni");
			String totaleCD = getText("column.calc.total.pubblicazioni.in.CD");
			String totaleScadute = getText("column.calc.total.pubblicazioni.scadute");
			String pubblicazioniInCD = getText("igeriv.pubblicazioni.conto.deposito");
			String pubblicazioniScadute = getText("igeriv.pubblicazioni.scadute");
			String totaleGenerale = getText("column.calc.grand.total");
			reportParams.put("titolo", titolo);
			reportParams.put("sottotitolo", sottotitolo);
			reportParams.put("prezzo", prezzoNetto);
			reportParams.put("prezzoLordo", prezzoLordo);
			reportParams.put("copie", copie);
			reportParams.put("numeroCopertina", numero);
			reportParams.put("dataUscitaTitle", dataUscita);
			reportParams.put("totale", totale);
			reportParams.put("totalePub", totalePubb);
			reportParams.put("totaleCD", totaleCD);
			reportParams.put("totaleScadute", totaleScadute);
			reportParams.put("pubblicazioniInCD", pubblicazioniInCD);
			reportParams.put("pubblicazioniScadute", pubblicazioniScadute);
			reportParams.put("totaleGenerale", totaleGenerale);
			reportParams.put("datiEdicola",
					((getAuthUser().getRagioneSocialeEdicola() != null)
							? getAuthUser().getRagioneSocialeEdicola().replaceAll("&nbsp;", " ") : "")
							+ "    "
							+ ((getAuthUser().getIndirizzoEdicolaPrimaRiga() != null)
									? getAuthUser().getIndirizzoEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
							+ " - "
							+ ((getAuthUser().getLocalitaEdicolaPrimaRiga() != null)
									? getAuthUser().getLocalitaEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
							+ "            "
							+ (getAuthUser().getPiva() != null ? getText("dpe.partita.iva")
									: getText("dpe.codice.fiscale.2"))
							+ ": "
							+ ((getAuthUser().getPiva() != null) ? getAuthUser().getPiva().replaceAll("&nbsp;", " ")
									: getAuthUser().getCodiceFiscale()));
			reportParams.put("datiEdicolaInt",
					((getAuthUser().getRagioneSocialeEdicola() != null)
							? getAuthUser().getRagioneSocialeEdicola().replaceAll("&nbsp;", " ") : "")
							+ "\n"
							+ ((getAuthUser().getIndirizzoEdicolaPrimaRiga() != null)
									? getAuthUser().getIndirizzoEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
							+ " - "
							+ ((getAuthUser().getLocalitaEdicolaPrimaRiga() != null)
									? getAuthUser().getLocalitaEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "")
							+ "\n"
							+ (getAuthUser().getPiva() != null ? getText("dpe.partita.iva")
									: getText("dpe.codice.fiscale.2"))
							+ ": "
							+ ((getAuthUser().getPiva() != null) ? getAuthUser().getPiva().replaceAll("&nbsp;", " ")
									: getAuthUser().getCodiceFiscale()));
			fileName = MessageFormat.format(getText("igeriv.report.inventario.file.name"), DateUtilities
					.getTimestampAsStringExport(inventarioVo.getDataInventario(), DateUtilities.FORMATO_DATA));
			setCookie();
			if (tipo.equals(IGerivConstants.PDF)) {
				return IGerivConstants.ACTION_REPORT_INVENTARIO_PDF;
			} else {
				buildExcelResult(dettagli, dataInventario, dataInventario1, titolo, sottotitolo, prezzoNetto,
						prezzoLordo, copie, numero, dataUscita, totalePubb, totaleCD, pubblicazioniInCD,
						totaleGenerale);
				return IGerivConstants.ACTION_REPORT_INVENTARIO_XLS;
			}
		}
		return null;
	}

	/**
	 * Costruisce l'excel da esportare
	 * 
	 * @param dettagli
	 * @param dataInventario
	 * @param dataInventario1
	 * @param titolo
	 * @param sottotitolo
	 * @param prezzoNetto
	 * @param prezzoLordo
	 * @param copie
	 * @param numero
	 * @param dataUscita
	 * @param totalePubb
	 * @param totaleCD
	 * @param pubblicazioniInCD
	 * @param totaleGenerale
	 * @throws IOException
	 */
	private void buildExcelResult(List<InventarioDettaglioVo> dettagli, String dataInventario, String dataInventario1,
			String titolo, String sottotitolo, String prezzoNetto, String prezzoLordo, String copie, String numero,
			String dataUscita, String totalePubb, String totaleCD, String pubblicazioniInCD, String totaleGenerale)
			throws IOException {
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet(MessageFormat.format(getText("igeriv.inventario.del"), dataInventario1));

		HSSFCellStyle styleBold = hwb.createCellStyle();
		HSSFFont fontBold = hwb.createFont();
		fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleBold.setFont(fontBold);

		HSSFRow titleRow = sheet.createRow(0);
		titleRow.createCell((short) 0)
				.setCellValue(MessageFormat.format(getText("igeriv.inventario.del"), dataInventario));
		sheet.addMergedRegion(new Region(0, (short) 1, 0, (short) 6));

		HSSFCellStyle dateStyle = hwb.createCellStyle();
		dateStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

		HSSFCellStyle intgerStyle = hwb.createCellStyle();
		intgerStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		HSSFDataFormat format = hwb.createDataFormat();
		intgerStyle.setDataFormat(format.getFormat("###,##0"));

		HSSFCellStyle numericStyle = hwb.createCellStyle();
		numericStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		numericStyle.setDataFormat(format.getFormat("###,##0.00"));

		HSSFCellStyle numericStyleBold = hwb.createCellStyle();
		numericStyleBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		numericStyleBold.setDataFormat(format.getFormat("###,##0.00"));
		numericStyleBold.setFont(fontBold);

		HSSFRow row = sheet.createRow(2);
		row.createCell((short) 0).setCellValue(titolo);
		row.createCell((short) 1).setCellValue(sottotitolo);
		row.createCell((short) 2).setCellValue(dataUscita);
		row.createCell((short) 3).setCellValue(numero);
		row.createCell((short) 4).setCellValue(prezzoNetto);
		row.createCell((short) 5).setCellValue(prezzoLordo);
		row.createCell((short) 6).setCellValue(copie);

		List<InventarioDettaglioVo> listNonInCD = select(dettagli,
				having(on(InventarioDettaglioVo.class).getIsContoDeposito(), equalTo(false)));
		listNonInCD = select(listNonInCD, having(on(InventarioDettaglioVo.class).getIsScaduta(), equalTo(false)));

		List<InventarioDettaglioVo> listNonInCDScadute = select(dettagli,
				having(on(InventarioDettaglioVo.class).getIsContoDeposito(), equalTo(false)));
		listNonInCDScadute = select(listNonInCDScadute,
				having(on(InventarioDettaglioVo.class).getIsScaduta(), equalTo(true)));

		List<InventarioDettaglioVo> listInCD = select(dettagli,
				having(on(InventarioDettaglioVo.class).getIsContoDeposito(), equalTo(true)));
		listInCD = select(listInCD, having(on(InventarioDettaglioVo.class).getIsScaduta(), equalTo(false)));

		List<InventarioDettaglioVo> listInCDScadute = select(dettagli,
				having(on(InventarioDettaglioVo.class).getIsContoDeposito(), equalTo(true)));
		listInCDScadute = select(listInCDScadute,
				having(on(InventarioDettaglioVo.class).getIsScaduta(), equalTo(true)));

		// Pubblicazioni
		int count = 4;
		for (InventarioDettaglioVo det : listNonInCD) {
			short cellCount = 0;
			HSSFRow detRow = sheet.createRow(count++);
			HSSFCell titoloCell = detRow.createCell(cellCount);
			titoloCell.setCellValue(det.getTitolo());
			HSSFCell sottotitoloCell = detRow.createCell(++cellCount);
			sottotitoloCell.setCellValue(det.getSottotitolo());
			HSSFCell dateCell = detRow.createCell(++cellCount);
			dateCell.setCellStyle(dateStyle);
			dateCell.setCellValue(det.getDataUscita());
			HSSFCell numCell = detRow.createCell(++cellCount);
			numCell.setCellStyle(numericStyle);
			numCell.setCellValue(det.getNumeroCopertina());
			HSSFCell prezzoNettoCell = detRow.createCell(++cellCount);
			prezzoNettoCell.setCellStyle(numericStyle);
			prezzoNettoCell.setCellValue(det.getPrezzoEdicola().doubleValue());
			HSSFCell prezzoLordoCell = detRow.createCell(++cellCount);
			prezzoLordoCell.setCellStyle(numericStyle);
			prezzoLordoCell.setCellValue(det.getPrezzoCopertina().doubleValue());
			HSSFCell qtaCell = detRow.createCell(++cellCount);
			qtaCell.setCellStyle(intgerStyle);
			qtaCell.setCellValue(det.getQuantita());
		}

		int rowIndex = ++count;
		HSSFRow totalRowNonInCD = sheet.createRow(rowIndex);
		HSSFCell totaleNonInCDTitleCell = totalRowNonInCD.createCell((short) 0);
		totaleNonInCDTitleCell.setCellStyle(styleBold);
		totaleNonInCDTitleCell.setCellValue(totalePubb);

		sheet.addMergedRegion(new Region(rowIndex, (short) 1, rowIndex, (short) 3));
		HSSFCell impNettoCell = totalRowNonInCD.createCell((short) 4);
		impNettoCell.setCellStyle(numericStyleBold);
		BigDecimal totaleNettoNonInCD = new BigDecimal(0);
		if (!listNonInCD.isEmpty()) {
			for (InventarioDettaglioVo dto : listNonInCD) {
				totaleNettoNonInCD = totaleNettoNonInCD.add(dto.getImporto());
			}
		}
		impNettoCell.setCellValue(totaleNettoNonInCD.doubleValue());

		HSSFCell impLordoCell = totalRowNonInCD.createCell((short) 5);
		impLordoCell.setCellStyle(numericStyleBold);
		BigDecimal totaleLordoNonInCD = new BigDecimal(0);
		if (!listNonInCD.isEmpty()) {
			for (InventarioDettaglioVo dto : listNonInCD) {
				totaleLordoNonInCD = totaleLordoNonInCD.add(dto.getImportoLordo());
			}
		}
		impLordoCell.setCellValue(totaleLordoNonInCD.doubleValue());

		// Pubblicazioni scadute
		int titleRowPubNoCDScaduteIndex = rowIndex + 2;
		HSSFRow titleRowPubNoCDScadute = sheet.createRow(titleRowPubNoCDScaduteIndex);
		titleRowPubNoCDScadute.createCell((short) 0).setCellValue("Pubblicazioni Scadute");
		sheet.addMergedRegion(
				new Region(titleRowPubNoCDScaduteIndex, (short) 1, titleRowPubNoCDScaduteIndex, (short) 6));

		count = titleRowPubNoCDScaduteIndex + 2;
		for (InventarioDettaglioVo det : listNonInCDScadute) {
			short cellCount = 0;
			HSSFRow detRow = sheet.createRow(count++);
			HSSFCell titoloCell = detRow.createCell(cellCount);
			titoloCell.setCellValue(det.getTitolo());
			HSSFCell sottotitoloCell = detRow.createCell(++cellCount);
			sottotitoloCell.setCellValue(det.getSottotitolo());
			HSSFCell dateCell = detRow.createCell(++cellCount);
			dateCell.setCellStyle(dateStyle);
			dateCell.setCellValue(det.getDataUscita());
			HSSFCell numCell = detRow.createCell(++cellCount);
			numCell.setCellStyle(numericStyle);
			numCell.setCellValue(det.getNumeroCopertina());
			HSSFCell prezzoNettoCell = detRow.createCell(++cellCount);
			prezzoNettoCell.setCellStyle(numericStyle);
			prezzoNettoCell.setCellValue(det.getPrezzoEdicola().doubleValue());
			HSSFCell prezzoLordoCell = detRow.createCell(++cellCount);
			prezzoLordoCell.setCellStyle(numericStyle);
			prezzoLordoCell.setCellValue(det.getPrezzoCopertina().doubleValue());
			HSSFCell qtaCell = detRow.createCell(++cellCount);
			qtaCell.setCellStyle(intgerStyle);
			qtaCell.setCellValue(det.getQuantita());
		}

		int rowIndexTotaleNonInCDScadute = ++count;
		HSSFRow totalRowNonInCDScadute = sheet.createRow(rowIndexTotaleNonInCDScadute);
		HSSFCell totaleNonInCDScaduteTitleCell = totalRowNonInCDScadute.createCell((short) 0);
		totaleNonInCDScaduteTitleCell.setCellStyle(styleBold);
		totaleNonInCDScaduteTitleCell.setCellValue(totalePubb);

		sheet.addMergedRegion(
				new Region(rowIndexTotaleNonInCDScadute, (short) 1, rowIndexTotaleNonInCDScadute, (short) 3));
		HSSFCell impNettoNonInCDScaduteCell = totalRowNonInCDScadute.createCell((short) 4);
		impNettoNonInCDScaduteCell.setCellStyle(numericStyleBold);
		BigDecimal totaleNettoNonInCDScadute = new BigDecimal(0);
		if (!listNonInCDScadute.isEmpty()) {
			for (InventarioDettaglioVo dto : listNonInCDScadute) {
				totaleNettoNonInCDScadute = totaleNettoNonInCDScadute.add(dto.getImporto());
			}
		}
		impNettoNonInCDScaduteCell.setCellValue(totaleNettoNonInCDScadute.doubleValue());

		HSSFCell impLordoNonInCDScaduteCell = totalRowNonInCDScadute.createCell((short) 5);
		impLordoNonInCDScaduteCell.setCellStyle(numericStyleBold);
		BigDecimal totaleLordoNonInCDScadute = new BigDecimal(0);
		if (!listNonInCD.isEmpty()) {
			for (InventarioDettaglioVo dto : listNonInCDScadute) {
				totaleLordoNonInCDScadute = totaleLordoNonInCDScadute.add(dto.getImportoLordo());
			}
		}
		impLordoNonInCDScaduteCell.setCellValue(totaleLordoNonInCDScadute.doubleValue());

		// Pubblicazioni in conto deposito
		int titleRowPubInCDIndex = count + 2;
		HSSFRow titleRowPubInCD = sheet.createRow(titleRowPubInCDIndex);
		titleRowPubInCD.createCell((short) 0).setCellValue(pubblicazioniInCD);
		sheet.addMergedRegion(new Region(titleRowPubInCDIndex, (short) 1, titleRowPubInCDIndex, (short) 6));

		count = titleRowPubInCDIndex + 2;
		for (InventarioDettaglioVo det : listInCD) {
			short cellCount = 0;
			HSSFRow detRow = sheet.createRow(count++);
			HSSFCell titoloCell = detRow.createCell(cellCount);
			titoloCell.setCellValue(det.getTitolo());
			HSSFCell sottotitoloCell = detRow.createCell(++cellCount);
			sottotitoloCell.setCellValue(det.getSottotitolo());
			HSSFCell dateCell = detRow.createCell(++cellCount);
			dateCell.setCellStyle(dateStyle);
			dateCell.setCellValue(det.getDataUscita());
			HSSFCell numCell = detRow.createCell(++cellCount);
			numCell.setCellStyle(numericStyle);
			numCell.setCellValue(det.getNumeroCopertina());
			HSSFCell prezzoNettoCell = detRow.createCell(++cellCount);
			prezzoNettoCell.setCellStyle(numericStyle);
			prezzoNettoCell.setCellValue(det.getPrezzoEdicola().doubleValue());
			HSSFCell prezzoLordoCell = detRow.createCell(++cellCount);
			prezzoLordoCell.setCellStyle(numericStyle);
			prezzoLordoCell.setCellValue(det.getPrezzoCopertina().doubleValue());
			HSSFCell qtaCell = detRow.createCell(++cellCount);
			qtaCell.setCellStyle(intgerStyle);
			qtaCell.setCellValue(det.getQuantita());
		}

		rowIndex = ++count;
		HSSFRow totalRowInCD = sheet.createRow(rowIndex);
		HSSFCell totaleInCDTitleCell = totalRowInCD.createCell((short) 0);
		totaleInCDTitleCell.setCellStyle(styleBold);
		totaleInCDTitleCell.setCellValue(totaleCD);

		sheet.addMergedRegion(new Region(rowIndex, (short) 1, rowIndex, (short) 3));
		HSSFCell impNettoCellInCD = totalRowInCD.createCell((short) 4);
		impNettoCellInCD.setCellStyle(numericStyleBold);
		BigDecimal totaleNettoInCD = new BigDecimal(0);
		if (!listInCD.isEmpty()) {
			for (InventarioDettaglioVo dto : listInCD) {
				totaleNettoInCD = totaleNettoInCD.add(dto.getImporto());
			}
		}
		impNettoCellInCD.setCellValue(totaleNettoInCD.doubleValue());
		HSSFCell impLordoCellInCD = totalRowInCD.createCell((short) 5);
		impLordoCellInCD.setCellStyle(numericStyleBold);
		BigDecimal totaleLordoInCD = new BigDecimal(0);
		if (!listInCD.isEmpty()) {
			for (InventarioDettaglioVo dto : listInCD) {
				totaleLordoInCD = totaleLordoInCD.add(dto.getImportoLordo());
			}
		}
		impLordoCellInCD.setCellValue(totaleLordoInCD.doubleValue());

		// Pubblicazioni in conto deposito Scadute
		int titleRowPubInCDScaduteIndex = rowIndex + 2;
		HSSFRow titleRowPubInCDScadute = sheet.createRow(titleRowPubInCDScaduteIndex);
		titleRowPubInCDScadute.createCell((short) 0).setCellValue("Pubblicazioni in C/D Scadute");
		sheet.addMergedRegion(
				new Region(titleRowPubInCDScaduteIndex, (short) 1, titleRowPubInCDScaduteIndex, (short) 6));

		count = titleRowPubInCDScaduteIndex + 2;
		for (InventarioDettaglioVo det : listInCDScadute) {
			short cellCount = 0;
			HSSFRow detRow = sheet.createRow(count++);
			HSSFCell titoloCell = detRow.createCell(cellCount);
			titoloCell.setCellValue(det.getTitolo());
			HSSFCell sottotitoloCell = detRow.createCell(++cellCount);
			sottotitoloCell.setCellValue(det.getSottotitolo());
			HSSFCell dateCell = detRow.createCell(++cellCount);
			dateCell.setCellStyle(dateStyle);
			dateCell.setCellValue(det.getDataUscita());
			HSSFCell numCell = detRow.createCell(++cellCount);
			numCell.setCellStyle(numericStyle);
			numCell.setCellValue(det.getNumeroCopertina());
			HSSFCell prezzoNettoCell = detRow.createCell(++cellCount);
			prezzoNettoCell.setCellStyle(numericStyle);
			prezzoNettoCell.setCellValue(det.getPrezzoEdicola().doubleValue());
			HSSFCell prezzoLordoCell = detRow.createCell(++cellCount);
			prezzoLordoCell.setCellStyle(numericStyle);
			prezzoLordoCell.setCellValue(det.getPrezzoCopertina().doubleValue());
			HSSFCell qtaCell = detRow.createCell(++cellCount);
			qtaCell.setCellStyle(intgerStyle);
			qtaCell.setCellValue(det.getQuantita());
		}

		rowIndex = ++count;
		HSSFRow totalRowInCDScadute = sheet.createRow(rowIndex);
		HSSFCell totaleInCDScaduteTitleCell = totalRowInCDScadute.createCell((short) 0);
		totaleInCDScaduteTitleCell.setCellStyle(styleBold);
		totaleInCDScaduteTitleCell.setCellValue(totaleCD);

		sheet.addMergedRegion(new Region(rowIndex, (short) 1, rowIndex, (short) 3));
		HSSFCell impNettoCellInCDScadute = totalRowInCDScadute.createCell((short) 4);
		impNettoCellInCDScadute.setCellStyle(numericStyleBold);
		BigDecimal totaleNettoInCDScadute = new BigDecimal(0);
		if (!listInCDScadute.isEmpty()) {
			for (InventarioDettaglioVo dto : listInCDScadute) {
				totaleNettoInCDScadute = totaleNettoInCDScadute.add(dto.getImporto());
			}
		}
		impNettoCellInCDScadute.setCellValue(totaleNettoInCDScadute.doubleValue());

		HSSFCell impLordoCellInCDScadute = totalRowInCDScadute.createCell((short) 5);
		impLordoCellInCDScadute.setCellStyle(numericStyleBold);
		BigDecimal totaleLordoInCDScadute = new BigDecimal(0);
		if (!listInCDScadute.isEmpty()) {
			for (InventarioDettaglioVo dto : listInCDScadute) {
				totaleLordoInCDScadute = totaleLordoInCDScadute.add(dto.getImportoLordo());
			}
		}
		impLordoCellInCDScadute.setCellValue(totaleLordoInCDScadute.doubleValue());

		rowIndex = rowIndex + 2;
		HSSFRow totalRow = sheet.createRow(rowIndex);
		HSSFCell totaleTitleCell = totalRow.createCell((short) 0);
		totaleTitleCell.setCellStyle(styleBold);
		totaleTitleCell.setCellValue(totaleGenerale);

		sheet.addMergedRegion(new Region(rowIndex, (short) 1, rowIndex, (short) 3));
		HSSFCell impTotalNettoCell = totalRow.createCell((short) 4);
		impTotalNettoCell.setCellStyle(numericStyleBold);
		BigDecimal totaleNetto = totaleNettoInCD.add(totaleNettoNonInCD).add(totaleNettoInCDScadute)
				.add(totaleNettoNonInCDScadute);
		impTotalNettoCell.setCellValue(totaleNetto.doubleValue());

		HSSFCell impTotalLordoCell = totalRow.createCell((short) 5);
		impTotalLordoCell.setCellStyle(numericStyleBold);
		BigDecimal totaleLordo = totaleLordoInCD.add(totaleLordoNonInCD).add(totaleLordoInCDScadute)
				.add(totaleLordoNonInCDScadute);
		impTotalLordoCell.setCellValue(totaleLordo.doubleValue());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		hwb.write(baos);
		excelStream = new ByteArrayInputStream(baos.toByteArray());
	}

	/**
	 * Crea il report degli ordini dei clienti
	 * 
	 * @return
	 * @throws Exception
	 */
	public String esportaOrdiniClientiPdf() throws Exception {
		StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
		String strData = st.nextToken();
		String tipo = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
		reportParams.put("ragSocEdicola", getAuthUser().getRagioneSocialeEdicola());
		reportParams.put("indirizzo",
				getAuthUser().getIndirizzoEdicolaPrimaRiga() + " - " + getAuthUser().getLocalitaEdicolaPrimaRiga());
		reportParams.put("dataBolla", strData);
		reportParams.put("tipo", tipo);
		if (esportaClientiDiTutteLeBolle != null && esportaClientiDiTutteLeBolle) {
			dataSourceList = rifornimentiService.getOrdiniClienti(getAuthUser().getCodFiegDl(), getAuthUser().getId(),
					DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA));
		} else {
			dataSourceList = rifornimentiService.getOrdiniClienti(getAuthUser().getCodFiegDl(), getAuthUser().getId(),
					idtnOrdini);
		}
		setCookie();
		if (Strings.isNullOrEmpty(tipoReportOrdiniClienti) || tipoReportOrdiniClienti.equals("1")) {
			return IGerivConstants.ACTION_ORDINI_CLIENTI_PDF;
		} else {
			dataSourceList = dataSourceList != null ? buildGroupedClientVoList(dataSourceList) : null;
			return IGerivConstants.ACTION_ORDINI_CLIENTI_DETTAGLIO_PDF;
		}
	}

	/**
	 * Crea il report degli ordini dei clienti dall'icona notifica e aggiorna il
	 * flag notifica
	 * 
	 * @return
	 * @throws Exception
	 */
	public String esportaOrdiniClientiNotifichePdf() throws Exception {
		reportParams.put("ragSocEdicola", getAuthUser().getRagioneSocialeEdicola());
		reportParams.put("indirizzo",
				getAuthUser().getIndirizzoEdicolaPrimaRiga() + " - " + getAuthUser().getLocalitaEdicolaPrimaRiga());
		dataSourceList = rifornimentiService.getOrdiniClientiNotifiche(getAuthUser().getArrCodFiegDl(),
				getAuthUser().getArrId());
		List<RichiestaClientePk> pks = extract(dataSourceList, on(RichiestaClienteDto.class).getPk());
		rifornimentiService.updateOrdiniClientiNotifiche(pks);
		setCookie();
		return IGerivConstants.ACTION_ORDINI_CLIENTI_PDF;
	}

	/**
	 * Raggruppa i dati dell'estratto conto per titolo, tipo prodotto, prezzo e
	 * somma le copie
	 * 
	 * @param List<EstrattoContoClienti>
	 *            subGroupList
	 * @return List<EstrattoContoClienti>
	 */
	private List<EstrattoContoClienti> groupListEstrattoContoClienti(List<EstrattoContoClienti> subGroupList) {
		Group<EstrattoContoClienti> group = group(((List<EstrattoContoClienti>) subGroupList),
				by(on(EstrattoContoClienti.class).getSortCriteria()));
		List<EstrattoContoClienti> outList = new ArrayList<EstrattoContoClienti>();
		for (Group<EstrattoContoClienti> subgroup : group.subgroups()) {
			List<EstrattoContoClienti> list = subgroup.findAll();
			EstrattoContoClienti first = list.get(0);
			first.setCopie(sum(list, on(EstrattoContoClienti.class).getCopie()));
			outList.add(first);
		}
		return outList;
	}

	/**
	 * Raggruppa i dati della fattura per titolo, tipo prodotto, prezzo e somma
	 * le copie
	 * 
	 * @param List<VenditeParamDto>
	 *            subGroupList
	 * @return List<VenditeParamDto>
	 */
	private List<VenditeParamDto> groupListVenditeParamDto(List<VenditeParamDto> subGroupList) {
		Group<VenditeParamDto> group = group(((List<VenditeParamDto>) subGroupList),
				by(on(VenditeParamDto.class).getSortCriteria()));
		List<VenditeParamDto> outList = new ArrayList<VenditeParamDto>();
		for (Group<VenditeParamDto> subgroup : group.subgroups()) {
			List<VenditeParamDto> list = subgroup.findAll();
			VenditeParamDto first = list.get(0);
			first.setCopie(sum(list, on(VenditeParamDto.class).getCopie()));
			outList.add(first);
		}
		return outList;
	}

	/**
	 * Emette le fatture dei clienti selezionati partendo dall'estratto conto
	 * 
	 * @return String
	 * @throws Exception
	 */
	private String emettiFattura() throws Exception {
		List<EstrattoContoClienti> list = (List<EstrattoContoClienti>) dataSourceList;
		vendite = new ArrayList<VenditeParamDto>();
		for (EstrattoContoClienti ec : list) {
			VenditeParamDto ve = new VenditeParamDto();
			ve.setCodCliente(ec.getCodCliente());
			ve.setCopie(ec.getCopie().intValue());
			ve.setTitolo(ec.getTitolo());
			ve.setImporto("" + ec.getPrezzo().floatValue());
			ve.setAliquota(new BigDecimal(ec.getAliquota()));
			ve.setProdottoNonEditoriale(ec instanceof EstrattoContoClientiProdottiDto);
			ve.setDatiBancari(ec.getDatiBancari());
			ve.setTipoPagamento(ec.getTipoPagamento());
			ve.setDatiCliente(ec.getDatiCliente());
			ve.setDataScadenzaPagamento(ec.getDataScadenzaPagamento());
			ve.setDataCompetenzaEstrattoContoClienti(ec.getDataCompetenzaEstrattoContoClienti());
			ve.setCausaleEsenzione(!Strings.isNullOrEmpty(ec.getDescrizioneCausaleIva())
					? WordUtils.capitalizeFully(ec.getDescrizioneCausaleIva()) : "");
			vendite.add(ve);
		}
		return reportVenditePdf();
	}

	/**
	 * Emette lo storno di una fattura
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String emettiStornoFattura() throws Exception {
		if (codCliente != null && numFattura != null) {
			List<EstrattoContoClienti> list = clientiService.getEstrattoContoClienti(getAuthUser().getArrId(),
					codCliente, null, null, numFattura);
			vendite = new ArrayList<VenditeParamDto>();
			for (EstrattoContoClienti ec : list) {
				VenditeParamDto ve = new VenditeParamDto();
				ve.setCodCliente(ec.getCodCliente());
				ve.setCopie(ec.getCopie().intValue());
				ve.setTitolo(ec.getTitolo());
				ve.setImporto("" + ec.getPrezzo().floatValue());
				ve.setAliquota(new BigDecimal(ec.getAliquota()));
				ve.setProdottoNonEditoriale(ec instanceof EstrattoContoClientiProdottiDto);
				ve.setDatiBancari(ec.getDatiBancari());
				ve.setTipoPagamento(ec.getTipoPagamento());
				ve.setDatiCliente(ec.getDatiCliente());
				ve.setGiorniScadenzaPagamento(ec.getGiorniScadenzaPagamento());
				ve.setDataCompetenzaEstrattoContoClienti(ec.getDataCompetenzaEstrattoContoClienti());
				vendite.add(ve);
			}
			return reportVenditePdf();
		}
		return null;

	}

	/**
	 * Genera il file xml dalla lista di Dto.
	 *
	 * @param List<EstrattoContoClienti>
	 *            listConsegne
	 * @param Map<String,
	 *            String> reportParams
	 * @param File
	 *            outFile
	 * @param now
	 * @param scontoCliente
	 * @throws JAXBException
	 */
	private void exportXmlDatiEstrattoConto(List<EstrattoContoClienti> listConsegne, Map reportParams, File outFile,
			Timestamp now, Integer scontoCliente) throws JAXBException {
		DateFormat dfTime = new SimpleDateFormat(DateUtilities.FORMATO_ORA_HHMMSS);
		EstrattoContoClientiXmlDto listWrapper = new EstrattoContoClientiXmlDto();
		listWrapper.setListConsegnePubblicazioni(selectPubblicazioni(listConsegne));
		listWrapper.setListConsegneProdotti(selectProdotti(listConsegne));
		EstrattoContoHeaderFooterXmlDto header = new EstrattoContoHeaderFooterXmlDto();
		header.setDataCreazioneFile(now);
		header.setOraCreazioneFile(dfTime.format(now));
		header.setControllareDatiLabel(reportParams.get("controllareDatiLabel").toString());
		header.setCopieLabel(reportParams.get("copieLabel").toString());
		header.setEdicola(reportParams.get("edicola").toString());
		header.setDatiEdicola(reportParams.get("datiEdicola").toString());
		header.setDatiEdicolaInt(reportParams.get("datiEdicolaInt").toString());
		header.setImportoLabel(reportParams.get("importoLabel").toString());
		header.setIntestazioneCliente(reportParams.get("intestazioneCliente").toString());
		header.setLogoFileName(
				reportParams.get("logoFileName") == null ? null : reportParams.get("logoFileName").toString());
		header.setPagamentoLabel(reportParams.get("pagamentoLabel").toString());
		header.setPieDiPagina(reportParams.get("pieDiPagina").toString().replaceAll("\\\n", "<br>"));
		header.setPrezzoLabel(reportParams.get("prezzoLabel").toString());
		header.setTitle(reportParams.get("title").toString());
		header.setTitle1(reportParams.get("title1").toString());
		header.setTitoloLabel(reportParams.get("titoloLabel").toString());
		header.setTotaleLabel(reportParams.get("totaleLabel").toString());
		header.setGgDataDocLabel(reportParams.get("ggDataDocLabel").toString());
		header.setDataScadenzaPagamentoLabel(reportParams.get("dataScadenzaPagamentoLabel") == null ? null
				: reportParams.get("dataScadenzaPagamentoLabel").toString());
		header.setValoreMarcaBollo(
				reportParams.get("valoreMarcaBollo") == null ? null : reportParams.get("valoreMarcaBollo").toString());
		header.setScontoPerc(scontoCliente);
		header.setImportoScontatoLabel(reportParams.get("importoScontatoLabel").toString());
		header.setScontoLabel(reportParams.get("scontoLabel").toString());
		header.setScontoPneLabel(reportParams.get("scontoPneLabel").toString());
		header.setMarcaBolloLabel(reportParams.get("marcaBolloLabel").toString());
		header.setNumeroEstrattoContoLabel(reportParams.get("numeroEstrattoContoLabel").toString());
		header.setFileName(reportParams.get("fileName").toString());
		listWrapper.setHeader(header);
		XmlUtils.marshall(listWrapper, EstrattoContoClientiXmlDto.class, outFile, true);
	}

	private List<EstrattoContoClientiProdottiDto> selectProdotti(List<EstrattoContoClienti> listConsegne) {
		List<EstrattoContoClientiProdottiDto> list = new ArrayList<EstrattoContoClientiProdottiDto>();
		for (EstrattoContoClienti ec : listConsegne) {
			if (ec instanceof EstrattoContoClientiProdottiDto) {
				list.add((EstrattoContoClientiProdottiDto) ec);
			}
		}
		return list;
	}

	private List<EstrattoContoClientiPubblicazioniDto> selectPubblicazioni(List<EstrattoContoClienti> listConsegne) {
		List<EstrattoContoClientiPubblicazioniDto> list = new ArrayList<EstrattoContoClientiPubblicazioniDto>();
		for (EstrattoContoClienti ec : listConsegne) {
			if (ec instanceof EstrattoContoClientiPubblicazioniDto) {
				list.add((EstrattoContoClientiPubblicazioniDto) ec);
			}
		}
		return list;
	}

	/**
	 * Produce il report pdf dell'estratto conto del cliente edicola andando a
	 * prendere i dati dal file .xml prodotto dal metodo
	 * emettiEstrattoContoClientiEdicola()
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String emettiEstrattoContoSingoloClienteEdicola() throws Exception {
		if (!Strings.isNullOrEmpty(this.dataEstrattoConto) && codCliente != null) {
			String[] split = this.dataEstrattoConto.split("\\|");
			String strDataCompetenza = this.dataEstrattoConto.contains("|") ? split[2] : this.dataEstrattoConto;
			Timestamp dataCompetenza = DateUtilities.parseDate(strDataCompetenza, DateUtilities.FORMATO_DATA_SLASH);
			File xmlFileDir = new File(pathEstrattiContoClienti + System.getProperty("file.separator") + "xml"
					+ System.getProperty("file.separator") + getAuthUser().getCodFiegDlMaster()
					+ System.getProperty("file.separator") + getAuthUser().getCodEdicolaMaster());
			File file = new File(xmlFileDir,
					MessageFormat.format(IGerivMessageBundle.get("igeriv.estratto_conto_cliente.file.name"),
							new Object[] { codCliente.toString(),
									DateUtilities.getTimestampAsString(dataCompetenza, DateUtilities.FORMATO_DATA) })
							+ ".xml");
			if (file != null && file.isFile()) {
				EstrattoContoClientiXmlDto unmarshall = (EstrattoContoClientiXmlDto) XmlUtils
						.unmarshall(EstrattoContoClientiXmlDto.class, file);
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
				reportParams.put("importoScontatoLabel",
						getText("igeriv.pne.report.magazzino.importo") + " " + getText("column.calc.total"));
				reportParams.put("ggDataDocLabel", getText("igeriv.giorni.data.documento"));
				reportParams.put("dataScadenzaPagamentoLabel", getText("igeriv.scadenza"));
				reportParams.put("controllareDatiLabel", header.getControllareDatiLabel());
				reportParams.put("scontoLabel", getText("igeriv.sconto"));
				reportParams.put("pagamentoLabel", header.getPagamentoLabel());
				reportParams.put("intestazioneCliente", header.getIntestazioneCliente());
				reportParams.put("pieDiPagina", header.getPieDiPagina().replaceAll("<br>", "\\\n"));
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

	/**
	 * Ritorna l'ultimo numero di fattura e inserisce il progressivo nella
	 * tabella.
	 * 
	 * @param Integer
	 *            tipoDocumento
	 * @return Long
	 */
	private Integer getNumeroFattura(Integer tipoDocumento) {
		Integer tdoc = IGerivConstants.TIPO_DOCUMENTO_FATTURA_EMESSA;
		ProdottiNonEditorialiProgressiviFatturazioneVo vo = new ProdottiNonEditorialiProgressiviFatturazioneVo();
		ProdottiNonEditorialiProgressiviFatturazionePk pk = new ProdottiNonEditorialiProgressiviFatturazionePk();
		pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
		pk.setTipoDocumento(tdoc);
		Date date = DateUtilities.getDateFirstDayYear();
		pk.setData(new java.sql.Date(date.getTime()));
		vo.setPk(pk);
		Long nextProg = contabilitaService.getNextProgressivoFatturazione(getAuthUser().getCodEdicolaMaster(), tdoc,
				date);
		vo.setProgressivo(nextProg);
		vo.setDataUltMovimentazione(new Timestamp(date.getTime()));
		clientiService.saveBaseVo(vo);
		return nextProg.intValue();
	}

	/**
	 * Ritorna l'ultimo numero di estratto conto e inserisce il progressivo
	 * nella tabella.
	 * 
	 * @param Integer
	 *            tipoDocumento
	 * @return Long
	 */
	private Integer getNumeroEstrattoConto() {
		ProdottiNonEditorialiProgressiviFatturazioneVo vo = new ProdottiNonEditorialiProgressiviFatturazioneVo();
		ProdottiNonEditorialiProgressiviFatturazionePk pk = new ProdottiNonEditorialiProgressiviFatturazionePk();
		pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
		pk.setTipoDocumento(IGerivConstants.TIPO_DOCUMENTO_ESTRATTO_CONTO);
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		pk.setData(new java.sql.Date(cal.getTimeInMillis()));
		vo.setPk(pk);
		Long nextProg = contabilitaService.getNextProgressivoFatturazione(getAuthUser().getCodEdicolaMaster(),
				IGerivConstants.TIPO_DOCUMENTO_ESTRATTO_CONTO, cal.getTime());
		vo.setProgressivo(nextProg);
		vo.setDataUltMovimentazione(new Timestamp(date.getTime()));
		clientiService.saveBaseVo(vo);
		return nextProg.intValue();
	}

	/**
	 * Costrusice una lista di Dto che raggruppa gli ordini dei clienti quattro
	 * alla volta per il report delle etichette ordini clienti.
	 * 
	 * @param List<RichiestaClienteDto>
	 *            list
	 * @return List<RichiestaClienteReportEtichetteDto>
	 */
	private List<?> buildGroupedClientVoList(List<?> list) {
		List<RichiestaClienteReportEtichetteDto> retList = new ArrayList<RichiestaClienteReportEtichetteDto>();
		RichiestaClienteReportEtichetteDto rc = new RichiestaClienteReportEtichetteDto();
		retList.add(rc);
		int i = 0;
		for (RichiestaClienteDto dto : (List<RichiestaClienteDto>) list) {
			if (i == 4) {
				rc = new RichiestaClienteReportEtichetteDto();
				retList.add(rc);
				i = 0;
			}
			if (i == 0) {
				rc.setNome(dto.getNome());
				rc.setCognome(dto.getCognome());
				rc.setNumeroCopertina(dto.getNumeroCopertina());
				rc.setQuantitaRichiesta(dto.getQuantitaRichiesta());
				rc.setSottoTitolo(dto.getTitolo());
				rc.setTitolo(dto.getTitolo());
				rc.setTelefono(dto.getTelefono());
			} else if (i == 1) {
				rc.setNome1(dto.getNome());
				rc.setCognome1(dto.getCognome());
				rc.setNumeroCopertina1(dto.getNumeroCopertina());
				rc.setQuantitaRichiesta1(dto.getQuantitaRichiesta());
				rc.setSottoTitolo1(dto.getTitolo());
				rc.setTitolo1(dto.getTitolo());
				rc.setTelefono1(dto.getTelefono());
			} else if (i == 2) {
				rc.setNome2(dto.getNome());
				rc.setCognome2(dto.getCognome());
				rc.setNumeroCopertina2(dto.getNumeroCopertina());
				rc.setQuantitaRichiesta2(dto.getQuantitaRichiesta());
				rc.setSottoTitolo2(dto.getTitolo());
				rc.setTitolo2(dto.getTitolo());
				rc.setTelefono2(dto.getTelefono());
			} else if (i == 3) {
				rc.setNome3(dto.getNome());
				rc.setCognome3(dto.getCognome());
				rc.setNumeroCopertina3(dto.getNumeroCopertina());
				rc.setQuantitaRichiesta3(dto.getQuantitaRichiesta());
				rc.setSottoTitolo3(dto.getTitolo());
				rc.setTitolo3(dto.getTitolo());
				rc.setTelefono3(dto.getTelefono());
			}
			i++;
		}
		return retList;
	}

	private void setCookie() {
		if (!Strings.isNullOrEmpty(downloadToken)) {
			downloadToken = downloadToken.replaceAll(",", "").trim();
			response.addCookie(new Cookie("fileDownloadToken", request.getParameter("downloadToken").toString()));
		}
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}
}
