package it.dpe.igeriv.bo.vendite;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.IdVenditaProdottNonEditorialeDto;
import it.dpe.igeriv.dto.IntervalloVenditaDto;
import it.dpe.igeriv.dto.PositionSizeDto;
import it.dpe.igeriv.dto.ProdottoClientVenditeDto;
import it.dpe.igeriv.dto.PubblicazioneLocalVenditeDto;
import it.dpe.igeriv.dto.VenditaDettaglioDto;
import it.dpe.igeriv.dto.VenditaRiepilogoDto;
import it.dpe.igeriv.dto.VendutoGiornalieroDto;
import it.dpe.igeriv.vo.BarraSceltaRapidaDestraVo;
import it.dpe.igeriv.vo.BarraSceltaRapidaProdottiVariVo;
import it.dpe.igeriv.vo.BarraSceltaRapidaSinistraVo;
import it.dpe.igeriv.vo.MessaggioRegistratoreCassaVo;
import it.dpe.igeriv.vo.RegistratoreCassaVo;
import it.dpe.igeriv.vo.RichiestaAggiornamentoBarcodeVo;
import it.dpe.igeriv.vo.VenditaVo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;



interface VenditeRepository extends BaseRepository {
	
	/**
	 * @param <T>
	 * @return
	 */
	public List<VenditaDettaglioDto> getContiVendite(List<Long> idConti, String codUtente);
	
	/**
	 * @param codFiegDlMaster
	 * @param codEdicolaMaster
	 * @param codUtente
	 * @param dateInizio
	 * @param dateFine
	 * @param limitResults
	 * @return List<IdVenditaProdottNonEditorialeDto>
	 */
	public List<IdVenditaProdottNonEditorialeDto> getLastContiVendita(Integer codFiegDlMaster, Integer codEdicolaMaster, String codUtente, Date dateInizio, Date dateFine, boolean limitResults);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataDa
	 * @param dataA
	 * @param periodicita
	 * @return
	 */
	public List<VenditaRiepilogoDto> getVenditeRiepilogo(Integer codFiegDl, Integer codEdicola, String codUtente, Timestamp dataDa, Timestamp dataA, Integer codRaggruppamento);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 */
	public void deleteBarraMenuSceltaRapida(Integer[] codFiegDl, Integer[] codEdicola);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @return
	 */
	public VendutoGiornalieroDto getVendutoGionaliero(Integer codFiegDl, Integer codEdicola);

	/**
	 * @param codContoVendite
	 * @return
	 */
	public VenditaVo getContoVendite(Long codContoVendite);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @return
	 */
	public void updateBarraSceltaRapidaVo(Integer codFiegDl, Integer codEdicola, PositionSizeDto psDto);
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataDa
	 * @param dataA
	 * @param tipoProdotto 
	 * @return
	 */
	public List<VenditaDettaglioDto> getContiVenditeDettaglio(Integer codFiegDl, Integer codEdicola, String codUtente, Timestamp dataDa, Timestamp dataA, Integer tipoProdotto);
	
	/**
	 * @param codEdicola
	 * @param codFiegDl
	 * @param cpu
	 * @return
	 */
	public BarraSceltaRapidaSinistraVo getBarraSceltaRapidaSinistra(Integer codEdicola, Integer codFiegDl, Integer cpu);
	
	/**
	 * @param codEdicola
	 * @param codFiegDl
	 * @param cpu
	 * @return
	 */
	public BarraSceltaRapidaDestraVo getBarraSceltaRapidaDestra(Integer codEdicola, Integer codFiegDl, Integer cpu);


	/**
	 * @param codPubblicazioni
	 * @param arrCoddl
	 * @param arrCodEdicola
	 * @param nomiImmagine
	 * @param tipiImmagine
	 */
	public void saveBarraSceltaRapidaSinistra(String[] codPubblicazioni, Integer[] arrCoddl, Integer[] arrCodEdicola, String[] nomiImmagine, String[] tipiImmagine);
	
	/**
	 * @param codPubblicazioni
	 * @param arrCoddl
	 * @param arrCodEdicola
	 * @param nomiImmagine
	 * @param tipiImmagine
	 */
	public void saveBarraSceltaRapidaDestra(String[] codPubblicazioni, Integer[] arrCoddl, Integer[] arrCodEdicola, String[] nomiImmagine, String[] tipiImmagine);
	
	
	/**
	 * @param codVendita
	 */
	public void deleteListVenditaDettaglio(Long codVendita);
	
	/**
	 * @param coddl
	 * @param idtn
	 * @return
	 */
	public RichiestaAggiornamentoBarcodeVo getRichiestaAggiornamentoBarcodeVo(Integer coddl, Integer idtn, String barcode, Boolean richiestaEseguita);
	
	/**
	 * @param coddl
	 * @param dataDa
	 * @return
	 */
	public List<RichiestaAggiornamentoBarcodeVo> getRichiesteEseguiteAggiornamentoBarcodeVo(Integer coddl, Date dataDa);
	
	/**
	 * @param coddl
	 * @param codEdicola
	 * @param dataIni
	 * @param dataFine
	 * @return
	 */
	public Long getCountRichiesteAggiornamentoNelPeriodo(Integer coddl, Integer codEdicola, java.util.Date dataIni, java.util.Date dataFine);
	
	/**
	 * @param arrCodFiegDl
	 * @param arrId
	 * @return
	 */
	public List<PubblicazioneLocalVenditeDto> getLocalDataVendite(Integer[] arrCodFiegDl, Integer[] arrId);

	/**
	 * @return
	 */
	public List<RegistratoreCassaVo> getRegistratoriCassa();

	/**
	 * @param codRegCassa
	 * @return
	 */
	public RegistratoreCassaVo getRegistratoreCassa(Integer codRegCassa);

	/**
	 * @param codCliente
	 */
	public void resetCodClienteVendite(Long codCliente);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public MessaggioRegistratoreCassaVo getMessaggioRegistratoreCassa(Integer codEdicola, Integer codRegCassa);
	
	/**
	 * @param pk
	 */
	public void deleteVenditaDettaglio(String pk);
	
	/**
	 * @param pk
	 */
	public void restoreVenditaDettaglio(String pk);
	
	/**
	 * @param codCliente
	 * @param numFattura
	 */
	public void updateVenditePerStornoFattura(Long codCliente, Integer numFattura);
	
	/**
	 * @param arrId
	 * @param codCliente 
	 * @return
	 */
	public Boolean getHasRitiriCancellati(Integer[] arrId, Long codCliente);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataDa
	 * @param dataA
	 * @return
	 */
	public List<IntervalloVenditaDto> getVenditeIntervalli(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA);
	
	/**
	 * @param coddl
	 * @return
	 */
	public List<ProdottoClientVenditeDto> getProdottiClientVendite(Integer[] coddl,Integer[] codEdicola);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @return
	 */
	public PositionSizeDto getBarraSceltaRapidaProdottiVariPositionSizeDto(Integer codFiegDl, Integer codEdicola);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @return
	 */
	public BarraSceltaRapidaProdottiVariVo getBarraSceltaRapidaProdottiVariVo(Integer codFiegDl, Integer codEdicola);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataUscita
	 * @return
	 */
	public int getCopieConsegnateGazzettaCard(Integer codFiegDl, Integer codEdicolaDl, Timestamp dataUscita);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dataUscita
	 * @return
	 */
	public int getDistribuitoGazzetta(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dataUscita);
}
