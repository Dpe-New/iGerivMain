package it.dpe.igeriv.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Fa il redirect alla action di login corretta in base alla url
 * 
 * @author mromano
 *
 */
public class BaseUrlFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
	    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
	    if (!httpResponse.isCommitted()) {
	    	if (httpRequest.getRequestURI().endsWith(httpRequest.getContextPath()) || httpRequest.getRequestURI().endsWith(httpRequest.getContextPath() + "/")) {
	    		httpResponse.sendRedirect(httpRequest.getContextPath() + "/home.action");
	    	} else {
	            filterChain.doFilter(servletRequest, servletResponse);
	        }
	    }
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
