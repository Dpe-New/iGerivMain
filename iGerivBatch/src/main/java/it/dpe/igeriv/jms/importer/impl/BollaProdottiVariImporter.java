package it.dpe.igeriv.jms.importer.impl;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.Matchers.equalTo;
import it.dpe.igeriv.exception.ProdottoNonEsistenteExcepton;
import it.dpe.igeriv.jms.importer.Importer;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCausaliMagazzinoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiBollaDettaglioPk;
import it.dpe.jms.dto.BollaProdottiVariDettaglioJmsMessage;
import it.dpe.jms.dto.BollaProdottiVariJmsMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mromano
 *
 */
@Component("BPVImporter")
public class BollaProdottiVariImporter implements Importer<BollaProdottiVariJmsMessage> {
	private final Logger log = Logger.getLogger(getClass());
	private final JmsService jmsService;
	private ProdottiNonEditorialiCausaliMagazzinoVo causaleAcquisto;
	
	@Autowired
	BollaProdottiVariImporter(JmsService jmsService) {
		this.jmsService = jmsService;
	}
	
	@Override
	public void importa(List<BollaProdottiVariJmsMessage> list, Integer codFornitore) {
		log.info("Entered method BollaProdottiVariImporter.importa() with parameters list size = " + (list != null ? list.size() : 0) + ", codFornitore= " + codFornitore);
		causaleAcquisto = selectUnique(jmsService.getListCausaliMagazzino(), having(on(ProdottiNonEditorialiCausaliMagazzinoVo.class).getCodiceCausale(), equalTo(IGerivConstants.CODICE_CAUSALE_ACQUISTO)));
		for (BollaProdottiVariJmsMessage message : list) {
			AnagraficaEdicolaVo edicola = jmsService.getAnagraficaEdicola(message.getCodiceFornitore(), message.getCodEdicolaDl());
			if (edicola != null) {
				try {
					ProdottiNonEditorialiBollaVo vo = jmsService.getProdottiNonEditorialiBolla(message.getIdDocumento());
					if (vo == null) {
						vo = new ProdottiNonEditorialiBollaVo();
						vo.setCausale(jmsService.getCausaleMagazzino(IGerivConstants.CODICE_CAUSALE_ACQUISTO));
						vo.setCodiceFornitore(message.getCodiceFornitore());
						vo.setDataDocumento(message.getDataDocumento());
						vo.setDataRegistrazione(jmsService.getSysdate());
						List<ProdottiNonEditorialiBollaDettaglioVo> dettagli = buildDettagliBolla(message.getDettagli(), edicola.getCodEdicola(), codFornitore);
						vo.setDettagli(dettagli);
						vo.setEdicola(edicola);
						vo.setIdDocumento(message.getIdDocumento());
						vo.setIndicatoreEmessoRicevuto(IGerivConstants.INDICATORE_DOCUMENTO_RICEVUTO);
						vo.setNumeroDocumento(message.getNumeroDocumento());
						vo.setNumeroOrdine(message.getNumeroOrdine());
						vo.setDeleted(false);
						jmsService.saveVo(vo);
					}
				} catch (Throwable e) {
					log.error("Error in BollaProdottiVariImporter.importa()", e);
					throw e;
				}
			}
		}
	}

	/**
	 * @param dettagli
	 * @param listCausali 
	 * @param codEdicola 
	 * @param codFornitore 
	 * @return
	 * @throws ProdottoNonEsistenteExcepton 
	 */
	private List<ProdottiNonEditorialiBollaDettaglioVo> buildDettagliBolla(List<BollaProdottiVariDettaglioJmsMessage> dettagli, Integer codEdicola, Integer codFornitore) throws ProdottoNonEsistenteExcepton {
		List<ProdottiNonEditorialiBollaDettaglioVo> list = new ArrayList<ProdottiNonEditorialiBollaDettaglioVo>();
		int prog = 1;
		for (BollaProdottiVariDettaglioJmsMessage det : dettagli) {
			ProdottiNonEditorialiBollaDettaglioVo vo = new ProdottiNonEditorialiBollaDettaglioVo();
			vo.setCausale(causaleAcquisto);
			vo.setCodiceProdottoFornitore(det.getCodiceProdottoFornitore());
			vo.setMagazzinoA(IGerivConstants.COD_MAGAZZINO_INTERNO);
			vo.setMagazzinoDa(IGerivConstants.COD_MAGAZZINO_ESTERNO);
			ProdottiNonEditorialiBollaDettaglioPk pk = new ProdottiNonEditorialiBollaDettaglioPk();
			pk.setIdDocumento(det.getIdDocumento());
			pk.setProgressivo(prog++);
			vo.setPk(pk);
			vo.setPrezzo(det.getPrezzo());
			ProdottiNonEditorialiPrezziAcquistoVo prodotto = jmsService.getProdottiNonEditorialeByCodFornitore(codEdicola, codFornitore, det.getCodiceProdottoFornitore());
			if (prodotto == null) {
				throw new ProdottoNonEsistenteExcepton(MessageFormat.format(IGerivMessageBundle.get("igeriv.prodotti.vari.error.prodotto.non.esistente"), det.getCodiceProdottoFornitore(), codFornitore));
			}
			vo.setProdotto(prodotto.getProdotto());
			vo.setQuantita(det.getQuantita());
			vo.setScontoPercentuale(det.getScontoPercentuale());
			vo.setScontoValore(det.getScontoValore());
			list.add(vo);
		}
		return list;
	}


}
