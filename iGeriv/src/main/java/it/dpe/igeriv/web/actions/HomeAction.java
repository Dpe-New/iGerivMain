package it.dpe.igeriv.web.actions;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action che ritorna la home page.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("homeAction")
public class HomeAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private final ClientiService<ClienteEdicolaVo> clientiService;
	private final String crumbName = getText("igeriv.home");
	private Boolean loginExecuted;
	
	public HomeAction() {
		this.clientiService = null;
	}
	
	@Autowired
	public HomeAction(ClientiService<ClienteEdicolaVo> clientiService) {
		this.clientiService = clientiService;
	}
	
	public String execute() throws Exception {
		Boolean emailValido = true;
		Integer daysLeft = 0;
		if (getAuthUser().getCodFiegDl() != null && !getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE)) {
			boolean isEdicolaStarter = sessionMap.get("hasProfiloStarter") != null ? Boolean.parseBoolean(sessionMap.get("hasProfiloStarter").toString()) : false;
			if (getAuthUser().getTipoUtente() == IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA) {
				ClienteEdicolaVo cled = clientiService.getClienteEdicola(getAuthUser().getArrId(), new Long(getAuthUser().getId()));
				emailValido = cled.getPrivacySottoscritta() && !Strings.isNullOrEmpty(cled.getEmail()) && cled.getEmailValido();
			} else if (getAuthUser().getTipoUtente() == IGerivConstants.TIPO_UTENTE_EDICOLA) {
				if (isEdicolaStarter) {
					emailValido = getAuthUser().isEmailValido();
					if (getAuthUser().getDtSospensioneEdicola() != null) {
						daysLeft = setDaysLeft();
					}
				} else if (getAuthUser().getDtSospensioneEdicola() != null) {
					daysLeft = setDaysLeft();
				}
			}
		}
		requestMap.put("emailValido", emailValido);
		requestMap.put("daysLeft", daysLeft);
		requestMap.put("codFiegDl",getAuthUser().getCodFiegDl());
		return SUCCESS;
	}

	/**
	 * @return
	 */
	private Integer setDaysLeft() {
		Integer daysLeft = 0;
		Calendar calDtSosp = Calendar.getInstance();
		Calendar calDtNow = Calendar.getInstance();
		calDtSosp.setTime(getAuthUser().getDtSospensioneEdicola());
		calDtNow.setTime(new Date());
		daysLeft = new Long(DateUtilities.getDifference(calDtNow, calDtSosp, TimeUnit.DAYS)).intValue();
		return daysLeft;
	}

	public String getTitle() {
		return super.getTitle() + getText("igeriv.home");
	}

}
