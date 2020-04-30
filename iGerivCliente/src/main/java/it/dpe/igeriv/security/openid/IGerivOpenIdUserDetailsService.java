package it.dpe.igeriv.security.openid;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.security.UserDetailBuilderClienteEdicola;
import it.dpe.igeriv.vo.ClienteEdicolaVo;

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
	private ClientiService<ClienteEdicolaVo> clientiService;
	@Autowired
	private UserDetailBuilderClienteEdicola userDetailBuilderClienteEdicola;
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
			ClienteEdicolaVo utente = clientiService.getCienteEdicolaByEmail(email);
			if (utente != null) { 
				user = userDetailBuilderClienteEdicola.buildUserDetails(utente.getCodCliente().toString(), utente);
			}
		}	
		return user;
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

	public UserDetailBuilderClienteEdicola getUserDetailBuilderClienteEdicola() {
		return userDetailBuilderClienteEdicola;
	}

	public void setUserDetailBuilderClienteEdicola(UserDetailBuilderClienteEdicola userDetailBuilderClienteEdicola) {
		this.userDetailBuilderClienteEdicola = userDetailBuilderClienteEdicola;
	}

	public ClientiService<ClienteEdicolaVo> getClientiService() {
		return clientiService;
	}

	public void setClientiService(ClientiService<ClienteEdicolaVo> clientiService) {
		this.clientiService = clientiService;
	}
	
}
