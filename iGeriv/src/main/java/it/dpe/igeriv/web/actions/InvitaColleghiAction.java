package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.isIn;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.AnagraficaAgenziaDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.BonusInvitaUnCollegaVo;
import it.dpe.igeriv.vo.DlVo;
import it.dpe.igeriv.vo.EdicolaDlVo;
import it.dpe.service.mail.MailingListService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la campagna "Invita un collega".
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("invitaColleghiAction")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class InvitaColleghiAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final EdicoleService edicoleService;
	private final AgenzieService agenzieService;
	private final MailingListService mailingListService;
	private final String[] dpeSupportMailingList;
	private final String crumbName = getText("igeriv.campagna.invita.un.collega");
	private final List<Integer> listCoddlAbilitatiCampagnaInvitaColleghi;
	private final String igerivUrl;
	private String tableTitle;
	private String actionName;
	private Integer codEdicolaDl;
	private Integer coddl;
	private String email;
	@Getter(AccessLevel.NONE)
	private String ragSoc;
	private List<DlVo> agenzie;
	private String nomeDl;
	private boolean hasProfiloEdicolaBase;
	
	public InvitaColleghiAction() {
		this(null, null, null, null, null, null);
	}
	
	@Autowired
	public InvitaColleghiAction(EdicoleService edicoleService, AgenzieService agenzieService, MailingListService mailingListService, @Value("${dpe.igeriv.support.mailing.list}") String dpeSupportMailingList, @Value("${igeriv.cod.dl.abilitati.campagna.invita.colleghi}") String coddlAbilitatiCampagnaInvitaColleghi, @Value("${igeriv.url}") String igerivUrl) {
		this.edicoleService = edicoleService;
		this.agenzieService = agenzieService;
		this.mailingListService = mailingListService;
		this.dpeSupportMailingList = dpeSupportMailingList.split(",");
		this.listCoddlAbilitatiCampagnaInvitaColleghi = new ArrayList<Integer>();
		this.igerivUrl = igerivUrl;
		for (String s : coddlAbilitatiCampagnaInvitaColleghi.split(",")) {
			if (!Strings.isNullOrEmpty(s)) {
				listCoddlAbilitatiCampagnaInvitaColleghi.add(new Integer(s));
			}
		}
	}
	
	@Override
	public void prepare() throws Exception {
		hasProfiloEdicolaBase = sessionMap.get(IGerivConstants.SESSION_VAR_HAS_PROFILO_EDICOLA_BASE_NOT_TEST) != null ? Boolean.parseBoolean(sessionMap.get(IGerivConstants.SESSION_VAR_HAS_PROFILO_EDICOLA_BASE_NOT_TEST).toString()) : false;
		if (hasProfiloEdicolaBase) {
			super.prepare();
			tableTitle = getText("igeriv.campagna.invita.un.collega");
			agenzie = select(agenzieService.getListAgenzie(), having(on(DlVo.class).getCoddl(), isIn(listCoddlAbilitatiCampagnaInvitaColleghi)));
			requestMap.put("emailValido", getAuthUser().isEmailValido());
		}
	}
	
	@Override
	public void validate() {
		try {
			if (!getAuthUser().isEmailValido()) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("igeriv.msg.email.non.registrata.iniziativa.invita.collega.alert") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			if (codEdicolaDl == null) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("igeriv.statistiche.utilizzo.codice.rivendita.dl")) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			} else if (Strings.isNullOrEmpty(email)) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("dpe.email")) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			EdicolaDlVo edicolaDl = edicoleService.getEdicolaDl(codEdicolaDl, coddl);
			AnagraficaAgenziaDto agenzia = new AnagraficaAgenziaDto();
			agenzia.setCodFiegDl(coddl);
			boolean isDlDpe = ((List<AnagraficaAgenziaDto>) context.getAttribute("listAgenzieDpe")).contains(agenzia);
			if (isDlDpe) {
				if (edicolaDl == null) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("igeriv.errore.codice.edicola.inesistente") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				} else if (edicolaDl.getAttiva() || codEdicolaDl.equals(getAuthUser().getCodEdicolaDl()) || email.equals(getAuthUser().getEmail())) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("igeriv.errore.edicola.gia.attiva") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
			} else if (!Strings.isNullOrEmpty(getAuthUser().getEmail()) && email.equals(getAuthUser().getEmail())) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("igeriv.errore.edicola.gia.attiva") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			BonusInvitaUnCollegaVo bonusInvitaUnCollega = edicoleService.getBonusInvitaUnCollega(coddl, codEdicolaDl);
			if (bonusInvitaUnCollega != null) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("igeriv.errore.richiesta.bonus.gia.inviata"), codEdicolaDl) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
	}
	
	@SkipValidation
	public String show() throws ParseException {
		if (coddl == null && hasProfiloEdicolaBase) {
			coddl = getAuthUser().getCodFiegDl();
		}
		return SUCCESS;
	}
	
	public String sendRequest() {
		if (hasProfiloEdicolaBase) {
			try {
				BonusInvitaUnCollegaVo vo = new BonusInvitaUnCollegaVo();
				vo.setCodDpeWebEdicola(getAuthUser().getCodEdicolaMaster());
				vo.setCodDl(coddl);
				vo.setCodEdicolaDl(codEdicolaDl);
				vo.setEmail(email);
				vo.setRagioneSociale(ragSoc);
				vo.setDataRichiesta(edicoleService.getSysdate());
				edicoleService.saveBaseVo(vo);
				try {
					String subjectRichiedente = getText("msg.subject.porta.collega.in.igeriv.richiedente");
					String messageRichiedente = MessageFormat.format(getText("msg.email.porta.collega.in.igeriv.richiedente"), nomeDl, codEdicolaDl.toString(), email);
					mailingListService.sendEmailWithAttachment(new String[]{getAuthUser().getEmail()}, subjectRichiedente, messageRichiedente, null, true, null, false, null);
					String subjectCollega = getText("msg.subject.porta.collega.in.igeriv.collega");
					String messageCollega = MessageFormat.format(getText("msg.email.porta.collega.in.igeriv.collega"), igerivUrl, getAuthUser().getCodEdicolaDl(), getAuthUser().getRagioneSocialeEdicola(), getAuthUser().getIndirizzoEdicolaPrimaRiga(), getAuthUser().getLocalitaEdicolaPrimaRiga());
					mailingListService.sendEmailWithAttachment(new String[]{email}, subjectCollega, messageCollega, null, true, null, false, null);
					String subjectDpe = MessageFormat.format(getText("msg.subject.porta.collega.in.igeriv.dpe"), getAuthUser().getCodEdicolaMaster());
					String messageDpe = MessageFormat.format(getText("msg.email.porta.collega.in.igeriv.dpe"), getAuthUser().getCodEdicolaMaster(), getAuthUser().getRagioneSocialeEdicola(), getAuthUser().getRagioneSocialeDl() + " (" + getAuthUser().getCodFiegDl().toString() + ")", nomeDl + " (" + coddl.toString() + ")", codEdicolaDl.toString(), getRagSoc(), email);
					mailingListService.sendEmailWithAttachment(dpeSupportMailingList, subjectDpe, messageDpe, null, true, null, true, null);
				} catch (Throwable e1) {
					if (vo != null) {
						edicoleService.deleteVo(vo);
					}
				}
			} catch (Throwable e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
		return SUCCESS;
	}
	
	public String getReportTitle() {
		return getText("igeriv.report.ritiri.cliente");
	}
	
	@Override
	public String getTitle() {
		String title = getText("igeriv.report.ritiri.cliente");
		return super.getTitle() + title;
	}

	/* (non-Javadoc)
	 * @see org.extremecomponents.table.state.State#getParameters(org.extremecomponents.table.context.Context, java.lang.String, java.lang.String)
	 */
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}
	
	public void saveParameters(Context context, String arg1, Map arg2) {
		
	}
	
	public String getRagSoc() {
		return ragSoc == null ? "" : ragSoc;
	}
	
	public List<DlVo> getListDl() {
		return agenzie;
	}

}
