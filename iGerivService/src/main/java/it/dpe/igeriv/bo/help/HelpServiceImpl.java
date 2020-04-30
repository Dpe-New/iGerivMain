package it.dpe.igeriv.bo.help;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.vo.HelpVo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("HelpService")
class HelpServiceImpl extends BaseServiceImpl implements HelpService {
	private final HelpRepository repository;
	
	@Autowired
	HelpServiceImpl(HelpRepository repository) {
		super(repository);
		this.repository = repository;
	}
	
	@Override
	public List<HelpVo> getAllHelp() {
		return repository.getAllHelp();
	}

	@Override
	public List<HelpVo> getHelpByCod(Integer[] codHelp) {
		return repository.getHelpByCod(codHelp);
	}
	

}
