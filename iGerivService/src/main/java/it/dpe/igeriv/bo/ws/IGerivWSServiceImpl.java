package it.dpe.igeriv.bo.ws;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.gdo.dto.GdoVenditaDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ClientCompatibilitaVersioniVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.RichiestaDatiWSVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.UtenteAgenziaVo;
import it.dpe.igeriv.vo.VenditaDettaglioVo;
import it.dpe.igeriv.vo.VenditaVo;
import it.dpe.igeriv.vo.pk.VenditaDettaglioPk;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementazione dell'interfaccia business WS per iGeriv. 
 * 
 * @author romanom
 * 
 */
@Service("IGerivWSService")
class IGerivWSServiceImpl extends BaseServiceImpl implements IGerivWSService {
	private final IGerivWSBo wsBo;
	private final PubblicazioniService pubblicazioniService;
	private final AccountService accountService;
	private final BolleService bolleService;
	
	@Autowired
	IGerivWSServiceImpl(IGerivWSBo wsBo, PubblicazioniService pubblicazioniService, AccountService accountService, BolleService bolleService) {
		super(wsBo);
		this.wsBo = wsBo;
		this.pubblicazioniService = pubblicazioniService;
		this.accountService = accountService;
		this.bolleService = bolleService;
	}
	
	@Override
	public UserVo getEdicolaByCodice(String codEdicola) {
		return accountService.getEdicolaByCodice(codEdicola);
	}
	
	@Override
	public UserVo getEdicolaByEmail(String email) {
		return accountService.getEdicolaByEmail(email);
	}
	
	@Override
	public UtenteAgenziaVo getAgenziaByCodice(Integer codFiegDl) {
		return wsBo.getAgenziaByCodice(codFiegDl);
	}
	
	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita) {
		return wsBo.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(codiceDl, codiceRivendita);
	}
	
	@Override
	public DlGruppoModuliVo getDlGruppoModuliVo(Integer idGruppoModuli, Integer codDl) {
		return wsBo.getDlGruppoModuliVo(idGruppoModuli, codDl);
	}
	
	@Override
	public GruppoModuliVo getGruppoModuliByRole(String roleIgerivClienteEdicola) {
		return wsBo.getGruppoModuliByRole(roleIgerivClienteEdicola);
	}
	
	@Override
	public List<BollaDettaglioDto> getDettaglioBolla(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dataBolla, boolean isMultiDl) {
		return bolleService.getDettaglioBolla(codFiegDl, codEdicola, dataBolla, null, false, isMultiDl);
	}
	
	@Override
	public List<FondoBollaDettaglioDto> getDettagliFondoBolla(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dataBolla, String tipoBolla, boolean showSoloRigheDaSpuntare, boolean showSoloRifornimenti, boolean isMultiDl) {
		return bolleService.getDettagliFondoBollaPubblicazioni(codFiegDl, codEdicola, dataBolla, tipoBolla, showSoloRigheDaSpuntare, showSoloRifornimenti, isMultiDl);
	}
	
	@Override
	public UserAbbonato buildUserDetails(String userId, BaseVo utenteBase) {
		return accountService.buildUserDetails(userId, utenteBase);
	}
	
	@Override
	public VenditaVo importDatiVenditeGdo(UserAbbonato user, List<GdoVenditaDto> list, List<String> warnings) throws IGerivBusinessException {
		VenditaVo vendita = new VenditaVo();
		vendita.setCodVendita(bolleService.getNextSeqVal(IGerivConstants.SEQ_VENDITE));
		vendita.setCodEdicola(user.getCodEdicolaMaster());
		vendita.setCodFiegDl(user.getCodFiegDlMaster());
		vendita.setCodUtente(user.getCodUtente());
		vendita.setDataVendita(new Timestamp(new Date().getTime()));
		vendita.setImportoTotale(sum(list, on(GdoVenditaDto.class).getImporto()));
		vendita.setContoScontrinato(true);
		vendita.setPagato(true);
		vendita.setIdScontrino(null);
		vendita.setDataScontrino(getSysdate());
		vendita.setFatturaEmessa(false);
		vendita.setFatturaContoUnico(false);
		vendita.setIdFattura(null);
		vendita.setProvenienzaConto(IGerivConstants.PROVENIENZA_SISTEMA_TGS);
		vendita.setDataCompetenzaEstrattoContoClienti(null);
		vendita.setDeleted(false);
		vendita.setTrasferitaGestionale(IGerivConstants.INDICATORE_RECORD_NON_TRASFERITO);
		List<VenditaDettaglioVo> listVenditaDettaglio = buildListVenditaDettaglioVo(list, vendita, user, warnings);
		vendita.setListVenditaDettaglio(listVenditaDettaglio);
		return bolleService.saveBaseVo(vendita);
	}
	
	/**
	 * @param list
	 * @param vendita
	 * @param user
	 * @param errori 
	 * @return
	 * @throws IGerivBusinessException
	 */
	private List<VenditaDettaglioVo> buildListVenditaDettaglioVo(List<GdoVenditaDto> list, VenditaVo vendita, UserAbbonato user, List<String> warnings) throws IGerivBusinessException {
		List<VenditaDettaglioVo> listVenditaDettaglio = new ArrayList<VenditaDettaglioVo>();
		Integer progressivo = 1;
		for (GdoVenditaDto dto : list) {
			PubblicazioneDto prod = pubblicazioniService.getCopertinaByIdtn(user.getCodFiegDlMaster(), dto.getIdtn());
			if (prod == null) {
				warnings.add(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.idtn.non.trovato.igeriv"), dto.getIdtn().toString()));
				continue;
			}
			VenditaDettaglioVo vo = new VenditaDettaglioVo();
			VenditaDettaglioPk pk = new VenditaDettaglioPk();
			pk.setVenditaVo(vendita);
			pk.setProgressivo(progressivo++);
			vo.setPk(pk);
			vo.setIdtn(dto.getIdtn());
			vo.setCodFiegDl(user.getCodFiegDlMaster());
			vo.setCodEdicola(user.getCodEdicolaMaster());
			vo.setPrezzoCopertina(dto.getPrezzoLordo());
			vo.setSottoTitolo(prod.getSottoTitolo());
			vo.setTitolo(prod.getTitolo());
			vo.setNumeroCopertina(prod.getNumeroCopertina());
			vo.setImportoTotale(dto.getImporto());
			vo.setQuantita(dto.getCopie());
			vo.setDeleted(false);
			vo.setTrasferitaGestionale(false);
			listVenditaDettaglio.add(vo);
		}
		return listVenditaDettaglio;
	}
	
	@Override
	public RichiestaDatiWSVo getRichiestaDatiWS(Integer codEdicola, Timestamp dataBolla) {
		return wsBo.getRichiestaDatiWS(codEdicola, dataBolla);
	}
	
	@Override
	public ClientCompatibilitaVersioniVo getClientCompatibilitaVersione(String app, Float clientVersion) {
		return wsBo.getClientCompatibilitaVersione(app, clientVersion);
	}
	
}
