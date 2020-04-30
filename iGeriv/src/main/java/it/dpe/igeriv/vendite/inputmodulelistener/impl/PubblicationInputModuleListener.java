package it.dpe.igeriv.vendite.inputmodulelistener.impl;

import static ch.lambdaj.Lambda.forEach;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dto.ProdottoDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.VenditeCardResultDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.vendite.inputmodulelistener.VenditeInputModuleListener;

/**
 * Classe per la gestione dell'evento di tipo ricerca per titolo pubblicazione.
 * 
 * @author romanom
 *
 */
@Component("PubblicationInputModuleListener")
public class PubblicationInputModuleListener implements VenditeInputModuleListener {
	private final ProdottiService prodottiService;
	private final PubblicazioniService pubblicazioniService;
	private final BolleService bolleService;
	
	@Autowired
	PubblicationInputModuleListener(ProdottiService prodottiService, PubblicazioniService pubblicazioniService, BolleService bolleService) {
		this.prodottiService = prodottiService;
		this.pubblicazioniService = pubblicazioniService;
		this.bolleService = bolleService;
	}
	
    @Override
	public VenditeCardResultDto execute(VenditeParamDto params) throws IGerivBusinessException {
    	String inputText = params.getInputText();
    	Integer[] codDl = params.getArrCodFiegDl();
        Integer[] idEdicola = params.getArrIdEdicola();
        Integer quantita = params.getQuantita();
        Boolean mostraTutteUscite = params.getMostraTutteUscite();
        Boolean ricercaProdottiVari = params.getRicercaProdottiVari();
        Integer gruppoSconto = params.getGruppoSconto();
        Boolean soloPubblicazioniBarcodeNullo = params.getSoloPubblicazioniBarcodeNullo();
        Boolean findCopieInContoDeposito = params.getFindCopieInContoDeposito();
        Boolean findGiacenza = params.getFindGiacenza();
        Timestamp dataStorico = params.getDataStorico();
        Boolean dlInforiv = params.getDlInforiv();
    	VenditeCardResultDto result = new VenditeCardResultDto();
		List<PubblicazioneDto> copertine = null;
		if (ricercaProdottiVari) {
			copertine = buildCopertineFromProdottiNonEditoriali(prodottiService.getProdottiVariByDescrizioneBarcode(idEdicola[0], inputText, null));
		} else if (!Strings.isNullOrEmpty(params.getDataBolla()) && !Strings.isNullOrEmpty(params.getTipoBolla())) {
			try {
				copertine = bolleService.getPubblicazioniInBolla(codDl, idEdicola, DateUtilities.parseDate(params.getDataBolla(), DateUtilities.FORMATO_DATA), params.getTipoBolla(), inputText);
			} catch (ParseException e) {
				throw new IGerivBusinessException(IGerivMessageBundle.get("gp.errore.imprevisto"));
			}
		} else {
			//Ticket 0000264
			if(params.getHarModalitaRicercaContenuto() && inputText!=null && !inputText.equals(""))
				inputText = "%"+inputText+"%";
			
			copertine = pubblicazioniService.getCopertineByTitoloBarcodeCpu(codDl, idEdicola, inputText, null, !mostraTutteUscite, soloPubblicazioniBarcodeNullo, null, gruppoSconto, findCopieInContoDeposito, findGiacenza, dataStorico, dlInforiv);
			if (!copertine.isEmpty()) {
				forEach(copertine).setProdottoNonEditoriale(false);
			}
		}
        if (copertine == null || copertine.isEmpty()) {
            String message = (ricercaProdottiVari) ? IGerivMessageBundle.get("label.pubblication_input_module_listener.Product_Not_Found") : IGerivMessageBundle.get("label.pubblication_input_module_listener.Pubblication_Not_Found");
            if (!Strings.isNullOrEmpty(params.getDataBolla()) && !Strings.isNullOrEmpty(params.getTipoBolla())) {
            	message = MessageFormat.format(IGerivMessageBundle.get("label.pubblication_input_module_listener.Pubblication_Not_Found_Bolla"), params.getDataBolla(), params.getTipoBolla());
            }
			throw new IGerivBusinessException(message);
        }
        forEach(copertine).setQuantita(quantita);
        result.setResult(copertine);
		result.setType(VenditeCardResultDto.ResultType.VENDITE_TITOLO);
        return result;
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
			list.add(dto);
		}
		return list;
	}
	
}
