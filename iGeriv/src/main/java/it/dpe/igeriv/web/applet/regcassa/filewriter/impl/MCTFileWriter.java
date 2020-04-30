package it.dpe.igeriv.web.applet.regcassa.filewriter.impl;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import it.dpe.igeriv.web.applet.regcassa.dto.RegCassaDatiDto;
import it.dpe.igeriv.web.applet.regcassa.dto.RegCassaDto;
import it.dpe.igeriv.web.applet.regcassa.filewriter.FileWriter;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaUtils;

/**
 * Classe che scrive il file scontrino per il reg. cassa MCT - Flash
 * 
 * @version 11/10/2017
 */
public class MCTFileWriter extends FileWriter {
	private Logger logger;
	private File file;
	private String templateEmissioneScontrino = "=K" + RegCassaUtils.getLineFeed() + "=C1{0}"
			+ RegCassaUtils.getLineFeed() + "=S" + RegCassaUtils.getLineFeed() + "=T";
	private String templateCreditoScontrino = "=K" + RegCassaUtils.getLineFeed() + "=C1{0}"
			+ RegCassaUtils.getLineFeed() + "=S" + RegCassaUtils.getLineFeed() + "=T2";
	private NumberFormat nf;

	public MCTFileWriter(File file, Logger logger) {
		this.file = file;
		this.logger = logger;
		nf = NumberFormat.getInstance(Locale.US);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
	}

	@Override
	public void write(Map<Integer, String> mapAliquoteReparti, List<RegCassaDatiDto> vendite, Boolean scontrinoACredito,
			FileWriter.Operation operazione, FileWriter.TipoScontrino tipoScontrino) throws IOException {
		logger.fine("Entered MCTFileWriter.write() with params: vendite=" + vendite + ", operazione=" + operazione);
		java.io.FileWriter fw = null;
		try {
			fw = new java.io.FileWriter(file);
			// List<RegCassaDto> list =
			// buildListRegCassaMCTDto(mapAliquoteReparti, vendite);
			List<RegCassaDto> list = tipoScontrino.equals(FileWriter.TipoScontrino.NORMALE)
					? buildListRegCassaMCTDto(mapAliquoteReparti, vendite)
					: buildListRegCassaDettagliatoMtcDto(mapAliquoteReparti, vendite);
			StringBuffer lines = new StringBuffer("");
			for (RegCassaDto dto : list) {
				if (tipoScontrino.equals(FileWriter.TipoScontrino.NORMALE)) {
					lines.append(MessageFormat.format(RegCassaUtils.getLineFeed() + "={0}/{1}", dto.getReparto(),dto.getPrezzo()));
				} else {
					//lines.append(MessageFormat.format(RegCassaUtils.getLineFeed() + "={0}/{1}", dto.getDescrizione(),dto.getPrezzo()));
					lines.append(MessageFormat.format(RegCassaUtils.getLineFeed() + "={0}/{1}/({2})", dto.getReparto(),	dto.getPrezzo(), dto.getDescrizione()));
				}
			}
			// lines.append(MessageFormat.format(RegCassaUtils.getLineFeed() +
			// "={0}", "MCTFileWriter"));

			fw.write(MessageFormat.format((scontrinoACredito ? templateCreditoScontrino : templateEmissioneScontrino),lines.toString()));
			
			logger.fine("@TEST FILE PRODOTTO =" + lines.toString());

			

		} finally {
			if (fw != null) {
				fw.close();
			}
		}
		logger.fine("Exiting MCTFileWriter.write()");
	}

	/**
	 * Dalla lista delle vendite costruisce una nuova lista di beans per la
	 * scrittura del file scontrino in formato dettagliato
	 * 
	 * @param Map<Integer,
	 *            String> mapAliquoteReparti
	 * @param List<RegCassaDatiDto>
	 *            vendite
	 * @return List<RegCassaMCTDto>
	 */
	private List<RegCassaDto> buildListRegCassaDettagliatoMtcDto(Map<Integer, String> mapAliquoteReparti,
			List<RegCassaDatiDto> vendite) {
		logger.fine("Entering DitronFileWriter.buildListRegCassaDettagliatoDto() with params: vendite=" + vendite);
		List<RegCassaDto> list = new ArrayList<RegCassaDto>();
		for (RegCassaDatiDto dto : vendite) {
			RegCassaDto dto1 = new RegCassaDto();
			String reparto = dto.getProdottoNonEditoriale() ? mapAliquoteReparti.get(dto.getAliquota())
					: RegCassaConstants.REPARTO_IVA_PUBBLICAZIONI;
			dto1.setReparto(reparto);

			DecimalFormat df = new DecimalFormat("#0.00");
			String prezzoFormat = "$" + df.format(dto.getPrezzoCopertina()).replaceAll("\\.", "").replaceAll(",", "");
			dto1.setPrezzo(prezzoFormat);
			//dto1.setPrezzo(nf.format(dto.getPrezzoCopertina()).replaceAll(",", ""));
			
			dto1.setQuantita(dto.getQuantita().toString());
			dto1.setDescrizione(dto.getTitolo());
			list.add(dto1);
		}
		logger.fine("Exiting DitronFileWriter.buildListRegCassaDettagliatoDto() with param list=" + list);
		return list;
	}

