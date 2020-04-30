package it.dpe.igeriv.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Classe che verifica se la session è ancora attiva, 
 * e se si tratta di una richiesta ajax ritorna un codice di errore gestito nel Javascript.
 * 
 * @author romanom
 */
@Component("sessionCheckFilter")
public class SessionCheckFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null || context.getAuthentication() == null) {
			String requestType = ((HttpServletRequest) request).getHeader("X-Requested-With");
			if (requestType != null && requestType.equals("XMLHttpRequest")) {
				((HttpServletResponse)response).setStatus(601);
				((HttpServletResponse)response).getOutputStream().flush();
				((HttpServletResponse)response).getOutputStream().close();
				return;
			} 
		}
		filterChain.doFilter(request, response);
	}

}
