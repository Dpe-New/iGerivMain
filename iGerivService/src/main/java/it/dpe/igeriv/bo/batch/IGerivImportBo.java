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

/**
 * @author romanom
 * 
 */
public interface IGerivImportBo {
	
	/**
	 * @param vo
	 */
	public BaseVo saveBaseVo(BaseVo vo);
	
	/**
	 * Importa i ritiri provenienti da RTAE.
	 * 
	 * @param Serializable object
	 * @throws IGerivBusinessException
	 */
	public void importRitiriRtae(Serializable object) throws IGerivBusinessException;
	
	/**
	 * Importa i dati della resa di un'edicola provenienti dall'impianto.
	 * 
	 * @param ResaEdicolaDto object
	 */
	public void importLavorazioneResaRivendita(ResaEdicolaDto object);

	/**
	 * Inserisce l'immagine del quotidiano nella tabella delle immagini. 
	 * 
	 * @param String name
	 */
	public void importImmagine(String name);
	
	/**
	 * Chiama la stored procedure P_INTERFACCE.P_IMPORTA_FILE per l'importazione
	 * del file del DL.
	 * 
	 * @param String name
	 * @param List<String> list
	 * @return ImportazioneFileDlResultDto
	 */
	public ImportazioneFileDlResultDto importaFileDl(String name, List<String> list);
	
	/**
	 * Chiama la stored procedure P_INFORIV_IMPORTA.P_IMPORTA_FILE per l'importazione
	 * del file del DL.
	 * 
	 * @param String name
	 * @param List<String> list
	 * @return ImportazioneFileDlResultDto
	 */
	public ImportazioneFileDlResultDto importaFileDlInforiv(String name, List<String> list);
	
	/**
	 * Inserisce l'immagine del quotidiano nella tabella della pubblicazioni. 
	 * 
	 * @param String name
	 */
	public void importImmagineMiniaturaQuotidiano(File fileImg, String titolo) throws IOException, ImageImportException;
	
	/**
	 * Inserisce l'immagine del periodico nella tabella della pubblicazioni. 
	 * 
	 * @param String name
	 */
	public void importImmagineMiniaturaPeriodico(File fileImg, String titolo) throws IOException;

	/**
	 * @param name
	 */
	public void addWatermarkToImage(File file) throws IOException;

	/**
	 * @param codiceDl
	 * @return
	 */
	public AnagraficaAgenziaVo getAgenziaByCodice(Integer codiceDl);
	
	/**
	 * @param codiceDlWeb
	 * @return
	 */
	public AnagraficaAgenziaVo getAgenziaByCodiceDlWeb(Integer codiceDlWeb);
	
	/**
	 * @param coddl
	 * @param codRivDl
	 * @return
	 */
	public EdicolaDto getEdicolaByCodRivDl(Integer coddl, Integer codRivDl);

	
}
