package it.dpe.igeriv.web.actions;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.contabilita.ContabilitaService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiProgressiviFatturazioneVo;
import it.dpe.igeriv.vo.RegistratoreCassaVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.pk.ParametriEdicolaPk;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiProgressiviFatturazionePk;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe per la configurazione dei parametri iGeriv dell'edicola
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("paramEdicolaAction")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ParametriEdicolaAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final AccountService accountService;
	private final EdicoleService edicoleService;
	private final ContabilitaService contabilitaService;
	private final String imgMiniatureEdicolaPathDir;
	private final String valoreMarcaBollo;
	private final String crumbName = getText("igeriv.menu.37");
	private Integer codParametro;
	private String valParametro;
	private String sqlType;
	private File attachment1;
	private String attachment1ContentType;
	private String attachment1FileName;
	private Long numIniFatture;
	private Long numIniEC;
	private Calendar cal;
	private Map<String, String> mapLastNumeroFatturazione;
	
	public ParametriEdicolaAction() {
		this.accountService = null;
		this.edicoleService = null;
		this.contabilitaService = null;
		this.imgMiniatureEdicolaPathDir = null;
		this.valoreMarcaBollo = null;
	}
	
	@Autowired
	public ParametriEdicolaAction(AccountService accountService, EdicoleService edicoleService, ContabilitaService contabilitaService, @Value("${img.miniature.edicola.path.dir}") String imgMiniatureEdicolaPathDir, @Value("${igeriv.valore.euro.marca.da.bollo.estratto.conto}") String valoreMarcaBollo) {
		this.accountService = accountService;
		this.edicoleService = edicoleService;
		this.contabilitaService = contabilitaService;
		this.imgMiniatureEdicolaPathDir = imgMiniatureEdicolaPathDir;
		this.valoreMarcaBollo = valoreMarcaBollo;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		numIniFatture = contabilitaService.getNextProgressivoFatturazione(getAuthUser().getCodEdicolaMaster(), IGerivConstants.TIPO_DOCUMENTO_FATTURA_EMESSA, cal.getTime()) - 1l;
		numIniEC = contabilitaService.getNextProgressivoFatturazione(getAuthUser().getCodEdicolaMaster(), IGerivConstants.TIPO_DOCUMENTO_ESTRATTO_CONTO, cal.getTime()) - 1l;
	}
	
	@Override
	public void validate() {
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showParams() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String getLastNumeroFatturazione() {
		mapLastNumeroFatturazione = new HashMap<String, String>();
		mapLastNumeroFatturazione.put("lastNumeroFatturazione", numIniFatture.toString());
		return "lastNumeroFatturazione";
	}
	
	@SkipValidation
	public String setLastNumeroFatturazione() {
		mapLastNumeroFatturazione = new HashMap<String, String>();
		try {
			setProgressivoFattureOEstrattoConto();
			ParametriEdicolaVo vo = edicoleService.getParametroEdicola(getAuthUser().getId(), codParametro);
			vo.setValue(valParametro);
			edicoleService.saveBaseVo(vo);
			Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
			ParametriEdicolaDto dto = mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + codParametro);
			dto.setValue(vo.getValue());
			sessionMap.put(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + codParametro, dto);
		} catch (IGerivBusinessException e) {
			mapLastNumeroFatturazione.put("error", e.getMessage());
		} catch (Throwable e) {
			mapLastNumeroFatturazione.put("error", getText("gp.errore.imprevisto"));
		}
		return "lastNumeroFatturazione";
	}
	
	public String saveParam() {
		if (!getFieldErrors().isEmpty() && getFieldErrors().containsKey("attachment1")) {
			return INPUT;
		}
		try {
			ParametriEdicolaVo vo = edicoleService.getParametroEdicola(getAuthUser().getId(), codParametro);
			if (vo == null) {
				vo = new ParametriEdicolaVo();
				ParametriEdicolaPk pk = new ParametriEdicolaPk();
				pk.setCodEdicola(getAuthUser().getId());
				pk.setCodParametro(codParametro);
				vo.setPk(pk);
			}
			if (attachment1 != null) {
				File outImgDirResized = new File(imgMiniatureEdicolaPathDir);
				if (!outImgDirResized.isDirectory()) {
					outImgDirResized.mkdirs();
				}
				String fileName = StringUtility.buildAttachmentFileName(attachment1FileName, getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
				File out = new File(imgMiniatureEdicolaPathDir + System.getProperty("file.separator") + fileName);
				FileUtils.copyFile(attachment1, out);
			    FileUtils.resizeImage(out, new File(imgMiniatureEdicolaPathDir), 400, 400);
			    valParametro = fileName;
			} else if (codParametro.equals(IGerivConstants.COD_PARAMETRO_LOGOMARCA_EDICOLA)) {
				valParametro = " ";
			} else if (codParametro.equals(IGerivConstants.COD_PARAMETRO_EMAIL_RIVENDITA) && !Strings.isNullOrEmpty(valParametro)) {
				UserVo edicolaByEmail = accountService.getEdicolaByEmail(valParametro);
				if (edicolaByEmail != null && !getAuthUser().getCodUtente().equals(edicolaByEmail.getCodUtente())) {
					throw new IGerivRuntimeException(getText("error.utente.stessa.mail"));
				}
			} else if (codParametro.equals(IGerivConstants.COD_PARAMETRO_SPUNTA_BOLLA_CONSEGNA)) {
				if (getAuthUser().isEdicolaPromo()) {
					valParametro = "false";
				}
				if (getAuthUser().getGruppoModuliVo() != null && getAuthUser().getGruppoModuliVo().getIsBollaConsegnaReadOnly()) {
					valParametro = "false";
				}
			}
			vo.setValue(valParametro);
			edicoleService.saveBaseVo(vo);
			Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
			ParametriEdicolaDto dto = mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + codParametro);
			dto.setValue(vo.getValue());
			sessionMap.put(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + codParametro, dto);
			setProgressivoFattureOEstrattoConto();
		} catch (IGerivBusinessException e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + e.getLocalizedMessage() + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		if (attachment1 != null) {
			return SUCCESS;
		}
		return "blank";
	}

	/**
	 * 
	 */
	private void setProgressivoFattureOEstrattoConto() throws IGerivBusinessException {
		if (codParametro.equals(IGerivConstants.COD_PARAMETRO_NUMERAZIONE_INIZIALE_FATTURE) || codParametro.equals(IGerivConstants.COD_PARAMETRO_NUMERAZIONE_INIZIALE_ESTRATTI_CONTO)) {
			Integer tdoc = codParametro.equals(IGerivConstants.COD_PARAMETRO_NUMERAZIONE_INIZIALE_FATTURE) ? IGerivConstants.TIPO_DOCUMENTO_FATTURA_EMESSA : IGerivConstants.TIPO_DOCUMENTO_ESTRATTO_CONTO;
			ProdottiNonEditorialiProgressiviFatturazioneVo pvo = new ProdottiNonEditorialiProgressiviFatturazioneVo();
			ProdottiNonEditorialiProgressiviFatturazionePk pk = new ProdottiNonEditorialiProgressiviFatturazionePk();
			pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
			pk.setTipoDocumento(tdoc);
			pk.setData(new java.sql.Date(cal.getTimeInMillis()));
			pvo.setPk(pk);
			Long progressivo = new Long(valParametro);
			pvo.setProgressivo(progressivo);
			pvo.setDataUltMovimentazione(new Timestamp(new Date().getTime()));
			Long nextProgressivoFatturazione = contabilitaService.getNextProgressivoFatturazione(getAuthUser().getCodEdicolaMaster(), tdoc, cal.getTime()) - 1l;
			if (nextProgressivoFatturazione == 0 || progressivo >= nextProgressivoFatturazione) {
				edicoleService.saveBaseVo(pvo);
			} else {
				throw new IGerivBusinessException(getText("igeriv.progressivo.inferiore.ultimo.utilizzato"));
			}
		}
	}
	
	@SkipValidation
	public String deleteEmailEdicola() {
		if (!getAuthUser().getCodFiegDl().equals(IGerivConstants.CDL_CODE)) {
			AbbinamentoEdicolaDlVo abbinamentoEdicolaDlVo = edicoleService.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(getAuthUser().getCodFiegDl(), getAuthUser().getCodEdicolaDl());
			AnagraficaEdicolaVo anagraficaEdicolaVo = edicoleService.getAnagraficaEdicola(getAuthUser().getId());
			anagraficaEdicolaVo.setEmail(null);
			abbinamentoEdicolaDlVo.setEmailValido(false);
			abbinamentoEdicolaDlVo.setPrivacySottoscritta(false);
			getAuthUser().setEmail(null);
			getAuthUser().setEmailValido(false);
			edicoleService.saveBaseVo(abbinamentoEdicolaDlVo);
			edicoleService.saveBaseVo(anagraficaEdicolaVo);
		}
		return "blank";
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
		return super.getTitle() + getText("igeriv.menu.37");
	}

	public List<RegistratoreCassaVo> getRegistratoriCassa() {
		return (List<RegistratoreCassaVo>) context.getAttribute("registratoriCassa");
	}
	
}
