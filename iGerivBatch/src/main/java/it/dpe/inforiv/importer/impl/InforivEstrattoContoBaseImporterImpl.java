package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaVo;
import it.dpe.igeriv.vo.pk.EstrattoContoEdicolaDettaglioPk;
import it.dpe.igeriv.vo.pk.EstrattoContoEdicolaPk;
import it.dpe.inforiv.dto.input.InforivEstrattoContoDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.lambdaj.group.Group;

/**
 * Classe base per l'importazione dell'estratto conto.
 * 
 * @author romanom
 *
 */
public abstract class InforivEstrattoContoBaseImporterImpl extends InforivBaseImporter implements InforivImporter<InforivEstrattoContoDto> {
	
	private final Logger log = Logger.getLogger(getClass());

	/* (non-Javadoc)
	 * @see it.dpe.inforiv.bo.importer.InforivImporter#importData(java.util.List, java.util.List, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public void importData(List<InforivEstrattoContoDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		try {
			log.info("Inizio importazione E/C");
			Group<InforivEstrattoContoDto> group = group(list, by(on(InforivEstrattoContoDto.class).getNumeroEstrattoConto()), by(on(InforivEstrattoContoDto.class).getDataEstrattoConto()));
			for (Group<InforivEstrattoContoDto> subgroup : group.subgroups()) {
				List<InforivEstrattoContoDto> listBolla = subgroup.findAll();
				if (!listBolla.isEmpty()) {
					try {
						InforivEstrattoContoDto estrattoContoTesta = listBolla.get(0);
						java.sql.Date dataEstrattoConto = new java.sql.Date(estrattoContoTesta.getDataEstrattoConto().getTime());
						EstrattoContoEdicolaVo bvo = buildEstrattoContoEdicolaVo(estrattoContoTesta, codEdicolaWeb, dataEstrattoConto);
						String lastDeleteted = parameters.get(bvo.toString());
						if (lastDeleteted == null || !Boolean.parseBoolean(lastDeleteted.toString())) {
							getBo().deleteVo(bvo);
							parameters.put(bvo.toString(), "true");
							bvo = buildNewEstrattoContoEdicolaVo(estrattoContoTesta, codEdicolaWeb, dataEstrattoConto);
							bvo.setDettagli(new ArrayList<EstrattoContoEdicolaDettaglioVo>());
						}
						Integer progressivo = getLastProgressivoRiga(codEdicolaWeb, estrattoContoTesta, dataEstrattoConto);
						for (InforivEstrattoContoDto dto : listBolla) {
							try {
								EstrattoContoEdicolaDettaglioVo bvod = buildEstrattoContoEdicolaDettaglioVo(dto, codEdicolaWeb, dataEstrattoConto, progressivo++);
								bvo.getDettagli().add(bvod);
							} catch (Exception e) {
								ImportException err = new ImportException(e);
								err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
								listErrori.add(err);
							}
						}
						getBo().saveBaseVo(bvo);
					} catch (Exception e) {
						ImportException err = new ImportException(e);
						err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
						listErrori.add(err);
					}
				}
			}
			log.info("Fine importazione E/C");
		} catch (Exception e) {
			ImportException err = new ImportException(e);
			e.printStackTrace();
			err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
			listErrori.add(err);
		}
	}

	/**
	 * Ritorna l'ultimo progressivo dei dettagli estratto conto per codice dl, codice edicola, data estratto conto.
	 * 
	 * @param codEdicolaWeb
	 * @param estrattoContoTesta
	 * @param dataEstrattoConto
	 * @return
	 */
	private Integer getLastProgressivoRiga(Integer codEdicolaWeb, InforivEstrattoContoDto estrattoContoTesta, java.sql.Date dataEstrattoConto) {
		Integer progressivo;
		progressivo = getBo().getLastProgressivoEstrattoConto(estrattoContoTesta.getCodFiegDl(), codEdicolaWeb, dataEstrattoConto);
		if (progressivo == null) {
			progressivo = 0;
		} else {
			progressivo++;
		}
		return progressivo;
	}

	/**
	 * Costruisce il dettaglio dell'estratto conto.
	 * 
	 * @param dto
	 * @param codEdicolaWeb
	 * @param dataEstrattoConto
	 * @param progressivo
	 * @return
	 */
	private EstrattoContoEdicolaDettaglioVo buildEstrattoContoEdicolaDettaglioVo(InforivEstrattoContoDto dto, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto, int progressivo) {
		EstrattoContoEdicolaDettaglioVo vo = getBo().getEstrattoContoEdicolaDettaglioVo(dto.getCodFiegDl(), codEdicolaWeb, dataEstrattoConto, progressivo);
		if (vo == null) {
			vo = new EstrattoContoEdicolaDettaglioVo();
			EstrattoContoEdicolaDettaglioPk pk = new EstrattoContoEdicolaDettaglioPk();
			pk.setCodEdicola(codEdicolaWeb);
			pk.setCodFiegDl(dto.getCodFiegDl());
			pk.setDataEstrattoConto(dataEstrattoConto);
			pk.setProgressivo(progressivo);
			vo.setPk(pk);
		}
		vo.setDataMovimento(new Timestamp(dto.getDataMovimentoContabile().getTime()));
		vo.setNote(dto.getDescrizioneCausale());
		buildMovimenti(dto, vo);
		return vo;
	}

	/**
	 * Costruisce la testa dell'estratto conto.
	 * 
	 * @param dto
	 * @param codEdicolaWeb
	 * @param dataEstrattoConto
	 * @return
	 * @throws InvalidRecordException
	 */
	private EstrattoContoEdicolaVo buildEstrattoContoEdicolaVo(InforivEstrattoContoDto dto, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto) throws InvalidRecordException {
		EstrattoContoEdicolaVo vo = getBo().getEstrattoContoEdicolaVo(dto.getCodFiegDl(), codEdicolaWeb, dataEstrattoConto);
		if (vo == null) {
			vo = buildNewEstrattoContoEdicolaVo(dto, codEdicolaWeb, dataEstrattoConto);
		}
		vo.setNumEstrattoConto(dto.getNumeroEstrattoConto());
		return vo;
	}

	/**
	 * Costruisce un nuovo EstrattoContoEdicolaVo.
	 *  
	 * @param dto
	 * @param codEdicolaWeb
	 * @param dataEstrattoConto
	 * @return
	 */
	private EstrattoContoEdicolaVo buildNewEstrattoContoEdicolaVo(InforivEstrattoContoDto dto, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto) {
		EstrattoContoEdicolaVo vo;
		vo = new EstrattoContoEdicolaVo();
		EstrattoContoEdicolaPk pk = new EstrattoContoEdicolaPk();
		pk.setCodEdicola(codEdicolaWeb);
		pk.setCodFiegDl(dto.getCodFiegDl());
		pk.setDataEstrattoConto(dataEstrattoConto);
		vo.setPk(pk);
		return vo;
	}
	
	/**
	 * @param dto
	 * @param vo
	 */
	protected abstract void buildMovimenti(InforivEstrattoContoDto dto, EstrattoContoEdicolaDettaglioVo vo);

	public abstract IGerivBatchService getBo();

}
