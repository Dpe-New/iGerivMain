package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.dto.MessaggioDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.BaseVo;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

/**
 * Classe action per la gestione dei messaggi del sistema da parte del DL.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({ "unused", "rawtypes"})
@Scope("prototype")
@Component("messagesDpeRivenditeAction")
public class MessagesDpeRivenditeAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	@Autowired
	private MessaggiService messaggiService;
	private String tableHeight;
	private String tableTitle;
	private List<MessaggioDto> messaggi;
	private String actionName;
	private String filterTitle;
	private MessaggioDto messaggio;
	private String dataDaStr;
	private String dataAStr;
	private String abilitati;
	private String pk;
	private String messageContent;
	private File attachment1;
	private String attachment1ContentType;
	private String attachment1FileName;
	private String message;
	private String uploadPathDir;
	private String edicolaLabel;
	private Integer destinatario;
	private String strCodEdicolaDl;
	private String coddl;
	private boolean disableForm;
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().contains("showMessages.action")) {
			filterTitle = getText("igeriv.messaggi.dpe");
			if (dataDaStr != null && dataAStr != null && !dataDaStr.equals("") && !dataAStr.equals("")) {
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
		} else if (getActionName() != null && getActionName().contains("saveMessage.action")) {
			if (this.pk != null && !this.pk.equals("")) {
				tableTitle = getText("igeriv.aggiorna.messaggio");
			} else {
				tableTitle = getText("igeriv.nuovo.messaggio");
			}
			if (messaggio.getMessaggio() == null || messaggio.getMessaggio().trim().equals("")) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("igeriv.email.vuoto") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
	}
	
	@SkipValidation
	public String showMessagesFilter() {
		filterTitle = getText("igeriv.messaggi.dpe");
		return SUCCESS;
	}
	
	public String showMessages() throws ParseException {
		filterTitle = getText("igeriv.messaggi.dpe");
		Timestamp dtMessaggioDa = (dataDaStr != null && !dataDaStr.equals("")) ? DateUtilities.floorDay(DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH)) : null;
		Timestamp dtMessaggioA = (dataAStr != null && !dataAStr.equals("")) ? DateUtilities.ceilDay(DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH)) : null;
		Boolean abilitati = (this.abilitati != null) ? Boolean.parseBoolean(this.abilitati) : null;
		messaggi = messaggiService.getMessaggiRivendite(null, dtMessaggioDa, dtMessaggioA, null, false);
		Group<MessaggioDto> group = group(messaggi, by(on(MessaggioDto.class).getDtMessaggio()));
		messaggi.clear();
		for (Group<MessaggioDto> subgroup : group.subgroups()) {
			messaggi.add(subgroup.findAll().get(0));
		}
		requestMap.put("messaggi", messaggi);
		return SUCCESS;
	}
	
	@SkipValidation
	public String editMessage() throws ParseException {
		if (this.pk != null && !this.pk.equals("")) {
			tableTitle = getText("igeriv.aggiorna.messaggio");
			StringTokenizer st = new StringTokenizer(pk, "|");
			Integer codFiegDl = Integer.parseInt(st.nextToken());
			Timestamp dtMessaggio = DateUtilities.parseDate(st.nextToken(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
			Integer tipoDestinatario = Integer.parseInt(st.nextToken());
			Integer destinatarioA = Integer.parseInt(st.nextToken());
			Integer destinatarioB = Integer.parseInt(st.nextToken());
			messaggio = messaggiService.getMessaggioRivendita(destinatarioA, codFiegDl, dtMessaggio, tipoDestinatario, destinatarioA, destinatarioB);
			messaggio.setMessaggioEscape(StringUtility.escapeHTML(messaggio.getMessaggio() != null ? messaggio.getMessaggio() : "", false));
			strCodEdicolaDl = messaggio.getDestinatarioA().toString();
			messageContent = messaggio.getMessaggio();
			requestMap.put("messaggio", messaggio);
			disableForm = (messaggio.getStatoMessaggio() != null && messaggio.getStatoMessaggio().equals(IGerivConstants.STATO_MESSAGGIO_INVIATO) ? true : false);
		} else {
			tableTitle = getText("igeriv.nuovo.messaggio");
			messaggio = new MessaggioDto();
			disableForm = false;
		}
		requestMap.put("newMessage", true);
		return IGerivConstants.ACTION_NEW_MESSAGE;
	}
	
	public String saveMessage() throws IOException {
		if (messaggio != null) {
			if (messaggio.getMessaggio() != null) {
				messaggio.setMessaggio(messaggio.getMessaggio().replaceAll("\\r|\\n", ""));
			}
			messaggio.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_NUOVO);
			saveMessaggio();
		}
		return SUCCESS;
	}

	public String saveAndSendMessage() throws IOException {
		if (messaggio != null) {
			messaggio.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_INVIATO);
			saveMessaggio();
		}
		return SUCCESS; 
	}

	public String deleteMessage() {
		tableTitle = getText("igeriv.nuovo.messaggio");
		if (messaggio != null) {
			messaggiService.deleteMessaggioRivendita(messaggio);
		}
		return SUCCESS;
	}
	
	private void saveMessaggio() throws IOException {
		String uploadDir = uploadPathDir + "/" + IGerivConstants.COD_DPE;
		File uploadfileDir = new File(uploadDir);
		if (!uploadfileDir.exists()) {
			uploadfileDir.mkdir();
		}
		if (attachment1 != null) {
			String path = uploadDir + "/" + attachment1FileName;
		    File out = new File(path);
			FileUtils.copyFile(attachment1, out);
			messaggio.setAttachmentName1(attachment1FileName);
		}
		if (edicolaLabel != null) {
			messaggio.setEdicolaLabel(edicolaLabel);
		}
		int tipoDestinatario = (destinatario == null) ? 0 : new Integer(destinatario);
		messaggio.setTipoDestinatario(tipoDestinatario);
		messaggio.setCodFiegDl((coddl != null && !coddl.equals("")) ? new Integer(coddl) : null);
		messaggio.setTipoMessaggio(IGerivConstants.TIPO_MESSAGGIO_IMMEDIATO);
		messaggiService.saveMessaggioRivenditaDpe(messaggio);
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
		return super.getTitle() + getText("igeriv.visualizza.edicole");
	}

	public String getTableHeight() {
		return tableHeight;
	}

	public void setTableHeight(String tableHeight) {
		this.tableHeight = tableHeight;
	}
	
	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	public String getFilterTitle() {
		return filterTitle;
	}

	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}

	public MessaggiService getMessaggiService() {
		return messaggiService;
	}

	public void setMessaggiService(MessaggiService messaggiService) {
		this.messaggiService = messaggiService;
	}

	public List<MessaggioDto> getMessaggi() {
		return messaggi;
	}

	public void setMessaggi(List<MessaggioDto> messaggi) {
		this.messaggi = messaggi;
	}

	public MessaggioDto getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(MessaggioDto messaggio) {
		this.messaggio = messaggio;
	}

	public String getDataDaStr() {
		return dataDaStr;
	}

	public void setDataDaStr(String dataDaStr) {
		this.dataDaStr = dataDaStr;
	}

	public String getDataAStr() {
		return dataAStr;
	}

	public void setDataAStr(String dataAStr) {
		this.dataAStr = dataAStr;
	}

	public String getAbilitati() {
		return abilitati;
	}

	public void setAbilitati(String abilitati) {
		this.abilitati = abilitati;
	}
	
	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public File getAttachment1() {
		return attachment1;
	}

	public void setAttachment1(File attachment1) {
		this.attachment1 = attachment1;
	}

	public String getAttachment1ContentType() {
		return attachment1ContentType;
	}

	public void setAttachment1ContentType(String attachment1ContentType) {
		this.attachment1ContentType = attachment1ContentType;
	}

	public String getAttachment1FileName() {
		return attachment1FileName;
	}

	public void setAttachment1FileName(String attachment1FileName) {
		this.attachment1FileName = attachment1FileName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUploadPathDir() {
		return uploadPathDir;
	}

	public void setUploadPathDir(String uploadPathDir) {
		this.uploadPathDir = uploadPathDir;
	}

	public String getEdicolaLabel() {
		return edicolaLabel;
	}

	public void setEdicolaLabel(String edicolaLabel) {
		this.edicolaLabel = edicolaLabel;
	}

	public Integer getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Integer destinatario) {
		this.destinatario = destinatario;
	}

	public String getStrCodEdicolaDl() {
		return strCodEdicolaDl;
	}

	public void setStrCodEdicolaDl(String strCodEdicolaDl) {
		this.strCodEdicolaDl = strCodEdicolaDl;
	}

	public String getCoddl() {
		return coddl;
	}

	public void setCoddl(String coddl) {
		this.coddl = coddl;
	}
	
	public boolean isDisableForm() {
		return disableForm;
	}

	public void setDisableForm(boolean disableForm) {
		this.disableForm = disableForm;
	}
}	
