package it.dpe.igeriv.web.actions;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.fornitori.FornitoriService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiFornitoreVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.TipoLocalitaVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiFornitorePk;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione delle anagrafiche dei prodotti non editoriali.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("prodottiNonEditorialiFornitoriAction")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProdottiNonEditorialiFornitoriAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final FornitoriService fornitoriService;
	private final ProdottiService prodottiService;
	private final String crumbName = getText("igeriv.fornitori.edicola");
	private String tableTitle;
	private List<ProdottiNonEditorialiFornitoreVo> fornitori;
	private ProdottiNonEditorialiFornitoreVo fornitore;
	private String ragioneSociale;
	private String piva;
	private Integer codFornitore;
	private Boolean isNew;
	
	public ProdottiNonEditorialiFornitoriAction() {
		this.fornitoriService = null;
		this.prodottiService = null;
	}
	
	@Autowired
	public ProdottiNonEditorialiFornitoriAction(FornitoriService fornitoriService, ProdottiService prodottiService) {
		this.fornitoriService = fornitoriService;
		this.prodottiService = prodottiService;
	}
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().contains("showFornitori")) {
			tableTitle = getText("igeriv.fornitori.edicola");
		} else if (fornitore != null && getActionName() != null && getActionName().contains("saveFornitore")) {
			tableTitle = getText("igeriv.inserisci.aggiorna.fornitore");
			if (fornitore.getNome() == null || fornitore.getNome().equals("")) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("dpe.rag.sociale")) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			if (fornitore.getEmail() != null && !fornitore.getEmail().equals("") && !StringUtility.isValidEmailAddress(fornitore.getEmail())) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.invalid.email") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFornitoriFilter() throws Exception {
		tableTitle = getText("igeriv.fornitori.edicola");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFornitori() throws ParseException {
		tableTitle = getText("igeriv.fornitori.edicola");
		fornitori = fornitoriService.getFornitoriEdicola(getAuthUser().getCodEdicolaMaster(), ragioneSociale, piva, true);
		requestMap.put("fornitori", fornitori);
		return SUCCESS;
	}
	
	@SkipValidation
	public String editFornitore() throws Exception {
		tableTitle = getText("igeriv.inserisci.aggiorna.fornitore");
		if (codFornitore != null) {
			fornitore = fornitoriService.getFornitore(getAuthUser().getCodEdicolaMaster(), codFornitore);
		} else {
			fornitore = new ProdottiNonEditorialiFornitoreVo();
			ProdottiNonEditorialiFornitorePk pk = new ProdottiNonEditorialiFornitorePk();
			pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
			pk.setCodFornitore(fornitoriService.getNextSeqVal("SEQUENZA_9500").intValue());
			fornitore.setPk(pk);
			isNew = true;
		}
		return "editFornitore";
	}
	
	public String saveFornitore() throws Exception {
		tableTitle = getText("igeriv.inserisci.aggiorna.fornitore");
		if (fornitore != null) {
			if (fornitore.getLocalita().getCodLocalita() == null) {
				fornitore.setLocalita(null);
			}
			if (fornitore.getProvincia().getCodProvincia() == null) {
				fornitore.setProvincia(null);
			} 
			if (fornitore.getTipoLocalita().getTipoLocalita() == null) {
				fornitore.setTipoLocalita(null);
			} 
			fornitore.getPk().setCodEdicola(getAuthUser().getCodEdicolaMaster());
			fornitoriService.saveBaseVo(fornitore);
		}
		return "editFornitore";
	}
	
	public String deleteFornitore() {
		tableTitle = getText("igeriv.inserisci.aggiorna.fornitore");
		if (fornitore != null) {
			try {
				List<ProdottiNonEditorialiBollaVo> bollaProdottiVariEdicolaByFornitore = prodottiService.getBollaProdottiVariEdicolaByFornitore(getAuthUser().getCodEdicolaMaster(), fornitore.getPk().getCodFornitore());
				if (bollaProdottiVariEdicolaByFornitore != null && !bollaProdottiVariEdicolaByFornitore.isEmpty()) {
					throw new DataIntegrityViolationException(IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.delete.fornitore.in.bolla.no.delete")  + IGerivConstants.END_EXCEPTION_TAG, fornitore.getNome()));
				}
				fornitoriService.deleteFornitore(fornitore.getPk().getCodFornitore(), getAuthUser().getCodEdicolaMaster());
			} catch (DataIntegrityViolationException e) {
				requestMap.put("igerivException", e.getLocalizedMessage());
				throw new IGerivRuntimeException();
			} catch (Exception e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
		return "pneEditFornitoreTiles";
	}
	
	public String saveCategoria() {
		return SUCCESS;
	}
	
	public String saveProdotto() {
		return SUCCESS;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#getParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.lang.String)
	 */
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#saveParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.util.Map)
	 */
	public void saveParameters(Context context, String arg1, Map arg2) {
	}

	@Override
	public String getTitle() {
		String title = getText("igeriv.menu.17");
		return super.getTitle() + title;
	}

	public List<TipoLocalitaVo> getTipiLocalita	() {
		return (List<TipoLocalitaVo>) context.getAttribute("tipiLocalita");
	}

	public List<ProvinciaVo> getProvince() {
		return (List<ProvinciaVo>) context.getAttribute("province");
	}

}
