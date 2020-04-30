package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.util.ServletContextAware;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.dto.PubblicazionePiuVendutaDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.BarraSceltaRapidaDestraVo;
import it.dpe.igeriv.vo.BarraSceltaRapidaSinistraVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.pk.BarraSceltaRapidaDestraPk;
import it.dpe.igeriv.vo.pk.BarraSceltaRapidaSinistraPk;
import it.dpe.service.mail.MailingListService;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe per la configurazione della barra di scelta rapida delle vendite
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("configAction")
@SuppressWarnings({ "rawtypes" })
public class ConfigAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State, RequestAware, ServletContextAware {
	private static final long serialVersionUID = 1L;
	private final PubblicazioniService pubblicazioniService;
	private final VenditeService venditeService;
	private final IGerivUtils iGerivUtils;
	private final MailingListService mailingListService;
	private final String dpeSupportMailingList;
	private final String imgMiniaturePathDir;
	private final String imgMiniatureEdicolaPathDir;
	private final Integer venditeIconWidth;
	private final Integer venditeIconHeight;
	private final String crumbName = getText("igeriv.barra.scelta.rapida.pubblicazioni");
	private List<PubblicazionePiuVendutaDto> listPubblicazioniPiuVendute;
	private List<PubblicazionePiuVendutaDto> listPubblicazioniBarraSceltaRapidaL;
	private List<PubblicazionePiuVendutaDto> listPubblicazioniBarraSceltaRapidaR;
	private Integer tipoPubblicazione;
	private Integer periodo; 
	private List<KeyValueDto> listTipoPubblicazione;
	private String codiciPubblicazioniL;
	private String coddlL;
	private String nomeImmaginiL; 
	private String tipoImmaginiL;
	private String codiciPubblicazioniR;
	private String coddlR;
	private String nomeImmaginiR; 
	private String tipoImmaginiR;
	private String nomeImmagine; 
	private Integer tipoImmagine;
	private Integer cpu;
	private BarraSceltaRapidaSinistraVo barraSceltaRapidaSinistra;
	private BarraSceltaRapidaDestraVo barraSceltaRapidaDestra;
	private File attachment1;
	private String attachment1ContentType;
	private String attachment1FileName;
	private String returnString;
	
	public ConfigAction() {
		this.pubblicazioniService = null;
		this.venditeService = null;
		this.iGerivUtils = null;
		this.mailingListService = null;
		this.dpeSupportMailingList = null;
		this.imgMiniaturePathDir = null;
		this.imgMiniatureEdicolaPathDir = null;
		this.venditeIconWidth = null;
		this.venditeIconHeight = null;
	}
	
	@Autowired
	public ConfigAction(
			PubblicazioniService pubblicazioniService,
			VenditeService venditeService,
			IGerivUtils iGerivUtils,
			MailingListService mailingListService,
			@Value("${dpe.igeriv.support.mailing.list}") String dpeSupportMailingList,
			@Value("${img.miniature.path.dir}") String imgMiniaturePathDir,
			@Value("${img.miniature.edicola.path.dir}") String imgMiniatureEdicolaPathDir,
			@Value("${vendite.icon.width}") Integer venditeIconWidth,
			@Value("${vendite.icon.height}") Integer venditeIconHeight) {
		this.pubblicazioniService = pubblicazioniService;
		this.venditeService = venditeService;
		this.iGerivUtils = iGerivUtils;
		this.mailingListService = mailingListService;
		this.dpeSupportMailingList = dpeSupportMailingList;
		this.imgMiniaturePathDir = imgMiniaturePathDir;
		this.imgMiniatureEdicolaPathDir = imgMiniatureEdicolaPathDir;
		this.venditeIconWidth = venditeIconWidth;
		this.venditeIconHeight = venditeIconHeight;
	}
	
	@Override
	public String input() throws Exception {
		return INPUT;
	} 

	@Override
	public void validate() {
	}

	@SkipValidation
	public String showFilter() {
		return SUCCESS;
	}

