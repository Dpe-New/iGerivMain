package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.UserVo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;



/**
 * Classe action che ritorna la home page.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("redirectAction")
public class RedirectAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private final AccountService accountService;
	private final PasswordEncoder passwordEncoder;
	private final ReflectionSaltSource saltSource;
	private final String rtaeUrl;
	private String url;
	
	public RedirectAction() {
		this(null,null,null,null);
	}
	
	@Autowired
	public RedirectAction(AccountService accountService, PasswordEncoder passwordEncoder, ReflectionSaltSource saltSource, @Value("${rtae.url}") String rtaeUrl) {
		this.accountService = accountService;
		this.passwordEncoder = passwordEncoder;
		this.saltSource = saltSource;
		this.rtaeUrl = rtaeUrl;
	}
	
	public String redirectToRtae() { 
		if (!getAuthUser().isRtaeAccessEnabled()) {
			throw new AccessDeniedException(null);
		}
		String username = Base64.encodeBase64URLSafeString((getAuthUser().getId().toString() + "|" + getAuthUser().getCodEdicolaDl().toString() + "|" + getAuthUser().getCodFiegDl()).getBytes());
		UserVo utenteEdicola = accountService.getUtenteEdicola(getAuthUser().getId().toString());
		String pwd = utenteEdicola.getPwd();
		if (utenteEdicola.getPwdCriptata() == null || !utenteEdicola.equals("1")) {
			List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
			authList.add(new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_BASE_ADMIN));
			UserAbbonato ua = new UserAbbonato(getAuthUser().getCodUtente(), pwd, true, true, true, true, authList);
			ua.setId(new Integer(getAuthUser().getId()));
			ua.setCodUtente(getAuthUser().getCodUtente());
			pwd = passwordEncoder.encodePassword(pwd, saltSource.getSalt(ua));
		}
		url = rtaeUrl + "?p1=" + username + "&p2=" + pwd;
		return "redirectToRtaeResult";
	}

	public String getTitle() {
		return super.getTitle() + getText("igeriv.home");
	}

}
