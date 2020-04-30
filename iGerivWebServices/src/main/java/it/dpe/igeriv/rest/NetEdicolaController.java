package it.dpe.igeriv.rest;

import it.dpe.igeriv.service.NetEdicolaRemote;

import java.util.List;

import models.EdicolaMappaDto;
import models.ListEdicolaMappa;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;

@Controller
@RequestMapping("/edicole")
public class NetEdicolaController {
	@Autowired
	NetEdicolaRemote netEdicolaRemote;
	
	@RequestMapping(value="/mappe", method=RequestMethod.GET)
	public ModelAndView getEdicoleMappaJ(@RequestParam String capDa, @RequestParam String capA) {
		if (!Strings.isNullOrEmpty(capDa) && !Strings.isNullOrEmpty(capA) && NumberUtils.isNumber(capDa) && NumberUtils.isNumber(capA)) {
			List<EdicolaMappaDto> list = netEdicolaRemote.getEdicoleMappa(new Integer(capDa), new Integer(capA));
			ListEdicolaMappa ret = new ListEdicolaMappa();
			ret.setEdicole(list); 
			return new ModelAndView("orderViewJson", "edicole", ret);
		}
		return null;
	}
	
	/*@RequestMapping(value="/mappe", method=RequestMethod.GET)
	public ModelAndView getEdicoleMappa(@RequestParam String capDa, @RequestParam String capA) {
		if (!Strings.isNullOrEmpty(capDa) && !Strings.isNullOrEmpty(capA) && NumberUtils.isNumber(capDa) && NumberUtils.isNumber(capA)) {
			List<EdicolaMappaDto> list = netEdicolaRemote.getEdicoleMappa(new Integer(capDa), new Integer(capA));
			ListEdicolaMappa ret = new ListEdicolaMappa();
			ret.setEdicole(list); 
			return new ModelAndView("edicoleView", "edicole", ret);
		}
		return null;
	}*/

	public NetEdicolaRemote getNetEdicolaRemote() {
		return netEdicolaRemote;
	}

	public void setNetEdicolaRemote(NetEdicolaRemote netEdicolaRemote) {
		this.netEdicolaRemote = netEdicolaRemote;
	}
	
	
}
