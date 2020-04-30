package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.sort;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action per il report dei rtiri di un cliente.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@SuppressWarnings({ "rawtypes" })
@Scope("prototype")
@Component("ritiriAction")
public class RitiriAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ClientiService<ClienteEdicolaDto> clientiService;
	@Autowired
	private VenditeService venditeService;
	private List<PubblicazioneDto> listPubblicazioni;
	private List<ClienteEdicolaDto> listClienti;
	private String tableTitle;
	private String actionName;
	private Long codCliente;
	private String strDataDa;
	private String strDataA;
	private String titolo;
	private Boolean hasRitiriCancellati;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		if (getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA)) {
			listClienti = clientiService.getClientiEdicola(getAuthUser().getArrId(), null, null, null, null);
			tableTitle = getText("igeriv.report.ritiri.cliente");
		} else {
			tableTitle = getText("igeriv.menu.75");
		}
	}
	
	@Override
	public void validate() {
		if (Strings.isNullOrEmpty(strDataDa) && Strings.isNullOrEmpty(strDataA)) {
			strDataDa = DateUtilities.getTimestampAsString(DateUtilities.togliGiorni(new Timestamp(new Date().getTime()), 15), DateUtilities.FORMATO_DATA_SLASH);
			strDataA = DateUtilities.getTimestampAsString(new Timestamp(new Date().getTime()), DateUtilities.FORMATO_DATA_SLASH);
		} else if (getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA) && codCliente == null) {
			addActionError(getText("igeriv.selezionare.il.cliente"));
			return;
		} else {
			try {
				DateUtilities.floorDay(DateUtilities.parseDate(strDataDa, DateUtilities.FORMATO_DATA_SLASH));
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), strDataDa));
				return;
			}
			try {
				DateUtilities.ceilDay(DateUtilities.parseDate(strDataA, DateUtilities.FORMATO_DATA_SLASH));
			} catch (ParseException e) {
				addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), strDataA));
				return;
			}
		}
	}
	
	@SkipValidation
	public String showFilter() {
		return SUCCESS;
	} 
	
	public String show() throws ParseException {
		Timestamp dataDa = Strings.isNullOrEmpty(strDataDa) ? null : DateUtilities.floorDay(DateUtilities.parseDate(strDataDa, DateUtilities.FORMATO_DATA_SLASH));
		Timestamp dataA = Strings.isNullOrEmpty(strDataA) ? null : DateUtilities.ceilDay(DateUtilities.parseDate(strDataA, DateUtilities.FORMATO_DATA_SLASH));
		codCliente = getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA) ? codCliente : getAuthUser().getId();
		listPubblicazioni = clientiService.getRitiriCliente(getAuthUser().getArrId(), titolo, dataDa, dataA, codCliente);
		requestMap.put("ritiri", listPubblicazioni);
		hasRitiriCancellati = venditeService.getHasRitiriCancellati(getAuthUser().getArrId(), codCliente);
		return SUCCESS;
	}
	
	@SkipValidation
	public String showRitiriCanc() {
		listPubblicazioni = clientiService.getRitiriClienteCancellati(getAuthUser().getArrId(), codCliente);
		requestMap.put("ritiriCanc", listPubblicazioni);
		return "ritiriCanc";
	}
	
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}
	
	public String getReportTitle() {
		if (codCliente != null 
				&& !Strings.isNullOrEmpty(strDataDa) 
				&& !Strings.isNullOrEmpty(strDataA)) {
			if (getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA)) {
				ClienteEdicolaVo cliente = clientiService.getClienteEdicola(getAuthUser().getArrId(), codCliente);
				return MessageFormat.format(getText("igeriv.report.ritiri.cliente.title"), cliente.getNomeCognome(), strDataDa, strDataA);
			} else {
				return MessageFormat.format(getText("igeriv.report.ritiri.title"), strDataDa, strDataA);
			}
		}
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

	public List<PubblicazioneDto> getListPubblicazioni() {
		return listPubblicazioni;
	}

	public void setListPubblicazioni(List<PubblicazioneDto> listPubblicazioni) {
		this.listPubblicazioni = listPubblicazioni;
	}

	public String getStrDataDa() {
		return strDataDa;
	}

	public void setStrDataDa(String strDataDa) {
		this.strDataDa = strDataDa;
	}

	public String getStrDataA() {
		return strDataA;
	}

	public void setStrDataA(String strDataA) {
		this.strDataA = strDataA;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public Long getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(Long codCliente) {
		this.codCliente = codCliente;
	}
	
	public List<ClienteEdicolaDto> getListClienti() {
		return listClienti;
	}

	public List<ClienteEdicolaDto> getListClientiConEC() {
		List<ClienteEdicolaDto> list = new ArrayList<ClienteEdicolaDto>();
		if (listClienti != null && !listClienti.isEmpty()) {
			list = sort(select(listClienti, having(on(ClienteEdicolaDto.class).getTipoEstrattoConto(), notNullValue())), on(ClienteEdicolaDto.class).getNomeSort());
		}
		return list;
	}
	
	public List<ClienteEdicolaDto> getListClientiSenzaEC() {
		List<ClienteEdicolaDto> list = new ArrayList<ClienteEdicolaDto>();
		if (listClienti != null && !listClienti.isEmpty()) {
			list = sort(select(listClienti, having(on(ClienteEdicolaDto.class).getTipoEstrattoConto(), nullValue())), on(ClienteEdicolaDto.class).getNomeSort());
		}
		return list;
	}

	public Boolean getHasRitiriCancellati() {
		return hasRitiriCancellati;
	}

	public void setHasRitiriCancellati(Boolean hasRitiriCancellati) {
		this.hasRitiriCancellati = hasRitiriCancellati;
	}

	public ClientiService<ClienteEdicolaDto> getClientiService() {
		return clientiService;
	}

	public void setClientiService(ClientiService<ClienteEdicolaDto> clientiService) {
		this.clientiService = clientiService;
	}

	public VenditeService getVenditeService() {
		return venditeService;
	}

	public void setVenditeService(VenditeService venditeService) {
		this.venditeService = venditeService;
	}
	
}
