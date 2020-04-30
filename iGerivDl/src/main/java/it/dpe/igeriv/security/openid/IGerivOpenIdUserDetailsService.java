package it.dpe.igeriv.security.openid;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe che esegue il login dei dl.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Component("IGerivOpenIdUserDetailsService")
public class IGerivOpenIdUserDetailsService implements UserDetailsService {
	@Autowired
	private AgenzieService agenzieService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ReflectionSaltSource saltSource;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/* (non-Javadoc)	
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException, DataAccessException {
		UserAbbonato user = null;
		if (!Strings.isNullOrEmpty(email)) {
			AnagraficaAgenziaVo utente = agenzieService.getAgenziaByEmail(email);
			if (utente != null) { 
				user = accountService.buildUserDetails(utente.getCodFiegDl().toString(), utente);
			}
		}
		return user;
	}

	public AgenzieService getAgenzieService() {
		return agenzieService;
	}


	public void setAgenzieService(AgenzieService agenzieService) {
		this.agenzieService = agenzieService;
	}

	public AccountService getAccountService() {
		return accountService;
	}


	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public ReflectionSaltSource getSaltSource() {
		return saltSource;
	}

	public void setSaltSource(ReflectionSaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}
