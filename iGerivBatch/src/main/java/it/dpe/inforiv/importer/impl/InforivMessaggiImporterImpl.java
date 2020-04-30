package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.Matchers.equalTo;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.MessaggiBollaVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.pk.MessaggiBollaPk;
import it.dpe.igeriv.vo.pk.MessaggioPk;
import it.dpe.inforiv.dto.input.InforivMessaggiDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

/**
 * @author romanom
 */
@Component("InforivMessaggiImporter")
public class InforivMessaggiImporterImpl extends InforivBaseImporter implements InforivImporter<InforivMessaggiDto> {
	private final IGerivBatchService bo;
	
	@Autowired
	InforivMessaggiImporterImpl(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	@Override 
	public void importData(List<InforivMessaggiDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		try {
			Group<InforivMessaggiDto> group = group(list, by(on(InforivMessaggiDto.class).getDataBollaEstrattoContoMessaggio()), by(on(InforivMessaggiDto.class).getTipoBolla()), by(on(InforivMessaggiDto.class).getNumeroBolla()));
			for (Group<InforivMessaggiDto> subgroup : group.subgroups()) {
				List<InforivMessaggiDto> listBolla = subgroup.findAll();
				for (InforivMessaggiDto dto : listBolla) {
					try {
						Integer codFiegDl = dto.getCodFiegDl();
						BaseVo vo = null;
						if (dto.getDataBollaEstrattoContoMessaggio() != null && dto.getTipoBolla() != null && dto.getNumeroBolla() != null && !dto.getTipoBolla().equals("")) {
							vo = buildMessaggiBollaVo(codFiegDl, codEdicolaWeb, dto);
						} else {
							vo = builMessaggioVo(codFiegDl, codEdicolaWeb, dto);
						}
						bo.saveBaseVo(vo);
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
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param idtn
	 * @param dto
	 * @param progressivo 
	 * @return
	 */
	private MessaggiBollaVo buildMessaggiBollaVo(Integer codFiegDl, Integer codEdicolaWeb, InforivMessaggiDto dto) {
		Timestamp dtBolla = new Timestamp(dto.getDataBollaEstrattoContoMessaggio().getTime());
		String tipo = dto.getTipoBolla();
		List<MessaggiBollaVo> listMessaggi = bo.getMessaggiBollaEdicola(codFiegDl, codEdicolaWeb, dtBolla, tipo);
		int progressivo = 0;
		if (listMessaggi != null && !listMessaggi.isEmpty()) {
			progressivo = listMessaggi.get(listMessaggi.size() - 1).getPk().getProgressivo();
			progressivo++;
		} 
		Object obj = (listMessaggi != null && !listMessaggi.isEmpty()) ? selectUnique(listMessaggi, having(on(MessaggiBollaVo.class).getMessaggio(), equalTo(dto.getTesto()))) : null;
		MessaggiBollaVo vo = (obj != null) ? (MessaggiBollaVo) obj : null;
		if (vo == null) {
			vo = new MessaggiBollaVo();
			MessaggiBollaPk pk = new MessaggiBollaPk();
			pk.setCodEdicola(codEdicolaWeb);
			pk.setCodFiegDl(codFiegDl);
			pk.setDtBolla(dtBolla);
			pk.setTipoBolla(tipo);
			pk.setTipoMessaggio(16);
			pk.setProgressivo(progressivo);
			vo.setPk(pk);
		}
		vo.setMessaggio(dto.getTesto());
		return vo;
	}

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dto
	 * @return
	 */
	private MessaggioVo builMessaggioVo(Integer codFiegDl, Integer codEdicolaWeb, InforivMessaggiDto dto) {
		String priorita = dto.getPriorita();
		Timestamp dtMessaggio = new Timestamp(dto.getDataBollaEstrattoContoMessaggio().getTime());
		MessaggioVo vo = bo.getMessaggioRivenditaVo(codFiegDl, dtMessaggio, IGerivConstants.COD_EDICOLA_SINGOLA, codEdicolaWeb, 0);
		if (vo == null) {
			vo = new MessaggioVo();
			MessaggioPk pk = new MessaggioPk();
			pk.setCodFiegDl(codFiegDl);
			pk.setDestinatarioA(codEdicolaWeb);
			pk.setDestinatarioB(0);
			pk.setDtMessaggio(dtMessaggio);
			pk.setTipoDestinatario(IGerivConstants.COD_EDICOLA_SINGOLA);
			vo.setPk(pk);
		}
		vo.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_INVIATO);
		vo.setTipoMessaggio((priorita.equals("A") ? 2 : (priorita.equals("M") ? 1 : (priorita.equals("B") ? 0 : 0))));
		vo.setMessaggio(dto.getTesto());
		return vo;
	}

}
