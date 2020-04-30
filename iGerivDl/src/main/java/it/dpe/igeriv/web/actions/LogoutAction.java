package it.dpe.igeriv.web.actions;

import lombok.Getter;
import lombok.Setter;
import it.dpe.igeriv.util.IGerivConstants;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Classe action che esegue il logout del sistema.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Scope("prototype")
@Component("logoutAction")
public class LogoutAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;

	public String execute() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(null);
		return IGerivConstants.REDIRECT;
	}

}
