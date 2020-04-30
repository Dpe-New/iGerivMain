package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.ParametriClienteDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.RegistratoreCassaVo;
import it.dpe.igeriv.vo.pk.ParametriEdicolaPk;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe per la configurazione dei parametri iGeriv dell'edicola
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Scope("prototype")
@Component("paramClienteAction")
public class ParametriClienteAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	@Autowired
	private EdicoleService edicoleService;
	@Autowired
	private ClientiService<ClienteEdicolaVo> clientiService;
	private Integer codParametro;
	private String valParametro;
	private String sqlType;
	private File attachment1;
	private String attachment1ContentType;
	private String attachment1FileName;
	@Value("${img.miniature.edicola.path.dir}")
	private String imgMiniatureEdicolaPathDir;
	@Value("${igeriv.valore.euro.marca.da.bollo.estratto.conto}")
	private String valoreMarcaBollo;
	private Long numIniFatture;
	private Long numIniEC;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
	}

	@SkipValidation
	public String showParams() {
		return SUCCESS;
	}
	
	public String saveParam() {
		try {
			ParametriEdicolaVo vo = edicoleService.getParametroEdicola(getAuthUser().getId(), codParametro);
			if (vo == null) {
				vo = new ParametriEdicolaVo();
				ParametriEdicolaPk pk = new ParametriEdicolaPk();
				pk.setCodEdicola(getAuthUser().getId());
				pk.setCodParametro(codParametro);
				vo.setPk(pk);
			}
			if (codParametro.equals(IGerivConstants.COD_PARAMETRO_EMAIL_CLIENTE) && !Strings.isNullOrEmpty(valParametro)) {
				ClienteEdicolaVo cienteEdicolaByEmail = clientiService.getCienteEdicolaByEmail(valParametro);
				if (cienteEdicolaByEmail != null && !getAuthUser().getCodUtente().equals(cienteEdicolaByEmail.getCodCliente().toString())) {
					throw new IGerivRuntimeException(getText("error.utente.stessa.mail"));
				}
			}
			vo.setValue(valParametro);
			edicoleService.saveBaseVo(vo);
			ParametriClienteDto dto = (ParametriClienteDto) sessionMap.get(IGerivConstants.SESSION_VAR_PARAMETRO_CLIENTE + codParametro);
			dto.setValue(vo.getValue());
			sessionMap.put(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + codParametro, dto);
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		if (attachment1 != null) {
			return SUCCESS;
		}
		return "blank";
	}

	@SkipValidation
	public String deleteEmailCliente() {
		ClienteEdicolaVo cliente = clientiService.getClienteEdicola(getAuthUser().getArrId(), new Long(getAuthUser().getId()));
		cliente.setEmail(null);
		cliente.setEmailValido(false);
		cliente.setPrivacySottoscritta(false);
		getAuthUser().setEmail(null);
		getAuthUser().setEmailValido(false);
		clientiService.saveBaseVo(cliente);
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

	public Integer getCodParametro() {
		return codParametro;
	}

	public void setCodParametro(Integer codParametro) {
		this.codParametro = codParametro;
	}

	public String getValParametro() {
		return valParametro;
	}

	public void setValParametro(String valParametro) {
		this.valParametro = valParametro;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public List<RegistratoreCassaVo> getRegistratoriCassa() {
		return (List<RegistratoreCassaVo>) context.getAttribute("registratoriCassa");
	}

	public File getAttachment1() {
		return attachment1;
	}

	public void setAttachment1(File attachment1) {
		this.attachment1 = attachment1;
	}

	public String getAttachment1ContentType() {
		return attachment1ContentType;
	}

	public void setAttachment1ContentType(String attachment1ContentType) {
		this.attachment1ContentType = attachment1ContentType;
	}

	public String getAttachment1FileName() {
		return attachment1FileName;
	}

	public void setAttachment1FileName(String attachment1FileName) {
		this.attachment1FileName = attachment1FileName;
	}

	public String getImgMiniatureEdicolaPathDir() {
		return imgMiniatureEdicolaPathDir;
	}

	public void setImgMiniatureEdicolaPathDir(String imgMiniatureEdicolaPathDir) {
		this.imgMiniatureEdicolaPathDir = imgMiniatureEdicolaPathDir;
	}

	public String getValoreMarcaBollo() {
		return valoreMarcaBollo;
	}

	public void setValoreMarcaBollo(String valoreMarcaBollo) {
		this.valoreMarcaBollo = valoreMarcaBollo;
	}

	public Long getNumIniFatture() {
		return numIniFatture;
	}

	public void setNumIniFatture(Long numIniFatture) {
		this.numIniFatture = numIniFatture;
	}

	public Long getNumIniEC() {
		return numIniEC;
	}

	public void setNumIniEC(Long numIniEC) {
		this.numIniEC = numIniEC;
	}

	public EdicoleService getEdicoleService() {
		return edicoleService;
	}

	public void setEdicoleService(EdicoleService edicoleService) {
		this.edicoleService = edicoleService;
	}

	public ClientiService<ClienteEdicolaVo> getClientiService() {
		return clientiService;
	}

	public void setClientiService(ClientiService<ClienteEdicolaVo> clientiService) {
		this.clientiService = clientiService;
	}
	
}
