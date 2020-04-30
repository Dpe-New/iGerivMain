package it.dpe.igeriv.bo.help;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.vo.HelpVo;

import java.util.List;


interface HelpRepository extends BaseRepository {
	
	/**
	 * @return
	 */
	public List<HelpVo> getAllHelp();
	
	/**
	 * 
	 * @param codHelp
	 * @return List<HelpVo>
	 */
	public List<HelpVo> getHelpByCod(Integer[] codHelp);
	
}
