package it.dpe.inforiv.bo.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.ftp.FTPClientFactory;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.ImportazioneInforivReplyDto;
import it.dpe.igeriv.exception.EdicolaInforivFtpException;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidFileInforivException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.exception.InvalidTipoRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.pk.BollaResaRiassuntoPk;
import it.dpe.igeriv.vo.pk.DlGruppoModuliPk;
import it.dpe.inforiv.bo.InforivImportBo;
import it.dpe.inforiv.dto.input.InforivBaseDto;
import it.dpe.inforiv.dto.input.InforivDto;
import it.dpe.inforiv.dto.input.InforivNullDtoImpl;
import it.dpe.inforiv.importer.factory.ImportDtoFactory;
import it.dpe.mail.MailingListService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.FixedOrderComparator;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

import com.ancientprogramming.fixedformat4j.exception.FixedFormatException;
import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;

/**
 * Implementazione dell'interfaccia BO per l'importazione dati inforiv. 
 * 
 * @author romanom
 */
@SuppressWarnings({"unchecked","rawtypes"})
@Component("InforivImportBo")
public class InforivImportBoImpl implements InforivImportBo {
	@Autowired
	private IGerivBatchService iGerivBatchBo;
	@Autowired
	private BolleService bolleService;
	@Resource
	private Map<String, Object> mapTipoRecord;
	@Autowired
	private MailingListService mailingListService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ReflectionSaltSource saltSource;
	@Value("${inforiv.numero.giorni.trial.version}")
	private String giorniTrial;
	@Autowired
	private ImportDtoFactory importDtoFactory;
	@Value("${inforiv.giorni.check.bolle.resa}")
	private String giorniCheckBolleResa;
	
