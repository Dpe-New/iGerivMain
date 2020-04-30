package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.vo.BaseVo;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action per la gestione delle vendite.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Scope("prototype")
@Component("GeneralRpcAction")
public class GeneralRpcAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements ServletContextAware {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void validate() {
	}

	@Override
	public void setServletContext(ServletContext context) {
		this.context = context;
	}
	
}
