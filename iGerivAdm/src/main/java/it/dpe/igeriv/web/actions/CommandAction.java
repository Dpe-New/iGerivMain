package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.jms.out.SendGateway;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivAdmConstants;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.jms.dto.BaseJmsMessage;
import it.dpe.jms.dto.NullJmsMessage;
import it.dpe.service.mail.MailingListService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.Message;
import org.springframework.integration.message.GenericMessage;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action per la gestione dei comandi da inviare al DL.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({"rawtypes"})
@Scope("prototype")
@Component("commandAction")
public class CommandAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	@Autowired
	private MailingListService mailingListService;
	@Autowired
	private SendGateway<NullJmsMessage> sendGateway;
	private String tableTitle;
	private List<EdicolaDto> edicole;
	private EdicolaDto edicola;
	private String tableStyle;
	private String codDl; 
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		if (Strings.isNullOrEmpty(codDl)) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("igeriv.selezionare.dl") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
	}
	
	@SkipValidation
	public String show() {
		tableTitle = getText("igeriv.visualizza.edicole");
		return SUCCESS;
	}
	
	public String esegui() {
		List<NullJmsMessage> listMessage = new ArrayList<NullJmsMessage>();
		listMessage.add(new NullJmsMessage());
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put(JmsConstants.HEADER_KEY_TIPO_MESSAGGIO, JmsConstants.TIPO_MESSAGGIO_COMANDO_ESEGUI_ESPORTAZIONE_PRODOTTI);
		try { 
			String channelName = MessageFormat.format(IGerivAdmConstants.CHANNEL_NAME_TEMPLATE_IGERIV_DL, codDl);
			SpringContextManager.getService(channelName);
			headers.put(JmsConstants.HEADER_KEY_CHANNEL_NAME, channelName);
			headers.put(JmsConstants.HEADER_KEY_TIMEOUT, IGerivAdmConstants.TIMEOUT_JMS_900_SECONDS);		
			Message<List<NullJmsMessage>> msg = new GenericMessage<List<NullJmsMessage>>(listMessage, headers);
			Future<Message<BaseJmsMessage>> send = sendGateway.send(msg);
			Message<BaseJmsMessage> message = send.get(IGerivAdmConstants.TIMEOUT_JMS_900_SECONDS, TimeUnit.SECONDS); 
        	int stato = new Integer(message.getHeaders().get("stato").toString());
        	if (stato == JmsConstants.COD_STATO_IMPORTAZIONE_MESSAGGIO_ERROR || stato == JmsConstants.COD_STATO_IMPORTAZIONE_MESSAGGIO_WARNING) {
    			throw new IGerivRuntimeException(message.getHeaders().get(JmsConstants.HEADER_KEY_DESCRIZIONE_ERRORE) != null ? message.getHeaders().get(JmsConstants.HEADER_KEY_DESCRIZIONE_ERRORE).toString() : (JmsConstants.HEADER_KEY_STACK_TRACE) != null ? message.getHeaders().get(JmsConstants.HEADER_KEY_STACK_TRACE).toString() : "");
        	}
		} catch (NoSuchBeanDefinitionException e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("igeriv.errore.jms.dl.non.collegato"), codDl) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (TimeoutException e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("igeriv.errore.jms.timeout"), codDl) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (IGerivRuntimeException e) {
    		requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + (e.getMessage() != null ? e.getMessage() : StringUtility.getStackTrace(e)) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (Throwable e) {
			String subject = MessageFormat.format(IGerivMessageBundle.get("error.jms.export.prodotti.dl.email.subject"), codDl);
			String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.jms.export.prodotti.dl.email.body"), (e.getMessage() == null ? "" : e.getMessage()), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, subject, emailMsg, true);
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + (e.getMessage() != null ? e.getMessage() : StringUtility.getStackTrace(e)) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
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

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	public String getTableStyle() {
		return tableStyle;
	}

	public void setTableStyle(String tableStyle) {
		this.tableStyle = tableStyle;
	}

	public List<EdicolaDto> getEdicole() {
		return edicole;
	}

	public void setEdicole(List<EdicolaDto> edicole) {
		this.edicole = edicole;
	}
	
	public EdicolaDto getEdicola() {
		return edicola;
	}

	public void setEdicola(EdicolaDto edicola) {
		this.edicola = edicola;
	}

	public String getCodDl() {
		return codDl;
	}

	public void setCodDl(String codDl) {
		this.codDl = codDl;
	}

	public SendGateway<NullJmsMessage> getSendGateway() {
		return sendGateway;
	}

	public void setSendGateway(SendGateway<NullJmsMessage> sendGateway) {
		this.sendGateway = sendGateway;
	}
	
}
