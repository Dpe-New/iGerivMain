package it.dpe.igeriv.bo.note;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.vo.NoteRivenditaPerCpuVo;
import it.dpe.igeriv.vo.NoteRivenditaVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("NoteService")
class NoteServiceImpl extends BaseServiceImpl implements NoteService {
	private final NoteRepository repository;
	
	@Autowired
	NoteServiceImpl(NoteRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public NoteRivenditaVo getNoteRivenditaVo(Integer idtn, Integer codEdicola) {
		return repository.getNoteRivenditaVo(idtn, codEdicola);
	}

	@Override
	public NoteRivenditaPerCpuVo getNoteRivenditaPerCpuVo(Integer cpu, Integer codEdicola) {
		return repository.getNoteRivenditaPerCpuVo(cpu, codEdicola);
	}

	

}
