package it.dpe.igeriv.jms.importer.impl;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.jms.importer.Importer;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiPrezziAcquistoPk;
import it.dpe.jms.dto.ProdottiJmsMessage;
import it.dpe.mail.MailingListService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author mromano
 *
 */
@Component("APRImporter")
public class AnagraficaProdottiVariImporter implements Importer<ProdottiJmsMessage> {
	private final Logger log = Logger.getLogger(getClass());
	private final JmsService jmsService;
	private final MailingListService mailingListService;
	private final String pathImgProdVariDlTmp;
	private final String pathImgProdVariEdicola;
	
	@Autowired
	AnagraficaProdottiVariImporter(JmsService jmsService, MailingListService mailingListService, @Value("${img.tmp.prodotti.vari.dl.path.dir}") String pathImgProdVariDlTmp, @Value("${img.miniature.edicola.prodotti.vari.path.dir}") String pathImgProdVariEdicola) {
		this.jmsService = jmsService;
		this.mailingListService = mailingListService;
		this.pathImgProdVariDlTmp = pathImgProdVariDlTmp;
		this.pathImgProdVariEdicola = pathImgProdVariEdicola;
	}

	@Override
	public void importa(List<ProdottiJmsMessage> list, Integer codFornitore) {
		log.info("Entered method AnagraficaProdottiVariImporter.importa() with parameters list size = " + (list != null ? list.size() : 0) + ", codFornitore= " + codFornitore);
		List<EdicolaDto> listEdicole = jmsService.getEdicoleAttive(codFornitore);
		boolean firstEdicola = true;
		for (EdicolaDto edicola : listEdicole) {
			Integer codEdicola = edicola.getCodEdicolaWeb();
			for (ProdottiJmsMessage message : list) {
				Long codiceProdottoFornitore = message.getCodProdottoInterno();
				String nomeImmagine = null;
				if (message.getImmagine() != null) {
					nomeImmagine = codFornitore + "_" + codiceProdottoFornitore + ".tmp";
					if (firstEdicola) {
						writeImageToDisk(codFornitore, message, codiceProdottoFornitore, nomeImmagine);
					}
				}
				ProdottiNonEditorialiPrezziAcquistoVo prodottoPrezzo = jmsService.getProdottiNonEditorialiPrezziAcquistoVo(codEdicola, codFornitore, codiceProdottoFornitore.toString());
				ProdottiNonEditorialiVo vo = null;
				if (prodottoPrezzo == null) {
					vo = new ProdottiNonEditorialiVo();
					Long codProdottoInterno = jmsService.getNextSeqVal(IGerivConstants.SEQ_PRODOTTO_NON_EDITORIALE);
					vo.setCodProdottoInterno(codProdottoInterno);
					AnagraficaEdicolaVo ed = new AnagraficaEdicolaVo();
					ed.setCodEdicola(codEdicola);
					vo.setEdicola(ed);
					prodottoPrezzo = new ProdottiNonEditorialiPrezziAcquistoVo(); 
					ProdottiNonEditorialiPrezziAcquistoPk pk = new ProdottiNonEditorialiPrezziAcquistoPk();
					pk.setCodEdicola(codEdicola);
					pk.setCodProdotto(codProdottoInterno);
					pk.setCodiceProdottoFornitore(codiceProdottoFornitore.toString());
					prodottoPrezzo.setPk(pk);
					prodottoPrezzo.setCodiceFornitore(codFornitore);
					prodottoPrezzo.setUltimoPrezzoAcquisto(0f);
				} else {
					vo = prodottoPrezzo.getProdotto();
				}
				vo.setAliquota(message.getAliquota());
				vo.setBarcode(message.getBarcode());
				vo.setCodCategoria(new Long(message.getCodCategoria()));
				//vo.setCodProdottoEsterno(codProdottoEsterno);
				vo.setCodSottoCategoria(new Long(message.getCodSottoCategoria()));
				vo.setDescrizioneProdottoA(message.getDescrizioneProdottoA());
				vo.setDescrizioneProdottoB(message.getDescrizioneProdottoB());
				vo.setUnitaMisura(message.getUnitaMisura());
				vo.setNomeImmagine(nomeImmagine != null ? nomeImmagine.replaceAll(".tmp", ".jpg") : null);
				vo.setProdottoDl(true);
				vo.setUnitaMinimaIncremento(message.getUnitaMinimaIncremento());
				vo.setObsoleto(message.isObsoleto());
				vo.setFormazionePacco(message.getFormazionePacco());
				vo.setPercentualeResaSuDistribuito(message.getPercentualeResaSuDistribuito());
				vo.setPrezzo(vo.getPrezzo() == null ? message.getPrezzoVenditaConsigliato() : vo.getPrezzo());
				jmsService.saveVo(vo);
				jmsService.saveVo(prodottoPrezzo);
			}
			firstEdicola = false;
		}
	}

	/**
	 * Scrive l'immagine nella cartella "temp" delle immagini prodotti vari del dl
	 * in modo da azionare il listener di importazione delle immagini 
	 * 
	 * @param codFornitore
	 * @param message
	 * @param codiceProdottoFornitore
	 * @param nomeImmagine
	 */
	private void writeImageToDisk(Integer codFornitore, ProdottiJmsMessage message, Long codiceProdottoFornitore, String nomeImmagine) {
		InputStream is = new ByteArrayInputStream(message.getImmagine());
		File imgDl = new File(pathImgProdVariDlTmp + System.getProperty("file.separator") + nomeImmagine);
		try {
			FileUtils.buildFileFromInputStream(is, imgDl);
			File dest = new File(pathImgProdVariDlTmp + System.getProperty("file.separator") + nomeImmagine.replace(".tmp", ".jpg"));
			File imgEdicola = new File(pathImgProdVariEdicola + System.getProperty("file.separator") + dest.getName());
			FileUtils.buildFileFromInputStream(is, imgEdicola);
			imgDl.renameTo(dest);
		} catch (IOException e) {
			mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.subject.errore.importazione.immagini"), 
					MessageFormat.format(IGerivMessageBundle.get("msg.errore.importazione.immagini"), new Object[]{imgDl.getName(), imgDl.getParentFile().getAbsolutePath()}) + StringUtility.getStackTrace(e), true);
		}
	}

}
