package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.contabilita.ContabilitaService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.service.mail.MailingListService;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action per la gestione dell'edicola da parte del DL.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@SuppressWarnings({ "rawtypes"})
@Scope("prototype")
@Component("edicoleAction")
public class EdicoleAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private final AgenzieService agenzieService;

	private final EdicoleService edicoleService;
	private final ContabilitaService contabilitaService;
	private final AccountService accountService;
	private final MenuService menuService;
	private final MailingListService mailingListService;
	private final String crumbName = getText("igeriv.visualizza.edicole");
	private final String crumbNameDelete = getText("igeriv.cancellazione.ec.edicola");
	private String tableTitle;
	private List<EdicolaDto> edicole;
	private EdicolaDto edicola;
	private AnagraficaAgenziaVo agenziaDl;
	private String actionName;
	private String tableStyle;
	private String codEdicolaDl;
	private String ragioneSociale;
	private String pk;
	private String dateSospensione;
	private Integer codEdicolaWeb;
	private String inviaEmail;
	private String strDataEC;
	private Integer idGruppo;
	
	private List<GruppoModuliVo> listModuliDl;
	
	public EdicoleAction() {
		this(null,null,null,null,null,null);
	}
	
	@Autowired
	public EdicoleAction(EdicoleService edicoleService, ContabilitaService contabilitaService, AccountService accountService, MenuService menuService, MailingListService mailingListService,AgenzieService agenzieService) {
		this.edicoleService = edicoleService;
		this.contabilitaService = contabilitaService;
		this.accountService = accountService;
		this.menuService = menuService;
		this.mailingListService = mailingListService;
		this.agenzieService = agenzieService;
	}
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().contains("execDeleteEc.action")) {
			if (Strings.isNullOrEmpty(codEdicolaDl)) {
				addActionError(MessageFormat.format(getText("error.specificare.edicola"), codEdicolaDl));
				return;
			}
			if (Strings.isNullOrEmpty(strDataEC)) {
				addActionError(MessageFormat.format(getText("error.specificare.data"), strDataEC));
				return;
			}
			try {
				DateUtilities.parseDate(strDataEC, DateUtilities.FORMATO_DATA_SLASH);
			} 
			catch (ParseException pe) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("msg.formato.data.invalido"), strDataEC) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
		else {
			if (dateSospensione != null && !dateSospensione.equals("")) {
				String[] dtArr = dateSospensione.split(",");
				for (String strDt : dtArr) {
					if (!strDt.trim().equals("")) {
						try {
							DateUtilities.parseDate(strDt.trim(), DateUtilities.FORMATO_DATA_SLASH);
						} catch (ParseException e) {
							requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("msg.formato.data.invalido"), strDt) + IGerivConstants.END_EXCEPTION_TAG);
							throw new IGerivRuntimeException();
						}
					}
				}
			}
		}
	}
	
	@BreadCrumb("%{crumbName}")
	public String showFilter() {
		tableTitle = getText("igeriv.visualizza.edicole");
		return SUCCESS;
	}
	
	@BreadCrumb("%{crumbName}")
	public String showEdicole() {
		tableTitle = getText("igeriv.visualizza.edicole");
		Integer codEdicolaDlInt = (codEdicolaDl != null && !codEdicolaDl.equals("")) ? Integer.parseInt(codEdicolaDl) : null;
		edicole = edicoleService.getEdicole(getAuthUser().getCodFiegDl(), null, codEdicolaDlInt, ragioneSociale);
		requestMap.put("edicole", edicole);
		return SUCCESS;
	}
	
	public String showPwdEdicola() {
		tableTitle = getText("igeriv.cambia.password.rivendita");
		edicola = (EdicolaDto) edicoleService.getEdicole(getAuthUser().getCodFiegDl(), codEdicolaWeb, null, null).get(0);

		agenziaDl = agenzieService.getAgenziaByCodice(edicola.getCodDl());
		
		if (getAuthUser().getCodFiegDl() != null && getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE) || getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_DL_MENTA)
				|| getAuthUser().getCodFiegDl().equals(IGerivConstants.COD_FIEG_DL_DEVIETTI)) {
					
			listModuliDl = extract(menuService.getListDlGruppoModuliVo(getAuthUser().getCodFiegDl(), true), on(DlGruppoModuliVo.class).getGruppoModuli());
			idGruppo = accountService.getEdicolaByCodiceEdicola(edicola.getCodEdicolaWeb()).getDlGruppoModuliVo().getPk().getCodGruppo();		
	
		}else{
			listModuliDl = extract(menuService.getListDlGruppoModuliVo(getAuthUser().getCodFiegDl(), false), on(DlGruppoModuliVo.class).getGruppoModuli());
			idGruppo = accountService.getEdicolaByCodiceEdicola(edicola.getCodEdicolaWeb()).getDlGruppoModuliVo().getPk().getCodGruppo();

			List<GruppoModuliVo> listModuliDl_tmp = new ArrayList<GruppoModuliVo>();
			for(GruppoModuliVo iter: listModuliDl){
				if(iter.getId() == idGruppo){
					listModuliDl_tmp.add(iter);
				}
			}
			listModuliDl = listModuliDl_tmp;
		}
		
		
		//listModuliDl = extract(menuService.getListDlGruppoModuliVo(getAuthUser().getCodFiegDl(), true), on(DlGruppoModuliVo.class).getGruppoModuli());
		//idGruppo = accountService.getEdicolaByCodiceEdicola(edicola.getCodEdicolaWeb()).getDlGruppoModuliVo().getPk().getCodGruppo();
		return "pwdEdicola";
	}
	
	@BreadCrumb("%{crumbNameDelete}")
	@SkipValidation
	public String showDeleteEc() {
		tableTitle = getText("igeriv.cancellazione.ec.edicola");
		actionName = "execDeleteEc.action";
		
		codEdicolaDl = null;
		strDataEC = null;
		
		return "showDeleteEc";
	}
	
	public String execDeleteEc() {
		try {
			Integer codEdicola = Integer.valueOf(codEdicolaDl);
			Timestamp dataECFinale = DateUtilities.parseDate(strDataEC, DateUtilities.FORMATO_DATA_SLASH);
			
			contabilitaService.deleteEstrattoContoFinoAllaData(getAuthUser().getCodFiegDl(), codEdicola, dataECFinale);
		}
		catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		
		return showDeleteEc();
	}
	
	public String savePwdEdicola() {
		tableTitle = getText("igeriv.visualizza.edicole");
		listModuliDl = extract(menuService.getListDlGruppoModuliVo(getAuthUser().getCodFiegDl(), true), on(DlGruppoModuliVo.class).getGruppoModuli());
		try {
			if (edicola != null) { 
				if(edicola.getPassword()!=null && !edicola.getPassword().equals("")){
					boolean hasEmail = edicola.getEmail() != null && !Strings.isNullOrEmpty(edicola.getEmail());
					UserVo user = accountService.getEdicolaByCodiceEdicola(edicola.getCodEdicolaWeb());
					boolean idPwdCriptata = edicola.getPassword().equals(user.getPwd()) && user.getPwdCriptata().equals(1) ? true : false; 
					boolean changePwd = hasEmail && !edicola.getPassword().equals(user.getPwd()) ? true : false;
					edicoleService.updatePwdEdicola(edicola.getCodEdicolaWeb(), edicola.getPassword(), changePwd, idPwdCriptata);
					boolean checkConsegneGazzetta = edicola.getCheckConsegneGazzetta() != null && edicola.getCheckConsegneGazzetta();
					edicoleService.updateDataSospensioneEdicole(getAuthUser().getCodFiegDl(), user.getAbbinamentoEdicolaDlVo().getCodEdicolaDl().toString(), edicola.getDataSospensioneString() == null ? null : edicola.getDataSospensioneString(), checkConsegneGazzetta);
					user = accountService.getEdicolaByCodiceEdicola(edicola.getCodEdicolaWeb());
					DlGruppoModuliVo gruppo = menuService.getDlGruppoModuliVo(idGruppo, getAuthUser().getCodFiegDl());
					user.setDlGruppoModuliVo(gruppo);
					accountService.saveBaseVo(user);
					if (inviaEmail != null && inviaEmail.equals("true") && hasEmail) {
						String titleMsg = null; 
						if (getAuthUser().getCodFiegDl() != null && getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE)) {
							titleMsg = getText("gp.titolo.cdl"); 
						} else {
							titleMsg = getText("gp.titolo");
						}
						String message = MessageFormat.format(getText("msg.email.reset.pwd"), edicola.getPassword());
						if (hasEmail) {
							try {
								mailingListService.sendEmail(new String[]{edicola.getEmail()}, titleMsg + " " + getText("msg.subject.pwd.reset"), message, false);
							} catch (Exception e) {
								requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
								throw new IGerivRuntimeException();
							}
						}
					}
				}else{
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.errore.pwd.isempty")+ IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
			}
		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "pwdEdicola";
	}
	
	public String saveEdicole() {
		tableTitle = getText("igeriv.visualizza.edicole");
		edicoleService.updateDataSospensioneEdicole(getAuthUser().getCodFiegDl(), getPk(), getDateSospensione(), false);
		return SUCCESS;
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
		return super.getTitle() + getText("igeriv.visualizza.edicole");
	}

}
