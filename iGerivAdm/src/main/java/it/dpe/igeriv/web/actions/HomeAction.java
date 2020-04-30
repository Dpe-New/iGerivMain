package it.dpe.igeriv.web.actions;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action che ritorna la home page.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Scope("prototype")
@Component("homeAction")
public class HomeAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	
	public String execute() throws Exception {
		return SUCCESS;
	}

	public String getTitle() {
		return super.getTitle() + getText("igeriv.home");
	}

}