	private final Logger log = Logger.getLogger(getClass());
	
	
	@Override
	public void impInforiv(File file) throws Exception {
		log.info("Inforiv inizio importazione file "+file.getName());
		FixedFormatManager manager = new FixedFormatManagerImpl();
		List<ImportException> listErrori = new ArrayList<ImportException>();
		List<InforivDto> listDatiTipoRecord = buildListDatiTipoRecord(file, manager, listErrori);
		if (!listDatiTipoRecord.isEmpty()) {
			Integer codEdicola = listDatiTipoRecord.get(0).getCodEdicola();
			Integer codFiegDl = listDatiTipoRecord.get(0).getCodFiegDl();
			AbbinamentoEdicolaDlVo edicola = iGerivBatchBo.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(codFiegDl, codEdicola);
			try {
				if (edicola == null) {
					throw new InvalidFileInforivException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.edicola.non.esiste"), codEdicola, codFiegDl));
				}
				Integer codEdicolaWeb = edicola.getCodDpeWebEdicola();
				Group<InforivDto> group = group(listDatiTipoRecord, by(on(InforivDto.class).getTipoRecord()));
				Map<String, String> parameters = new HashMap<String, String>();
				for (Group<InforivDto> subgroup : group.subgroups()) {
					List<InforivDto> subGroupList = subgroup.findAll();
					String tipoRecord = subGroupList.get(0).getTipoRecord();
					List<InforivDto> listDati = new ArrayList<InforivDto>();
					try {
						List<String> list = extract(subGroupList, on(InforivBaseDto.class).getRecord());
						buildListDto(subGroupList, manager, listDati, listErrori, list);
						importDtoFactory.getInforivImporter(tipoRecord, list.get(0)).importData(listDati, listErrori, codEdicolaWeb, parameters);
					} catch (InvalidTipoRecordException e) {
						ImportException err = new ImportException(e);
						err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), tipoRecord, "", e.getLocalizedMessage()));
						listErrori.add(err);
					}
				}
				checkExistsBollaResaTesta(codEdicolaWeb, codFiegDl);
				sendWarningEmail(file, listErrori);
			} catch (InvalidFileInforivException e) {
				mailingListService.sendEmail(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.importazione.inforiv.subject"), 
						MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.fatale.importazione.inforiv.body"), file.getName(), e.getLocalizedMessage()));
			} catch (MessagingException e) {
				throw new Exception(IGerivMessageBundle.get("msg.errore.invio.email")); 
			} 
		}
		log.info("Inforiv fine importazione file "+file.getName());
	}
	
	@Override
	public ImportazioneInforivReplyDto importEdicolaInforiv(EdicolaDto edicola) throws Exception {
		ImportazioneInforivReplyDto reply = new ImportazioneInforivReplyDto();
		try {
			Integer codFiegDl = edicola.getCodDl();
			Integer codEdicolaDl = edicola.getCodEdicolaDl();
			String ftpUser = edicola.getFtpUser();
			String ftpPwd = edicola.getFtpPwd();
			String ftpHost = edicola.getFtpHost();
			checkFtp(ftpHost, ftpUser, ftpPwd);
			AbbinamentoEdicolaDlVo abevo = iGerivBatchBo.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(codFiegDl, codEdicolaDl);
			if (abevo == null) {
				AnagraficaAgenziaVo aa = iGerivBatchBo.getAgenziaByCodice(codFiegDl);
				if (aa == null) {
					aa = buildAnagraficaAgenziaVo(edicola);
					iGerivBatchBo.saveBaseVo(aa);
					List<DlGruppoModuliVo> gruppiModuloDl = buildDlGruppoModuliVo(codFiegDl);
					iGerivBatchBo.saveVoList(gruppiModuloDl);
				}
				AnagraficaEdicolaVo aevo = buildAnagraficaEdicolaVo(edicola);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DAY_OF_MONTH, new Integer(giorniTrial));
				Date dataFineAbbonamento = cal.getTime();
				abevo = buildAbbinamentoEdicolaDlVo(edicola, aevo, aa, dataFineAbbonamento);
				UserVo user = buildUserVo(edicola, abevo);
				iGerivBatchBo.saveBaseVo(aevo);
				iGerivBatchBo.saveBaseVo(abevo);
				iGerivBatchBo.saveBaseVo(user);
				String message = MessageFormat.format(IGerivMessageBundle.get("msg.email.nuova.edicola.inforiv"), edicola.getRagioneSociale(), user.getCodUtente(), user.getPwdDecriptata(), giorniTrial, DateUtilities.getTimestampAsString(dataFineAbbonamento, DateUtilities.FORMATO_DATA_SLASH));
				mailingListService.sendEmail(edicola.getEmail(), IGerivMessageBundle.get("msg.subject.nuovo.utente.edicola"), message);
				reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_SUCCESS);
				reply.setMessaggio(IGerivMessageBundle.get("msg.importazione.edicola.inforiv.creata.success"));
			} else {
				reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_WARNING);
				reply.setMessaggio(IGerivMessageBundle.get("msg.importazione.edicola.inforiv.gia.esistente"));
			}
		} catch (EdicolaInforivFtpException e) {
			reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_ERRORE);
			reply.setMessaggio(IGerivMessageBundle.get("msg.importazione.edicola.inforiv.ftp.invalido"));
		} catch (Exception e) {
			reply.setStato(IGerivConstants.STATO_IMPORTAZIONE_EDICOLA_INFORIV_CONCLUSO_ERRORE);
			reply.setMessaggio(IGerivMessageBundle.get("msg.importazione.edicola.errore.fatale"));
			String message = MessageFormat.format(IGerivMessageBundle.get("msg.email.dpe.errore.fatale.importazione.edicola.inforiv"), edicola.getCodDl(), edicola.getCodEdicolaDl(), edicola.getRagioneSociale(), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.email.dpe.errore.fatale.importazione.edicola.inforiv.subject"), message, true);
		}
		return reply;
	}
	
	@Override
	public List<EdicolaDto> getEdicoleInforiv() {
		return iGerivBatchBo.getEdicoleInforiv();
	}
	
	/**
	 * Ordina i file per data contenuta nel nome.
	 * 
	 * @author romanom
	 * 
	 */
	public class FTPFileComparator implements Comparator<FTPFile> {
		@Override
		public int compare(FTPFile arg0, FTPFile arg1) {
			if (arg0 != null && arg1 != null) {
				int year = Integer.parseInt(arg0.getName().substring(9, 11));
				int month = Integer.parseInt(arg0.getName().substring(11, 13));
				int day = Integer.parseInt(arg0.getName().substring(13, 15));
				int hourOfDay = Integer.parseInt(arg0.getName().substring(16, 18));
				int minute = Integer.parseInt(arg0.getName().substring(18, 20));
				int seconds = Integer.parseInt(arg0.getName().substring(20, 22));
				
				int year1 = Integer.parseInt(arg1.getName().substring(9, 11));
				int month1 = Integer.parseInt(arg1.getName().substring(11, 13));
				int day1 = Integer.parseInt(arg1.getName().substring(13, 15));
				int hourOfDay1 = Integer.parseInt(arg1.getName().substring(16, 18));
				int minute1 = Integer.parseInt(arg1.getName().substring(18, 20));
				int seconds1 = Integer.parseInt(arg1.getName().substring(20, 22));
				
				Calendar cal = Calendar.getInstance();
				cal.set(year, month, day, hourOfDay, minute, seconds);
				Calendar cal1 = Calendar.getInstance();
				cal1.set(year1, month1, day1, hourOfDay1, minute1, seconds1);

				return cal.getTime().compareTo(cal1.getTime());
			}
			return 0;
		}
	}
	
	/**
	 * Verifica se esiste la testa bolla di resa
	 * corrispondente ad ogni bolla di consegna per gli ultimi 5 giorni. 
	 * Se non esiste la inserisce.
	 *  
	 * @param codEdicola
	 * @param codFiegDl
	 */
	private void checkExistsBollaResaTesta(Integer codEdicola, Integer codFiegDl) {
		List<BollaRiassuntoVo> listBolle = iGerivBatchBo.getTestaBolleConsegna(codEdicola, codFiegDl, new Integer(giorniCheckBolleResa));
		if (listBolle != null && !listBolle.isEmpty()) {
			for (BollaRiassuntoVo vo : listBolle) {
								
				List<BollaResaRiassuntoVo> brr = bolleService.getBollaResaRiassunto(new Integer[]{codFiegDl},new Integer[]{codEdicola}, vo.getPk().getDtBolla(), vo.getPk().getTipoBolla());
				if (brr == null || brr.isEmpty()) {
					BollaResaRiassuntoVo brrvo = new BollaResaRiassuntoVo();
					BollaResaRiassuntoPk pk = new BollaResaRiassuntoPk();
					pk.setCodEdicola(codEdicola);
					pk.setCodFiegDl(codFiegDl);
					pk.setDtBolla(vo.getPk().getDtBolla());
					pk.setTipoBolla(vo.getPk().getTipoBolla());
					brrvo.setPk(pk);
					//0000175 - 09-03-2015 tolto commento per problemi legati ad errore fatale - impossibile aggiornare ("IGERIV_DATI"."TBL_9620"."IRETR9620") a NULL
					brrvo.setBollaTrasmessaDl(IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA);
					brrvo.setDtTrasmissione(null);
					
					brrvo.setGruppoSconto(codEdicola);
					brrvo.setNumVoci(0);
					iGerivBatchBo.saveBaseVo(brrvo);
				}
			}
		}
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
				client.disconnect();
			}
		}
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
	 * 
	 * @param EdicolaDto edicola
	 * @param AbbinamentoEdicolaDlVo abevo
	 * @return UserVo
	 */
	private UserVo buildUserVo(EdicolaDto edicola, AbbinamentoEdicolaDlVo abevo) {
		UserVo uvo = new UserVo();
		uvo.setAbbinamentoEdicolaDlVo(abevo);
		uvo.setAbilitato(1);
		uvo.setChangePassword(1);
		uvo.setCodUtente("" + iGerivBatchBo.getNextSeqVal(IGerivConstants.SEQ_UTENTI_EDICOLA));
		uvo.setEmail(edicola.getEmail());
		uvo.setNomeUtente(edicola.getRagioneSociale());
		uvo.setPwd(buildUserPwd(abevo, uvo));
		uvo.setPwdCriptata(1);
		uvo.setUtenteAmministratore(1);
		DlGruppoModuliVo dlGruppoModuliVo = iGerivBatchBo.getDlGruppoModuliVo(iGerivBatchBo.getGruppoModuliByRole(IGerivConstants.ROLE_IGERIV_BASE_ADMIN).getId(), edicola.getCodDl());
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
		String pwd = passwordEncoder.encodePassword(randomPwd, saltSource.getSalt(ua));
		return pwd;
	}

	/**
	 * Costruisce l'abbinamento tra l'edicola inforiv e il dl.
	 * @param dataFineAbbonamentoStr 
	 * 
	 * @param EdicolaDto edicola
	 * @param AnagraficaEdicolaVo aevo
	 * @param AnagraficaAgenziaVo aa
	 * @return AbbinamentoEdicolaDlVo
	 */
	private AbbinamentoEdicolaDlVo buildAbbinamentoEdicolaDlVo(EdicolaDto edicola, AnagraficaEdicolaVo aevo, AnagraficaAgenziaVo aa, Date dataFineAbbonamento) {
		AbbinamentoEdicolaDlVo abevo = new AbbinamentoEdicolaDlVo();
		abevo.setAgenziaFatturazione(null);
		abevo.setAnagraficaAgenziaVo(aa);
		abevo.setAnagraficaEdicolaVo(aevo);
		abevo.setCodDpeWebEdicola(aevo.getCodEdicola());
		abevo.setCodEdicolaDl(edicola.getCodEdicolaDl());
		abevo.setCodiceContabileCliente(0l);
		abevo.setDtAttivazioneEdicola(iGerivBatchBo.getSysdate());
		abevo.setDtSospensioneEdicola(null);
		abevo.setEdicolaTest(false);
		abevo.setGruppoSconto(aevo.getCodEdicola());
		abevo.setHostFtp(edicola.getFtpHost());
		abevo.setPwdFtp(edicola.getFtpPwd());
		abevo.setUserFtp(edicola.getFtpUser());
		abevo.setDtSospensioneEdicola(new Timestamp(dataFineAbbonamento.getTime()));
		abevo.setPrivacySottoscritta(true);
		abevo.setEmailValido(true);
		return abevo;
	}

	/**
	 * Costruisce l'anagrafica dell'agenzia dell'edicola inforiv.
	 * 
	 * @param EdicolaDto edicola
	 * @return AnagraficaAgenziaVo
	 */
	private AnagraficaAgenziaVo buildAnagraficaAgenziaVo(EdicolaDto edicola) {
		AnagraficaAgenziaVo aa = new AnagraficaAgenziaVo();
		aa.setCodFiegDl(edicola.getCodDl());
		aa.setRagioneSocialeDlPrimaRiga(null);
		aa.setCodDpeWebDl(iGerivBatchBo.getNextSeqVal(IGerivConstants.SEQ_COD_DPE_WEB_DL).intValue());
		aa.setCodGruppoModuliVo(iGerivBatchBo.getGruppoModuliByRole(IGerivConstants.ROLE_IGERIV_DL_ADV).getId());
		aa.setDlInforiv(true);
		return aa;
	}

	/**
	 * Costruisce l'anagrafica dell'edicola inforiv.
	 * 
	 * @param EdicolaDto edicola
	 * @return AnagraficaEdicolaVo
	 */
	private AnagraficaEdicolaVo buildAnagraficaEdicolaVo(EdicolaDto edicola) {
		AnagraficaEdicolaVo ae = new AnagraficaEdicolaVo();
		ae.setCap(edicola.getCap());
		ae.setCodEdicola(iGerivBatchBo.getLastId(AbbinamentoEdicolaDlVo.class, "codDpeWebEdicola", "anagraficaAgenziaVo.codFiegDl", edicola.getCodDl()));
		ae.setCoddl(edicola.getCodDl());
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
	 * Se la lista di errori non è vuota, invia un email 
	 * con i le inconsistenze riscontrate durante l'importazione.
	 * 
	 * @param File file
	 * @param List<ImportException> listErrori
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	private void sendWarningEmail(File file, List<ImportException> listErrori) throws MessagingException, UnsupportedEncodingException {
		if (!listErrori.isEmpty()) {
			
			
			StringBuffer sb = new StringBuffer("<br><div style='font-size:small'>");
			for (ImportException e : listErrori) {
				sb.append(e.getDescrizione() + "<br><br>");
    			log.warn(String.format("Errori importazione file Inforiv %s - errore %s", file.getName(), e.getDescrizione()));
			}
			sb.append("</div>");
			
			//14/05/2015
			
			
			
//			mailingListService.sendEmailWithAttachment(null, IGerivMessageBundle.get("dpe.validation.msg.warnings.importazione.inforiv.subject"), 
//					MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.warnings.importazione.inforiv.body"), file.getName(), sb.toString()), null, true, null, true, null);
		}
	}
	
	/**
	 * Importa il file in una lista di InforivBaseDto dove è presente l'intera riga nel campo record.
	 * Sorta la lista nell'ordine corretto della LinkedHashMap mapTipoRecord definita nel customBeansContext.xml.
	 * Aggiunge eventuali errori alla listErrori.
	 * 
	 * @param File file
	 * @param FixedFormatManager 
	 * @param List<ImportErrorDto> listErrori
	 * @throws IOException 
	 */
	private List<InforivDto> buildListDatiTipoRecord(File file, FixedFormatManager manager, List<ImportException> listErrori) throws IOException {
		List<InforivDto> list = new ArrayList<InforivDto>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String riga = null;
		while ((riga = reader.readLine()) != null) {
			try {
				InforivBaseDto dto = manager.load(InforivBaseDto.class, riga);
				dto.setRecord(riga);
				list.add(dto);
			} catch (FixedFormatException ffe) {
				ImportException err = new ImportException(ffe);
				err.setDescrizione(ffe.getLocalizedMessage());
				listErrori.add(err);
			}
		}
		if (reader != null) {
			reader.close();
		}
		Comparator<InforivDto> tipoRecordComparator = new FixedOrderComparator(mapTipoRecord.keySet().toArray());
		Comparator<InforivDto> inforivDtoComparator = new BeanComparator("tipoRecord", tipoRecordComparator);
		Collections.sort(list, inforivDtoComparator);
		return list;
	}

	/**
	 * Costruisce la lista di dto corrispondente al tipo record e aggiunge eventuali errori alla listErrori.
	 * 
	 * @param list 
	 * @param List<BaseInforivDto> listTipoRecord 
	 * @param FixedFormatManager manager
	 * @param List<ImportErrorDto> listErrori
	 */
	private void buildListDto(List<InforivDto> listTipoRecord, FixedFormatManager manager, List listDati, List<ImportException> listErrori, List<String> list) throws InvalidTipoRecordException {
		String tipoRecord = listTipoRecord.get(0).getTipoRecord();
		for (String riga : list) {
			try {
				Object tr = importDtoFactory.getImportDto(tipoRecord, riga);
				if (tr != null && !(tr instanceof InforivNullDtoImpl)) {
					Object load = manager.load(tr.getClass(), riga);
					((InforivDto) load).validate(riga);
					listDati.add(load);
				}
			} catch (FixedFormatException ffe) {
				ImportException err = new ImportException(ffe);
				err.setDescrizione(ffe.getLocalizedMessage());
				listErrori.add(err);
			} catch (InvalidRecordException e) {
				ImportException err = new ImportException(e);
				err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), tipoRecord, riga, e.getLocalizedMessage()));
				listErrori.add(err);
			}
		}
	}
	
	public IGerivBatchService getiGerivBatchBo() {
		return iGerivBatchBo;
	}

	public void setiGerivBatchBo(IGerivBatchService iGerivBatchBo) {
		this.iGerivBatchBo = iGerivBatchBo;
	}

	public Map<String, Object> getMapTipoRecord() {
		return mapTipoRecord;
	}

	public void setMapTipoRecord(Map<String, Object> mapTipoRecord) {
		this.mapTipoRecord = mapTipoRecord;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public ReflectionSaltSource getSaltSource() {
		return saltSource;
	}

	public void setSaltSource(ReflectionSaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public String getGiorniTrial() {
		return giorniTrial;
	}

	public void setGiorniTrial(String giorniTrial) {
		this.giorniTrial = giorniTrial;
	}

	public String getGiorniCheckBolleResa() {
		return giorniCheckBolleResa;
	}

	public void setGiorniCheckBolleResa(String giorniCheckBolleResa) {
		this.giorniCheckBolleResa = giorniCheckBolleResa;
	}

	public ImportDtoFactory getImportDtoFactory() {
		return importDtoFactory;
	}

	public void setImportDtoFactory(ImportDtoFactory importDtoFactory) {
		this.importDtoFactory = importDtoFactory;
	}
	
}
