package it.dpe.igeriv.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.exception.EdicolaSospesaException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.web.rest.service.RestService;

/**
 * Classe che esegue il login delle edicole.
 * 
 * TODO 
 * Sviluppare un servizio REST per l'autenticazione globale delle rivendite (KAS)
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Component("IGerivUserDetailsService")
public class IGerivUserDetailsService implements UserDetailsService {
	private final AccountService accountService;
	private final List<Integer> listCoddlAbilitatiCampagnaInvitaColleghi;
	//private final RestService<EdicolaDto, UserVo> kasService;
	
	@Autowired
	IGerivUserDetailsService(AccountService accountService, @Qualifier("KasService") RestService<EdicolaDto, UserVo> kasService, @Value("${igeriv.cod.dl.abilitati.campagna.invita.colleghi}") String coddlAbilitatiCampagnaInvitaColleghi) {
		this.accountService = accountService; 
		//this.kasService = kasService;
		this.listCoddlAbilitatiCampagnaInvitaColleghi = new ArrayList<Integer>();
		for (String s : coddlAbilitatiCampagnaInvitaColleghi.split(",")) {
			if (!Strings.isNullOrEmpty(s)) {
				listCoddlAbilitatiCampagnaInvitaColleghi.add(new Integer(s));
			}
		}
	}
	
	/* (non-Javadoc)	
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String arg0)
			throws UsernameNotFoundException, DataAccessException {
		UserAbbonato user = null;
		if (arg0 != null && !arg0.equals("")) {
			UserVo utente = accountService.getEdicolaByCodice(arg0);
			/*if (utente.getUtenteAmministratore() != null && utente.getUtenteAmministratore().equals(1)) {
				EdicolaDto edicola = utente != null ? kasService.getResponseEntity(utente).getBody() : null;
				buildUserVo(utente, edicola);
			}*/
			if (utente != null && utente.getAbilitato() != null && utente.getAbilitato().equals(IGerivConstants.COD_ABILITATO)) { 
				if (utente.getAbbinamentoEdicolaDlVo().getDtSospensioneEdicola() != null && utente.getAbbinamentoEdicolaDlVo().getDtSospensioneEdicola().before(new Date())) {
					if (utente.getAbbinamentoEdicolaDlVo().getEdicolaStarter()) {
						accountService.saveDowngradeAccountToStarter(utente);
						utente = accountService.getEdicolaByCodice(arg0);
					} else {
						throw new EdicolaSospesaException(IGerivMessageBundle.get("edicola.sospesa.message"));
					}
				} 
				user = accountService.buildUserDetails(arg0, utente);
				user.setCampagnaInvitaColleghiAbilitata(CollectionUtils.containsAny(listCoddlAbilitatiCampagnaInvitaColleghi, Arrays.asList(user.getArrCodFiegDl())));
			}
		}
		return user;
	}
	
}
