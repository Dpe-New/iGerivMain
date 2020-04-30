package it.dpe.igeriv.web.applet.regcassa.filewriter.impl;

import java.io.File;
import java.io.IOException;
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
 * Classe che scrive il file scontrino per il reg. cassa Ditron
 * 
 * @author mromano
 *
 */
public class DitronFileWriter extends FileWriter {
	private static final String VENDITA_LINE_TEMPLATE_NORMAL = "VEND REP={0},PRE={1}";
	private static final String VENDITA_LINE_TEMPLATE_DETAILED = "VEND REP={0},PRE={1},QTY={2},DES=''{3}''";
	private Logger logger;
	private File file;
	private String templateEmissioneScontrino = "CLEAR" + RegCassaUtils.getLineFeed() + "CHIAVE REG" + "{0}" + RegCassaUtils.getLineFeed() + "CHIUS T=1" + RegCassaUtils.getLineFeed();
	private String templateCreditoScontrino = "CLEAR" + RegCassaUtils.getLineFeed() + "CHIAVE REG" + "{0}" + RegCassaUtils.getLineFeed() + "CHIUS T=2" + RegCassaUtils.getLineFeed();
	private NumberFormat nf;
	
	public DitronFileWriter(File file, Logger logger) {
		this.file = file;
		this.logger = logger;
		nf = NumberFormat.getInstance(Locale.US);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
	}
	
	@Override
	public void write(Map<Integer, String> mapAliquoteReparti, List<RegCassaDatiDto> vendite, Boolean scontrinoACredito, FileWriter.Operation operazione, FileWriter.TipoScontrino tipoScontrino) throws IOException {
		logger.fine("Entered DitronFileWriter.write() with params: vendite=" + vendite + ", operazione=" + operazione);
		java.io.FileWriter fw = null;
		try {
			fw = new java.io.FileWriter(file);
			List<RegCassaDto> list = tipoScontrino.equals(FileWriter.TipoScontrino.NORMALE) ? buildListRegCassaDto(mapAliquoteReparti, vendite) : buildListRegCassaDettagliatoDto(mapAliquoteReparti, vendite);
			StringBuffer lines = new StringBuffer("");
			for (RegCassaDto dto : list) {
				String line = null;
				if (tipoScontrino.equals(FileWriter.TipoScontrino.NORMALE)) {
					line = MessageFormat.format(RegCassaUtils.getLineFeed() + VENDITA_LINE_TEMPLATE_NORMAL, dto.getReparto().replace("R", ""),  dto.getPrezzo());
				} else {
					line = MessageFormat.format(RegCassaUtils.getLineFeed() + VENDITA_LINE_TEMPLATE_DETAILED, dto.getReparto().replace("R", ""),  dto.getPrezzo(), dto.getQuantita(), dto.getDescrizione());
				}
				lines.append(line);
			}
			//lines.append(MessageFormat.format(RegCassaUtils.getLineFeed() + "={0}", "MCTFileWriter"));
			
			String scontrino = MessageFormat.format((scontrinoACredito ? templateCreditoScontrino : templateEmissioneScontrino), lines.toString());
			
			logger.fine("scontrino=" + scontrino);
			fw.write(scontrino);
			
			
			
		} finally {
			if (fw != null) {
				fw.close();
			}
		}
		logger.fine("Exiting DitronFileWriter.write()");
	}
	
	/**
	 * Dalla lista delle vendite costruisce una nuova lista di beans per la scrittura del file scontrino in formato dettagliato
	 * 
	 * @param Map<Integer, String> mapAliquoteReparti 
	 * @param List<RegCassaDatiDto> vendite
	 * @return List<RegCassaMCTDto>
	 */
	private List<RegCassaDto> buildListRegCassaDettagliatoDto(Map<Integer, String> mapAliquoteReparti, List<RegCassaDatiDto> vendite) {
		logger.fine("Entering DitronFileWriter.buildListRegCassaDettagliatoDto() with params: vendite=" + vendite);
		List<RegCassaDto> list = new ArrayList<RegCassaDto>();
		for (RegCassaDatiDto dto : vendite) {
			RegCassaDto dto1 = new RegCassaDto();
			String reparto = dto.getProdottoNonEditoriale() ? mapAliquoteReparti.get(dto.getAliquota()) : RegCassaConstants.REPARTO_IVA_PUBBLICAZIONI;
			dto1.setReparto(reparto);
			dto1.setPrezzo(nf.format(dto.getPrezzoCopertina()).replaceAll(",", ""));
			dto1.setQuantita(dto.getQuantita().toString());
			dto1.setDescrizione(dto.getTitolo());
			list.add(dto1);
		}
		logger.fine("Exiting DitronFileWriter.buildListRegCassaDettagliatoDto() with param list=" + list);
		return list;
	}

	/**
	 * Dalla lista delle vendite costruisce una nuova lista di beans per la scrittura del file scontrino 
	 * raggruppati per reparto, somma prezzo degli articoli di questo reparto
	 * 
	 * @param Map<Integer, String> mapAliquoteReparti 
	 * @param List<RegCassaDatiDto> vendite
	 * @return List<RegCassaMCTDto>
	 */
	private List<RegCassaDto> buildListRegCassaDto(Map<Integer, String> mapAliquoteReparti, List<RegCassaDatiDto> vendite) {
		logger.fine("Entering DitronFileWriter.buildListRegCassaDto() with params: vendite=" + vendite);
		List<RegCassaDto> list = new ArrayList<RegCassaDto>();
		Map<String, Float> mapRepartoPrezzo = new HashMap<String, Float>();
		for (RegCassaDatiDto dto : vendite) {
			String reparto = dto.getProdottoNonEditoriale() ? mapAliquoteReparti.get(dto.getAliquota()) : RegCassaConstants.REPARTO_IVA_PUBBLICAZIONI;
			Float prezzo = (dto.getQuantita().floatValue() * dto.getPrezzoCopertina());
			Float pre = mapRepartoPrezzo.get(reparto) == null ? 0 : mapRepartoPrezzo.get(reparto);
			mapRepartoPrezzo.put(reparto, (pre + prezzo));
		}
		for (Map.Entry<String, Float> entry : mapRepartoPrezzo.entrySet()) {
			RegCassaDto dto = new RegCassaDto();
			dto.setReparto(entry.getKey());
			String prezzoFormat = nf.format(entry.getValue());
			dto.setPrezzo(prezzoFormat.replaceAll(",", ""));
			logger.fine("dto=" + dto);
			list.add(dto);
		}
		logger.fine("Exiting DitronFileWriter.buildListRegCassaDto() with param list=" + list);
		return list;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.file + ")";
	}

}
