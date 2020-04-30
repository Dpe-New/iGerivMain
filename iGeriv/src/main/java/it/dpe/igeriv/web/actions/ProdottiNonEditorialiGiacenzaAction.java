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

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.dto.CategoriaDto;
import it.dpe.igeriv.dto.GiacenzaPneDto;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiFornitoreVo;
import lombok.Getter;
import lombok.Setter;

/**
* Classe action per il report della gianceza prodotti non editoriali.
* 
* @author Marcello Romano - DPE
* 
*/
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("prodottiNonEditorialiGiacenzaAction")
@SuppressWarnings({"rawtypes"}) 
public class ProdottiNonEditorialiGiacenzaAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final ProdottiService prodottiService;
	private final String crumbName = getText("igeriv.report.giacenza");
	private String tableTitle;
	private List<ProdottiNonEditorialiFornitoreVo> conti;
	private List<CategoriaDto> categorie;
	private String codCategoria;
	private String codSottoCategoria;
	private String codProdotto;
	
	public ProdottiNonEditorialiGiacenzaAction() {
		this.prodottiService = null;
	}
	
	@Autowired
	public ProdottiNonEditorialiGiacenzaAction(ProdottiService prodottiService) {
		this.prodottiService = prodottiService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		categorie = prodottiService.getCategoriePne(getAuthUser().getCodEdicolaMaster());
	}
	
	@Override
	public void validate() {
		tableTitle = getText("igeriv.report.giacenza");
	}

	@SkipValidation
	public String showFilter() {
		tableTitle = getText("igeriv.report.giacenza");
		return SUCCESS;
	}

	public String showGiacenza() {
		tableTitle = getText("igeriv.report.giacenza");
		Long codCategoria = !Strings.isNullOrEmpty(this.codCategoria) ? new Long(this.codCategoria) : null;
		Long codSottoCategoria = !Strings.isNullOrEmpty(this.codSottoCategoria) ? new Long(this.codSottoCategoria) : null;
		Long codProdotto = !Strings.isNullOrEmpty(this.codProdotto) ? new Long(this.codProdotto) : null;
		List<GiacenzaPneDto> reportGiacenza = prodottiService.getGiacenzaPne(getAuthUser().getCodEdicolaMaster(), codCategoria, codSottoCategoria, codProdotto);
		//CDL RICHIESTA - AGGIUNGERE PREZZO UNITARIO E TOTALE PER OGNI RIGA ESCLUDENDO VALORI NEGATIVI
		//CDL RICHIESTA 19/02/2016 è stata ripristinata la precedente query perchè la modifica effettuata non era adeguata.
//		Float prezzoTotaleReport = new Float(0);
//		for(GiacenzaPneDto ind : reportGiacenza){
//			if(ind.getPrezzototale()!=null)
//			prezzoTotaleReport = prezzoTotaleReport+ind.getPrezzototale();
//		}
//		GiacenzaPneDto totReport = new GiacenzaPneDto();
//		totReport.setDescrizione("TOTALE REPORT");
//		totReport.setPrezzototale(prezzoTotaleReport);
//		
//		reportGiacenza.add(totReport);
		
		requestMap.put("reportGiacenza", reportGiacenza);
		return SUCCESS;
	}

	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	public void saveParameters(Context context, String arg1, Map arg2) {
	}

	@Override
	public String getTitle() {
		String title = getText("igeriv.menu.50");
		return super.getTitle() + title;
	}
}
