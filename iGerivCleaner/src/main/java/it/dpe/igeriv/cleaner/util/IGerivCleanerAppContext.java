package it.dpe.igeriv.cleaner.util;

import org.springframework.context.ApplicationContext;

/**
 * Classe di utility per ritornare il contesto Spring in modo statico, es:
 * AppContext.getApplicationContext()
 * 
 * @author romanom
 *
 */
public class IGerivCleanerAppContext {
	private static ApplicationContext ctx;

	/**
	 * Injected from the class "ApplicationContextProvider" which is
	 * automatically loaded during Spring-Initialization.
	 */
	public static void setApplicationContext(
			ApplicationContext applicationContext) {
		ctx = applicationContext;
	}

	/**
	 * Get access to the Spring ApplicationContext from everywhere in your
	 * Application.
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

}
