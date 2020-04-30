package it.dpe.igeriv.bo.edicole;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.EstrattoContoDto;
import it.dpe.igeriv.dto.GiroDto;
import it.dpe.igeriv.dto.GiroTipoDto;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaAggiuntiviVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BonusInvitaUnCollegaVo;
import it.dpe.igeriv.vo.EdicolaDlVo;
import it.dpe.igeriv.vo.EmailRivenditaVo;
import it.dpe.igeriv.vo.ImmagineAnagraficaEdicolaVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.PianoProfiliEdicolaVo;

import java.util.Date;
import java.util.List;


/**
 * Interfaccia edicole
 * 
 * @author mromano
 *
 */
public interface EdicoleService extends BaseService {
	
	/**
	 * @param <T>
	 * @param codEdicolaDl
	 * @param codEdicolaDl2 
	 * @param ragioneSociale
	 * @return
	 */
	public List<EdicolaDto> getEdicole(Integer codDl, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale);
	
	/**
	 * 
	 * @param codDl
	 * @param codEdicolaWeb
	 * @param codEdicolaDl
	 * @param ragioneSociale
	 * @return
	 */
	public List<EdicolaDto> getEdicole(Integer codDl,Integer idProfiloDL, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale);
	/**
	 * @param <T>
	 * @param codEdicolaDl
	 * @param codEdicolaDl2 
	 * @param ragioneSociale
	 * @return
	 */
	public List<EdicolaDto> getEdicole(Integer codDl, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale, boolean soloUtentiAmministratori);
	
	/**
	 * @param codDl
	 * @param codEdicolaWeb
	 * @param codEdicolaDl
	 * @param ragioneSociale
	 * @return
	 */
	public List<EdicolaDto> getEdicoleInforivDl(Integer codDl, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale, boolean utenteAmministratore);
	
