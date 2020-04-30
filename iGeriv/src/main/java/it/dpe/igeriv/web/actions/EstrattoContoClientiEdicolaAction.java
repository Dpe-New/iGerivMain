package it.dpe.igeriv.web.actions;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import it.dpe.igeriv.dto.DateEstrattoContoClienteDto;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per l'estratto conto delle rivendite.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("estrattoContoClientiEdicolaAction")
@SuppressWarnings({ "rawtypes" })
public class EstrattoContoClientiEdicolaAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final ClientiService<ClienteEdicolaDto> clientiService;
	private final String crumbName = getText("igeriv.report.estratto.conto.clienti");
	private final String crumbNameEmEC = getText("igeriv.emissione.estratto.conto.clienti");
	private String tableTitle;
	private String actionName;
	private String strDataDocumento;
	private String strDataCompetenza;
	private String tipiEstrattoConto;
	private Integer tipoProdottiInEstrattoConto;
	private Integer[] arrCodEdicola;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String piva;
	private String nomeCliente;
	private String codCliente;
	private List<KeyValueDto> listEstrattiConto;
	
	public EstrattoContoClientiEdicolaAction() {
		this.clientiService = null;
	}
	
	@Autowired
	public EstrattoContoClientiEdicolaAction(ClientiService<ClienteEdicolaDto> clientiService) {
		this.clientiService = clientiService;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		arrCodEdicola = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getId()};
	}
	
	@Override
	public void validate() {
		tableTitle = getText("igeriv.emissione.estratto.conto.clienti");
		if (!Strings.isNullOrEmpty(getActionName()) && (getActionName().equals("estrattoContoClientiEdicola_showClientiEstrattoConto.action") || getActionName().equals("estrattoContoClientiEdicola_showClientiReportEstrattoConto.action"))) {
			if (Strings.isNullOrEmpty(strDataCompetenza)) {
				addActionError(MessageFormat.format(getText("error.campo.x.obbligatorio.2"), getText("igeriv.data.competenza.estratto.conto")));
				return;
			} else {
				try {
					if (strDataCompetenza.contains(",")) {
						strDataCompetenza = strDataCompetenza.substring(0, strDataCompetenza.indexOf(",")).trim();
					}
					DateUtilities.parseDate(strDataCompetenza, DateUtilities.FORMATO_DATA_SLASH);
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), strDataCompetenza));
					return;
				}
			}
		} 
		if (!Strings.isNullOrEmpty(getActionName()) && getActionName().equals("estrattoContoClientiEdicola_emettiEstrattoConto.action")) {
			if (Strings.isNullOrEmpty(strDataDocumento)) {
				addActionError(MessageFormat.format(getText("error.campo.x.obbligatorio.2"), getText("igeriv.data.documento")));
				return;
			} else {
				try {
					DateUtilities.parseDate(strDataDocumento, DateUtilities.FORMATO_DATA_SLASH);
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), strDataDocumento));
					return;
				}
			}
		}
		if (!Strings.isNullOrEmpty(tipiEstrattoConto) && tipiEstrattoConto.contains(",")) {
			tipiEstrattoConto = tipiEstrattoConto.substring(0, tipiEstrattoConto.indexOf(",")).trim();
		}
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilterViewEstrattoContoClienti() throws Exception {
		tableTitle = getText("igeriv.report.estratto.conto.clienti");
		return "report_ec";
	} 
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showClientiReportEstrattoConto() throws Exception {
		tableTitle = getText("igeriv.report.estratto.conto.clienti");
		List<ClienteEdicolaDto> clienti = clientiService.getClientiEdicolaConEstrattoConto(arrCodEdicola, null, null, true, nome, cognome, codiceFiscale, piva, IGerivConstants.TIPO_PRODOTTO_EDITORIALE, null);
		requestMap.put("clienti", clienti);
		return "report_ec";
	}
	
	@SkipValidation
	public String showSceltaReportEstrattiConto() throws Exception {
		tableTitle = getText("igeriv.report.estratto.conto.clienti");
		requestMap.put("nomeCliente", nomeCliente);
		if (!Strings.isNullOrEmpty(codCliente)) {
			List<DateEstrattoContoClienteDto> dateEstrattiContoCliente = clientiService.getDateReportEstrattiContoCliente(new Long(codCliente));
			listEstrattiConto = new ArrayList<KeyValueDto>();
			for (DateEstrattoContoClienteDto d : dateEstrattiContoCliente) {
				KeyValueDto dto = new KeyValueDto();
				dto.setKey(DateUtilities.getTimestampAsString(d.getDataEstrattoConto(), DateUtilities.FORMATO_DATA_SLASH) + "|" + d.getTipoProdottiInEstrattoConto() + "|" + DateUtilities.getTimestampAsString(d.getDataCompetenzaEstrattoContoClienti(), DateUtilities.FORMATO_DATA_SLASH));
				dto.setValue(DateUtilities.getTimestampAsString(d.getDataCompetenzaEstrattoContoClienti(), DateUtilities.FORMATO_DATA_SLASH));
				listEstrattiConto.add(dto);
			}
		}
		return "popup";
	}
	
	@BreadCrumb("%{crumbNameEmEC}")
	@SkipValidation
	public String showFilter() throws Exception {
		tableTitle = getText("igeriv.emissione.estratto.conto.clienti");
		strDataDocumento = (strDataDocumento == null) ? DateUtilities.getTimestampAsStringExport(new Date(), DateUtilities.FORMATO_DATA_SLASH) : strDataDocumento;
		return SUCCESS;
	} 
	
	/**
	 * @return
	 * @throws Exception
	 */
	@BreadCrumb("%{crumbNameEmEC}")
	public String showClientiEstrattoConto() throws Exception {
		tableTitle = getText("igeriv.emissione.estratto.conto.clienti");
		Timestamp dataCompetenza = DateUtilities.ceilDay(DateUtilities.parseDate(this.strDataCompetenza, DateUtilities.FORMATO_DATA_SLASH));
		List<Integer> tipiEstrattoConto = new ArrayList<Integer>();
		if (!Strings.isNullOrEmpty(this.tipiEstrattoConto)) {
			String[] split = this.tipiEstrattoConto.split("\\|");
			for (String tec : split) {
				String tipoEC = tec.trim();
				if (!Strings.isNullOrEmpty(tipoEC)) {
					tipiEstrattoConto.add(new Integer(tipoEC));
				}
			}
		}
		List<ClienteEdicolaDto> clienti = clientiService.getClientiEdicolaConEstrattoConto(arrCodEdicola, tipiEstrattoConto, dataCompetenza, false, nome, cognome, codiceFiscale, piva, tipoProdottiInEstrattoConto, null);
		if (Strings.isNullOrEmpty(strDataDocumento)) {
			strDataDocumento = DateUtilities.getTimestampAsStringExport(new Date(), DateUtilities.FORMATO_DATA_SLASH);
		}
		requestMap.put("clienti", clienti);
		return SUCCESS;
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

	public List<KeyValueDto> getListTipiEstrattoConto() {
		List<KeyValueDto> listTipiEstrattoConto = new ArrayList<KeyValueDto>();
		KeyValueDto dto = new KeyValueDto();
		dto.setKey(IGerivConstants.TIPO_ESTRATTO_CONTO_CLIENTE_EDICOLA_MENSILE + "|" + IGerivConstants.TIPO_ESTRATTO_CONTO_CLIENTE_EDICOLA_SETTIMANALE);
		dto.setValue(getText("igeriv.tutti"));
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey(IGerivConstants.TIPO_ESTRATTO_CONTO_CLIENTE_EDICOLA_MENSILE.toString());
		dto1.setValue(getText("igeriv.mensile"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey(IGerivConstants.TIPO_ESTRATTO_CONTO_CLIENTE_EDICOLA_SETTIMANALE.toString());
		dto2.setValue(getText("igeriv.settimanale"));
		listTipiEstrattoConto.add(dto);
		listTipiEstrattoConto.add(dto1);
		listTipiEstrattoConto.add(dto2);
		return listTipiEstrattoConto;
	}
	
	public List<KeyValueDto> getListTipiProdottoEstrattoConto() {
		List<KeyValueDto> listTipiEstrattoConto = new ArrayList<KeyValueDto>();
		KeyValueDto dto = new KeyValueDto();
		dto.setKeyInt(IGerivConstants.TIPO_PRODOTTO_EDITORIALE);
		dto.setValue(getText("igeriv.solo.prodotti.editoriali"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKeyInt(IGerivConstants.TIPO_PRODOTTO_MISTO);
		dto2.setValue(getText("igeriv.solo.prodotti.editoriali.e.non"));
		listTipiEstrattoConto.add(dto);
		listTipiEstrattoConto.add(dto2);
		return listTipiEstrattoConto;
	}
	
}
