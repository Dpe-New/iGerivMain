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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.google.common.base.Strings;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.MenuModuloVo;

/**
 * Impedisce alle edicole devietti/todis con il flag testmo9206 attivo di entrare su igeriv (devono estrare su edismart), 
 * e modifica gli array di codFiegDl e crivw settati nell’utente, a seconda della url chiamata.
 * 
 * @author romanom
 *
 */
@Component("DeviettiTodisEdicoleFilter")
public class DeviettiTodisEdicoleFilter extends GenericFilterBean {
	private final String[] uriListFilterDlSingolo;
	private final String[] uriListFilterDlSingoloTest;
	
	@Autowired
	public DeviettiTodisEdicoleFilter(@Value("${uri.devietti.todis.filter.dl.singolo}") String uriListFilterDlSingolo, @Value("${uri.devietti.todis.filter.dl.singolo.test}") String uriListFilterDlSingoloTest) {
		this.uriListFilterDlSingolo = !Strings.isNullOrEmpty(uriListFilterDlSingolo) ? uriListFilterDlSingolo.split(",") : new String[]{""};
		this.uriListFilterDlSingoloTest = !Strings.isNullOrEmpty(uriListFilterDlSingoloTest) ? uriListFilterDlSingoloTest.split(",") : new String[]{""};
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		boolean isAuth = (SecurityContextHolder.getContext() != null 
				&& SecurityContextHolder.getContext().getAuthentication() != null 
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserAbbonato
				//&& ((UserAbbonato) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEdicolaDeviettiTodis()
				&& request.getParameter(IGerivConstants.PARAM_COD_DL_SELECT) == null
				&& request instanceof HttpServletRequest);
		UserAbbonato ua = null;
		Integer[] arrCodDl = null;
		Integer[] arrId = null;
		boolean uriAllowed = false;
		HttpServletRequest req = (HttpServletRequest) request;
		req.setAttribute("hasGestioneWebClienti", true);
		if (isAuth) {
			ua = (UserAbbonato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			/*TOGLIERE IL COMMENTO PRIMA DEL RILASCIO IN PRODUZIONE
			
			if(req.getContextPath().equals(IGerivConstants.CONTEXT_PATH_EDISMART)){
				// 05/11/2015 - Impedisce alle edicole di entrare su edismart 
				if(!ua.getEdicolaDeviettiTodisCavaglia()){
					((HttpServletResponse) response).sendRedirect(IGerivConstants.J_SPRING_SECURITY_LOGOUT);
					return;
				}
			}else{
				// 05/11/2015 - Impedisce alle edicole devietti/todis di entrare su igeriv 
				if(ua.getEdicolaDeviettiTodisCavaglia()){
					((HttpServletResponse) response).sendRedirect(IGerivConstants.J_SPRING_SECURITY_LOGOUT);
					return;
				}
			}
			*/
			/* FINE COMMENTO PER LOCALHOST */
			
//			
//			if (ua.getEdicolaTestPerModifiche() && !ua.getEdicolaDeviettiTodis() && !req.getContextPath().equals(IGerivConstants.CONTEXT_PATH_EDISMART)) {
//				
//				// 07/10/2014 - Impedisce alle edicole devietti/todis di entrare su igeriv 
//				((HttpServletResponse) response).sendRedirect(IGerivConstants.J_SPRING_SECURITY_LOGOUT);
//				return;
//			}
//			if (ua.getEdicolaDeviettiTodis() && !req.getContextPath().equals(IGerivConstants.CONTEXT_PATH_EDISMART)) {
//				
//				// 07/10/2014 - 
//				((HttpServletResponse) response).sendRedirect(IGerivConstants.J_SPRING_SECURITY_LOGOUT);
//				return;
//			}
//			
			if (ua.getEdicolaTestPerModifiche()) {
				req.setAttribute("hasGestioneWebClienti", false);
			}
			String currentUri = req.getRequestURI();
			if (!ua.getEdicolaTestPerModifiche()) {
				List<Object> flatten = flatten(ua.getModuli());
					// 16/11/2016 CHIMINELLI o CDl PER IL PROFILO LITHE 
					if(!ua.getEdicolaIlChiosco() && !ua.isEdicolaCDLBologna()){
						
						// vittorio 09/11/2018 ????
						// Vittorio 26/08/2020
						//if (ua.getModuli().get(0).size() > 4) {
						//	ua.getModuli().get(0).get(4).remove(selectFirst(flatten, having(on(MenuModuloVo.class).getTitolo(), equalTo(IGerivMessageBundle.get("igeriv.menu.88")))));
						//}
					}
				for (String uri : uriListFilterDlSingolo) {
					if (currentUri.endsWith(uri)) {
						uriAllowed = true;
						break;
					}
				}
			} else if (ua.getEdicolaTestPerModifiche()) {
				for (String uri : uriListFilterDlSingoloTest) {
					if (currentUri.endsWith(uri)) {
						uriAllowed = true;
						break;
					}
				}
			}
			if (uriAllowed) {
				if (ua.getArrCodFiegDl().length > 1) {
					arrCodDl = ua.getArrCodFiegDl();
				}
				if (ua.getArrCodFiegDl().length > 1) {
					arrId = ua.getArrId();
				}
				ua.setArrCodFiegDl(new Integer[]{ua.getCodFiegDl()});
	 			ua.setArrId(new Integer[]{ua.getId()});
			}
		}
		try {
			filterChain.doFilter(request, response);
		} finally {
			if (isAuth && uriAllowed && ua != null && arrCodDl != null && arrId != null) {
				ua.setArrCodFiegDl(arrCodDl);
				ua.setArrId(arrId);
			}
		}
	}
	
}
