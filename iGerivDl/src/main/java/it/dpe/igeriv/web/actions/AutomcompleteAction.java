package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.localita.LocalitaService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.GiroTipoDto;
import it.dpe.igeriv.dto.ProdottoDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.EdicolaDlVo;
import it.dpe.igeriv.vo.LocalitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
@SuppressWarnings({"rawtypes"}) 
@Scope("prototype")
@Component("automcompleteAction")
public class AutomcompleteAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State, 
		RequestAware {
	private static final long serialVersionUID = 1L;
	private final BolleService bolleService;
	private final LocalitaService localitaService;
	private final EdicoleService edicoleService;
	private final ProdottiService prodottiService;
	private final PubblicazioniService pubblicazioniService;
	private List<Map<String,String>> localita;
	private String term;
	private Integer coddl;
	private String soloUtentiAmministratori;
	
	public AutomcompleteAction() {
		this(null,null,null,null,null);
	}
			
	@Autowired
	public AutomcompleteAction(BolleService bolleService, LocalitaService localitaService, EdicoleService edicoleService, ProdottiService prodottiService, PubblicazioniService pubblicazioniService) {
		this.bolleService = bolleService;
		this.localitaService = localitaService;
		this.edicoleService = edicoleService;
		this.prodottiService = prodottiService;
		this.pubblicazioniService = pubblicazioniService;
	}
	
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
		List<EdicolaDto> listEdicole = edicoleService.getEdicole(getAuthUser().getCodFiegDl(), null, codEdicola, ragSoc);
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
	
	public String edicoleWebByCrivDl() {
		Integer codEdicola = it.dpe.igeriv.util.NumberUtils.getAsInteger(term);
		String ragSoc = null;
		if (codEdicola == null) {
			ragSoc = term;
		}
		boolean soloUtentiAmministratori = !Strings.isNullOrEmpty(this.soloUtentiAmministratori) && Boolean.parseBoolean(this.soloUtentiAmministratori) ? true : false;
		List<EdicolaDto> listEdicole = edicoleService.getEdicole(getAuthUser().getCodFiegDl(), null, codEdicola, ragSoc, soloUtentiAmministratori);
		localita = new ArrayList<Map<String, String>>();
		for (EdicolaDto vo : listEdicole) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getCodEdicolaWeb());
			m.put("label", vo.getCodEdicolaDl() + " - " + vo.getRagioneSociale1() + " - " + vo.getLocalita());
			m.put("value", "" + vo.getCodEdicolaWeb());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;		
	}
	
	public String edicole() {
		Integer codEdicola = it.dpe.igeriv.util.NumberUtils.getAsInteger(term);
		String ragSoc = null;
		if (codEdicola == null) {
			ragSoc = term;
		}
		boolean soloUtentiAmministratori = !Strings.isNullOrEmpty(this.soloUtentiAmministratori) && Boolean.parseBoolean(this.soloUtentiAmministratori) ? true : false;
		List<EdicolaDto> listEdicole = edicoleService.getEdicole(getAuthUser().getCodFiegDl(), codEdicola, null, ragSoc, soloUtentiAmministratori);
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
	
	/**
	 * Ritorna i prodotti editoriali per codice o descrizione
	 * 
	 * @return String
	 */
	public String getProdottoByCodOrTitolo() {
		Integer codPubblicazione = it.dpe.igeriv.util.NumberUtils.getAsInteger(term);
		String titolo = null;
		if (codPubblicazione == null) {
			titolo = term;
		}
		List<PubblicazioneDto> listPubblicazioni = pubblicazioniService.getPubblicazioniByTitoloCpu(getAuthUser().getCodFiegDl(), titolo, codPubblicazione);
		localita = new ArrayList<Map<String, String>>();
		for (PubblicazioneDto vo : listPubblicazioni) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getCodicePubblicazione());
			m.put("label", vo.getCodicePubblicazione() + " - " + vo.getTitolo());
			m.put("value", "" + vo.getCodicePubblicazione());
			localita.add(m);
		}
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;		
	}
	
	/**
	 * Ritorna i numeri copertina di una pubblicazione
	 * 
	 * @return String
	 */
	public String getNumeriCopertina() {
		Integer codPubblicazione = it.dpe.igeriv.util.NumberUtils.getAsInteger(term);
		if (codPubblicazione != null) {
			List<StoricoCopertineVo> listCopertine = pubblicazioniService.getStoricoCopertineByCpu(getAuthUser().getCodFiegDl(), codPubblicazione);
			localita = new ArrayList<Map<String, String>>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			for (StoricoCopertineVo vo : listCopertine) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				map.put("id", String.valueOf(vo.getPk().getIdtn()));
				if (vo.getSottoTitolo() == null || vo.getSottoTitolo().trim().isEmpty()) {
					map.put("label", String.format("%s - %s", vo.getNumeroCopertina(), sdf.format(new Date(vo.getDataUscita().getTime()))));
				}
				else {
					map.put("label", String.format("%s - %s - %s", vo.getNumeroCopertina(), sdf.format(new Date(vo.getDataUscita().getTime())), vo.getSottoTitolo()));
				}
				map.put("value", String.valueOf(vo.getPk().getIdtn()));
				localita.add(map);
			}
		}
		
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;
	}
	
	public String tipiBollaConsegna() throws Exception {
		Timestamp dataBolla = DateUtilities.parseDate(term, DateUtilities.FORMATO_DATA_SLASH);
		
		List<String> listTipiBolla = bolleService.getTipiBollaConsegna(getAuthUser().getCodFiegDl(), dataBolla);
		localita = new ArrayList<Map<String, String>>();
		for (String tipoBolla : listTipiBolla) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("id", tipoBolla);
			map.put("label", tipoBolla);
			localita.add(map);
		}
		
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;
	}
	
	public String tipiBollaResa() throws Exception {
		Timestamp dataBolla = DateUtilities.parseDate(term, DateUtilities.FORMATO_DATA_SLASH);
		
		List<String> listTipiBolla = bolleService.getTipiBollaResa(getAuthUser().getCodFiegDl(), dataBolla);
		localita = new ArrayList<Map<String, String>>();
		for (String tipoBolla : listTipiBolla) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("id", tipoBolla);
			map.put("label", tipoBolla);
			localita.add(map);
		}
		
		return IGerivConstants.ACTION_SUCCESS_LOCALITA;
	}
	
	@Override
	public void saveParameters(Context context, String tableId, Map parameterMap) {
		
	}

	@Override
	public Map getParameters(Context context, String tableId, String stateAttr) {
		return null;
	}

}
