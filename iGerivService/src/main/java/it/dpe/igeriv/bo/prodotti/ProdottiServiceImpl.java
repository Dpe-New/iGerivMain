package it.dpe.igeriv.bo.prodotti;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import ch.lambdaj.group.Group;
import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.contabilita.ContabilitaService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.BollaResaProdottiVariDto;
import it.dpe.igeriv.dto.CategoriaDto;
import it.dpe.igeriv.dto.GiacenzaPneDto;
import it.dpe.igeriv.dto.ProdottoDto;
import it.dpe.igeriv.dto.ReportMagazzinoPneDto;
import it.dpe.igeriv.dto.RichiestaProdottoDto;
import it.dpe.igeriv.dto.SottoCategoriaDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiAliquotaIvaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCausaliContabilitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCausaliMagazzinoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiDocumentiEmessiVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiGenericaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezzoVenditaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiProgressiviFatturazioneVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiCategoriaEdicolaPk;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiSottoCategoriaEdicolaPk;
import it.dpe.igeriv.vo.pk.ProdottoNonEditorialePrezzoVenditaPk;

@Service("ProdottiService")
class ProdottiServiceImpl extends BaseServiceImpl implements ProdottiService {
	private final ProdottiRepository repository;
	private final EdicoleService edicoleService;
	private final ContabilitaService contabilitaService;
	
	@Autowired
	ProdottiServiceImpl(ProdottiRepository repository, EdicoleService edicoleService, ContabilitaService contabilitaService) {
		super(repository);
		this.repository = repository;
		this.edicoleService = edicoleService;
		this.contabilitaService = contabilitaService;
	}

	@Override
	public void updateFakeImgMiniaturaName(Integer codFiegDl, Integer cpu, String nomeImmagine) {
		repository.updateFakeImgMiniaturaName(codFiegDl, cpu, nomeImmagine);
	}

	@Override
	public List<ProdottoDto> getProdotti(Integer codEdicola, Integer codFiegDl, Long codContabileCliente, String codiceEsterno, String descrizione, Long categoria, Long sottocategoria, String barcode, Float prezzo) {
		return repository.getProdotti(codEdicola, codFiegDl, codContabileCliente, codiceEsterno, descrizione, categoria, sottocategoria, barcode, prezzo);
	}

	@Override
	public Integer getGiacenzaProdottoDl(String codiceProdottoFornitore, Integer codFornitore) {
		return repository.getGiacenzaProdottoDl(codiceProdottoFornitore, codFornitore);
	}

	@Override
	public List<RichiestaProdottoDto> getRichiesteProdotti(Integer codFornitore, Integer codEdicolaDl, String descrizione, String stato, Timestamp dataDa, Timestamp dataA) {
		return repository.getRichiesteProdotti(codFornitore, codEdicolaDl, descrizione, stato, dataDa, dataA);
	}

	@Override
	public List<ProdottiNonEditorialiCategoriaEdicolaVo> getCategorie(Integer codFiegDl, Integer codEdicola) {
		return repository.getCategorie(codFiegDl, codEdicola);
	}

	@Override
	public List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> getSottocategorieProdottiDl(Integer codFiegDl, Integer codEdicola, Long categoria) {
		return repository.getSottocategorieProdottiDl(codFiegDl, codEdicola, categoria);
	}

	@Override
	public RichiestaProdottoDto getRichiestaRifornimentoProdotto(Long codice) {
		return repository.getRichiestaRifornimentoProdotto(codice);
	}

	@Override
	public ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo getProdottiRichiesteRifornimentoDettaglioVo(Long codice, Long codRichiestaRifornimento) {
		return repository.getProdottiRichiesteRifornimentoDettaglioVo(codice, codRichiestaRifornimento);
	}

	@Override
	public List<CategoriaDto> getCategoriePne(Integer codEdicola) {
		return repository.getCategoriePne(codEdicola);
	}

	@Override
	public List<SottoCategoriaDto> getSottocategoriePne(Long categoria, Integer codEdicola) {
		return repository.getSottocategoriePne(categoria, codEdicola);
	}

	@Override
	public List<ProdottoDto> getProdottiNonEditoriali(Long categoria, Long sottocategoria) {
		return repository.getProdottiNonEditoriali(categoria, sottocategoria);
	}

	@Override
	public List<ProdottoDto> getProdottoNonEditorialeByBarcode(Integer codEdicola, String barcode, Integer codFornitore, Boolean soloProdottiForniti) {
		
		List<ProdottoDto> LST =repository.getProdottoNonEditorialeByBarcode(codEdicola, barcode, codFornitore, soloProdottiForniti); 
		
		return LST;
	}

	@Override
	public ProdottiNonEditorialiVo getProdottoNonEditorialeVo(Long codiceProdottoInterno) {
		return repository.getProdottoNonEditorialeVo(codiceProdottoInterno);
	}

	@Override
	public List<ProdottiNonEditorialiGenericaVo> getProdottiNonEditoriali(Long categoria, Long sottocategoria, Integer codEdicolaProdottiDaEscudere) {
		return repository.getProdottiNonEditoriali(categoria, sottocategoria, codEdicolaProdottiDaEscudere);
	}

