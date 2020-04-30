package it.dpe.igeriv.web.actions;

import lombok.Getter;
import lombok.Setter;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action che ritorna la home page.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("homeAction")
public class HomeAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private final String crumbName = getText("igeriv.home");
	
	public String execute() throws Exception {
		return SUCCESS;
	}

	public String getTitle() {
		return super.getTitle() + getText("igeriv.home");
	}

}
