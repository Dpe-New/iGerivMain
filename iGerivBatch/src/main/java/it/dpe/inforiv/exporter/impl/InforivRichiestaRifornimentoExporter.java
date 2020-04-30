package it.dpe.inforiv.exporter.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.inforiv.InforivExportService;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.inforiv.dto.output.InforivRichiestaRifornimentoDto;
import it.dpe.inforiv.exporter.InforivExporter;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

/**
 * Crea le righe da esportare le richieste di rifornimento e aggiunge le righe
 * al file corrispondente a ciascuna edicola.
 * 
 * @author romanom
 *
 */
@Component("InforivRichiestaRifornimentoExporter")
public class InforivRichiestaRifornimentoExporter extends InforivBaseExporter implements InforivExporter {
	@Autowired
	private InforivExportService exportBo;
	
	@Override
	public void exportData(String tipoRecord, Timestamp now, Integer codFiegDl, Map<Integer, File> mapExportFiles, Map<Integer, Map<String, String>> mapFtpParams, FixedFormatManager manager,File dirOutputLocalDL) {
		List<RichiestaRifornimentoVo> listRichieste = exportBo.getRichiesteRifornimenti(codFiegDl);
		List<InforivRichiestaRifornimentoDto> listDto = getInforivRichiesteRifornimentoDto(listRichieste);
		Group<InforivRichiestaRifornimentoDto> groupBolleTrasmesse = group(listDto, by(on(InforivRichiestaRifornimentoDto.class).getCodEdicola()));
		for (Group<InforivRichiestaRifornimentoDto> subgroup : groupBolleTrasmesse.subgroups()) {
			List<InforivRichiestaRifornimentoDto> list = subgroup.findAll();
			forEach(list).setTipoMovimento("R");
			exportLines(tipoRecord, now, codFiegDl, mapExportFiles, mapFtpParams, manager, list,dirOutputLocalDL);
		}
		if (listRichieste != null && !listRichieste.isEmpty()) {
			forEach(listRichieste).setStato(IGerivConstants.STATO_INVIATO_DL_SIGLA);
		}
		exportBo.saveVoList(listRichieste);
	}

	/**
	 * @param listRichieste
	 * @return
	 */
	private List<InforivRichiestaRifornimentoDto> getInforivRichiesteRifornimentoDto(List<RichiestaRifornimentoVo> listRichieste) {
		List<InforivRichiestaRifornimentoDto> list = new ArrayList<InforivRichiestaRifornimentoDto>();
		for (RichiestaRifornimentoVo vo : listRichieste) {
			InforivRichiestaRifornimentoDto dto = new InforivRichiestaRifornimentoDto();
			dto.setCodEdicola(vo.getPk().getCodEdicola());
			dto.setCodFiegDl(vo.getPk().getCodFiegDl());
			dto.setCodiceRichiesta(getBo().getNextSeqVal(IGerivConstants.SEQ_RICHIESTE_RIFORNIMENTO).intValue());
			dto.setIdProdotto(vo.getIdtnTrascodifica());
			dto.setNote(vo.getNoteVendita());
			dto.setCopieRichieste(vo.getQuantitaRichiesta());
			list.add(dto);
		}
		return list;
	}

	public InforivExportService getExportBo() {
		return exportBo;
	}

	public void setExportBo(InforivExportService exportBo) {
		this.exportBo = exportBo;
	}
	
}
