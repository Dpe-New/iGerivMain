package it.dpe.igeriv.web.actions;

import java.text.MessageFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action per il login del sistema.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Scope("prototype")
@Component("loginAction")
public class LoginAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private String pwdSent;
	private String pwdChanged;
	private String loginType;
	@Value("${igeriv.env.deploy.name}")
	private String env;
	@Value("${igeriv.app.name}")
	private String appName;
	private String msgDpe; 
	
	@Override
	public void prepare() throws Exception {
		msgDpe = MessageFormat.format(getText("dpe.by"), "" + Calendar.getInstance().get(Calendar.YEAR));
		super.prepare();
	}
	
	public String execute() throws Exception {
		return SUCCESS;
	}

	public String getPwdSent() {
		return pwdSent;
	}

	public void setPwdSent(String pwdSent) {
		this.pwdSent = pwdSent;
	}

	public String getPwdChanged() {
		return pwdChanged;
	}

	public void setPwdChanged(String pwdChanged) {
		this.pwdChanged = pwdChanged;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	
	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getMsgDpe() {
		return msgDpe;
	}

	public void setMsgDpe(String msgDpe) {
		this.msgDpe = msgDpe;
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("login");
	}
	
}
