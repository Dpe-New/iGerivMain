package it.dpe.igeriv.bo.scolastica;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.OrdineLibriDto;
import it.dpe.igeriv.vo.OrdineLibriDettVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo.ETrack;
import it.dpe.igeriv.vo.OrdineLibriVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

interface ScolasticaRepository  extends BaseRepository{
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param numeroOrdine
	 * @param codCliente 
	 * @return List<OrdineLibriDto>
	 */
	public List<OrdineLibriDto> findListOrdiniLibri(Integer codFiegDl, Integer codEdicola, Long numeroOrdine, Long codCliente);

	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param numeroOrdine
	 * @param codCliente
	 * @return
	 */
	public List<OrdineLibriDto> findListOrdiniPerDettaglio(Integer codFiegDl, Integer codEdicola, Long numeroOrdine, Long codCliente);
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param numeroOrdine
	 * @param codCliente 
	 * @param numeroCollo
	 * @return List<OrdineLibriVo>
	 */
	public List<OrdineLibriVo> findOrdiniLibri(Integer codFiegDl, Integer codEdicola, Long numeroOrdine, Long codCliente, Long numeroCollo);
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param codCliente
	 * @return Integer
	 */
	public Long insertNewOrdineLibri(Integer codFiegDl, Integer codEdicola,Long codCliente);
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param numeroOrdine
	 * @return boolean
	 */
	public boolean deleteOrdineLibri(Long numeroOrdine);
	
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param codCliente
	 * @return boolean
	 */
	public boolean existOrdineAttivo(Integer codFiegDl, Integer codEdicola,Long codCliente);
	
	/**
	 * 
	 * @param numeroOrdine
	 * @return
	 */
	public boolean visualizzareOrdineCliente(Long numeroOrdine);
	
	/**
	 * 
	 * @param codDl
	 * @param numeroOrdine
	 * @return
	 */
	public boolean existLibriDaConsegnare(Integer codDl, String numOrdineTxt);
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param codCliente
	 * @return
	 */
	public Long getOrdineAttivo(Integer codFiegDl, Integer codEdicola,Long codCliente);
	
	/**
	 * 
	 * @param numOrdine
	 * @return Integer
	 */
	public Long countLibriCarrello(Long numOrdine);
	
	/**
	 * 
	 * @param dettaglioLibro
	 */
	public void addLibriCarrello(OrdineLibriDettVo dettaglioLibro);
	
	
	/**
	 * 
	 * @param numeroOrdine
	 * @return
	 */
	public OrdineLibriVo getDettaglioOrdineLibri(Long numeroOrdine);
	
	/**
	 * 
	 * @param numeroOrdine
	 * @param track
	 * @return
	 */
	public OrdineLibriVo getDettaglioOrdineLibri(Long numeroOrdine, ETrack track);
	
	/**
	 * 
	 * @param numeroOrdine
	 * @return
	 */
	public OrdineLibriDettVo getInformazioneCopertinaUltimoLibroInserito(Long numeroOrdine);
	
	/**
	 * 
	 * @param numeroOrdine
	 * @param seqOrdine
	 * @return
	 */
	public OrdineLibriDettVo getInformazioneCopertinaLibro(Long numeroOrdine,Long seqOrdine);
	
	/**
	 * 
	 * @param numOrdine
	 * @return
	 */
	public BigDecimal sumPrezzoLibriCarrello(Long numOrdine);
	
	/**
	 * 
	 * @param numOrdine
	 * @return Integer 
	 */
	public BigDecimal sumPrezzoCopertinatura(Long numOrdine);
	
	/**
	 * 
	 * @param numOrdine
	 * @return Integer 
	 */
	public BigDecimal sumPrezzoTotale(Long numOrdine);
	
	/**
	 * 
	 * @param numeroOrdine
	 * @param sku
	 * @return
	 */
	public boolean deleteLibroDaOrdine(Long numeroOrdine,Long seqOrdine, String sku);
	