	/**
	 * @param codiceDl
	 * @param codiceRivendita
	 */
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita);
	
	/**
	 * @param codiceDl
	 * @param codiceRivendita
	 */
	public AnagraficaEdicolaVo getEdicolaByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita);
	
	/**
	 * @param codiceRivenditaWeb
	 * @return
	 */
	public AnagraficaEdicolaAggiuntiviVo getAnagraficaEdicolaAggiuntiviVoByCodEdicolaWeb(Integer codiceRivenditaWeb);
	
	/**
	 * @param codiceRivenditaDl
	 * @param codFiegDl
	 * @return
	 */
	public Integer getCodDpeWebEdicolaAbbinamentoEdicolaDlVo(Integer codiceRivenditaDl, Integer codFiegDl);
	
	/**
	 * @param codDl
	 * @param pk
	 * @param dateSospensione
	 * @param checkConsegneGazzetta
	 */
	public void updateDataSospensioneEdicole(final Integer codDl, final String codEdicole, final String dateSospensione, final Boolean checkConsegneGazzetta);
	
	/**
	 * @param codEdicolaDl
	 * @param password
	 */
	public void updatePwdEdicola(Integer codEdicolaDl, String password, boolean changePassword, boolean isPwdCripata);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public AnagraficaEdicolaVo getAnagraficaEdicola(Integer codEdicola);
	
	/**
	 * @param codUtente
	 * @param pk
	 * @param dataInserimento
	 * @param dataSospensione
	 * @param profilo
	 * @param edicolaTest
	 * @param edicolaPromoDtIni
	 * @param edicolaPromoDtFin
	 * @param edicolaPlusDtIni
	 * @param edicolaPlusDtFin
	 */
	public void updateDatiEdicole(Integer codUtente, String pk, String dataInserimento, String dataSospensione, String profilo, String edicolaTest, String edicolaPromoDtIni, String edicolaPromoDtFin, String edicolaPlusDtIni, String edicolaPlusDtFin);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodEdicolaWeb(Integer codEdicola);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public List<ParametriEdicolaDto> getParametriEdicola(Integer codEdicola);

	/**
	 * @param codParametro
	 * @return
	 */
	public ParametriEdicolaVo getParametroEdicola(Integer codEdicola, Integer codParametro);
	
	/**
	 * @return
	 */
	public List<AbbinamentoEdicolaDlVo> getEdicoleNonSospese();
	
	/**
	 * @return
	 */
	public List<AbbinamentoEdicolaDlVo> getEdicoleNonSospese(Integer coddl);
	
	/**
	 * @param coddl
	 * @return
	 */
	public List<AbbinamentoEdicolaDlVo> getEdicoleAutorizzateAggiornamentoBarcode(Integer coddl);
	
	/**
	 * @param codEdicolaDl
	 * @param codDl
	 * @return
	 */
	public EdicolaDlVo getEdicolaDl(Integer codEdicolaDl, Integer codDl);
	
	/**
	 * @param coddl
	 * @param codEdicola
	 * @param ragSoc
	 * @return
	 */
	public List<EdicolaDlVo> getEdicoleDl(Integer coddl, Integer codEdicola, String ragSoc);
	
	/**
	 * @param arrId
	 * @param arrCodFiegDl
	 * @return
	 */
	public Boolean getEmailValidoMultiDl(Integer[] arrId, Integer[] arrCodFiegDl);
	
	/**
	 * @param codEdicola
	 * @param codDl
	 * @param email
	 * @return
	 */
	public BonusInvitaUnCollegaVo getBonusInvitaUnCollega(Integer codEdicola, Integer codDl);
	
	/**
	 * @param coddl
	 * @param codEdicola
	 * @param dataEC
	 * @return
	 */
	public EstrattoContoDto getEstrattoContoEdicolaPdf(Integer coddl, Integer codEdicola, java.sql.Date dataEC);
	
	/**
	 * @param idEmailRivendita
	 * @return
	 */
	public EmailRivenditaVo getEmailRivenditaVo(Integer idEmailRivendita);
	
	/**
	 * @param codEdicola
	 * @param giroTipo
	 * @return
	 */
	public List<GiroDto> getGiri(Integer codEdicola, Integer giroTipo);
	
	/**
	 * @param codEdicola
	 * @param giroTipo
	 * @return
	 */
	public List<GiroDto> getZone(Integer codEdicola, Integer zonaTipo);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param codice
	 * @param desc
	 * @return
	 */
	public List<GiroTipoDto> getGiriTipo(Integer codFiegDl, Integer codEdicola, Integer codice, String desc);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param codice
	 * @param desc
	 * @return
	 */
	public List<GiroTipoDto> getZoneTipo(Integer codFiegDl, Integer codEdicola, Integer codice, String desc);
	
	/**
	 * @param codEdicola
	 * @param progressivo
	 * @return
	 */
	public ImmagineAnagraficaEdicolaVo getImmagineAnagraficaEdicola(Integer codEdicola, Integer progressivo);
	
	/**
	 * @param anagraficaEdicolaAggiuntiviVo
	 * @param listImg
	 */
	public void saveAnagraficaEdicola(AnagraficaEdicolaAggiuntiviVo anagraficaEdicolaAggiuntiviVo, List<ImmagineAnagraficaEdicolaVo> listImg);
	
	/**
	 * Ritorna le edicole del dl create in data dataCreazione e non ancora aggiornate con i prodotti vari
	 * 
	 * @param codFiegDl
	 * @param dataCreazione
	 * @return
	 */
	public List<EdicolaDto> getNuoveEdicole(Integer codFiegDl, Date dataCreazione);
	
	/**
	 * @param codFiegDl
	 * @param idtn
	 * @return
	 */
	public List<EdicolaDto> getEdicoleByIdtn(Integer codFiegDl, Integer idtn);
	
	/**
	 * @param coddl
	 * @param idtn
	 * @return
	 */
	public List<AbbinamentoEdicolaDlVo> getEdicoleConPubblicazioneInGiacenza(Integer coddl, Integer idtn);
	
	/**
	 * Ritorna la lista di edicole inforiv dl associate allo stesso codice edicola master
	 * 
	 * @param codEdicola
	 * @return
	 */
	public List<AbbinamentoEdicolaDlVo> getEdicoleInforivDlByCodEdicolaWebMaster(Integer codEdicolaMaster);
	
	/**
	 * 
	 * @param edicola
	 * @param kmArea
	 * @return
	 */
	public List<EdicolaDto> getEdicoleInArea(AnagraficaEdicolaVo edicola, Integer kmArea);
	
	
	/**
	 * 
	 * @param codFiegDl
	 * @return List<EdicolaDto>
	 */
	public List<EdicolaDto> getEdicoleDlAttive(Integer codFiegDl);
	
	
	/**
	 * 
	 * @param codEdicolaWeb
	 * @param codDl
	 * @return List<PianoProfiliEdicolaVo>
	 */
	public List<PianoProfiliEdicolaVo> getPianoProfiliEdicola(Integer codEdicolaWeb, Integer codDl );
	
	/**
	 * 
	 * @param codEdicolaWeb
	 * @param codDl
	 * @return PianoProfiliEdicolaVo
	 */
	public PianoProfiliEdicolaVo getPianoProfiloEdicolaAttivo(Integer codEdicolaWeb, Integer codDl );
	
	
}