	@Override
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicola(Long categoria, Long sottocategoria, Integer codEdicola) {
		return repository.getProdottiNonEditorialiEdicola(categoria, sottocategoria, codEdicola);
	}

	@Override
	public List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> getProdottiNonEditorialiSottoCategorieEdicolaVo(Long codCategoria, Integer codEdicola) {
		return repository.getProdottiNonEditorialiSottoCategorieEdicolaVo(codCategoria, codEdicola);
	}

	@Override
	public ProdottiNonEditorialiGenericaVo getProdottiNonEditorialiGenericaVo(Long codProdottoInterno) {
		return repository.getProdottiNonEditorialiGenericaVo(codProdottoInterno);
	}

	@Override
	public ProdottiNonEditorialiVo getProdottiNonEditorialiVo(Long codProdotto, Integer codEdicola) {
		return repository.getProdottiNonEditorialiVo(codProdotto, codEdicola);
	}

	@Override
	public void deletePrezziProdottoEdicola(Integer codEdicola, Long codProdottoInterno) {
		repository.deletePrezziProdottoEdicola(codEdicola, codProdottoInterno);	
	}

	@Override
	public void deleteProdottiNonEditorialiPrezziAcquisto(Integer codEdicola, List<Long> codProdotto) {
		repository.deleteProdottiNonEditorialiPrezziAcquisto(codEdicola, codProdotto);
	}
	
	@Override
	public List<ProdottoDto> getProdottiNonEditorialiEdicolaDto(Integer codEdicola) {
		return repository.getProdottiNonEditorialiEdicolaDto(codEdicola);
	}
	
	@Override
	public List<ProdottoDto> getProdottiVariByDescrizioneBarcode(Integer codEdicola, String inputText, String barcode) {
		List<ProdottoDto> LST =repository.getProdottiVariByDescrizioneBarcode(codEdicola, inputText, barcode); 
		
		return LST;
	}

	@Override
	public ProdottiNonEditorialiCategoriaVo getProdottiNonEditorialiCategoriaVo(Long codCategoria) {
		return repository.getProdottiNonEditorialiCategoriaVo(codCategoria);
	}

	@Override
	public ProdottiNonEditorialiSottoCategoriaVo getProdottiNonEditorialiSottoCategoriaVo(Long codCategoria, Long codSottoCategoria) {
		return repository.getProdottiNonEditorialiSottoCategoriaVo(codCategoria, codSottoCategoria);
	}

	@Override
	public List<ProdottiNonEditorialiBollaVo> getBolleProdottiVariEdicola(Integer codEdicola, Integer codFornitore, Integer codiceCausale, Timestamp dataDocumento, String numeroDocumento, boolean excludeDl) {
		return repository.getBolleProdottiVariEdicola(codEdicola, codFornitore, codiceCausale, dataDocumento, numeroDocumento, excludeDl);
	}

	@Override
	public ProdottiNonEditorialiBollaVo getBolleProdottiVariEdicola(Integer codEdicola, Long idDocumento) {
		return repository.getBolleProdottiVariEdicola(codEdicola, idDocumento);
	}
	
	@Override
	public ProdottiNonEditorialiBollaVo getBolleProdottiVariEdicola(Long idDocumento) {
		return repository.getBolleProdottiVariEdicola(idDocumento);
	}

	@Override
	public List<ProdottiNonEditorialiBollaVo> getBollaProdottiVariEdicolaByFornitore(Integer codEdicola, Integer codFornitore) {
		return repository.getBollaProdottiVariEdicolaByFornitore(codEdicola, codFornitore);
	}

	@Override
	public List<ProdottiNonEditorialiCausaliMagazzinoVo> getCausali() {
		return repository.getCausali();
	}

	@Override
	public ProdottiNonEditorialiCausaliMagazzinoVo getCausaleMagazzino(Integer codiceCausale) {
		return repository.getCausaleMagazzino(codiceCausale);
	}

	@Override
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezziAcquisto(Integer codEdicola, Integer codFornitore, Boolean soloProdottiFornitiString, String idProdottoFornitore) {
		return repository.getProdottiNonEditorialiPrezziAcquisto(codEdicola, codFornitore, soloProdottiFornitiString, idProdottoFornitore);
	}
	
	@Override
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezziAcquisto(Integer codEdicola, Integer codFornitore, Boolean soloProdottiFornitiString, String idProdottoFornitore, boolean getGiacenza) {
		return repository.getProdottiNonEditorialiPrezziAcquisto(codEdicola, codFornitore, soloProdottiFornitiString, idProdottoFornitore, getGiacenza);
	}
	
	@Override
	public List<ProdottoDto> getProdottiNonEditorialiEdicolaByDescrizione(String descrizione, Integer codEdicola, Integer codFornitore, Boolean soloProdottiForniti) {
		return repository.getProdottiNonEditorialiEdicolaByDescrizione(descrizione, codEdicola, codFornitore, soloProdottiForniti);
	}

