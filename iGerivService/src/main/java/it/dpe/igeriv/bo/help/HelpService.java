package it.dpe.igeriv.bo.help;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.vo.HelpVo;

import java.util.List;



/**
 * Interfaccia account
 * 
 * @author mromano
 *
 */
public interface HelpService extends BaseService {
	
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
