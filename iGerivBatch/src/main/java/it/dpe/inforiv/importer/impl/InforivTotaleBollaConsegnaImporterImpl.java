package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.pk.BollaRiassuntoPk;
import it.dpe.inforiv.dto.input.InforivTotaleBollaConsegnaDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.math.BigDecimal;
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
@Component("InforivTotaleBollaConsegnaImporter")
public class InforivTotaleBollaConsegnaImporterImpl extends InforivBaseImporter implements InforivImporter<InforivTotaleBollaConsegnaDto> {
	private final IGerivBatchService bo;
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	InforivTotaleBollaConsegnaImporterImpl(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	@Override
	public void importData(List<InforivTotaleBollaConsegnaDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		try {
			log.info("Inizio importazione BC");
			Group<InforivTotaleBollaConsegnaDto> group = group(list, by(on(InforivTotaleBollaConsegnaDto.class).getDataBolla()), by(on(InforivTotaleBollaConsegnaDto.class).getTipoBolla()), by(on(InforivTotaleBollaConsegnaDto.class).getNumeroBolla()));
			for (Group<InforivTotaleBollaConsegnaDto> subgroup : group.subgroups()) {
				List<InforivTotaleBollaConsegnaDto> listBolla = subgroup.findAll();
				for (InforivTotaleBollaConsegnaDto dto : listBolla) {
					try {
						BollaRiassuntoVo bvo = buildBollaRiassuntoVo(dto, codEdicolaWeb);
						bo.saveBaseVo(bvo);
					} catch (InvalidRecordException e) {
						ImportException err = new ImportException(e);
						err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
						listErrori.add(err);
					}
				}
			}
			log.info("Fine importazione BC");
		} catch (Exception e) {
			ImportException err = new ImportException(e);
			e.printStackTrace();
			err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
			listErrori.add(err);
		}
	}

	private BollaRiassuntoVo buildBollaRiassuntoVo(InforivTotaleBollaConsegnaDto dto, Integer codEdicolaWeb) throws InvalidRecordException {
		Timestamp dtBolla = new Timestamp(dto.getDataBolla().getTime());
		BollaRiassuntoVo vo = bo.getBollaRiassunto(dto.getCodFiegDl(), codEdicolaWeb, dtBolla, dto.getTipoBolla());
		if (vo == null) {
			vo = new BollaRiassuntoVo();
			//0000175
			vo.setBollaTrasmessaDl(IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA);
			BollaRiassuntoPk pk = new BollaRiassuntoPk();
			pk.setCodEdicola(codEdicolaWeb);
			pk.setCodFiegDl(dto.getCodFiegDl());
			pk.setDtBolla(dtBolla);
			pk.setTipoBolla(dto.getTipoBolla());
			vo.setPk(pk);
		}
		//Importazione Dati da Inforiv - ERRORE FATALE
		if(vo.getBollaTrasmessaDl()==null)
			vo.setBollaTrasmessaDl(IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA);
		
		vo.setValoreBolla(new BigDecimal(dto.getValoreFondoBolla()));
		vo.setGruppoSconto(codEdicolaWeb);
		return vo;
	}

}
