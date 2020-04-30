package it.dpe.igeriv.cleaner.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Classe di utility utilizzata da Spring per rendere disponibile il contesto.
 * 
 * @author romanom
 *
 */
public class IGerivCleanerApplicationContextProvider implements ApplicationContextAware {
	
	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		// Wiring the ApplicationContext into a static method
		IGerivCleanerAppContext.setApplicationContext(ctx);
	}
	
}
