package it.dpe.igeriv.web.actions;

import java.text.MessageFormat;
import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action per il login del sistema.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Scope("prototype")
@Component("loginAction")
public class LoginAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private final String env;
	private final String appName;
	private String pwdSent;
	private String pwdChanged;
	private String loginType;
	private String msgDpe; 
	
	public LoginAction() {
		this(null,null);
	}
	
	@Autowired
	public LoginAction(@Value("${igeriv.env.deploy.name}") String env, @Value("${igeriv.app.name}") String appName) {
		this.env = env;
		this.appName = appName;
	}
	
	@Override
	public void prepare() throws Exception {
		msgDpe = MessageFormat.format(getText("dpe.by"), "" + Calendar.getInstance().get(Calendar.YEAR));
		super.prepare();
	}
	
	public String execute() throws Exception {
		return SUCCESS;
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("login");
	}
	
}