	/**
	 * Ritorna le pubblicazioni della barra di scelta rapida delle vendite
	 * 
	 * @return String
	 */
	@BreadCrumb("%{crumbName}")
	@SkipValidation 
	public String showMenuSceltaRapidaFilter() {
		listTipoPubblicazione = new ArrayList<KeyValueDto>();
		KeyValueDto quotidiano = new KeyValueDto();
		quotidiano.setKey("" + IGerivConstants.TIPO_PUBBLICAZIONE_QUOTIDIANO);
		quotidiano.setValue(getText("igeriv.quotidiano")); 
		KeyValueDto periodico = new KeyValueDto();
		periodico.setKey("" + IGerivConstants.TIPO_PUBBLICAZIONE_PERIODICO);
		periodico.setValue(getText("igeriv.periodico"));
		listTipoPubblicazione.add(quotidiano);
		listTipoPubblicazione.add(periodico);
		sessionMap.put("listTipoPubblicazione", listTipoPubblicazione);
		tipoPubblicazione = (tipoPubblicazione == null) ? IGerivConstants.TIPO_PUBBLICAZIONE_QUOTIDIANO : tipoPubblicazione;
		Integer[] arrCoddl = (getAuthUser().isMultiDl() || getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDlMaster()};
		Integer[] arrCodEdicola = (getAuthUser().isMultiDl() || getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodEdicolaMaster()};
		listPubblicazioniBarraSceltaRapidaL = pubblicazioniService.getListPubblicazioniBarraSceltaRapidaSinistra(arrCoddl, arrCodEdicola, true);
		listPubblicazioniBarraSceltaRapidaR = pubblicazioniService.getListPubblicazioniBarraSceltaRapidaDestra(arrCoddl, arrCodEdicola, true);
		requestMap.put("tableTitle", getText("igeriv.configurazione.barra.scelta.rapida.pubblicazioni"));
		return SUCCESS;
	}
	  
	/**
	 * Ritorna il le pubblicazioni della barra di scelta rapida delle vendite
	 * 
	 * @return String
	 * @throws IOException
	 */
	public String showMenuSceltaRapida() throws IOException {
		if (tipoPubblicazione != null) {
			listPubblicazioniPiuVendute = pubblicazioniService.getListPubblicazioniPiuVendute(getAuthUser().getCodFiegDl(), getAuthUser().getId(), tipoPubblicazione, periodo);
			listPubblicazioniPiuVendute = (tipoPubblicazione.equals(1)) ? buildListQuotidianiPiuVenduti(listPubblicazioniPiuVendute) : listPubblicazioniPiuVendute;
			Integer[] arrCoddl = (getAuthUser().isMultiDl() || getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDlMaster()};
			Integer[] arrCodEdicola = (getAuthUser().isMultiDl() || getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodEdicolaMaster()};
			listPubblicazioniBarraSceltaRapidaL = pubblicazioniService.getListPubblicazioniBarraSceltaRapidaSinistra(arrCoddl, arrCodEdicola, true);
			listPubblicazioniBarraSceltaRapidaR = pubblicazioniService.getListPubblicazioniBarraSceltaRapidaDestra(arrCoddl, arrCodEdicola, true);
			for (PubblicazionePiuVendutaDto ppv : listPubblicazioniBarraSceltaRapidaL) {
				List<PubblicazionePiuVendutaDto> listToRemove = select(listPubblicazioniPiuVendute, having(on(PubblicazionePiuVendutaDto.class).getCodicePubblicazione(), greaterThan(ppv.getCodInizioQuotidiano())).and(having(on(PubblicazionePiuVendutaDto.class).getCodicePubblicazione(), lessThan(ppv.getCodFineQuotidiano()))));
				listPubblicazioniPiuVendute.removeAll(listToRemove);
			}
			for (PubblicazionePiuVendutaDto ppv : listPubblicazioniBarraSceltaRapidaR) {
				List<PubblicazionePiuVendutaDto> listToRemove = select(listPubblicazioniPiuVendute, having(on(PubblicazionePiuVendutaDto.class).getCodicePubblicazione(), greaterThan(ppv.getCodInizioQuotidiano())).and(having(on(PubblicazionePiuVendutaDto.class).getCodicePubblicazione(), lessThan(ppv.getCodFineQuotidiano()))));
				listPubblicazioniPiuVendute.removeAll(listToRemove);
			}
			for (PubblicazionePiuVendutaDto ppv : listPubblicazioniPiuVendute) {
				if (Strings.isNullOrEmpty(ppv.getNomeImmagine())) {
					String nomeImmagine = FileUtils.createFakeImage(ppv.getTitolo(), venditeIconWidth, venditeIconWidth, imgMiniaturePathDir, 8, Font.BOLD, 14);
					ppv.setNomeImmagine(nomeImmagine);
					ppv.setTipoImmagine(IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA);
				}
			}
		}
		requestMap.put("tableTitle", getText("igeriv.configurazione.barra.scelta.rapida.pubblicazioni"));
		return SUCCESS;
	}
	
