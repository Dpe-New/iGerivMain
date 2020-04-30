package it.dpe.igeriv.bo.vendite;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.IdVenditaProdottNonEditorialeDto;
import it.dpe.igeriv.dto.IntervalloVenditaDto;
import it.dpe.igeriv.dto.PositionSizeDto;
import it.dpe.igeriv.dto.ProdottoClientVenditeDto;
import it.dpe.igeriv.dto.PubblicazioneLocalVenditeDto;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.dto.VenditaDettaglioDto;
import it.dpe.igeriv.dto.VenditaDto;
import it.dpe.igeriv.dto.VenditaRiepilogoDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.dto.VendutoGiornalieroDto;
import it.dpe.igeriv.vo.BarraSceltaRapidaDestraVo;
import it.dpe.igeriv.vo.BarraSceltaRapidaProdottiVariVo;
import it.dpe.igeriv.vo.BarraSceltaRapidaSinistraVo;
import it.dpe.igeriv.vo.MessaggioRegistratoreCassaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaVo;
import it.dpe.igeriv.vo.RegistratoreCassaVo;
import it.dpe.igeriv.vo.RichiestaAggiornamentoBarcodeVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.VenditaVo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * Interfaccia vendite
 * 
 * @author mromano
 *
 */
public interface VenditeService extends BaseService {
	
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
	 * @param codPubblicazioniL
	 * @param arrCoddlL
	 * @param arrCodEdicolaL
	 * @param nomiImmagineL
	 * @param tipiImmagineL
	 * @param codPubblicazioniR
	 * @param arrCoddlR
	 * @param arrCodEdicolaR
	 * @param nomiImmagineR
	 * @param tipiImmagineR
	 */
	public void saveBarraSceltaRapida(Integer[] arrCoddl, Integer[] arrCodEdicola, String[] codPubblicazioniL, Integer[] arrCoddlL, Integer[] arrCodEdicolaL, String[] nomiImmagineL, String[] tipiImmagineL, String[] codPubblicazioniR, Integer[] arrCoddlR, Integer[] arrCodEdicolaR, String[] nomiImmagineR, String[] tipiImmagineR);
	
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
	 * @param idtn
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
	 * @param vendita
	 * @param bpne
	 */
	public void saveContoVendite(VenditaVo vendita, ProdottiNonEditorialiBollaVo bpne);
	
	/**
	 * @param cop
	 * @param copertinaPrecedente
	 * @param richiesta
	 * @param giorniDataBolla
	 */
	public void saveAssociazioneBarcodePubblicazione(StoricoCopertineVo cop, StoricoCopertineVo copertinaPrecedente, RichiestaAggiornamentoBarcodeVo richiesta, int giorniDataBolla);
	
	/**
	 * @param contoVendite
	 */
	public void deleteVenditaVo(VenditaVo contoVendite);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataDa
	 * @param dataA
	 * @return
	 */
	public List<IntervalloVenditaDto> getVenditeIntervalli(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA);
	
	/**
	 * Crea un conto vendite con i dati di evasione del/dei cliente/i dell'edicola in modo da aggiornare la giacenza e le vendite
	 * 
	 * @param String codUtente
	 * @param Map<Integer, List<RichiestaClienteDto>> mapEvasione
	 * @param Timestamp dataCompEC 
	 */
	public Long saveContoVendite(String codUtente, Map<Long, List<RichiestaClienteDto>> mapEvasione, Timestamp dataCompEC);
	
	/**
	 * Chiude il conto vendita di un'edicola
	 * 
	 * @param params
	 * @param idConto
	 * @param codCliente
	 * @param importoTotale
	 * @param totaleScontrino
	 * @param contoScontrinato
	 * @param pagato
	 * @param idScontrino
	 * @param dataScontrino
	 * @param numeroFattura
	 * @param codEdicolaMaster
	 * @param codFiegDlMaster
	 * @param codUtente
	 * @param codEdicolaDl
	 * @param arrId
	 * @param isMultiDl
	 * @param arrCodFiegDl
	 * @param id
	 */
	public VenditaVo chiudiConto(List<VenditeParamDto> params, String idConto, String codCliente, String importoTotale, String totaleScontrino, String contoScontrinato, String pagato, String idScontrino, String dataScontrino, Integer numeroFattura, Integer codEdicolaMaster, Integer codFiegDlMaster, String codUtente, Integer codEdicolaDl, Integer[] arrId, boolean isMultiDl, Integer[] arrCodFiegDl, Integer id);
	
	/**
	 * @param codFiegDlMaster
	 * @param codEdicolaMaster
	 * @param isMultiDl
	 * @param codUtente
	 * @return
	 */
	public List<VenditaDto> getStoricoConti(Integer codFiegDlMaster, Integer codEdicolaMaster, boolean isMultiDl, String codUtente);
	
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
