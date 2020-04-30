package it.dpe.igeriv.bo.batch;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.ImportazioneFileDlResultDto;
import it.dpe.igeriv.dto.ResaEdicolaDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.ImageImportException;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.BaseVo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mromano
 * 
 */
@Service("IGerivImportService")
class IGerivImportServiceImpl implements IGerivImportService {
	private final IGerivImportBo bo;
	
	@Autowired
	IGerivImportServiceImpl(IGerivImportBo bo) {
		this.bo = bo;
	}
	
	@Override
	public BaseVo saveBaseVo(BaseVo vo) {
		return bo.saveBaseVo(vo);
	}
	
	@Override
	public void importRitiriRtae(Serializable object) throws IGerivBusinessException {
		bo.importRitiriRtae(object);
	}

	@Override
	public void importLavorazioneResaRivendita(ResaEdicolaDto object) {
		bo.importLavorazioneResaRivendita(object);
	}

	@Override
	public void importImmagine(String name) {
		bo.importImmagine(name);
	}

	@Override
	public synchronized ImportazioneFileDlResultDto importaFileDl(String name, List<String> list) {
		return bo.importaFileDl(name, list);
	}

	@Override
	public synchronized ImportazioneFileDlResultDto importaFileDlInforiv(String name, List<String> list) {
		return bo.importaFileDlInforiv(name, list);
	}

	@Override
	public void importImmagineMiniaturaQuotidiano(File fileImg, String titolo) throws IOException, ImageImportException {
		bo.importImmagineMiniaturaQuotidiano(fileImg, titolo);
	}

	@Override
	public void importImmagineMiniaturaPeriodico(File fileImg, String titolo) throws IOException {
		bo.importImmagineMiniaturaPeriodico(fileImg, titolo);
	}

	@Override
	public void addWatermarkToImage(File file) throws IOException {
		bo.addWatermarkToImage(file);
	}

	@Override
	public AnagraficaAgenziaVo getAgenziaByCodice(Integer codiceDl) {
		return bo.getAgenziaByCodice(codiceDl);
	}
	
	@Override
	public AnagraficaAgenziaVo getAgenziaByCodiceDlWeb(Integer codiceDlWeb) {
		return bo.getAgenziaByCodiceDlWeb(codiceDlWeb);
	}
	
	@Override
	public EdicolaDto getEdicolaByCodRivDl(Integer coddl, Integer codRivDl) {
		return bo.getEdicolaByCodRivDl(coddl, codRivDl);
	}

}
