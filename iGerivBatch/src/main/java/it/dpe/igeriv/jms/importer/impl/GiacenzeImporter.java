package it.dpe.igeriv.jms.importer.impl;

import it.dpe.igeriv.jms.importer.Importer;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.ProdottiNonEditorialiGiacenzeVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiGiacenzePk;
import it.dpe.jms.dto.GiacenzaJmsMessage;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mromano
 *
 */
@Component("GIAImporter")
public class GiacenzeImporter implements Importer<GiacenzaJmsMessage> {
	private final Logger log = Logger.getLogger(getClass());
	private final JmsService jmsService;
	
	@Autowired
	GiacenzeImporter(JmsService jmsService) {
		this.jmsService = jmsService;
	}

	@Override
	public void importa(List<GiacenzaJmsMessage> list, Integer codFornitore) {
		log.info("Entered method GiacenzeImporter.importa() with parameters list size = " + (list != null ? list.size() : 0) + ", codFornitore= " + codFornitore);
		for (GiacenzaJmsMessage message : list) {
			Long codProdottoFornitore = message.getCodProdottoInterno();
			ProdottiNonEditorialiGiacenzeVo vo = jmsService.getGiacenza(codFornitore, codProdottoFornitore.toString());
			if (vo == null) {
				vo = new ProdottiNonEditorialiGiacenzeVo();
				ProdottiNonEditorialiGiacenzePk pk = new ProdottiNonEditorialiGiacenzePk();
				pk.setCodDl(codFornitore);
				pk.setCodProdottoFornitore(message.getCodProdottoInterno().toString());
				pk.setNumeroMagazzino(IGerivConstants.COD_MAGAZZINO_INTERNO);
				vo.setPk(pk);
			}
			vo.setGiacenza(message.getGiacenza());
			jmsService.saveVo(vo);
		}
	}

}
