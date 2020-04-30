package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.BaseVo;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action per la gestione delle anagrafiche agenzia
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@SuppressWarnings({"rawtypes"})
@Scope("prototype")
@Component("anagraficaAgenziaAction")
public class AnagraficaAgenziaAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	@Autowired
	private final AgenzieService agenzieService;
	private String tableTitle;
	private List<AnagraficaAgenziaVo> agenzie;
	private AnagraficaAgenziaVo agenzia;
	private Integer codFiegDl;
	private Integer codDpeWebDl;
	private Long codGruppoModuliVo;
	private String ragioneSociale;
	private Boolean isNew;
	private final String crumbName = getText("igeriv.menu.88");
	
	public AnagraficaAgenziaAction() {
		this(null);
	}
	
	@Autowired
	public AnagraficaAgenziaAction(AgenzieService agenzieService) {
		this.agenzieService = agenzieService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().contains("saveAgenzia.action")) {
			if (agenzia.getCodFiegDl() == null) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.required.field") + " " + getText("dpe.login.cod.fieg.dl.code.short") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
			}
			if (agenzia.getCodGruppoModuliVo() == null) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.required.field") + " " + getText("igeriv.params.agenzia.24") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			
//			if (codDpeWebDl == null) {
//				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.required.field") + " " + getText("igeriv.statistiche.utilizzo.codice.dpe.web.dl") + IGerivConstants.END_EXCEPTION_TAG);
//				throw new IGerivRuntimeException();
//			}
		}
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() {
		tableTitle = getText("igeriv.visualizza.agenzie");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showAgenzie() {
		tableTitle = getText("igeriv.visualizza.agenzie");
		agenzie = agenzieService.getAgenzie();
		requestMap.put("agenzie", agenzie);
		return SUCCESS;
	}
	
	@SkipValidation
	public String showAgenzia() {
		tableTitle = getText("igeriv.inserisci.modifica.agenzia");
		if (codFiegDl != null) {
			agenzia = agenzieService.getAgenziaByCodice(codFiegDl);
		} else {
			agenzia = new AnagraficaAgenziaVo();
			agenzia.setCodDpeWebDl(agenzieService.getNewCodDlWeb().intValue());
			isNew = true;
			
		}
		return "editAgenzia";
	}
	
	
	public String saveAgenzia() {
		try {
			if (agenzia != null) {
				if (isNew) {
					agenzieService.insertNewAgenzia(agenzia);
				} else {
					agenzieService.saveBaseVo(agenzia);
				}
			}
		} catch (Exception e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(IGerivMessageBundle.get("msg.importazione.agenzia.errore.fatale"), e.getMessage()) + IGerivConstants.END_EXCEPTION_TAG);
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
		return super.getTitle() + getText("igeriv.visualizza.agenzie");
	}

}
