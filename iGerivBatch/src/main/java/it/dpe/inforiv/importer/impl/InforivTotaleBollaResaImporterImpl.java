package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import it.dpe.igeriv.vo.pk.BollaResaRiassuntoPk;
import it.dpe.inforiv.dto.input.InforivTotaleBollaResaDto;
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
@Component("InforivTotaleBollaResaImporter")
public class InforivTotaleBollaResaImporterImpl extends InforivBaseImporter implements InforivImporter<InforivTotaleBollaResaDto> {
	private final IGerivBatchService bo;
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	InforivTotaleBollaResaImporterImpl(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	@Override
	public void importData(List<InforivTotaleBollaResaDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		try {
			log.info("Inizio importazione TD");
			Group<InforivTotaleBollaResaDto> group = group(list, by(on(InforivTotaleBollaResaDto.class).getDataBolla()), by(on(InforivTotaleBollaResaDto.class).getTipoBolla()), by(on(InforivTotaleBollaResaDto.class).getNumeroBolla()));
			for (Group<InforivTotaleBollaResaDto> subgroup : group.subgroups()) {
				List<InforivTotaleBollaResaDto> listBolla = subgroup.findAll();
				for (InforivTotaleBollaResaDto dto : listBolla) {
					try {
						BollaResaRiassuntoVo bvo = buildBollaResaRiassuntoVo(dto, codEdicolaWeb);
						bo.saveBaseVo(bvo);
					} catch (InvalidRecordException e) {
						ImportException err = new ImportException(e);
						err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
						listErrori.add(err);
					}
				}
			}
			log.info("Fine importazione TD");
		} catch (Exception e) {
			ImportException err = new ImportException(e);
			err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
			listErrori.add(err);
		}
	}

	private BollaResaRiassuntoVo buildBollaResaRiassuntoVo(InforivTotaleBollaResaDto dto, Integer codEdicolaWeb) throws InvalidRecordException {
		Timestamp dtBolla = new Timestamp(dto.getDataBolla().getTime());
		List<BollaResaRiassuntoVo> list = bo.getBollaResaRiassunto(new Integer[]{dto.getCodFiegDl()}, new Integer[]{codEdicolaWeb}, dtBolla, dto.getTipoBolla());
		BollaResaRiassuntoVo vo = (list == null || list.isEmpty()) ? null : list.get(0);
		if (vo == null) {
			vo = new BollaResaRiassuntoVo();
			//0000175
			vo.setBollaTrasmessaDl(IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA);
			BollaResaRiassuntoPk pk = new BollaResaRiassuntoPk();
			pk.setCodEdicola(codEdicolaWeb);
			pk.setCodFiegDl(dto.getCodFiegDl());
			pk.setDtBolla(dtBolla);
			pk.setTipoBolla(dto.getTipoBolla());
			vo.setPk(pk);
		}
		//Importazione Dati da Inforiv - ERRORE FATALE
		if(vo.getBollaTrasmessaDl()==null)
			vo.setBollaTrasmessaDl(IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA);
		
		vo.setValoreBolla(new BigDecimal(dto.getTotaleBollaResa()));
		vo.setGruppoSconto(codEdicolaWeb);
		return vo;
	}
	
}
