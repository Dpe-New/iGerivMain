package it.dpe.igeriv.web.actions;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.arretrati.ArretratiService;
import it.dpe.igeriv.dto.ArretratiDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.IGerivConstants;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per gli arretrati.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("arretratiAction")
public class ArretratiAction extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final String crumbName = getText("conferma.ordine.arretrati");
	private final ArretratiService<ArretratiDto> service;
	private String filterTitle;
	private Map<Integer, Integer> arretratiMap;
	private Map<Integer, String> noteArretratoMap;
	private String titleArretrati;
	private String titleArretratiMessaggio;
	
	public ArretratiAction() {
		this.service = null;
	}
	
	@Autowired
	public ArretratiAction(ArretratiService<ArretratiDto> service) {
		this.service = service;
	}
	
	@Override
	public void prepare() throws Exception {
		titleArretrati = getText("conferma.ordine.arretrati");
		titleArretratiMessaggio = getText("conferma.ordine.arretrati.messaggio");
		super.prepare();
	}
	
	@SkipValidation
	public String showArretrati() {
		List<ArretratiDto> arretrati = service.getArretrati(getAuthUser().getCodFiegDl(), getAuthUser().getCodDpeWebEdicola());
		requestMap.put("arretrati", arretrati);
		requestMap.put("memorizzaInvia", getText("igeriv.memorizza.invia"));
		return SUCCESS;
	}
	
	@SkipValidation
	public String showReportArretrati() {
		titleArretrati = getText("conferma.report.arretrati");
		titleArretratiMessaggio = getText("conferma.ordine.arretrati.messaggio");
		List<ArretratiDto> arretrati = service.getReportArretrati(getAuthUser().getCodFiegDl(), getAuthUser().getCodDpeWebEdicola());
		requestMap.put("arretrati", arretrati);
		return "report";
	}
	
	@SkipValidation
	public String saveArretrati() {
		try {
			service.updateArretrati(arretratiMap, noteArretratoMap);
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}
	
	@Override
	public void saveParameters(Context context, String tableId, @SuppressWarnings("rawtypes") Map parameterMap) {
		
	}

	@Override
	public Map<String, String> getParameters(Context context, String tableId, String stateAttr) {
		return null;
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.menu.84");
	}
	
	
	
	
}
