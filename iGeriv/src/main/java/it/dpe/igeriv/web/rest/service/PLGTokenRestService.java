package it.dpe.igeriv.web.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.web.rest.factory.RestRequest;
import it.dpe.igeriv.web.rest.factory.RestRequestFactory;

/**
 * Chiama il servizio rest di login che restituisce il token di autenticazione
 * 
 * @author mromano
 *
 */
@Component("PLGTokenRestService")
class PLGTokenRestService  extends BaseRestService<String, VenditeParamDto> implements RestService<String, VenditeParamDto> {
	private final AccountService accountService;
	private final PasswordEncoder passwordEncoder;
	private final ReflectionSaltSource saltSource;
	private final String env; 
	private final String rtaeRestUrl;
	
	@Autowired
	PLGTokenRestService(AccountService accountService, PasswordEncoder passwordEncoder, ReflectionSaltSource saltSource, @Value("${igeriv.plg.rest.url}") String rtaeRestUrl, @Value("${igeriv.env.deploy.name}") String env) {
		this.accountService = accountService;
		this.passwordEncoder = passwordEncoder;
		this.saltSource = saltSource;
		this.rtaeRestUrl = rtaeRestUrl;
		this.env = env;
	}
	
	@Override
	public ResponseEntity<String> getEntity(VenditeParamDto params) {
		params.setPwd(getPwd(params.getIdEdicola(), params.getCodFiegDl(), params.getCodEdicolaDl()));
		RestRequest<ResponseEntity<String>, VenditeParamDto> tokenRestRequest = RestRequestFactory.getTokenRestRequest();
		return executeRestRequest(tokenRestRequest, params, rtaeRestUrl + "/rest/j_spring_security_check", HttpMethod.POST, String.class, env);
	}
	
	@Override
	public ResponseEntity<String> putEntity(VenditeParamDto entity) {
		return null;
	}
	
	/**
	 * @param idEdicola
	 * @param codFiegDl 
	 * @param codEdicolaDl 
	 * @param params
	 * @return
	 */
	private String getPwd(Integer idEdicola, Integer codFiegDl, Integer codEdicolaDl) {
		UserVo utenteEdicola = accountService.getUtenteEdicola(idEdicola.toString());
		String pwd = utenteEdicola.getPwd();
		if (utenteEdicola.getPwdCriptata() == null || !utenteEdicola.getPwdCriptata().equals(1)) {
			List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
			authList.add(new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_BASE_ADMIN));
			UserAbbonato ua = new UserAbbonato(idEdicola.toString(), pwd, true, true, true, true, authList);
			ua.setCodFiegDl(codFiegDl);
			ua.setCodEdicolaDl(codEdicolaDl);
			pwd = passwordEncoder.encodePassword(pwd, saltSource.getSalt(ua));
		}
		return pwd;
	}

}
