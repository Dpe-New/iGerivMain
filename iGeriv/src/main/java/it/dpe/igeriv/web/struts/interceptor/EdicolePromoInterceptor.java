package it.dpe.igeriv.web.struts.interceptor;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.web.actions.BaseAction;
import it.dpe.igeriv.web.resources.ExposablePropertyPaceholderConfigurer;

/**
 * Struts Interceptor per le edicole promozionali
 * Rimanda alla pagina precedente, e da' una avviso all'utente, se la action richiesta non è compresa tra quelle
 * autorizzate nella chiave igeriv.edicole.promo.actions.allowed del file iGeriv.properties
 * 
 * @author romanom
 *
 */
public class EdicolePromoInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;
	private final List<String> actionsAllowedEdicolePromo;
	
	public EdicolePromoInterceptor() {
		String strActionsAllowedEdicolePromo = SpringContextManager.getSpringContext().getBean(ExposablePropertyPaceholderConfigurer.class).getResolvedProps().get("igeriv.edicole.promo.actions.allowed");
		this.actionsAllowedEdicolePromo = Arrays.asList(strActionsAllowedEdicolePromo.split(","));
	}
	
	@SuppressWarnings("unused")
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		if (invocation.getAction() instanceof BaseAction) {
			if (SecurityContextHolder.getContext() != null 
				&& SecurityContextHolder.getContext().getAuthentication() != null 
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserAbbonato
				&& ((UserAbbonato) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEdicolaPromo()) {
				
				BaseAction action = (BaseAction) invocation.getAction();
				HttpServletRequest request = action.getHttpServletRequest();
				UserAbbonato ua = (UserAbbonato) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				HttpServletRequest req = (HttpServletRequest) request;
				boolean isAjaxRequest = req.getHeader("X-Requested-With") != null;
				String requestURI = req.getRequestURI();
				String currentAction = requestURI.replaceAll(req.getContextPath(), "").replaceAll("/", "");
				if (requestURI.endsWith(".action") && !isActionAllowed(req, currentAction)) {
					if (!isAjaxRequest) {
						ServletActionContext.getResponse().sendRedirect(req.getHeader("referer").contains("?ana=true") ? req.getHeader("referer") : req.getHeader("referer") + "?ana=true");
						return null;
					} else {
						if (req.getHeader("Auto-Call") != null && req.getHeader("Auto-Call").equals("true")) {
							return null;
						}
						req.setAttribute("igerivException", IGerivConstants.START_EXCEPTION_TAG + IGerivMessageBundle.get("igeriv.action.not.allowed") + IGerivConstants.END_EXCEPTION_TAG);
						return "IGerivRuntimeException"; 
					}
				}
			}
		}
		return invocation.invoke();
	}

	/**
	 * @param req
	 * @param currentAction
	 * @return
	 */
	private boolean isActionAllowed(HttpServletRequest req, String currentAction) {
		boolean isXls = (req.getAttribute("BollaControlloTab_ev") != null && req.getAttribute("BollaControlloTab_ev").toString().toUpperCase().equals("XLS"))
				|| (req.getAttribute("BollaResaTab_ev") != null && req.getAttribute("BollaResaTab_ev").toString().toUpperCase().equals("XLS"));
		String baseAction = currentAction.contains("_") ? currentAction.substring(0, currentAction.indexOf("_") + 1) : "";
		return !isXls && (actionsAllowedEdicolePromo.contains(currentAction) || actionsAllowedEdicolePromo.contains(baseAction) || currentAction.contains("Filter") || currentAction.contains("Template"));
	}
	
}
