package it.dpe.inforiv.importer.impl;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.BollaStatisticaStoricoVo;
import it.dpe.igeriv.vo.pk.BollaStatisticaStoricoPk;
import it.dpe.inforiv.dto.input.InforivStatisticaStoricoDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 */
@Component("InforivStatisticaStoricoImporter")
public class InforivStatisticaStoricoImporterImpl extends InforivBaseImporter implements InforivImporter<InforivStatisticaStoricoDto> {
	private final IGerivBatchService bo;
	
	@Autowired
	InforivStatisticaStoricoImporterImpl(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	@Override
	public void importData(List<InforivStatisticaStoricoDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		try {
			for (InforivStatisticaStoricoDto dto : list) {
				try {
					Integer codFiegDl = dto.getCodFiegDl();
					AbbinamentoIdtnInforivVo abii = bo.getAbbinamentoIdtnInforiv(codFiegDl, dto.getIdProdotto());
					if (abii == null) {
						throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.idtn.non.trovato"), dto.getIdProdotto()));
					}
					Integer idtn = abii.getIdtn();
					BollaStatisticaStoricoVo bvo = buildBollaStatisticaStoricoVo(dto, codFiegDl, codEdicolaWeb, idtn);
					bo.saveBaseVo(bvo);
				} catch (InvalidRecordException e) {
					ImportException err = new ImportException(e);
					err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
					listErrori.add(err);
				}
			}
			AbbinamentoEdicolaDlVo abed = bo.getAbbinamentoEdicolaDlVoByCodEdicolaWeb(codEdicolaWeb);
			abed.setDataCreazioneStatistica(bo.getSysdate());
			bo.saveBaseVo(abed);
		} catch (Exception e) {
			ImportException err = new ImportException(e);
			e.printStackTrace();
			err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
			listErrori.add(err);
		}
	}

	/**
	 * @param dto
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param idtn
	 * @return
	 * @throws InvalidRecordException
	 */
	private BollaStatisticaStoricoVo buildBollaStatisticaStoricoVo(InforivStatisticaStoricoDto dto, Integer codFiegDl, Integer codEdicolaWeb, Integer idtn) throws InvalidRecordException {
		BollaStatisticaStoricoVo vo = bo.getBollaStatisticaStoricoVo(codFiegDl, codEdicolaWeb, idtn);
		if (vo == null) {
			vo = new BollaStatisticaStoricoVo();
			BollaStatisticaStoricoPk pk = new BollaStatisticaStoricoPk();
			pk.setCodEdicola(codEdicolaWeb);
			pk.setCodFiegDl(dto.getCodFiegDl());
			pk.setIdtn(idtn);
			vo.setPk(pk);
		}
		vo.setFornito(dto.getFornito());
		vo.setReso(dto.getReso());
		return vo;
	}

}
