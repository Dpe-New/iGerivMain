package it.dpe.igeriv.web.filter;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;

/**
 * Personalizza lo stile per edismart (igeriv per devietti/todis).
 * 
 * @author romanom
 *
 */
@Component("DeviettiTodisStyleFilter")
public class DeviettiTodisStyleFilter extends GenericFilterBean {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if ((request instanceof HttpServletRequest && 
				((SecurityContextHolder.getContext() != null 
				&& SecurityContextHolder.getContext().getAuthentication() != null 
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserAbbonato
				&& (((UserAbbonato) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEdicolaDeviettiTodis())
				&& ((UserAbbonato) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEdicolaTestPerModifiche())
				|| (((HttpServletRequest) request).getContextPath()).equals("/edismart")))) {
			request.setAttribute(IGerivConstants.REQUEST_ATTR_STYLE_SUFFIX, IGerivConstants.REQUEST_ATTR_STYLE_SUFFIX_DEVIETTI_TODIS);
			if (((HttpServletRequest) request).getSession() != null && ((HttpServletRequest) request).getSession().getServletContext() != null) {
				((HttpServletRequest) request).getSession().getServletContext().setAttribute("poweredBy", MessageFormat.format(IGerivMessageBundle.get("dpe.powered.by.dvtd"), "" + Calendar.getInstance().get(Calendar.YEAR)));
			}
		} else {
			request.setAttribute(IGerivConstants.REQUEST_ATTR_STYLE_SUFFIX, "");
		}
		filterChain.doFilter(request, response);
	}
	
}
