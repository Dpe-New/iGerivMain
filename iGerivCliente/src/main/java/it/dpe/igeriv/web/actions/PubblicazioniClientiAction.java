package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.BollaVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@SuppressWarnings({ "rawtypes" })
@Scope("prototype")
@Component("pubblicazioniClientiAction")
public class PubblicazioniClientiAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State, RequestAware {

	private static final long serialVersionUID = -7508211228476534290L;
	@Autowired
	private PubblicazioniService pubblicazioniService;
	@Autowired
	private EdicoleService edicoleService;
	private String tableTitle;
	private String titolo;
	private String sottotitolo;
	private String argomento;
	private String periodicita;
	private String prezzo;
	private String codPubblicazione;
	private String codBarre;
	private String idtn;
	private String coddl;
	private String data;

	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		tableTitle = getText("igeriv.ricerca.pubblicazioni");
		if (!Strings.isNullOrEmpty(getActionName()) && getActionName().equals("pubblicazioniClienti_showPubblicazioni.action")) {
			if ((titolo == null || titolo.equals("")) && (sottotitolo == null || sottotitolo.equals("")) && (argomento == null || argomento.equals(""))
					&& (periodicita == null || periodicita.equals("")) && (prezzo == null || prezzo.equals("")) 
					&& (codPubblicazione == null || codPubblicazione.equals("")) && (codBarre == null || codBarre.equals(""))) {
				addActionError(getText("error.criterio.ricerca"));
			}
			if (titolo != null && !titolo.equals("") && (sottotitolo == null || sottotitolo.equals(""))) {
				if (titolo.trim().length() < 1) {
					addActionError(getText("error.ricerca.per.titolo.almeno.un.carattere"));
				}
			}
			if (sottotitolo != null && !sottotitolo.equals("") && (titolo == null || titolo.equals(""))) {
				if (sottotitolo.trim().length() < 1) {
					addActionError(getText("error.ricerca.per.titolo.almeno.un.carattere"));
				}
			}
			if (prezzo != null && !prezzo.equals("")) {
				int commaPos = prezzo.lastIndexOf(",");
				int dotPos = prezzo.lastIndexOf(".");
				if (dotPos > commaPos) {
					prezzo = prezzo.replaceAll(",", "");
				} else if (commaPos > dotPos) {
					prezzo = prezzo.replaceAll("\\.", "");
					if (prezzo.contains(",")) {
						prezzo = prezzo.replace(',', '.');
					}
				}
				if (!NumberUtils.isNumber(prezzo)) {
					addFieldError("prezzo", getText("xwork.default.invalid.fieldvalue"));
				}
				if ((titolo == null || titolo.equals("")) && (sottotitolo == null || sottotitolo.equals("")) && (argomento == null || argomento.equals(""))
						&& (periodicita == null || periodicita.equals("")) && (codPubblicazione == null || codPubblicazione.equals("")) 
						&& (codBarre == null || codBarre.equals(""))) {
					addActionError(getText("error.criterio.ricerca.prezzo"));
				}
			}
			if (periodicita != null && !periodicita.equals("")) {
				if ((titolo == null || titolo.equals("")) && (sottotitolo == null || sottotitolo.equals("")) && (argomento == null || argomento.equals(""))
						&& (codPubblicazione == null || codPubblicazione.equals("")) && (prezzo == null || prezzo.equals("")) 
						&& (codBarre == null || codBarre.equals(""))) {
					addActionError(getText("error.criterio.ricerca.periodicita"));
				}
			}
			if (codPubblicazione != null && !codPubblicazione.equals("")) {
				if (!NumberUtils.isNumber(codPubblicazione)) {
					addFieldError("codPubblicazione", getText("xwork.default.invalid.fieldvalue"));
				}
				
			}
			if (codBarre != null && !codBarre.equals("")) {
				if (!NumberUtils.isNumber(codBarre)) {
					addFieldError("codBarre", getText("xwork.default.invalid.fieldvalue"));
				}
			}
		}
		else if (!Strings.isNullOrEmpty(getActionName()) && getActionName().equals("pubblicazioniClientiUsciteData_showPubblicazioniUsciteDataClienti.action")) {
			tableTitle = getText("igeriv.menu.84");
			if (!Strings.isNullOrEmpty(data)) {
				try {
					DateUtilities.floorDay(DateUtilities.parseDate(data, DateUtilities.FORMATO_DATA_SLASH));
				} catch (ParseException e) {
					addActionError(MessageFormat.format(getText("msg.formato.data.invalido"), data));
					return;
				}
			}
			else {
				addActionError(getText("error.specificare.data.o.intervallo.date"));
			}
		}
	}
	
	@SkipValidation
	public String showFilterRicercaPubEdicola() throws Exception {
		tableTitle = getText("igeriv.ricerca.pubblicazioni");
		
		return "ricercaPubEdicolaInput";
	}
	
	@SkipValidation
	public String showFilterPubUsciteData() throws Exception {
		tableTitle = getText("igeriv.menu.84");
	
		return "pubUsciteDataInput";
	}
	
	public String showPubblicazioni() {
		tableTitle = getText("igeriv.ricerca.pubblicazioni");
		Integer codPubblicazioneInt = (codPubblicazione != null && !codPubblicazione.equals("")) ? Integer.parseInt(codPubblicazione) : null;
		BigDecimal prezzoBd = (prezzo != null && !prezzo.equals("")) ? new BigDecimal(prezzo) : null;
		Integer[] codDl = ((getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) || (getAuthUser().isDlInforiv() && getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA))) ? getAuthUser().getArrCodFiegDl() : new Integer[]{getAuthUser().getCodFiegDl()};
		List<PubblicazioneDto>  listPubblicazioni = pubblicazioniService.getCopertine(true, false, false, getAuthUser().getCodEdicolaMaster(), codDl, getAuthUser().getArrId(), titolo, sottotitolo, argomento, periodicita, prezzoBd, codPubblicazioneInt, codBarre, false, null, null, false, null, codPubblicazioneInt);
		requestMap.put("listPubblicazioni", listPubblicazioni);
		
		return "pubblicazioni";
	}
	
	@SkipValidation
	public String showPubblicazioneEdicola() throws Exception {
		Integer idtn = Integer.valueOf(this.idtn);
		Integer coddl = Integer.valueOf(this.coddl);
		
		PubblicazioneDto pubblicazione = pubblicazioniService.getCopertinaByIdtn(coddl, idtn);
		requestMap.put("pubblicazione", pubblicazione);
		
		List<AbbinamentoEdicolaDlVo> edicole = edicoleService.getEdicoleConPubblicazioneInGiacenza(coddl, idtn);
		requestMap.put("listEdicole", edicole);
		
		return "pubblicazioniEdicola";
	}
	
	public String showPubblicazioniUsciteDataClienti() throws Exception {
		tableTitle = getText("igeriv.menu.84");
		
		Timestamp dataRicerca = DateUtilities.floorDay(DateUtilities.parseDate(data, DateUtilities.FORMATO_DATA_SLASH));
		Integer coddl = getAuthUser().getCodFiegDl();
		
		List<PubblicazioneDto> listPubblicazioni = pubblicazioniService.getCopertineUsciteData(dataRicerca, coddl);
		requestMap.put("listPubblicazioni", listPubblicazioni);
		
		return "pubblicazioniUsciteData";
	}
	
	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}
	
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	public String getSottotitolo() {
		return sottotitolo;
	}

	public void setSottotitolo(String sottotitolo) {
		this.sottotitolo = sottotitolo;
	}

	public String getArgomento() {
		return argomento;
	}

	public void setArgomento(String argomento) {
		this.argomento = argomento;
	}

	public String getPeriodicita() {
		return periodicita;
	}

	public void setPeriodicita(String periodicita) {
		this.periodicita = periodicita;
	}

	public String getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	public String getCodPubblicazione() {
		return codPubblicazione;
	}

	public void setCodPubblicazione(String codPubblicazione) {
		this.codPubblicazione = codPubblicazione;
	}

	public String getCodBarre() {
		return codBarre;
	}

	public void setCodBarre(String codBarre) {
		this.codBarre = codBarre;
	}
	
	public String getIdtn() {
		return idtn;
	}

	public void setIdtn(String idtn) {
		this.idtn = idtn;
	}

	public String getCoddl() {
		return coddl;
	}

	public void setCoddl(String coddl) {
		this.coddl = coddl;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	@Override
	public void saveParameters(Context arg0, String arg1, Map arg2) {
	}

}
