package it.dpe.igeriv.bo.rifornimenti;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.dto.EsportazioneDatiDlResultDto;
import it.dpe.igeriv.dto.PrenotazioneDto;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.dto.RichiestaFissaClienteDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.PrenotazioneVo;
import it.dpe.igeriv.vo.RichiestaClienteVo;
import it.dpe.igeriv.vo.RichiestaFissaClienteEdicolaVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoLivellamentiVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.RisposteClientiCodificateVo;
import it.dpe.igeriv.vo.pk.PrenotazionePk;
import it.dpe.igeriv.vo.pk.RichiestaClientePk;
import it.dpe.igeriv.vo.pk.RichiestaFissaClienteEdicolaPk;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("RifornimentiService")
class RifornimentiServiceImpl extends BaseServiceImpl implements RifornimentiService {
	private final RifornimentiRepository repository;
	private final PubblicazioniService pubblicazioniService;
	private final VenditeService venditeService;
	
	@Autowired
	RifornimentiServiceImpl(RifornimentiRepository repository, PubblicazioniService pubblicazioniService, VenditeService venditeService) {
		super(repository);
		this.repository = repository;
		this.pubblicazioniService = pubblicazioniService;
		this.venditeService = venditeService;
	}

	@Override
	public List<RichiestaClienteDto> getRichiesteClienteByIdClienteViewOnly(Integer[] codFiegDl, Integer[] codEdicolaDl, List<Long> codCliente, String titolo, String stato, Integer provenienza, Timestamp dataDa, Timestamp dataA,
			Boolean filterDataSospensionePrenotazioni) {
		return repository.getRichiesteClienteByIdClienteViewOnly(codFiegDl, codEdicolaDl, codCliente, titolo, stato, provenienza, dataDa, dataA, filterDataSospensionePrenotazioni);
	}

	@Override
	public List<RichiestaClienteDto> getRichiesteClienteByPk(String pk) {
		return repository.getRichiesteClienteByPk(pk);
	}

	@Override
	public List<RichiestaFissaClienteDto> getRichiesteClienteFisseByIdCliente(Integer[] codFiegDl, Integer[] codEdicolaDl, Long codCliente, String titolo, String stato, Timestamp dataDa, Timestamp dataA, Boolean findUltimoIdtnPrenotazioniFisse) {
		return repository.getRichiesteClienteFisseByIdCliente(codFiegDl, codEdicolaDl, codCliente, titolo, stato, dataDa, dataA, findUltimoIdtnPrenotazioniFisse);
	}

	@Override
	public List<RichiestaRifornimentoDto> getRichiesteCliente(Integer coddl, Integer[] codFiegDl, Integer[] codEdicolaDl, Integer idtn, Long codCliente, Integer provenienza) {
		return repository.getRichiesteCliente(coddl, codFiegDl, codEdicolaDl, idtn, codCliente, provenienza);
	}

	@Override
	public List<RichiestaClienteVo> getRichiesteClienteByPk(Integer[] codFiegDl, Integer[] codEdicola, Long codCliente, Integer provenienza, Set<String> pkSet) throws ParseException {
		return repository.getRichiesteClienteByPk(codFiegDl, codEdicola, codCliente, provenienza, pkSet);
	}

	@Override
	public List<RichiestaClienteDto> getRichiesteClienteByIdtn(Integer[] codFiegDl, Integer[] codEdicolaDl, Integer idtn, Integer provenienza) {
		return repository.getRichiesteClienteByIdtn(codFiegDl, codEdicolaDl, idtn, provenienza);
	}

	@Override
	public List<RichiestaRifornimentoDto> getRichiesteRifornimenti(Integer coddl, Integer[] codFiegDl, Integer[] codEdicolaDl, Integer idtn, boolean isMultiDl, Timestamp dataStorico, Integer currCodDl, Map<String,Object> params) {
		return repository.getRichiesteRifornimenti(coddl, codFiegDl, codEdicolaDl, idtn, isMultiDl, dataStorico, currCodDl, params);
	}

	@Override
	public List<RichiestaRifornimentoDto> getRichiesteRifornimenti(Integer codFiegDl, Integer codEdicolaDl, String titolo, String stato, Timestamp dataDa, Timestamp dataA, boolean isDlInforiv) {
		return repository.getRichiesteRifornimenti(codFiegDl, codEdicolaDl, titolo, stato, dataDa, dataA, isDlInforiv);
	}
	
