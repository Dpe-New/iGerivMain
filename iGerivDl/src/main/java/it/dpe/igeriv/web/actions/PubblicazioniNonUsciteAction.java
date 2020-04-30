package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.dto.BollaRiassuntoDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BollaVo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
@Scope("prototype")
@Component("pubblicazioniNonUsciteAction")
@SuppressWarnings({ "rawtypes" })
public class PubblicazioniNonUsciteAction <T extends BaseDto> extends RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final BolleService bolleService;
	private final String crumbName = getText("igeriv.menu.87");
	private String filterTitle;
	private String dataTipoBolla;
	private static final String ITEMS_NON_USCITE = "itemsNonUscite";
	private String pk;
	private String selectedDataTipoBolla;
	
	public PubblicazioniNonUsciteAction() {
		this.bolleService = null;
	}
	
	@Autowired
	public PubblicazioniNonUsciteAction(BolleService bolleService, RifornimentiService rifornimentiService, MessaggiService messaggiService) {
		this.bolleService = bolleService;
	}
	
	@Override
	public void validate() {
		
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		filterTitle = getText("igeriv.menu.87");
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() throws Exception {
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showElencoPubblicazioni() throws Exception
	{
		if (!Strings.isNullOrEmpty(dataTipoBolla) || !Strings.isNullOrEmpty(selectedDataTipoBolla)) {
			//è il valore della combo che sul filter è in dataTipoBolla mentre dopo  "Ricerca"è in selectedDataTipoBolla
			// questo metodo viene richiamato anche sull'ordinamento delle colonne
			selectedDataTipoBolla = dataTipoBolla != null ? dataTipoBolla : selectedDataTipoBolla;
			StringTokenizer st = new StringTokenizer(selectedDataTipoBolla, "|");
			String strData = st.nextToken();
			String tipo = st.nextToken();
			
			Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS);
			Integer codFiegDl = getAuthUser().getCodFiegDl();
			List<BollaVo> itemsBolla = bolleService.getDettaglioBolla(codFiegDl,tipo, dtBolla); 
			requestMap.put(ITEMS_NON_USCITE, itemsBolla);
		}
		
		return SUCCESS;
	}	
	
	@SkipValidation
	public String save()
	{
		try {
			bolleService.updateNonUscite(pk, selectedDataTipoBolla, getAuthUser().getCodFiegDl());
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}
	
	@SkipValidation
	public List<BollaRiassuntoDto> getListaBolle()
	{
		Integer codFiegDl = getAuthUser().getCodFiegDl();
		return bolleService.getListaBolle(codFiegDl,50);		
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
		return super.getTitle() + getText("igeriv.menu.87");
	}
}

