package it.dpe.igeriv.jms.importer.impl;

import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.jms.importer.Importer;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.jms.dto.NullJmsMessage;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mromano
 *
 */
@Component("TERImporter")
public class TerminationImporter implements Importer<NullJmsMessage> {
	private final Logger log = Logger.getLogger(getClass());
	private final JmsService jmsService;
	private final EdicoleService edicoleService;
	
	@Autowired
	TerminationImporter(JmsService jmsService, EdicoleService edicoleService) {
		this.jmsService = jmsService;
		this.edicoleService = edicoleService;
	}
	
	@Override
	public void importa(List<NullJmsMessage> list, Integer codFornitore) {
		log.info("Entered method TerminationImporter.importa() with parameters list size = " + (list != null ? list.size() : 0) + ", codFornitore= " + codFornitore);
		List<EdicolaDto> listEdicole = jmsService.getEdicoleAttive(codFornitore);
		for (EdicolaDto edicola : listEdicole) {
			AbbinamentoEdicolaDlVo abb = edicoleService.getAbbinamentoEdicolaDlVoByCodEdicolaWeb(edicola.getCodEdicolaWeb());
			abb.setAggiornataProdottiVariDl(true);
			edicoleService.saveBaseVo(abb);
		}
	}


}
