package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.dto.MessaggioDpeDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.BaseVo;
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
@Component("messagesDpeAction")
@SuppressWarnings({ "unused", "rawtypes" })
public class MessagesDpeAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final MessaggiService messaggiService;
	private final AgenzieService agenzieService;
	private final MailingListService mailingListService;
	private String tableHeight;
	private String tableTitle;
	private List<MessaggioDpeDto> messaggi;
	private String actionName;
	private String filterTitle;
	private MessaggioDpeDto messaggio;
	private String dataDaStr;
	private String dataAStr;
	private String abilitati;
	private String codMessaggio;
	private String messageContent;
	
	public MessagesDpeAction() {
		this.messaggiService = null;
		this.agenzieService = null;
		this.mailingListService = null;
	}
	
	@Autowired
	public MessagesDpeAction(MessaggiService messaggiService, AgenzieService agenzieService, MailingListService mailingListService) {
		this.messaggiService = messaggiService;
		this.agenzieService = agenzieService;
		this.mailingListService = mailingListService;
	}
	
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
			if (this.codMessaggio != null && !this.codMessaggio.equals("")) {
				tableTitle = getText("igeriv.aggiorna.messaggio");
			} else {
				tableTitle = getText("igeriv.nuovo.messaggio");
			}
			if (messaggio.getTitolo() == null || messaggio.getTitolo().trim().equals("")) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("igeriv.titolo.vuoto") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			if (messaggio.getTesto() == null || messaggio.getTesto().trim().equals("")) {
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
		messaggi = messaggiService.getMessaggiDpe(dtMessaggioDa, dtMessaggioA, abilitati);
		requestMap.put("messaggi", messaggi);
		return SUCCESS;
	}
	
	public String viewActiveMessages() throws ParseException {
		filterTitle = getText("igeriv.messaggi.dpe");
		showMessages();
		return "viewActiveMessages";
	}
	
	public String viewActiveMessagesIFrame() throws ParseException {
		filterTitle = getText("igeriv.messaggi.dpe");
		showMessages();
		return "viewActiveMessagesIFrame";
	}
	
	@SkipValidation
	public String editMessage() {
		if (this.codMessaggio != null && !this.codMessaggio.equals("")) {
			tableTitle = getText("igeriv.aggiorna.messaggio");
			Long codMessaggio = (this.codMessaggio != null && !this.codMessaggio.equals("")) ? Long.valueOf(this.codMessaggio) : null;
			messaggio = messaggiService.getMessaggioDpe(codMessaggio);
			messageContent = messaggio.getTesto().replaceAll("\\\\'", "'");
			messaggio.setTitolo(messaggio.getTitolo().replaceAll("\\\\'", "'"));
			requestMap.put("messaggio", messaggio);
		} else {
			tableTitle = getText("igeriv.nuovo.messaggio");
			messaggio = new MessaggioDpeDto();
		}
		requestMap.put("newMessage", true);
		return IGerivConstants.ACTION_NEW_MESSAGE;
	}
	
	public String saveMessage() throws IOException {
		if (messaggio != null) {
			messaggio.setData(new Timestamp(new Date().getTime()));
			try {
				boolean isNuovoMessaggio = messaggio.getCodice() == null ? true : false;
				if (messaggio.getTesto() != null) {
					messaggio.setTesto(messaggio.getTesto().replaceAll("\\r|\\n", ""));
					messaggio.setTesto(StringUtility.escapeHTML(messaggio.getTesto(), false));
					messaggio.setTesto(messaggio.getTesto().replaceAll("'", "\\\\'"));
				}
				if (messaggio.getTitolo() != null) {
					messaggio.setTitolo(messaggio.getTitolo().replaceAll("\\r|\\n", ""));
					messaggio.setTitolo(StringUtility.escapeHTML(messaggio.getTitolo(), false));
					messaggio.setTitolo(messaggio.getTitolo().replaceAll("'", "\\\\'"));
				}
				messaggiService.saveMessaggioDpe(messaggio);
				if (messaggio.getInviaNotificaDl()) {
					sendEmailToDL(messaggio.getTitolo(), messaggio.getTesto());
				}
			} catch (Exception e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException(); 
			}
		}
		return IGerivConstants.REDIRECT;
	}

	private void sendEmailToDL(String titolo, String testo) throws MessagingException, UnsupportedEncodingException {
		List<AnagraficaAgenziaVo> agenzie = agenzieService.getAgenzie();
		List<String> emails = extract(agenzie, on(AnagraficaAgenziaVo.class).getEmail());
		String subject = getText("msg.nuovo.messaggio.edicole.subject");
		String message = getText("msg.nuovo.messaggio.edicole.body") + "<br><br><i>Titolo</i>:" + titolo + "<br><i>Messaggio</i>:<br>" + testo;
		mailingListService.sendEmailWithAttachment(emails.toArray(new String[]{}), subject, message, null, true, null, false, null);
	}

	public String deleteMessage() {
		tableTitle = getText("igeriv.nuovo.messaggio");
		if (messaggio != null) {
			messaggiService.deleteMessaggioDpe(messaggio);
		}
		return IGerivConstants.REDIRECT;
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
	
}
