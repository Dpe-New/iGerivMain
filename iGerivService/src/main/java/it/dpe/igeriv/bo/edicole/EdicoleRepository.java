package it.dpe.igeriv.bo.edicole;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.EstrattoContoDto;
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

import java.util.List;


interface EdicoleRepository extends BaseRepository {
	
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
	 * @param idProfiloDL
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
	 * @param codiceDl
	 * @param codiceRivendita
	 */
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita);
	
	/**
	 * @param codiceDl
	 * @param codiceRivendita
	 * @return
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
	public void updatePwdUserEdicola(Integer codEdicolaDl, String password, boolean changePassword, boolean isPwdCripata);
	
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
	 * @param edicolaPlus 
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
	 * @param codEdicola
	 * @param codDl
	 * @param email
	 * @return
	 */
	public BonusInvitaUnCollegaVo getBonusInvitaUnCollega(Integer codEdicola, Integer codDl);
	
	/**
	 * @param arrId
	 * @param arrCodFiegDl
	 * @return
	 */
	public Boolean getEmailValidoMultiDl(Integer[] arrId, Integer[] arrCodFiegDl);
	
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
	 * @param progressivo
	 * @return
	 */
	public ImmagineAnagraficaEdicolaVo getImmagineAnagraficaEdicola(Integer codEdicola, Integer progressivo);

	/**
	 * @param codFiegDl
	 * @param dataCreazione
	 * @return
	 */
	public List<EdicolaDto> getNuoveEdicole(Integer codFiegDl, java.util.Date dataCreazione);

	/**
	 * @param codFiegDl
	 * @param idtn
	 * @return
	 */
	public List<EdicolaDto> getEdicoleByIdtn(final Integer codFiegDl, final Integer idtn);

	/**
	 * @param coddl
	 * @param idtn
	 * @return
	 */
	public List<EdicolaDto> getEdicoleConPubblicazioneInGiacenza(Integer coddl, Integer idtn);

	/**
	 * @param codDl
	 * @param codEdicolaWeb
	 * @param codEdicolaDl
	 * @param ragioneSociale
	 * @return
	 */
	public List<EdicolaDto> getEdicoleInforivDl(Integer codDl, Integer codEdicolaWeb, Integer codEdicolaDl, String ragioneSociale, boolean utenteAmministratore);

	/**
	 * @param codEdicola
	 * @return
	 */
	public List<AbbinamentoEdicolaDlVo> getEdicoleInforivDlByCodEdicolaWebMaster(Integer codEdicola);
	
	/**
	 * 
	 * @param edicola
	 * @param kmArea
	 * @return List<EdicolaDto>
	 */
	public List<EdicolaDto> getEdicoleInArea(final AnagraficaEdicolaVo edicola,final Integer kmArea);
	
	
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
