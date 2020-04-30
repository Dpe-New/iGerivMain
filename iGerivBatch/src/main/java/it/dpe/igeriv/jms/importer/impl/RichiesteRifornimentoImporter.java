package it.dpe.igeriv.jms.importer.impl;

import it.dpe.igeriv.jms.importer.Importer;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.jms.dto.RichiestaRifornimentoJmsMessage;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mromano
 *
 */
@Component("RRCImporter")
public class RichiesteRifornimentoImporter implements Importer<RichiestaRifornimentoJmsMessage> {
	private final Logger log = Logger.getLogger(getClass());
	private final JmsService jmsService;
	
	@Autowired
	RichiesteRifornimentoImporter(JmsService jmsService) {
		this.jmsService = jmsService;
	}
	
	@Override
	public void importa(List<RichiestaRifornimentoJmsMessage> list, Integer codFornitore) {
		log.info("Entered method RichiesteRifornimentoImporter.importa() with parameters list size = " + (list != null ? list.size() : 0) + ", codFornitore= " + codFornitore);
		for (RichiestaRifornimentoJmsMessage message : list) {
			String stato = message.getStatoOrdine().equals(0) ? IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INSERITO : ((message.getStatoOrdine().equals(1) ? IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_EVASA_PARZIALMENTE : IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_EVASA));
			Integer quatitaEvasa = message.getQuantitaEvasa();
			Long codRichiestaRifornimento = new Long(message.getNumeroDocumento());
			Long codProdottoInterno = message.getCodProdottoInterno();
			String note = message.getNote();
			jmsService.updateRichiestaRifornimentoEvasa(codRichiestaRifornimento, codProdottoInterno, stato, quatitaEvasa, note);
		}
	}

}