	@Override
	public void deleteBollaProdottiVari(Long idDocumento) {
		repository.deleteBollaProdottiVari(idDocumento);
	}

	@Override
	public ProdottiNonEditorialiPrezziAcquistoVo getProdottiNonEditorialiPrezzoAcquisto(Integer codEdicola, Long codiceProdotto, String codiceProdottoFornitore) {
		return repository.getProdottiNonEditorialiPrezzoAcquisto(codEdicola, codiceProdotto, codiceProdottoFornitore);
	}
	
	@Override
	public ProdottiNonEditorialiPrezziAcquistoVo getProdottoNonEditorialiPrezzoAcquistoByCodFornitore(Integer codEdicola, Integer codiceFornitore, String codiceProdottoFornitore) {
		return repository.getProdottoNonEditorialiPrezzoAcquistoByCodFornitore(codEdicola, codiceFornitore, codiceProdottoFornitore);
	}

	@Override
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezzoAcquistoNonUsatiInBolla(Integer codEdicola, Integer codFornitore) {
		return repository.getProdottiNonEditorialiPrezzoAcquistoNonUsatiInBolla(codEdicola, codFornitore);
	}

	@Override
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezziAcquistoByDescrizione(Integer codEdicola, String descrizione) {
		return repository.getProdottiNonEditorialiPrezziAcquistoByDescrizione(codEdicola, descrizione);
	}
	
	@Override
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicolaByCodEdicolaOrDescr(Integer codEdicola, String descrizione) {
		return repository.getProdottiNonEditorialiEdicolaByCodEdicolaOrDescr(codEdicola, descrizione);
	}
	
	@Override
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicolaByCodOrDescr(Integer codEdicola, Long codProdotto, String descrizione) {
		return repository.getProdottiNonEditorialiEdicolaByCodOrDescr(codEdicola, codProdotto, descrizione);
	}

	@Override
	public void deleteDettagliBollaProdotto(Integer codEdicola, Long codProdottoInterno) {
		repository.deleteDettagliBollaProdotto(codEdicola, codProdottoInterno);
	}

	@Override
	public void deletePrezziAcquistoProdotto(Integer codEdicola, Long codProdottoInterno) {
		repository.deletePrezziAcquistoProdotto(codEdicola, codProdottoInterno);
	}

	@Override
	public ProdottiNonEditorialiCategoriaEdicolaVo getProdottiNonEditorialiCategoriaEdicolaVo(Long codCategoria, Integer codEdicola) {
		return repository.getProdottiNonEditorialiCategoriaEdicolaVo(codCategoria, codEdicola);
	}

	@Override
	public ProdottiNonEditorialiSottoCategoriaEdicolaVo getProdottiNonEditorialiSottoCategoriaEdicolaVo(Long codCategoria, Long codSottoCategoria, Integer codEdicola) {
		return repository.getProdottiNonEditorialiSottoCategoriaEdicolaVo(codCategoria, codSottoCategoria, codEdicola);
	}

	@Override
	public List<ProdottiNonEditorialiCategoriaEdicolaVo> getCategorieProdottiNonEditorialiEdicola(Integer codEdicola) {
		return repository.getCategorieProdottiNonEditorialiEdicola(codEdicola);
	}

	@Override
	public List<ReportMagazzinoPneDto> getReportMagazzinoPne(Integer codRivendita, Timestamp dataIniziale, Timestamp dataFinale, List<Integer> codContoList, List<Integer> codCategoriaList, List<Integer> codCausaleList, Integer codProdotto) {
		return repository.getReportMagazzinoPne(codRivendita, dataIniziale, dataFinale, codContoList, codCategoriaList, codCausaleList, codProdotto);
	}
	
	@Override
	public List<GiacenzaPneDto> getGiacenzaPne(Integer codEdicola, Long codCategoria, Long codSottoCategoria, Long codProdotto) {
		return repository.getGiacenzaPne(codEdicola, codCategoria, codSottoCategoria, codProdotto);
	}

	@Override
	public void updateCategoryPosition(Integer posizione, Long idCat, Integer codEdicola) {
		repository.updateCategoryPosition(posizione, idCat, codEdicola);
	}

	@Override
	public void updateSubCategoryPosition(Integer posizione, Long idCat, Long idSCat, Integer codEdicola) {
		repository.updateSubCategoryPosition(posizione, idCat, idSCat, codEdicola);
	}

	@Override
	public void updateSubCategoryPosition(Integer posizione, Long idProd) {
		repository.updateSubCategoryPosition(posizione, idProd);
	}

	@Override
	public boolean codiceProdottoEdicolaExists(String codiceProdotto, Integer codEdicola) {
		return repository.codiceProdottoEdicolaExists(codiceProdotto, codEdicola);
	}
	
	@Override
	public void deleteProdottiNonEditorialiBollaDettaglio(Long idDocumento) {
		repository.deleteProdottiNonEditorialiBollaDettaglio(idDocumento);
	}
	
