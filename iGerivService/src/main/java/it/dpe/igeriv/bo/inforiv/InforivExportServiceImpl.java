package it.dpe.igeriv.bo.inforiv;

import it.dpe.ftp.FTPClientFactory;
import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.ws.IGerivWSService;
import it.dpe.igeriv.dto.Edicola;
import it.dpe.igeriv.dto.ImportazioneInforivReplyDto;
import it.dpe.igeriv.exception.EdicolaGiaEsistenteException;
import it.dpe.igeriv.exception.EdicolaInforivFtpException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AbbinamentoTipoMovimentoFondoBollaInforivVo;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.LimitiPeriodicitaVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.UtenteAgenziaVo;
import it.dpe.igeriv.vo.pk.DlGruppoModuliPk;
import it.dpe.igeriv.vo.pk.LimitiPeriodicitaPk;
import it.dpe.igeriv.vo.pk.ParametriEdicolaPk;
import it.dpe.inforiv.dto.output.InforivMancanzeEccedenzeDto;
import it.dpe.inforiv.dto.output.InforivResaDichiarataDto;
import it.dpe.inforiv.dto.output.InforivTotaleBollaConsegnaAccertataDto;
import it.dpe.inforiv.dto.output.InforivTotaleBollaResaEdicolaDto;
import it.dpe.inforiv.dto.output.InforivVariazioniServizioDto;
import it.dpe.inforiv.dto.output.InforivVenditeDto;

import java.io.IOException;
import java.net.SocketException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Service;

/**
 * @author mromano
 * 
 */
@Service("InforivExportService")
@SuppressWarnings("deprecation")
class InforivExportServiceImpl extends BaseServiceImpl implements InforivExportService {
	private final Logger log = Logger.getLogger(getClass());
	private final InforivExportBo bo;
	private final IGerivWSService iGerivWSService;
	private final PasswordEncoder passwordEncoder;
	private final ReflectionSaltSource saltSource;
	
	@Autowired
	InforivExportServiceImpl(InforivExportBo bo, IGerivWSService iGerivWSService, PasswordEncoder passwordEncoder, ReflectionSaltSource saltSource) {
		super(bo);
		this.bo = bo;
		this.iGerivWSService = iGerivWSService;
		this.passwordEncoder = passwordEncoder;
		this.saltSource = saltSource;
	}
	
	@Override
	public List<InforivTotaleBollaConsegnaAccertataDto> getBolleRiassuntoDaTrasmettereDl(Integer codFiegDl) {
		return bo.getBolleRiassuntoDaTrasmettereDl(codFiegDl);
	}

	@Override
	public void updateBolleRiassuntoTrasmesseDl(Integer codFiegDl) {
		bo.updateBolleRiassuntoTrasmesseDl(codFiegDl);
	}

	@Override
	public void updateBolleResaRiassuntoTrasmesseDl(Integer codFiegDl) {
		bo.updateBolleResaRiassuntoTrasmesseDl(codFiegDl);
	}

	@Override
	public List<InforivMancanzeEccedenzeDto> getBollaDettaglioConDifferenze(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla) {
		return bo.getBollaDettaglioConDifferenze(codFiegDl, codEdicola, dataBolla, tipoBolla);
	}

	@Override
	public List<InforivTotaleBollaResaEdicolaDto> getBolleResaRiassuntoDaTrasmettereDl(Integer codFiegDl) {
		return bo.getBolleResaRiassuntoDaTrasmettereDl(codFiegDl);
	}

	@Override
	public List<InforivResaDichiarataDto> getBollaResaDettaglio(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla) {
		return bo.getBollaResaDettaglio(codFiegDl, codEdicola, dataBolla, tipoBolla);
	}

	@Override
	public List<InforivResaDichiarataDto> getBollaResaDettaglioFuoriVoce(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla) {
		return bo.getBollaResaDettaglioFuoriVoce(codFiegDl, codEdicola, dataBolla, tipoBolla);
	}

