package it.dpe.igeriv.web.actions;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe action che ritorna la home page.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("exceptionAction")
public class ExceptionAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	
	public String execute() throws Exception {
		return SUCCESS;
	}

	public String getTitle() {
		return super.getTitle() + getText("igeriv.home");
	}

}
