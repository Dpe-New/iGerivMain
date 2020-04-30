package it.dpe.igeriv.web.filter;

import static ch.lambdaj.Lambda.flatten;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectFirst;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.MenuModuloVo;

/**
 * Usato in ambiente Menta. Nasconde o mostra l’anagrafica dell'edicola subito dopo il login.
 * 
 * @author romanom
 *
 */
@Component("CheckAnagraficaRivenditaFilter")
public class CheckAnagraficaRivenditaFilter extends GenericFilterBean {

	private static final String REDIRECT_ACTION = "anagraficaRivendita_show.action";
	private static final String SAVE_ACTION = "anagraficaRivendita_save.action";
	private static final String AVVISO_ACTION_PREFIX = "avviso_";
	private static final Integer MENU_ID = 310;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		
		if (SecurityContextHolder.getContext() != null && 
			SecurityContextHolder.getContext().getAuthentication() != null && 
			SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
			SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null && 
			SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserAbbonato && 
			((UserAbbonato) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCodFiegDl().equals(IGerivConstants.COD_FIEG_MENTA)) {
			UserAbbonato userAbbonato = (UserAbbonato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			String requestURI = httpRequest.getRequestURI();
			
			if (requestURI.endsWith(".action")) {
				if (userAbbonato.getGestioneAnagraficaRivenditaObbligatoria() != null
					&& userAbbonato.getGestioneAnagraficaRivenditaObbligatoria()) {
					if (!requestURI.endsWith(REDIRECT_ACTION) && !requestURI.endsWith(SAVE_ACTION) && !requestURI.contains(AVVISO_ACTION_PREFIX)) {
						if (userAbbonato == null || !userAbbonato.getAnagraficaCompilata()) {
							((HttpServletResponse)response).sendRedirect(REDIRECT_ACTION);
							return;
						}
					}				
				}
				else {
					// devo eliminare dal menu l'anagrafica dell'edicola
					List<Object> flatten = flatten(userAbbonato.getModuli());
					
					MenuModuloVo anagraficaEdicolaModuloVo = selectFirst(flatten, having(on(MenuModuloVo.class).getId(), equalTo(MENU_ID)));
					
					if (anagraficaEdicolaModuloVo != null) {
						Integer idPadre = anagraficaEdicolaModuloVo.getIdModuloPadre();
						
						List<List<List<MenuModuloVo>>> moduli = userAbbonato.getModuli();
						for (List<List<MenuModuloVo>> firstLevel : moduli) {
							boolean found = false;
							MenuModuloVo modulo = firstLevel.get(0).get(0);
							if (modulo.getId().equals(idPadre)) {
								// Trovato il padre
								for (int i = 1, n = firstLevel.size(); i < n; i++) {
									modulo = firstLevel.get(i).get(0);
									if (modulo.getId().equals(MENU_ID)) {
										// Trovato il modulo
										firstLevel.remove(i);
										found = true;
										break;
									}
								}
							}
							if (found) {
								break;
							}
						}
					}
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