	@Override
	public void deleteBolleProdottiVariEdicolaBeforeDate(Integer codEdicola, Date date) {
		repository.deleteBolleProdottiVariEdicolaBeforeDate(codEdicola, date);
	}
	
	@Override
	public ProdottiNonEditorialiCausaliContabilitaVo getCausaleContabilita(Integer tipoCausale, Integer codiceCausale) {
		return repository.getCausaleContabilita(tipoCausale, codiceCausale);
	}
	
	@Override
	public List<ProdottiNonEditorialiCausaliContabilitaVo> getCausaliIva(Integer aliquota) {
		return repository.getCausaliIva(aliquota);
	}
	
	@Override
	public List<ProdottiNonEditorialiAliquotaIvaVo> getListAliquoteIva() {
		return repository.getListAliquoteIva();
	}
	
	@Override
	public ProdottiNonEditorialiVo getFirstProdottoNonEditorialeEdicola(Long codCategoria, Long codSottoCategoria, Integer codEdicola) {
		return repository.getFirstProdottoNonEditorialeEdicola(codCategoria, codSottoCategoria, codEdicola);
	}
	
	@Override
	public ProdottiNonEditorialiSottoCategoriaEdicolaVo getFirstSottocategoriaNonEditorialeEdicola(Long codCategoria, Integer codEdicola) {
		return repository.getFirstSottocategoriaNonEditorialeEdicola(codCategoria, codEdicola);
	}
	
	@Override
	public List<Map<String, List<Map<String, List<ProdottoDto>>>>> getProdottiNonEditorialiTree(Integer codEdicolaProdottiDaEscudere) {
		List<ProdottiNonEditorialiGenericaVo> list = getProdottiNonEditoriali(null, null, codEdicolaProdottiDaEscudere);
		List<Map<String, List<Map<String, List<ProdottoDto>>>>> retList = new ArrayList<Map<String,List<Map<String,List<ProdottoDto>>>>>();
		Group<ProdottiNonEditorialiGenericaVo> groupCat = group(list, by(on(ProdottiNonEditorialiGenericaVo.class).getCodCategoria()));
		Map<String, List<Map<String, List<ProdottoDto>>>> mapCat = new HashMap<String, List<Map<String,List<ProdottoDto>>>>();
		for (Group<ProdottiNonEditorialiGenericaVo> subgroupCat : groupCat.subgroups()) {
			List<Map<String, List<ProdottoDto>>> list1 = new ArrayList<Map<String,List<ProdottoDto>>>();
			List<ProdottiNonEditorialiGenericaVo> findAll = subgroupCat.findAll();
			Group<ProdottiNonEditorialiGenericaVo> groupSubcat = group(findAll, by(on(ProdottiNonEditorialiGenericaVo.class).getCodSottoCategoria()));
			for (Group<ProdottiNonEditorialiGenericaVo> subgroupSubcat : groupSubcat.subgroups()) {
				List<ProdottiNonEditorialiGenericaVo> findAllSub = subgroupSubcat.findAll();
				Map<String, List<ProdottoDto>> map2 = new HashMap<String, List<ProdottoDto>>();
				List<ProdottoDto> prodotti = new ArrayList<ProdottoDto>();
				for (ProdottiNonEditorialiGenericaVo pgvo : findAllSub) {
					ProdottoDto dto = new ProdottoDto();
					dto.setDescrizione(pgvo.getDescrizioneProdottoA());
					dto.setCodProdottoInterno(pgvo.getCodProdottoInterno());
					dto.setCodCategoria(pgvo.getCodCategoria());
					dto.setCodSubCategoria(pgvo.getCodSottoCategoria());
					prodotti.add(dto);
				}
				map2.put(findAllSub.get(0).getSottocategoria().getDescrizione(), prodotti);
				list1.add(map2);
			}
			String descrizione = findAll.get(0).getCategoria().getDescrizione();
			mapCat.put(descrizione, list1);
		}
		retList.add(mapCat);
		return retList;
	}
	
