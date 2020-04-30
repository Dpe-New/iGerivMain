package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.DESCENDING;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

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

import it.dpe.igeriv.bo.contabilita.ContabilitaService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.EstrattoContoDto;
import it.dpe.igeriv.dto.VendutoEstrattoContoDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per l'estratto conto delle rivendite.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("estrattoContoAction")
@SuppressWarnings({ "rawtypes" })
public class EstrattoContoAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final ContabilitaService contabilitaService;
	private final EdicoleService edicoleService;
	private final String crumbName = getText("igeriv.visualizza.estratto.conto");
	private List<EstrattoContoDto> listDateEstrattoConto;
	private List<VendutoEstrattoContoDto> listVendutoEstrattoConto;
	private List<EstrattoContoEdicolaDettaglioVo> listEstrattoContoDettaglio;
	private String tableHeight;
	private String tableTitle;
	private String dataEstrattoConto;
	private String actionName;
	private String tableStyle;
	private String EstrattoContoTab_ev;
	private String dataDaStr;
	private String dataAStr;
	
	
	public EstrattoContoAction() {
		this.contabilitaService = null;
		this.edicoleService = null;
	}
	
	@Autowired
	public EstrattoContoAction(ContabilitaService contabilitaService, EdicoleService edicoleService) {
		this.contabilitaService = contabilitaService;
		this.edicoleService = edicoleService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		requestMap.put("mostraGrafico", getText("igeriv.grafico.venduto.estratto.conto"));
	}
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().contains("showVenduto.action")) {
			tableTitle = getText("igeriv.visualizza.venduto.estratto.conto");
			if (dataDaStr == null || dataAStr == null || dataDaStr.equals("") || dataAStr.equals("")) {
				addActionError(getText("error.specificare.data.o.intervallo.date"));
			} else {
				try {
					DateUtilities.floorDay(DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataDaStr));
					return;
				}
				try {
					DateUtilities.ceilDay(DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataAStr));
					return;
				}
			}
		}
	}
	
	@SkipValidation
	public String showFilter() throws Exception {
		tableTitle = getText("igeriv.visualizza.estratto.conto");
		if (getAuthUser().getDataInizioEstrattoContoPdf() == null) {
			listDateEstrattoConto = contabilitaService.getListDateEstrattoConto(getAuthUser().getCodFiegDl(), getAuthUser().getId(), new java.sql.Date(DateUtilities.END_OF_TIME.getTime()));
		} else {
			listDateEstrattoConto = contabilitaService.getListDateEstrattoConto(getAuthUser().getCodFiegDl(), getAuthUser().getId(), getAuthUser().getDataInizioEstrattoContoPdf());
			List<EstrattoContoDto> listDateEstrattoContoPdf = contabilitaService.getListDateEstrattoContoPdf(getAuthUser().getCodFiegDl(), getAuthUser().getId());
			listDateEstrattoConto.addAll(listDateEstrattoContoPdf);
			listDateEstrattoConto = sort(listDateEstrattoConto, on(EstrattoContoDto.class).getDataEstrattoConto(), DESCENDING);
		}
		sessionMap.put("listDateEstrattoConto", listDateEstrattoConto);
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameVendEC}")
	@SkipValidation
	public String showVendutoFilter() {
		tableTitle = getText("igeriv.visualizza.venduto.estratto.conto");
		return SUCCESS;
	}
	
	@SkipValidation
	public String showEstratto() throws ParseException {
		tableTitle = getText("igeriv.visualizza.estratto.conto");
		if (dataEstrattoConto != null) {
			String[] split = dataEstrattoConto.split("\\|");
			String dataEcStr = split[0];
			Integer tipo = new Integer(split[1]);
			Timestamp dtEstrattoConto = DateUtilities.parseDate(dataEcStr, DateUtilities.FORMATO_DATA);
			if (tipo.equals(IGerivConstants.COD_TIPO_ESTRATTO_CONTO_DATI)) {
				listEstrattoContoDettaglio = contabilitaService.getListEstrattoContoDettaglio(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtEstrattoConto);
				EstrattoContoDto estrattoConto = contabilitaService.getEstrattoConto(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtEstrattoConto);
				requestMap.put("listEstrattoContoDettaglio", listEstrattoContoDettaglio);
				if (EstrattoContoTab_ev != null && (EstrattoContoTab_ev.toUpperCase().equals("PDF") || EstrattoContoTab_ev.toUpperCase().equals("XLS"))) {
					setAgenziaEdicolaExportTitle();
				}
				String exportTitle = "";
				if (estrattoConto != null) {
					exportTitle = MessageFormat.format(getText("igeriv.data.estratto.conto.title.pdf"), estrattoConto.getNumEstrattoConto().toString(), estrattoConto.getDataEstrattoContoStr());
					requestMap.put("title", exportTitle.replaceAll("\\.", " "));
					requestMap.put("data", estrattoConto.getDataEstrattoContoStr());
					requestMap.put("numero", estrattoConto.getNumEstrattoConto());
				}
			} else {
				EstrattoContoDto ecpdf = edicoleService.getEstrattoContoEdicolaPdf(getAuthUser().getCodFiegDl(), getAuthUser().getId(), new java.sql.Date(dtEstrattoConto.getTime()));
				requestMap.put("nomeFile", ecpdf != null ? ecpdf.getNomeFile() : null);
			}
			requestMap.put("tipo", tipo);
			
		}
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameVendEC}")
	public String showVenduto() throws ParseException {
		Timestamp dataDa = DateUtilities.floorDay(DateUtilities.parseDate(dataDaStr, DateUtilities.FORMATO_DATA_SLASH));
		Timestamp dataA = DateUtilities.ceilDay(DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH));
		listVendutoEstrattoConto = contabilitaService.getListVendutoEstrattoConto(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), dataDa, dataA);
		requestMap.put("listVendutoEstrattoConto", listVendutoEstrattoConto);
		if (EstrattoContoTab_ev != null && (EstrattoContoTab_ev.toUpperCase().equals("PDF") || EstrattoContoTab_ev.toUpperCase().equals("XLS"))) {
			setAgenziaEdicolaExportTitle();
		}
		String exportTitle = getText("igeriv.visualizza.venduto.estratto.conto") + " " + getText("igeriv.da") + " " + dataDaStr + " " + getText("igeriv.a") + " " + dataAStr;
		requestMap.put("title", exportTitle.replaceAll("\\.", " "));
		return SUCCESS;
	}
	
	@SkipValidation
	public String showVendutoChart() {
		return "vendutoChart";
	}
	
	@Override
	public String getTitle() {
		String title = getText("igeriv.data.estratto.conto");
		if (!Strings.isNullOrEmpty(getActionName())) {
			if (getActionName().contains("venduto_")) {
				title = getText("igeriv.menu.53");
			}
		}
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

	public String getEstrattoContoTab_ev() {
		return EstrattoContoTab_ev;
	}

	public void setEstrattoContoTab_ev(String estrattoContoTab_ev) {
		EstrattoContoTab_ev = estrattoContoTab_ev;
	}
	
}
