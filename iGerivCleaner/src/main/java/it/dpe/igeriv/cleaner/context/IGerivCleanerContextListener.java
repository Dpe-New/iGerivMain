package it.dpe.igeriv.cleaner.context;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import it.dpe.igeriv.cleaner.resources.IGerivMessageBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;

/**
 * Classe che ascolta l'oggetto javax.servlet.ServletContext,
 * esegue operazioni di inizializzazione e setta variabili 
 * nel contesto "application".
 * 
 * @author romanom
 *
 */
public class IGerivCleanerContextListener extends ContextLoaderListener {
	 
	private final Logger log = Logger.getLogger(getClass());
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		IGerivMessageBundle.initialize();
	}
	
	/**
     * Deregisters JDBC driver
     * 
     * Prevents Tomcat 7 from complaining about memory leaks.
     */
//    @Override
//    public void contextDestroyed(ServletContextEvent servletContextEvent) {
//        
//    	ClassLoader cl = Thread.currentThread().getContextClassLoader();
//        Enumeration<Driver> drivers = DriverManager.getDrivers();
//        while (drivers.hasMoreElements()) {
//            Driver driver = drivers.nextElement();
//            if (driver.getClass().getClassLoader() == cl) {
//                // This driver was registered by the webapp's ClassLoader, so deregister it:
//                try {
//                	log.info("Deregistering JDBC driver "+driver.toString());
//                    DriverManager.deregisterDriver(driver);
//                } catch (SQLException ex) {
//                    log.error("Error deregistering JDBC driver "+driver.toString()+" "+ex.getMessage());
//                }
//            } else {
//                // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
//                log.trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader");
//            }
//        }
//    }
    
}