	@Override
	public List<Map<String, List<Map<String, List<ProdottoDto>>>>> getProdottiNonEditorialiEdicolaTree(Integer codEdicola) {
		List<Map<String, List<Map<String, List<ProdottoDto>>>>> retList = new ArrayList<Map<String,List<Map<String,List<ProdottoDto>>>>>();
		Map<String, List<Map<String, List<ProdottoDto>>>> mapCat = new LinkedHashMap<String, List<Map<String,List<ProdottoDto>>>>();
		Map<String, List<ProdottoDto>> mapProdottiSottocategorie = buildMapProdottiSottocategorie(codEdicola);
		Map<String, List<ProdottiNonEditorialiSottoCategoriaEdicolaVo>> mapSottocategorieProdotti = buildMapProdottiNonEditorialiSottoCategoriaEdicola(codEdicola);
		List<ProdottiNonEditorialiCategoriaEdicolaVo> listCatEdicola = getCategorieProdottiNonEditorialiEdicola(codEdicola);
		for (ProdottiNonEditorialiCategoriaEdicolaVo vo : listCatEdicola) {
			List<Map<String, List<ProdottoDto>>> list1 = new ArrayList<Map<String,List<ProdottoDto>>>();
			List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> sottocategorie = mapSottocategorieProdotti.get(vo.getPk().toString());
			String catKey = vo.getDescrizione() + "|" + vo.getPk().getCodCategoria();
			if (sottocategorie != null) {
				for (ProdottiNonEditorialiSottoCategoriaEdicolaVo svo : sottocategorie) {
					String keySottocategoria = svo.getPk().getCodCategoria() + "|" + svo.getPk().getCodSottoCategoria();
					List<ProdottoDto> prodotti = new ArrayList<ProdottoDto>();
					Map<String, List<ProdottoDto>> map2 = new LinkedHashMap<String, List<ProdottoDto>>();
					List<ProdottoDto> listProdotti = mapProdottiSottocategorie.get(keySottocategoria); 
					if (listProdotti != null) {
						for (ProdottoDto pgvo : listProdotti) {
							if ((pgvo.getCodCategoria().equals(vo.getPk().getCodCategoria())) && (pgvo.getCodSubCategoria().equals(svo.getPk().getCodSottoCategoria()))) {
								prodotti.add(pgvo);
							}
						} 
					}
					String scatKey = svo.getDescrizione() + "|" + keySottocategoria;
					map2.put(scatKey, prodotti);
					list1 = mapCat.get(catKey) != null ? mapCat.get(catKey) : list1;
					list1.add(map2);
				}
			}
			mapCat.put(catKey, list1);
		}
		retList.add(mapCat);
		return retList;
	}

	/**
	 * Costruisce una mappa con:
	 * key = codice categoria | codice edicola
	 * value = lista di ProdottiNonEditorialiSottoCategoriaEdicolaVo della categoria
	 * 
	 * @param Integer codEdicola
	 * @return Map<String, List<ProdottiNonEditorialiSottoCategoriaEdicolaVo>>
	 */
	private Map<String, List<ProdottiNonEditorialiSottoCategoriaEdicolaVo>> buildMapProdottiNonEditorialiSottoCategoriaEdicola(Integer codEdicola) {
		Map<String, List<ProdottiNonEditorialiSottoCategoriaEdicolaVo>> map = new HashMap<String, List<ProdottiNonEditorialiSottoCategoriaEdicolaVo>>();
		List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> sottocategorie = getProdottiNonEditorialiSottoCategorieEdicolaVo(null, codEdicola);
		Group<ProdottiNonEditorialiSottoCategoriaEdicolaVo> groupCategorie = group(sottocategorie, by(on(ProdottiNonEditorialiSottoCategoriaEdicolaVo.class).getPk().getCodCategoria()));
		for (Group<ProdottiNonEditorialiSottoCategoriaEdicolaVo> groupSottoCategoria : groupCategorie.subgroups()) {
			List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> listSottoCategorie = sort(groupSottoCategoria.findAll(), on(ProdottiNonEditorialiSottoCategoriaEdicolaVo.class).getPosizione());
			ProdottiNonEditorialiSottoCategoriaEdicolaVo pnesc = (listSottoCategorie != null && !listSottoCategorie.isEmpty()) ? listSottoCategorie.get(0) : null;
			if (pnesc != null) {
				String key = pnesc.getPk().getCodCategoria() + "|" + pnesc.getPk().getCodEdicola();
				map.put(key, listSottoCategorie);
			}
		}
		return map;
	}

	/**
	 * Costruisce una mappa con:
	 * key = codice categoria | codice sottocategoria
	 * value = lista di ProdottiNonEditorialiVo della sottocategoria
	 * 
	 * @param Integer codEdicola 
	 * @return Map<String, List<ProdottoDto>>
	 */
	private Map<String, List<ProdottoDto>> buildMapProdottiSottocategorie(Integer codEdicola) {
		Map<String, List<ProdottoDto>> map = new HashMap<String, List<ProdottoDto>>();
		List<ProdottoDto> list = getProdottiNonEditorialiEdicolaDto(codEdicola);
		Group<ProdottoDto> groupCategorie = group(list, by(on(ProdottoDto.class).getCodCategoria()));
		for (Group<ProdottoDto> groupCategoria : groupCategorie.subgroups()) {
			List<ProdottoDto> listCategorie = groupCategoria.findAll();
			Group<ProdottoDto> groupSottocategorie = group(listCategorie, by(on(ProdottoDto.class).getCodSubCategoria()));
			for (Group<ProdottoDto> groupSottocategoria : groupSottocategorie.subgroups()) {
				List<ProdottoDto> listScat = sort(groupSottocategoria.findAll(), on(ProdottoDto.class).getPosizione());
				ProdottoDto prodottiNonEditorialiVo = (listScat != null && !listScat.isEmpty()) ? listScat.get(0) : null;
				if (prodottiNonEditorialiVo != null) {
					String key = prodottiNonEditorialiVo.getCodCategoria() + "|" + prodottiNonEditorialiVo.getCodSubCategoria();
					map.put(key, listScat);
				}
			}
		}
		return map;
	}
	
