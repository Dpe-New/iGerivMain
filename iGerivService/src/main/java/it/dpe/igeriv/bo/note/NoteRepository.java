package it.dpe.igeriv.bo.note;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.vo.NoteRivenditaPerCpuVo;
import it.dpe.igeriv.vo.NoteRivenditaVo;



interface NoteRepository extends BaseRepository {
	
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
