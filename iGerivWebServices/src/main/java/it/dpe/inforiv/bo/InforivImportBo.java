package it.dpe.inforiv.bo;

import it.dpe.igeriv.dto.Edicola;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.ImportazioneInforivReplyDto;

import java.util.List;

import models.ResultSaveDto;

/**
 * @author romanom
 * 
 */
public interface InforivImportBo {
	
	/**
	 * Importa un'edicola inforiv (chiamato dal web service) 
	 * Questo metodo è transazionale. 
	 * 
	 * @param edicola
	 * @throws Exception
	 */
	public ImportazioneInforivReplyDto importEdicolaInforiv(Edicola edicola);
	
	
	/**
	 * Importa un'edicola inforiv (chiamato dal web service) 
	 * Questo metodo è transazionale. 
	 * 
	 * @param edicola
	 * @throws Exception
	 */
	public ResultSaveDto importEdicolaNetEdicola(Edicola edicola);
	
	/**
	 * @return
	 */
	public List<EdicolaDto> getEdicoleInforiv();
}
