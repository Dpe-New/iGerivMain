package it.dpe.inforiv.importer.impl;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.inforiv.dto.input.InforivEstrattoContoDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 */
@Component("InforivEstrattoContoAccreditiNonEditorialiImporter")
public class InforivEstrattoContoAccreditiNonEditorialiImporterImpl extends InforivEstrattoContoBaseImporterImpl implements InforivImporter<InforivEstrattoContoDto> {
	private final IGerivBatchService bo;
	
	@Autowired
	InforivEstrattoContoAccreditiNonEditorialiImporterImpl(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	/* (non-Javadoc)
	 * @see it.dpe.inforiv.bo.importer.impl.InforivEstrattoContoBaseImporterImpl#buildMovimenti(it.dpe.inforiv.dto.InforivEstrattoContoDto, it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo)
	 */
	protected void buildMovimenti(InforivEstrattoContoDto dto, EstrattoContoEdicolaDettaglioVo vo) {
		vo.setTipoRecord(0);
		vo.setTipoMovimento(6);
		if (dto.getImporto().intValue() < 0) {
			vo.setImportoDare(new BigDecimal(Math.abs(dto.getImporto())));
			vo.setImportoAvere(new BigDecimal(0));
		} else {
			vo.setImportoDare(new BigDecimal(0));
			vo.setImportoAvere(new BigDecimal(Math.abs(dto.getImporto())));
		}
	}

	@Override
	public IGerivBatchService getBo() {
		return bo;
	}

}
