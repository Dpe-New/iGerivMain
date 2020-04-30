package it.dpe.igeriv.ws;

import it.dpe.igeriv.bo.ws.IGerivWSService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.vo.UserVo;

import java.text.MessageFormat;

import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.ws.soap.security.xwss.XwsSecurityValidationException;

public class BaseEndPoint {
	private PasswordEncoder passwordEncoder;
	private IGerivWSService iGerivBo;
	private ReflectionSaltSource saltSource;
	
	/**
	 * @param codEdicola
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	protected UserAbbonato getUserAbbonato(Integer codEdicola, String pwd) throws XwsSecurityValidationException {
		UserVo edicolaByCodice = iGerivBo.getEdicolaByCodice(codEdicola.toString());
		if (edicolaByCodice == null) {
			throw new XwsSecurityValidationException(MessageFormat.format(IGerivMessageBundle.get("msg.rivendita.non.esiste.1"), codEdicola.toString()));
		}
		UserAbbonato ud = iGerivBo.buildUserDetails(edicolaByCodice.getCodUtente(), edicolaByCodice);
		String pwdEdicola = (ud.isPwdCriptata()) ? edicolaByCodice.getPwd() : passwordEncoder.encodePassword(edicolaByCodice.getPwd(), saltSource.getSalt(ud));
		if (!pwdEdicola.equals(pwd)) {
			throw new XwsSecurityValidationException(IGerivMessageBundle.get("gp.login.failed"));
		}
		return ud;
	}
	
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public IGerivWSService getiGerivBo() {
		return iGerivBo;
	}

	public void setiGerivBo(IGerivWSService iGerivBo) {
		this.iGerivBo = iGerivBo;
	}

	public ReflectionSaltSource getSaltSource() {
		return saltSource;
	}

	public void setSaltSource(ReflectionSaltSource saltSource) {
		this.saltSource = saltSource;
	}
	
}
