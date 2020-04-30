package it.dpe.igeriv.bo.rifornimenti;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.EsportazioneDatiDlResultDto;
import it.dpe.igeriv.dto.PrenotazioneDto;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.dto.RichiestaFissaClienteDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.vo.PrenotazioneVo;
import it.dpe.igeriv.vo.RichiestaClienteVo;
import it.dpe.igeriv.vo.RichiestaFissaClienteEdicolaVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.RisposteClientiCodificateVo;
import it.dpe.igeriv.vo.pk.RichiestaClientePk;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

interface RifornimentiRepository extends BaseRepository {
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param codCliente
	 * @param titolo 
	 * @param stato 
	 * @param dataA 
	 * @param dataDa 
	 * @param Boolean filterDataSospensionePrenotazioni
	 * @return  
	 */
	public List<RichiestaClienteDto> getRichiesteClienteByIdClienteViewOnly(Integer[] codFiegDl, Integer[] codEdicolaDl, List<Long> codCliente, String titolo, String stato, Integer provenienza, Timestamp dataDa, Timestamp dataA, Boolean filterDataSospensionePrenotazioni);
	
	/**
	 * @param <T>
	 * @param pk
	 * @return
	 */
	public List<RichiestaClienteDto> getRichiesteClienteByPk(String pk);
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param codCliente
	 * @param titolo
	 * @param dataA 
	 * @param dataDa 
	 * @param findUltimoIdtnPrenotazioniFisse 
	 * @return
	 */
	public List<RichiestaFissaClienteDto> getRichiesteClienteFisseByIdCliente(Integer[] codFiegDl, Integer[] codEdicolaDl, Long codCliente, String titolo, String stato, Timestamp dataDa, Timestamp dataA, Boolean findUltimoIdtnPrenotazioniFisse);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param idtn
	 * @return
	 */
	public List<RichiestaRifornimentoDto> getRichiesteCliente(Integer coddl, Integer[] codFiegDl, Integer[] codEdicolaDl, Integer idtn, Long codCliente, Integer provenienza);
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicola
	 * @param pkSet
	 * @return
	 */
	public List<RichiestaClienteVo> getRichiesteClienteByPk(Integer[] codFiegDl, Integer[] codEdicola, Long codCliente, Integer provenienza, Set<String> pkSet) throws ParseException;
	
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param idtn
	 * @return
	 */
	public List<RichiestaClienteDto> getRichiesteClienteByIdtn(Integer[] codFiegDl, Integer[] codEdicolaDl, Integer idtn, Integer provenienza);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param idtn
	 * @param isDlInforiv 
	 * @return
	 */
	public List<RichiestaRifornimentoDto> getRichiesteRifornimenti(Integer coddl, Integer[] codFiegDl, Integer[] codEdicolaDl, Integer idtn, boolean isMultiDl, Timestamp dataStorico, Integer currCodDl);
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param stato 
	 * @param isDlInforiv 
	 * @return
	 */
	public List<RichiestaRifornimentoDto> getRichiesteRifornimenti(Integer codFiegDl, Integer codEdicolaDl, String titolo, String stato, Timestamp dataDa, Timestamp dataA, boolean isDlInforiv);
	
	/**
	 * 
	 * @param codDlPubb
	 * @param arrCodFiegDl
	 * @param arrCodWebEdicola
	 * @param idtn
	 * @param isMultiDl
	 * @param dataStorico
	 * @param currCodDl
	 * @return
	 */
	public List<RichiestaRifornimentoDto> getPubblicazioniPossibiliPerRichiesteRifornimenti(Integer codDlPubb,Integer[] arrCodFiegDl, Integer[] arrCodWebEdicola, Integer idtn, boolean isMultiDl, Timestamp dataStorico, Integer currCodDl);
	
	/**
	 * 
	 * @param codDlPubb
	 * @param arrCodFiegDl
	 * @param arrCodWebEdicola
	 * @param idtn
	 * @param isMultiDl
	 * @param dataStorico
	 * @param currCodDl
	 * @return
	 */
	public List<RichiestaRifornimentoDto> getPubblicazioniEsitoRichiesteRifornimenti(Integer codDlPubb,Integer[] arrCodFiegDl, Integer[] arrCodWebEdicola, Integer idtn, boolean isMultiDl, Timestamp dataStorico, Integer currCodDl);
	
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param stato 
	 * @return
	 */
	public List<RichiestaRifornimentoVo> getRichiesteRifornimentiVo(Integer[] codFiegDl, Integer[] codEdicolaDl, Set<String> pkSet) throws ParseException;
	
