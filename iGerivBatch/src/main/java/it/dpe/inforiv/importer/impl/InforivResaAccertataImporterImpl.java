package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.ResaRiscontrataVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.pk.ResaRiscontrataPk;
import it.dpe.inforiv.dto.input.InforivResaDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

/**
 * @author romanom
 */
@Component("InforivResaAccertataImporter")
public class InforivResaAccertataImporterImpl extends InforivBaseImporter implements InforivImporter<InforivResaDto> {
	private final IGerivBatchService bo;
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	InforivResaAccertataImporterImpl(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	@Override 
	public void importData(List<InforivResaDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		try {
			log.info("Inizio importazione RD");
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
						ResaRiscontrataVo bvo = buildResaRiscontrataVo(idtn, codFiegDl, dto, codEdicolaWeb);
						bo.saveBaseVo(bvo);
					} catch (Exception e) {
						ImportException err = new ImportException(e);
						err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
						listErrori.add(err);
					}
				}
			}
			log.info("Fine importazione RD");
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
	 */
	private ResaRiscontrataVo buildResaRiscontrataVo(Integer idtn, Integer codFiegDl, InforivResaDto apdto, Integer codEdicolaWeb) {
		ResaRiscontrataVo vo = bo.getResaRiscontrataVo(codFiegDl, codEdicolaWeb, apdto.getDataBolla(), apdto.getTipoBolla(), idtn);
		if (vo == null) {
			vo = new ResaRiscontrataVo();
			ResaRiscontrataPk pk = new ResaRiscontrataPk();
			pk.setCodEdicola(codEdicolaWeb);
			pk.setCodFiegDl(codFiegDl);
			pk.setDtBolla(new Timestamp(apdto.getDataBolla().getTime()));
			pk.setTipoBolla(apdto.getTipoBolla());
			pk.setIdtn(idtn);
			vo.setPk(pk);
		}
		StoricoCopertineVo copertina = bo.getStoricoCopertinaByPk(codFiegDl, idtn);
		vo.setCpu(copertina.getCodicePubblicazione());
		vo.setTitolo(copertina.getAnagraficaPubblicazioniVo().getTitolo());
		vo.setNumero(copertina.getNumeroCopertina());
		vo.setPrezzoNetto(apdto.getPrezzoNetto());
		vo.setResoRiscontrato(apdto.getCopie());
		return vo;
	}

}
