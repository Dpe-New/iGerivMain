package it.dpe.igeriv.bo.prodotti;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.BollaResaProdottiVariDto;
import it.dpe.igeriv.dto.CategoriaDto;
import it.dpe.igeriv.dto.GiacenzaPneDto;
import it.dpe.igeriv.dto.ProdottoDto;
import it.dpe.igeriv.dto.ReportMagazzinoPneDto;
import it.dpe.igeriv.dto.RichiestaProdottoDto;
import it.dpe.igeriv.dto.SottoCategoriaDto;
import it.dpe.igeriv.vo.ProdottiNonEditorialiAliquotaIvaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCausaliContabilitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCausaliMagazzinoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiDocumentiEmessiVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiGenericaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Interfaccia prodotti
 * 
 * @author mromano
 *
 */
public interface ProdottiService extends BaseService {
	
	/**
	 * @param codFiegDl
	 * @param cpu
	 * @param nomeImmagine
	 */
	public void updateFakeImgMiniaturaName(Integer codFiegDl, Integer cpu, String nomeImmagine);
	
	/**
	 * @param codFiegDl
	 * @param codDpeWebEdicola
	 * @param codice
	 * @param descrizione
	 * @param categoria
	 * @param sottocategoria
	 * @param barcode
	 * @param prezzo
	 * @return
	 */
	public List<ProdottoDto> getProdotti(Integer codEdicola, Integer codFiegDl, Long codContabileCliente, String codiceEsterno, String descrizione, Long categoria, Long sottocategoria, String barcode, Float prezzo);
	
	/**
	 * @param codiceProdottoFornitore
	 * @param codFornitore
	 * @return
	 */
	public Integer getGiacenzaProdottoDl(String codiceProdottoFornitore, Integer codFornitore);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param codice
	 * @param descrizione
	 * @param categoria
	 * @param sottocategoria
	 * @param barcode
	 * @param prezzo
	 * @return
	 */
	public List<RichiestaProdottoDto> getRichiesteProdotti(Integer codFornitore, Integer codEdicolaDl, String descrizione, String stato, Timestamp dataDa, Timestamp dataA);
	
	/**
	 * @param Integer codFiegDl
	 * @return List<ProdottiCategoriaVo>
	 */
	public List<ProdottiNonEditorialiCategoriaEdicolaVo> getCategorie(Integer codFiegDl, Integer codEdicola);

	/**
	 * @param Integer codFiegDl
	 * @param Integer categoria
	 * @return List<ProdottiSottoCategoriaVo>
	 */
	public List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> getSottocategorieProdottiDl(Integer codFiegDl, Integer codEdicola, Long categoria);
	
	/**
	 * @param codFiegDl
	 * @param codice
	 * @return
	 */
	public RichiestaProdottoDto getRichiestaRifornimentoProdotto(Long codice);

	/**
	 * @param codFiegDl
	 * @param codice
	 * @param codRichiestaRifornimento
	 * @return
	 */
	public ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo getProdottiRichiesteRifornimentoDettaglioVo(Long codice, Long codRichiestaRifornimento);
	
	/**
	 * @return
	 */
	public List<CategoriaDto> getCategoriePne(Integer codEdicola);

	/**
	 * @param categoria 
	 * @return
	 */
	public List<SottoCategoriaDto> getSottocategoriePne(Long categoria, Integer codEdicola);

	/**
	 * @param categoria
	 * @param sottocategoria
	 * @return
	 */
	public List<ProdottoDto> getProdottiNonEditoriali(Long categoria, Long sottocategoria);

	/**
	 * @param barcode
	 * @param codFornitore 
	 * @return
	 */
	public List<ProdottoDto> getProdottoNonEditorialeByBarcode(Integer codEdicola, String barcode, Integer codFornitore, Boolean soloProdottiForniti);
	
	/**
	 * @param codiceProdottoInterno
	 * @return
	 */
	public ProdottiNonEditorialiVo getProdottoNonEditorialeVo(Long codiceProdottoInterno);
	
	/**
	 * @return
	 */
	public List<ProdottiNonEditorialiGenericaVo> getProdottiNonEditoriali(Long categoria, Long sottocategoria, Integer codEdicolaProdottiDaEscudere);
	
	/**
	 * @param includiGiacenza 
	 * @return
	 */
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicola(Long categoria, Long sottocategoria, Integer codEdicola);
	
	/**
	 * @param codCategoria
	 * @param codEdicola
	 * @return
	 */
	public List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> getProdottiNonEditorialiSottoCategorieEdicolaVo(Long codCategoria, Integer codEdicola);
	
	/**
	 * @param codProdottoInterno
	 * @return
	 */
	public ProdottiNonEditorialiGenericaVo getProdottiNonEditorialiGenericaVo(Long codProdottoInterno);

