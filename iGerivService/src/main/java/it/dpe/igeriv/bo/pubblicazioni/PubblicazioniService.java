package it.dpe.igeriv.bo.pubblicazioni;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.AnagraficaEditoreDto;
import it.dpe.igeriv.dto.ContoDepositoDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.PubblicazionePiuVendutaDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.AnagraficaEditoreDlVo;
import it.dpe.igeriv.vo.AnagraficaPubblicazioniVo;
import it.dpe.igeriv.vo.ArgomentoVo;
import it.dpe.igeriv.vo.BollaVo;
import it.dpe.igeriv.vo.ContoDepositoVo;
import it.dpe.igeriv.vo.ImmaginePubblicazioneVo;
import it.dpe.igeriv.vo.PeriodicitaTrascodificaInforeteVo;
import it.dpe.igeriv.vo.PeriodicitaVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Interfaccia pubblicazioni
 * 
 * @author mromano
 *
 */
public interface PubblicazioniService extends BaseService {
	
	/**
	 * @param codDl
	 * @param idtn
	 * @return
	 */
	public PubblicazioneDto getCopertinaByIdtn(Integer codDl, Integer idtn);
	
	/**
	 * @param codDl
	 * @param idtn
	 * @return
	 */
	public PubblicazioneDto getCopertinaByIdtn(Integer[] codDl, Integer[] codEdicola, Integer idtn);
	
	/**
	 * @param codDl
	 * @param codEdicola
	 * @param idtnInt
	 * @param showPubblicazioniTuttiDl
	 * @return
	 */
	public PubblicazioneDto getCopertinaByIdtn(Integer[] codDl, Integer[] codEdicola, Integer idtnInt, boolean showPubblicazioniTuttiDl, Boolean dlInforiv);
	
	/**
	 * @param codDl
	 * @param codEdicola
	 * @param barcode
	 * @param multiDl
	 * @return
	 */
	public PubblicazioneDto getCopertinaByCpuNum(final Integer[] codDl, final Integer[] codEdicola, final Integer cpu, final Integer cddt, final String num, final Boolean multiDl);
	
	/**
	 * @param <T>
	 * @param codDl
	 * @param codicePubblicazione
	 * @param codiceInizioQuotidiano
	 * @param codiceFineQuotidiano
	 * @param numCopertinePrecedentiPerRifornimenti
	 * @param currCodDl 
	 * @param isMultiDl 
	 * @return
	 */
	public List<RichiestaRifornimentoDto> getCopertineByCodPubblicazione(Integer[] codDl, Integer[] codEdicola, Integer codicePubblicazione, Integer codiceInizioQuotidiano, Integer codiceFineQuotidiano, Integer numCopertinePrecedentiPerRifornimenti, Timestamp dataStorico, Timestamp dataUscitaLimite, boolean isMultiDl, Integer currCodDl, Integer agenziaFatturazione, Map<String,Object> params);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param idtn
	 * @return
	 */
	public Long getGiacenza(Integer codFiegDl, Integer codEdicola, Integer idtn, Timestamp dataStorico);
	
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param idtn
	 * @return
	 */
	public Long getFornito(final Integer codFiegDl, final Integer codEdicola, final Integer idtn);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param idtn
	 * @return
	 */
	public ContoDepositoVo getPubblicazioneInContoDeposito(Integer codFiegDl, Integer codEdicola, Integer idtn);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param gruppoSconto
	 * @param titolo
	 * @param codBarre
	 * @return
	 */
	public List<ContoDepositoDto> getPubblicazioniContoDeposito(Integer[] codFiegDl, Integer[] codEdicola, Integer gruppoSconto, String titolo, String codBarre);
	
