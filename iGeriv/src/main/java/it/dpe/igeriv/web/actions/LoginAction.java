package it.dpe.igeriv.web.actions;

import java.text.MessageFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.util.IGerivConstants;
import lombok.Getter;
import lombok.Setter;


/**
 * Classe action per il login del sistema.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("loginAction")
public class LoginAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private String env;
	private String appName;
	private String pwdSent;
	private String pwdChanged;
	private String loginType;
	private String msgDpe; 
	
	public LoginAction() {
		this.env = null;
		this.appName = null;
	}
	
	@Autowired
	public LoginAction(@Value("${igeriv.env.deploy.name}") String env, @Value("${igeriv.app.name}") String appName) {
		this.env = env;
		this.appName = appName;
	}
	
	@Override
	public void prepare() throws Exception {
		msgDpe = MessageFormat.format(getText("dpe.by" + (requestMap.get(IGerivConstants.REQUEST_ATTR_STYLE_SUFFIX) != null && !Strings.isNullOrEmpty(requestMap.get(IGerivConstants.REQUEST_ATTR_STYLE_SUFFIX).toString()) ? requestMap.get(IGerivConstants.REQUEST_ATTR_STYLE_SUFFIX).toString().replace("_", ".") : "")), "" + Calendar.getInstance().get(Calendar.YEAR));
		super.prepare();
	}
	
	public String execute() throws Exception {
		return SUCCESS;
	}

	public String getTitle() {
		return getTitolo() + getText("login");
	}
	
	private String getTitolo() {
		String titolo = getText("gp.titolo") + " ";
		if (request.getContextPath().equals(IGerivConstants.CONTEXT_PATH_CDLONLINE)) {
			titolo = "";
		} else if (request.getContextPath().equals(IGerivConstants.CONTEXT_PATH_EDISMART)) {
			titolo = getText("gp.titolo.edismart") + " ";
		}
		return titolo;
	}
	
}
