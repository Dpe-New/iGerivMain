package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.isIn;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.dto.ConfermaLetturaMessaggioDto;
import it.dpe.igeriv.dto.EmailDlDto;
import it.dpe.igeriv.dto.EmailRivenditaDto;
import it.dpe.igeriv.dto.MessaggioDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ControlloLetturaMessaggioVo;
import it.dpe.igeriv.vo.EmailRivenditaVo;
import it.dpe.igeriv.vo.MessaggioIdtnVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.pk.ControlloLetturaMessaggioPk;
import it.dpe.igeriv.vo.pk.MessaggioPk;
import it.dpe.service.mail.MailingListService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.mail.MessagingException;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.hibernate.exception.ConstraintViolationException;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/**
 * Classe action per la gestione dei messaggi del sistema da parte del DL.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@SuppressWarnings({ "unused", "rawtypes", "unchecked"})
@Scope("prototype")
@Component("messagesAction")
public class MessagesAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final MessaggiService messaggiService;
	private final IGerivBatchService iGerivBatchService;
	private final EdicoleService edicoleService;
	private final MailingListService mailingListService;
	private final String uploadPathDir;
	private final String edicolaUploadPathDir;
	private final String emailDlToRivendita;
	private final String crumbName = getText("igeriv.menu.2");
	private final String crumbNameConferme = getText("igeriv.conferme.lettura");
	private final String crumbNameMsgRicevuti = getText("igeriv.menu.35");
	private final String crumbNameNuovoMsg = getText("igeriv.nuovo.messaggio");
	private final String crumbNameNuovoIdtn = getText("igeriv.nuovo.messaggio.idtn");
	private final String crumbNameAggiornaMsg = getText("igeriv.aggiorna.visualizza.messaggio");
	private String tableHeight;
	private String tableTitle;
	private List<MessaggioDto> messaggi;
	private List<EmailRivenditaDto> emailsRivendita;
	private String actionName;
	private String filterTitle;
	private String reportTitle;
	private MessaggioDto messaggioRivendita;
	private MessaggioVo messaggio;
	private MessaggioIdtnVo messaggioIdtnVo;
	private EmailRivenditaDto messaggioInviatoDaRivendita;
	private String messagePk;
	private String message;
	private String messageType;
	private File attachment1;
	private String attachment1ContentType;
	private String attachment1FileName;
	private File attachment2;
	private String attachment2ContentType;
	private String attachment2FileName;
	private File attachment3;
	private String attachment3ContentType;
	private String attachment3FileName;
	private String strCodEdicolaDl;
	private String strDataMessaggioDa;
	private String strDataMessaggioA;
	private String edicolaLabel;
	private boolean disableForm;
	private String letto;
	private String emailsToSend;
	private String msgSent;
	private String destinatari;
	private String dataDaStr;
	private String dataAStr;
	private Timestamp dataDa;
	private Timestamp dataA;
	private Integer idEmailRivendita;
	private Integer destinatario;
	private String destinatarioSelected;
	private String giroTipo;
	private String zonaTipo;
	private String giro;
	private String zona;
	private String giroSelected;
	private String zonaSelected;
	private String giroTipoSelected;
	private String zonaTipoSelected;
	private boolean isCDL;
	private List<Integer> rivenditeSel;
	private Integer idtn;
	private Map<String, String> jsonMap;
	private String strDataMessaggio;
	private String oraMessaggio;
	private String minutoMessaggio;
	private Boolean sysdate;
	
	public MessagesAction() {
		this(null,null,null,null,null,null,null);
	}
	
	@Autowired
	public MessagesAction(MessaggiService messaggiService, IGerivBatchService iGerivBatchService, EdicoleService edicoleService, MailingListService mailingListService, @Value("${upload.path.dir}") String uploadPathDir, @Value("${edicole.upload.path.dir}") String edicolaUploadPathDir, @Value("${email.dl.to.rivendite}") String emailDlToRivendita) {
		this.messaggiService = messaggiService;
		this.iGerivBatchService = iGerivBatchService;
		this.edicoleService = edicoleService;
		this.mailingListService = mailingListService;
		this.uploadPathDir = uploadPathDir;
		this.edicolaUploadPathDir = edicolaUploadPathDir;
		this.emailDlToRivendita = emailDlToRivendita;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		isCDL = getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE);
	}
	
	@Override
	public void validate(){
		
		 String str_action = (getActionName() != null)?getActionName():"";
		 System.out.println(str_action);
		
		if (getActionName() != null && getActionName().contains("showEmailInviati.action")) {
			filterTitle = getText("igeriv.msg.inviati");
			if (dataDaStr == null || dataAStr == null || dataDaStr.equals("") || dataAStr.equals("")) {
				addActionError(getText("error.specificare.data.o.intervallo.date"));
			} else {
				try {
					dataDa = DateUtilities.floorDay(DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataDaStr));
					return;
				}
				try {
					dataA = DateUtilities.ceilDay(DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataAStr));
					return;
				}
			}
		}
		else if (getActionName() != null && getActionName().contains("saveMessageIdtn.action")) {
			
			tableTitle = getText("igeriv.nuovo.messaggio.idtn");
			if (messaggioIdtnVo.getIdtn() == null || messaggioIdtnVo.getTesto() == null || messaggioIdtnVo.getTesto().equals("")) {
				return;
			}
		}
		//07-08-2017 verificare il mancato inserimento dell'edicola per l'invio del messaggio puntuale.
		else if (getActionName() != null && getActionName().contains("showMessages.action")) {
			
			if (destinatario.equals(IGerivConstants.COD_EDICOLA_SINGOLA)) {
				Integer codEdicolaDl = null;
				if (!Strings.isNullOrEmpty(strCodEdicolaDl) && NumberUtils.isNumber(strCodEdicolaDl)) {
					codEdicolaDl = Integer.parseInt(strCodEdicolaDl);
				} else if (!Strings.isNullOrEmpty(messaggio.getEdicolaLabel()) && NumberUtils.isNumber(messaggio.getEdicolaLabel().trim())) {
					codEdicolaDl = new Integer(messaggio.getEdicolaLabel().trim());
				}
				if(codEdicolaDl == null || codEdicolaDl == 0){
					addActionError(getText("msg.destinatario.singola.edicola.invalido"));
					return;
				}
			}
			
		}
		
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() {
		filterTitle = getText("igeriv.comunicazioni.per.le.rivendite");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameMsgRicevuti}")
	@SkipValidation
	public String showMessagesEdicoleFilter() {
		filterTitle = getText("igeriv.menu.35");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showMessages() throws ParseException {
		filterTitle = getText("igeriv.comunicazioni.per.le.rivendite");
		Timestamp dtMessaggioDa = (strDataMessaggioDa != null && !strDataMessaggioDa.equals("")) ? DateUtilities.floorDay(DateUtilities.parseDate(strDataMessaggioDa, DateUtilities.FORMATO_DATA_SLASH)) : null;
		Timestamp dtMessaggioA = (strDataMessaggioA != null && !strDataMessaggioA.equals("")) ? DateUtilities.ceilDay(DateUtilities.parseDate(strDataMessaggioA, DateUtilities.FORMATO_DATA_SLASH)) : null;
		Integer codEdicolaDl = (strCodEdicolaDl != null && !strCodEdicolaDl.equals("")) ? Integer.parseInt(strCodEdicolaDl) : null;
		messaggi = messaggiService.getMessaggiRivendite(new Integer[]{getAuthUser().getCodFiegDl()}, dtMessaggioDa, dtMessaggioA, new Integer[]{codEdicolaDl}, (codEdicolaDl != null));
		requestMap.put("messaggi", messaggi);
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameMsgRicevuti}")
	@SkipValidation
	public String showMessagesEdicole() throws ParseException {
		filterTitle = getText("igeriv.menu.35");
		Integer[] codDl = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		Integer[] codEdicola = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getCodFiegDl()};
		strDataMessaggioDa = Strings.isNullOrEmpty(strDataMessaggioDa) ? DateUtilities.getTimestampAsString(DateUtilities.togliGiorni(new Timestamp(new Date().getTime()), 15), DateUtilities.FORMATO_DATA_SLASH) : strDataMessaggioDa;
		strDataMessaggioA = Strings.isNullOrEmpty(strDataMessaggioA) ? DateUtilities.getTimestampAsString(new Timestamp(new Date().getTime()), DateUtilities.FORMATO_DATA_SLASH) : strDataMessaggioA;
		Timestamp dtMessaggioDa = DateUtilities.floorDay(DateUtilities.parseDate(strDataMessaggioDa, DateUtilities.FORMATO_DATA_SLASH));
		Timestamp dtMessaggioA = DateUtilities.ceilDay(DateUtilities.parseDate(strDataMessaggioA, DateUtilities.FORMATO_DATA_SLASH));
		messaggi = messaggiService.getMessaggiRivendite(codDl, dtMessaggioDa, dtMessaggioA, codEdicola, true);
		requestMap.put("messaggi", messaggi);
		reportTitle = MessageFormat.format(getText("igeriv.messaggi.ricevuti.dal.al"), strDataMessaggioDa, strDataMessaggioA);
		return SUCCESS;
	}
	
	@SkipValidation
	public String showConfermeLettura() throws ParseException {
		filterTitle = getText("igeriv.conferme.lettura");
		if (messagePk != null && !messagePk.equals("")) {
			StringTokenizer st = new StringTokenizer(messagePk, "|");
			Integer codFiegDl = Integer.parseInt(st.nextToken());
			Timestamp dtMessaggio = DateUtilities.parseDate(st.nextToken(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
			Integer tipoDestinatario = Integer.parseInt(st.nextToken());
			Integer destinatarioA = Integer.parseInt(st.nextToken());
			Integer destinatarioB = Integer.parseInt(st.nextToken());
			Integer codEdicolaDl = (strCodEdicolaDl != null && !strCodEdicolaDl.equals("")) ? Integer.parseInt(strCodEdicolaDl) : null;
			List<ConfermaLetturaMessaggioDto> conferme = messaggiService.getConfermeLettura(codEdicolaDl, codFiegDl, dtMessaggio, tipoDestinatario, destinatarioA, destinatarioB);
			requestMap.put("conferme", conferme);
			requestMap.put("title", getText("igeriv.conferme.lettura"));
		}
		return IGerivConstants.ACTION_SHOW_CONFERME_LETTURA;
	}
	
	@SkipValidation
	public String showMessageEdicole() throws ParseException {
		filterTitle = getText("igeriv.comunicazioni.per.le.rivendite");
		if (messagePk != null && !messagePk.equals("")) {
			StringTokenizer st = new StringTokenizer(messagePk, "|");
			Integer codFiegDl = Integer.parseInt(st.nextToken());
			Timestamp dtMessaggio = DateUtilities.parseDate(st.nextToken(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
			Integer tipoDestinatario = Integer.parseInt(st.nextToken());
			Integer destinatarioA = Integer.parseInt(st.nextToken());
			Integer destinatarioB = Integer.parseInt(st.nextToken());
			messaggioRivendita = messaggiService.getMessaggioRivendita(getAuthUser().getCodFiegDl(), codFiegDl, dtMessaggio, tipoDestinatario, destinatarioA, destinatarioB);
			messaggioRivendita.setMessaggioEscape(StringUtility.escapeHTML(messaggioRivendita.getMessaggio() != null ? messaggioRivendita.getMessaggio() : "", false));
		}
		return IGerivConstants.ACTION_SHOW_MESSAGE;
	}
	
	@SkipValidation
	public String showAvvisoNuovoMessaggio() {
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameAggiornaMsg}")
	@SkipValidation
	public String showMessage() throws ParseException {
		tableTitle = getText("igeriv.aggiorna.messaggio");
		if (messagePk != null && !messagePk.equals("")) {
			StringTokenizer st = new StringTokenizer(messagePk, "|");
			Integer codFiegDl = Integer.parseInt(st.nextToken());
			Timestamp dtMessaggio = DateUtilities.parseDate(st.nextToken(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
			Integer tipoDestinatario = Integer.parseInt(st.nextToken());
			Integer destinatarioA = Integer.parseInt(st.nextToken());
			Integer destinatarioB = Integer.parseInt(st.nextToken());
			messaggio = messaggiService.getMessaggioRivenditaVo(codFiegDl, dtMessaggio, tipoDestinatario, destinatarioA, destinatarioB);
			messaggio.setMessaggioEscape((messaggio.getMessaggio() != null && !messaggio.getMessaggio().equals("")) ? StringUtility.escapeHTML(messaggio.getMessaggio(), false) : "");
			destinatario = messaggio.getPk().getTipoDestinatario();
			giroTipo = messaggio.getPk().getDestinatarioA().toString();
			zonaTipo = messaggio.getPk().getDestinatarioA().toString();
			giroTipoSelected = "" + messaggio.getPk().getDestinatarioA();
			zonaTipoSelected = "" + messaggio.getPk().getDestinatarioA();
			giroSelected = "" + messaggio.getPk().getDestinatarioB();
			zonaSelected = "" + messaggio.getPk().getDestinatarioB();
			if (messaggio.getPk().getTipoDestinatario().equals(IGerivConstants.COD_EDICOLA_SINGOLA)) {
				strCodEdicolaDl = "" + messaggio.getPk().getDestinatarioA();
			}
			requestMap.put("messaggio", messaggio);
			disableForm = (messaggio.getStatoMessaggio() != null && messaggio.getStatoMessaggio().equals(IGerivConstants.STATO_MESSAGGIO_INVIATO) ? true : false);
		} 
		return IGerivConstants.ACTION_NEW_MESSAGE;
	}
	
	@BreadCrumb("%{crumbNameNuovoMsg}")
	@SkipValidation
	public String newMessage() {
		tableTitle = getText("igeriv.nuovo.messaggio");
		MessaggioPk pk = new MessaggioPk();
		pk.setCodFiegDl(getAuthUser().getCodFiegDl());
		messaggio = new MessaggioVo();
		messaggio.setPk(pk);
		disableForm = false;
		strDataMessaggio = null;
		oraMessaggio = null;
		minutoMessaggio = null;
		sysdate = null;
		return IGerivConstants.ACTION_NEW_MESSAGE;
	}
	
	@BreadCrumb("%{crumbNameNuovoIdtn}")
	@SkipValidation
	public String newMessageIdtn() {
		tableTitle = getText("igeriv.nuovo.messaggio.idtn");
		
		messaggioIdtnVo = new MessaggioIdtnVo();
		idtn = null;
		message = null;
		attachment1 = null;
		attachment2 = null;
		attachment3 = null;
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String loadMessageIdtn() {
		try {
			messaggioIdtnVo = messaggiService.getMessaggioIdtnVo(getAuthUser().getCodFiegDl(), idtn);
			jsonMap = new HashMap<String, String>();
			if (messaggioIdtnVo == null) {
				jsonMap.put("id", "");
			}
			else {
				jsonMap.put("id", messaggioIdtnVo.getIdMessaggioIdtn().toString());
				jsonMap.put("testo", messaggioIdtnVo.getTesto());
				jsonMap.put("allegato1", messaggioIdtnVo.getAttachmentName1());
				jsonMap.put("allegato2", messaggioIdtnVo.getAttachmentName2());
				jsonMap.put("allegato3", messaggioIdtnVo.getAttachmentName3());
			}
		}
		catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		
		return "loadMessageIdtn";
	}
	
	public String deleteMessageIdtn() {
		if (messaggioIdtnVo != null) {
			try {
				messaggioIdtnVo = messaggiService.getMessaggioIdtnVo(messaggioIdtnVo.getIdMessaggioIdtn());
				messaggiService.deleteVo(messaggioIdtnVo);
			}
			catch (Throwable e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
		
		return newMessageIdtn();
	}
	
	public String saveMessageIdtn() {
		if (messaggioIdtnVo != null) {
			try {
				messaggioIdtnVo.setCodDl(getAuthUser().getCodFiegDl());
				messaggioIdtnVo.setIdtn(idtn);
				if (messaggioIdtnVo.getAttachmentName1() != null && messaggioIdtnVo.getAttachmentName1().equals("")) {
					messaggioIdtnVo.setAttachmentName1(null);
				}
				if (messaggioIdtnVo.getAttachmentName2() != null && messaggioIdtnVo.getAttachmentName2().equals("")) {
					messaggioIdtnVo.setAttachmentName2(null);
				}
				if (messaggioIdtnVo.getAttachmentName3() != null && messaggioIdtnVo.getAttachmentName3().equals("")) {
					messaggioIdtnVo.setAttachmentName3(null);
				}
				
				String uploadDir = uploadPathDir + System.getProperty("file.separator") + getAuthUser().getCodFiegDl();
				File uploadfileDir = new File(uploadDir);
				if (!uploadfileDir.exists()) {
					uploadfileDir.mkdir();
				}
				if (attachment1 != null) {
					String path = uploadDir + System.getProperty("file.separator") + StringUtility.getFileName(attachment1FileName);
				    File out = new File(path);
					FileUtils.copyFile(attachment1, out);
					messaggioIdtnVo.setAttachmentName1(attachment1FileName);
				}
				if (attachment2 != null) {
					String path = uploadDir + System.getProperty("file.separator") + StringUtility.getFileName(attachment2FileName);
				    File out = new File(path);
					FileUtils.copyFile(attachment2, out);
					messaggioIdtnVo.setAttachmentName2(attachment2FileName);
				}
				if (attachment3 != null) {
					String path = uploadDir + System.getProperty("file.separator") + StringUtility.getFileName(attachment3FileName);
				    File out = new File(path);
					FileUtils.copyFile(attachment3, out);
					messaggioIdtnVo.setAttachmentName3(attachment3FileName);
				}
				
				messaggiService.saveBaseVo(messaggioIdtnVo);
			}
			catch (Throwable e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
		
		return newMessageIdtn();
	}
	
	public String saveMessageEdicola() {
		ControlloLetturaMessaggioVo vo = new ControlloLetturaMessaggioVo();
		ControlloLetturaMessaggioPk pk = new ControlloLetturaMessaggioPk();
		pk.setCodFiegDl(messaggioRivendita.getCodFiegDl());
		pk.setCodiceEdicola(getAuthUser().getCodFiegDl());
		pk.setDtMessaggio(messaggioRivendita.getDtMessaggio());
		vo.setPk(pk);
		vo.setMessaggioLetto(IGerivConstants.COD_MESSAGGIO_LETTO);
		messaggiService.saveBaseVo(vo);
		return null;
	}
	
	public String saveMessage() throws IOException, ParseException , IGerivRuntimeException{
		if (messaggio != null) {
			messaggio.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_NUOVO);
			String msgerror = saveMessaggio();
			if(msgerror!=null){
				//requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("msg.destinatario.singola.edicola.invalido") + IGerivConstants.END_EXCEPTION_TAG);
				//throw new IGerivRuntimeException();
				addActionError(getText("gp.errore.imprevisto"));
				return IGerivConstants.REDIRECT_ERROR;
			}
		}
		return IGerivConstants.REDIRECT;
	}

	public String saveAndSendMessage() throws IOException, ParseException,IGerivRuntimeException {
		if (messaggio != null) {
			messaggio.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_INVIATO);
			String msgerror = saveMessaggio();
			if(msgerror!=null){
				//requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("msg.destinatario.singola.edicola.invalido") + IGerivConstants.END_EXCEPTION_TAG);
				//throw new IGerivRuntimeException();
				addActionError(getText("gp.errore.imprevisto"));
				return IGerivConstants.REDIRECT_ERROR;
			}
		}
		return IGerivConstants.REDIRECT; 
	}
	
	public String deleteMessage() {
		tableTitle = getText("igeriv.nuovo.messaggio");
		if (messaggio != null) {
			Integer tipoDest = null; 
			Integer destA = 0;
			Integer destB = 0;
			if (destinatario.equals(IGerivConstants.COD_TUTTE_LE_EDICOLE)) {
				tipoDest = IGerivConstants.COD_TUTTE_LE_EDICOLE;
			} else if (destinatario.equals(IGerivConstants.COD_EDICOLA_SINGOLA)) {
				tipoDest = IGerivConstants.COD_EDICOLA_SINGOLA;
				Integer codEdicolaDl = (strCodEdicolaDl != null && !strCodEdicolaDl.equals("")) ? Integer.parseInt(strCodEdicolaDl) : (messaggio.getEdicolaLabel() != null && !messaggio.getEdicolaLabel().equals("") && NumberUtils.isNumber(messaggio.getEdicolaLabel().trim()) ? new Integer(messaggio.getEdicolaLabel().trim()) : null);
				destA = ((codEdicolaDl != null) ? codEdicolaDl : 0);
			} else if (destinatario.equals(IGerivConstants.COD_GIRO_TIPO)) {
				tipoDest = IGerivConstants.COD_GIRO_TIPO;
				destA = new Integer(giroTipo);
			} else if (destinatario.equals(IGerivConstants.COD_ZONA_TIPO)) {
				tipoDest = IGerivConstants.COD_ZONA_TIPO;
				destA = new Integer(zonaTipo);
			}
			messaggio = messaggiService.getMessaggioRivenditaVo(getAuthUser().getCodFiegDl(), messaggio.getDtMessaggio(), tipoDest, destA, destB);
			messaggiService.deleteVo(messaggio);
		}
		return IGerivConstants.REDIRECT;
	}
	
	public String showEmailRivendita() {
		destinatari = (sessionMap.get(IGerivConstants.SESSION_VAR_EMAIL_DL_LIST) == null || ((List)sessionMap.get(IGerivConstants.SESSION_VAR_EMAIL_DL_LIST)).isEmpty()) ? getAuthUser().getRagioneSocialeDl() : null;
		requestMap.put("emailValido", (isCDL || (!Strings.isNullOrEmpty(getAuthUser().getEmail()) && getAuthUser().isEmailValido())) ? true : false);
		return SUCCESS;
	}
	
	public String showEmailInviatiFilter() {
		filterTitle = getText("igeriv.msg.inviati");
		return SUCCESS;
	}
	
	public String showEmailInviati() throws ParseException {
		filterTitle = getText("igeriv.msg.inviati");
		dataDa = DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH);
		dataA = DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH);
		emailsRivendita = messaggiService.getEmailInviatiDaRivendita(getAuthUser().getCodFiegDl(), DateUtilities.floorDay(dataDa), DateUtilities.ceilDay(dataA));
		requestMap.put("messaggi", emailsRivendita);
		requestMap.put("codRivendita", getAuthUser().getCodFiegDl());
		return SUCCESS;
	} 
	
	@SkipValidation
	public String showMessageInviatoDaRivendita() {
		if (idEmailRivendita != null) {
			messaggioInviatoDaRivendita = messaggiService.getMessaggioInviatoDaRivendita(idEmailRivendita);
			messaggioInviatoDaRivendita.setMessaggioEscape(StringUtility.escapeHTML(messaggioInviatoDaRivendita.getMessaggio(), false));
			String uploadDir = edicolaUploadPathDir + System.getProperty("file.separator") + getAuthUser().getCodFiegDl();
			requestMap.put("codRivendita", getAuthUser().getCodFiegDl());
		}
		return IGerivConstants.ACTION_SHOW_MESSAGE;
	}
	
	public String sendEmailRivendita() throws MessagingException, IOException { 
		tableTitle = getText("igeriv.nuovo.messaggio");
		List<String> emails = null;
		List<String> destinatari = null;
		if (emailsToSend != null && !emailsToSend.equals("")) {
			String[] emailsDl = emailsToSend.trim().split(",");
			List<Integer> emailIdList = new ArrayList<Integer>();
			for (String emailId : emailsDl) {
				emailIdList.add(new Integer(emailId));
			}
			List<EmailDlDto> emailList = (List<EmailDlDto>) sessionMap.get(IGerivConstants.SESSION_VAR_EMAIL_DL_LIST);
			List<EmailDlDto> emailDlList = select(emailList, having(on(EmailDlDto.class).getCodEmailVo(), isIn(emailIdList)));
			emails = extract(emailDlList, on(EmailDlDto.class).getEmail());
			destinatari = extract(emailDlList, on(EmailDlDto.class).getNome());
		} else {
			emails = Arrays.asList(sessionMap.get(IGerivConstants.SESSION_VAR_EMAIL_DL).toString());
		}
		File out = null; 
		if (attachment1 != null) {
			String uploadDir = edicolaUploadPathDir + System.getProperty("file.separator") + getAuthUser().getCodFiegDl();
			File dirs = new File(uploadDir);
			if (!dirs.isDirectory()) {
				dirs.mkdirs();
			}
			String path = uploadDir + System.getProperty("file.separator") + StringUtility.getFileName(attachment1FileName);
		    out = new File(path);
			FileUtils.copyFile(attachment1, out);
		}
		String replyToEmail = null;
		String label = null;
		if (getAuthUser().isEmailReplyToInstantMessages()) {
			replyToEmail = emailDlToRivendita;
			label = getAuthUser().getCodFiegDl() + "|" + getAuthUser().getCodFiegDl();
		} else {
			replyToEmail = getAuthUser().getEmail();
		}
		mailingListService.sendEmailWithAttachment(emails.toArray(new String[]{}), sessionMap.get("messaggioDaRivendita").toString(), message, out, true, replyToEmail, false, label);
		EmailRivenditaVo vo = new EmailRivenditaVo();
		vo.setDataMessaggio(new Timestamp(new Date().getTime()));
		AnagraficaEdicolaVo edicola = new AnagraficaEdicolaVo();
		edicola.setCodEdicola(getAuthUser().getCodFiegDl());
		vo.setEdicola(edicola);
		vo.setTitolo(sessionMap.get("messaggioDaRivendita").toString());
		vo.setMessaggio(message);
		vo.setAllegato(StringUtility.getFileName(attachment1FileName));
		if (destinatari == null || destinatari.isEmpty()) {
			vo.setDestinatari(getAuthUser().getRagioneSocialeDl());
		} else {
			vo.setDestinatari(Joiner.on(",").join(destinatari));
		}
		messaggiService.saveBaseVo(vo);
		return IGerivConstants.REDIRECT;
	}
	
	private String saveMessaggio() throws IOException, ParseException , IGerivRuntimeException{
		
		String msgError = null;
		
		if (messaggio.getPk().getDtMessaggio() == null) {
			if (sysdate != null && sysdate) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(new Date().getTime());
				calendar.set(Calendar.SECOND, 0);
				messaggio.getPk().setDtMessaggio(new Timestamp(calendar.getTimeInMillis()));
			} else {
				String date = String.format("%s %s:%s:00", strDataMessaggio, oraMessaggio, minutoMessaggio);
				Timestamp messageDate = DateUtilities.parseDate(date, DateUtilities.FORMATO_DATA_SLASH_HHMMSS);
				
				// Controllo se c'è già un messaggio inserito per la stessa data/ora/minuti
				Timestamp maxMessageDate = messaggiService.getMaxSecondsDateMessaggio(getAuthUser().getCodFiegDl(), messageDate);
				if (maxMessageDate != null) {
					// Esiste già
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(maxMessageDate.getTime());
					int seconds = calendar.get(Calendar.SECOND);
					seconds++;
					calendar.setTimeInMillis(messageDate.getTime());
					calendar.set(Calendar.SECOND, seconds);
					messageDate = new Timestamp(calendar.getTimeInMillis());
				}
				
				messaggio.getPk().setDtMessaggio(messageDate);
			}
		}
		if (destinatario != null && destinatario.equals(IGerivConstants.COD_EDICOLE_MULTIPLE) && rivenditeSel != null && !rivenditeSel.isEmpty()) {
			for (Integer codEdicola : rivenditeSel) {
				MessaggioVo msg = new MessaggioVo(this.messaggio);
				msg.getPk().setTipoDestinatario(IGerivConstants.COD_EDICOLA_SINGOLA);
				msg.getPk().setDestinatarioA(codEdicola);
				msg.getPk().setDestinatarioB(0);
				AbbinamentoEdicolaDlVo abbEdicola = edicoleService.getAbbinamentoEdicolaDlVoByCodEdicolaWeb(codEdicola);
				msg.setEdicolaLabel(abbEdicola.getCodEdicolaDl() + " - " + abbEdicola.getAnagraficaEdicolaVo().getRagioneSocialeEdicolaPrimaRiga() + " - " + abbEdicola.getAnagraficaEdicolaVo().getLocalitaEdicolaPrimaRiga());
				prepareMessageVo(msg);
			}
		} else {
			
			if (destinatario != null) {
				
				/* 13-04-2017 @menta - destinatario messaggio edicola singola non valorizzato correttamente. */
				if (destinatario.equals(IGerivConstants.COD_EDICOLA_SINGOLA)) {
					Integer codEdicolaDl = null;
					if (!Strings.isNullOrEmpty(strCodEdicolaDl) && NumberUtils.isNumber(strCodEdicolaDl)) {
						codEdicolaDl = Integer.parseInt(strCodEdicolaDl);
					} else if (!Strings.isNullOrEmpty(messaggio.getEdicolaLabel()) && NumberUtils.isNumber(messaggio.getEdicolaLabel().trim())) {
						codEdicolaDl = new Integer(messaggio.getEdicolaLabel().trim());
					}
					if(codEdicolaDl == null || codEdicolaDl == 0){
						msgError = getText("msg.destinatario.singola.edicola.invalido");
						return msgError;
//						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("msg.destinatario.singola.edicola.invalido") + IGerivConstants.END_EXCEPTION_TAG);
//						throw new IGerivRuntimeException();
							
					}
				}
				setMessagePk();
				
				
			}
			try {
				prepareMessageVo(this.messaggio);
			} catch (DataIntegrityViolationException e) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.messaggio.getPk().getDtMessaggio());
				cal.add(Calendar.SECOND, 1);
				this.messaggio.getPk().setDtMessaggio(new Timestamp(cal.getTimeInMillis()));
				prepareMessageVo(this.messaggio);
			}
		}
		return msgError;
	} 

	private void prepareMessageVo(MessaggioVo messaggio) throws IOException {
		String uploadDir = uploadPathDir + System.getProperty("file.separator") + getAuthUser().getCodFiegDl();
		File uploadfileDir = new File(uploadDir);
		if (!uploadfileDir.exists()) {
			uploadfileDir.mkdir();
		}
		if (attachment1 != null) {
			String fileName = StringUtility.getFileName(attachment1FileName);
			String path = uploadDir + System.getProperty("file.separator") + fileName;
		    File out = new File(path);
			FileUtils.copyFile(attachment1, out);
			messaggio.setAttachmentName1(fileName);
		}
		if (attachment2 != null) {
			String fileName = StringUtility.getFileName(attachment2FileName);
			String path = uploadDir + System.getProperty("file.separator") + fileName;
		    File out = new File(path);
			FileUtils.copyFile(attachment2, out);
			messaggio.setAttachmentName2(fileName);
		}
		if (attachment3 != null) {
			String fileName = StringUtility.getFileName(attachment3FileName);
			String path = uploadDir + System.getProperty("file.separator") + fileName;
		    File out = new File(path);
			FileUtils.copyFile(attachment3, out);
			messaggio.setAttachmentName3(fileName);
		}
		if (edicolaLabel != null) {
			messaggio.setEdicolaLabel(edicolaLabel);
		}
		if (giro != null && !giro.equals("")) {
			String[] giri = giro.split(",");
			for (String g : giri) {
				messaggio.getPk().setDestinatarioB(new Integer(g.trim()));
				saveMessageVo(messaggio);
			}
		} else if (zona != null && !zona.equals("")) {
			String[] zone = zona.split(",");
			for (String z : zone) {
				messaggio.getPk().setDestinatarioB(new Integer(z.trim()));
				saveMessageVo(messaggio);
			}
		} else { 
			saveMessageVo(messaggio);
		}
	}

	/**
	 * @param messaggio
	 */
	private void saveMessageVo(MessaggioVo messaggio) {
		if (message.length() >= 4000) {
			messaggio.setMessaggio(null);
			iGerivBatchService.saveMessaggioWithBlob(messaggio, message.getBytes());
		} else {
			messaggio.setMessaggio(message);
			messaggiService.saveBaseVo(messaggio);
		}
	}

	private void setMessagePk() {
		if (destinatario.equals(IGerivConstants.COD_TUTTE_LE_EDICOLE)) {
			messaggio.getPk().setTipoDestinatario(IGerivConstants.COD_TUTTE_LE_EDICOLE);
			messaggio.getPk().setDestinatarioA(0);
			messaggio.getPk().setDestinatarioB(0);
		} else if (destinatario.equals(IGerivConstants.COD_EDICOLA_SINGOLA)) {
			messaggio.getPk().setTipoDestinatario(IGerivConstants.COD_EDICOLA_SINGOLA);
			Integer codEdicolaDl = null;
			if (!Strings.isNullOrEmpty(strCodEdicolaDl) && NumberUtils.isNumber(strCodEdicolaDl)) {
				codEdicolaDl = Integer.parseInt(strCodEdicolaDl);
			} else if (!Strings.isNullOrEmpty(messaggio.getEdicolaLabel()) && NumberUtils.isNumber(messaggio.getEdicolaLabel().trim())) {
				codEdicolaDl = new Integer(messaggio.getEdicolaLabel().trim());
			}
			messaggio.getPk().setDestinatarioA(((codEdicolaDl != null) ? codEdicolaDl : 0));
			messaggio.getPk().setDestinatarioB(0);
		} else if (destinatario.equals(IGerivConstants.COD_GIRO_TIPO)) {
			messaggio.getPk().setTipoDestinatario(IGerivConstants.COD_GIRO_TIPO);
			messaggio.getPk().setDestinatarioA(new Integer(giroTipo));
		} else if (destinatario.equals(IGerivConstants.COD_ZONA_TIPO)) {
			messaggio.getPk().setTipoDestinatario(IGerivConstants.COD_ZONA_TIPO);
			messaggio.getPk().setDestinatarioA(new Integer(zonaTipo));
		}
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
		String title = getText("igeriv.messaggi");
		if (!Strings.isNullOrEmpty(getActionName())) {
			if (getActionName().contains("email_")) {
				title = getText("igeriv.menu.27");
			} else if (getActionName().contains("email_")) {
				title = getText("igeriv.menu.3");
			}
		}
		return super.getTitle() + title;
	}
	
	public boolean isDisableForm() {
		return disableForm;
	}
	
}
