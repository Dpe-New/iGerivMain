package it.dpe.logging;

import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.IGerivGenericError;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
		} catch (AuthenticationException e) {
			throw e;
		} catch (DataIntegrityViolationException e) {
			throw e;
		} catch (AccessDeniedException e) {
			throw e;
		} catch (Throwable e) {
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = requestAttributes != null && requestAttributes instanceof ServletRequestAttributes ? ((ServletRequestAttributes) requestAttributes).getRequest() : null;
			HttpSession session = request != null ? request.getSession() : null;
			if (requestAttributes != null && request != null && session != null && session.getAttribute("ud") != null && session.getAttribute("ud") instanceof UserAbbonato) {
				UserAbbonato user = (UserAbbonato) session.getAttribute("ud");
				String requestURI = request.getRequestURI() != null ? request.getRequestURI() : "";
				String msg = IGerivMessageBundle.get("igeriv.errore.fatale.generico") != null ? IGerivMessageBundle.get("igeriv.errore.fatale.generico") : "Errore fatale iGeriv - Codice Web Rivendita : {0} - Codice DL : {1} - URI richiesta : {2}";
				String codEdicola = user.getCodDpeWebEdicola() != null ? user.getCodDpeWebEdicola().toString() : "";
				String codFiegDl = user.getCodFiegDl() != null ? user.getCodFiegDl().toString() : "";
				e = new IGerivGenericError(MessageFormat.format(msg, codEdicola, codFiegDl, requestURI), e);
			}
			log.error("Errore fatale iGeriv", e);
			throw e;
		}

		log.debug("from logging aspect: exiting method ["
				+ call.toShortString() + "] with return as:" + point);

		return point;
	}
}
