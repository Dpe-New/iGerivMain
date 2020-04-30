package it.dpe.igeriv.web.actions;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.dto.FileFatturaDto;
import it.dpe.igeriv.util.DateUtilities;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per i report delle fatture emesse nella pagina delle vendite.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("reportFattureAction")
@SuppressWarnings({"rawtypes"}) 
public class ReportFattureAction<T extends BaseDto> extends RestrictedAccessBaseAction implements State, 
		RequestAware {
	private static final long serialVersionUID = 1L;
	private final ClientiService<FileFatturaDto> clientiService;
	private List<FileFatturaDto> fatture;
	private String filterTitle;
	private String dataDaStr;
	private String dataAStr;
	private Timestamp dataDa;
	private Timestamp dataA;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String piva;
	private String crumbName = getText("igeriv.report.fatture.emesse");
	
	public ReportFattureAction() {
		this.clientiService = null;
	}
	
	@Autowired
	public ReportFattureAction(ClientiService<FileFatturaDto> clientiService) {
		this.clientiService = clientiService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		filterTitle = getText("igeriv.report.fatture.emesse");
		if (Strings.isNullOrEmpty(dataDaStr) || Strings.isNullOrEmpty(dataAStr)) {
			addActionError(getText("error.specificare.data.o.intervallo.date"));
		} else {
			try {
				dataDa = Strings.isNullOrEmpty(dataDaStr) ? null : DateUtilities.floorDay(DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH));
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataDaStr));
				return;
			}
			try {
				dataA = Strings.isNullOrEmpty(dataAStr) ? null : DateUtilities.ceilDay(DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH));
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataAStr));
				return;
			}
		}
	}
	
	@SkipValidation
	public String showFilter() throws Exception {
		filterTitle = getText("igeriv.report.fatture.emesse");
		return SUCCESS;
	}
	
	public String showFatture() throws Exception {
		filterTitle = getText("igeriv.report.fatture.emesse");
		fatture = clientiService.getFattureClienti(getAuthUser().getCodEdicolaMaster(), dataDa, dataA, nome, cognome, codiceFiscale, piva);
		return SUCCESS;
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
	
	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.report.fatture.emesse");
	}
	
}
