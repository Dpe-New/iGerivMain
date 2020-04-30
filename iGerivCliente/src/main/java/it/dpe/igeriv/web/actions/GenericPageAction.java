package it.dpe.igeriv.web.actions;

import org.springframework.stereotype.Component;

/**
 * Classe action generica che ritorna la view senza eseguire nessuna logica.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Component("genericPageAction")
public class GenericPageAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String param;
	
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
	
}