	/**
	 * @param codProdotto
	 * @param codEdicola
	 * @return
	 */
	public ProdottiNonEditorialiVo getProdottiNonEditorialiVo(Long codProdotto, Integer codEdicola);

	/**
	 * @param codEdicola
	 * @param codProdottoInterno
	 */
	public void deletePrezziProdottoEdicola(Integer codEdicola, Long codProdottoInterno);
	
	/**
	 * @param codEdicola
	 * @param codProdotto
	 */
	public void deleteProdottiNonEditorialiPrezziAcquisto(Integer codEdicola, List<Long> codProdotto);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public List<ProdottoDto> getProdottiNonEditorialiEdicolaDto(Integer codEdicola);
	
	/**
	 * @param idEdicola
	 * @param inputText
	 * @return
	 */
	public List<ProdottoDto> getProdottiVariByDescrizioneBarcode(Integer codEdicola, String inputText, String barcode);
	
	/**
	 * @param codCategoria
	 * @return
	 */
	public ProdottiNonEditorialiCategoriaVo getProdottiNonEditorialiCategoriaVo(Long codCategoria);

	/**
	 * @param codCategoria
	 * @param codSottoCategoria
	 * @return
	 */
	public ProdottiNonEditorialiSottoCategoriaVo getProdottiNonEditorialiSottoCategoriaVo(Long codCategoria, Long codSottoCategoria);
	
	/**
	 * @param codEdicola
	 * @param codFornitore
	 * @param dataDocumento
	 * @param numeroDocumento
	 * @param excludeDl 
	 * @return
	 */
	public List<ProdottiNonEditorialiBollaVo> getBolleProdottiVariEdicola(Integer codEdicola, Integer codFornitore, Integer codiceCausale, Timestamp dataDocumento, String numeroDocumento, boolean excludeDl);
	
	/**
	 * @param codEdicola
	 * @param idDocumento
	 * @return
	 */
	public ProdottiNonEditorialiBollaVo getBolleProdottiVariEdicola(Integer codEdicola, Long idDocumento);
	
	/**
	 * @param idDocumento
	 * @return
	 */
	public ProdottiNonEditorialiBollaVo getBolleProdottiVariEdicola(Long idDocumento);
	
	/**
	 * @param codEdicola
	 * @param codFornitore
	 * @return
	 */
	public List<ProdottiNonEditorialiBollaVo> getBollaProdottiVariEdicolaByFornitore(Integer codEdicola, Integer codFornitore);

	/**
	 * @return
	 */
	public List<ProdottiNonEditorialiCausaliMagazzinoVo> getCausali();

	/**
	 * @param codiceCausale
	 * @return
	 */
	public ProdottiNonEditorialiCausaliMagazzinoVo getCausaleMagazzino(Integer codiceCausale);

	/**
	 * @param codEdicola
	 * @param idProdottoFornitore
	 * @return
	 */
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezziAcquisto(Integer codEdicola, Integer codFornitore, Boolean soloProdottiForniti, String idProdottoFornitore);
	
	/**
	 * @param codEdicola
	 * @param idProdottoFornitore
	 * @return
	 */
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezziAcquisto(Integer codEdicola, Integer codFornitore, Boolean soloProdottiForniti, String idProdottoFornitore, boolean getGiacenza);

	/**
	 * @param descrizione
	 * @param codFornitore 
	 * @return
	 */
	public List<ProdottoDto> getProdottiNonEditorialiEdicolaByDescrizione(String descrizione, Integer codEdicola, Integer codFornitore, Boolean soloProdottiForniti);

	/**
	 * @param codEdicola
	 * @param codResa
	 * @param numeroDocumento
	 */
	public void deleteBollaProdottiVari(Long codResa);

	/**
	 * @param codEdicola
	 * @param prodotto
	 * @param codiceProdottoFornitore
	 * @return
	 */
	public ProdottiNonEditorialiPrezziAcquistoVo getProdottiNonEditorialiPrezzoAcquisto(Integer codEdicola, Long codiceProdotto, String codiceProdottoFornitore);
	
	/**
	 * @param codEdicola
	 * @param codiceFornitore
	 * @param codiceProdottoFornitore
	 * @return
	 */
	public ProdottiNonEditorialiPrezziAcquistoVo getProdottoNonEditorialiPrezzoAcquistoByCodFornitore(Integer codEdicola, Integer codiceFornitore, String codiceProdottoFornitore);
	
	/**
	 * @param codEdicola
	 * @param codFornitore
	 * @return
	 */
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezzoAcquistoNonUsatiInBolla(Integer codEdicola, Integer codFornitore);
	
	/**
	 * @param codEdicola
	 * @param codiceProdotto
	 * @param codiceProdottoFornitore
	 * @return
	 */
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezziAcquistoByDescrizione(Integer codEdicola, String descrizione);
	