	/**
	 * 
	 * @param ordine
	 * @return
	 */
	public boolean confermaOrdine(OrdineLibriVo ordine);
	/**
	 * 
	 * @param ordine
	 * @param track
	 * @return
	 */
	public List<OrdineLibriDettVo> getOrdiniFornitore(String ordine, ETrack track, Integer codEdicola);
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public List<OrdineLibriDettVo> getOrdiniFornitore(Long[] ids);
	
	/**
	 * 
	 * @param cliente
	 * @return
	 */
	public List<OrdineLibriDettVo> getOrdiniFornitore(Long cliente);
	
	
	/**
	 * 
	 * @param cliente
	 * @return
	 */
	public List<OrdineLibriVo> getListOrdiniCliente(Integer codFiegDl,Integer codEdicola, Long codCliente);
	
	/**
	 * 
	 * @param numOrdineTxt
	 * @return
	 */
	public OrdineLibriVo getOrdine(String numOrdineTxt);
	
	/**
	 * 
	 * @param numOrdineTxt
	 * @param codCliente
	 * @return
	 */
	public OrdineLibriVo getOrdine(String numOrdineTxt,Long codCliente);
	
	/**
	 * 
	 * @param numeroOrdine
	 * @param sku
	 * @return
	 */
	public OrdineLibriDettVo getDettaglioLibro(Long numeroOrdine,String sku);
	
	/**
	 * 
	 * @param numeroOrdine
	 * @param sku
	 * @param keynum
	 * @return
	 */
	public OrdineLibriDettVo getDettaglioLibro(Long numeroOrdine,String sku, Long keynum);
	
	/**
	 * 
	 * @param numeroOrdine
	 * @param sku
	 * @return
	 */
	public OrdineLibriDettVo getDettaglioLibroNotKeyNum(Long numeroOrdine,String sku);
	
	/**
	 * 
	 * @param codDl
	 * @param numeroCollo
	 * @return
	 */
	public List<OrdineLibriDettVo> getDettaglioOrdiniByNumCollo(Integer codDl, Long numeroCollo);
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param codCliente
	 * @return
	 */
	public boolean existOrdiniInattivi(Integer codFiegDl, Integer codEdicola,Long codCliente);
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param codCliente
	 * @return
	 */
	public boolean existOrdini(Integer codFiegDl, Integer codEdicola,Long codCliente);
	
	/**
	 * 
	 * @param codDl
	 * @param numeroCollo
	 * @return
	 */
	public List<OrdineLibriDettVo> getDettaglioOrdiniInEdicolaByNumCollo(Integer codDl, Long numeroCollo);

	/**
	 * 
	 * @param codDl
	 * @param numeroCollo
	 * @return
	 */
	public List<Long> getDistinctClientiByNumCollo(Integer codDl, Long numeroCollo);
	
	/**
	 * 
	 * @param ordini
	 * @param stato
	 * @param dataArrivo
	 * @return
	 */
	public int updateStatoOrdini(List<OrdineLibriDettVo> ordini, ETrack stato);
	
	/**
	 * 
	 * @param codDl
	 * @param numOrdineTxt
	 * @return
	 */
	public boolean existTrackingOrdine(Integer codDl, String numOrdineTxt);
	
	/**
	 * 
	 * @param codDl
	 * @param codDpeWebEdicola
	 * @param numOrdineTxt
	 * @return
	 */
	public boolean existTrackingOrdineEdicola(Integer codDl,Integer codDpeWebEdicola, String numOrdineTxt);
	
	/**
	 * 
	 * @param codDl
	 * @param numOrdineTxt
	 * @return
	 */
	public boolean existTrackingOrdineParzialmenteEvaso(Integer codDl, String numOrdineTxt);

	/**
	 * 
	 * @param codDl
	 * @param numOrdineTxt
	 * @return
	 */
	public List<Date> getDateDiConsegnaCliente(Integer codDl, String numOrdineTxt);
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param codCliente
	 * @return
	 */
	public boolean existOrdiniDaConsegnareAlCliente(Integer codFiegDl, Integer codEdicola,Long codCliente);
}