	/**
	 * @param <T>
	 * @param ultimaPubblicazione
	 * @param contoDeposito 
	 * @param titolo
	 * @param sottotitolo
	 * @param argomento
	 * @param periodicita
	 * @param prezzo
	 * @param codPubblicazione
	 * @param codBarre
	 * @param gruppoSconto 
	 * @param showPubblicazioniTuttiDl 
	 * @param numMaxResults
	 * @return
	 */
	public List<PubblicazioneDto> getCopertine(boolean ultimaPubblicazione, boolean statistiche, boolean contoDeposito, Integer codEdicolaMaster, Integer[] codDl, Integer[] codEdicola, String titolo, String sottotitolo, String argomento, String periodicita, BigDecimal prezzo, Integer codPubblicazione, String codBarre, boolean showOnlyUltimoDistribuito, Timestamp dataStorico, Integer gruppoSconto, boolean showPubblicazioniTuttiDl, Integer currDlMultiDl, Integer anagEditori, Integer agenziaFatturazione, Boolean isSecondaCintura, Timestamp dataPartSecCintura);
	
	
	/**
	 * 
	 * @param codDl
	 * @param titolo
	 * @param sottotitolo
	 * @param argomento
	 * @param periodicita
	 * @param prezzo
	 * @param codPubblicazione
	 * @param codBarre
	 * @return List<PubblicazioneDto>
	 */
	public List<PubblicazioneDto> getCopertineByDL(boolean ultimaPubblicazione,Integer codDl, String titolo, String sottotitolo, String argomento, String periodicita, BigDecimal prezzo, Integer codPubblicazione, String codBarre);
	
	
	/**
	 * @param <T>
	 * @param codDl
	 * @param codEdicola
	 * @param titolo
	 * @param codBarre
	 * @param soloUltimeCopertine
	 * @param findGiacenza 
	 * @param dataStorico 
	 * @param dlInforiv 
	 * @return
	 */
	public List<PubblicazioneDto> getCopertineByTitoloBarcodeCpu(Integer[] codDl, Integer[] codEdicola, String titolo, String codBarre, boolean soloUltimeCopertine, boolean soloPubblicazioniBarcodeNullo, Integer cpu, Integer gruppoSconto, Boolean findCopieInContoDeposito, Boolean findGiacenza, Timestamp dataStorico, Boolean dlInforiv);
	
	/**
	 * 
	 * @param codDl
	 * @param titolo
	 * @param cpu
	 * @return
	 */
	public List<PubblicazioneDto> getPubblicazioniByTitoloCpu(Integer codDl, String titolo, Integer cpu);
	
	/**
	 * @param codFiegDl
	 * @param codPubblicazioneInt
	 * @return
	 */
	public AnagraficaPubblicazioniVo getAnagraficaPubblicazioneByPk(Integer codFiegDl, Integer codPubblicazioneInt);
	
	/**
	 * @param titolo
	 * @return
	 */
	public List<AnagraficaPubblicazioniVo> getQuotidianoByTitolo(String titolo);
	
	/**
	 * @param titolo
	 * @return
	 */
	public List<AnagraficaPubblicazioniVo> getPeriodicoByTitolo(String titolo);
	
	/**
	 * @param codFiegDl
	 * @param codInizioQuotidiano
	 * @param codFineQuotidiano
	 */
	public List<AnagraficaPubblicazioniVo> getListAnagraficaPubblicazioneByCodQuotidiano(Integer codFiegDl, Integer codInizioQuotidiano, Integer codFineQuotidiano);
	
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param tipoPubblicazione
	 * @param periodo 
	 * @return
	 */
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniPiuVendute(Integer codFiegDl, Integer codEdicola, Integer tipoPubblicazione, Integer periodo);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param cpu
	 * @return
	 */
	public PubblicazionePiuVendutaDto getPubblicazionePiuVendutaByCpu(Integer[] codFiegDl, Integer[] codEdicola, Integer cpu);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @return
	 */
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniBarraSceltaRapidaSinistra(Integer[] codFiegDl, Integer[] codEdicola);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param barcodeUltimaCopertina
	 * @return
	 */
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniBarraSceltaRapidaSinistra(Integer[] codFiegDl, Integer[] codEdicola, boolean barcodeUltimaCopertina);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param barcodeUltimaCopertina
	 * @return
	 */
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniBarraSceltaRapidaDestra(Integer[] codFiegDl, Integer[] codEdicola, boolean barcodeUltimaCopertina);
	
	/**
	 * @param codDl 
	 * @param <T>
	 * @return
	 */
	public List<ArgomentoVo> getArgomenti(Integer codDl);
	
	/**
	 * @param codDl 
	 * @param <T>
	 * @return
	 */
	public Map<Integer, List<ArgomentoVo>> getMapArgomentiDl();
	
	/**
	 * @param <T>
	 * @return
	 */
	public List<PeriodicitaVo> getPeriodicita();
	
	/**
	 * @param codFiegDl
	 * @param barcode
	 * @return
	 */
	public Integer getCpuByBarcode(Integer codFiegDl, String barcode);
	
	/**
	 * @param barcode
	 * @return
	 */
	public ImmaginePubblicazioneVo getImmaginePubblicazione(String barcode);

	/**
	 * @param tipoOperazioneInforete
	 * @param codPeriodicita
	 * @return PeriodicitaTrascodificaInforeteVo
	 */
	public PeriodicitaTrascodificaInforeteVo getPeriodicitaTrascodificaInforete(Integer codPeriodicitaInforete);
	