	/**
	 * Salva la barra di scelta rapida delle vendite
	 * 
	 * @return String
	 */
	public String saveMenuSceltaRapida() {
		try {
			Integer[] arrCoddlL =  Strings.isNullOrEmpty(coddlL) ? getAuthUser().getArrCodFiegDl() : buildArrCoddl(coddlL);
			Integer[] arrCodEdicolaL = Strings.isNullOrEmpty(coddlL) ? getAuthUser().getArrId() : buildArrCodEdicola(coddlL);
			String[] codPubblicazioniL = (codiciPubblicazioniL != null) ? codiciPubblicazioniL.split(",") : new String[]{};
			String[] nomiImmagineL = (nomeImmaginiL != null) ? nomeImmaginiL.split(",") : new String[]{};
			String[] tipiImmagineL = (tipoImmaginiL != null) ? tipoImmaginiL.split(",") : new String[]{};
			
			Integer[] arrCoddlR =  Strings.isNullOrEmpty(coddlR) ? getAuthUser().getArrCodFiegDl() : buildArrCoddl(coddlR);
			Integer[] arrCodEdicolaR = Strings.isNullOrEmpty(coddlR) ? getAuthUser().getArrId() : buildArrCodEdicola(coddlR);
			String[] codPubblicazioniR = (codiciPubblicazioniR != null) ? codiciPubblicazioniR.split(",") : new String[]{};
			String[] nomiImmagineR = (nomeImmaginiR != null) ? nomeImmaginiR.split(",") : new String[]{};
			String[] tipiImmagineR = (tipoImmaginiR != null) ? tipoImmaginiR.split(",") : new String[]{};
			
			venditeService.saveBarraSceltaRapida(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), codPubblicazioniL, arrCoddlL, arrCodEdicolaL, nomiImmagineL, tipiImmagineL, codPubblicazioniR, arrCoddlR, arrCodEdicolaR, nomiImmagineR, tipiImmagineR);
			requestMap.put("tableTitle", getText("igeriv.configurazione.barra.scelta.rapida.pubblicazioni"));
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return SUCCESS;
	}

	/**
	 * @param coddl
	 * @return
	 */
	private Integer[] buildArrCoddl(String coddl) {
		List<Integer> list = new ArrayList<Integer>(); 
		for (String cod : coddl.split(",")) {
			list.add(new Integer(cod.trim()));
		}
		return list.toArray(new Integer[]{});
	}
	
	/**
	 * @param coddl
	 * @return
	 */
	private Integer[] buildArrCodEdicola(String coddl) {
		List<Integer> list = new ArrayList<Integer>(); 
		for (String cod : coddl.split(",")) {
			list.add(iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(new Integer(cod.trim()), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId()));
		}
		return list.toArray(new Integer[]{});
	}

	/**
	 * Apre la dialog di modifica immagine miniatura sx
	 * 
	 * @return String
	 */
	public String editImgMiniaturaEdicolaSinistra() {
		barraSceltaRapidaSinistra = venditeService.getBarraSceltaRapidaSinistra(getAuthUser().getId(), getAuthUser().getCodFiegDl(), cpu);
		if (barraSceltaRapidaSinistra == null) {
			barraSceltaRapidaSinistra = new BarraSceltaRapidaSinistraVo();
			BarraSceltaRapidaSinistraPk pk = new BarraSceltaRapidaSinistraPk(); 
			pk.setCodEdicola(getAuthUser().getId());
			pk.setCodFiegDl(getAuthUser().getCodFiegDl());
			pk.setCodicePubblicazione(cpu);
			barraSceltaRapidaSinistra.setPk(pk);
			barraSceltaRapidaSinistra.setNomeImmagine(nomeImmagine);
			barraSceltaRapidaSinistra.setTipoImmagine(tipoImmagine);
		}
		return "successEditImgMiniaturaSinistra";
	}
	
