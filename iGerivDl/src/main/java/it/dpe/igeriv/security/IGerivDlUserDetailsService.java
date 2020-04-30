package it.dpe.igeriv.security;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.vo.UtenteAgenziaVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

/**
 * Classe che esegue il login dei DL.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Component("IGerivDlUserDetailsService")
public class IGerivDlUserDetailsService implements UserDetailsService {
	private final AgenzieService agenzieService;
	private final AccountService accountService;
	
	@Autowired
	public IGerivDlUserDetailsService(AgenzieService agenzieService, AccountService accountService) {
		this.agenzieService = agenzieService;
		this.accountService = accountService;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String arg0)
			throws UsernameNotFoundException, DataAccessException {
		UserAbbonato user = null;
		Integer codCliente = getCodEdidiolaAsNumber(arg0);
		if (arg0 != null && !arg0.equals("") && codCliente != null) {
			UtenteAgenziaVo utente = agenzieService.getAgenziaByCodiceLogin(codCliente);
			if (utente != null) {
				user = accountService.buildDlUserDetails(arg0, utente);
			}
		}
		return user;
	}

	private Integer getCodEdidiolaAsNumber(String arg0) {
		Integer codEdicola = null;
		try {
			codEdicola = NumberUtils.parseNumber(arg0, Integer.class);
		} catch (Exception e) {
			
		}
		return codEdicola;
	}

}
