package it.dpe.igeriv.vendite.inputmodulelistener.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.VenditeCardResultDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.vendite.inputmodulelistener.VenditeInputModuleListener;

/**
 * Classe per la gestione dell'evento di tipo vendita a prezzo.
 * 
 * @author romanom
 */
@Component("PriceInputModuleListener")
public class PriceInputModuleListener implements VenditeInputModuleListener {

	@Override
	public VenditeCardResultDto execute(VenditeParamDto params)
			throws IGerivBusinessException {
    	VenditeCardResultDto result = new VenditeCardResultDto();
    	PubblicazioneDto dto = new PubblicazioneDto();
    	String inputText = params.getInputText();
    	Integer quantita = params.getQuantita();
    	dto.setTitolo(IGerivMessageBundle.get("label.various.print.Value_Sell_Label"));
    	dto.setSottoTitolo("");
    	dto.setNumeroCopertina("");
    	dto.setPrezzoCopertina(new BigDecimal(inputText)); 
    	dto.setQuantita(quantita);
        result.setResult(dto);
		result.setType(VenditeCardResultDto.ResultType.VENDITE_PREZZO);
        return result;
	}
	

}
