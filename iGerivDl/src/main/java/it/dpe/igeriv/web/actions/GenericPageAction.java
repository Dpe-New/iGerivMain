package it.dpe.igeriv.web.actions;

import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;

/**
 * Classe action generica che ritorna la view senza eseguire nessuna logica.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Component("genericPageAction")
public class GenericPageAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String param;
	
	public String execute() throws Exception {
		return SUCCESS;
	}
	
}