	/**
	 * Dalla lista delle vendite costruisce una nuova lista di beans specifici
	 * per la scrittura del file scontrino di questo registratore di cassa
	 * 
	 * @param Map<Integer,
	 *            String> mapAliquoteReparti
	 * @param List<RegCassaDatiDto>
	 *            vendite
	 * @return List<RegCassaMCTDto>
	 */
	private List<RegCassaDto> buildListRegCassaMCTDto(Map<Integer, String> mapAliquoteReparti,
			List<RegCassaDatiDto> vendite) {
		logger.fine("Entering MCTFileWriter.buildListVenditaRegCassaDto() with params: vendite=" + vendite);
		List<RegCassaDto> list = new ArrayList<RegCassaDto>();
		Map<String, Float> mapRepartoPrezzo = new HashMap<String, Float>();
		for (RegCassaDatiDto dto : vendite) {
			String reparto = dto.getProdottoNonEditoriale() ? mapAliquoteReparti.get(dto.getAliquota())
					: RegCassaConstants.REPARTO_IVA_PUBBLICAZIONI;
			Float prezzo = (dto.getQuantita().floatValue() * dto.getPrezzoCopertina());
			Float pre = mapRepartoPrezzo.get(reparto) == null ? 0 : mapRepartoPrezzo.get(reparto);
			mapRepartoPrezzo.put(reparto, (pre + prezzo));
		}
		for (Map.Entry<String, Float> entry : mapRepartoPrezzo.entrySet()) {
			RegCassaDto dto = new RegCassaDto();
			dto.setReparto(entry.getKey());
			DecimalFormat df = new DecimalFormat("#0.00");
			String prezzoFormat = "$" + df.format(entry.getValue()).replaceAll("\\.", "").replaceAll(",", "");
			dto.setPrezzo(prezzoFormat);
			list.add(dto);
		}
		logger.fine("Exiting MCTFileWriter.buildListVenditaRegCassaDto() with param list=" + list);
		return list;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.file + ")";
	}

}

/********************************************************************
 * Classe che scrive il file scontrino per il reg. cassa MCT - Flash
 * 
 * @author mromano
 *
 *         old version
 *
 *
 */
// public class MCTFileWriter extends FileWriter {
// private Logger logger;
// private File file;
// private String templateEmissioneScontrino = "=K" +
// RegCassaUtils.getLineFeed() + "=C1{0}" + RegCassaUtils.getLineFeed() + "=S" +
// RegCassaUtils.getLineFeed() + "=T";
// private String templateCreditoScontrino = "=K" + RegCassaUtils.getLineFeed()
// + "=C1{0}" + RegCassaUtils.getLineFeed() + "=S" + RegCassaUtils.getLineFeed()
// + "=T2";
//
// public MCTFileWriter(File file, Logger logger) {
// this.file = file;
// this.logger = logger;
// }
//
// @Override
// public void write(Map<Integer, String> mapAliquoteReparti,
// List<RegCassaDatiDto> vendite, Boolean scontrinoACredito,
// FileWriter.Operation operazione, FileWriter.TipoScontrino tipoScontrino)
// throws IOException {
// logger.fine("Entered MCTFileWriter.write() with params: vendite=" + vendite +
// ", operazione=" + operazione);
// java.io.FileWriter fw = null;
// try {
// fw = new java.io.FileWriter(file);
// List<RegCassaDto> list = buildListRegCassaMCTDto(mapAliquoteReparti,
// vendite);
// StringBuffer lines = new StringBuffer("");
// for (RegCassaDto dto : list) {
// lines.append(MessageFormat.format(RegCassaUtils.getLineFeed() + "={0}/{1}",
// dto.getReparto(), dto.getPrezzo()));
// }
// //lines.append(MessageFormat.format(RegCassaUtils.getLineFeed() + "={0}",
// "MCTFileWriter"));
//
// fw.write(MessageFormat.format((scontrinoACredito ? templateCreditoScontrino :
// templateEmissioneScontrino), lines.toString()));
//
//
//
//
// } finally {
// if (fw != null) {
// fw.close();
// }
// }
// logger.fine("Exiting MCTFileWriter.write()");
// }
//
// /**
// * Dalla lista delle vendite costruisce una nuova lista di beans specifici per
// la scrittura del file scontrino
// * di questo registratore di cassa
// *
// * @param Map<Integer, String> mapAliquoteReparti
// * @param List<RegCassaDatiDto> vendite
// * @return List<RegCassaMCTDto>
// */
// private List<RegCassaDto> buildListRegCassaMCTDto(Map<Integer, String>
// mapAliquoteReparti, List<RegCassaDatiDto> vendite) {
// logger.fine("Entering MCTFileWriter.buildListVenditaRegCassaDto() with
// params: vendite=" + vendite);
// List<RegCassaDto> list = new ArrayList<RegCassaDto>();
// Map<String, Float> mapRepartoPrezzo = new HashMap<String, Float>();
// for (RegCassaDatiDto dto : vendite) {
// String reparto = dto.getProdottoNonEditoriale() ?
// mapAliquoteReparti.get(dto.getAliquota()) :
// RegCassaConstants.REPARTO_IVA_PUBBLICAZIONI;
// Float prezzo = (dto.getQuantita().floatValue() * dto.getPrezzoCopertina());
// Float pre = mapRepartoPrezzo.get(reparto) == null ? 0 :
// mapRepartoPrezzo.get(reparto);
// mapRepartoPrezzo.put(reparto, (pre + prezzo));
// }
// for (Map.Entry<String, Float> entry : mapRepartoPrezzo.entrySet()) {
// RegCassaDto dto = new RegCassaDto();
// dto.setReparto(entry.getKey());
// DecimalFormat df = new DecimalFormat("#0.00");
// String prezzoFormat = "$" + df.format(entry.getValue()).replaceAll("\\.",
// "").replaceAll(",", "");
// dto.setPrezzo(prezzoFormat);
// list.add(dto);
// }
// logger.fine("Exiting MCTFileWriter.buildListVenditaRegCassaDto() with param
// list=" + list);
// return list;
// }
//
// @Override
// public String toString() {
// return this.getClass().getSimpleName() + "(" + this.file + ")";
// }
//
// }
