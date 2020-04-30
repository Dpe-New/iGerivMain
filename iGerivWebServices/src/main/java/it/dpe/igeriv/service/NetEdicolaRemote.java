package it.dpe.igeriv.service;

import it.dpe.igeriv.dto.Edicola;

import java.util.List;

import models.AbbonatoDto;
import models.EdicolaDto;
import models.EdicolaMappaDto;
import models.ResultSaveDto;


/**
 * Interfaccia per la gestione dell'anagrafica dei clienti
 * E' utilizzata anche dalla pagina pubblica di RTAE. 
 * 
 * @author mromano
 *
 */
public interface NetEdicolaRemote {
	
	/**
	 * Inserisce/aggiorna nel db il clente lettore abbonato
	 * @param abbonato
	 */
	public ResultSaveDto saveCliente(AbbonatoDto abbonato);
	
	/**
	 * @param capA 
	 * @param capDa 
	 * @return
	 */
	public List<EdicolaMappaDto> getEdicoleMappa(Integer capDa, Integer capA);

	/**
	 * @return
	 */
	public EdicolaDto getEdicola(Integer codEdicola);
	
	/**
	 * @return
	 */
	public ResultSaveDto saveEdicola(Edicola codEdicola);

}
