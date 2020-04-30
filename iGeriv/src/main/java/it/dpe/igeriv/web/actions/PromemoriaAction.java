package it.dpe.igeriv.web.actions;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.promemoria.PromemoriaService;
import it.dpe.igeriv.dto.PromemoriaDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione dei promemoria delle edicole.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("promemoriaAction")
@SuppressWarnings({ "rawtypes" })
public class PromemoriaAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final PromemoriaService promemoriaService;
	private final String crumbName = getText("igeriv.gestione.promemoria");
	private String tableHeight;
	private String tableTitle;
	private List<PromemoriaDto> listPromemoria;
	private String actionName;
	private String filterTitle;
	private String reportTitle;
	private PromemoriaDto promemoria;
	private String emailsToSend;
	private String dataDaStr;
	private String dataAStr;
	private Timestamp dataDa;
	private Timestamp dataA;
	private Long codPromemoria;
	private boolean disableForm;
	private String messageContent;
	
	public PromemoriaAction() {
		this.promemoriaService = null;
	}
	
	@Autowired
	public PromemoriaAction(PromemoriaService promemoriaService) {
		this.promemoriaService = promemoriaService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		filterTitle = getText("igeriv.gestione.promemoria");
		if (Strings.isNullOrEmpty(dataDaStr) || Strings.isNullOrEmpty(dataAStr)) {
			dataDaStr = DateUtilities.getTimestampAsString(DateUtilities.togliGiorni(new Timestamp(new Date().getTime()), 15), DateUtilities.FORMATO_DATA_SLASH);
			dataAStr = DateUtilities.getTimestampAsString(new Timestamp(new Date().getTime()), DateUtilities.FORMATO_DATA_SLASH);
		} else if (!Strings.isNullOrEmpty(dataDaStr) && !Strings.isNullOrEmpty(dataAStr)) {
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
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() {
		filterTitle = getText("igeriv.gestione.promemoria");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbName}")
	public String showListPromemoria() {
		filterTitle = getText("igeriv.gestione.promemoria");
		try {
			java.sql.Date dtMessaggioDa = (dataDaStr != null && !dataDaStr.equals("")) ? new java.sql.Date(DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH).getTime()) : null;
			java.sql.Date dtMessaggioA = (dataAStr != null && !dataAStr.equals("")) ? new java.sql.Date(DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH).getTime()) : null;
			listPromemoria = promemoriaService.getListPromemoria(getAuthUser().getCodEdicolaMaster(), dtMessaggioDa, dtMessaggioA);
			requestMap.put("listPromemoria", listPromemoria);
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String showPromemoria() {
		tableTitle = getText("igeriv.nuovo.promemoria");
		try {
			if (codPromemoria != null) {
				promemoria = promemoriaService.getPromemoria(codPromemoria);
				promemoria.setMessaggioEscape((promemoria.getMessaggio() != null && !promemoria.getMessaggio().equals("")) ? StringUtility.escapeHTML(promemoria.getMessaggio(), false) : "");
				requestMap.put("promemoria", promemoria);
				messageContent = promemoria.getMessaggio().replaceAll("\\\\'", "'");
			}
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
		return "showPromemoria";
	}
	
	@SkipValidation
	public String showFirstPromemoria() {
		tableTitle = getText("igeriv.nuovo.promemoria");
		try {
			promemoria = promemoriaService.getFirstPromemoria(getAuthUser().getCodEdicolaMaster());
			promemoria.setMessaggioEscape((promemoria.getMessaggio() != null && !promemoria.getMessaggio().equals("")) ? StringUtility.escapeHTML(promemoria.getMessaggio(), false) : "");
			requestMap.put("promemoria", promemoria);
			messageContent = promemoria.getMessaggio().replaceAll("\\\\'", "'");
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "showPromemoria";
	}
	
	@SkipValidation
	public String newPromemoria() {
		tableTitle = getText("igeriv.nuovo.messaggio");
		promemoria = new PromemoriaDto();
		return IGerivConstants.ACTION_NEW_MESSAGE;
	}
	
	@SkipValidation
	public String savePromemoria() throws IOException {
		try {
			if (promemoria != null) {
				promemoria.setCodEdicola(getAuthUser().getCodEdicolaMaster());
				promemoriaService.savePromemoria(promemoria);
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}

	@SkipValidation
	public String deletePromemoria() {
		tableTitle = getText("igeriv.nuovo.messaggio");
		try {
			if (promemoria != null) {
				promemoriaService.deletePromemoria(promemoria);
			}
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
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
