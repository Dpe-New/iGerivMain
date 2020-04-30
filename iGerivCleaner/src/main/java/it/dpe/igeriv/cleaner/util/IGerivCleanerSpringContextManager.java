package it.dpe.igeriv.cleaner.util;
import org.springframework.context.ApplicationContext;

/**
 * Classe chde definisce dei metodi di utilità che ritornare gli Spring beans 
 * o settare un contesto Spring.
 * 
 * @author romanom
 *
 */
public class IGerivCleanerSpringContextManager {
	
    /**
     * Private constructer.
     */
    private IGerivCleanerSpringContextManager() {
    }

    /**
     * Initializes and returns the Spring Context.
     * 
     * @return ApplicationContext instance.
     */
    public static synchronized ApplicationContext getSpringContext() {
        return IGerivCleanerAppContext.getApplicationContext();
    }
    
    /**
	 * Utility method for fetching beans from Spring Bean Factory.
	 * @param nameOfService
	 * @return
	 */
	public static Object getService(String nameOfBean){
		ApplicationContext context = IGerivCleanerAppContext.getApplicationContext();
        Object service = context.getBean(nameOfBean);
		return service;
	}
    
   
    /**
     * Method to set mock context for Junits.
     */
    public static void setMockContext(ApplicationContext context){
    	IGerivCleanerAppContext.setApplicationContext(context);
    }
    
}
