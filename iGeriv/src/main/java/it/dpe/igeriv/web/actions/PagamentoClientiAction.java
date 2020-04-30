package it.dpe.igeriv.web.actions;

import java.sql.Timestamp;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.EstrattoContoClientiEdicolaDto;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione dei pagamenti dei clienti.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("pagamentoClientiAction")
@SuppressWarnings({ "rawtypes" })
public class PagamentoClientiAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final ClientiService<ClienteEdicolaDto> clientiService;
	private final String crumbName = getText("igeriv.gestione.pagamenti.clienti");
	private List<EstrattoContoClientiEdicolaDto> listEstrattoContoDettaglio;
	private String tableTitle;
	private String actionName;
	private String strDataCompetenzaDa;
	private String strDataCompetenzaA;
	private Integer[] arrCodEdicola;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String piva;
	private String codCliente;
	private List<KeyValueDto> listEstrattiConto;
	private String spunte;
	private String contiDaPagare; 
	
	public PagamentoClientiAction() {
		this.clientiService = null;
	}
	
	@Autowired
	public PagamentoClientiAction(ClientiService<ClienteEdicolaDto> clientiService) {
		this.clientiService = clientiService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		arrCodEdicola = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getId()};
	}
	
	@Override
	public void validate() {
		tableTitle = getText("igeriv.gestione.pagamenti.clienti");
		if (!Strings.isNullOrEmpty(nome) && nome.contains(",")) {
			nome = nome.substring(0, nome.indexOf(","));
		}
		if (!Strings.isNullOrEmpty(cognome) && cognome.contains(",")) {
			cognome = cognome.substring(0, cognome.indexOf(","));
		}
		if (!Strings.isNullOrEmpty(codiceFiscale) && codiceFiscale.contains(",")) {
			codiceFiscale = codiceFiscale.substring(0, codiceFiscale.indexOf(","));
		}
		if (!Strings.isNullOrEmpty(piva) && piva.contains(",")) {
			piva = piva.substring(0, piva.indexOf(","));
		}
		if (!Strings.isNullOrEmpty(contiDaPagare) && contiDaPagare.contains(",")) {
			contiDaPagare = contiDaPagare.substring(0, contiDaPagare.indexOf(","));
		}
		if (!Strings.isNullOrEmpty(strDataCompetenzaDa) && strDataCompetenzaDa.contains(",")) {
			strDataCompetenzaDa = strDataCompetenzaDa.substring(0, strDataCompetenzaDa.indexOf(","));
		}
		if (!Strings.isNullOrEmpty(strDataCompetenzaA) && strDataCompetenzaA.contains(",")) {
			strDataCompetenzaA = strDataCompetenzaA.substring(0, strDataCompetenzaA.indexOf(","));
		}
	}
	
	@SkipValidation
	public String showFilter() {
		tableTitle = getText("igeriv.gestione.pagamenti.clienti");
		return SUCCESS;
	} 
	
	public String showClienti() throws ParseException {
		tableTitle = getText("igeriv.gestione.pagamenti.clienti");
		List<ClienteEdicolaDto> clienti = null;
		Boolean daPagare = Boolean.valueOf(Strings.isNullOrEmpty(contiDaPagare) ? "true" : contiDaPagare);
		Timestamp dataCompetenzaDa = Strings.isNullOrEmpty(strDataCompetenzaDa) ? null : DateUtilities.floorDay(DateUtilities.parseDate(strDataCompetenzaDa, DateUtilities.FORMATO_DATA_SLASH));
		Timestamp dataCompetenzaA = Strings.isNullOrEmpty(strDataCompetenzaA) ? null : DateUtilities.ceilDay(DateUtilities.parseDate(strDataCompetenzaA, DateUtilities.FORMATO_DATA_SLASH));
		clienti = clientiService.getClientiEdicolaConPagamentiPendenti(arrCodEdicola, nome, cognome, codiceFiscale, piva, daPagare, dataCompetenzaDa, dataCompetenzaA);
		requestMap.put("clienti", clienti);
		return SUCCESS;
	}
	
	@SkipValidation
	public String savePagamentoClienti() {
		try {
			if (!Strings.isNullOrEmpty(spunte)) {
				String[] arrCodVenditaStr = spunte.split(",");
				List<Long> listCodCliente = new ArrayList<Long>();
				List<Timestamp> listDataCompetenzaEstrattoConto = new ArrayList<Timestamp>();
				for (String codClienteStr : arrCodVenditaStr) {
					String[] split = codClienteStr.split("\\|");
					listCodCliente.add(new Long(split[0].trim()));
					listDataCompetenzaEstrattoConto.add(DateUtilities.parseDate(split[1].trim(), DateUtilities.FORMATO_DATA_YYYY_MM_DD_HHMMSS));
				}
				clientiService.updatePagamentoClienti(listCodCliente, listDataCompetenzaEstrattoConto, true);
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}
	
	
	@Override
	public String getTitle() {
		String title = getText("igeriv.emissione.estratto.conto.clienti");
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

	public List<KeyValueDto> getListContiDaPagare() {
		List<KeyValueDto> list = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("true");
		dto1.setValue(getText("igeriv.solo.estratti.da.pagare"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("false");
		dto2.setValue(getText("igeriv.solo.estratti.pagati"));
		list.add(dto1);
		list.add(dto2);
		return list;
	}
	
}
