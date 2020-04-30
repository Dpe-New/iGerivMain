package it.dpe.igeriv.vendite.inputmodulelistener.impl;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.card.CardService;
import it.dpe.igeriv.dto.IGerivCardResultDto;
import it.dpe.igeriv.dto.VenditaDettaglioDto;
import it.dpe.igeriv.dto.VenditeCardResultDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.TesseraNonAbilitataBusinessException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.vendite.inputmodulelistener.VenditeInputModuleListener;
import it.dpe.igeriv.vo.IGerivCardVo;

/**
 * Classe per la gestione dell'evento di tipo card igeriv.
 * 
 * @author romanom
 *
 */
@Component("IGerivCardInputModuleListener")
public class IGerivCardInputModuleListener implements VenditeInputModuleListener {
	private final CardService cardService;
    private final String giorniStoricoAcquistiIgerivCard;
    
	@Autowired
	IGerivCardInputModuleListener(CardService cardService, @Value("${igeriv.giorni.storico.acquisti.igeriv.card}") String giorniStoricoAcquistiIgerivCard) {
		this.cardService = cardService;
		this.giorniStoricoAcquistiIgerivCard = giorniStoricoAcquistiIgerivCard;
	}
	
    @Override
    public VenditeCardResultDto execute(VenditeParamDto params) throws IGerivBusinessException {
    	String inputText = params.getInputText();
    	Integer codDl = params.getCodFiegDl();
        Integer idEdicola = params.getIdEdicola();
        VenditeCardResultDto result = new VenditeCardResultDto();
        // TODO EDICOLA TEST
        if (idEdicola == 949) {
	    	IGerivCardVo card = cardService.getIGerivCardVo(inputText, idEdicola, null);
	    	if (card == null) {
	    		String message = MessageFormat.format(IGerivMessageBundle.get("label.pubblication_input_module_listener.IGeriv_Card_Not_Active"), new Object[]{inputText});
				throw new TesseraNonAbilitataBusinessException(message);
	    	}
	    	IGerivCardResultDto dto = new IGerivCardResultDto();
	    	List<VenditaDettaglioDto> ultimiAcquisti = cardService.getUltimiAcquistiIGerivCard(inputText, idEdicola, codDl, new Integer(giorniStoricoAcquistiIgerivCard), card.getCliente().getCodCliente(), params.getDataStorico());
	    	List<VenditaDettaglioDto> suggerimentiVendita = cardService.getSuggerimentiVendita(inputText, idEdicola);
	    	dto.setUltimiAcquisti(ultimiAcquisti);
			dto.setSuggerimentiVendita(suggerimentiVendita);
			dto.setContoNome(MessageFormat.format(IGerivMessageBundle.get("igeriv.conto.del.cliente.tessera.o.nome"), !NumberUtils.isNumber(card.getCliente().getNome()) ? card.getCliente().getNome() + " " + card.getCliente().getCognome() : IGerivMessageBundle.get("igeriv.tessera.numero") + " " + card.getPk().getCard()));
			dto.setCodCliente(card.getCodCliente());
	        result.setResult(dto);
			result.setType(VenditeCardResultDto.ResultType.VENDITE_IGERIV_CARD);
        }
        return result;
    }
    
}
