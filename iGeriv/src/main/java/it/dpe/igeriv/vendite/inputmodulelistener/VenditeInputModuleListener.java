package it.dpe.igeriv.vendite.inputmodulelistener;

import it.dpe.igeriv.dto.VenditeCardResultDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.exception.IGerivBusinessException;

/**
 * Interfaccia delle classi di servizi delle vendite.
 * 
 * @author romanom
 * 
 */
public interface VenditeInputModuleListener {

	public VenditeCardResultDto execute(VenditeParamDto params) throws IGerivBusinessException;
	
}
