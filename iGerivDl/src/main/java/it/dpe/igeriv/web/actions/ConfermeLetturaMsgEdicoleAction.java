package it.dpe.igeriv.web.actions;

import java.text.MessageFormat;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action che ritorna la home page.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("confermeLetturaMsgEdicoleAction")
public class ConfermeLetturaMsgEdicoleAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private String codEdicolaDl;
	
	public String execute() {
		if (!Strings.isNullOrEmpty(codEdicolaDl)) {
			requestMap.put("message", MessageFormat.format(getText("msg.conferma.di.lettura.inviata.a.edicola"), codEdicolaDl));
		}
		return SUCCESS;
	}

	public String getTitle() {
		return super.getTitle() + getText("igeriv.home");
	}

}
