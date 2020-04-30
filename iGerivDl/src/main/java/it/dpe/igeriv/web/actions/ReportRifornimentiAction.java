package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.util.DateUtilities;

import java.sql.Timestamp;
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

import com.google.common.base.Strings;

@Getter
@Setter
@SuppressWarnings({"rawtypes"}) 
@Scope("prototype")
@Component("reportRifornimentiAction")
public class ReportRifornimentiAction<T extends BaseDto> extends RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final RifornimentiService rifornimentiService;
	private final String crumbName = getText("igeriv.report.richieste.rifornimento");
	private List<RichiestaRifornimentoDto> richiesteRifornimento;
	private String filterTitle;
	private String strDataDa;
	private String strDataA;
	private String codEdicola;
	private String codPubblicazione;
	private String numCopertina;
	private String autocompleteRiv;
	private String autocompletePub;

	public ReportRifornimentiAction() {
		this(null);
	}
	
	@Autowired
	public ReportRifornimentiAction(RifornimentiService rifornimentiService) {
		this.rifornimentiService = rifornimentiService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		filterTitle = getText("igeriv.report.richieste.rifornimento");
		
		if (Strings.isNullOrEmpty(strDataDa) || Strings.isNullOrEmpty(strDataA)) {
			addActionError(getText("error.specificare.intervallo.date"));
		}
		super.validate();
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() throws Exception {
		filterTitle = getText("igeriv.report.richieste.rifornimento");

		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbName}")
	public String showRichiesteRifornimento() throws Exception {
		filterTitle = getText("igeriv.report.richieste.rifornimento");
		Timestamp dataOrdineInizio = DateUtilities.parseDate(strDataDa, DateUtilities.FORMATO_DATA_SLASH);
		Timestamp dataOrdineFine = DateUtilities.parseDate(strDataA, DateUtilities.FORMATO_DATA_SLASH);
		Integer codEdicola = Strings.isNullOrEmpty(this.codEdicola) ? null : new Integer(this.codEdicola);
		Integer codPubblicazione = Strings.isNullOrEmpty(this.codPubblicazione) ? null : new Integer(this.codPubblicazione);
		Integer idtn = Strings.isNullOrEmpty(numCopertina) ? null : new Integer(numCopertina);
		richiesteRifornimento = rifornimentiService.getRichiesteRifornimento(getAuthUser().getCodFiegDl(), dataOrdineInizio, dataOrdineFine, codEdicola, codPubblicazione, idtn);
		return SUCCESS;
	}
	
	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.report.richieste.rifornimento");
	}
	
	/* (non-Javadoc)
	 * @see org.extremecomponents.table.state.State#getParameters(org.extremecomponents.table.context.Context, java.lang.String, java.lang.String)
	 */
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	/* (non-Javadoc)
	 * @see org.extremecomponents.table.state.State#saveParameters(org.extremecomponents.table.context.Context, java.lang.String, java.util.Map)
	 */
	public void saveParameters(Context context, String arg1, Map arg2) {
	}
	
}
