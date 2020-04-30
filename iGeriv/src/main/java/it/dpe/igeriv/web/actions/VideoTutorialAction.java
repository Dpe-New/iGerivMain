package it.dpe.igeriv.web.actions;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.help.HelpService;
import it.dpe.igeriv.vo.HelpVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per le mancanze.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("videoTutorialAction")
@SuppressWarnings({"rawtypes"}) 
public class VideoTutorialAction extends RestrictedAccessBaseAction implements State {
	
	private static final long serialVersionUID = 1L;
	private String filterTitle;
	private final String crumbName = getText("igeriv.menu.900");
	private final HelpService helpService;
	private String listCodHelp;
	private List<HelpVo> listVideoHelp;
	
	
	
	public VideoTutorialAction() {	
		this(null,null);
	}
	@Autowired
	public VideoTutorialAction(@Value("${list.codice.help.pagina.video.tutorial}") String listCodHelp,HelpService helpService) {	
		this.listCodHelp = listCodHelp;
		this.helpService = helpService;
	}
	
		
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showVideoTutorial() {
		filterTitle = getText("igeriv.menu.900");
		String[] str_listCodiciHelp;
		
		if (!Strings.isNullOrEmpty(listCodHelp)) {
			str_listCodiciHelp = listCodHelp.split(",");
			if (str_listCodiciHelp != null && str_listCodiciHelp.length > 0) {
				int i = 0;
				Integer[] int_listCodiciHelp = new Integer[str_listCodiciHelp.length];
				for (String ch : str_listCodiciHelp) {
					int_listCodiciHelp[i++]=Integer.valueOf(ch);
				}
				listVideoHelp = helpService.getHelpByCod(int_listCodiciHelp);
				requestMap.put("listVideoHelp", listVideoHelp);
				
			}
		}
			
			
		return SUCCESS;
	}

	@Override
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return null;
	}

	@Override
	public void saveParameters(Context arg0, String arg1, Map arg2) {
		
	}
	
	
	
}
