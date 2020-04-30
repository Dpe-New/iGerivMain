package it.dpe.igeriv.security.openid;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.exception.EdicolaSospesaException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.UserVo;
import lombok.Getter;

/**
 * Classe che esegue il login delle edicole.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Component("IGerivOpenIdUserDetailsService")
public class IGerivOpenIdUserDetailsService implements UserDetailsService {
	private final EdicoleService edicoleService;
	private final AccountService accountService;
	
	@Autowired
	IGerivOpenIdUserDetailsService(EdicoleService edicoleService, AccountService accountService) {
		this.edicoleService = edicoleService;
		this.accountService = accountService;
	}
	
	/* (non-Javadoc)	
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException, DataAccessException {
		UserAbbonato user = null;
		if (!Strings.isNullOrEmpty(email)) {
			UserVo utente = accountService.getEdicolaByEmail(email);
			if (utente != null && utente.getAbilitato() != null && utente.getAbilitato().equals(IGerivConstants.COD_ABILITATO)) { 
				if (utente.getAbbinamentoEdicolaDlVo().getDtSospensioneEdicola() != null && utente.getAbbinamentoEdicolaDlVo().getDtSospensioneEdicola().before(new Date())) {
					if (utente.getAbbinamentoEdicolaDlVo().getEdicolaStarter()) {
						accountService.saveDowngradeAccountToStarter(utente);
						utente = accountService.getEdicolaByEmail(email);
					} else {
						throw new EdicolaSospesaException(IGerivMessageBundle.get("edicola.sospesa.message"));
					}
				} 
				user = accountService.buildUserDetails(utente.getCodUtente(), utente);
			}
		}
		return user;
	}

}
