package it.dpe.igeriv.security;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.vo.ClienteEdicolaVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

/**
 * Classe che esegue il login dei clienti delle edicole.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Component("IGerivClienteEdicolaUserDetailsService")
public class IGerivClienteEdicolaUserDetailsService implements UserDetailsService {
	@Autowired
	private ClientiService<ClienteEdicolaVo> clientiService;
	@Autowired
	private UserDetailBuilderClienteEdicola userDetailBuilderClienteEdicola;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String arg0)
			throws UsernameNotFoundException, DataAccessException {
		UserAbbonato user = null;
		Integer codCliente = getCodEdidiolaAsNumber(arg0);
		if (arg0 != null && !arg0.equals("") && codCliente != null) {
			ClienteEdicolaVo utente = clientiService.getCienteEdicolaByCodiceLogin(new Long(codCliente));
			if (utente != null) { 
				user = userDetailBuilderClienteEdicola.buildUserDetails(arg0, utente);
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

	public ClientiService<ClienteEdicolaVo> getClientiService() {
		return clientiService;
	}

	public void setClientiService(ClientiService<ClienteEdicolaVo> clientiService) {
		this.clientiService = clientiService;
	}

	public UserDetailBuilderClienteEdicola getUserDetailBuilderClienteEdicola() {
		return userDetailBuilderClienteEdicola;
	}

	public void setUserDetailBuilderClienteEdicola(UserDetailBuilderClienteEdicola userDetailBuilderClienteEdicola) {
		this.userDetailBuilderClienteEdicola = userDetailBuilderClienteEdicola;
	}
	
}