	@Override
	public void saveProdottoEdicola(ProdottiNonEditorialiVo prodottoEdicola, String prezziSelected, String validitaSelected) {
		deletePrezziProdottoEdicola(prodottoEdicola.getEdicola().getCodEdicola(), prodottoEdicola.getCodProdottoInterno());
		StringTokenizer stPrezzi = new StringTokenizer(prezziSelected, ",");
		StringTokenizer stValidita = new StringTokenizer(validitaSelected, ",");
		TreeMap<Date, Float> map = new TreeMap<Date, Float>();
		while (stPrezzi.hasMoreTokens()) {
			String prezzoStr = stPrezzi.nextToken();
			String validitaStr = stValidita.nextToken();
			if (!prezzoStr.equals("") && !validitaStr.equals("")) {
				Float prezzo = NumberUtils.parseNumber(prezzoStr, Float.class);
				Date validita = null;
				try {
					validita = new Date(DateUtilities.parseDate(validitaStr, DateUtilities.FORMATO_DATA_SLASH).getTime());
				} catch (ParseException e) {
					throw new IGerivRuntimeException();
				}
				map.put(validita, prezzo);
			}
		}
		saveBaseVo(prodottoEdicola);	
		TreeMap<Date, Float> reverseOrderedMap = new TreeMap<Date, Float>(Collections.reverseOrder());
		reverseOrderedMap.putAll(map);
		Calendar cal = Calendar.getInstance();
		cal.set(2999, 12, 31);
		for (Iterator<Map.Entry<Date, Float>> iter = reverseOrderedMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<Date, Float> entry = (Map.Entry<Date, Float>) iter.next();
			Date validita = (Date) entry.getKey();
			Float prezzo = (Float) entry.getValue();
			ProdottoNonEditorialePrezzoVenditaPk pk = new ProdottoNonEditorialePrezzoVenditaPk();
			pk.setCodEdicola(prodottoEdicola.getEdicola().getCodEdicola());
			pk.setCodProdottoInterno(prodottoEdicola.getCodProdottoInterno());
			pk.setDataValiditaFinale(new java.sql.Date(cal.getTime().getTime()));
			ProdottiNonEditorialiPrezzoVenditaVo pnev = new ProdottiNonEditorialiPrezzoVenditaVo();
			pnev.setPk(pk);
			pnev.setDataValiditaIniziale(new java.sql.Date(validita.getTime()));
			pnev.setPrezzoLisitino(prezzo);
			pnev.setScontoPercentuale(0f);
			pnev.setScontoValore(0f);
			pnev.setEdicola(prodottoEdicola.getEdicola());
			saveBaseVo(pnev);
			cal.setTime(validita);
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
	}
	
	@Override
	public void saveBollaProdottiVari(ProdottiNonEditorialiBollaVo bolla, List<ProdottiNonEditorialiPrezziAcquistoVo> prezziAcquistoEdit, List<ProdottiNonEditorialiVo> listProdotti, Integer codFornitore, Integer codEdicola, Boolean isNew) throws it.dpe.igeriv.exception.ConstraintViolationException {
		try {
			deleteBollaProdottiVari(bolla.getIdDocumento(), codFornitore, codEdicola);
			deleteProdottiNonEditorialiPrezziAcquisto(prezziAcquistoEdit);
			mergeVoList(listProdotti);
			saveVoList(prezziAcquistoEdit);
			saveBaseVo(bolla);
		} catch (ConstraintViolationException e) {
			throw new it.dpe.igeriv.exception.ConstraintViolationException();
		}
	}
	
	/**
	 * @param list
	 */
	private void deleteProdottiNonEditorialiPrezziAcquisto(List<ProdottiNonEditorialiPrezziAcquistoVo> list) {
		if (!list.isEmpty()) {
			ProdottiNonEditorialiPrezziAcquistoVo pnepa = list.get(0);
			List<Long> listCodProdotto = extract(list, on(ProdottiNonEditorialiPrezziAcquistoVo.class).getPk().getCodProdotto());
			deleteProdottiNonEditorialiPrezziAcquisto(pnepa.getPk().getCodEdicola(), listCodProdotto);
		}
	}
	
	
	@Override
	public void deleteBollaProdottiVari(Long idDocumento, Integer codFornitore, Integer codEdicola) {
		deleteBollaProdottiVari(idDocumento);
		List<ProdottiNonEditorialiPrezziAcquistoVo> prezziAcquistoDelete = getProdottiNonEditorialiPrezzoAcquistoNonUsatiInBolla(codEdicola, codFornitore);
		for (ProdottiNonEditorialiPrezziAcquistoVo vo : (List<ProdottiNonEditorialiPrezziAcquistoVo>) prezziAcquistoDelete) {
			deleteVo(vo);
		}
	}
	
	@Override
	public void deleteProdottoEdicola(ProdottiNonEditorialiVo vo, Boolean bDeleteDependenciesProdotto) {
		if (bDeleteDependenciesProdotto) {
			deleteDettagliBollaProdotto(vo.getEdicola().getCodEdicola(), vo.getCodProdottoInterno());
			deletePrezziAcquistoProdotto(vo.getEdicola().getCodEdicola(), vo.getCodProdottoInterno());
		}
		deleteVo(vo);
	}
	
	@Override
	public void deleteCategoria(Long codCategoria, Integer codEdicola, boolean recursive) {
		ProdottiNonEditorialiCategoriaEdicolaVo cat = getProdottiNonEditorialiCategoriaEdicolaVo(codCategoria, codEdicola);
		if (recursive) {
			List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> sottocategorie = getProdottiNonEditorialiSottoCategorieEdicolaVo(codCategoria, codEdicola);
			for (ProdottiNonEditorialiSottoCategoriaEdicolaVo vo : sottocategorie) {
				deleteSottoCategoria(vo.getPk().getCodCategoria(), vo.getPk().getCodSottoCategoria(), codEdicola, recursive);
			}
		}
		deleteVo(cat);
	}
	
	@Override
	public void deleteSottoCategoria(Long codCategoria, Long codSottoCategoria, Integer codEdicola, boolean recursive) {
		List<ProdottiNonEditorialiVo> prodottiNonEditorialiEdicola = getProdottiNonEditorialiEdicola(codCategoria, codSottoCategoria, codEdicola);
		ProdottiNonEditorialiSottoCategoriaEdicolaVo scvo = null;
		for (ProdottiNonEditorialiVo vo : prodottiNonEditorialiEdicola) {
			scvo = vo.getSottocategoria();
			deleteProdottoEdicola(vo, recursive);
		}
		if (scvo == null) {
			scvo = getProdottiNonEditorialiSottoCategoriaEdicolaVo(codCategoria, codSottoCategoria, codEdicola);
		}
		deleteVo(scvo);
	}
	
	public void updateProductCategory(ProdottiNonEditorialiVo vo, List<ProdottiNonEditorialiVo> prodotti) {
		if (prodotti != null && !prodotti.isEmpty()) {
			saveVoList(prodotti);
		}
		saveBaseVo(vo);
	}
	
	@Override
	public void updateCategoryPositions(List<Long> catIds, Integer codEdicola) {
		for (int i = 0; i < catIds.size(); i++) {
			updateCategoryPosition(i, (Long) catIds.get(i), codEdicola);
		}
	}
	
	@Override
	public void updateSubCategoryPositions(List<Long> catIds, List<Long> subcatIds, Integer codEdicola) {
		for (int i = 0; i < catIds.size(); i++) {
			updateSubCategoryPosition(i, (Long) catIds.get(i), (Long) subcatIds.get(i), codEdicola);
		}
	}
	
	@Override
	public void updateProductPositions(List<Long> prodIds) {
		for (int i = 0; i < prodIds.size(); i++) {
			updateSubCategoryPosition(i, (Long) prodIds.get(i));
		}
	}
	
	@Override
	public void saveProdottiTemplate(String idProdottiEdicola, Integer codEdicola) {
		StringTokenizer st = new StringTokenizer(idProdottiEdicola, ",");
		while (st.hasMoreElements()) {
			String idStr = st.nextToken().trim();
			if (!idStr.equals("") && !idStr.equals(",")) {
				Long codProdottoInterno = new Long(idStr);
				ProdottiNonEditorialiGenericaVo vo = getProdottiNonEditorialiGenericaVo(codProdottoInterno);
				if (vo != null) {
					ProdottiNonEditorialiVo pne = new ProdottiNonEditorialiVo();
					AnagraficaEdicolaVo edicola = edicoleService.getAnagraficaEdicola(codEdicola);
					ProdottiNonEditorialiCategoriaEdicolaVo categoria = getProdottiNonEditorialiCategoriaEdicolaVo(vo.getCodCategoria(), codEdicola);
					ProdottiNonEditorialiSottoCategoriaEdicolaVo sottocategoria = getProdottiNonEditorialiSottoCategoriaEdicolaVo(vo.getCodCategoria(), vo.getCodSottoCategoria(), codEdicola);
					if (categoria == null) {
						ProdottiNonEditorialiCategoriaVo categoriaGenerica = getProdottiNonEditorialiCategoriaVo(vo.getCodCategoria());
						categoria = new ProdottiNonEditorialiCategoriaEdicolaVo();
						ProdottiNonEditorialiCategoriaEdicolaPk pk = new ProdottiNonEditorialiCategoriaEdicolaPk();
						pk.setCodCategoria(categoriaGenerica.getCodCategoria());
						pk.setCodEdicola(codEdicola);
						categoria.setPk(pk);
						categoria.setDescrizione(categoriaGenerica.getDescrizione());
						categoria.setImmagine(categoriaGenerica.getImmagine());
						saveBaseVo(categoria);
					}
					if (sottocategoria == null) {
						ProdottiNonEditorialiSottoCategoriaVo sottocategoriaGenerica = getProdottiNonEditorialiSottoCategoriaVo(vo.getCodCategoria(), vo.getCodSottoCategoria());
						sottocategoria = new ProdottiNonEditorialiSottoCategoriaEdicolaVo();
						ProdottiNonEditorialiSottoCategoriaEdicolaPk pk = new ProdottiNonEditorialiSottoCategoriaEdicolaPk();
						pk.setCodCategoria(sottocategoriaGenerica.getPk().getCodCategoria());
						pk.setCodSottoCategoria(sottocategoriaGenerica.getPk().getCodSottoCategoria());
						pk.setCodEdicola(codEdicola);
						sottocategoria.setPk(pk);
						sottocategoria.setDescrizione(sottocategoriaGenerica.getDescrizione());
						sottocategoria.setImmagine(sottocategoriaGenerica.getImmagine());
						saveBaseVo(sottocategoria);
					}
					pne.setCodProdottoInterno(codProdottoInterno);
					pne.setEdicola(edicola);
					pne.setDescrizioneProdottoA(vo.getDescrizioneProdottoA());
					pne.setAliquota(vo.getAliquota());
					pne.setBarcode(vo.getBarcode());
					pne.setCodCategoria(vo.getCodCategoria());
					pne.setCodProdottoEsterno(vo.getCodProdottoEsterno());
					pne.setCodSottoCategoria(vo.getCodSottoCategoria());
					pne.setDescrizioneProdottoB(vo.getDescrizioneProdottoB());
					pne.setNomeImmagine(vo.getNomeImmagine());
					pne.setUnitaMinimaIncremento(vo.getUnitaMinimaIncremento());
					pne.setUnitaMisura(vo.getUnitaMisura());
					saveBaseVo(pne);
				}
			}
		}
	}
	
	@Override
	public List<ProdottiNonEditorialiBollaVo> getReseProdottiVariEdicola(Integer codEdicola, Integer codFornitore, Timestamp dataDocumento, String numeroDocumento) {
		return repository.getReseProdottiVariEdicola(codEdicola, codFornitore, dataDocumento, numeroDocumento);
	}
	
	@Override
	public ProdottiNonEditorialiBollaVo getResaProdottiVariEdicola(Long idDocumento) {
		return repository.getResaProdottiVariEdicola(idDocumento);
	}
	
	@Override
	public void deleteBollaResaProdottiVari(Integer codEdicola, Long codResa, Integer numeroDocumento) {
		if (codEdicola != null && numeroDocumento != null) {
			ProdottiNonEditorialiProgressiviFatturazioneVo prog = contabilitaService.getNextProgressivoFatturazioneVo(codEdicola, IGerivConstants.TIPO_DOCUMENTO_BOLLA_RESA_PRODOTTI_VARI, DateUtilities.getDateFirstDayYear());
			if (prog != null && prog.getPk().getData().getTime() == DateUtilities.floorDay(DateUtilities.getDateFirstDayYear()).getTime() && numeroDocumento.equals(prog.getProgressivo().intValue())) {
				contabilitaService.setNextProgressivoFatturazione(codEdicola, IGerivConstants.TIPO_DOCUMENTO_BOLLA_RESA_PRODOTTI_VARI, DateUtilities.getDateFirstDayYear(), new Long(numeroDocumento - 1));
			}
		}
		repository.deleteBollaResaProdottiVari(codResa);
	}
	
	@Override
	public Timestamp getLastDataBollaResa(Integer codFornitore, Integer codEdicola, Long codResaExclude) {
		return repository.getLastDataBollaResa(codFornitore, codEdicola, codResaExclude);
	}
	
	@Override
	public Long getQuantitaResa(Integer codEdicola, Long codProdotto) {
		return repository.getQuantitaResa(codEdicola, codProdotto);
	}
	
	@Override
	public Long getQuantitaDistribuita(Integer codEdicola, Long codProdotto) {
		return repository.getQuantitaDistribuita(codEdicola, codProdotto);
	}
	
	@Override
	public void saveBollaResaProdottiVari(Integer codEdicola, ProdottiNonEditorialiBollaVo resa, Boolean isNew) {
		contabilitaService.setNextProgressivoFatturazione(codEdicola, IGerivConstants.TIPO_DOCUMENTO_BOLLA_RESA_PRODOTTI_VARI, DateUtilities.getDateFirstDayYear(), new Long(resa.getNumeroDocumento()));
		if (isNew == null || !isNew) {
			repository.deleteBollaResaProdottiVari(resa.getIdDocumento());
		}
		repository.saveBaseVo(resa);
	}
	
	@Override
	public List<BollaResaProdottiVariDto> getDettagliBollaResaProdottiVariDto(Long idResa) {
		return repository.getDettagliBollaResaProdottiVariDto(idResa);
	}
	
	@Override
	public ProdottiNonEditorialiDocumentiEmessiVo getProdottiNonEditorialiDocumentiEmessi(Long codForn, Long idDoc) {
		return repository.getProdottiNonEditorialiDocumentiEmessi(codForn, idDoc);
	}
	
}
