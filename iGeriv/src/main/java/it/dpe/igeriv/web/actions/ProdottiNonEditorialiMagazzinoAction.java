package it.dpe.igeriv.web.actions;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.fornitori.FornitoriService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.dto.CategoriaDto;
import it.dpe.igeriv.dto.ReportMagazzinoPneDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiFornitoreVo;
import lombok.Getter;
import lombok.Setter;

/**
* Classe action per il report del magazzino.
* 
* @author Vincenzo Mazzeo
*
*/
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("prodottiNonEditorialiMagazzinoAction")
@SuppressWarnings({"rawtypes" }) 
public class ProdottiNonEditorialiMagazzinoAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final FornitoriService fornitoriService;
	private final ProdottiService prodottiService;
	private final String crumbName = getText("igeriv.pne.report.magazzino");
	private String tableTitle;
	private List<ProdottiNonEditorialiFornitoreVo> conti;
	private List<CategoriaDto> categorie;
	private String dataIniziale;
	private String dataFinale;
	private String codiceConto;
	private String codCategoria;
	private String codiceCausale;
	private String codProdotto;
	private TreeMap<String, String> totaliMap;
	private String ReportMagazzinoTab_ev;
	
	public ProdottiNonEditorialiMagazzinoAction() {
		this.fornitoriService = null;
		this.prodottiService = null;
	}
	
	@Autowired
	public ProdottiNonEditorialiMagazzinoAction(FornitoriService fornitoriService, ProdottiService prodottiService) {
		this.fornitoriService = fornitoriService;
		this.prodottiService = prodottiService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		conti = fornitoriService.getFornitoriEdicola(getAuthUser().getCodEdicolaMaster(), null, null, true);
		categorie = prodottiService.getCategoriePne(getAuthUser().getCodEdicolaMaster());
	}
	
	@Override
	public void validate() {
		tableTitle = getText("igeriv.pne.report.magazzino");
		if (dataIniziale == null || dataFinale == null || dataIniziale.equals("") || dataFinale.equals("")) {
			addActionError(getText("error.specificare.data.o.intervallo.date"));
		} else {
			Timestamp dataDa = null;
			Timestamp dataA = null;
			try {
				dataDa = DateUtilities.parseDate(dataIniziale, DateUtilities.FORMATO_DATA_SLASH);
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataIniziale));
				return;
			}
			try {
				dataA = DateUtilities.parseDate(dataFinale, DateUtilities.FORMATO_DATA_SLASH);
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), dataFinale));
				return;
			}
			if (dataA.before(dataDa)) {
				addActionError(getText("msg.formato.intervall.invalido"));
				return;
			}
		}
	}

	@SkipValidation
	public String showFilter() {
		tableTitle = getText("igeriv.pne.report.magazzino");
		return SUCCESS;
	}

	public String showReport() throws ParseException {
		tableTitle = getText("igeriv.pne.report.magazzino");
		Timestamp dataIniziale = DateUtilities.floorDay(DateUtilities.parseDate(this.dataIniziale, DateUtilities.FORMATO_DATA_SLASH));
		Timestamp dataFinale = DateUtilities.ceilDay(DateUtilities.parseDate(this.dataFinale, DateUtilities.FORMATO_DATA_SLASH));
		List<Integer> codContoList = new ArrayList<Integer>();
		List<Integer> codCategoriaList = new ArrayList<Integer>();
		List<Integer> codCausaleList = new ArrayList<Integer>();
		Integer codProdotto = null;
		
		if (this.codiceConto != null && !this.codiceConto.trim().equals("")) {
			String[] values = this.codiceConto.split(",");
			for (String value : values) {
				codContoList.add(Integer.valueOf(value.trim()));
			}
		}
		
		if (this.codCategoria != null && !this.codCategoria.trim().equals("")) {
			String[] values = this.codCategoria.split(",");
			for (String value : values) {
				codCategoriaList.add(Integer.valueOf(value.trim()));
			}
		}
		
		if (this.codiceCausale != null && !this.codiceCausale.trim().equals("")) {
			String[] values = this.codiceCausale.split(",");
			for (String value : values) {
				codCausaleList.add(Integer.valueOf(value.trim()));
			}
		}
		
		codProdotto = this.codProdotto.equals("") ? null : Integer.valueOf(this.codProdotto);
		
		List<ReportMagazzinoPneDto> reportMagazzino = prodottiService.getReportMagazzinoPne(getAuthUser().getCodEdicolaMaster(), dataIniziale, dataFinale, codContoList, codCategoriaList, codCausaleList, codProdotto);
		requestMap.put("reportMagazzino", reportMagazzino);
		
		totaliMap = new TreeMap<String, String>();
		
		if (!reportMagazzino.isEmpty()) {
			String causale = "";
			Float totale = 0f;
			
			for (ReportMagazzinoPneDto dto : reportMagazzino) {
				if (!causale.equals(dto.getCausale())) {
					if (!causale.equals("")) {
						totaliMap.put(causale, NumberUtils.formatNumber(totale));
					}
					causale = dto.getCausale();
					totale = 0f;
				}
				
				totale += dto.getImporto();
			}
			
			totaliMap.put(causale, NumberUtils.formatNumber(totale));
		}
		
		if (ReportMagazzinoTab_ev != null && (ReportMagazzinoTab_ev.toUpperCase().equals("PDF") || ReportMagazzinoTab_ev.toUpperCase().equals("XLS"))) {
			for (ReportMagazzinoPneDto dto : reportMagazzino) {
				dto.setDescrizioneProdotto(dto.getDescrizioneProdotto().replaceAll(IGerivConstants.EURO_SIGN_DECIMAL, IGerivConstants.EURO_SIGN));
			}
		}
		return SUCCESS;
	}

	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	public void saveParameters(Context context, String arg1, Map arg2) {
	}

	@Override
	public String getTitle() {
		String title = getText("igeriv.menu.51");
		return super.getTitle() + title;
	}
}
