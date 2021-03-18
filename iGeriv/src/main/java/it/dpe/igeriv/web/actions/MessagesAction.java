package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.isIn;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.dto.ConfermaLetturaMessaggioDto;
import it.dpe.igeriv.dto.EmailDlDto;
import it.dpe.igeriv.dto.EmailRivenditaDto;
import it.dpe.igeriv.dto.MessaggioDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.EncryptUtils;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ControlloLetturaMessaggioVo;
import it.dpe.igeriv.vo.EmailRivenditaVo;
import it.dpe.igeriv.vo.MessaggioIdtnVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.pk.ControlloLetturaMessaggioPk;
import it.dpe.igeriv.vo.pk.MessaggioPk;
import it.dpe.service.mail.MailingListService;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione dei messaggi del sistema da parte del DL.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("messagesAction")
@SuppressWarnings({ "unused", "rawtypes", "unchecked"})
public class MessagesAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(getClass());
	private final MessaggiService messaggiService;
	private final AgenzieService agenzieService;
	private final MailingListService mailingListService;
	private final String uploadPathDir;
	private final String edicolaUploadPathDir;
	private final String emailDlToRivendita;
	private final String returnReceiptToAddress;
	private final String crumbName = getText("igeriv.menu.35");
	private final String crumbNameComRiv = getText("igeriv.comunicazioni.per.le.rivendite");
	private final String crumbNameNewMes = getText("igeriv.nuovo.messaggio");
	private final String crumbNameEmailInv = getText("igeriv.msg.inviati");
	private final String crumbNameInvMesDl = getText("igeriv.menu.27");
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
	private Integer idMessaggioIdtn;
	private Integer priorita;
	
	public MessagesAction() {
		this.messaggiService = null;
		this.agenzieService = null;
		this.mailingListService = null;
		this.uploadPathDir = null;
		this.edicolaUploadPathDir = null;
		this.emailDlToRivendita = null;
		this.returnReceiptToAddress = null;
	}
	
	@Autowired
	public MessagesAction(MessaggiService messaggiService, AgenzieService agenzieService, MailingListService mailingListService, @Value("${upload.path.dir}") String uploadPathDir, @Value("${edicole.upload.path.dir}") String edicolaUploadPathDir, @Value("${email.dl.to.rivendite}") String emailDlToRivendita, @Value("${email.return.receipt.address}") String returnReceiptToAddress) {
		this.messaggiService = messaggiService;
		this.agenzieService = agenzieService;
		this.mailingListService = mailingListService;
		this.uploadPathDir = uploadPathDir;
		this.edicolaUploadPathDir = edicolaUploadPathDir;
		this.emailDlToRivendita = emailDlToRivendita;
		this.returnReceiptToAddress = returnReceiptToAddress;
	}
	

	@Override
	public void prepare() throws Exception {
		super.prepare();
		isCDL = getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE);
	}
	
	@Override
	public void validate() {
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
	}
	
	@BreadCrumb("%{crumbNameComRiv}")
	@SkipValidation
	public String showFilter() {
		filterTitle = getText("igeriv.comunicazioni.per.le.rivendite");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showMessagesEdicoleFilter() {
		filterTitle = getText("igeriv.menu.35");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameComRiv}")
	@SkipValidation
	public String showMessages() throws ParseException {
		filterTitle = getText("igeriv.comunicazioni.per.le.rivendite");
		Timestamp dtMessaggioDa = (strDataMessaggioDa != null && !strDataMessaggioDa.equals("")) ? DateUtilities.floorDay(DateUtilities.parseDate(strDataMessaggioDa, DateUtilities.FORMATO_DATA_SLASH)) : null;
		Timestamp dtMessaggioA = (strDataMessaggioA != null && !strDataMessaggioA.equals("")) ? DateUtilities.ceilDay(DateUtilities.parseDate(strDataMessaggioA, DateUtilities.FORMATO_DATA_SLASH)) : null;
		Integer codEdicolaDl = (strCodEdicolaDl != null && !strCodEdicolaDl.equals("")) ? Integer.parseInt(strCodEdicolaDl) : null;
		messaggi = messaggiService.getMessaggiRivendite(new Integer[]{getAuthUser().getId()}, dtMessaggioDa, dtMessaggioA, new Integer[]{codEdicolaDl}, (codEdicolaDl != null));
		requestMap.put("messaggi", messaggi);
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showMessagesEdicole() throws ParseException {
		filterTitle = getText("igeriv.menu.35");
		
		Integer[] codDl = (getAuthUser().isMultiDl() || getAuthUser().isDlInforiv()) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		Integer[] codEdicola = (getAuthUser().isMultiDl() || getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getId()};
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
			messaggioRivendita = messaggiService.getMessaggioRivendita(getAuthUser().getId(), codFiegDl, dtMessaggio, tipoDestinatario, destinatarioA, destinatarioB);
			String html = null;
			if (messaggioRivendita != null && messaggioRivendita.getMessaggioEsteso() != null) {
				html = messaggioRivendita.getMessaggio();
			} else if (messaggioRivendita != null) {
				html = StringUtility.escapeHTML(messaggioRivendita.getMessaggio() != null ? messaggioRivendita.getMessaggio() : "", false);
			}
			messaggioRivendita.setMessaggioEscape(html);
		}
		return IGerivConstants.ACTION_SHOW_MESSAGE;
	}
	
	@SkipValidation
	public String showAvvisoNuovoMessaggio() {
		return SUCCESS;
	}
	
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
			String html = null;
			if (messaggio != null && messaggio.getMessaggioEsteso() != null) {
				html = messaggioRivendita.getMessaggio();
			} else if (messaggio != null && !Strings.isNullOrEmpty(messaggio.getMessaggio())) {
				html = StringUtility.escapeHTML(messaggio.getMessaggio(), false);
			}
			messaggioRivendita.setMessaggioEscape(html);
			messaggio.setMessaggioEscape(html);
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
	
	@BreadCrumb("%{crumbNameNewMes}")
	@SkipValidation
	public String newMessage() {
		tableTitle = getText("igeriv.nuovo.messaggio");
		MessaggioPk pk = new MessaggioPk();
		pk.setCodFiegDl(getAuthUser().getId());
		pk.setDtMessaggio(new Timestamp(new Date().getTime()));
		messaggio = new MessaggioVo();
		messaggio.setPk(pk);
		disableForm = false;
		return IGerivConstants.ACTION_NEW_MESSAGE;
	}
	
	public String saveMessageEdicola() {
		if (messaggioNonLettoDto != null && messaggioNonLettoDto.getCategoria() != null && messaggioNonLettoDto.getCategoria().equals(IGerivConstants.CATEGORIA_MESSAGGIO_AVVISO_RILEVAMENTI)) {
			return null;
		}
		ControlloLetturaMessaggioVo vo = new ControlloLetturaMessaggioVo();
		ControlloLetturaMessaggioPk pk = new ControlloLetturaMessaggioPk();
		pk.setCodFiegDl(messaggioRivendita.getCodFiegDl());
		pk.setCodiceEdicola(getAuthUser().getId());
		pk.setDtMessaggio(messaggioRivendita.getDtMessaggio());
		vo.setPk(pk);
		vo.setMessaggioLetto(IGerivConstants.COD_MESSAGGIO_LETTO);
		messaggiService.saveBaseVo(vo);
		return null;
	}
	
	public String saveMessage() throws IOException {
		if (messaggio != null) {
			messaggio.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_NUOVO);
			saveMessaggio();
		}
		return IGerivConstants.REDIRECT;
	}

	public String saveAndSendMessage() throws IOException {
		if (messaggio != null) {
			messaggio.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_INVIATO);
			saveMessaggio();
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
	
	@BreadCrumb("%{crumbNameInvMesDl}")
	public String showEmailRivendita() {
		destinatari = (sessionMap.get(IGerivConstants.SESSION_VAR_EMAIL_DL_LIST) == null || ((List)sessionMap.get(IGerivConstants.SESSION_VAR_EMAIL_DL_LIST)).isEmpty()) ? getAuthUser().getRagioneSocialeDl() : null;
		requestMap.put("emailValido", getAuthUser().isEmailValido());
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameEmailInv}")
	public String showEmailInviatiFilter() {
		filterTitle = getText("igeriv.msg.inviati");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameEmailInv}")
	public String showEmailInviati() throws ParseException {
		filterTitle = getText("igeriv.msg.inviati");
		dataDa = DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH);
		dataA = DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH);
		emailsRivendita = messaggiService.getEmailInviatiDaRivendita(getAuthUser().getId(), DateUtilities.floorDay(dataDa), DateUtilities.ceilDay(dataA));
		requestMap.put("messaggi", emailsRivendita);
		requestMap.put("codRivendita", getAuthUser().getId());
		return SUCCESS;
	} 
	
	@SkipValidation
	public String showMessageInviatoDaRivendita() {
		if (idEmailRivendita != null) {
			messaggioInviatoDaRivendita = messaggiService.getMessaggioInviatoDaRivendita(idEmailRivendita);
			messaggioInviatoDaRivendita.setMessaggioEscape(StringUtility.escapeHTML(messaggioInviatoDaRivendita.getMessaggio(), false));
			String uploadDir = edicolaUploadPathDir + System.getProperty("file.separator") + getAuthUser().getId();
			requestMap.put("codRivendita", getAuthUser().getId());
		}
		return IGerivConstants.ACTION_SHOW_MESSAGE;
	}
	
	public String sendEmailRivendita() { 
		tableTitle = getText("igeriv.nuovo.messaggio");
		EmailRivenditaVo voRet = null;
		try {
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
				//emails = Arrays.asList(sessionMap.get(IGerivConstants.SESSION_VAR_EMAIL_DL).toString());
				emails = Arrays.asList(getAuthUser().getEmailDl());
			}
			File out = null; 
			String fileName = null;
			if (attachment1 != null) {
				String uploadDir = edicolaUploadPathDir + System.getProperty("file.separator") + getAuthUser().getId();
				File dirs = new File(uploadDir);
				if (!dirs.isDirectory()) {
					dirs.mkdirs();
				}
				fileName = StringUtility.buildAttachmentFileName(attachment1FileName, getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
				String path = uploadDir + System.getProperty("file.separator") + fileName;
			    out = new File(path);
				FileUtils.copyFile(attachment1, out);
			}
			EmailRivenditaVo vo = new EmailRivenditaVo();
			vo.setDataMessaggio(new Timestamp(new Date().getTime()));
			AnagraficaEdicolaVo edicola = new AnagraficaEdicolaVo();
			edicola.setCodEdicola(getAuthUser().getId());
			vo.setEdicola(edicola);
			String titolo = getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE) ? IGerivMessageBundle.get("gp.titolo.cdl") : IGerivMessageBundle.get("gp.titolo");
			String subject = MessageFormat.format(titolo + IGerivMessageBundle.get("igeriv.messaggio.da.rivendita"), (vo.getIdEmailRivendita() == null ? "" : "<" + vo.getIdEmailRivendita() + ">"), getAuthUser().getCodEdicolaDl(), getAuthUser().getRagioneSocialeEdicola().replaceAll("\\.", " ").replaceAll("\"", "'"));
			vo.setTitolo(subject);
			vo.setMessaggio(message);
			vo.setAllegato(fileName);
			if (destinatari == null || destinatari.isEmpty()) {
				vo.setDestinatari(getAuthUser().getRagioneSocialeDl());
			} else {
				vo.setDestinatari(Joiner.on(",").join(destinatari));
			}
			
			vo.setIsSend(true);
			voRet =  messaggiService.saveBaseVo(vo);
			
			String replyToEmail = null;
			String returnReceiptToAddress = null;
			if (getAuthUser().isEmailReplyToInstantMessages() || getAuthUser().getReturnReceiptTo()) {
				if (getAuthUser().isEmailReplyToInstantMessages()) {
					replyToEmail = emailDlToRivendita;
				}
				if (getAuthUser().getReturnReceiptTo()) {
					returnReceiptToAddress = getReturnReceiptToAddress();
				}
			} else {
				replyToEmail = getAuthUser().getEmail();
			}
			subject = MessageFormat.format(titolo + IGerivMessageBundle.get("igeriv.messaggio.da.rivendita"), (vo.getIdEmailRivendita() == null ? "" : "<" + vo.getIdEmailRivendita() + "><" + getAuthUser().getCodFiegDl() + "|" + getAuthUser().getId() + ">"), getAuthUser().getCodEdicolaDl(), getAuthUser().getRagioneSocialeEdicola().replaceAll("\\.", " ").replaceAll("\"", "'"));
			if (getAuthUser().getReturnReceiptTo()) {
				String params = getAuthUser().getCodFiegDl() + "|" + getAuthUser().getCodEdicolaDl().toString();
				String hash = EncryptUtils.encrypt(EncryptUtils.getEncrypter(IGerivConstants.ENCRYPTION_TOKEN), params).replace("\r\n", "");
				String dlPwd = agenzieService.getPasswordDl(getAuthUser().getCodFiegDl());
				String url = (isCDL ? IGerivConstants.CONTEXT_PATH_CDLONLINE_DL : IGerivConstants.CONTEXT_PATH_IGERIV_DL) + "/" + "confermeLetturaMsgEdicole_execute.action";
				String link = "http://" + request.getServerName() + ":" + request.getServerPort() + url + "?hash=" + URLEncoder.encode(hash, "UTF-8") + "&hash1=" + dlPwd + "&id=" + vo.getIdEmailRivendita();
				String messageLinkToReceipt = MessageFormat.format(getText("msg.email.send.read.receipt.link.body"), link, getAuthUser().getCodEdicolaDl());
				message = message + messageLinkToReceipt;
			}
			
			mailingListService.sendEmailWithAttachment(emails.toArray(new String[]{}), subject, message, out, true, replyToEmail, false, null, returnReceiptToAddress, null);
			
		} catch (Throwable e) {
			log.error(e.getMessage());
			addActionError(getText("gp.errore.imprevisto"));
			voRet.setIsSend(false);
			messaggiService.saveBaseVo(voRet);
			return IGerivConstants.REDIRECT_ERROR;
		}
		return IGerivConstants.REDIRECT;
	}
	
	@SkipValidation
	public String showMessaggioIdtn() {
		messaggioIdtnVo = messaggiService.getMessaggioIdtnVo(idMessaggioIdtn);
		
		return IGerivConstants.ACTION_SHOW_MESSAGE_IDTN;
	}
	
	private void saveMessaggio() throws IOException {
		setMessagePk();
		messaggio.setMessaggio(message);
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
				messaggiService.saveBaseVo(messaggio);
			}
		} else if (zona != null && !zona.equals("")) {
			String[] zone = zona.split(",");
			for (String z : zone) {
				messaggio.getPk().setDestinatarioB(new Integer(z.trim()));
				messaggiService.saveBaseVo(messaggio);
			}
		} else { 
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
			Integer codEdicolaDl = (strCodEdicolaDl != null && !strCodEdicolaDl.equals("")) ? Integer.parseInt(strCodEdicolaDl) : (messaggio.getEdicolaLabel() != null && !messaggio.getEdicolaLabel().equals("") && NumberUtils.isNumber(messaggio.getEdicolaLabel().trim()) ? new Integer(messaggio.getEdicolaLabel().trim()) : null);
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
	
}
