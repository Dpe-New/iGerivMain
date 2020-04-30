package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
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

import it.dpe.igeriv.bo.inventario.InventarioService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.dto.InventarioDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.InventarioVo;
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
@Component("inventarioAction")
@SuppressWarnings({"rawtypes"}) 
public class InventarioAction<T extends BaseDto> extends RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final InventarioService inventarioService;
	private final String crumbName = getText("igeriv.inventario");
	private String strDataInventario;
	private String filterTitle;
	private String tableTitle;
	private List<InventarioDto> listInventarioDto;
	private Date dataInventario;
	private Long idInventario;
	
	public InventarioAction() {
		this.inventarioService = null;
	}
	
	@Autowired
	public InventarioAction(InventarioService inventarioService) {
		this.inventarioService = inventarioService;
	}
	
	@Override
	public void validate() {
		if (!Strings.isNullOrEmpty(strDataInventario)) {
			try {
				dataInventario = new Date(DateUtilities.parseDate(strDataInventario, DateUtilities.FORMATO_DATA_SLASH).getTime());
			} catch (ParseException e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("msg.formato.data.invalido"), strDataInventario) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			List<Date> listDate = extract(listInventarioDto, on(InventarioDto.class).getDataInventario());
			if (listDate.contains(dataInventario)) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("igeriv.error.msg.inventario.gia.esistente") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		} else {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("igeriv.data.inventario")) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		listInventarioDto = inventarioService.getListInventarioDto(getAuthUser().getCodEdicolaMaster());
		filterTitle = getText("igeriv.inventario");
	}
	
	@SkipValidation
	public String showFilter() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String showPopupInventario() {
		return "popup";
	}
	
	public String saveInventario() {
		try {
			InventarioVo vo = new InventarioVo();
			vo.setCodEdicola(getAuthUser().getCodEdicolaMaster());
			vo.setDataInventario(new java.sql.Date(dataInventario.getTime()));
			inventarioService.saveBaseVo(vo);
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}
	
	@SkipValidation
	public String deleteInventario() {
		try {
			inventarioService.deleteInventario(idInventario);
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
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
		return super.getTitle() + getText("igeriv.inventario");
	}
	
}
