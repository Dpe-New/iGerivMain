package it.dpe.igeriv.bo.bolle;

import static ch.lambdaj.Lambda.forEach;
import it.dpe.igeriv.bo.BaseServiceImpl;
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
import it.dpe.igeriv.util.IGerivConstants;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("BolleService")
class BolleServiceImpl extends BaseServiceImpl implements BolleService {
	private final BolleRepository repository;
	
	@Autowired
	BolleServiceImpl(BolleRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<BollaDettaglioDto> getDettaglioBolla(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dataBolla, String tipoBolla, boolean showSoloRigheSpuntare, boolean isMultiDl) {
		return repository.getDettaglioBolla(codFiegDl, codEdicolaDl, dataBolla, tipoBolla, showSoloRigheSpuntare, isMultiDl);
	}

	@Override
	public List<FondoBollaDettaglioDto> getDettagliFondoBolla(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dataBolla, String tipoBolla, boolean showSoloRigheSpuntare, boolean showSoloRifornimenti, boolean isMultiDl) {
		return repository.getDettagliFondoBolla(codFiegDl, codEdicolaDl, dataBolla, tipoBolla, showSoloRigheSpuntare, showSoloRifornimenti, isMultiDl);
	}
	
	@Override
	public List<FondoBollaDettaglioDto> getDettagliFondoBollaPubblicazioni(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dataBolla, String tipoBolla, boolean showSoloRigheSpuntare, boolean showSoloRifornimenti, boolean isMultiDl) {
		return repository.getDettagliFondoBollaPubblicazioni(codFiegDl, codEdicolaDl, dataBolla, tipoBolla, showSoloRigheSpuntare, showSoloRifornimenti, isMultiDl);
	}

	@Override
	public List<BollaRiassuntoVo> getBolleRiassunto(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dataBolla) {
		return repository.getBolleRiassunto(codFiegDl, codEdicolaDl, dataBolla);
	}

	@Override
	public List<BollaRiassuntoVo> getBolleRiassunto(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dataBolla, String tipoBolla, Integer statoBolla, Boolean excludeSospese) {
		return repository.getBolleRiassunto(codFiegDl, codEdicolaDl, dataBolla, tipoBolla, statoBolla, excludeSospese);
	}

	@Override
	public Set<BollaResaRiassuntoVo> getBolleResaRiassunto(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dataBolla) {
		return repository.getBolleResaRiassunto(codFiegDl, codEdicolaDl, dataBolla);
	}

	@Override
	public List<BollaResaRiassuntoVo> getBolleResaRiassunto(Integer codFiegDl, Integer codEdicolaDl, Timestamp dataBolla, String tipoBolla, Integer statoBolla, Boolean excludeSospese) {
		return repository.getBolleResaRiassunto(codFiegDl, codEdicolaDl, dataBolla, tipoBolla, statoBolla, excludeSospese);
	}

	@Override
	public List<ResaRiscontrataDto> getBolleResaRiassuntoConResaRiscontrata(Integer codFiegDl, Integer codEdicolaDl) {
		return repository.getBolleResaRiassuntoConResaRiscontrata(codFiegDl, codEdicolaDl);
	}

	@Override
	public List<LavorazioneResaVo> getBolleResaRiassuntoLavorazioneResa(Integer codFiegDl, Integer codEdicolaDl) {
		return repository.getBolleResaRiassuntoLavorazioneResa(codFiegDl, codEdicolaDl);
	}

	@Override
	public List<BollaVoDto> getBollaVoSonoInoltreUscite(Integer codFiegDl, Integer codEdicolaDl, Timestamp dataBolla, String tipoBolla) {
		return repository.getBollaVoSonoInoltreUscite(codFiegDl, codEdicolaDl, dataBolla, tipoBolla);
	}

	@Override
	public List<BollaResaDettaglioDto> getDettaglioBollaResa(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipo, Boolean soloResoDaInserire,Boolean soloResoConGiacenza) {
		return repository.getDettaglioBollaResa(codFiegDl, codEdicola, dtBolla, tipo, soloResoDaInserire, soloResoConGiacenza);
	}

	@Override
	public List<BollaResaDettaglioDto> getDettaglioBollaResaCDLCeste(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipo, Boolean soloResoDaInserire,Boolean soloResoConGiacenza, String cesta){
		return repository.getDettaglioBollaResaCDLCeste(codFiegDl, codEdicola, dtBolla, tipo, soloResoDaInserire, soloResoConGiacenza,cesta);
	}
	
	@Override
	public List<Integer> getCpusResaDimeticataNotInBollaResa(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipo) {
		return repository.getCpusResaDimeticataNotInBollaResa(codFiegDl, codEdicola, dtBolla, tipo);
	}

	@Override
	public List<BollaResaFuoriVoceDettaglioDto> getDettaglioFuoriVoce(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipo, Boolean soloResoDaInserire) {
		return repository.getDettaglioFuoriVoce(codFiegDl, codEdicola, dtBolla, tipo, soloResoDaInserire);
	}

	@Override
	public List<BollaResaRichiamoPersonalizzatoDettaglioDto> getDettaglioRichiamoPersonalizzato(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipo, Boolean soloResoDaInserire) {
		return repository.getDettaglioRichiamoPersonalizzato(codFiegDl, codEdicola, dtBolla, tipo, soloResoDaInserire);
	}

	@Override
	public BollaRiassuntoVo getBollaRiassunto(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipo) {
		return repository.getBollaRiassunto(codFiegDl, codEdicola, dtBolla, tipo);
	}

	@Override
	public List<BollaResaRiassuntoVo> getBollaResaRiassunto(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipo) {
		return repository.getBollaResaRiassunto(codFiegDl, codEdicola, dtBolla, tipo);
	}

	@Override
	public List<BollaResaFuoriVoceVo> buildNuoviDettagliFuoriVoce(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipo, Integer cpu, Integer gruppoSconto, boolean showNumeriOmogenei, Timestamp dataStorico,String tipoResaNoContoDeposito, Boolean accettoCD) {
		return repository.buildNuoviDettagliFuoriVoce(codFiegDl, codEdicola, dtBolla, tipo, cpu, gruppoSconto, showNumeriOmogenei, dataStorico, tipoResaNoContoDeposito, accettoCD);
	}

	@Override
	public Integer findUltimaPosizioneRigaBolla(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipo) {
		return repository.findUltimaPosizioneRigaBolla(codFiegDl, codEdicola, dtBolla, tipo);
	}

	@Override
	public void saveSpuntaBollaRivendita(String differenze, String spunte, String pk, String differenzeFondoBolla, String spunteFondoBolla, String pkFondoBolla, Integer codEdicola){
		repository.saveSpuntaBollaRivendita(differenze, spunte, pk, differenzeFondoBolla, spunteFondoBolla, pkFondoBolla, codEdicola);
	}
	
	@Override
	public void saveSpuntaBollaRivendita(String differenze, String spunte, String pk, String differenzeFondoBolla, String spunteFondoBolla, String pkFondoBolla, Integer codEdicola, LinkedHashMap<String, BollaRiassuntoVo> listBolleDistinct) {
		
		if(listBolleDistinct.size()>0){
			for(String key:listBolleDistinct.keySet()){
				BollaRiassuntoVo bollaRiassunto =  listBolleDistinct.get(key);
				if(bollaRiassunto!=null){
					bollaRiassunto.setDtTrasmissione(getSysdate());
					saveBaseVo(bollaRiassunto);
				}
			}
		}
		
		repository.saveSpuntaBollaRivendita(differenze, spunte, pk, differenzeFondoBolla, spunteFondoBolla, pkFondoBolla, codEdicola);
	}
	
	@Override
	public void saveBollaRivendita(Integer codEdicola, Map<String, String> fields, Map<String, String> spunte, Map<String, String> fieldsFB, Map<String, String> spunteFB) {
		repository.saveBollaRivendita(codEdicola, fields, spunte, fieldsFB, spunteFB);
	}
		
	
	@Override
	public void saveBollaRivendita(Integer codEdicola, Map<String, String> fields, Map<String, String> spunte, Map<String, String> fieldsFB, Map<String, String> spunteFB, BollaRiassuntoVo bollaRiassunto) {
		// 30/09/2014 errore 
		if (bollaRiassunto != null ) {
			bollaRiassunto.setDtTrasmissione(getSysdate());
			saveBaseVo(bollaRiassunto);
		}
		repository.saveBollaRivendita(codEdicola, fields, spunte, fieldsFB, spunteFB);
	}

	@Override
	public void saveBollaResa(Map<String, String> reso, Map<String, String> resoFuoriVoce, Map<String, String> resoRichiamo) {
		repository.saveBollaResa(reso, resoFuoriVoce, resoRichiamo);
	}
	
	@Override
	public void saveBollaResa(Map<String, String> reso, Map<String, String> resoFuoriVoce, Map<String, String> resoRichiamo,List<BollaResaRiassuntoVo> bollaResaRiassunto) {
		
		if (bollaResaRiassunto != null && !bollaResaRiassunto.isEmpty()) {
			//30/12/2014 0000136 Commento il set della data di trasmissione in fase di salvataggio
			//forEach(bollaResaRiassunto).setDtTrasmissione(getSysdate());
			saveVoList(bollaResaRiassunto);
		}
		repository.saveBollaResa(reso, resoFuoriVoce, resoRichiamo);
	}
	
	
	
	@Override
	public void saveBolleRiassunto(String stato, String pk) {
		repository.saveBolleRiassunto(stato, pk);
	}

	@Override
	public void saveBolleResaRiassunto(String stato, String pk) {
		repository.saveBolleResaRiassunto(stato, pk);
	}

	@Override
	public LavorazioneResaVo getLavorazioneResaVo(String zipFile) {
		return repository.getLavorazioneResaVo(zipFile);
	}

	@Override
	public LavorazioneResaVo getLavorazioneResaVo(Integer codFiegDl, Integer codEdicolaDl, Timestamp dtBolla, String tipoBolla) {
		return repository.getLavorazioneResaVo(codFiegDl, codEdicolaDl, dtBolla, tipoBolla);
	}

	@Override
	public List<LavorazioneResaImmagineDto> getListLavorazioneResaImmagineVo(String nomeFile) {
		return repository.getListLavorazioneResaImmagineVo(nomeFile);
	}
	
	@Override
	public List<QuadraturaResaDto> getQuadraturaResa(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla) {
		return repository.getQuadraturaResa(codFiegDl, codEdicola, dtBolla, tipoBolla);
	}
	
	@Override
	public Boolean cpuExistsInBolla(Integer codFiegDl, Timestamp dtBolla, String tipoBolla, Integer cpu) {
		return repository.cpuExistsInBolla(codFiegDl, dtBolla, tipoBolla, cpu);
	}
	
	@Override
	public BollaResaDettaglioVo getBollaResaDettaglioVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		return repository.getBollaResaDettaglioVo(codFiegDl, codEdicola, dataBolla, tipoBolla, idtn);
	}
	