	@Override
	public List<RichiestaRifornimentoDto> getPubblicazioniPossibiliPerRichiesteRifornimenti(Integer codDlPubb,Integer[] arrCodFiegDl, Integer[] arrCodWebEdicola, Integer idtn, boolean isMultiDl, Timestamp dataStorico, Integer currCodDl){
		return repository.getPubblicazioniPossibiliPerRichiesteRifornimenti(codDlPubb,arrCodFiegDl, arrCodWebEdicola,idtn, isMultiDl, dataStorico,currCodDl);
	}
	
	@Override
	public List<RichiestaRifornimentoDto> getPubblicazioniEsitoRichiesteRifornimenti(Integer codDlPubb,Integer[] arrCodFiegDl, Integer[] arrCodWebEdicola, Integer idtn, boolean isMultiDl, Timestamp dataStorico, Integer currCodDl){
		return repository.getPubblicazioniEsitoRichiesteRifornimenti(codDlPubb,arrCodFiegDl, arrCodWebEdicola,idtn, isMultiDl, dataStorico,currCodDl);
	}
	
	@Override
	public List<RichiestaRifornimentoVo> getRichiesteRifornimentiVo(Integer[] codFiegDl, Integer[] codEdicolaDl, Set<String> pkSet) throws ParseException {
		return repository.getRichiesteRifornimentiVo(codFiegDl, codEdicolaDl, pkSet);
	}

	@Override
	public RichiestaFissaClienteEdicolaVo getRichiestaFissaClienteVo(Integer[] codEdicolaDl, Long codCliente, Integer[] codFiegDl, Integer codicePubblicazione) {
		return repository.getRichiestaFissaClienteVo(codEdicolaDl, codCliente, codFiegDl, codicePubblicazione);
	}

	@Override
	public void saveEvasionePrenotazioniClientiEdicola(Map<Long, List<RichiestaClienteDto>> mapEvasione) {
		repository.saveEvasionePrenotazioniClientiEdicola(mapEvasione);
	}

	@Override
	public Map<Long, List<RichiestaClienteDto>> buildMapEvasione(String pk, String qtaEvasa, String qtaDaEvadere, String ultimaRisposta, String messLibero) throws ParseException {
		return repository.buildMapEvasione(pk, qtaEvasa, qtaDaEvadere, ultimaRisposta, messLibero);
	}

	@Override
	public List<RisposteClientiCodificateVo> getRisposteClientiCodificate() {
		return repository.getRisposteClientiCodificate();
	}

	@Override
	public EsportazioneDatiDlResultDto esportaRifornimenti(Integer codDl) {
		return repository.esportaRifornimenti(codDl);
	}

	@Override
	public EsportazioneDatiDlResultDto esportaAltriDati(Integer codDl) {
		return repository.esportaAltriDati(codDl);
	}
	
	@Override
	public EsportazioneDatiDlResultDto esportaTutto(Integer codDl) {
		return repository.esportaTutto(codDl);
	}

	@Override
	public List<RichiestaClienteDto> getOrdiniClienti(Integer codFiegDl, Integer codEdicola, String idtnOrdini) {
		return repository.getOrdiniClienti(codFiegDl, codEdicola, idtnOrdini);
	}

	@Override
	public List<RichiestaClienteDto> getOrdiniClienti(Integer codFiegDl, Integer codEdicola, Timestamp dataBaolla) {
		return repository.getOrdiniClienti(codFiegDl, codEdicola, dataBaolla);
	}

	@Override
	public List<RichiestaClienteDto> getOrdiniClientiNotifiche(Integer[] codFiegDl, Integer[] codEdicola) {
		return repository.getOrdiniClientiNotifiche(codFiegDl, codEdicola);
	}
	
	@Override
	public Boolean hasNotificheOrdiniClienti(Integer codFiegDl, Integer codEdicola) {
		return repository.hasNotificheOrdiniClienti(codFiegDl, codEdicola);
	}
	
