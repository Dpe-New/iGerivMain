package it.dpe.inforiv.bo;

import it.dpe.igeriv.vo.AnagraficaAgenziaVo;

import java.io.File;

public interface InforivExportFileBo {
	 
	/**
	 * 
	 */
	public void exportInforiv();
	
	/**
	 * Esportazione dati e creazione in locale dei files  
	 */
	public void exportInforivFileLocal(AnagraficaAgenziaVo agenzia,File dirOutputLocalDL,File dirOutputBKPLocalDL);
	
	/**
	 * 
	 * @param agenzia
	 * @param codEdicolaWeb
	 * @param dirOutputLocalDL
	 * @param dirOutputBKPLocalDL
	 */
	public void exportInforivFileLocal(AnagraficaAgenziaVo agenzia,Integer codEdicolaWeb,File dirOutputLocalDL,File dirOutputBKPLocalDL);

	/**
	 * 
	 * @param coddl
	 * @param codRivenditaDL
	 * @param file
	 */
	public void exportInforivFileFtp(Integer coddl, Integer codRivenditaDL, File file, String bckDir);
	
}