	@Override
	public BollaResaVo getBollaResaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer numeroRiga) {
		return repository.getBollaResaVo(codFiegDl, dataBolla, tipoBolla, numeroRiga);
	}
	
	@Override
	public DecodificaRichiamiResaVo getRichiamoResa(Integer codFiegDl, Integer tipoRichiamoResa) {
		return repository.getRichiamoResa(codFiegDl, tipoRichiamoResa);
	}
	
	@Override
	public BollaStatisticaStoricoVo getBollaStatisticaStoricoVo(Integer codFiegDl, Integer codEdicola, Integer idtn) {
		return repository.getBollaStatisticaStoricoVo(codFiegDl, codEdicola, idtn);
	}
	
	@Override
	public ResaRiscontrataVo getResaRiscontrataVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		return repository.getResaRiscontrataVo(codFiegDl, codEdicola, dataBolla, tipoBolla, idtn);
	}
	
	@Override
	public Integer getLastRigaBolla(Integer codFiegDl, Timestamp dataBolla, String tipoBolla) {
		return repository.getLastRigaBolla(codFiegDl, dataBolla, tipoBolla);
	}
	
	@Override
	public BollaDettaglioVo getBollaDettaglioVo(String pk, Integer idtn) throws ParseException {
		return repository.getBollaDettaglioVo(pk, idtn);
	}
	
	@Override
	public void deleteFondoBollaEdicolaInforiv(Integer codFiegDl, Integer codEdicola, Timestamp dataBolla, String tipoBolla) {
		repository.deleteFondoBollaEdicolaInforiv(codFiegDl, codEdicola, dataBolla, tipoBolla);
	}
	
	@Override
	public BollaVo getBollaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer posizioneRiga, Integer idtn) {
		return repository.getBollaVo(codFiegDl, dataBolla, tipoBolla, posizioneRiga, idtn);
	}
	
	@Override
	public BollaDettaglioVo getDettaglioBolla(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer posizioneRiga) {
		return repository.getDettaglioBolla(codFiegDl, codEdicola, dataBolla, tipoBolla, posizioneRiga);
	}
	
	@Override
	public Integer getLastPosizioneRigaBolla(Integer codFiegDl, Integer codEdicola, Timestamp dataBolla, String tipoBolla) {
		return repository.getLastPosizioneRigaBolla(codFiegDl, codEdicola, dataBolla, tipoBolla);
	}
	
	@Override
	public Boolean allowResaDimenticata(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla, Integer cpu, Integer numMaxCpuResaDimenticata,String tipoResaNoRifornimentoDimenticato, Integer giornoSettimanaPermesso) {
		
		if(tipoResaNoRifornimentoDimenticato!=null && tipoBolla.equals(tipoResaNoRifornimentoDimenticato)){
			return true;
		}
		if (giornoSettimanaPermesso != null && giornoSettimanaPermesso >= 1 && giornoSettimanaPermesso <= 7) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dtBolla);
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (giornoSettimanaPermesso != day) {
				return false;
			}
		}
		
		Boolean cpuExistsInBolla = cpuExistsInBolla(codFiegDl, dtBolla, tipoBolla, cpu);
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(getCpusResaDimeticataNotInBollaResa(codFiegDl, codEdicola, dtBolla, tipoBolla));
		if (!cpuExistsInBolla) {
			set.add(cpu);
		}
		set.add(cpu);
		return set.size() <= numMaxCpuResaDimenticata || cpuExistsInBolla;
	}
	
	@Override
	public void saveAndSendBollaResa(Map<String, String> reso, Map<String, String> resoFuoriVoce, Map<String, String> resoRichiamo, List<BollaResaRiassuntoVo> bollaResaRiassunto) {
		if (bollaResaRiassunto != null && !bollaResaRiassunto.isEmpty()) {
			forEach(bollaResaRiassunto).setBollaTrasmessaDl(IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL);
			forEach(bollaResaRiassunto).setDtTrasmissione(getSysdate());
			// 23/10/2015 Gestione della data di memorizzazione e invio bolla
			forEach(bollaResaRiassunto).setDtMemorizzazioneInvio(getSysdate());
			saveVoList(bollaResaRiassunto);
		}
		saveBollaResa(reso, resoFuoriVoce, resoRichiamo);
	}
	
	@Override
	public void saveAndSendBollaRivendita(Integer codEdicola, Map<String, String> fields, Map<String, String> spunte, Map<String, String> fieldsFB, Map<String, String> spunteFB, BollaRiassuntoVo bollaRiassunto) {
    	
		bollaRiassunto.setBollaTrasmessaDl(IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL);
    	bollaRiassunto.setDtTrasmissione(getSysdate());
		saveBaseVo(bollaRiassunto);
		saveBollaRivendita(codEdicola, fields, spunte, fieldsFB, spunteFB);
	}
	
	@Override
	public void saveAndSendSpuntaBollaRivendita(String differenze,
			String spunte, String pk, String differenzeFondoBolla,
			String spunteFondoBolla, String pkFondoBolla, Integer codEdicola,
			LinkedHashMap<String, BollaRiassuntoVo> listBolleDistinct) {
		
		if(listBolleDistinct.size()>0){
			for(String key:listBolleDistinct.keySet()){
				BollaRiassuntoVo bollaRiassunto =  listBolleDistinct.get(key);
				if(bollaRiassunto!=null){
					bollaRiassunto.setBollaTrasmessaDl(IGerivConstants.INDICATORE_RECORD_IN_TRASMISSIONE_DL);
			    	bollaRiassunto.setDtTrasmissione(getSysdate());
					saveBaseVo(bollaRiassunto);
				}
			}
		}
		
		saveSpuntaBollaRivendita(differenze, spunte, pk, differenzeFondoBolla, spunteFondoBolla, pkFondoBolla, codEdicola);
	}
	
	
	@Override
	public List<String> getTipiBollaConsegna(Integer codFiegDl, Timestamp dataBolla) {
		return repository.getTipiBollaConsegna(codFiegDl, dataBolla);
	}
	
	@Override
	public List<String> getTipiBollaResa(Integer codFiegDl, Timestamp dataBolla) {
		return repository.getTipiBollaResa(codFiegDl, dataBolla);
	}
	
	@Override
	public List<PubblicazioneDto> getPubblicazioniInBolla(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dtBolla, String tipoBolla, String titolo) {
		return repository.getPubblicazioniInBolla(codFiegDl, codEdicola, dtBolla, tipoBolla, titolo);
	}
	
	@Override
	public Integer getProgressivoIdtnBollaConsegna(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla, Integer idtn) {
		return repository.getProgressivoIdtnBollaConsegna(codFiegDl, codEdicola, dtBolla, tipoBolla, idtn);
	}
	
	@Override
	public Integer getProgressivoIdtnBollaResa(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla, Integer idtn) {
		return repository.getProgressivoIdtnBollaResa(codFiegDl, codEdicola, dtBolla, tipoBolla, idtn);
	}
	
	@Override
	public Boolean isPubblicazioneInBolleConsegnaTipo(Integer codFiegDl, Integer cpu, String tipoBolla) {
		return repository.isPubblicazioneInBolleConsegnaTipo(codFiegDl, cpu, tipoBolla);
	}
	
	@Override
	public List<BollaRiassuntoDto> getBolleRiassuntoNonInviate(Timestamp[] dateBolla, String tipo, Integer codFiegDl) {
		return repository.getBolleRiassuntoNonInviate(dateBolla, tipo, codFiegDl);
	}
	
	@Override
	public List<BollaRiassuntoDto> getBolleResaRiassuntoNonInviate(Timestamp[] dateBolla, String tipo, Integer codFiegDl) {
		return repository.getBolleResaRiassuntoNonInviate(dateBolla, tipo, codFiegDl);
	}
	
	@Override
	public BollaResaRichiamoPersonalizzatoVo getBollaResaRichiamoPersonalizzato(Integer codFiegDl, Integer codEdicola, Timestamp dtBolla, String tipoBolla, Integer progressivo) {
		return repository.getBollaResaRichiamoPersonalizzato(codFiegDl, codEdicola, dtBolla, tipoBolla, progressivo);
	}

	@Override
	public List<BollaRiassuntoDto> getListaBolle(Integer codFiegDl, Integer maxResult) {
		return repository.getListaBolle(codFiegDl, maxResult);
	}
	
	@Override
	public List<BollaVo> getDettaglioBolla(Integer codFiegDl,String tipoBolla,Timestamp dataBolla) {
		return repository.getDettaglioBolla(codFiegDl, tipoBolla, dataBolla);
	}
	
	@Override
	public void updateNonUscite(String pk, String selectedDataTipoBolla, Integer codFiegDl) {	
		repository.updateNonUscite(pk, selectedDataTipoBolla, codFiegDl);
	}	
	
	/**
	 * @param codFiegDl 
	 * @param cpu 
	 * @param vo
	 * @return
	 */
	@Override
	public boolean isPeriodicitaPubblicazioneRifornimentoCorretta(Integer periodicita, String tipoBolla, boolean hasBolleQuotidianiPeriodiciDivise, Integer codFiegDl, Integer cpu) {
		boolean isPerCorretta = true;
		
		if(hasBolleQuotidianiPeriodiciDivise)
		{
			if (tipoBolla.equals(IGerivConstants.TIPO_BOLLA_QUOTIDIANI) || tipoBolla.equals(IGerivConstants.TIPO_BOLLA_PERIODICI)) {
				if ((tipoBolla.equals(IGerivConstants.TIPO_BOLLA_QUOTIDIANI) && !periodicita.equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO))
						|| (tipoBolla.equals(IGerivConstants.TIPO_BOLLA_PERIODICI) && periodicita.equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO))) {
					if (!isPubblicazioneInBolleConsegnaTipo(codFiegDl, cpu, tipoBolla)) {
						isPerCorretta = false;
					}
				}
			}
		}
		return isPerCorretta;
	}

	
}