	/**
	 * @param codEdicola
	 * @param codProdotto
	 * @param descrizione
	 * @return
	 */
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicolaByCodOrDescr(Integer codEdicola, Long codProdotto, String descrizione);
	
	/**
	 * @param codEdicola
	 * @param descrizione
	 * @return
	 */
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicolaByCodEdicolaOrDescr(Integer codEdicola, String descrizione);
	
	/**
	 * @param codEdicola
	 * @param codProdottoInterno
	 */
	public void deleteDettagliBollaProdotto(Integer codEdicola, Long codProdottoInterno);

	/**
	 * @param codEdicola
	 * @param codProdottoInterno
	 */
	public void deletePrezziAcquistoProdotto(Integer codEdicola, Long codProdottoInterno);

	/**
	 * @param codCategoria
	 */
	public ProdottiNonEditorialiCategoriaEdicolaVo getProdottiNonEditorialiCategoriaEdicolaVo(Long codCategoria, Integer codEdicola);

	/**
	 * @param codCategoria
	 * @param codSottoCategoria
	 * @param codEdicola
	 * @return
	 */
	public ProdottiNonEditorialiSottoCategoriaEdicolaVo getProdottiNonEditorialiSottoCategoriaEdicolaVo(Long codCategoria, Long codSottoCategoria, Integer codEdicola);

	/**
	 * @param codEdicola
	 */
	public List<ProdottiNonEditorialiCategoriaEdicolaVo> getCategorieProdottiNonEditorialiEdicola(Integer codEdicola);

	/**
	 * 
	 * @param codRivendita
	 * @param dataIniziale
	 * @param dataFinale
	 * @param codContoList
	 * @param codCategoriaList
	 * @param codCausaleList
	 * @param codProdotto
	 * @return
	 */
	public List<ReportMagazzinoPneDto> getReportMagazzinoPne(Integer codRivendita, Timestamp dataIniziale, Timestamp dataFinale, List<Integer> codContoList, List<Integer> codCategoriaList, List<Integer> codCausaleList, Integer codProdotto);
	
	/**
	 * @param codEdicola
	 * @param codCategoria
	 * @param codSottoCategoria
	 * @param codProdotto
	 * @return
	 */
	public List<GiacenzaPneDto> getGiacenzaPne(Integer codEdicola, Long codCategoria, Long codSottoCategoria, Long codProdotto);
	
	
	/**
	 * @param posizione
	 * @param idCat
	 * @param codEdicola
	 */
	public void updateCategoryPosition(Integer posizione, Long idCat, Integer codEdicola);
	
	/**
	 * @param posizione
	 * @param idCat
	 * @param codEdicola
	 */
	public void updateSubCategoryPosition(Integer posizione, Long idCat, Long idSCat, Integer codEdicola);

	/**
	 * @param posizione
	 * @param long1
	 * @param codEdicola
	 */
	public void updateSubCategoryPosition(Integer posizione, Long idProd);

	/**
	 * @param codiceProdotto
	 * @return
	 */
	public boolean codiceProdottoEdicolaExists(String codiceProdotto, Integer codEdicola);
	
	/**
	 * @param idDocumento
	 */
	public void deleteProdottiNonEditorialiBollaDettaglio(Long idDocumento);
	
	/**
	 * @param codDpeWebEdicola
	 * @param date
	 */
	public void deleteBolleProdottiVariEdicolaBeforeDate(Integer codEdicola, Date date);
	
	/**
	 * @return
	 */
	public List<ProdottiNonEditorialiAliquotaIvaVo> getListAliquoteIva();

	/**
	 * @param aliquota
	 * @return
	 */
	public List<ProdottiNonEditorialiCausaliContabilitaVo> getCausaliIva(Integer aliquota);

	/**
	 * @param tipoCausale
	 * @param codiceCausale
	 * @return
	 */
	public ProdottiNonEditorialiCausaliContabilitaVo getCausaleContabilita(Integer tipoCausale, Integer codiceCausale);
	
	/**
	 * @param codEdicolaProdottiDaEscudere
	 * @return
	 */
	public List<Map<String, List<Map<String, List<ProdottoDto>>>>> getProdottiNonEditorialiTree(Integer codEdicolaProdottiDaEscudere);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public List<Map<String, List<Map<String, List<ProdottoDto>>>>> getProdottiNonEditorialiEdicolaTree(Integer codEdicola);
	
	/**
	 * @param prodottoEdicola
	 * @param prezziSelected
	 * @param validitaSelected
	 */
	public void saveProdottoEdicola(ProdottiNonEditorialiVo prodottoEdicola, String prezziSelected, String validitaSelected);
	
