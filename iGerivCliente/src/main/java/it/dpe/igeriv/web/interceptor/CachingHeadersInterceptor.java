package it.dpe.igeriv.web.interceptor;

import it.dpe.igeriv.util.IGerivConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Classe interceptor che definisce gli headers da inviare al browser dell'utente.
 * Se il browser è IE, niente può essere cachato.
 * 
 * @author romanom
 *
 */
public class CachingHeadersInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		final ActionContext context = invocation.getInvocationContext();
		
		HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
		String userAgent = request.getHeader(IGerivConstants.HEADER_USER_AGENT);
		
		if (userAgent != null && userAgent.contains("MSIE")) {
			HttpServletResponse response = (HttpServletResponse) context.get(StrutsStatics.HTTP_RESPONSE);
			if (response != null) {
				response.setHeader("Cache-Control", "no-cache, no-store");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Expires", "-1");
			}
		}
		
		return invocation.invoke();
	} 

}
