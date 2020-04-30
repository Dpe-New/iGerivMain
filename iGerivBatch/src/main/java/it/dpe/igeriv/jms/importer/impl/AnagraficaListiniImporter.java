package it.dpe.igeriv.jms.importer.impl;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.jms.importer.Importer;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.jms.dto.ListinoJmsMessage;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mromano
 *
 */
@Component("ALIImporter")
public class AnagraficaListiniImporter implements Importer<ListinoJmsMessage> {
	private final Logger log = Logger.getLogger(getClass());
	private final JmsService jmsService;
	
	@Autowired
	AnagraficaListiniImporter(JmsService jmsService) {
		this.jmsService = jmsService;
	}
	
	@Override
	public void importa(List<ListinoJmsMessage> list, Integer codFornitore) {
		log.info("Entered method AnagraficaListiniImporter.importa() with parameters list size = " + (list != null ? list.size() : 0) + ", codFornitore= " + codFornitore);
		List<EdicolaDto> listEdicole = jmsService.getEdicoleAttive(codFornitore);
		for (EdicolaDto edicola : listEdicole) {
			Integer codEdicola = edicola.getCodEdicolaWeb();
			Long codiceContabileCliente = edicola.getCodiceContabileCliente();
			List<ListinoJmsMessage> list1 = select(list, having(on(ListinoJmsMessage.class).getCodiceCliente(), equalTo(0l)).or(having(on(ListinoJmsMessage.class).getCodiceCliente(), equalTo(codiceContabileCliente))));
			for (ListinoJmsMessage message : list1) {
				Long codiceProdottoFornitore = message.getCodProdottoInterno();
				ProdottiNonEditorialiPrezziAcquistoVo vo = jmsService.getProdottiNonEditorialiPrezziAcquistoVo(codEdicola, codFornitore, codiceProdottoFornitore.toString());
				if (vo != null) {
					Float ultimoPrezzoAcquisto = message.getPrezzoLisitino() - message.getScontoValore() - (message.getPrezzoLisitino() * message.getScontoValore() / 100);
					vo.setUltimoPrezzoAcquisto(ultimoPrezzoAcquisto);
					jmsService.saveVo(vo);
				}
			}
		}
	}

}
