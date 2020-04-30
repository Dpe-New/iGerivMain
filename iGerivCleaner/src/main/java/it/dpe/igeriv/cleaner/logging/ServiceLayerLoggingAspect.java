package it.dpe.igeriv.cleaner.logging;

import it.dpe.igeriv.cleaner.exception.IGerivBusinessException;
import it.dpe.igeriv.cleaner.exception.IGerivRuntimeException;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Classe gestita dallo Spring AOP e configurata nell'applicationContext.xml
 * che esegue il log4j nel modo definito dalla configurazione.
 * 
 * @author romanom
 *
 */
public class ServiceLayerLoggingAspect {
	private final Logger log = Logger.getLogger(getClass());
	
	public Object log(ProceedingJoinPoint call) throws Throwable {
		log.debug("from logging aspect: entering method ["
				+ call.toShortString() + "]" + ((call.getArgs() != null && call.getArgs().length > 0) ? " with param:" + call.getArgs()[0] : ""));
		
		Object point = null;
		try {
			point = call.proceed();
		} catch(IGerivBusinessException e) {
			throw e;
		} catch(IGerivRuntimeException e) {
			throw e; 
		} catch (Exception e) {
			log.error(e);
			throw e;	
		}

		log.debug("from logging aspect: exiting method ["
				+ call.toShortString() + "with return as:" + point);

		return point;
	}
}
