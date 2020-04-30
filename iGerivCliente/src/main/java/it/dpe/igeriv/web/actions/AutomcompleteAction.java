package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.localita.LocalitaService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.GiroTipoDto;
import it.dpe.igeriv.dto.ProdottoDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.EdicolaDlVo;
import it.dpe.igeriv.vo.LocalitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action che ritorna una lista di dati in formato JSON.
 * Utilizzata nei campi autocomplete.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({"rawtypes", "unused"}) 
@Scope("prototype")
@Component("automcompleteAction")
public class AutomcompleteAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State, 
		RequestAware {
	private static final long serialVersionUID = 1L;
	@Autowired
	private EdicoleService edicoleService;
	@Autowired
	private LocalitaService localitaService;
	@Autowired
	private ProdottiService prodottiService;
	private Map<String, Object> request = new HashMap<String, Object>();
	private List<Map<String,String>> localita;
	private String term;
	private Integer coddl;
	
	public String localita() {
		List<LocalitaVo> listLocalitaVo = localitaService.getLocalita(term);
		localita = new ArrayList<Map<String,String>>();
		for (LocalitaVo vo : listLocalitaVo) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getCodLocalita());
			m.put("label", vo.getDescrizione().trim());
			m.put("value", vo.getDescrizione().trim());
			m.put("idProvincia", "" + vo.getProvinciaVo().getCodProvincia());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;		
	}
	
	/**
	 * Cerca l'edicola per crivdl nella tabella anagrafica edicola
	 * 
	 * @return String
	 */
	public String edicoleByCrivDl() {
		Integer codEdicola = it.dpe.igeriv.util.NumberUtils.getAsInteger(term);
		String ragSoc = null;
		if (codEdicola == null) {
			ragSoc = term;
		}
		List<EdicolaDto> listEdicole = edicoleService.getEdicole(getAuthUser().getId(), null, codEdicola, ragSoc);
		localita = new ArrayList<Map<String, String>>();
		for (EdicolaDto vo : listEdicole) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getCodEdicolaDl());
			m.put("label", vo.getCodEdicolaDl() + " - " + vo.getRagioneSociale1() + " - " + vo.getLocalita());
			m.put("value", "" + vo.getCodEdicolaDl());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;		
	}
	
	/**
	 * Cerca l'edicola per crivdl nella tabella anagrafica generale delle edicole
	 * (dove ci sono tutte le edicole di un DL anche se non attive su iGeriv)
	 * 
	 * @return String
	 */
	public String edicoleGeneraleByCrivDl() {
		if (coddl != null && !Strings.isNullOrEmpty(term)) {
			Integer codEdicola = it.dpe.igeriv.util.NumberUtils.getAsInteger(term);
			String ragSoc = null;
			if (codEdicola == null) {
				ragSoc = term;
			}
			List<EdicolaDlVo> listEdicole = edicoleService.getEdicoleDl(coddl, codEdicola, ragSoc);
			localita = new ArrayList<Map<String, String>>();
			for (EdicolaDlVo vo : listEdicole) {
				Map<String,String> m = new HashMap<String, String>();	
				m.put("id", "" + vo.getPk().getCodRivDl());
				m.put("label", vo.getPk().getCodRivDl() + " - " + vo.getRagioneSocialeDlPrimaRiga() + " - " + vo.getLocalitaDlPrimaRiga());
				m.put("value", vo.getRagioneSocialeDlPrimaRiga());
				localita.add(m);
			}
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;		
	}
	
	public String edicole() {
		Integer codEdicola = it.dpe.igeriv.util.NumberUtils.getAsInteger(term);
		String ragSoc = null;
		if (codEdicola == null) {
			ragSoc = term;
		}
		List<EdicolaDto> listEdicole = edicoleService.getEdicole(getAuthUser().getId(), codEdicola, null, ragSoc);
		localita = new ArrayList<Map<String, String>>();
		for (EdicolaDto vo : listEdicole) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getCodEdicolaWeb());
			m.put("label", vo.getCodEdicolaWeb() + " - " + vo.getRagioneSociale1() + " - " + vo.getLocalita());
			m.put("value", "" + vo.getCodEdicolaWeb());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;		
	}
	
	public String edicoleWeb() {
		Integer codEdicola = it.dpe.igeriv.util.NumberUtils.getAsInteger(term);
		String ragSoc = null;
		if (codEdicola == null) {
			ragSoc = term;
		}
		List<EdicolaDto> listEdicole = edicoleService.getEdicole(coddl, codEdicola, null, ragSoc);
		localita = new ArrayList<Map<String, String>>();
		for (EdicolaDto vo : listEdicole) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getCodEdicolaWeb());
			m.put("label", vo.getCodEdicolaWeb() + " - " + vo.getRagioneSociale1() + " - " + vo.getLocalita());
			m.put("value", "" + vo.getCodEdicolaWeb());
			m.put("coddl", "" + vo.getCodDl());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;		
	}
	
	public String giriTipo() {
		Integer codice = it.dpe.igeriv.util.NumberUtils.getAsInteger(term);
		String desc = null;
		if (codice == null) {
			desc = term;
		}
		List<GiroTipoDto> listGiroTipo = edicoleService.getGiriTipo(getAuthUser().getCodFiegDl(), getAuthUser().getId(), codice, desc);
		localita = new ArrayList<Map<String, String>>();
		for (GiroTipoDto vo : listGiroTipo) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getIdGiroTipo());
			m.put("label", "" + vo.getIdGiroTipo());
			m.put("value", "" + vo.getIdGiroTipo());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;		
	} 
	
	public String zoneTipo() {
		Integer codice = it.dpe.igeriv.util.NumberUtils.getAsInteger(term);
		String desc = null;
		if (codice == null) {
			desc = term;
		}
		List<GiroTipoDto> listGiroTipo = edicoleService.getZoneTipo(getAuthUser().getCodFiegDl(), getAuthUser().getId(), codice, desc);
		localita = new ArrayList<Map<String, String>>();
		for (GiroTipoDto vo : listGiroTipo) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getIdGiroTipo());
			m.put("label", "" + vo.getIdGiroTipo());
			m.put("value", "" + vo.getIdGiroTipo());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;		
	}
	 
	/**
	 * Ritorna i prodotti non editoriali corrispondenti a una porzione del codice prodotto del fornitore
	 * 
	 * @return String
	 */
	public String getProdottoNonEditorialeInBolla() {
		List<ProdottiNonEditorialiPrezziAcquistoVo> listProdotti = prodottiService.getProdottiNonEditorialiPrezziAcquisto(getAuthUser().getCodEdicolaMaster(), null, null, term);
		localita = new ArrayList<Map<String, String>>();
		for (ProdottiNonEditorialiPrezziAcquistoVo vo : listProdotti) {
			ProdottiNonEditorialiVo prodotto = vo.getProdotto();
			Map<String,String> m = new HashMap<String, String>();	
			m.put("codiceFornitore", vo.getPk().getCodiceProdottoFornitore());
			m.put("codProdotto", vo.getPk().getCodProdotto().toString());
			m.put("descrizione", prodotto.getDescrizioneProdottoA());
			m.put("prezzo", vo.getUltimoPrezzoAcquisto() != null ? vo.getUltimoPrezzoAcquisto().toString() : "");
			m.put("iva", prodotto.getAliquota() != null ? prodotto.getAliquota().toString() : "");
			m.put("barcode", prodotto.getBarcode() != null ? prodotto.getBarcode().toString() : "");
			m.put("prodottoDl", (prodotto.getProdottoDl() == null) ? "false" : prodotto.getProdottoDl().toString());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;
	}
	
	/**
	 * Ritorna i prodotti non editoriali per descrizione
	 * 
	 * @return String
	 */
	public String getProdottoNonEditorialeInBollaPerDescrizione() {
		List<ProdottoDto> listProdotti = prodottiService.getProdottiNonEditorialiEdicolaByDescrizione(term, getAuthUser().getCodEdicolaMaster(), null, null);
		localita = new ArrayList<Map<String, String>>();
		for (ProdottoDto prodotto : listProdotti) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("codProdotto", prodotto.getCodProdottoInterno().toString());
			m.put("descrizione", prodotto.getDescrizione());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;
	}
	
	/**
	 * Ritorna i prodotti non editoriali per codice o descrizione
	 * 
	 * @return
	 */
	public String getProdottoNonEditorialeByCodOrDescr() {
		Long codProdotto = it.dpe.igeriv.util.NumberUtils.getAsLong(term);
		String descrizione = null;
		if (codProdotto == null) {
			descrizione = term;
		}
		List<ProdottiNonEditorialiVo> listProdotti = prodottiService.getProdottiNonEditorialiEdicolaByCodOrDescr(getAuthUser().getCodEdicolaMaster(), codProdotto, descrizione);
		localita = new ArrayList<Map<String, String>>();
		for (ProdottiNonEditorialiVo prodotto : listProdotti) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("codProdotto", prodotto.getCodProdottoInterno().toString());
			m.put("descrizione", prodotto.getDescrizioneProdottoA());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;		
	}
	
	/**
	 * Ritorna i prodotti non editoriali per codice prodotto edicola o descrizione
	 * 
	 * @return
	 */
	public String getProdottoNonEditorialeByCodEdicolaOrDescr() {
		List<ProdottiNonEditorialiVo> listProdotti = prodottiService.getProdottiNonEditorialiEdicolaByCodEdicolaOrDescr(getAuthUser().getCodEdicolaMaster(), term);
		localita = new ArrayList<Map<String, String>>();
		for (ProdottiNonEditorialiVo prodotto : listProdotti) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("codProdotto", prodotto.getCodProdottoInterno().toString());
			m.put("descrizione", (prodotto.getCodProdottoEsterno() == null ? "" : prodotto.getCodProdottoEsterno()) + " - " + prodotto.getDescrizioneProdottoA());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;		
	}
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
	
	public List<Map<String, String>> getLocalita() {
		return localita;
	}

	public void setLocalita(List<Map<String, String>> localita) {
		this.localita = localita;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts2.interceptor.RequestAware#setRequest(java.util.Map)
	 */
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}
	
	@Override
	public void saveParameters(Context context, String tableId, Map parameterMap) {
		
	}

	@Override
	public Map getParameters(Context context, String tableId, String stateAttr) {
		return null;
	}

	public Integer getCoddl() {
		return coddl;
	}

	public void setCoddl(Integer coddl) {
		this.coddl = coddl;
	}

	public EdicoleService getEdicoleService() {
		return edicoleService;
	}

	public void setEdicoleService(EdicoleService edicoleService) {
		this.edicoleService = edicoleService;
	}

	public LocalitaService getLocalitaService() {
		return localitaService;
	}

	public void setLocalitaService(LocalitaService localitaService) {
		this.localitaService = localitaService;
	}

	public ProdottiService getProdottiService() {
		return prodottiService;
	}

	public void setProdottiService(ProdottiService prodottiService) {
		this.prodottiService = prodottiService;
	}
	
}
