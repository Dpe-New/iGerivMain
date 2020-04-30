package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.inforiv.InforivExportService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.ImportazioneInforivReplyDto;
import it.dpe.igeriv.exception.EdicolaGiaEsistenteException;
import it.dpe.igeriv.exception.EdicolaInforivFtpException;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.service.mail.MailingListService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action per la gestione delle edicole inforiv Dl
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@SuppressWarnings({"rawtypes"})
@Scope("prototype")
@Component("edicolaInforivDlAction")
public class EdicolaInforivDlAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final InforivExportService inforivExportService;
	private final EdicoleService edicoleService;
	private final AccountService accountService;
	private final MailingListService mailingListService;
	private final String igerivUrl;
	private final String giorniTrial;
	private String tableTitle;
	private EdicolaDto edicola;
	private List<EdicolaDto> edicole;
	private Integer codEdicolaWeb;
	private String codEdicolaWebStr;
	private String ragioneSociale;
	private Boolean isNew = false;
	
	public EdicolaInforivDlAction() {
		this(null,null,null,null,null,null);
	}
	
	@Autowired
	public EdicolaInforivDlAction(InforivExportService inforivExportService, EdicoleService edicoleService, AccountService accountService, MailingListService mailingListService, @Value("${igeriv.url}") String igerivUrl, @Value("${inforiv.numero.giorni.trial.version}") String giorniTrial) {
		this.inforivExportService = inforivExportService;
		this.edicoleService = edicoleService;
		this.accountService = accountService;
		this.mailingListService = mailingListService;
		this.igerivUrl = igerivUrl;
		this.giorniTrial = giorniTrial;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		super.validate();
	}
	
	@SkipValidation
	public String showFilter() {
		tableTitle = getText("igeriv.visualizza.edicole.inforiv.dl");
		return SUCCESS;
	}
	
	@SkipValidation
	public String showEdicoleInforivDl() {
		tableTitle = getText("igeriv.visualizza.edicole.inforiv.dl");
		edicole = edicoleService.getEdicoleInforivDl(null, (codEdicolaWebStr != null && !codEdicolaWebStr.equals("")) ? new Integer(codEdicolaWebStr) : null, null, ragioneSociale, false);
		requestMap.put("edicole", edicole);
		return SUCCESS;
	}
	
	@SkipValidation
	public String showEdicolaInforivDl() {
		tableTitle = getText("igeriv.visualizza.edicole.inforiv.dl");
		edicola = new EdicolaDto();
		if (codEdicolaWeb != null) {
			UserVo user = accountService.getEdicolaByCodiceEdicola(codEdicolaWeb);
			List<AbbinamentoEdicolaDlVo> list = null;
			if (user.getAbbinamentoEdicolaDlVo().getCodEdicolaMaster() != null) {
				list = edicoleService.getEdicoleInforivDlByCodEdicolaWebMaster(user.getAbbinamentoEdicolaDlVo().getCodEdicolaMaster());
			} else {
				list = new ArrayList<>();
				list.add(user.getAbbinamentoEdicolaDlVo());
			}
			buildEdicola(list, user);
		}
		return "editEdicola";
	}
	
	@SkipValidation
	public String saveEdicolaInforivDl() {
		tableTitle = getText("igeriv.visualizza.edicole.inforiv.dl");
		try {
			if (edicola != null) {
				ImportazioneInforivReplyDto reply = inforivExportService.importEdicola(edicola, false, new Integer(giorniTrial), igerivUrl, false, true);
				mailingListService.sendEmail(edicola.getEmail(), IGerivMessageBundle.get("msg.subject.nuovo.utente.edicola"), reply.getEmailMessage());
			}
		} catch (EdicolaInforivFtpException e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + IGerivMessageBundle.get("msg.importazione.edicola.inforiv.ftp.invalido") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (EdicolaGiaEsistenteException e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(IGerivMessageBundle.get("msg.importazione.edicola.inforiv.gia.esistente"), igerivUrl) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (Exception e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + IGerivMessageBundle.get("msg.importazione.edicola.errore.fatale") + IGerivConstants.END_EXCEPTION_TAG);
			String message = MessageFormat.format(IGerivMessageBundle.get("msg.email.dpe.errore.fatale.importazione.edicola.inforiv"), edicola.getCodDl(), edicola.getCodEdicolaDl(), edicola.getRagioneSociale(), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, IGerivMessageBundle.get("msg.email.dpe.errore.fatale.importazione.edicola.inforiv.subject"), message, true);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}

	private void buildEdicola(List<AbbinamentoEdicolaDlVo> list, UserVo user) {
		if (list != null && !list.isEmpty()) {
			AbbinamentoEdicolaDlVo abb = list.get(0);
			AnagraficaEdicolaVo ed = abb.getAnagraficaEdicolaVo();
			List<Integer> listCodDl = extract(list, on(AbbinamentoEdicolaDlVo.class).getAnagraficaAgenziaVo().getCodFiegDl());
			List<Integer> listCodEdicolaDl = extract(list, on(AbbinamentoEdicolaDlVo.class).getCodEdicolaDl());
			edicola.setCodEdicolaWeb(abb.getCodDpeWebEdicola());
			edicola.setCodEdicolaDl(listCodEdicolaDl.get(0));
			edicola.setCodEdicolaDl2(listCodEdicolaDl.size() > 1 ? listCodEdicolaDl.get(1) : null);
			edicola.setCodEdicolaDl3(listCodEdicolaDl.size() > 2 ? listCodEdicolaDl.get(2) : null);
			edicola.setCodDl(listCodDl.get(0));
			edicola.setCodDl2(listCodDl.size() > 1 ? listCodDl.get(1) : null);
			edicola.setCodDl3(listCodDl.size() > 2 ? listCodDl.get(2) : null);
			edicola.setRagioneSociale1(ed.getRagioneSocialeEdicolaPrimaRiga());
			edicola.setRagioneSociale2(ed.getRagioneSocialeEdicolaSecondaRiga());
			edicola.setIndirizzo(ed.getIndirizzoEdicolaPrimaRiga());
			edicola.setLocalita(ed.getLocalitaEdicolaPrimaRiga());
			edicola.setProvincia(ed.getSiglaProvincia());
			edicola.setEmail(user.getEmail());
			edicola.setCap(ed.getCap());
			edicola.setTelefono(ed.getTelefono());
			edicola.setFax(ed.getFax());
			edicola.setPiva(ed.getPiva());
			edicola.setCodFiscale(ed.getCodiceFiscale());
			edicola.setLatitudine(ed.getLatitudine());
			edicola.setLongitudine(ed.getLongitudine());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#getParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.lang.String)
	 */
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#saveParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.util.Map)
	 */
	public void saveParameters(Context context, String arg1, Map arg2) {
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.visualizza.edicole.inforiv.dl");
	}

}
