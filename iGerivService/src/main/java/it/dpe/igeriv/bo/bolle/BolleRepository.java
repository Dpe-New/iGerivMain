package it.dpe.igeriv.bo.bolle;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.dto.BollaResaDettaglioDto;
import it.dpe.igeriv.dto.BollaResaFuoriVoceDettaglioDto;
import it.dpe.igeriv.dto.BollaResaRichiamoPersonalizzatoDettaglioDto;
import it.dpe.igeriv.dto.BollaRiassuntoDto;
import it.dpe.igeriv.dto.BollaVoDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.dto.LavorazioneResaImmagineDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.QuadraturaResaDto;
import it.dpe.igeriv.dto.ResaRiscontrataDto;
import it.dpe.igeriv.vo.BollaDettaglioVo;
import it.dpe.igeriv.vo.BollaResaDettaglioVo;
import it.dpe.igeriv.vo.BollaResaFuoriVoceVo;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import it.dpe.igeriv.vo.BollaResaRichiamoPersonalizzatoVo;
import it.dpe.igeriv.vo.BollaResaVo;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.BollaStatisticaStoricoVo;
import it.dpe.igeriv.vo.BollaVo;
import it.dpe.igeriv.vo.DecodificaRichiamiResaVo;
import it.dpe.igeriv.vo.LavorazioneResaVo;
import it.dpe.igeriv.vo.ResaRiscontrataVo;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

interface BolleRepository extends BaseRepository {
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param dataBolla
	 * @param tipoBolla
	 * @param showSoloRigheSpuntare 
	 * @param isMultiDl 
	 * @return
	 */
	public List<BollaDettaglioDto> getDettaglioBolla(Integer[] codFiegDl,
			Integer[] codEdicolaDl, Timestamp dataBolla, String tipoBolla, boolean showSoloRigheSpuntare, boolean isMultiDl);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param dataBolla
	 * @param tipoBolla
	 * @param showSoloRigheSpuntare 
	 * @param isMultiDl 
	 * @return
	 */
	public List<FondoBollaDettaglioDto> getDettagliFondoBolla(Integer[] codFiegDl,
			Integer[] codEdicolaDl, Timestamp dataBolla, String tipoBolla, boolean showSoloRigheSpuntare, boolean showSoloRifornimenti, boolean isMultiDl);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param dataBolla
	 * @param tipoBolla
	 * @param showSoloRigheSpuntare 
	 * @param isMultiDl 
	 * @return
	 */
	public List<FondoBollaDettaglioDto> getDettagliFondoBollaPubblicazioni(Integer[] codFiegDl,
			Integer[] codEdicolaDl, Timestamp dataBolla, String tipoBolla, boolean showSoloRigheSpuntare, boolean showSoloRifornimenti, boolean isMultiDl);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @return
	 */
	public List<BollaRiassuntoVo> getBolleRiassunto(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dataBolla);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @return
	 */
	public List<BollaRiassuntoVo> getBolleRiassunto(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dataBolla, String tipoBolla, Integer statoBolla, Boolean excludeSospese);
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param dataBolla 
	 * @return
	 */
	public Set<BollaResaRiassuntoVo> getBolleResaRiassunto(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dataBolla);
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param dataBolla 
	 * @return
	 */
	public List<BollaResaRiassuntoVo> getBolleResaRiassunto(Integer codFiegDl, Integer codEdicolaDl, Timestamp dataBolla, String tipoBolla, Integer statoBolla, Boolean excludeSospese);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @return List<ResaRiscontrataDto>
	 */
	public List<ResaRiscontrataDto> getBolleResaRiassuntoConResaRiscontrata(Integer codFiegDl, Integer codEdicolaDl);
	
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @return
	 */
	public List<LavorazioneResaVo> getBolleResaRiassuntoLavorazioneResa(Integer codFiegDl, Integer codEdicolaDl);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param dataBolla
	 * @param tipoBolla
	 * @return
	 */
	public List<BollaVoDto> getBollaVoSonoInoltreUscite(Integer codFiegDl, Integer codEdicolaDl, Timestamp dataBolla, String tipoBolla);
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipo
	 * @param soloResoDaInserire 
	 * @return
	 */
	public List<BollaResaDettaglioDto> getDettaglioBollaResa(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipo, Boolean soloResoDaInserire,Boolean soloResoConGiacenza);
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipo
	 * @param soloResoDaInserire
	 * @param soloResoConGiacenza
	 * @param cesta
	 * @return
	 */
	public List<BollaResaDettaglioDto> getDettaglioBollaResaCDLCeste(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipo, Boolean soloResoDaInserire,Boolean soloResoConGiacenza, String cesta);
	
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param tipo
	 * @return
	 */
	public List<Integer> getCpusResaDimeticataNotInBollaResa(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipo);
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipo
	 * @param soloResoDaInserire 
	 * @return
	 */
	public List<BollaResaFuoriVoceDettaglioDto> getDettaglioFuoriVoce(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipo, Boolean soloResoDaInserire);
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipo
	 * @param soloResoDaInserire 
	 * @return
	 */
	public List<BollaResaRichiamoPersonalizzatoDettaglioDto> getDettaglioRichiamoPersonalizzato(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipo, Boolean soloResoDaInserire);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipo
	 * @return
	 */
	public BollaRiassuntoVo getBollaRiassunto(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipo);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipo
	 * @return
	 */
	public List<BollaResaRiassuntoVo> getBollaResaRiassunto(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipo);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipo
	 * @param cpu
	 * @param gruppoSconto
	 * @param showNumeriOmogenei
	 * @param dataStorico
	 * @param tipoResaNoContoDeposito
	 * @param accettoCD
	 * @return
	 */
	public List<BollaResaFuoriVoceVo> buildNuoviDettagliFuoriVoce(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipo, Integer cpu, Integer gruppoSconto, boolean showNumeriOmogenei, Timestamp dataStorico, String tipoResaNoContoDeposito, Boolean accettoCD);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipo
	 * @return
	 */
	public Integer findUltimaPosizioneRigaBolla(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipo);
	
