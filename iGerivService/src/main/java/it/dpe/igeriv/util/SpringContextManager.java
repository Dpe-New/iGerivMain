package it.dpe.igeriv.util;
import org.springframework.context.ApplicationContext;

/**
 * Classe chde definisce dei metodi di utilità che ritornare gli Spring beans 
 * o settare un contesto Spring.
 * 
 * @author romanom
 *
 */
public class SpringContextManager {
	
    /**
     * Private constructer.
     */
    private SpringContextManager() {
    }

    /**
     * Initializes and returns the Spring Context.
     * 
     * @return ApplicationContext instance.
     */
    public static synchronized ApplicationContext getSpringContext() {
        return AppContext.getApplicationContext();
    }
    
    /**
	 * Utility method for fetching beans from Spring Bean Factory.
	 * @param nameOfService
	 * @return
	 */
	public static Object getService(String nameOfBean){
		ApplicationContext context = AppContext.getApplicationContext();
        Object service = context.getBean(nameOfBean);
		return service;
	}
    
   
    /**
     * Method to set mock context for Junits.
     */
    public static void setMockContext(ApplicationContext context){
    	AppContext.setApplicationContext(context);
    }
    
}
