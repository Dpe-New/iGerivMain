package it.dpe.igeriv.vendite.inputmodulelistener.impl;

import static ch.lambdaj.Lambda.forEach;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.statistiche.StatisticheService;
import it.dpe.igeriv.dto.ProdottoDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.VenditeCardResultDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.exception.IGerivAssociateBarcodeBusinessException;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.IGerivSendRequestAssociateBarcodeBusinessException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.vendite.inputmodulelistener.VenditeInputModuleListener;

/**
 * Classe per la gestione dell'evento di tipo ricerca pubblicazione per barcode.
 * 
 * @author romanom
 *
 */
@Component("BarcodeInputModuleListener")
public class BarcodeInputModuleListener implements VenditeInputModuleListener {
	private final ProdottiService prodottiService;
	private final PubblicazioniService pubblicazioniService;
	
	private final StatisticheService statisticheService;
    private final IGerivUtils iGerivUtils;
    
    @Autowired
	BarcodeInputModuleListener(ProdottiService prodottiService, PubblicazioniService pubblicazioniService, IGerivUtils iGerivUtils, StatisticheService statisticheService) {
		this.prodottiService = prodottiService;
		this.pubblicazioniService = pubblicazioniService;
		this.iGerivUtils = iGerivUtils;
		this.statisticheService= statisticheService;
	}
	
