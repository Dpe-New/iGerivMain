package it.dpe.igeriv.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * Classe che estende lo user di base e che implementa la funzionalità del blocco dopo n tentaivi di login falliti.
 * 
 * @author romanom
 *
 */
public class UserAdmin extends UserAbbonato {
	private static final long serialVersionUID = 1L;
	public static final short MAX_FAILED_LOGIN_ATTEMPTS = 5;
	private int failedLoginAttempts; 
	
	public UserAdmin(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities) throws IllegalArgumentException {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	
	@Override
	public boolean isAccountNonLocked() {
		if (this.getFailedLoginAttempts() >= MAX_FAILED_LOGIN_ATTEMPTS) {
			return false;
		}
		return true;
	}

	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	public void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}
	
}
