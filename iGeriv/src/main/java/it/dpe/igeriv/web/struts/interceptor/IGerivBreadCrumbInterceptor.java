package it.dpe.igeriv.web.struts.interceptor;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.containsString;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;

import com.opensymphony.xwork2.ActionInvocation;

import it.dpe.igeriv.web.actions.BaseAction;

public class IGerivBreadCrumbInterceptor extends BreadCrumbInterceptor {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		boolean isAjaxRequest = false;
		boolean isPdfXlsExport = false;
		if (invocation.getAction() instanceof BaseAction) {
			BaseAction action = (BaseAction) invocation.getAction();
			isAjaxRequest = action.getHttpServletRequest().getHeader("X-Requested-With") != null;
			Map<String, String[]> parameterMap = action.getHttpServletRequest().getParameterMap();
			Set<String> keySet = parameterMap.keySet();
			List<String> keys = select(keySet, having(on(String.class), containsString("_ev")));
			for (String key : keys) {
				if ((parameterMap.get(key) != null && parameterMap.get(key).length > 0) && (parameterMap.get(key)[0].equals("pdf") || parameterMap.get(key)[0].equals("xls") || parameterMap.get(key)[0].equals("csv"))) {
					isPdfXlsExport = true;
					break;
				}
			}
		}
		if (!isAjaxRequest && !isPdfXlsExport) {
			return super.intercept(invocation);
		}
		return invocation.invoke();
	}
}