    @Override
    public VenditeCardResultDto execute(VenditeParamDto params) throws IGerivBusinessException {
    	String inputText = params.getInputText();
    	Integer[] codDl = params.getArrCodFiegDl();
        Integer[] idEdicola = params.getArrIdEdicola();
        Integer quantita = params.getQuantita();
        Integer gruppoSconto = params.getGruppoSconto();
        Boolean abilitataCorrezioneBarcode = params.getAbilitataCorrezioneBarcode();
        Boolean hasEdicoleAutorizzateAggiornaBarcode = params.getHasEdicoleAutorizzateAggiornaBarcode();
        Boolean findCopieInContoDeposito = params.getFindCopieInContoDeposito();
        Boolean findGiacenza = params.getFindGiacenza();
        Timestamp dataStorico = params.getDataStorico();
        Boolean dlInforiv = params.getDlInforiv();
        Boolean hasRichiestaRifornimentoNelleVendite = params.getHasRichiestaRifornimentoNelleVendite();
    	VenditeCardResultDto result = new VenditeCardResultDto();
		List<PubblicazioneDto> copertine = new ArrayList<PubblicazioneDto>();
		copertine.addAll(buildCopertineFromProdottiNonEditoriali(prodottiService.getProdottiVariByDescrizioneBarcode(params.getIdEdicola(), null, inputText)));
		List<PubblicazioneDto> copertineByTitoloBarcode = pubblicazioniService.getCopertineByTitoloBarcodeCpu(codDl, idEdicola, null, inputText, false, false, null, gruppoSconto, findCopieInContoDeposito, findGiacenza, dataStorico, dlInforiv);
		if (!copertineByTitoloBarcode.isEmpty()) {
			forEach(copertineByTitoloBarcode).setProdottoNonEditoriale(false);
		}
		copertine.addAll(copertineByTitoloBarcode);
		
		//PubblicazioneDto copertina = (copertine != null && copertine.size() > 0) ? copertine.get(0) : null;
		PubblicazioneDto copertina = null;
		if(copertine!=null && copertine.size()>1){
			//ticket 0000342
			//Gestione multi pubblicazioni per lo stesso Barcode ( Menta 19/06/2015 )
			//Ricercare per ogni pubblicazione il fornito all'interno dell'edicola ( CodiceDl, Crivw, IdtnPubbli)
			for (PubblicazioneDto pubblicazioneDto : copertine) {
	        	int fornito = pubblicazioniService.getFornito(pubblicazioneDto.getCoddl(), params.isMultiDl() ? iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(pubblicazioneDto.getCoddl(), codDl, idEdicola) : idEdicola[0], pubblicazioneDto.getIdtn()).intValue();
	        	if(fornito>0){
	        		copertina = pubblicazioneDto;
	        		break;
	        	}
			}
			if(copertina==null){
				copertina = copertine.get(0);
			}
		}else if(copertine!=null && copertine.size()==1){
			copertina = copertine.get(0);
		}else{
			copertina = null;
		}
		 
		if (copertina == null) {
        	if (abilitataCorrezioneBarcode) {
        		throw new IGerivAssociateBarcodeBusinessException(MessageFormat.format(IGerivMessageBundle.get("label.barcode_input_module_listener.Pubblication_Not_Found.Associate.Barcode"), inputText));
        	} else if (hasEdicoleAutorizzateAggiornaBarcode) {
        		throw new IGerivSendRequestAssociateBarcodeBusinessException(IGerivMessageBundle.get("label.barcode_input_module_listener.Pubblication_Not_Found") + "<br>" + IGerivMessageBundle.get("label.barcode_input_module_listener.Pubblication_Not_Found.Send_Request.Associate.Barcode"));
        	} else {
        		throw new IGerivBusinessException(IGerivMessageBundle.get("label.barcode_input_module_listener.Pubblication_Not_Found"));
        	}
        }
        if (!copertina.getProdottoNonEditoriale()) {
        	int giac = pubblicazioniService.getGiacenza(copertina.getCoddl(), params.isMultiDl() ? iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(copertina.getCoddl(), codDl, idEdicola) : idEdicola[0], copertina.getIdtn(), params.getDataStorico()).intValue();
        	copertina.setGiacenzaIniziale(giac);
        	if (hasRichiestaRifornimentoNelleVendite && copertina.getDataUscita() != null && copertina.getNumGiorniDaDataUscitaPerRichiestaRifornimento() > 0) {
        		Date dataLimiteRichiesteRifo = DateUtilities.ceilDay(DateUtilities.aggiungiGiorni(copertina.getDataUscita(), copertina.getNumGiorniDaDataUscitaPerRichiestaRifornimento()));
        		if (pubblicazioniService.getSysdate().before(dataLimiteRichiesteRifo)) {
        			PubblicazioneDto pubblicazioneDto = pubblicazioniService.getCopertinaByIdtn(copertina.getCoddl(), copertina.getIdtn());
        			if (params.getVenditeEsauritoControlloGiacenzaDL() && pubblicazioneDto.getGiancezaPressoDl() <= 0) {
        				copertina.setRichiedereRifornimenti(false);
        				copertina.setPuoRichiedereRifornimenti(false);
        			}
        			else {
        				copertina.setRichiedereRifornimenti(giac <= 1);
        				copertina.setPuoRichiedereRifornimenti(true);
        			}
        		}
        	}
		}
        copertina.setQuantita(quantita);
        result.setResult(copertina);
		result.setType(VenditeCardResultDto.ResultType.VENDITE_BARCODE);
        return result;
    }
    
    
    private Timestamp getDataStorico() {
		Calendar cal = Calendar.getInstance();
		return new Timestamp(cal.getTimeInMillis());
	}
    
    
    
	/**
	 * Costruisce una lista di PubblicazioneDto dai prodotti non editoriali
	 * 
	 * @param List<ProdottoDto> prodottiVari
	 * @return List<PubblicazioneDto>
	 */
	private List<PubblicazioneDto> buildCopertineFromProdottiNonEditoriali(List<ProdottoDto> prodottiVari) {
		List<PubblicazioneDto> list = new ArrayList<PubblicazioneDto>();
		for (ProdottoDto pdto : prodottiVari) {
			PubblicazioneDto dto = new PubblicazioneDto();
			dto.setTitolo(pdto.getDescrizione());
			dto.setSottoTitolo(pdto.getDescrizioneB());
			dto.setNumeroCopertina(pdto.getCodProdottoEsterno());
			dto.setPrezzoCopertina(new BigDecimal(pdto.getPrezzo()));
			dto.setDataUscita(null);
			dto.setIdtn(pdto.getCodProdottoInterno().intValue());
			dto.setIdProdotto(pdto.getCodProdottoInterno());
			dto.setProdottoNonEditoriale(true);
			dto.setAliquota(pdto.getAliquota());
			dto.setBarcode(pdto.getBarcode());
			list.add(dto);
		}
		return list;
	}
	
}