	/**
	 * @param codPeriodicita
	 * @param codPeriodicitaEnciclopedia 
	 * @return
	 */
	public PeriodicitaVo getPeriodicita(Integer tipoOperazione, Integer codPeriodicitaEnciclopedia);
	
	/**
	 * @param arrCodEdicola 
	 * @param arrCodFiegDl 
	 * @param titolo
	 * @param dataDa
	 * @param dataA
	 * @param soloPeriodici 
	 * @param baseCalcolo
	 * @return
	 */
	public List<PubblicazioneDto> getPubblicazioniInvendute(Integer[] arrCodFiegDl, Integer[] arrCodEdicola, String titolo, Timestamp dataDa, Timestamp dataA, Integer gruppoSconto, Boolean escludiQuotidiani, Boolean escludiCD, Integer baseCalcolo, Integer page, Integer maxRows);
	
	/**
	 * @param arrCodFiegDl
	 * @param arrCodEdicola
	 * @param titolo
	 * @param dataDa
	 * @param dataA
	 * @param gruppoSconto
	 * @param escludiQuotidiani
	 * @param baseCalcolo
	 * @return
	 */
	public Integer getCountPubblicazioniInvendute(Integer[] arrCodFiegDl, Integer[] arrCodEdicola, String titolo, Timestamp dataDa, Timestamp dataA, Integer gruppoSconto, Boolean escludiQuotidiani, Boolean escludiCD, Integer baseCalcolo);
	
	/**
	 * @param idtni
	 * @return
	 */
	public AbbinamentoIdtnInforivVo getAbbinamentoIdtnInforiv(Integer codFiegDl, String idtni);
	
	/**
	 * @param codFiegDl
	 * @param idtn
	 * @return
	 */
	public StoricoCopertineVo getStoricoCopertinaByPk(Integer codFiegDl, Integer idtn);
	
	/**
	 * @param codEdicola
	 * @param codFiegDl
	 * @param idtn
	 * @return
	 */
	public PubblicazioneDto getPubblicazioneConPrezzoEdicola(Integer[] codEdicola, Integer codFiegDl, Integer idtn, Integer gruppoSconto);
	
	/**
	 * @param coddl
	 * @param barcode
	 * @return
	 */
	public StoricoCopertineVo getStoricoCopertinaByBarcode(Integer coddl, String barcode);

	/**
	 * @param coddl
	 * @param barcode
	 * @return
	 */
	public StoricoCopertineVo getStoricoCopertinaByBarcode(Integer[] coddl, String barcode);

	/**
	 * @param codDl
	 * @param cpu
	 * @return
	 */
	public List<StoricoCopertineVo> getStoricoCopertineByCpu(Integer codDl, Integer cpu);
	
	/**
	 * @param codDl
	 * @param cpu
	 * @return
	 */
	public StoricoCopertineVo getLastStoricoCopertina(Integer codDl, Integer cpu);
	
	/**
	 * @param codDl
	 * @param cpu
	 * @return
	 */
	public PubblicazioneDto getLastPubblicazioneDto(Integer codDl, Integer cpu);

	/**
	 * @param codDl
	 * @param cpuInizio
	 * @param cpuFine
	 * @return
	 */
	public List<AnagraficaPubblicazioniVo> getQuotidiani(Integer codDl, Integer cpuInizio, Integer cpuFine);
	
	/**
	 * 
	 */
	public void updateBarcodeTodisConDevietti(Integer days);
	
	/**
	 * @param coddl
	 * @param cpuDa
	 * @param cpuA
	 * @param dataUscita
	 * @return
	 */
	public PubblicazioneDto getQuotidianoByDataUscita(Integer coddl, Integer cpuDa, Integer cpuA, Timestamp dataUscita);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @return
	 */
	public Set<Integer> getCpuBarreSceltaRapidaPubblicazioni(Integer[] codFiegDl, Integer[] codEdicola);

	/**
	 * @param dataUscita
	 * @param codFiegDl
	 * @return
	 */
	public List<PubblicazioneDto> getCopertineUsciteData(Timestamp dataUscita, Integer codFiegDl);
	
	
	/**
	 * 
	 * @param coddl
	 * @return
	 */
	public boolean isCodDlPubblicazioneCorretto(Integer coddl, List<PubblicazioneDto> listPubb);
	
	/**
	 * 
	 * @param coddl
	 * @return List<AnagraficaEditoreDlVo>
	 */
	public List<AnagraficaEditoreDto> getListAnagraficaEditori(Integer[] coddl);
	
}
