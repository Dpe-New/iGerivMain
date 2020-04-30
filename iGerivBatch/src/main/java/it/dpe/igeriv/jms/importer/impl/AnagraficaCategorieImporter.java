package it.dpe.igeriv.jms.importer.impl;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.jms.importer.Importer;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaEdicolaVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiCategoriaEdicolaPk;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiSottoCategoriaEdicolaPk;
import it.dpe.jms.dto.CategorieJmsMessage;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mromano
 *
 */
@Component("ACTImporter")
public class AnagraficaCategorieImporter implements Importer<CategorieJmsMessage> {
	private static final String PREFIX = "<b>(DL)</b>&nbsp;";
	private final Logger log = Logger.getLogger(getClass());
	private final JmsService jmsService;
	
	@Autowired
	AnagraficaCategorieImporter(JmsService jmsService) {
		this.jmsService = jmsService;
	}

	@Override
	public void importa(List<CategorieJmsMessage> list, Integer codFornitore) {
		log.info("Entered method AnagraficaCategorieImporter.importa() with parameters list size = " + (list != null ? list.size() : 0) + ", codFornitore= " + codFornitore);
		List<EdicolaDto> listEdicole = jmsService.getEdicoleAttive(codFornitore);
		for (EdicolaDto edicola : listEdicole) {
			Integer codEdicola = edicola.getCodEdicolaWeb();
			for (CategorieJmsMessage message : list) {
				Integer codCategoria = message.getDescrizioneLivello1();
				Integer codSottocategoria = message.getDescrizioneLivello2();
				String descrizione = message.getDescrizione();
				if (codCategoria != null && codSottocategoria != null) {
					insertOrUpdateCategoria(codEdicola, codCategoria, descrizione);
					updateOrInsertSottoCategoria(codEdicola, codCategoria, codSottocategoria, descrizione);
				} else if (codCategoria != null && codSottocategoria == null) {
					insertOrUpdateCategoria(codEdicola, codCategoria, descrizione);
					updateOrInsertSottoCategoria(codEdicola, codCategoria, codCategoria, descrizione);
				}
			}
		}
	}

	private void insertOrUpdateCategoria(Integer codEdicola, Integer codCategoria, String descrizione) {
		ProdottiNonEditorialiCategoriaEdicolaVo vo = jmsService.getCategoriaEdicola(new Long(codCategoria), codEdicola);
		if (vo == null) {
			vo = new ProdottiNonEditorialiCategoriaEdicolaVo();
			ProdottiNonEditorialiCategoriaEdicolaPk pk = new ProdottiNonEditorialiCategoriaEdicolaPk();
			pk.setCodCategoria(new Long(codCategoria));
			pk.setCodEdicola(codEdicola);
			vo.setPk(pk);
		}
		vo.setDescrizione(PREFIX + descrizione);
		jmsService.saveVo(vo);
	}

	private void updateOrInsertSottoCategoria(Integer codEdicola, Integer codCategoria, Integer codSottocategoria, String descrizione) {
		ProdottiNonEditorialiSottoCategoriaEdicolaVo vo = jmsService.getSottoCategoriaEdicola(new Long(codCategoria), new Long(codSottocategoria), codEdicola);
		if (vo == null) {
			vo = new ProdottiNonEditorialiSottoCategoriaEdicolaVo();
			ProdottiNonEditorialiSottoCategoriaEdicolaPk pk = new ProdottiNonEditorialiSottoCategoriaEdicolaPk();
			pk.setCodCategoria(new Long(codCategoria));
			pk.setCodSottoCategoria(new Long(codSottocategoria));
			pk.setCodEdicola(codEdicola);
			vo.setPk(pk);
		}
		vo.setDescrizione(PREFIX + descrizione);
		jmsService.saveVo(vo);
	}

}
