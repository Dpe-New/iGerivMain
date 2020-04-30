package it.dpe.igeriv.web.listener;

import it.dpe.igeriv.resources.IGerivMessageBundle;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

/**
 * Classe che ascolta l'oggetto javax.servlet.ServletContext,
 * esegue operazioni di inizializzazione e setta variabili 
 * nel contesto "application".
 * 
 * @author romanom
 *
 */
@Component("IGerivServletContextAware")
public class IGerivServletContextAware implements ServletContextAware {
	 
	@Override
	public void setServletContext(ServletContext arg0) {
		IGerivMessageBundle.initialize();
	}
	
}
