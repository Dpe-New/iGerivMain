package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.PianoProfiliEdicolaVo;
import it.dpe.service.mail.MailingListService;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action per la gestione dell'edicola da parte del ADM.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@SuppressWarnings({"rawtypes", "unchecked"})
@Scope("prototype")
@Component("profilazioneAdmAction")
public class ProfilazioneAdmAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	@Autowired
	private EdicoleService edicoleService;
	@Autowired
	private MailingListService mailingListService;
	private String tableHeight;
	private String tableTitle;
	private List<EdicolaDto> edicole;
	private AnagraficaEdicolaVo dettEdicola;
	private AbbinamentoEdicolaDlVo dettProfiloEdicola;
	private List<PianoProfiliEdicolaVo> listPianoProfiliEdicola;
	private EdicolaDto edicola;
	private String actionName;
	private String tableStyle;
	private String codEdicolaDl;
	private String ragioneSociale;
	private String pk;
	private Integer codEdicolaWeb;
	private String codEdicolaWebStr;
	private String inviaEmail;
	private String codDl; 
	private String profiliDL;
	private String dataInserimento;
	private String dataSospensione;
	private String edicolaTest;
	private String edicolaPromo;
	private String edicolaPlusdtIni;
	private String edicolaPlusdtFin;
	private String keys;
	private String values;
	private String profilo;
	private String edicolaPlusdtIniHidden;
	private String edicolaPlusdtFinHidden;
	private String edicolaPromodtIniHidden;
	private String edicolaPromodtFinHidden;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		List<GruppoModuliVo> profili = (List<GruppoModuliVo>) sessionMap.get(IGerivConstants.SESSION_VAR_PROFILI_EDICOLA);
		keys = "";
		values = "";
		int count = 0;
		for (GruppoModuliVo gmvo : profili) {
			keys += ((count > 0) ? "," : "") + gmvo.getId();
			values += ((count > 0) ? "," : "") + gmvo.getRoleName();
			count++;
		}
	}
	
	@Override
	public void validate() {
		
	}
	
	@SkipValidation
	public String showFilter() {
		tableTitle = getText("gp.gestione.minicard");
		return SUCCESS;
	}
	
	@SkipValidation
	public String showEdicole() {
		tableTitle = getText("gp.gestione.minicard");
		Integer codDlInt = (codDl != null && !codDl.equals("")) ? Integer.parseInt(codDl) : null;
		Integer idProfiliDL = (profiliDL != null && !profiliDL.equals("")) ? Integer.parseInt(profiliDL) : null;
		edicole = edicoleService.getEdicole(codDlInt,idProfiliDL,(codEdicolaWebStr != null && !codEdicolaWebStr.equals("")) ? new Integer(codEdicolaWebStr) : null, null, ragioneSociale);
		requestMap.put("edicole", edicole);
	
		return SUCCESS;
	}
	
	@SkipValidation
	public String saveEdicole() {
		tableTitle = getText("gp.gestione.minicard");
		edicoleService.updateDatiEdicole(getAuthUser().getId(), getPk(), getDataInserimento(), getDataSospensione(), getProfilo(), getEdicolaTest(), getEdicolaPromodtIniHidden(), getEdicolaPromodtFinHidden(), getEdicolaPlusdtIniHidden(), getEdicolaPlusdtFinHidden());
		return SUCCESS;
	}
	
	
	@SkipValidation
	public String showDettaglioProfilo() {
		tableTitle = getText("gp.gestione.minicard");
		//dettEdicola = edicoleService.getEdicolaByCodFiegDlCodRivDl(new Integer(codDl), new Integer(codEdicolaWeb));
		dettProfiloEdicola = edicoleService.getAbbinamentoEdicolaDlVoByCodEdicolaWeb(new Integer(codEdicolaWeb));
		listPianoProfiliEdicola = edicoleService.getPianoProfiliEdicola(new Integer(codEdicolaWeb), new Integer(codDl));
		
		
		requestMap.put("dettEdicola", dettEdicola);
		requestMap.put("dettProfiloEdicola", dettProfiloEdicola);
		requestMap.put("codDl", codDl);
		requestMap.put("listPianoProfiliEdicola",listPianoProfiliEdicola);
		return "openDettaglioProfilo";
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
