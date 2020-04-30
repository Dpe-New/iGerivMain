package it.dpe.igeriv.web.actions;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.callback.LimitCallback;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.web.extremecomponents.limit.InvendutiLimitCallback;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per il report delle testate arrivate che non si vendono.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("pubblicazioniInveduteAction")
@SuppressWarnings({ "rawtypes" })
public class PubblicazioniInveduteAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final PubblicazioniService pubblicazioniService;
	private final String crumbName = getText("igeriv.pubblicazioni.invedute");
	private List<PubblicazioneDto> listPubblicazioni;
	private String tableTitle;
	private String actionName;
	private String numMesiDataUscitaDaInventario;
	private String titolo;
	private String escludiQuotidiani;
	private String escludiCD;
	private List<KeyValueDto> listEstrattiConto;
	private Integer baseCalcolo;
	private Integer PubblicazioniTab_p;
	private Integer PubblicazioniTab_rd;
	
	public PubblicazioniInveduteAction() {
		this.pubblicazioniService = null;
	}
	
	@Autowired
	public PubblicazioniInveduteAction(PubblicazioniService pubblicazioniService) {
		this.pubblicazioniService = pubblicazioniService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
	}
	
	@SkipValidation
	public String showFilter() {
		tableTitle = getText("igeriv.pubblicazioni.invedute");
		escludiQuotidiani = "true";
		escludiCD = "true";
		return SUCCESS;
	} 
	
	public String show() throws ParseException {
		tableTitle = getText("igeriv.pubblicazioni.invedute");
		Timestamp sysdate = pubblicazioniService.getSysdate();
		Timestamp dataDa = DateUtilities.floorDay(DateUtilities.aggiungiMesi(sysdate, -new Integer(numMesiDataUscitaDaInventario)));
		Timestamp dataA = DateUtilities.ceilDay(sysdate);
		Boolean escludiQuotidiani = Strings.isNullOrEmpty(this.escludiQuotidiani) ? true : Boolean.valueOf(this.escludiQuotidiani);
		Boolean escludiCD = Strings.isNullOrEmpty(this.escludiCD) ? true : Boolean.valueOf(this.escludiCD);
		PubblicazioniTab_p = PubblicazioniTab_p == null ? 1 : PubblicazioniTab_p;
		PubblicazioniTab_rd = PubblicazioniTab_rd == null ? InvendutiLimitCallback.MAX_PAGE_ROWS : PubblicazioniTab_rd; 
		listPubblicazioni = pubblicazioniService.getPubblicazioniInvendute(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), titolo, dataDa, dataA, getAuthUser().getGruppoSconto(), escludiQuotidiani, escludiCD, baseCalcolo, PubblicazioniTab_p, PubblicazioniTab_rd);
		requestMap.put("listPubblicazioni", listPubblicazioni);
		requestMap.put("pageRows", InvendutiLimitCallback.MAX_PAGE_ROWS);
		requestMap.put(LimitCallback.TOTAL_ROWS, pubblicazioniService.getCountPubblicazioniInvendute(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), titolo, dataDa, dataA, getAuthUser().getGruppoSconto(), escludiQuotidiani, escludiCD, baseCalcolo));
		return SUCCESS;
	}
	
	@Override
	public String getTitle() {
		String title = getText("igeriv.pubblicazioni.invedute");
		return super.getTitle() + title;
	}

	/* (non-Javadoc)
	 * @see org.extremecomponents.table.state.State#getParameters(org.extremecomponents.table.context.Context, java.lang.String, java.lang.String)
	 */
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}
	
	public void saveParameters(Context context, String arg1, Map arg2) {
		
	}

	public List<KeyValueDto> getListBaseCalcolo() {
		List<KeyValueDto> list = new ArrayList<KeyValueDto>();
		KeyValueDto dto = new KeyValueDto();
		dto.setKeyInt(IGerivConstants.BASE_CALCOLO_VENDITE);
		dto.setValue(IGerivMessageBundle.get("igeriv.basato.su.vendite"));
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKeyInt(IGerivConstants.BASE_CALCOLO_RESE);
		dto1.setValue(IGerivMessageBundle.get("igeriv.basato.su.rese"));
		list.add(dto);
		list.add(dto1);
		return list;
	}

}
