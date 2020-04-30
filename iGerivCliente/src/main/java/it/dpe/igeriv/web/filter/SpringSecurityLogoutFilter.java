package it.dpe.igeriv.web.filter;

import it.dpe.igeriv.bo.statistiche.StatisticheService;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.StatistichePagineVo;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.google.common.base.Strings;

public class SpringSecurityLogoutFilter extends LogoutFilter {
	private StatisticheService statisticheService;
	
	public SpringSecurityLogoutFilter(LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers) {
		super(logoutSuccessHandler, handlers);
	}
	
	public SpringSecurityLogoutFilter(String logoutSuccessUrl, LogoutHandler... handlers) {
		super(logoutSuccessUrl, handlers);
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		if (req instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) req;
			String requestUrl = httpServletRequest.getRequestURL() != null ? httpServletRequest.getRequestURL().toString() : "";
			if (!Strings.isNullOrEmpty(requestUrl) && requestUrl.contains("j_spring_security_logout")) {
				HttpSession session = httpServletRequest.getSession();
				Object attribute = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (statisticheService != null && attribute != null) {
					UserAbbonato user = (UserAbbonato) attribute;
					boolean hasProfiloEdicola = session.getAttribute("hasProfiloEdicola") != null && Boolean.parseBoolean(session.getAttribute("hasProfiloEdicola").toString());
					StatistichePagineVo ultimaPagina = statisticheService.getUltimaPagina(user.getId(), user.getCodUtente());
					if (ultimaPagina != null && hasProfiloEdicola) {
						ultimaPagina.setDataUscita(statisticheService.getSysdate());
						statisticheService.saveBaseVo(ultimaPagina);
					}
				}
			}
		}
		super.doFilter(req, res, chain);
	}

	public StatisticheService getStatisticheService() {
		return statisticheService;
	}

	public void setStatisticheService(StatisticheService statisticheService) {
		this.statisticheService = statisticheService;
	}

	

}
