package it.dpe.igeriv.web.actions;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import it.dpe.igeriv.bo.contestazioni.ContestazioniService;
import it.dpe.igeriv.bo.mancanze.MancanzeService;
import it.dpe.igeriv.dto.ContestazioneResaDto;
import it.dpe.igeriv.dto.MancanzaBollaDto;
import it.dpe.igeriv.dto.MancanzaDto;
import it.dpe.igeriv.util.DateUtilities;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per le mancanze.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("mancanzeAction")
@SuppressWarnings({"rawtypes"}) 
public class MancanzeAction extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final MancanzeService mancanzeService;
	private final ContestazioniService contestazioniService;
	private final String crumbName = getText("igeriv.report.mancanze");
	private final String crumbNameContResa = getText("igeriv.report.contestazioni.resa");
	private final String crumbNameRepMancBolla = getText("igeriv.report.mancanze.bolla");
	private final String crumbNameDetRepMancBolla = getText("igeriv.report.dettaglio.mancanze.bolla");
	private String filterTitle;
	private String titolo;
	private String stato;
	private String strDataDa;
	private String strDataA;
	private Timestamp dataDa;
	private Timestamp dataA;
	private String tipoBolla;
	private String soloDifferenze; 
	private String idtn;
	private String dtBolla;
	private List<MancanzaBollaDto> mancanzeDettaglio;
	
	public MancanzeAction() {
		this.mancanzeService = null;
		this.contestazioniService = null;
	}
	
	@Autowired
	public MancanzeAction(MancanzeService mancanzeService, ContestazioniService contestazioniService) {
		this.mancanzeService = mancanzeService;
		this.contestazioniService = contestazioniService;
	}
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().contains("mancanze")) {
			filterTitle = getText("igeriv.report.mancanze");
		} else {
			filterTitle = getText("igeriv.report.contestazioni.resa");
		}
		if (strDataDa == null || strDataA == null || strDataDa.equals("") || strDataA.equals("")) {
			addActionError(getText("error.specificare.data.o.intervallo.date"));
		} else {
			try {
				dataDa = DateUtilities.floorDay(DateUtilities.parseDate(strDataDa, DateUtilities.FORMATO_DATA_SLASH));
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), strDataDa));
				return;
			}
			try {
				dataA = DateUtilities.ceilDay(DateUtilities.parseDate(strDataA, DateUtilities.FORMATO_DATA_SLASH));
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), strDataA));
				return;
			}
		}
	}
	
	@SkipValidation
	public String showMancanzeFilter() {
		filterTitle = getText("igeriv.report.mancanze");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameContResa}")
	@SkipValidation
	public String showFilterContestazioniResa() {
		filterTitle = getText("igeriv.report.contestazioni.resa");
		return SUCCESS;
	}
	
	public String showMancanze() {
		filterTitle = getText("igeriv.report.mancanze");
		List<MancanzaDto> mancanze = mancanzeService.getMancanze(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dataDa, dataA, titolo, (stato != null && !stato.equals("")) ? new Integer(stato) : null);
		requestMap.put("mancanze", mancanze);
		return SUCCESS;
	} 
	
	@BreadCrumb("%{crumbNameContResa}")
	public String showContestazioniResa() {
		filterTitle = getText("igeriv.report.contestazioni.resa");
		List<ContestazioneResaDto> contestazioniResa = contestazioniService.getContestazioniResa(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dataDa, dataA, titolo, (stato != null && !stato.equals("")) ? new Integer(stato) : null);
		requestMap.put("contestazioniResa", contestazioniResa);
		requestMap.put("title", filterTitle + " " + getText("igeriv.da") + " " + strDataDa + " " + getText("igeriv.a") + " " + strDataA);
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameRepMancBolla}")
	@SkipValidation
	public String showMancanzeBollaFilter() {
		filterTitle = getText("igeriv.report.mancanze.bolla");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbNameRepMancBolla}")
	public String showMancanzeBolla() {
		filterTitle = getText("igeriv.report.mancanze.bolla");
		Boolean soloDifferenze = (this.soloDifferenze != null) ? Boolean.parseBoolean(this.soloDifferenze) : null;
		List<MancanzaBollaDto> mancanze = mancanzeService.getMancanzeBolla(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dataDa, dataA, titolo, soloDifferenze);
		requestMap.put("mancanze", mancanze);
		return SUCCESS;
	} 
	
	@BreadCrumb("%{crumbNameDetRepMancBolla}")
	@SkipValidation
	public String showMancanzeDettaglioBolla() throws ParseException {
		filterTitle = getText("igeriv.report.dettaglio.mancanze.bolla");
		if (idtn != null && !idtn.equals("") && dtBolla != null && !dtBolla.equals("")) {
			Timestamp dtBolla  = DateUtilities.floorDay(DateUtilities.parseDate(this.dtBolla, DateUtilities.FORMATO_DATA_SLASH));
			Integer idtn = NumberUtils.parseNumber(this.idtn, Integer.class);
			mancanzeDettaglio = mancanzeService.getMancanzeDettaglioBolla(getAuthUser().getCodFiegDl(), getAuthUser().getId(), idtn, dtBolla);
			requestMap.put("mancanzeDettaglio", mancanzeDettaglio);
		}
		return "mancanzeDetail";
	} 
	
	@Override
	public void saveParameters(Context context, String tableId, Map parameterMap) {
		
	}

	@Override
	public Map getParameters(Context context, String tableId, String stateAttr) {
		return null;
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.report.mancanze");
	}
	
}
