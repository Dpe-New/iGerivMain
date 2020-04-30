package it.dpe.inforiv.importer.impl;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.inforiv.dto.input.RispostaRichiestaRifornimentoDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 */
@Component("InforivRispostaRichiestaRifornimentoImporter")
public class InforivRispostaRichiestaRifornimentoImporter extends InforivBaseImporter implements InforivImporter<RispostaRichiestaRifornimentoDto> {
	private final IGerivBatchService bo;
	
	@Autowired
	InforivRispostaRichiestaRifornimentoImporter(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	@Override
	public void importData(List<RispostaRichiestaRifornimentoDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		for (RispostaRichiestaRifornimentoDto dto : list) {
			try {
				AbbinamentoIdtnInforivVo abii = bo.getAbbinamentoIdtnInforiv(dto.getCodFiegDl(), dto.getIdProdotto());
				if (abii == null) {
					throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.idtn.non.trovato"), dto.getIdProdotto()));
				}
				Integer idtn = abii.getIdtn();
				RichiestaRifornimentoVo rrvo = bo.getRichiestaRifornimento(dto.getCodFiegDl(), codEdicolaWeb, idtn, dto.getCodiceRichiesta());
				if (rrvo == null) {
					throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.richiesta.riforimento.non.trovata"), dto.getIdProdotto(), dto.getCodFiegDl(), codEdicolaWeb));
				}
				rrvo.setDataRispostaDl(DateUtilities.floorDay(bo.getSysdate()));
				rrvo.setDescCausaleNonEvadibilita(getDescCausaleMancataEvasione(dto.getCausaleMancataEvasione()));
				rrvo.setNoteVendita(dto.getNote());
				bo.saveBaseVo(rrvo);
			} catch (Exception e) {
				ImportException err = new ImportException(e);
				err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
				listErrori.add(err);
			}
		}
	}

	/**
	 * @param causaleMancataEvasione
	 * @return
	 */
	private String getDescCausaleMancataEvasione(final Integer causaleMancataEvasione) {
		switch (causaleMancataEvasione) {
			case 1: 
				return IGerivMessageBundle.get("");
			case 2: 
				return IGerivMessageBundle.get("");
			case 3: 
				return IGerivMessageBundle.get("");
			case 4: 
				return IGerivMessageBundle.get("");
			case 5: 
				return IGerivMessageBundle.get("");
			case 6: 
				return IGerivMessageBundle.get("");
		}
		return null;
	}

}
