package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.BollaResaNonTrovataException;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.BollaResaDettaglioVo;
import it.dpe.inforiv.dto.input.InforivResaDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

/**
 * @author romanom
 */
@Component("InforivResaDichiarataImporter")
public class InforivResaDichiarataImpl extends InforivBaseImporter implements InforivImporter<InforivResaDto> {
	private final IGerivBatchService bo;
	
	@Autowired
	InforivResaDichiarataImpl(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	@Override 
	public void importData(List<InforivResaDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		try {
			Group<InforivResaDto> group = group(list, by(on(InforivResaDto.class).getDataBolla()), by(on(InforivResaDto.class).getTipoBolla()), by(on(InforivResaDto.class).getNumeroBolla()));
			for (Group<InforivResaDto> subgroup : group.subgroups()) {
				List<InforivResaDto> listBolla = subgroup.findAll();
				for (InforivResaDto dto : listBolla) {
					try {
						Integer codFiegDl = dto.getCodFiegDl();
						AbbinamentoIdtnInforivVo abii = bo.getAbbinamentoIdtnInforiv(codFiegDl, dto.getIdProdotto());
						if (abii == null) {
							throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.idtn.non.trovato"), dto.getIdProdotto()));
						}
						Integer idtn = abii.getIdtn();
						BollaResaDettaglioVo bdvo = buildBollaResaDettaglioVo(idtn, codFiegDl, dto, codEdicolaWeb);
						bo.saveBaseVo(bdvo);
					} catch (Exception e) {
						ImportException err = new ImportException(e);
						err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
						listErrori.add(err);
					}
				}
			}
		} catch (Exception e) {
			ImportException err = new ImportException(e);
			err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
			listErrori.add(err);
		}
	}

	/**
	 * @param idtn
	 * @param codFiegDl
	 * @param apdto
	 * @param codEdicolaWeb
	 * @return
	 * @throws BollaResaNonTrovataException 
	 */
	private BollaResaDettaglioVo buildBollaResaDettaglioVo(Integer idtn, Integer codFiegDl, InforivResaDto apdto, Integer codEdicolaWeb) throws BollaResaNonTrovataException {
		BollaResaDettaglioVo vo = bo.getBollaResaDettaglioVo(codFiegDl, codEdicolaWeb, apdto.getDataBolla(), apdto.getTipoBolla(), idtn);
		if (vo == null) {
			throw new BollaResaNonTrovataException(MessageFormat.format(IGerivMessageBundle.get("imp.file.inforiv.bolla.resa.non.trovata.exception"), codFiegDl.toString(), codEdicolaWeb.toString(), DateUtilities.getTimestampAsString(apdto.getDataBolla(), DateUtilities.FORMATO_DATA), apdto.getTipoBolla(), idtn.toString()));
		}
		vo.setReso(new Integer(apdto.getCopie()));
		return vo;
	}

}
