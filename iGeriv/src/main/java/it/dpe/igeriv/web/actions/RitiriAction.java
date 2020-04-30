package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.sort;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per il report dei rtiri di un cliente.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("ritiriAction")
@SuppressWarnings({ "rawtypes" })
public class RitiriAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final ClientiService<ClienteEdicolaDto> clientiService;
	private final VenditeService venditeService;
	private final String crumbName = getText("igeriv.report.ritiri.cliente");
	private List<PubblicazioneDto> listPubblicazioni;
	private List<ClienteEdicolaDto> listClienti;
	private String tableTitle;
	private String actionName;
	private Long codCliente;
	private String strDataDa;
	private String strDataA;
	private String titolo;
	private Boolean hasRitiriCancellati;
	private EdicoleService edicoleService;
	
	
	public RitiriAction() {
		this.clientiService = null;
		this.venditeService = null;
		this.edicoleService = null;
	}
	
	@Autowired
	public RitiriAction(ClientiService<ClienteEdicolaDto> clientiService, VenditeService venditeService,EdicoleService edicoleService) {
		this.clientiService = clientiService;
		this.venditeService = venditeService;
		this.edicoleService = edicoleService;
	}
	
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
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showFilter() {
		return SUCCESS;
	} 
	
	@BreadCrumb("%{crumbName}")
	public String show() throws ParseException {
		Timestamp dataDa = Strings.isNullOrEmpty(strDataDa) ? null : DateUtilities.floorDay(DateUtilities.parseDate(strDataDa, DateUtilities.FORMATO_DATA_SLASH));
		Timestamp dataA = Strings.isNullOrEmpty(strDataA) ? null : DateUtilities.ceilDay(DateUtilities.parseDate(strDataA, DateUtilities.FORMATO_DATA_SLASH));
		codCliente = getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA) ? codCliente : getAuthUser().getId();
		
		ParametriEdicolaVo paramModalitaRicerca = edicoleService.getParametroEdicola(getAuthUser().getCodEdicolaMaster(), IGerivConstants.COD_PARAMETRO_RICERCA_MODALITA_CONTENUTO);
		if (paramModalitaRicerca != null && paramModalitaRicerca.getValue().equals("true")) {
			titolo = (titolo != null && !titolo.equals(""))?"%"+titolo+"%":titolo;
		}
		
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

}
