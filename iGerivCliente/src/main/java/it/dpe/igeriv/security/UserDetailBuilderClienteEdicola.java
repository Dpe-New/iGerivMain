package it.dpe.igeriv.security;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.dto.CodDlEdicoleSecondarieDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.MenuModuloVo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe che costruisce lo user.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Component("UserDetailBuilderClienteEdicola")
public class UserDetailBuilderClienteEdicola {
	@Autowired
	private AccountService accountService;
	@Autowired
	private MenuService menuService;
	
	@SuppressWarnings("unchecked")
	public UserAbbonato buildUserDetails(String userId, BaseVo utenteBase) {
		ClienteEdicolaVo utente = (ClienteEdicolaVo) utenteBase;
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		authList.add(new GrantedAuthorityImpl(utente.getGruppoModuliVo().getRoleName()));
		if (utente != null && !Strings.isNullOrEmpty(utente.getPassword())) {
			UserAbbonato user = new UserAbbonato(userId, utente.getPassword(), true, true, true, true, authList);
			Integer codDpeWebEdicola = utente.getEdicola().getCodDpeWebEdicola();
			AnagraficaAgenziaVo anagraficaAgenziaVo = utente.getEdicola().getAnagraficaAgenziaVo();
			Integer codFiegDl = anagraficaAgenziaVo.getCodFiegDl();
			user.setId(new Integer(userId));
			user.setCodUtente(userId);
			user.setStatoAttivo(true);
			if (anagraficaAgenziaVo.getDlInforiv()) {
				List<CodDlEdicoleSecondarieDto> listCodDpeWebEdicola = accountService.getListCodDlECodEdicolaSecondarie(utente.getEdicola().getCodEdicolaMaster());
				user.setArrId(extract(listCodDpeWebEdicola, on(CodDlEdicoleSecondarieDto.class).getCodDpeWebEdicola()).toArray(new Integer[]{}));
				user.setArrCodFiegDl(extract(listCodDpeWebEdicola, on(CodDlEdicoleSecondarieDto.class).getCodFiegDl()).toArray(new Integer[]{}));
				user.setDlInforiv(true);
			} else {
				user.setArrId(new Integer[]{codDpeWebEdicola});
				user.setArrCodFiegDl(new Integer[]{codFiegDl});
			}
			user.setCodFiegDlMaster(codFiegDl);
			user.setCodEdicolaMaster(codDpeWebEdicola);
			user.setNome(utente.getNome());
			user.setCognome(utente.getCognome());
			user.setRagioneSocialeDl(anagraficaAgenziaVo.getRagioneSocialeDlPrimaRiga());
			user.setRagioneSocialeEdicola(utente.getEdicola().getAnagraficaEdicolaVo().getRagioneSocialeEdicolaPrimaRiga());
			user.setIndirizzoEdicolaPrimaRiga(utente.getEdicola().getAnagraficaEdicolaVo().getIndirizzoEdicolaPrimaRiga());
			user.setLocalitaEdicolaPrimaRiga(utente.getEdicola().getAnagraficaEdicolaVo().getLocalitaEdicolaPrimaRiga());
			user.setCodEdicolaDl(utente.getEdicola().getCodEdicolaDl());
			user.setCodDpeWebEdicola(codDpeWebEdicola);
			user.setCodFiegDl(codFiegDl);
			user.setEmail(utente.getEmail());
			user.setChangePassword(utente.getChangePassword() != null && utente.getChangePassword().equals(1) ? true : false); 
			user.setTipoUtente(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA);
			user.setPwdCriptata(utente.getPwdCriptata() != null && utente.getPwdCriptata().equals(1) ? true : false);
			user.setEmailValido(utente.getEmailValido());
			List<MenuModuloVo> moduli = utente.getGruppoModuliVo().getModuli();
			forEach(moduli).setAttivo(true);
			List<List<List<MenuModuloVo>>> list = menuService.buildListModuli(moduli);
			user.setModuli(list);
			return user;
		}
		return null;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

}
