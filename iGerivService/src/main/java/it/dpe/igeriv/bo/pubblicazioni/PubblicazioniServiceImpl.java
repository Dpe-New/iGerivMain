package it.dpe.igeriv.bo.pubblicazioni;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.AnagraficaEditoreDto;
import it.dpe.igeriv.dto.ContoDepositoDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.PubblicazionePiuVendutaDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.AnagraficaEditoreDlVo;
import it.dpe.igeriv.vo.AnagraficaPubblicazioniVo;
import it.dpe.igeriv.vo.ArgomentoVo;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PubblicazioniService")
class PubblicazioniServiceImpl extends BaseServiceImpl implements PubblicazioniService {
	private final PubblicazioniRepository repository;
	
	@Autowired
	PubblicazioniServiceImpl(PubblicazioniRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public PubblicazioneDto getCopertinaByIdtn(Integer codDl, Integer idtn) {
		return repository.getCopertinaByIdtn(codDl, idtn);
	}

	@Override
	public PubblicazioneDto getCopertinaByIdtn(Integer[] codDl, Integer[] codEdicola, Integer idtn) {
		return repository.getCopertinaByIdtn(codDl, codEdicola, idtn);
	}

	@Override
	public PubblicazioneDto getCopertinaByIdtn(Integer[] codDl, Integer[] codEdicola, Integer idtnInt, boolean showPubblicazioniTuttiDl, Boolean dlInforiv) {
		return repository.getCopertinaByIdtn(codDl, codEdicola, idtnInt, showPubblicazioniTuttiDl, dlInforiv);
	}
	
	@Override
	public PubblicazioneDto getCopertinaByCpuNum(final Integer[] codDl, final Integer[] codEdicola, final Integer cpu, final Integer cddt, final String num, final Boolean multiDl) { 
		return repository.getCopertinaByCpuNum(codDl, codEdicola, cpu, cddt, num, multiDl);
	}

	@Override
	public List<RichiestaRifornimentoDto> getCopertineByCodPubblicazione(Integer[] codDl, Integer[] codEdicola, Integer codicePubblicazione, Integer codiceInizioQuotidiano, Integer codiceFineQuotidiano, Integer numCopertinePrecedentiPerRifornimenti,
			Timestamp dataStorico, Timestamp dataUscitaLimite, boolean isMultiDl, Integer currCodDl, Integer agenziaFatturazione, Map<String,Object> params) {
		return repository.getCopertineByCodPubblicazione(codDl, codEdicola, codicePubblicazione, codiceInizioQuotidiano, codiceFineQuotidiano, numCopertinePrecedentiPerRifornimenti, dataStorico, dataUscitaLimite, isMultiDl, currCodDl, agenziaFatturazione, params);
	}
	
	@Override
	public Long getGiacenza(Integer codFiegDl, Integer codEdicola, Integer idtn, Timestamp dataStorico) {
		return repository.getGiacenza(codFiegDl, codEdicola, idtn, dataStorico);
	}

	@Override
	public Long getFornito(Integer codFiegDl, Integer codEdicola, Integer idtn) {
		return repository.getFornito(codFiegDl, codEdicola, idtn);
	}
	
	@Override
	public ContoDepositoVo getPubblicazioneInContoDeposito(Integer codFiegDl, Integer codEdicola, Integer idtn) {
		return repository.getPubblicazioneInContoDeposito(codFiegDl, codEdicola, idtn);
	}
	
	@Override
	public List<ContoDepositoDto> getPubblicazioniContoDeposito(Integer[] codFiegDl, Integer[] codEdicola, Integer gruppoSconto, String titolo, String codBarre) {
		return repository.getPubblicazioniContoDeposito(codFiegDl, codEdicola, gruppoSconto, titolo, codBarre);
	}

	@Override
	public List<PubblicazioneDto> getCopertine(boolean ultimaPubblicazione, boolean statistiche, boolean contoDeposito, Integer codEdicolaMaster, Integer[] codDl, Integer[] codEdicola, String titolo, String sottotitolo, String argomento,
			String periodicita, BigDecimal prezzo, Integer codPubblicazione, String codBarre, boolean showOnlyUltimoDistribuito, Timestamp dataStorico, Integer gruppoSconto, boolean showPubblicazioniTuttiDl, Integer currDlMultiDl, Integer anagEditori, Integer agenziaFatturazione, Boolean isSecondaCintura, Timestamp dataPartSecCintura) {
		return repository.getCopertine(ultimaPubblicazione, statistiche, contoDeposito, codEdicolaMaster, codDl, codEdicola, titolo, sottotitolo, argomento, periodicita, prezzo, codPubblicazione, codBarre, showOnlyUltimoDistribuito, dataStorico, gruppoSconto, showPubblicazioniTuttiDl, currDlMultiDl, anagEditori, agenziaFatturazione, isSecondaCintura, dataPartSecCintura);
	}
	
	@Override
	public List<PubblicazioneDto> getCopertineByDL(boolean ultimaPubblicazione,Integer codDl, String titolo, String sottotitolo, String argomento, String periodicita, BigDecimal prezzo, Integer codPubblicazione, String codBarre){
		return repository.getCopertineByDL(ultimaPubblicazione,codDl, titolo, sottotitolo, argomento, periodicita, prezzo, codPubblicazione, codBarre);
	}
	

	@Override
	public List<PubblicazioneDto> getCopertineByTitoloBarcodeCpu(Integer[] codDl, Integer[] codEdicola, String titolo, String codBarre, boolean soloUltimeCopertine, boolean soloPubblicazioniBarcodeNullo, Integer cpu, Integer gruppoSconto,
			Boolean findCopieInContoDeposito, Boolean findGiacenza, Timestamp dataStorico, Boolean dlInforiv) {
		return repository.getCopertineByTitoloBarcodeCpu(codDl, codEdicola, titolo, codBarre, soloUltimeCopertine, soloPubblicazioniBarcodeNullo, cpu, gruppoSconto, findCopieInContoDeposito, findGiacenza, dataStorico, dlInforiv);
	}

	@Override
	public List<PubblicazioneDto> getPubblicazioniByTitoloCpu(Integer codDl, String titolo, Integer cpu) {
		return repository.getPubblicazioniByTitoloCpu(codDl, titolo, cpu);
	}

	@Override
	public AnagraficaPubblicazioniVo getAnagraficaPubblicazioneByPk(Integer codFiegDl, Integer codPubblicazioneInt) {
		return repository.getAnagraficaPubblicazioneByPk(codFiegDl, codPubblicazioneInt);
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getQuotidianoByTitolo(String titolo) {
		return repository.getQuotidianoByTitolo(titolo);
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getPeriodicoByTitolo(String titolo) {
		return repository.getPeriodicoByTitolo(titolo);
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getListAnagraficaPubblicazioneByCodQuotidiano(Integer codFiegDl, Integer codInizioQuotidiano, Integer codFineQuotidiano) {
		return repository.getListAnagraficaPubblicazioneByCodQuotidiano(codFiegDl, codInizioQuotidiano, codFineQuotidiano);
	}

	@Override
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniPiuVendute(Integer codFiegDl, Integer codEdicola, Integer tipoPubblicazione, Integer periodo) {
		return repository.getListPubblicazioniPiuVendute(codFiegDl, codEdicola, tipoPubblicazione, periodo);
	}

	@Override
	public PubblicazionePiuVendutaDto getPubblicazionePiuVendutaByCpu(Integer[] codFiegDl, Integer[] codEdicola, Integer cpu) {
		return repository.getPubblicazionePiuVendutaByCpu(codFiegDl, codEdicola, cpu);
	}

	@Override
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniBarraSceltaRapidaSinistra(Integer[] codFiegDl, Integer[] codEdicola) {
		return repository.getListPubblicazioniBarraSceltaRapidaSinistra(codFiegDl, codEdicola);
	}

	@Override
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniBarraSceltaRapidaSinistra(Integer[] codFiegDl, Integer[] codEdicola, boolean barcodeUltimaCopertina) {
		return repository.getListPubblicazioniBarraSceltaRapidaSinistra(codFiegDl, codEdicola, barcodeUltimaCopertina);
	}

	@Override
	public List<PubblicazionePiuVendutaDto> getListPubblicazioniBarraSceltaRapidaDestra(Integer[] codFiegDl, Integer[] codEdicola, boolean barcodeUltimaCopertina) {
		return repository.getListPubblicazioniBarraSceltaRapidaDestra(codFiegDl, codEdicola, barcodeUltimaCopertina);
	}

	@Override
	public List<ArgomentoVo> getArgomenti(Integer codDl) {
		return repository.getArgomenti(codDl);
	}
	
	@Override
	public Map<Integer, List<ArgomentoVo>> getMapArgomentiDl() {
		return repository.getMapArgomentiDl();
	}
	
	@Override
	public List<PeriodicitaVo> getPeriodicita() {
		return repository.getPeriodicita();
	}
	
	@Override
	public Integer getCpuByBarcode(Integer codFiegDl, String barcode) {
		return repository.getCpuByBarcode(codFiegDl, barcode);
	}
	
	@Override
	public ImmaginePubblicazioneVo getImmaginePubblicazione(String barcode) {
		return repository.getImmaginePubblicazione(barcode);
	}
	
	@Override
	public PeriodicitaTrascodificaInforeteVo getPeriodicitaTrascodificaInforete(Integer codPeriodicitaInforete) {
		return repository.getPeriodicitaTrascodificaInforete(codPeriodicitaInforete);
	}
	
	@Override
	public PeriodicitaVo getPeriodicita(Integer tipoOperazione, Integer codPeriodicitaEnciclopedia) {
		return repository.getPeriodicita(tipoOperazione, codPeriodicitaEnciclopedia);
	}
	
	@Override
	public List<PubblicazioneDto> getPubblicazioniInvendute(Integer[] arrCodFiegDl, Integer[] arrCodEdicola, String titolo, Timestamp dataDa, Timestamp dataA, Integer gruppoSconto, Boolean escludiQuotidiani, Boolean escludiCD, Integer baseCalcolo, Integer page, Integer maxRows) {
		return repository.getPubblicazioniInvendute(arrCodFiegDl, arrCodEdicola, titolo, dataDa, dataA, gruppoSconto, escludiQuotidiani, escludiCD, baseCalcolo, page, maxRows);
	}
	
	@Override
	public Integer getCountPubblicazioniInvendute(Integer[] arrCodFiegDl, Integer[] arrCodEdicola, String titolo, Timestamp dataDa, Timestamp dataA, Integer gruppoSconto, Boolean escludiQuotidiani, Boolean escludiCD, Integer baseCalcolo) {
		return repository.getCountPubblicazioniInvendute(arrCodFiegDl, arrCodEdicola, titolo, dataDa, dataA, gruppoSconto, escludiQuotidiani, escludiCD, baseCalcolo);
	}
	
	@Override
	public AbbinamentoIdtnInforivVo getAbbinamentoIdtnInforiv(Integer codFiegDl, String idtni) {
		return repository.getAbbinamentoIdtnInforiv(codFiegDl, idtni);
	}
	
	@Override
	public StoricoCopertineVo getStoricoCopertinaByBarcode(Integer coddl, String barcode) {
		return repository.getStoricoCopertinaByBarcode(coddl, barcode);
	}
	
	@Override
	public StoricoCopertineVo getStoricoCopertinaByBarcode(Integer[] coddl, String barcode){
		return repository.getStoricoCopertinaByBarcode(coddl, barcode);
	}
	
	
	
	@Override
	public StoricoCopertineVo getStoricoCopertinaByPk(Integer codFiegDl, Integer idtn) {
		return repository.getStoricoCopertinaByPk(codFiegDl, idtn);
	}
	
	@Override
	public PubblicazioneDto getPubblicazioneConPrezzoEdicola(Integer[] codEdicola, Integer codFiegDl, Integer idtn, Integer gruppoSconto) {
		return repository.getPubblicazioneConPrezzoEdicola(codEdicola, codFiegDl, idtn, gruppoSconto);
	}
	
	@Override
	public List<StoricoCopertineVo> getStoricoCopertineByCpu(Integer codDl, Integer cpu) {
		return repository.getStoricoCopertineByCpu(codDl, cpu);
	}
	
	@Override
	public StoricoCopertineVo getLastStoricoCopertina(Integer codDl, Integer cpu) {
		return repository.getLastStoricoCopertina(codDl, cpu);
	}
	
	@Override
	public PubblicazioneDto getLastPubblicazioneDto(Integer codDl, Integer cpu) {
		return repository.getLastPubblicazioneDto(codDl, cpu);
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getQuotidiani(Integer codDl, Integer cpuInizio, Integer cpuFine) {
		return repository.getQuotidiani(codDl, cpuInizio, cpuFine);
	}
	
	@Override
	public void updateBarcodeTodisConDevietti(Integer days) {
		repository.updateBarcodeTodisConDevietti(days);
	}
	
	@Override
	public PubblicazioneDto getQuotidianoByDataUscita(Integer coddl, Integer cpuDa, Integer cpuA, Timestamp dataUscita) {
		return repository.getQuotidianoByDataUscita(coddl, cpuDa, cpuA, dataUscita);
	}
	
	@Override
	public Set<Integer> getCpuBarreSceltaRapidaPubblicazioni(Integer[] codFiegDl, Integer[] codEdicola) {
		return repository.getCpuBarreSceltaRapidaPubblicazioni(codFiegDl, codEdicola);
	}
	
	@Override
	public List<PubblicazioneDto> getCopertineUsciteData(Timestamp dataUscita, Integer codFiegDl) {
		return repository.getCopertineUsciteData(dataUscita, codFiegDl);
	}
	
	@Override
	public boolean isCodDlPubblicazioneCorretto(Integer coddl, List<PubblicazioneDto> listPubb)
	{
		for(PubblicazioneDto dto:listPubb)
		{
			if(dto.getCoddl().compareTo(coddl) != 0)
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public List<AnagraficaEditoreDto> getListAnagraficaEditori(Integer[] coddl){
		return repository.getListAnagraficaEditori(coddl);
	}
	
}
