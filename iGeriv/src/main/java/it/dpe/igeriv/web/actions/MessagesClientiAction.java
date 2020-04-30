package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.EmailTemplateVo;
import it.dpe.igeriv.vo.MessaggioClienteVo;
import it.dpe.service.mail.MailingListService;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione dei messaggi dall'edicola ai clienti.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("messagesClientiAction")
@SuppressWarnings({ "rawtypes" })
public class MessagesClientiAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final MessaggiService messaggiService;
	private final ClientiService<ClienteEdicolaVo> clientiService;
	private final EdicoleService edicoleService;
	private final MailingListService mailingListService;
	private final String edicolaUploadPathDir;
	private final String emailDlToRivendita;
	private final String returnReceiptToAddress;
	private final String crumbNameInviaEmail = getText("igeriv.invia.email.cliente");
	private final String crumbNameEmailInviati = getText("igeriv.email.inviati.clienti");
	private Long codMessaggio;
	private List<ClienteEdicolaDto> clienti;
	private List<MessaggioClienteVo> messaggi;
	private List<Long> destinatari;
	private String dataDaStr;
	private String dataAStr;
	private String filterTitle;
	private MessaggioClienteVo messaggio;
	private File attachment1;
	private String attachment1ContentType;
	private String attachment1FileName;
	private File attachment2;
	private String attachment2ContentType;
	private String attachment2FileName;
	private File attachment3;
	private String attachment3ContentType;
	private String attachment3FileName;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String piva;
	private File fileField;
	private String fileFieldContentType;
	private String fileFieldFileName;
	private String url;
	private EmailTemplateVo template; 
	private Integer codTemplate;
	private Map<String, String> mapTemplates;
	
	public MessagesClientiAction() {
		this.messaggiService = null;
		this.edicoleService = null;
		this.clientiService = null;
		this.mailingListService = null;
		this.edicolaUploadPathDir = null;
		this.emailDlToRivendita = null;
		this.returnReceiptToAddress = null;
	}
	
	@Autowired
	public MessagesClientiAction(MessaggiService messaggiService, EdicoleService edicoleService, ClientiService<ClienteEdicolaVo> clientiService, MailingListService mailingListService,@Value("${edicole.upload.path.dir}") String edicolaUploadPathDir, @Value("${email.dl.to.rivendite}") String emailDlToRivendita, @Value("${email.return.receipt.address}") String returnReceiptToAddress) {
		this.messaggiService = messaggiService;
		this.edicoleService = edicoleService;
		this.clientiService = clientiService;
		this.mailingListService = mailingListService;
		this.edicolaUploadPathDir = edicolaUploadPathDir;
		this.emailDlToRivendita = emailDlToRivendita;
		this.returnReceiptToAddress = returnReceiptToAddress;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().contains("showMessages.action")) {
			filterTitle = getText("igeriv.msg.inviati");
			if (Strings.isNullOrEmpty(dataDaStr) || Strings.isNullOrEmpty(dataAStr)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				dataAStr = DateUtilities.getTimestampAsString(cal.getTime(), DateUtilities.FORMATO_DATA_SLASH);
				cal.add(Calendar.DAY_OF_MONTH, -15);
				dataDaStr = DateUtilities.getTimestampAsString(cal.getTime(), DateUtilities.FORMATO_DATA_SLASH);
			} else {
				try {
					DateUtilities.floorDay(DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataDaStr));
					return;
				}
				try {
					DateUtilities.ceilDay(DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataAStr));
					return;
				}
			}
		}
	}
	
	@BreadCrumb("%{crumbNameEmailInviati}")
	@SkipValidation
	public String showFilter() {
		filterTitle = getText("igeriv.email.inviati.clienti");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameInviaEmail}")
	@SkipValidation
	public String newMessage() {
		filterTitle = getText("igeriv.invia.email.cliente");
		clienti = select(clientiService.getClientiEdicola(getAuthUser().getArrId(), null, null, null, null), having(on(ClienteEdicolaDto.class).getEmail(), notNullValue()));
		messaggio = new MessaggioClienteVo();
		return SUCCESS;
	}
	
	@SkipValidation
	public String saveMessage() {
		try {
			if (messaggio != null) {
				messaggio.setCodEdicola(getAuthUser().getCodEdicolaMaster());
				messaggio.setDataMessaggio(messaggiService.getSysdate());
				String uploadDir = edicolaUploadPathDir + System.getProperty("file.separator") + getAuthUser().getCodEdicolaMaster();
				File uploadfileDir = new File(uploadDir);
				if (!uploadfileDir.exists()) {
					uploadfileDir.mkdir();
				}
				List<File> attachments = new ArrayList<File>();
				if (attachment1 != null) {
					String name = StringUtility.buildAttachmentFileName(attachment1FileName, getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
					String path = uploadDir + System.getProperty("file.separator") + name;
				    File out = new File(path);
					FileUtils.copyFile(attachment1, out);
					messaggio.setAllegato1(name);
					attachments.add(out);
				}
				if (attachment2 != null) {
					String name = StringUtility.buildAttachmentFileName(attachment2FileName, getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
					String path = uploadDir + System.getProperty("file.separator") + name;
				    File out = new File(path);
					FileUtils.copyFile(attachment2, out);
					messaggio.setAllegato2(name);
					attachments.add(out);
				}
				if (attachment3 != null) {
					String name = StringUtility.buildAttachmentFileName(attachment3FileName, getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
					String path = uploadDir + System.getProperty("file.separator") + name;
				    File out = new File(path);
					FileUtils.copyFile(attachment3, out);
					messaggio.setAllegato3(name);
					attachments.add(out);
				}
				boolean isNew = messaggio != null && messaggio.getCodice() == null;
				List<ClienteEdicolaVo> clienti = clientiService.getClientiEdicola(new Integer[]{getAuthUser().getCodEdicolaMaster()}, destinatari);
				for (ClienteEdicolaVo cli : clienti) {
					try {
						messaggio.setCodCliente(cli.getCodCliente());
						messaggiService.saveBaseVo(messaggio);
						mailingListService.sendEmailWithAttachments(new String[]{cli.getEmail()}, messaggio.getOggetto(), messaggio.getMessaggio(), attachments.toArray(new File[]{}), true, getAuthUser().getEmail(), false, getAuthUser().getRagioneSocialeEdicola(), null, getAuthUser().getRagioneSocialeEdicola());
					} catch (Throwable e) {
						if (isNew && messaggio.getCodice() != null) {
							messaggiService.deleteVo(messaggio);
						}
						throw e;
					}
				}
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}
	
	@BreadCrumb("%{crumbNameEmailInviati}")
	public String showMessages() {
		filterTitle = getText("igeriv.email.inviati.clienti");
		try {
			Timestamp dataDa = DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH);
			Timestamp dataA = DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH);
			messaggi = messaggiService.getMessaggiCliente(getAuthUser().getCodEdicolaMaster(), dataDa, dataA, nome, cognome, codiceFiscale, piva);
			requestMap.put("messaggi", messaggi);
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String showMessage() {
		filterTitle = getText("igeriv.aggiorna.messaggio");
		messaggio = messaggiService.getMessaggioCliente(codMessaggio);
		return IGerivConstants.ACTION_SHOW_MESSAGE;
	}
	
	public String uploadImage() throws IOException {
		String uploadDir = edicolaUploadPathDir + System.getProperty("file.separator") + getAuthUser().getCodEdicolaMaster();
		File uploadfileDir = new File(uploadDir);
		if (!uploadfileDir.exists()) {
			uploadfileDir.mkdir();
		}
		if (fileField != null) {
			String fileName = StringUtility.buildAttachmentFileName(fileFieldFileName, getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
			String path = uploadDir + System.getProperty("file.separator") + fileName;
		    File out = new File(path);
			FileUtils.copyFile(fileField, out);
			url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/uploads_ed/" + getAuthUser().getCodEdicolaMaster() + "/" + fileName;
		}
		return "uploadResult";
	}
	
	@SkipValidation
	public String saveEmailTemplateMessage() {
		mapTemplates = new HashMap<String, String>();
		try {
			if (template != null) {
				template.setCodEdicola(getAuthUser().getCodEdicolaMaster());
				messaggiService.saveBaseVo(template);
				mapTemplates.put("success", "true");
			}
		} catch (Throwable e) {
			mapTemplates.put("error", getText("gp.errore.imprevisto"));
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String getEmailTemplate() {
		mapTemplates = new HashMap<String, String>();
		try {
			if (codTemplate != null) {
				template = clientiService.getEmailTemplate(codTemplate);
				mapTemplates.put("template", template.getContenuto());
			}
		} catch (Throwable e) {
			mapTemplates.put("error", getText("gp.errore.imprevisto"));
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String getEmailTemplates() {
		mapTemplates = new HashMap<String, String>();
		try {
			for (EmailTemplateVo vo : getTemplates()) {
				mapTemplates.put(vo.getCodice().toString(), vo.getNome());
			}
		} catch (Throwable e) {
			mapTemplates.put("error", getText("gp.errore.imprevisto"));
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String deleteEmailTemplate() {
		mapTemplates = new HashMap<String, String>();
		try {
			template = clientiService.getEmailTemplate(codTemplate);
			messaggiService.deleteVo(template);
			mapTemplates.put("success", "true");
		} catch (Throwable e) {
			mapTemplates.put("error", getText("gp.errore.imprevisto"));
		}
		return SUCCESS;
	}
	
	public List<EmailTemplateVo> getTemplates() {
		return clientiService.getEmailTemplates(getAuthUser().getCodEdicolaMaster());
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
		return super.getTitle() + getText("");
	}
	
}
