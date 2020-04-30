package it.dpe.inforiv.bo;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.ImportazioneInforivReplyDto;

import java.io.File;
import java.util.List;

/**
 * @author romanom
 * 
 */
public interface InforivImportBo {
	
	/**
	 * Importa il file inforiv per edicola.
	 * 
	 * @param file
	 */
	public void impInforiv(File file) throws Exception;
	
	/**
	 * Importa un'edicola inforiv (chiamato dal web service) 
	 * Questo metodo è transazionale. 
	 * 
	 * @param edicola
	 * @throws Exception
	 */
	public ImportazioneInforivReplyDto importEdicolaInforiv(EdicolaDto edicola) throws Exception;

	/**
	 * @return
	 */
	public List<EdicolaDto> getEdicoleInforiv();

	
}
