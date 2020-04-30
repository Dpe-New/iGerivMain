package it.dpe.igeriv.security.openid;

import static ch.lambdaj.Lambda.forEach;
import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.security.UserAdmin;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.MenuModuloVo;
import it.dpe.igeriv.vo.UserAdminVo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe che esegue il login dei clienti delle edicole.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Component("IGerivAdminOpenIdUserDetailsService")
public class IGerivAdminOpenIdUserDetailsService implements UserDetailsService {
	public static final short MAX_FAILED_LOGIN_ATTEMPTS = 3;
	@Autowired
	private MenuService menuService;
	@Autowired
	private AccountService accountService;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException, DataAccessException {
		UserAdmin user = null;
		if (!Strings.isNullOrEmpty(email)) {
			UserAdminVo utente = accountService.getUserAdminVo(email);
			if (utente != null) { 
				List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
				authList.add(new GrantedAuthorityImpl(utente.getGruppoModuliVo().getRoleName()));
				String pwd = utente.getPwd();
				if (utente.getFailedLoginAttempts() > MAX_FAILED_LOGIN_ATTEMPTS) {
					pwd = "";
				}
				user = new UserAdmin(utente.getCodUtente(), pwd, true, true, true, true, authList);
				user.setId(new Integer(utente.getCodUtente()));
				user.setCodUtente(utente.getCodUtente());
				user.setStatoAttivo(true);
				user.setNome(utente.getNomeUtente());
				user.setEmail(utente.getEmail());
				user.setChangePassword(utente.getChangePassword() != null && utente.getChangePassword().equals(1) ? true : false); 
				user.setTipoUtente(IGerivConstants.TIPO_UTENTE_ADM_SITO);
				user.setPwdCriptata(utente.getPwdCriptata() != null && utente.getPwdCriptata().equals(1) ? true : false);
				user.setFailedLoginAttempts(utente.getFailedLoginAttempts());
				List<MenuModuloVo> moduli = utente.getGruppoModuliVo().getModuli();
				forEach(moduli).setAttivo(true);
				List<List<List<MenuModuloVo>>> list = menuService.buildListModuli(moduli);
				user.setModuli(list);
			}
		}	
		return user;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
}