	@Override
	public List<InforivResaDichiarataDto> getBollaResaDettaglioRichiamoPersonalizzato(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla) {
		return bo.getBollaResaDettaglioRichiamoPersonalizzato(codFiegDl, codEdicola, dataBolla, tipoBolla);
	}

	@Override
	public void updateBollaResaRiassuntoTrasmesseDl(Integer codFiegDl) {
		bo.updateBollaResaRiassuntoTrasmesseDl(codFiegDl);
	}

	@Override
	public List<InforivVenditeDto> getVenditeDaTrasmettereDl(Integer codFiegDl) {
		return bo.getVenditeDaTrasmettereDl(codFiegDl);
	}

	@Override
	public List<RichiestaRifornimentoVo> getRichiesteRifornimenti(Integer codFiegDl) {
		return bo.getRichiesteRifornimenti(codFiegDl);
	}

	@Override
	public List<InforivVariazioniServizioDto> getVariazioni(Integer codFiegDl) {
		return bo.getVariazioni(codFiegDl);
	}

	@Override
	public void updateVenditeTrasmesse(Integer codFiegDl) {
		bo.updateVenditeTrasmesse(codFiegDl);
	}

	@Override
	public void updateRichiesteRifornimenti(Integer codFiegDl) {
		bo.updateRichiesteRifornimenti(codFiegDl);
	}

	@Override
	public void updateVariazioni(Integer codFiegDl) {
		bo.updateVariazioni(codFiegDl);
	}

	@Override
	public AbbinamentoTipoMovimentoFondoBollaInforivVo getAbbinamentoTipoMovimentoFondoBollaInforiv(Integer tipoMovimentoInforiv) {
		return bo.getAbbinamentoTipoMovimentoFondoBollaInforiv(tipoMovimentoInforiv);
	}
	