	/**
	 * @param bolla
	 * @param prezziAcquistoEdit
	 * @param listProdotti
	 * @param codFornitore
	 * @param codEdicola
	 * @param isNew
	 * @throws it.dpe.igeriv.exception.ConstraintViolationException
	 */
	public void saveBollaProdottiVari(ProdottiNonEditorialiBollaVo bolla, List<ProdottiNonEditorialiPrezziAcquistoVo> prezziAcquistoEdit, List<ProdottiNonEditorialiVo> listProdotti, Integer codFornitore, Integer codEdicola, Boolean isNew) throws it.dpe.igeriv.exception.ConstraintViolationException;
	
	/**
	 * @param idDocumento
	 * @param codFornitore
	 * @param codEdicola
	 */
	public void deleteBollaProdottiVari(Long idDocumento, Integer codFornitore, Integer codEdicola);
	
	/**
	 * @param vo
	 * @param bDeleteDependenciesProdotto
	 */
	public void deleteProdottoEdicola(ProdottiNonEditorialiVo vo, Boolean bDeleteDependenciesProdotto);
	
	/**
	 * @param codCategoria
	 * @param codEdicola
	 * @param recursive
	 */
	public void deleteCategoria(Long codCategoria, Integer codEdicola, boolean recursive);
	
	/**
	 * @param codCategoria
	 * @param codSottoCategoria
	 * @param codEdicola
	 * @param recursive
	 */
	public void deleteSottoCategoria(Long codCategoria, Long codSottoCategoria, Integer codEdicola, boolean recursive);
	
	/**
	 * @param vo
	 * @param prodotti
	 */
	public void updateProductCategory(ProdottiNonEditorialiVo vo, List<ProdottiNonEditorialiVo> prodotti);
	
	/**
	 * @param catIds
	 * @param codEdicola
	 */
	public void updateCategoryPositions(List<Long> catIds, Integer codEdicola);
	
	/**
	 * @param catIds
	 * @param subcatIds
	 * @param codEdicola
	 */
	public void updateSubCategoryPositions(List<Long> catIds, List<Long> subcatIds, Integer codEdicola);
	
	/**
	 * @param prodIds
	 */
	public void updateProductPositions(List<Long> prodIds);
	
	/**
	 * @param idProdottiEdicola
	 * @param codEdicola
	 */
	public void saveProdottiTemplate(String idProdottiEdicola, Integer codEdicola);
	
	/**
	 * @param codCategoria
	 * @param codSottoCategoria
	 * @param codEdicola
	 * @return
	 */
	public ProdottiNonEditorialiVo getFirstProdottoNonEditorialeEdicola(final Long codCategoria, final Long codSottoCategoria, final Integer codEdicola);
	
	/**
	 * @param codCategoria
	 * @param codEdicola
	 * @return
	 */
	public ProdottiNonEditorialiSottoCategoriaEdicolaVo getFirstSottocategoriaNonEditorialeEdicola(final Long codCategoria, final Integer codEdicola);
	
	/**
	 * @param codEdicola
	 * @param codFornitore
	 * @param dataDocumento
	 * @param numeroDocumento
	 * @return
	 */
	public List<ProdottiNonEditorialiBollaVo> getReseProdottiVariEdicola(Integer codEdicola, Integer codFornitore, Timestamp dataDocumento, String numeroDocumento);
	
	/**
	 * @param idDocumento
	 * @return
	 */
	public ProdottiNonEditorialiBollaVo getResaProdottiVariEdicola(Long idDocumento);
	
	/**
	 * @param codResa
	 */
	public void deleteBollaResaProdottiVari(Integer codEdicola, Long codResa, Integer numeroDocumento);
	
	/**
	 * @param codFornitore
	 * @param codEdicola
	 * @return
	 */
	public Timestamp getLastDataBollaResa(Integer codFornitore, Integer codEdicola, Long codResaExclude);
	
	/**
	 * @param codEdicola
	 * @param codProdotto
	 * @return
	 */
	public Long getQuantitaResa(Integer codEdicola, Long codProdotto);
	
	/**
	 * @param codEdicola
	 * @param codProdotto
	 * @return
	 */
	public Long getQuantitaDistribuita(Integer codEdicola, Long codProdotto);
	
	/**
	 * @param resa
	 */
	public void saveBollaResaProdottiVari(Integer codEdicola, ProdottiNonEditorialiBollaVo resa, Boolean isNew);
	
	/**
	 * @param idResa
	 * @return
	 */
	public List<BollaResaProdottiVariDto> getDettagliBollaResaProdottiVariDto(Long idResa);
	
	/**
	 * @param codForn
	 * @param idDoc
	 * @return
	 */
	public ProdottiNonEditorialiDocumentiEmessiVo getProdottiNonEditorialiDocumentiEmessi(Long codForn, Long idDoc);
	
}
