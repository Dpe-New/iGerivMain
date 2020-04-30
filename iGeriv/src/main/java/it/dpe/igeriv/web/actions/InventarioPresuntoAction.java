package it.dpe.igeriv.web.actions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import it.dpe.igeriv.bo.inventario.InventarioService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.dto.InventarioPresuntoDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.Types;
import it.dpe.igeriv.util.Types.ContoDepositoType;
import it.dpe.igeriv.util.Types.TipoPubblicazioneType;
import lombok.Getter;
import lombok.Setter;


/**
 * Classe action per la gestione dell'inventario.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("inventarioPresuntoAction")
@SuppressWarnings({"rawtypes"}) 
public class InventarioPresuntoAction<T extends BaseDto> extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final InventarioService inventarioService;
	private final String crumbName = getText("igeriv.inventario.presunto");
	private String strDataInventario;
	private String filterTitle;
	private String tableTitle;
	private List<InventarioPresuntoDto> listInventarioDto;
	private Types.ContoDepositoType filtroContoDeposito; 
	private Types.TipoPubblicazioneType filtroTipoPubblicazioni;
	private Boolean escludiPubblicazioniSenzaPrezzo; 
	
	public InventarioPresuntoAction() {
		this.inventarioService = null;
	}
	
	@Autowired
	public InventarioPresuntoAction(InventarioService inventarioService) {
		this.inventarioService = inventarioService;
	}
	
	@Override
	public void validate() {
		
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		filterTitle = MessageFormat.format(getText("igeriv.inventario.presunto.title"), DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH));
	}
	
	@SkipValidation
	public String showInventarioFilter() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String showInventario() {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.YEAR, -1);
			listInventarioDto = inventarioService.getInventarioPresunto(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), cal.getTime(), getAuthUser().getDataStorico(), getAuthUser().getGruppoSconto(), filtroContoDeposito, filtroTipoPubblicazioni, escludiPubblicazioniSenzaPrezzo);
			String today = DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA);
			requestMap.put("title", MessageFormat.format(getText("igeriv.inventario.presunto.title.pdf"), today));
			requestMap.put("data_inventario", today);
			return SUCCESS;
		} catch (Throwable e) {
			addActionError(getText("gp.errore.imprevisto"));
			return INPUT;
		}
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
		return super.getTitle() + getText("igeriv.inventario.presunto");
	}
	
	public List<ContoDepositoType> getListFiltroContoDeposito() {
		List<ContoDepositoType> list = new ArrayList<ContoDepositoType>();
		list.add(ContoDepositoType.TUTTO);
		list.add(ContoDepositoType.ESCLUDI_CONTO_DEPOSITO);
		list.add(ContoDepositoType.SOLO_CONTO_DEPOSITO);
		return list;
	}
	
	public List<TipoPubblicazioneType> getTipoPubblicazioni() {
		List<TipoPubblicazioneType> list = new ArrayList<TipoPubblicazioneType>();
		list.add(TipoPubblicazioneType.TUTTO);
		list.add(TipoPubblicazioneType.ESCLUDI_PUBBLICAZIONI_SCADUTE);
		list.add(TipoPubblicazioneType.SOLO_PUBBLICAZIONI_SCADUTE);
		return list;
	}

}