	/**
	 * @param differenze
	 * @param spunte
	 * @param pk
	 * @param pkFondoBolla 
	 * @param spunteFondoBolla 
	 * @param differenzeFondoBolla 
	 * @param codEdicola 
	 */
	public void saveSpuntaBollaRivendita(String differenze, String spunte, String pk, String differenzeFondoBolla, String spunteFondoBolla, String pkFondoBolla, Integer codEdicola);
	
	/**
	 * @param codEdicola
	 * @param fields
	 * @param spunte
	 * @param spunteFB 
	 * @param fieldsFB
	 */
	public void saveBollaRivendita(Integer codEdicola, Map<String, String> fields, Map<String, String> spunte, Map<String, String> fieldsFB, Map<String, String> spunteFB);

	/**
	 * @param reso
	 * @param resoFuoriVoce
	 * @param resoRichiamo
	 */
	public void saveBollaResa(Map<String, String> reso, Map<String, String> resoFuoriVoce, Map<String, String> resoRichiamo);

	/**
	 * @param stato
	 * @param pk
	 */
	public void saveBolleRiassunto(String stato, String pk);

	/**
	 * @param stato
	 * @param pk
	 */
	public void saveBolleResaRiassunto(String stato, String pk);
	
	/**
	 * @param zipFile
	 * @return
	 */
	public LavorazioneResaVo getLavorazioneResaVo(String zipFile);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param dtBolla
	 * @param tipoBolla
	 * @return
	 */
	public LavorazioneResaVo getLavorazioneResaVo(Integer codFiegDl, Integer codEdicolaDl, Timestamp dtBolla, String tipoBolla);
	
	/**
	 * @param nomeFile
	 * @return
	 */
	public List<LavorazioneResaImmagineDto> getListLavorazioneResaImmagineVo(String nomeFile);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipoBolla
	 * @return
	 */
	public List<QuadraturaResaDto> getQuadraturaResa(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla);
	
	/**
	 * @param codFiegDl
	 * @param dtBolla
	 * @param tipoBolla
	 * @param cpu
	 * @return
	 */
	public Boolean cpuExistsInBolla(Integer codFiegDl, Timestamp dtBolla, String tipoBolla, Integer cpu);
	
