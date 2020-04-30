package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.libs.com.zwitserloot.cmdreader.Excludes;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action per la menutenzione delle bolle da parte del DL.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@SuppressWarnings({"rawtypes"}) 
@Scope("prototype")
@Component("manutenzioneBolleAction")
public class ManutenzioneBolleAction<T extends BaseDto> extends RestrictedAccessBaseAction implements State, 
		RequestAware {
	private static final long serialVersionUID = 1L;
	private final BolleService bolleService;
	private final EdicoleService edicoleService;
	private final String crumbNameConsegna = getText("igeriv.manutenzione.bolla.consegna");
	private final String crumbNameResa = getText("igeriv.manutenzione.bolla.resa");
	private List bolleRiassunto;
	private String codEdicola;
	private String pk;
	private String stato;
	private String filterTitle;
	private String autocomplete;
	private String strData;
	private String tipoBolla;
	private String statoBolla;
	private HashMap<Integer, String> statoMap;
	private LinkedHashMap<String, String> tipoBollaMap;
	
	public ManutenzioneBolleAction() {
		this(null,null);
	}
	
	@Autowired
	public ManutenzioneBolleAction(BolleService bolleService, EdicoleService edicoleService) {
		this.bolleService = bolleService;
		this.edicoleService = edicoleService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		statoMap = new HashMap<Integer, String>();
		statoMap.put(IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA, getText(IGerivConstants.BOLLA_NON_TRASMESSA));
		statoMap.put(IGerivConstants.INDICATORE_BOLLA_DA_TRASMETTERE, getText(IGerivConstants.BOLLA_DA_TRASMETTERE));
		statoMap.put(IGerivConstants.INDICATORE_BOLLA_IN_TRASMISSIONE, getText(IGerivConstants.BOLLA_IN_TRASMISSIONE));
		statoMap.put(IGerivConstants.INDICATORE_BOLLA_TRASMESSA, getText(IGerivConstants.BOLLA_TRASMESSA));
		tipoBollaMap = new LinkedHashMap<String, String>();
	}
	
	@Override
	public void validate() {
		if (Strings.isNullOrEmpty(autocomplete) && Strings.isNullOrEmpty(strData)) {
			addActionError(getText("error.criterio.ricerca"));
		}
		if (getActionName() != null && getActionName().toUpperCase().contains("BOLLARESA")) {
			filterTitle = getText("igeriv.manutenzione.bolla.resa");
		} else if (getActionName() != null && getActionName().toUpperCase().contains("BOLLACONSEGNA")) {
			filterTitle = getText("igeriv.manutenzione.bolla.consegna");
		}
		super.validate();
	}
	
	@BreadCrumb("%{crumbNameConsegna}")
	@SkipValidation
	public String showFilterBollaConsegna() throws Exception {
		filterTitle = getText("igeriv.manutenzione.bolla.consegna");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameResa}")
	@SkipValidation
	public String showFilterBollaResa() throws Exception {
		filterTitle = getText("igeriv.manutenzione.bolla.resa");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameConsegna}")
	public String showBolleRiassunto() throws Exception {
		filterTitle = getText("igeriv.manutenzione.bolla.consegna");
		
		Timestamp dataBolla = Strings.isNullOrEmpty(strData) ? null : DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA_SLASH);
		String tipoBolla = Strings.isNullOrEmpty(this.tipoBolla) ? null : this.tipoBolla;
		Integer statoBolla = Strings.isNullOrEmpty(this.statoBolla) ? null : new Integer(this.statoBolla);
		if (!Strings.isNullOrEmpty(codEdicola)) {
			AbbinamentoEdicolaDlVo abbinamentoEdicolaDlVoByCodFiegDlCodRivDl = edicoleService.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(getAuthUser().getCodFiegDl(), new Integer(codEdicola));
			if (abbinamentoEdicolaDlVoByCodFiegDlCodRivDl != null) {
				Integer codEdicolaWeb = abbinamentoEdicolaDlVoByCodFiegDlCodRivDl.getCodDpeWebEdicola();
				bolleRiassunto = bolleService.getBolleRiassunto(new Integer[]{getAuthUser().getCodFiegDl()}, new Integer[]{codEdicolaWeb}, dataBolla, tipoBolla, statoBolla, true);
				requestMap.put("bolleRiassunto", bolleRiassunto);
			}
		} else {
			bolleRiassunto = bolleService.getBolleRiassunto(new Integer[]{getAuthUser().getCodFiegDl()}, null, dataBolla, tipoBolla, statoBolla, true);
		}
		requestMap.put("titoloManutenzione", getText("igeriv.manutenzione.bolla.consegna"));
		requestMap.put("edicola", (autocomplete != null) ? autocomplete.replaceAll("&nbsp;", " ").replaceAll("\\.", " ") : "");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameResa}")
	public String showBolleResaRiassunto() throws Exception {
		filterTitle = getText("igeriv.manutenzione.bolla.resa");
		Timestamp dataBolla = Strings.isNullOrEmpty(strData) ? null : DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA_SLASH);
		String tipoBolla = Strings.isNullOrEmpty(this.tipoBolla) ? null : this.tipoBolla;
		Integer statoBolla = Strings.isNullOrEmpty(this.statoBolla) ? null : new Integer(this.statoBolla);
		if (!Strings.isNullOrEmpty(codEdicola)) {
			AbbinamentoEdicolaDlVo abbinamentoEdicolaDlVoByCodFiegDlCodRivDl = edicoleService.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(getAuthUser().getCodFiegDl(), new Integer(codEdicola));
			if (abbinamentoEdicolaDlVoByCodFiegDlCodRivDl != null) {
				Integer codEdicolaWeb = abbinamentoEdicolaDlVoByCodFiegDlCodRivDl.getCodDpeWebEdicola();
				bolleRiassunto = bolleService.getBolleResaRiassunto(getAuthUser().getCodFiegDl(), codEdicolaWeb, dataBolla, tipoBolla, statoBolla, true);
				requestMap.put("bolleRiassunto", bolleRiassunto);
			}
		} else {
			bolleRiassunto = bolleService.getBolleResaRiassunto(getAuthUser().getCodFiegDl(), null, dataBolla, tipoBolla, statoBolla, true);
		}
		List<BollaResaRiassuntoVo> listBollaResaRiassunto = new ArrayList<BollaResaRiassuntoVo>();
		// 14/10/2016 Exception NullPointerException
		if(bolleRiassunto!=null && bolleRiassunto.size()>0){
			for(Object iter: bolleRiassunto ){
				BollaResaRiassuntoVo obj = (BollaResaRiassuntoVo) iter;
				if((obj.getVer_totaleBolleResaDettaglio()!=null && obj.getVer_totaleBolleResaDettaglio()>0)){
					listBollaResaRiassunto.add(obj);
				}else if((obj.getVer_totaleBollaResaDimenticata()!=null && obj.getVer_totaleBollaResaDimenticata()>0)){
					listBollaResaRiassunto.add(obj);
				}	
			}
		}
		bolleRiassunto = listBollaResaRiassunto;		
		
		requestMap.put("titoloManutenzione", getText("igeriv.manutenzione.bolla.resa"));
		requestMap.put("edicola", (autocomplete != null) ? autocomplete.replaceAll("&nbsp;", " ").replaceAll("\\.", " ") : "");
		return SUCCESS;
	}
	
	public String saveBolleRiassunto() throws Exception {
    	bolleService.saveBolleRiassunto(stato, pk);
		return SUCCESS;
	}
	
	public String saveBolleResaRiassunto() throws Exception {
    	bolleService.saveBolleResaRiassunto(stato, pk);
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
		return super.getTitle() + getText("igeriv.manutenzione.bolla.consegna");
	}

	public String getStatoSelectKeys() {
		return "" + IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA + "," + IGerivConstants.INDICATORE_BOLLA_DA_TRASMETTERE + "," + IGerivConstants.INDICATORE_BOLLA_IN_TRASMISSIONE + "," + IGerivConstants.INDICATORE_BOLLA_TRASMESSA;
	}

	public String getStatoSelectValues() {
		return getText(IGerivConstants.BOLLA_NON_TRASMESSA) + "," + getText(IGerivConstants.BOLLA_DA_TRASMETTERE) + "," + getText(IGerivConstants.BOLLA_IN_TRASMISSIONE) + "," + getText(IGerivConstants.BOLLA_TRASMESSA);
	}

}