	@Override
	public ImportazioneInforivReplyDto importEdicola(Edicola edicola, boolean isNetEdicola, Integer giorniTrial, String igerivUrl, boolean validateFtpConection, boolean edicolaIGerivInforivDl) throws EdicolaGiaEsistenteException, SocketException, IOException, EdicolaInforivFtpException {
		log.info("Entered importEdicola with params: edicola=" + edicola + " isNetEdicola=" + isNetEdicola);
		ImportazioneInforivReplyDto reply = new ImportazioneInforivReplyDto();
		Integer codFiegDl1 = edicola.getCodDl();
		Integer codFiegDl2 = edicola.getCodDl2();
		Integer codFiegDl3 = edicola.getCodDl3();
		Integer codEdicolaDl1 = edicola.getCodEdicolaDl();
		Integer codEdicolaDl2 = edicola.getCodEdicolaDl2();
		Integer codEdicolaDl3 = edicola.getCodEdicolaDl3();
		String ftpUser = edicola.getFtpUser();
		String ftpPwd = edicola.getFtpPwd();
		String ftpHost = edicola.getFtpHost();
		if (validateFtpConection) {
			checkFtp(ftpHost, ftpUser, ftpPwd);
		}
		Integer[] arrCodFiegDl = new Integer[]{codFiegDl1,codFiegDl2,codFiegDl3};
		Integer[] arrCodEdicolaDl = new Integer[]{codEdicolaDl1,codEdicolaDl2,codEdicolaDl3};
		
		Date dataFineAbbonamento = null;
		UserVo user = null;
		boolean edicolaNuova = false;
		boolean isMultiDl = arrCodFiegDl.length > 1 && arrCodFiegDl[0] != null && arrCodFiegDl[1] != null && arrCodFiegDl[0] > 0 && arrCodFiegDl[1] > 0;
		Integer codEdicolaMaster = null;
		for (int i = 0; i < arrCodFiegDl.length; i++) {
			Integer codFiegDl = arrCodFiegDl[i];
			Integer codEdicola = arrCodEdicolaDl[i];
			if (codFiegDl != null && codEdicola != null && codFiegDl > 0 && codEdicola > 0) {
				AbbinamentoEdicolaDlVo abevo = iGerivWSService.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(codFiegDl, codEdicola);
				if (abevo == null) {
					UtenteAgenziaVo aa = iGerivWSService.getAgenziaByCodice(codFiegDl);
					if (aa == null) {
						AnagraficaAgenziaVo ag = buildAnagraficaAgenziaVo(codFiegDl, isMultiDl);
						saveBaseVo(ag);
						List<DlGruppoModuliVo> gruppiModuloDl = buildDlGruppoModuliVo(codFiegDl);
						saveVoList(gruppiModuloDl);
						List<LimitiPeriodicitaVo> limitiPeriodicita = buildLimitiPeriodicita(codFiegDl);
						saveVoList(limitiPeriodicita);
						aa = new UtenteAgenziaVo();
						aa.setCodFiegDl(codFiegDl);
						aa.setCodGruppo(ag.getCodGruppoModuliVo());
						aa.setCodUtente(codFiegDl);
						aa.setEmail(ag.getEmail());
						aa.setPasswordDl(null);
						aa.setAgenzia(ag);
						saveBaseVo(aa);
					}
					AnagraficaEdicolaVo aevo = buildAnagraficaEdicolaVo(edicola, codFiegDl);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DAY_OF_MONTH, new Integer(giorniTrial));
					dataFineAbbonamento = cal.getTime();
					abevo = buildAbbinamentoEdicolaDlVo(edicola, aevo, aa.getAgenzia(), dataFineAbbonamento, codEdicola, isNetEdicola, edicolaIGerivInforivDl);
					if (!edicolaNuova) {
						codEdicolaMaster = aevo.getCodEdicola();
					}
					if (isMultiDl) {
						abevo.setCodEdicolaMaster(codEdicolaMaster);
					}
					saveBaseVo(aevo);
					saveBaseVo(abevo);
					UserVo currUser = buildUserVo(edicola, abevo, isNetEdicola);
					if (!edicolaNuova) {
						user = currUser;
					} else {
						currUser.setEmail(null);
						currUser.setAbilitato(0);
					}
					saveBaseVo(currUser);
					saveVoList(buildListParametriEdicola(codEdicola));
					edicolaNuova = true;
				}
			}
		}
		if (!edicolaNuova) {
			throw new EdicolaGiaEsistenteException();
		}
		if (isMultiDl) {
			UtenteAgenziaVo agenzia1 = iGerivWSService.getAgenziaByCodice(arrCodFiegDl[0]);
			AnagraficaAgenziaVo aa = agenzia1 != null ? agenzia1.getAgenzia() : null;
			UtenteAgenziaVo agenzia2 = iGerivWSService.getAgenziaByCodice(arrCodFiegDl[1]);
			AnagraficaAgenziaVo aa1 = agenzia2 != null ? agenzia2.getAgenzia() : null;
			AnagraficaAgenziaVo aa2 = (arrCodFiegDl[2] != null) ? (iGerivWSService.getAgenziaByCodice(arrCodFiegDl[2]) != null ? (AnagraficaAgenziaVo) iGerivWSService.getAgenziaByCodice(arrCodFiegDl[2]).getAgenzia() : null) : null;
			aa.setCodFiegDlMaster(aa.getCodFiegDl());
			aa1.setCodFiegDlMaster(aa.getCodFiegDl());
			aa.setAgenziaSecondaria(aa1);
			aa1.setAgenziaSecondaria(aa);
			saveBaseVo(aa1);
			saveBaseVo(aa);
			if (aa2 != null) {
				aa2.setCodFiegDlMaster(aa.getCodFiegDl());
				aa2.setAgenziaSecondaria(aa1);
				saveBaseVo(aa2);
			}
		}
		String message = MessageFormat.format(IGerivMessageBundle.get("msg.email.nuova.edicola.inforiv"), (edicola != null ? edicola.getRagioneSociale() : ""), igerivUrl, (user != null ? user.getCodUtente() : ""), (user != null ? user.getPwdDecriptata() : ""), giorniTrial, (dataFineAbbonamento != null ? DateUtilities.getTimestampAsString(dataFineAbbonamento, DateUtilities.FORMATO_DATA_SLASH) : ""));
		reply.setEmailMessage(message);
		reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_SUCCESS);
		reply.setMessaggio(IGerivMessageBundle.get("msg.importazione.edicola.inforiv.creata.success"));
		return reply;
	}
	
	/**
	 * Testa la connessione Ftp con username e pwd fornite dall'edicole.
	 * @param ftpPwd 
	 * 
	 * @param String ftpUser
	 * @param String ftpPwd
	 * @throws SocketException
	 * @throws IOException
	 * @throws EdicolaInforivFtpException
	 */
	private void checkFtp(String ftpHost, String ftpUser, String ftpPwd) throws SocketException, IOException, EdicolaInforivFtpException {
		FTPClient client = null;
		try {
			client = FTPClientFactory.getClient(ftpHost, ftpUser, ftpPwd, "/", 21, null, FTPClient.ACTIVE_LOCAL_DATA_CONNECTION_MODE, null);
			if (!client.isConnected()) {
				throw new EdicolaInforivFtpException(ftpHost, ftpUser, ftpPwd);
			}
		} catch (Exception e) {
			throw new EdicolaInforivFtpException(ftpHost, ftpUser, ftpPwd);
		} finally {
			if (client != null) {
				client.logout();
				client.disconnect();
				client = null;
			}
		}
	}

	/**
	 * Costruisce i limite delle periodicita
	 * @param Integer codFiegDl
	 * @return List<LimitiPeriodicitaVo>
	 */
	private List<LimitiPeriodicitaVo> buildLimitiPeriodicita(Integer codFiegDl) {
		List<LimitiPeriodicitaVo> list = new ArrayList<LimitiPeriodicitaVo>();
		for (int i = 0; i <= 10; i++) {
			LimitiPeriodicitaVo vo = new LimitiPeriodicitaVo();
			LimitiPeriodicitaPk pk = new LimitiPeriodicitaPk();
			pk.setCodFiegDl(codFiegDl);
			pk.setCodPeriodicita(i);
			vo.setNumMaxCopertineVecchieResa(5);
			vo.setNumMaxStoriaCopertine(5);
			vo.setPermetteResaNumeroNuovo("S");
			vo.setMaxStatisticaVisualizzare(12);
			vo.setPermettePubblicazioniInContoDeposito("N");
			vo.setGiorniValiditaRichiesteRifornimento(7);
			vo.setPk(pk);
			list.add(vo);
		}
		return list;
	}

	
	/**
	 * Costruisce la lista di gruppi modulo per il nuovo dl.
	 * 
	 * @param Integer codFiegDl
	 * @return List<GruppoModuliVo>
	 */
	private List<DlGruppoModuliVo> buildDlGruppoModuliVo(Integer codFiegDl) {
		List<DlGruppoModuliVo> gruppiModuloDl = new ArrayList<DlGruppoModuliVo>();
		for (int i = 1; i < 8; i++) {
			DlGruppoModuliVo vo = new DlGruppoModuliVo();
			DlGruppoModuliPk pk = new DlGruppoModuliPk();
			pk.setCodDl(codFiegDl);
			pk.setCodGruppo(i);
			vo.setPk(pk);
			gruppiModuloDl.add(vo);
		}
 		return gruppiModuloDl;
	}
	
	/**
	 * Costruisce un utente per il login dell'edicola inforiv.
	 * @param isNetEdicola 
	 * 
	 * @param EdicolaDto edicola
	 * @param AbbinamentoEdicolaDlVo abevo
	 * @return UserVo
	 */
	private UserVo buildUserVo(Edicola edicola, AbbinamentoEdicolaDlVo abevo, boolean isNetEdicola) {
		UserVo uvo = new UserVo();
		uvo.setAbbinamentoEdicolaDlVo(abevo);
		uvo.setAbilitato(1);
		uvo.setChangePassword(0);
		uvo.setCodUtente("" + getNextSeqVal(IGerivConstants.SEQ_UTENTI_EDICOLA));
		uvo.setEmail(edicola.getEmail());
		uvo.setNomeUtente(edicola.getRagioneSociale());
		uvo.setPwd(buildUserPwd(abevo, uvo));
		uvo.setPwdCriptata(1);
		uvo.setUtenteAmministratore(1);
		DlGruppoModuliVo dlGruppoModuliVo = iGerivWSService.getDlGruppoModuliVo(iGerivWSService.getGruppoModuliByRole(isNetEdicola ? IGerivConstants.ROLE_IGERIV_NET_EDICOLA : IGerivConstants.ROLE_IGERIV_BASE_ADMIN).getId(), edicola.getCodDl());
		uvo.setDlGruppoModuliVo(dlGruppoModuliVo);
		return uvo;
	}

	/**
	 * Costruisce la password dell'utente dell'edicola inforiv.
	 * 
	 * @param AbbinamentoEdicolaDlVo abevo
	 * @param UserVo uvo
	 * @return String
	 */
	private String buildUserPwd(AbbinamentoEdicolaDlVo abevo, UserVo uvo) {
		String randomPwd = StringUtility.getRandomString(8);
		uvo.setPwdDecriptata(randomPwd);
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		authList.add(new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_BASE));
		UserAbbonato ua = new UserAbbonato("" + uvo.getCodUtente(), randomPwd, true, true, true, true, authList);
		ua.setId(abevo.getCodDpeWebEdicola());
		ua.setCodUtente(uvo.getCodUtente());
		ua.setCodFiegDl(abevo.getAnagraficaAgenziaVo().getCodFiegDl());
		ua.setCodEdicolaDl(abevo.getCodEdicolaDl());
		String pwd = passwordEncoder.encodePassword(randomPwd, saltSource.getSalt(ua));
		return pwd;
	}

	/**
	 * Costruisce l'abbinamento tra l'edicola inforiv e il dl.
	 * @param codEdicola 
	 * @param isNetEdicola 
	 * @param edicolaIGerivInforivDl 
	 * @param dataFineAbbonamentoStr 
	 * 
	 * @param EdicolaDto edicola
	 * @param AnagraficaEdicolaVo aevo
	 * @param AnagraficaAgenziaVo aa
	 * @return AbbinamentoEdicolaDlVo
	 */
	private AbbinamentoEdicolaDlVo buildAbbinamentoEdicolaDlVo(Edicola edicola, AnagraficaEdicolaVo aevo, AnagraficaAgenziaVo aa, Date dataFineAbbonamento, Integer codEdicola, boolean isNetEdicola, Boolean edicolaIGerivInforivDl) {
		AbbinamentoEdicolaDlVo abevo = new AbbinamentoEdicolaDlVo();
		abevo.setAgenziaFatturazione(null);
		abevo.setAnagraficaAgenziaVo(aa);
		abevo.setAnagraficaEdicolaVo(aevo);
		abevo.setCodDpeWebEdicola(aevo.getCodEdicola());
		abevo.setCodEdicolaDl(codEdicola);
		abevo.setCodiceContabileCliente(0l);
		abevo.setDtAttivazioneEdicola(getSysdate());
		abevo.setDtSospensioneEdicola(null);
		abevo.setEdicolaTest(false);
		abevo.setGruppoSconto(aevo.getCodEdicola());
		abevo.setHostFtp(edicola.getFtpHost());
		abevo.setPwdFtp(edicola.getFtpPwd());
		abevo.setUserFtp(edicola.getFtpUser());
		abevo.setDtSospensioneEdicola(new Timestamp(dataFineAbbonamento.getTime()));
		abevo.setPrivacySottoscritta(true);
		abevo.setEmailValido(true);
		abevo.setAnagraficaCompilata(false);
		abevo.setCondizioniUsoAccettate(false);
		abevo.setAggiornataProdottiVariDl(false);
		abevo.setEdicolaIGerivInforivDl(edicolaIGerivInforivDl);
		if (isNetEdicola) {
			abevo.setEdicolaIGerivNet(true);
			abevo.setEdicolaIGerivInforiv(false);
		} else {
			abevo.setEdicolaIGerivInforiv(true);
			abevo.setEdicolaIGerivNet(false);
		}
		return abevo;
	}

	/**
	 * Costruisce l'anagrafica dell'agenzia dell'edicola inforiv.
	 * @param isMultiDl 
	 * 
	 * @param Integer codFiegDl
	 * @return AnagraficaAgenziaVo
	 */
	private AnagraficaAgenziaVo buildAnagraficaAgenziaVo(Integer codFiegDl, boolean isMultiDl) {
		AnagraficaAgenziaVo aa = new AnagraficaAgenziaVo();
		aa.setCodFiegDl(codFiegDl);
		aa.setRagioneSocialeDlPrimaRiga("Agenzia " + codFiegDl);
		aa.setCodDpeWebDl(getNextSeqVal(IGerivConstants.SEQ_COD_DPE_WEB_DL).intValue());
		aa.setCodGruppoModuliVo(iGerivWSService.getGruppoModuliByRole(IGerivConstants.ROLE_IGERIV_DL_ADV).getId());
		aa.setHasButtonCopiaDifferenze(true);
		aa.setHasResaAnticipata(true);
		aa.setDlInforiv(true);
		aa.setEdicoleVedonoMessaggiDpe(true);
		aa.setVenditeEsauritoControlloGiacenzaDL(false);
		aa.setHasPopupConfermaSuMemorizzaInviaBolle(true);
		aa.setHasMessaggioDocumentoDisponibile(false);
		return aa;
	}

	/**
	 * Costruisce l'anagrafica dell'edicola inforiv.
	 * @param codEdicola 
	 * 
	 * @param EdicolaDto edicola
	 * @return AnagraficaEdicolaVo
	 */
	private AnagraficaEdicolaVo buildAnagraficaEdicolaVo(Edicola edicola, Integer codFiegDl) {
		AnagraficaEdicolaVo ae = new AnagraficaEdicolaVo();
		ae.setCap(edicola.getCap());
		ae.setCodEdicola(getNextSeqVal(IGerivConstants.SEQ_COD_DPE_WEB_RIV).intValue());
		ae.setCoddl(codFiegDl);
		ae.setCodiceFiscale(edicola.getCodFiscale());
		ae.setCodNazione(IGerivConstants.COD_NAZIONE_ITALIA);
		ae.setEmail(edicola.getEmail());
		ae.setFax(edicola.getFax());
		ae.setIndirizzoEdicolaPrimaRiga(edicola.getIndirizzo());
		ae.setLocalitaEdicolaPrimaRiga(edicola.getLocalita());
		ae.setPassword(edicola.getPassword());
		ae.setPiva(edicola.getPiva());
		ae.setRagioneSocialeEdicolaPrimaRiga(edicola.getRagioneSociale());
		ae.setSiglaProvincia(edicola.getProvincia());
		ae.setTelefono(edicola.getTelefono());
		return ae;
	}
	
	/**
	 * @param codEdicola
	 * @return
	 */
	private List<ParametriEdicolaVo> buildListParametriEdicola(Integer codEdicola) {
		List<ParametriEdicolaVo> list = new ArrayList<ParametriEdicolaVo>();
		ParametriEdicolaVo paramModalitaBollaInforiv = new ParametriEdicolaVo();
		ParametriEdicolaPk pk = new ParametriEdicolaPk();
		pk.setCodEdicola(codEdicola);
		pk.setCodParametro(IGerivConstants.COD_PARAMETRO_ABILITA_MODALITA_INFORIV_BOLLE);
		paramModalitaBollaInforiv.setPk(pk);
		paramModalitaBollaInforiv.setValue("true");
		list.add(paramModalitaBollaInforiv);
		return list;
	}

}
