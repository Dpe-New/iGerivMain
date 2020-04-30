package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.exception.PubblicazioneInBollaConsegnaException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.BollaVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.pk.BollaPk;
import it.dpe.inforiv.dto.input.InforivFornitureSonoInoltreUsciteDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

/**
 * @author romanom
 */
@Component("InforivSonoInoltreUsciteImporter")
public class InforivSonoInoltreUsciteImporterImpl extends InforivBaseImporter implements InforivImporter<InforivFornitureSonoInoltreUsciteDto> {
	private final IGerivBatchService bo;
	
	@Autowired
	InforivSonoInoltreUsciteImporterImpl(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	@Override
	public void importData(List<InforivFornitureSonoInoltreUsciteDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		/*
		try {
			Group<InforivFornitureSonoInoltreUsciteDto> group = group(list, by(on(InforivFornitureSonoInoltreUsciteDto.class).getDataBolla()), by(on(InforivFornitureSonoInoltreUsciteDto.class).getTipoBolla()), by(on(InforivFornitureSonoInoltreUsciteDto.class).getNumeroBolla()));
			for (Group<InforivFornitureSonoInoltreUsciteDto> subgroup : group.subgroups()) {
				List<InforivFornitureSonoInoltreUsciteDto> listBolla = subgroup.findAll();
				for (InforivFornitureSonoInoltreUsciteDto dto : listBolla) {
					try {
						Integer codFiegDl = dto.getCodFiegDl();
						AbbinamentoIdtnInforivVo abii = bo.getAbbinamentoIdtnInforiv(codFiegDl, dto.getIdProdotto());
						if (abii == null) {
							throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.idtn.non.trovato"), dto.getIdProdotto()));
						}
						Integer idtn = abii.getIdtn();
						Timestamp dataBolla = new Timestamp(dto.getDataBolla().getTime());
						String tipoBolla = dto.getTipoBolla();
						Integer maxRiga = bo.getLastRigaBolla(codFiegDl, dataBolla, tipoBolla);
						BollaVo bvo = buildBollaVo(idtn, codFiegDl, ++maxRiga, dto);
						bo.saveBaseVo(bvo);
					} catch (PubblicazioneInBollaConsegnaException e) {
						// Pubblicazione già in bolla di consegna
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
		*/
	}
	
	/**
	 * @param Integer idtn
	 * @param Integer codFiegDl
	 * @param InforivFornitureDto apdto
	 * @return BollaVo
	 */
	private BollaVo buildBollaVo(Integer idtn, Integer codFiegDl, Integer maxRiga, InforivFornitureSonoInoltreUsciteDto apdto) throws PubblicazioneInBollaConsegnaException {
		Date dataBolla = apdto.getDataBolla();
		String tipoBolla = apdto.getTipoBolla();
		BollaVo vo = bo.getBollaVo(codFiegDl, dataBolla, tipoBolla, idtn);
		if (vo != null) {
			throw new PubblicazioneInBollaConsegnaException();
		}
		vo = new BollaVo();
		BollaPk pk = new BollaPk();
		pk.setCodFiegDl(codFiegDl);
		pk.setDtBolla(new Timestamp(dataBolla.getTime()));
		pk.setTipoBolla(tipoBolla);
		pk.setPosizioneRiga(idtn);
		vo.setPk(pk);
		vo.setIdTestataNumero(idtn);
		StoricoCopertineVo copertina = bo.getStoricoCopertinaByPk(codFiegDl, idtn);
		if (copertina != null) {
			vo.setCpuDl(copertina.getCodicePubblicazione());
			vo.setNumeroPubblicazione(copertina.getNumeroCopertina());
			vo.setTitolo(copertina.getAnagraficaPubblicazioniVo().getTitolo());
			vo.setSottoTitolo(copertina.getSottoTitolo());
			vo.setPrezzoLordo(copertina.getPrezzoCopertina());
		} else {
			vo.setCpuDl(0);
			vo.setNumeroPubblicazione(" ");
			vo.setTitolo(" ");
			vo.setSottoTitolo(" ");
		}
		vo.setRiga(maxRiga);
		return vo;
	}

}
