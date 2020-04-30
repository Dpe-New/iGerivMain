package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.jms.out.SendGateway;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivDlConstants;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
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

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.Message;
import org.springframework.integration.message.GenericMessage;
import org.springframework.stereotype.Component;

/**
 * Classe per la configurazione dei parametri iGeriv dell'edicola
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("paramDlAction")
@SuppressWarnings({ "rawtypes" })
public class ParametriDlAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final AgenzieService agenzieService;
	private final MailingListService mailingListService;
	private final SendGateway sendGateway;
	private final String crumbName = getText("igeriv.menu.37");
	private AnagraficaAgenziaVo agenzia;
	private Boolean hasInterfacciaJmsProdottiVari;
	
	public ParametriDlAction() {
		this(null,null,null);	
	}
	
	@Autowired
	public ParametriDlAction(AgenzieService agenzieService, MailingListService mailingListService, SendGateway sendGateway) {
		this.agenzieService = agenzieService;
		this.mailingListService = mailingListService;
		this.sendGateway = sendGateway;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		String channelName = MessageFormat.format(IGerivDlConstants.CHANNEL_NAME_TEMPLATE_IGERIV_DL, getAuthUser().getCodFiegDl());
		hasInterfacciaJmsProdottiVari = true;
		try {
			SpringContextManager.getService(channelName);
		} catch (NoSuchBeanDefinitionException e) {
			hasInterfacciaJmsProdottiVari = false; 
		}
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showParams() {
		agenzia = agenzieService.getAgenziaByCodice(getAuthUser().getCodFiegDl());
		return SUCCESS;
	}
	
	public String esegui() {
		List<NullJmsMessage> listMessage = new ArrayList<NullJmsMessage>();
		listMessage.add(new NullJmsMessage());
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put(JmsConstants.HEADER_KEY_TIPO_MESSAGGIO, JmsConstants.TIPO_MESSAGGIO_COMANDO_ESEGUI_ESPORTAZIONE_PRODOTTI);
		try { 
			String channelName = MessageFormat.format(IGerivDlConstants.CHANNEL_NAME_TEMPLATE_IGERIV_DL, getAuthUser().getCodFiegDl());
			SpringContextManager.getService(channelName);
			headers.put(JmsConstants.HEADER_KEY_CHANNEL_NAME, channelName);
			headers.put(JmsConstants.HEADER_KEY_TIMEOUT, IGerivDlConstants.TIMEOUT_JMS_900_SECONDS);		
			Message<List<NullJmsMessage>> msg = new GenericMessage<List<NullJmsMessage>>(listMessage, headers);
			Future<Message<BaseJmsMessage>> send = sendGateway.send(msg);
			Message<BaseJmsMessage> message = send.get(IGerivDlConstants.TIMEOUT_JMS_900_SECONDS, TimeUnit.SECONDS); 
        	int stato = new Integer(message.getHeaders().get("stato").toString());
        	if (stato == JmsConstants.COD_STATO_IMPORTAZIONE_MESSAGGIO_ERROR || stato == JmsConstants.COD_STATO_IMPORTAZIONE_MESSAGGIO_WARNING) {
        		throw new IGerivRuntimeException(message.getHeaders().get(JmsConstants.HEADER_KEY_DESCRIZIONE_ERRORE) != null ? message.getHeaders().get(JmsConstants.HEADER_KEY_DESCRIZIONE_ERRORE).toString() : (JmsConstants.HEADER_KEY_STACK_TRACE) != null ? message.getHeaders().get(JmsConstants.HEADER_KEY_STACK_TRACE).toString() : "");
        	}
		} catch (NoSuchBeanDefinitionException e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("igeriv.errore.jms.dl.non.collegato"), getAuthUser().getCodFiegDl()) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (TimeoutException e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("igeriv.errore.jms.timeout"), getAuthUser().getCodFiegDl()) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (IGerivRuntimeException e) {
    		requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + (e.getMessage() != null ? e.getMessage() : StringUtility.getStackTrace(e)) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (Throwable e) {
			String subject = MessageFormat.format(IGerivMessageBundle.get("error.jms.export.prodotti.dl.email.subject"), getAuthUser().getCodFiegDl());
			String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.jms.export.prodotti.dl.email.body"), (e.getMessage() == null ? "" : e.getMessage()), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, subject, emailMsg, true);
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + (e.getMessage() != null ? e.getMessage() : StringUtility.getStackTrace(e)) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}
	
	public String saveParam() {
		try {
			if (agenzia != null) {
				AnagraficaAgenziaVo vo = agenzieService.getAgenziaByCodice(getAuthUser().getCodFiegDl());
				setParametriAgenzia(vo);
				agenzieService.saveBaseVo(vo);
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}

	/**
	 * Setta i campi dell'anagrafica
	 * @param vo
	 */
	private void setParametriAgenzia(AnagraficaAgenziaVo vo) {
		if (agenzia != null) {
			vo.setRagioneSocialeDlPrimaRiga(agenzia.getRagioneSocialeDlPrimaRiga());
			vo.setRagioneSocialeDlSecondaRiga(agenzia.getRagioneSocialeDlSecondaRiga());
			vo.setIndirizzoDlPrimaRiga(agenzia.getIndirizzoDlPrimaRiga());
			vo.setIndirizzoDlSecondaRiga(agenzia.getIndirizzoDlSecondaRiga());
			vo.setLocalitaDlPrimaRiga(agenzia.getLocalitaDlPrimaRiga());
			vo.setLocalitaDlSecondaRiga(agenzia.getLocalitaDlSecondaRiga());
			vo.setSiglaProvincia(agenzia.getSiglaProvincia());
			vo.setCap(agenzia.getCap());
			vo.setTelefono(agenzia.getTelefono());
			vo.setFax(agenzia.getFax());
			vo.setEmail(agenzia.getEmail());
			vo.setUrl(agenzia.getUrl());
			vo.setFtpServerGestionaleAddress(agenzia.getFtpServerGestionaleAddress());
			vo.setFtpServerGestionaleUser(agenzia.getFtpServerGestionaleUser());
			vo.setFtpServerGestionalePwd(agenzia.getFtpServerGestionalePwd());
			vo.setFtpServerGestionaleDir(agenzia.getFtpServerGestionaleDir());
			vo.setNumMaxCpuResaDimeticata(agenzia.getNumMaxCpuResaDimeticata());
			vo.setHasButtonCopiaDifferenze(agenzia.getHasButtonCopiaDifferenze());
			vo.setHasResaAnticipata(agenzia.getHasResaAnticipata());
			vo.setVisualizzaSemaforoGiacenza(agenzia.getVisualizzaSemaforoGiacenza());
			vo.setEmailReplyToInstantMessages(agenzia.getEmailReplyToInstantMessages());
			vo.setReturnReceiptTo(agenzia.isReturnReceiptTo());
			vo.setGiornoSettimanaPermessaResaDimenticata(agenzia.getGiornoSettimanaPermessaResaDimenticata());
			vo.setDataUltimaModfica(agenzieService.getSysdate());
			vo.setCodUtenteUltimaModfica(getAuthUser().getId());
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
		return super.getTitle() + getText("igeriv.menu.37");
	}

}