	/**
	 * @param codFiegDl
	 * @param dataBolla
	 * @param tipoBolla
	 * @param numeroRiga
	 * @return
	 */
	public BollaResaVo getBollaResaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer numeroRiga);
	
	/**
	 * @param codFiegDl
	 * @param tipoRichiamoResa
	 * @return
	 */
	public DecodificaRichiamiResaVo getRichiamoResa(Integer codFiegDl, Integer tipoRichiamoResa);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataBolla
	 * @param tipoBolla
	 * @param idtn
	 * @return
	 */
	public BollaResaDettaglioVo getBollaResaDettaglioVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataBolla
	 * @param tipoBolla
	 * @param idtn
	 * @return
	 */
	public ResaRiscontrataVo getResaRiscontrataVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn);

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param idtn
	 * @return
	 */
	public BollaStatisticaStoricoVo getBollaStatisticaStoricoVo(Integer codFiegDl, Integer codEdicola, Integer idtn);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dataBolla
	 * @param tipoBolla
	 * @return
	 */
	public Integer getLastRigaBolla(Integer codFiegDl, Timestamp dataBolla, String tipoBolla);
	
	/**
	 * @param pk
	 * @param barcode
	 * @param quantita
	 */
	public BollaDettaglioVo getBollaDettaglioVo(String pk, Integer idtn) throws ParseException;
	
	/**
	 * @param codFiegDl
	 * @param dataBolla
	 * @param tipoBolla
	 * @return
	 */
	public BollaVo getBollaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer posizioneRiga, Integer idtn);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataBolla
	 * @param tipoBolla
	 * @param posizioneRiga
	 * @return BollaDettaglioVo
	 */
	public BollaDettaglioVo getDettaglioBolla(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer posizioneRiga);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataBolla
	 * @param tipoBolla
	 */
	public void deleteFondoBollaEdicolaInforiv(Integer codFiegDl, Integer codEdicola, Timestamp dataBolla, String tipoBolla);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataBolla
	 * @param tipoBolla
	 * @return
	 */
	public Integer getLastPosizioneRigaBolla(Integer codFiegDl, Integer codEdicola, Timestamp dataBolla, String tipoBolla);

	/**
	 * @param codFiegDl
	 * @param dataBolla
	 * @return
	 */
	public List<String> getTipiBollaConsegna(Integer codFiegDl, Timestamp dataBolla);
	
	/**
	 * @param codFiegDl
	 * @param dataBolla
	 * @return
	 */
	public List<String> getTipiBollaResa(Integer codFiegDl, Timestamp dataBolla);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipoBolla
	 * @param titolo
	 * @return
	 */
	public List<PubblicazioneDto> getPubblicazioniInBolla(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipoBolla, String titolo);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipoBolla
	 * @param idtn
	 * @return
	 */
	public Integer getProgressivoIdtnBollaConsegna(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla, Integer idtn);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipoBolla
	 * @param idtn
	 * @return
	 */
	public Integer getProgressivoIdtnBollaResa(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla, Integer idtn);
	
	/**
	 * @param codFiegDl
	 * @param cpu
	 * @param tipoBolla
	 * @return
	 */
	public Boolean isPubblicazioneInBolleConsegnaTipo(Integer codFiegDl, Integer cpu, String tipoBolla);

	/**
	 * @param dataBolla
	 * @return
	 */ 
	public List<BollaRiassuntoDto> getBolleRiassuntoNonInviate(Timestamp[] dateBolla, String tipo, Integer codFiegDl);

	/**
	 * @param dataBolla
	 * @param tipo
	 * @param codFiegDl
	 * @return
	 */
	public List<BollaRiassuntoDto> getBolleResaRiassuntoNonInviate(Timestamp[] dateBolla, String tipo, Integer codFiegDl);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtBolla
	 * @param tipoBolla
	 * @param progressivo
	 * @return
	 */
	public BollaResaRichiamoPersonalizzatoVo getBollaResaRichiamoPersonalizzato(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla, Integer progressivo);	
	
	/**
	 * 
	 * @param codFiegDl
	 * @param maxResult
	 * @return
	 */
	public List<BollaRiassuntoDto> getListaBolle(Integer codFiegDl,Integer maxResult);
	
	
	/**
	 * 
	 * @param codFiegDl
	 * @param tipoBolla
	 * @param dataBolla
	 * @return
	 */
	public List<BollaVo> getDettaglioBolla(Integer codFiegDl,String tipoBolla,Timestamp dataBolla);
	
	/**
	 * 
	 * @param pk
	 * @param selectedDataTipoBolla 
	 * @param codFiegDl 
	 */
	public void updateNonUscite(String pk, String selectedDataTipoBolla, Integer codFiegDl);
	
}
