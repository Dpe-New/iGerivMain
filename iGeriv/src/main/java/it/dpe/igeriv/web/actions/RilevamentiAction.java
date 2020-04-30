package it.dpe.igeriv.web.actions;

import java.text.MessageFormat;
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

import it.dpe.igeriv.bo.rilevamenti.RilevamentiService;
import it.dpe.igeriv.dto.RilevamentiDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per le mancanze.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("rilevamentiAction")
public class RilevamentiAction extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final RilevamentiService service;
	private final String crumbName = getText("igeriv.menu.84");
	private String filterTitle;
	private Map<Integer, Integer> rilevamentoMap;
	private String titleRilevamenti;
	
	public RilevamentiAction() {
		this(null);
	}
	
	@Autowired
	public RilevamentiAction(RilevamentiService service) {
		this.service = service;
	}
	
	@Override
	public void prepare() throws Exception {
		titleRilevamenti = MessageFormat.format(getText("rilevamenti.del"), DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH));
		super.prepare();
	}
	
	@SkipValidation
	public String showRilevamenti() {
		List<RilevamentiDto> rilevamenti = service.getRilevamenti(getAuthUser().getCodFiegDl(), getAuthUser().getCodDpeWebEdicola());
		requestMap.put("rilevamenti", rilevamenti);
		requestMap.put("memorizzaInvia", getText("igeriv.memorizza.invia"));
		return SUCCESS;
	}
	
	@SkipValidation
	public String saveRilevamenti() {
		try {
			service.updateRilevamenti(rilevamentoMap, getAuthUser().getCodFiegDl(), getAuthUser().getCodDpeWebEdicola());
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
