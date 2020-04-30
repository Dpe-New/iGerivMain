package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.util.NumberUtils;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.RequestAware;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action del DL per il download dei dati delle rivendite.
 * La pagina JSP contiene un bottone che genera il file di dati sul server e  
 * una Applet che esegue il download del file e l'upload sull'FTP gestionale.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("ScambioDatiDlAction")
public class ScambioDatiDlAction extends RestrictedAccessBaseAction implements RequestAware {
	private static final long serialVersionUID = 1L;
	private String fileName;
	private final String appletDir;
	private final String datiDlPathDir;
	private final String datiDlOutPathDir;
	private final String datiDlAlias;
	private final String crumbName = getText("igeriv.menu.56");
	
	public ScambioDatiDlAction() {
		this(null,null,null,null);
	}
	
	@Autowired
	public ScambioDatiDlAction(@Value("${applet.tomcat.dir}") String appletDir, @Value("${dl.dati.path.dir}") String datiDlPathDir, @Value("${dl.out.path.dir}") String datiDlOutPathDir, @Value("${dl.dati.dir.alias}") String datiDlAlias) {
		this.appletDir = appletDir;
		this.datiDlPathDir = datiDlPathDir;
		this.datiDlOutPathDir = datiDlOutPathDir;
		this.datiDlAlias = datiDlAlias;
	}
	
	@Override
	public void validate() {
		super.validate();
	}
	
	@BreadCrumb("%{crumbName}")
	@SuppressWarnings("deprecation")
	public String execute() throws IOException {
		String requestURL = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		String baseUrl = requestURL.substring(0, requestURL.indexOf(contextPath));
		requestMap.put("appletDir", appletDir);
		requestMap.put("ftpUser", getAuthUser().getFtpServerGestionaleUser());
		requestMap.put("ftpPwd", getAuthUser().getFtpServerGestionalePwd());
		requestMap.put("ftpDir", getAuthUser().getFtpServerGestionaleDir());
		requestMap.put("ftpServerUrl", getAuthUser().getFtpServerGestionaleAddress());
		requestMap.put("httpProxyServer", getAuthUser().getHttpProxyServer() == null ? "" : getAuthUser().getHttpProxyServer());
		requestMap.put("httpProxyPort", getAuthUser().getHttpProxyPort() == null ? "" : getAuthUser().getHttpProxyPort());
		requestMap.put("ftpPort", "21");
		requestMap.put("urlDl", baseUrl + datiDlAlias + "/" + NumberUtils.formatNumber(getAuthUser().getCodFiegDl(), 4, 0, false) + "/" + datiDlOutPathDir);
		requestMap.put("sessionId", request.getSession().getId());
		return SUCCESS;
	}
	
	public String getTitle() {
		return super.getTitle() + getText("igeriv.menu.56");
	}
	
}
