package it.dpe.igeriv.web.actions;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.util.IGerivConstants;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action generica che ritorna la view senza eseguire nessuna logica.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("bannerAction")
public class BannerAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private String actionName;
	
	public String execute() throws Exception {
		// TODO 
		// Loggare il "click" sul banner nel DB
		return IGerivConstants.REDIRECT;
	}
	
}