	/**
	 * Apre la dialog di modifica immagine miniatura dx
	 * 
	 * @return String
	 */
	public String editImgMiniaturaEdicolaDestra() {
		barraSceltaRapidaDestra = venditeService.getBarraSceltaRapidaDestra(getAuthUser().getId(), getAuthUser().getCodFiegDl(), cpu);
		if (barraSceltaRapidaDestra == null) {
			barraSceltaRapidaDestra = new BarraSceltaRapidaDestraVo();
			BarraSceltaRapidaDestraPk pk = new BarraSceltaRapidaDestraPk();
			pk.setCodEdicola(getAuthUser().getId());
			pk.setCodFiegDl(getAuthUser().getCodFiegDl());
			pk.setCodicePubblicazione(cpu);
			barraSceltaRapidaDestra.setPk(pk);
			barraSceltaRapidaDestra.setNomeImmagine(nomeImmagine);
			barraSceltaRapidaDestra.setTipoImmagine(tipoImmagine);
		}
		return "successEditImgMiniaturaDestra";
	}
	
	/**
	 * Aggiorna l'immagine miniatura di una pubblicazione per un'edicola
	 *  
	 * @return String
	 * @throws IOException 
	 */
	public String saveImgMiniaturaEdicolaSinistra() {
		try {
			if (barraSceltaRapidaSinistra != null) {
				if (attachment1 != null) {
					File outImgDirResized = new File(imgMiniatureEdicolaPathDir);
					if (!outImgDirResized.isDirectory()) {
						outImgDirResized.mkdirs();
					}
					returnString = StringUtility.buildAttachmentFileName(attachment1FileName, getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
					File out = new File(imgMiniatureEdicolaPathDir + System.getProperty("file.separator") + returnString);
					FileUtils.copyFile(attachment1, out);
				    FileUtils.resizeImage(out, new File(imgMiniatureEdicolaPathDir), 100, 100);
					barraSceltaRapidaSinistra.setNomeImmagine(returnString);
					barraSceltaRapidaSinistra.setTipoImmagine(IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA_EDICOLA);
				}
				venditeService.saveBaseVo(barraSceltaRapidaSinistra);
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "successSaveImgMiniatura";
	}
	
	/**
	 * Aggiorna l'immagine miniatura di una pubblicazione per un'edicola
	 *  
	 * @return String
	 * @throws IOException 
	 */
	public String saveImgMiniaturaEdicolaDestra() {
		try {
			if (barraSceltaRapidaDestra != null) {
				if (attachment1 != null) {
					File outImgDirResized = new File(imgMiniatureEdicolaPathDir);
					if (!outImgDirResized.isDirectory()) {
						outImgDirResized.mkdirs();
					}
					returnString = StringUtility.buildAttachmentFileName(attachment1FileName, getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
					File out = new File(imgMiniatureEdicolaPathDir + System.getProperty("file.separator") + returnString);
					FileUtils.copyFile(attachment1, out);
				    FileUtils.resizeImage(out, new File(imgMiniatureEdicolaPathDir), 100, 100);
					barraSceltaRapidaDestra.setNomeImmagine(returnString);
					barraSceltaRapidaDestra.setTipoImmagine(IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA_EDICOLA);
				}
				venditeService.saveBaseVo(barraSceltaRapidaDestra);
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "successSaveImgMiniatura";
	}
	
	/**
	 * Invia email di richiesta del logo dell'immagine della pubblicazione
	 * 
	 * @return String
	 */
	public String sendRequestEmail() { 
		String titolo = pubblicazioniService.getAnagraficaPubblicazioneByPk(getAuthUser().getCodFiegDlMaster(), cpu).getTitolo();
		String msg = MessageFormat.format(getText("msg.email.richiersta.logomarca.immagine.body"), 
				DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH_HHMMSS), getAuthUser().getId().toString(), 
				getAuthUser().getRagioneSocialeEdicola(), getAuthUser().getCodFiegDl().toString(), titolo, cpu);
		mailingListService.sendEmail(new String[]{dpeSupportMailingList}, getText("msg.email.richiersta.logomarca.immagine.subject"), msg, true);
		return "successSendEmail";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#getParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.lang.String)
	 */
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#saveParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.util.Map)
	 */
	public void saveParameters(Context context, String arg1, Map arg2) {
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.barra.scelta.rapida.pubblicazioni");
	}

	
}
