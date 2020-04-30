package it.dpe.igeriv.bo.note;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.vo.NoteRivenditaPerCpuVo;
import it.dpe.igeriv.vo.NoteRivenditaVo;


/**
 * Interfaccia account
 * 
 * @author mromano
 *
 */
public interface NoteService extends BaseService {

	/**
	 * @param idtn
	 * @param codEdicola
	 * @return
	 */
	public NoteRivenditaVo getNoteRivenditaVo(Integer idtn, Integer codEdicola);
	
	/**
	 * @param cpu
	 * @param codEdicola
	 * @return
	 */
	public NoteRivenditaPerCpuVo getNoteRivenditaPerCpuVo(Integer cpu, Integer codEdicola);
	
}
