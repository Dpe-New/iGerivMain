package it.dpe.igeriv.jms.importer.impl;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.jms.importer.Importer;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.igeriv.vo.LocalitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiFornitoreVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiFornitorePk;
import it.dpe.jms.dto.ProdottiNonEditorialiFornitoreJmsMessage;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mromano
 *
 */
@Component("AFPImporter")
public class AnagraficaFornitoriImporter implements Importer<ProdottiNonEditorialiFornitoreJmsMessage> {
	private final Logger log = Logger.getLogger(getClass());
	private final JmsService jmsService;
	
	@Autowired
	AnagraficaFornitoriImporter(JmsService jmsService) {
		this.jmsService = jmsService;
	}

	@Override
	public void importa(List<ProdottiNonEditorialiFornitoreJmsMessage> list, Integer codFornitore) {
		log.info("Entered method AnagraficaFornitoriImporter.importa() with parameters list size = " + (list != null ? list.size() : 0) + ", codFornitore= " + codFornitore);
		List<EdicolaDto> listEdicole = jmsService.getEdicoleAttive(codFornitore);
		for (EdicolaDto edicola : listEdicole) {
			Integer codEdicola = edicola.getCodEdicolaWeb();
			for (ProdottiNonEditorialiFornitoreJmsMessage message : list) {
				ProdottiNonEditorialiFornitoreVo vo = jmsService.getFornitore(codEdicola, codFornitore);
				if (vo == null) {
					vo = new ProdottiNonEditorialiFornitoreVo();
					ProdottiNonEditorialiFornitorePk pk = new ProdottiNonEditorialiFornitorePk();
					pk.setCodEdicola(codEdicola);
					pk.setCodFornitore(codFornitore);
					vo.setPk(pk);
				}
				vo.setNome(message.getRagioneSociale1());
				vo.setCognome(message.getRagioneSociale2());
				vo.setIndirizzo(message.getIndirizzo());
				vo.setCap(message.getCap());
				vo.setCodiceFiscale(message.getCodiceFiscale());
				LocalitaVo localita = jmsService.getLocalita(message.getLocalita());
				vo.setLocalita(localita);
				vo.setPiva(message.getPiva());
				ProvinciaVo provincia = jmsService.getProvincia(message.getProvincia());
				vo.setProvincia(provincia);
				vo.setTelefono(message.getTelefono());
				jmsService.saveVo(vo);
			}
		}
	}

}
