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

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.web.rest.factory.RestRequest;
import it.dpe.igeriv.web.rest.factory.RestRequestFactory;

/**
 * Chiama il servizio rest KAS (Kiosk Authentication Service) che restituisce il login e i dati anagrafici dell'edicola
 * 
 * @author mromano
 *
 */
@Component("KasService")
class KasRestService extends BaseRestService<EdicolaDto, UserVo> implements RestService<EdicolaDto, UserVo> {
	private final PasswordEncoder passwordEncoder;
	private final ReflectionSaltSource saltSource;
	private final String env; 
	private final String rtaeRestUrl;
	
	@Autowired
	KasRestService(PasswordEncoder passwordEncoder, ReflectionSaltSource saltSource, @Value("${igeriv.rtae.rest.url}") String rtaeRestUrl, @Value("${igeriv.env.deploy.name}") String env) {
		this.passwordEncoder = passwordEncoder;
		this.saltSource = saltSource;
		this.rtaeRestUrl = rtaeRestUrl;
		this.env = env;
	}
	
	@Override
	public ResponseEntity<EdicolaDto> getEntity(UserVo user) {
		user.setPwd(getPwd(user.getPwd(), (user.getPwdCriptata() != null && user.getPwdCriptata().equals(1)), user.getCodUtente(), user.getAbbinamentoEdicolaDlVo().getAnagraficaAgenziaVo().getCodFiegDl(), user.getAbbinamentoEdicolaDlVo().getCodEdicolaDl()));
		RestRequest<ResponseEntity<EdicolaDto>, UserVo> restRequest = RestRequestFactory.getKasRestRequest();
		return executeRestRequest(restRequest, user, rtaeRestUrl + "/rest/auth", HttpMethod.GET, EdicolaDto.class, env);
	}
	
	@Override
	public ResponseEntity<EdicolaDto> putEntity(UserVo user) {
		RestRequest<ResponseEntity<EdicolaDto>, UserVo> restRequest = RestRequestFactory.getKasPutRestRequest();
		return executeRestRequest(restRequest, user, rtaeRestUrl + "/rest/changePwd", HttpMethod.PUT, EdicolaDto.class, env);
	}
	
	/**
	 * @param pwdCriptata 
	 * @param pass 
	 * @param params
	 * @return
	 */
	private String getPwd(String pass, Boolean pwdCriptata, String codUtente, Integer codFiegDl, Integer crivDl) {
		String pwd = pass;
		if (pwdCriptata) {
			List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
			authList.add(new GrantedAuthorityImpl(IGerivConstants.ROLE_IGERIV_BASE_ADMIN));
			UserAbbonato ua = new UserAbbonato(codUtente, pwd, true, true, true, true, authList);
			ua.setCodFiegDl(codFiegDl);
			ua.setCodEdicolaDl(crivDl);
			pwd = passwordEncoder.encodePassword(pwd, saltSource.getSalt(ua));
		}
		return pwd;
	}
	
}
