package it.dpe.igeriv.web.actions;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.I18nInterceptor;

import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.visitor.ActionVisitor;

/**
 * Classe base per tutte le action che setta campi di utilità generale.
 * 
 * @author romanom
 *
 */
public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, SessionAware, ParameterAware, VisitableAction {
	private static final long serialVersionUID = 1L;
	private String localeString;
	private String actionName;
	private String requestUri;
	private String userName;
	private String language;
	private String country;
	private Locale currentLocale;
	protected Map<String, String[]> parameterMap;
	protected Map<String, Object> sessionMap;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		if (servletPath.indexOf("/") != -1) {
			servletPath = servletPath.substring(servletPath.indexOf("/") + 1);
		}
		if (request.getAttribute(DPEWebContants.SPRING_SECURITY_LAST_USERNAME) != null) {
			setUserName((String)request.getAttribute(DPEWebContants.SPRING_SECURITY_LAST_USERNAME));
		}
		setRequestUri(request.getRequestURI());
		setActionName(servletPath);
		this.request = request;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		sessionMap = session;
		try {
			setStrLocale();
		} catch (IOException e) {
			throw new IGerivRuntimeException(e);
		}
	}
	
	@Override
	public void setParameters(Map<String, String[]> parameters) {
		parameterMap = parameters;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLocaleString() {
		return localeString;
	}
	
	public void setLocaleString(String localeString) {
		this.localeString = localeString;
	}

	public String getActionName() {
		return actionName;
	}
	
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	private void setStrLocale() throws IOException {
		String strlocale = null;
		if (parameterMap != null && parameterMap.get("request_locale") != null && ((String[])parameterMap.get("request_locale")).length > 0) {
			String requestLocale = ((String[])parameterMap.get("request_locale"))[0];
			language = requestLocale.substring(0, requestLocale.indexOf(DPEWebContants.UNDERSCORE));
			country = requestLocale.substring(requestLocale.indexOf(DPEWebContants.UNDERSCORE) + 1);
			currentLocale = new Locale(language, country);
			sessionMap.put(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, currentLocale);
		} else if (sessionMap != null && sessionMap.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE) != null) {
			currentLocale = ((Locale)sessionMap.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE));
			language = currentLocale.getLanguage();
			country = currentLocale.getCountry();
		} /*else {
			ExposablePropertyPaceholderConfigurer props = (ExposablePropertyPaceholderConfigurer) SpringContextManager.getService("igerivProperties");
			String setLocaleProp = props.getResolvedProps().get("default.locale");
			currentLocale = new Locale(setLocaleProp.substring(0, 2), setLocaleProp.substring(3));
			language = currentLocale.getLanguage();
			country = currentLocale.getCountry();
			sessionMap.put(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, currentLocale);
		}*/
		if (currentLocale != null) {
			strlocale = currentLocale.toString();
			setLocaleString(strlocale);
		}
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}

	public HttpServletRequest getHttpServletRequest() {
		return request;
	}
	
	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}
	
	
}