	/**
	 * @param codEdicolaDl
	 * @param codCliente
	 * @param codFiegDl
	 * @param codicePubblicazione
	 * @return
	 */
	public RichiestaFissaClienteEdicolaVo getRichiestaFissaClienteVo(Integer[] codEdicolaDl, Long codCliente, Integer[] codFiegDl,Integer codicePubblicazione);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param cpu
	 * @return
	 */
	public PrenotazioneVo getPrenotazione(Integer codFiegDl, Integer codEdicolaDl, Integer cpu);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param titolo
	 * @param stato
	 * @return
	 */
	public List<PrenotazioneDto> getRichiesteVariazioni(Integer[] codFiegDl, Integer[] codEdicola, String titolo, Integer stato, Timestamp dataDa, Timestamp dataA, Integer codUtente);
	
	/**
	 * @param mapEvasione
	 */
	public void saveEvasionePrenotazioniClientiEdicola(Map<Long, List<RichiestaClienteDto>> mapEvasione);
	
	/**
	 * @param pk
	 * @param qtaEvasa
	 * @param qtaDaEvadere
	 * @param ultimaRisposta
	 * @param messLibero
	 * @return
	 * @throws ParseException
	 */
	public Map<Long, List<RichiestaClienteDto>> buildMapEvasione(String pk, String qtaEvasa, String qtaDaEvadere, String ultimaRisposta, String messLibero) throws ParseException;
	
	/**
	 * @param <T>
	 * @return
	 */
	public List<RisposteClientiCodificateVo> getRisposteClientiCodificate();
	
	/**
	 * @param codDl
	 * @return
	 */
	public EsportazioneDatiDlResultDto esportaRifornimenti(Integer codDl);
	
	/**
	 * @param codDl
	 * @return
	 */
	public EsportazioneDatiDlResultDto esportaAltriDati(Integer codDl);
	
	/**
	 * @param codDl
	 * @return
	 */
	public EsportazioneDatiDlResultDto esportaTutto(Integer codDl);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param idtnOrdini
	 * @return
	 */
	public List<RichiestaClienteDto> getOrdiniClienti(Integer codFiegDl, Integer codEdicola, String idtnOrdini);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataBaolla
	 * @return
	 */
	public List<RichiestaClienteDto> getOrdiniClienti(Integer codFiegDl, Integer codEdicola, Timestamp dataBaolla);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @return
	 */
	public List<RichiestaClienteDto> getOrdiniClientiNotifiche(Integer[] codFiegDl, Integer[] codEdicola);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public Boolean hasNotificheOrdiniClienti(Integer codFiegDl, Integer codEdicola);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param idtn
	 * @param ordine
	 * @return
	 */
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn, Integer ordine);
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param idtn
	 * @return
	 */
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn);
	
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dataInserimento,Integer idtn);
		
	/**
	 * @param pks
	 */
	public void updateOrdiniClientiNotifiche(List<RichiestaClientePk> pks);
	
	/**
	 * @param codEdicola
	 * @param codCliente
	 * @param provenienza
	 * @param dataInserimento
	 * @param codDl
	 * @param idtn
	 * @return
	 */
	public RichiestaClienteVo getRichiestaClienteVo(Integer codEdicola, Long codCliente, Integer provenienza, Timestamp dataInserimento, Integer codDl, Integer idtn);

	/**
	 * @param codEdicola
	 * @param codCliente
	 * @param codDl
	 * @param codicePubblicazione
	 * @return
	 */
	public RichiestaFissaClienteEdicolaVo getRichiestaFissaClienteEdicolaVo(Integer codEdicola, Long codCliente, Integer codDl, Integer codicePubblicazione);
	
	/**
	 * @param Integer codEdicola
	 * @param Integer codDl
	 * @param Timestamp dtBolla
	 * @param String tipoBolla
	 * @return List<RichiestaFissaClienteDto>
	 */
	public List<PrenotazioneDto> getPrenotazioniFisseNonEvase(Integer codEdicola, Integer codDl, Timestamp dtBolla, String tipoBolla);

	/**
	 * @param Integer codEdicola
	 * @param Integer codDl
	 * @param Timestamp dtBolla
	 * @param String tipoBolla
	 * @return Boolean
	 */
	public Boolean hasPrenotazioniFisseNonEvase(Integer codEdicola, Integer codDl, Timestamp dtBolla, String tipoBolla);
	
	/**
	 * @param codDl
	 * @param dataOrdineIniziale
	 * @param dataOrdineFinale
	 * @param codRivenditaDl
	 * @param codPubblicazione
	 * @param idtn
	 * @return
	 */
	public List<RichiestaRifornimentoDto> getRichiesteRifornimento(Integer codDl, Timestamp dataOrdineIniziale, Timestamp dataOrdineFinale, Integer codRivenditaDl, Integer codPubblicazione, Integer idtn);
	
}
