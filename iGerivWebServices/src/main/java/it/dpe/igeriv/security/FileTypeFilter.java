package it.dpe.igeriv.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Classe che serve per validare l'email che è stata inviato all'email del cliente.
 * 
 * @author romanom
 *
 */
@Component("fileTypeFilter")
public class FileTypeFilter extends GenericFilterBean {
	
	@SuppressWarnings("unused")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		filterChain.doFilter(request, response);
	}
	

}
