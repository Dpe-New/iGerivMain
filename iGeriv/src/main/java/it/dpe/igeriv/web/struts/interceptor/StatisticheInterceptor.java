package it.dpe.igeriv.web.struts.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.base.Strings;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import it.dpe.igeriv.bo.statistiche.StatisticheService;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.vo.StatistichePagineVo;

/**
 * Classe interceptor che popola le statistiche per le pagine delle edicole.
 *
 * @author romanom
 *
 */
@SuppressWarnings({"unchecked"})
public class StatisticheInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		final ActionContext context = invocation.getInvocationContext();
		if (context != null && context.getSession() != null) {
			Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (object != null && object instanceof UserAbbonato) {
				UserAbbonato user = (UserAbbonato) object;
				boolean hasProfiloEdicola = context.getSession().get("hasProfiloEdicola") != null && Boolean.parseBoolean(context.getSession().get("hasProfiloEdicola").toString());
				if (user.getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA) && hasProfiloEdicola) {
					/*
					 * VITTORIO 27/09/18 per ottimizzare performance
					HttpServletRequest httpServletRequest = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
					String queryString = httpServletRequest.getQueryString();
					if (Strings.isNullOrEmpty(queryString) || !queryString.contains("ispopup=true")) {
						String requestUrl = httpServletRequest.getRequestURL().toString();
						String page = requestUrl.substring(requestUrl.lastIndexOf("/") + 1, requestUrl.lastIndexOf(".action")).toLowerCase();
						Map<String, Integer> pageMonitorMap = (Map<String, Integer>) context.getApplication().get(IGerivConstants.PAGE_MONITOR_MAP);
						Integer codPagina  = getPageStatistiche(page, pageMonitorMap);
						if (codPagina != null && page.indexOf("save") == -1) {
							final StatisticheService igerivService = SpringContextManager.getSpringContext().getBean(StatisticheService.class);
							StatistichePagineVo ultimaPagina = igerivService.getUltimaPagina(user.getId(), user.getCodUtente());
							if (ultimaPagina != null && ultimaPagina.getDataUscita() == null && !ultimaPagina.getPagina().getCodPagina().equals(codPagina)) {
								ultimaPagina.setDataUscita(igerivService.getSysdate());
								igerivService.saveBaseVo(ultimaPagina);
								addNewStatistica(user, codPagina, igerivService);
							} else if (ultimaPagina == null || (ultimaPagina != null && !ultimaPagina.getPagina().getCodPagina().equals(codPagina))) {
								addNewStatistica(user, codPagina, igerivService);
							}
						}
					}
					*/
				}
			}
		}
		return invocation.invoke();
	}

	private void addNewStatistica(UserAbbonato user, Integer codPagina, StatisticheService igerivService) {
		StatistichePagineVo spvo = new StatistichePagineVo();
		spvo.setCodEdicola(user.getId());
		spvo.setCodUtente(user.getCodUtente());
		spvo.setCodPagina(codPagina);
		spvo.setDataIngresso(igerivService.getSysdate());
		igerivService.saveBaseVo(spvo);
	}

	/**
	 * @param page
	 * @param pageMonitorMap
	 * @return
	 */
	private Integer getPageStatistiche(String page, Map<String, Integer> pageMonitorMap) {
		for (String s : pageMonitorMap.keySet()) {
			if (page.contains(s)) {
				return pageMonitorMap.get(s);
			}
		}
		return null;
	}
	

}