	@Override
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn, Integer ordine) {
		return repository.getRichiestaRifornimento(codFiegDl, codEdicolaWeb, idtn, ordine);
	}
	
	@Override
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn) {
		return repository.getRichiestaRifornimento(codFiegDl, codEdicolaWeb, idtn);
	}
	
	@Override
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb,
			Timestamp dataInserimento, Integer idtn) {
		return repository.getRichiestaRifornimento(codFiegDl, codEdicolaWeb,dataInserimento, idtn);
	}
	
	
	@Override
	public void updateOrdiniClientiNotifiche(List<RichiestaClientePk> pks) {
		repository.updateOrdiniClientiNotifiche(pks);
	}
	
	@Override
	public RichiestaClienteVo getRichiestaClienteVo(Integer codEdicola, Long codCliente, Integer provenienza, Timestamp dataInserimento, Integer codDl, Integer idtn) {
		return repository.getRichiestaClienteVo(codEdicola, codCliente, provenienza, dataInserimento, codDl, idtn);
	}
	
	@Override
	public RichiestaFissaClienteEdicolaVo getRichiestaFissaClienteEdicolaVo(Integer codEdicola, Long codCliente, Integer codDl, Integer codicePubblicazione) {
		return repository.getRichiestaFissaClienteEdicolaVo(codEdicola, codCliente, codDl, codicePubblicazione);
	}
	
	@Override
	public List<PrenotazioneDto> getPrenotazioniFisseNonEvase(Integer codEdicola, Integer codDl, Timestamp dtBolla, String tipoBolla) {
		return repository.getPrenotazioniFisseNonEvase(codEdicola, codDl, dtBolla, tipoBolla);
	}
	
	@Override
	public Boolean hasPrenotazioniFisseNonEvase(Integer codEdicola, Integer codDl, Timestamp dtBolla, String tipoBolla) {
		return repository.hasPrenotazioniFisseNonEvase(codEdicola, codDl, dtBolla, tipoBolla);
	}
	
	@Override
	public List<RichiestaRifornimentoDto> getRichiesteRifornimento(Integer codDl, Timestamp dataOrdineIniziale, Timestamp dataOrdineFinale, Integer codRivenditaDl, Integer codPubblicazione, Integer idtn) {
		return repository.getRichiesteRifornimento(codDl, dataOrdineIniziale, dataOrdineFinale, codRivenditaDl, codPubblicazione, idtn);
	}
	
	@Override
	public List<PrenotazioneDto> getRichiesteVariazioni(Integer[] codFiegDl, Integer[] codEdicola, String titolo, Integer stato, Timestamp dataDa, Timestamp dataA, Integer codUtente) {
		return repository.getRichiesteVariazioni(codFiegDl, codEdicola, titolo, stato, dataDa, dataA, codUtente);
	}
	
	@Override
	public void saveRichiestaRifornimento(List<RichiestaRifornimentoVo> buildListRichiestaRifornimentoVo, List<RichiestaRifornimentoVo> listRichiestaRifornimentoVoAggiunte, List<RichiestaRifornimentoVo> listDeleteRichiestaRifornimentoVo, PrenotazioneVo prenotazioneVo, List<RichiestaRifornimentoLivellamentiVo> richiesteRifornimentoLivellamentiVo, List<RichiestaRifornimentoLivellamentiVo> listDeleteRichiestaRifornimentoLivellamentiVo) {
		if (!listDeleteRichiestaRifornimentoVo.isEmpty()) {
			deleteVoList(listDeleteRichiestaRifornimentoVo);
		}
		if (!buildListRichiestaRifornimentoVo.isEmpty()) {
			saveVoList(buildListRichiestaRifornimentoVo);
		}
		if (!listRichiestaRifornimentoVoAggiunte.isEmpty()) {
			saveVoList(listRichiestaRifornimentoVoAggiunte);
		}
		if (!listDeleteRichiestaRifornimentoLivellamentiVo.isEmpty()) {
			deleteVoList(listDeleteRichiestaRifornimentoLivellamentiVo);
		}
		if (!richiesteRifornimentoLivellamentiVo.isEmpty()) {
			saveVoList(richiesteRifornimentoLivellamentiVo);
		}
		if (prenotazioneVo != null) {
			saveBaseVo(prenotazioneVo);
		}
	}
	
	@Override
	public void saveRichiestaRifornimentoClienteEdicola(List<RichiestaClienteVo> listRichiestaRifornimentoVo, RichiestaFissaClienteEdicolaVo richiestaFissaClienteVo) {
		for (RichiestaClienteVo vo : (List<RichiestaClienteVo>) listRichiestaRifornimentoVo) {
			if (vo.getQuantitaRichiesta() == null || vo.getQuantitaRichiesta().equals(0)) {
				deleteVo(vo);
			} else {
				saveBaseVo(vo);
			}
		}
		if (richiestaFissaClienteVo != null) {
			if (richiestaFissaClienteVo.getQuantitaRichiesta() == null || richiestaFissaClienteVo.getQuantitaRichiesta().equals(0)) {
				deleteVo(richiestaFissaClienteVo);
			} else {
				richiestaFissaClienteVo.setIndicatoreTrasmessoDl(IGerivConstants.INDICATORE_RECORD_NON_TRASMESSO_DL);
				richiestaFissaClienteVo.setDataRichiesta(new Date(new java.util.Date().getTime()));
				saveBaseVo(richiestaFissaClienteVo);
			}
		}
	}
	
	@Override
	public RichiestaFissaClienteEdicolaVo getRichiestaFissaClienteVo(Integer coddl, Integer codEdicola, Integer[] codEdicolaDl, Long codCliente, Integer[] codFiegDl, Integer codicePubblicazione) {
		RichiestaFissaClienteEdicolaVo prenotazione = repository.getRichiestaFissaClienteVo(codEdicolaDl, codCliente, codFiegDl, codicePubblicazione);
		if (prenotazione == null) {
			prenotazione = new RichiestaFissaClienteEdicolaVo();
			RichiestaFissaClienteEdicolaPk pk = new RichiestaFissaClienteEdicolaPk();
			pk.setCodDl(coddl);
			pk.setCodEdicola(codEdicola);
			pk.setCodicePubblicazione(codicePubblicazione);
			pk.setCodCliente(codCliente);
			prenotazione.setPk(pk);
			prenotazione.setIndicatoreTrasmessoDl(IGerivConstants.INDICATORE_RECORD_NON_TRASMESSO_DL);
			prenotazione.setDataRichiesta(new Date(getSysdate().getTime()));
			prenotazione.setEnabled(true);
			prenotazione.setAnagraficaPubblicazioniVo(pubblicazioniService.getAnagraficaPubblicazioneByPk(prenotazione.getPk().getCodDl(), codicePubblicazione));
		} else if (!prenotazione.getIndicatoreTrasmessoDl().equals(IGerivConstants.INDICATORE_RECORD_NON_TRASMESSO_DL)) {
			prenotazione.setEnabled(false);
		}
		return prenotazione;
	}
	
	@Override
	public PrenotazioneVo getPrenotazione(Integer codFiegDl, Integer codEdicolaDl, Integer cpu) {
		PrenotazioneVo prenotazione = repository.getPrenotazione(codFiegDl, codEdicolaDl, cpu);
		if (prenotazione == null) {
			prenotazione = new PrenotazioneVo();
			PrenotazionePk pk = new PrenotazionePk();
			pk.setCodDl(codFiegDl);
			pk.setCodEdicola(codEdicolaDl);
			pk.setCodicePubblicazione(cpu);
			pk.setDataRichiesta(new Date(getSysdate().getTime()));
			prenotazione.setPk(pk);
			prenotazione.setIndicatoreTrasmessoDl(IGerivConstants.INDICATORE_RECORD_NON_TRASMESSO_DL);
			prenotazione.setEnabled(true);
		} else if (prenotazione.getIndicatoreTrasmessoDl().equals(IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL)) {
			prenotazione.setEnabled(false);
		} else if (prenotazione.getIndicatoreTrasmessoDl().equals(IGerivConstants.INDICATORE_RECORD_TRASMESSO_DL)) {
			prenotazione.setEnabled(true);
		}
		return prenotazione;
	}
	
	@Override
	public Long saveEvasionePrenotazioniClientiEdicola(String codUtente, Timestamp dataCompEC, Boolean saveEvasionePrenotazioneVendite, Map<Long, List<RichiestaClienteDto>> mapEvasione) throws ParseException {
		repository.saveEvasionePrenotazioniClientiEdicola(mapEvasione);
		if (saveEvasionePrenotazioneVendite) {
			return venditeService.saveContoVendite(codUtente, mapEvasione, dataCompEC);
		}
		return 0l;
	}
	
}
