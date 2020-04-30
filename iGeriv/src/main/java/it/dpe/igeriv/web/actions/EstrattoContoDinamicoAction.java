package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;

import java.math.BigDecimal;
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

import it.dpe.igeriv.bo.contabilita.ContabilitaService;
import it.dpe.igeriv.dto.EstrattoContoDinamicoDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.vo.BaseVo;
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
@Component("estrattoContoDinamicoAction")
@SuppressWarnings({ "rawtypes" })
public class EstrattoContoDinamicoAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final ContabilitaService contabilitaService;
	private final String crumbName = getText("igeriv.estratto.conto.dinamico");
	private List<EstrattoContoDinamicoDto> listEstrattoContoDinamico;
	private String tableTitle;
	private String data;
	private String EstrattoContoDinamicoTab_ev;
	private String exportTitle;
	
	public EstrattoContoDinamicoAction() {
		this.contabilitaService = null;
	}
	
	@Autowired
	public EstrattoContoDinamicoAction(ContabilitaService contabilitaService) {
		this.contabilitaService = contabilitaService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		tableTitle = getText("igeriv.estratto.conto.dinamico");
		requestMap.put("data", DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH));
	}
	
	@SkipValidation
	public String show() {
		listEstrattoContoDinamico = contabilitaService.getEstrattoContoDinamico(getAuthUser().getCodFiegDl(), getAuthUser().getCodDpeWebEdicola());
		if (listEstrattoContoDinamico != null && !listEstrattoContoDinamico.isEmpty()) {
			BigDecimal importoDare = sum(listEstrattoContoDinamico, on(EstrattoContoDinamicoDto.class).getImportoDare());
			BigDecimal importoAvere = sum(listEstrattoContoDinamico, on(EstrattoContoDinamicoDto.class).getImportoAvere());
			EstrattoContoDinamicoDto dto = new EstrattoContoDinamicoDto();
			if (importoDare.doubleValue() >= importoAvere.doubleValue()) {
				dto.setImportoDare(importoDare.subtract(importoAvere));
				dto.setImportoAvere(BigDecimal.ZERO);
			} else {
				dto.setImportoDare(BigDecimal.ZERO);
				dto.setImportoAvere(importoAvere.subtract(importoDare));
			}
			dto.setDescrizione(getText("igeriv.saldo"));
			dto.setCalcRow(true);
			listEstrattoContoDinamico.add(dto);
		}
		requestMap.put("listEstrattoContoDinamico", listEstrattoContoDinamico);
		exportTitle = getText("igeriv.data.estratto.conto") + " " + getText("igeriv.data") + " " + DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH);
		requestMap.put("title", exportTitle.replaceAll("\\.", " "));
		if (EstrattoContoDinamicoTab_ev != null && (EstrattoContoDinamicoTab_ev.toUpperCase().equals("PDF") || EstrattoContoDinamicoTab_ev.toUpperCase().equals("XLS"))) {
			setAgenziaEdicolaExportTitle();
		}
		return SUCCESS;
	}
	
	@Override
	public String getTitle() {
		String title = getText("igeriv.estratto.conto.dinamico");
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

	
}
