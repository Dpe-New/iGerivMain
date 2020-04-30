package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.DESCENDING;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import it.dpe.igeriv.bo.contabilita.ContabilitaService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.EstrattoContoDto;
import it.dpe.igeriv.dto.FattureDLDto;
import it.dpe.igeriv.dto.VendutoEstrattoContoDto;
import it.dpe.igeriv.util.DateUtilities;

import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.FatturaClienteEdicolaVo;
import it.dpe.igeriv.vo.FattureEdicolaPdfVo;
import lombok.Data;

@Data
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("fattureDLAction")
@SuppressWarnings({ "rawtypes" })
public class FattureDLAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final ContabilitaService contabilitaService;
	private final EdicoleService edicoleService;
	private final String crumbName = getText("igeriv.visualizza.fatture.dl");
	private List<FattureEdicolaPdfVo> listFattureDL;
	private String tableHeight;
	private String tableTitle;
	private String dataNumeroFattura;
	private String actionName;
	private String tableStyle;
	private String dataDaStr;
	private String dataAStr;

	public FattureDLAction() {
		this.contabilitaService = null;
		this.edicoleService = null;
	}

	@Autowired
	public FattureDLAction(ContabilitaService contabilitaService, EdicoleService edicoleService) {
		this.contabilitaService = contabilitaService;
		this.edicoleService = edicoleService;
	}

	@Override
	public void prepare() throws Exception {
		super.prepare();
		// requestMap.put("mostraGrafico",
		// getText("igeriv.grafico.venduto.estratto.conto"));
	}

	@Override
	public void validate() {
		if (getActionName() != null) {

		}

	}

	@SkipValidation
	public String showFilter() throws Exception {
		tableTitle = getText("igeriv.visualizza.fatture.dl");
		listFattureDL = contabilitaService.getFattureDL(getAuthUser().getCodFiegDl(), getAuthUser().getId());
		sessionMap.put("listFattureDL", listFattureDL);
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	@SkipValidation
	public String showFatture() throws ParseException {
		tableTitle = getText("igeriv.visualizza.fatture.dl");
		listFattureDL = (List<FattureEdicolaPdfVo>) sessionMap.get("listFattureDL");
		if (dataNumeroFattura == null || dataNumeroFattura.isEmpty()) {
			return SUCCESS;
		}
		String[] split = dataNumeroFattura.split("\\|");
		String dataFatt = split[0];
		int numeroFatt = Integer.parseInt(split[1]);
		Timestamp tFatt = DateUtilities.parseDate(dataFatt, DateUtilities.FORMATO_DATA);

		FattureEdicolaPdfVo vo = null;
		for (FattureEdicolaPdfVo curr : listFattureDL) {
			if (curr.getPk().getDataFattura() != null && curr.getPk().getNumeroFattura() != null) {
				if (curr.getPk().getDataFattura().equals(tFatt) && numeroFatt == curr.getPk().getNumeroFattura()) {
					vo = curr;
					break;
				}
			}

		}
		String nomeFile = vo == null ? null : vo.getNomeFile();
		requestMap.put("nomeFile", nomeFile);

		return SUCCESS;
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.menu.98");
	}

	@Override
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return null;
	}

	@Override
	public void saveParameters(Context arg0, String arg1, Map arg2) {

	}

}
