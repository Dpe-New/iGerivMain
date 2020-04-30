package it.dpe.igeriv.web.actions;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.RichiestaClienteVo;
import it.dpe.igeriv.vo.RichiestaFissaClienteEdicolaVo;
import it.dpe.igeriv.vo.pk.RichiestaClientePk;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per i clienti dell'edicola.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("clientiEdicolaAction")
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class ClientiEdicolaAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 1L;
	private final RifornimentiService rifornimentiService;
	private final IGerivUtils iGerivUtils;
	private final String crumbName = getText("igeriv.prenotazioni.clienti");
	private String tipoOperazione;
	private List<RichiestaClienteVo> richiesteCliente;
	private RichiestaFissaClienteEdicolaVo richiestaFissaClienteVo;
	private String idtn;
	private String filterTitle;
	private String tableHeight;
	private String actionName1;
	private String pk;
	private String dataScadenzaRichiesta;
	private String quantitaRifornimento;
	private Integer validitaVariazioneServizio;
	private String noteVendita;
	private String titolo;
	private String stato;
	private String prenotazioneDisabled;
	private String coddl;
	private String dataDa;
	private String dataA;
	
	public ClientiEdicolaAction() {
		this.rifornimentiService = null;
		this.iGerivUtils = null;
	}
	
	@Autowired
	public ClientiEdicolaAction(RifornimentiService rifornimentiService, IGerivUtils iGerivUtils) {
		this.rifornimentiService = rifornimentiService;
		this.iGerivUtils = iGerivUtils;
	}
	
	@SkipValidation
	public String showFilter() throws Exception {
		return SUCCESS;
	}

	@SkipValidation
	public String showPrenotazioniClienteEdicola() throws Exception {
		filterTitle = getText("igeriv.prenotazioni.clienti");
		requestMap.put("title", filterTitle);
		if (idtn != null && !idtn.equals("")) {
			Integer coddl = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getCodFiegDl() : new Integer(this.coddl);
			Integer codEdicola = Strings.isNullOrEmpty(this.coddl) ? getAuthUser().getCodDpeWebEdicola() : iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId());
			List<RichiestaRifornimentoDto> ordini = rifornimentiService.getRichiesteCliente(
					coddl,
					getAuthUser().getArrCodFiegDl(),
					getAuthUser().getArrId(),
					new Integer(idtn),
					new Long(getAuthUser().getId()),
					IGerivConstants.PROVENIENZA_CLIENTE);
			requestMap.put("ordini", ordini);
			tableHeight = getTabHeight(ordini);
			if (richiestaFissaClienteVo == null) {
				RichiestaRifornimentoDto rfvo = (RichiestaRifornimentoDto) ordini.get(0);
				richiestaFissaClienteVo = rifornimentiService.getRichiestaFissaClienteVo(coddl, codEdicola, getAuthUser().getArrId(), new Long(getAuthUser().getId()), getAuthUser().getArrCodFiegDl(), rfvo.getCodicePubblicazione());
				if (!richiestaFissaClienteVo.isEnabled()) {
					prenotazioneDisabled = "true";
				}
			}
		}
		String titoloRichiesteRifornimento = MessageFormat.format(getText("igeriv.prenotazioni.del"), DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH));
		String cliente	= getText("igeriv.provenienza.evasione.cliente") + " " + getAuthUser().getNome().toUpperCase().replaceAll("&nbsp;", " ").replaceAll("\\.", " ") + " "  +getAuthUser().getCognome().toUpperCase().replaceAll("&nbsp;", " ").replaceAll("\\.", " ");
		requestMap.put("titoloRichiesteRifornimento", titoloRichiesteRifornimento);
		requestMap.put("cliente", cliente);
		return IGerivConstants.ACTION_PRENOTAZIONE_CLIENTI;
	}
	
	@SkipValidation
	public String showPrenotazioniInseriteClienteEdicola() throws Exception {
		filterTitle = getText("igeriv.prenotazioni.clienti");
		requestMap.put("title", filterTitle);
		requestMap.put("tipoRifornimento", "clienteEdicola");
		List listRichieste = new ArrayList();
		if (Strings.isNullOrEmpty(this.dataDa) || Strings.isNullOrEmpty(this.dataA)) {
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtilities.FORMATO_DATA_SLASH);
			this.dataDa = sdf.format(buildDefaultDataDa());
			this.dataA = sdf.format(new Date());
		}
		Timestamp dataDa = DateUtilities.floorDay(DateUtilities.parseDate(this.dataDa, DateUtilities.FORMATO_DATA_SLASH));
		Timestamp dataA = DateUtilities.ceilDay(DateUtilities.parseDate(this.dataA, DateUtilities.FORMATO_DATA_SLASH));
		if (stato != null && !stato.equals("") ) {
			Integer statoInt = new Integer(stato); 
			if (statoInt.equals(IGerivConstants.PRENOTAZIONI_EVASE) || statoInt.equals(IGerivConstants.PRENOTAZIONI_INSERITE)) {
				listRichieste = rifornimentiService.getRichiesteClienteByIdClienteViewOnly(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), Arrays.asList(new Long[]{new Long(getAuthUser().getId())}), titolo, stato, null, dataDa, dataA, false);
			} 
			else if (statoInt.equals(IGerivConstants.PRENOTAZIONI_FISSE)) {
				listRichieste = rifornimentiService.getRichiesteClienteFisseByIdCliente(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), new Long(getAuthUser().getId()), titolo, stato, dataDa, dataA, false);
			} 
		} else {
			listRichieste = rifornimentiService.getRichiesteClienteByIdClienteViewOnly(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), Arrays.asList(new Long[]{new Long(getAuthUser().getId())}), titolo, stato, null, dataDa, dataA, false);
			listRichieste.addAll(rifornimentiService.getRichiesteClienteFisseByIdCliente(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), new Long(getAuthUser().getId()), titolo, stato, dataDa, dataA, false));
		}
		requestMap.put("richiesteRifornimento", listRichieste);
		String titoloRichiesteRifornimento = MessageFormat.format(getText("igeriv.prenotazioni.del"), DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH));
		String cliente	= getText("igeriv.provenienza.evasione.cliente") + " " + getAuthUser().getNome().toUpperCase().replaceAll("&nbsp;", " ").replaceAll("\\.", " ") + " "  +getAuthUser().getCognome().toUpperCase().replaceAll("&nbsp;", " ").replaceAll("\\.", " ");
		requestMap.put("titoloRichiesteRifornimento", titoloRichiesteRifornimento);
		requestMap.put("cliente", cliente);
		tableHeight = getTabHeight(listRichieste);
		return IGerivConstants.ACTION_VIEW_PRENOTAZIONI_INSERITE_CLIENTI_EDICOLA;
	}
	
	/**
	 * Ritorna oggi - 90 giorni
	 * @return
	 */
	private Timestamp buildDefaultDataDa() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -90);
		return DateUtilities.floorDay(cal.getTime());
	}

	public String savePrenotazioniClienteEdicola() {
		try {
			filterTitle = getText("igeriv.prenotazioni.clienti");
			Set<String> pkSet = buildSet(pk);
			List<RichiestaClienteVo> richiestaRifornimento = rifornimentiService.getRichiesteClienteByPk(
					getAuthUser().getArrCodFiegDl(), 
					getAuthUser().getArrId(),
					new Long(getAuthUser().getId()), 
					IGerivConstants.COD_PROVENIENZA_RICHIESTA_CLIENTE,
					pkSet);
			List<RichiestaClienteVo> listRichiestaRifornimentoVo = buildListRichiestaClienteVo(richiestaRifornimento, pkSet, quantitaRifornimento);
			if (richiestaFissaClienteVo.getQuantitaRichiesta() != null && richiestaFissaClienteVo.getQuantitaRichiesta() < 0) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("dpe.validation.msg.qta.richiesta.fissa.negativa") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			rifornimentiService.saveRichiestaRifornimentoClienteEdicola(listRichiestaRifornimentoVo, richiestaFissaClienteVo);
		} catch (IGerivRuntimeException e) {
			throw e;
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return IGerivConstants.ACTION_PRENOTAZIONE_CLIENTI;
	}
	
	/** 
	 * Ritorna la lista di RichiestaClienteVo caricata dal db 
	 * con gli attributi introdotti dall'utente via form.
	 * 
	 * @param List<RichiestaRifornimentoVo> richiestaRifornimento
	 * @param Set pk
	 * @param String quantitaRifornimento
	 * @return List<RichiestaRifornimentoVo>
	 * @throws ParseException
	 */
	private List<RichiestaClienteVo> buildListRichiestaClienteVo(
			List<RichiestaClienteVo> richiestaRifornimento, Set<String> pkSet, String quantitaRifornimento) throws ParseException {
		Set<RichiestaClienteVo> set = new HashSet<RichiestaClienteVo>(); 
		String[] arrQtaRichiesta = (quantitaRifornimento == null) ? new String[]{""} : quantitaRifornimento.split(",");
		List<String> listPks = new ArrayList<String>();
		listPks.addAll(pkSet);
		for (RichiestaClienteVo vo : richiestaRifornimento) {
			String pkVal = vo.getPk().toString().trim();
			for (int i = 0; i < arrQtaRichiesta.length; i++) {
				if (pkVal.equals(listPks.get(i).toString())) {
					Integer quantitaRichiesta = (arrQtaRichiesta[i] != null && !arrQtaRichiesta[i].trim().equals("")) ? new Integer(arrQtaRichiesta[i].trim()) : 0;
					vo.setQuantitaRichiesta(quantitaRichiesta);
					vo.setNotificareOrdineRivendita(true);
					set.add(vo);
					break;
				}
			}
		}
		for (int i = 0; i < arrQtaRichiesta.length; i++) {
			if (arrQtaRichiesta[i] != null && !arrQtaRichiesta[i].trim().equals("")) {
				String[] pkArrVal = listPks.get(i).toString().split("\\|");
				RichiestaClienteVo vo = new RichiestaClienteVo();
				RichiestaClientePk pk = new RichiestaClientePk();
				pk.setCodDl(new Integer(pkArrVal[4]));
				pk.setCodEdicola(new Integer(pkArrVal[0]));
				pk.setCodCliente(new Long(getAuthUser().getId()));
				pk.setDataInserimento(DateUtilities.parseDate(pkArrVal[3], DateUtilities.FORMATO_DATA_YYYY_MM_DD_HHMMSS));
				pk.setIdtn(new Integer(pkArrVal[5]));
				pk.setProvenienza(new Integer(pkArrVal[2]));
				vo.setPk(pk);
				vo.setQuantitaRichiesta(new Integer(arrQtaRichiesta[i].trim()));
				vo.setNotificareOrdineRivendita(true);
				vo.setQuantitaEvasa(0);
				vo.setStatoEvasione(IGerivConstants.COD_STATO_EVASIONE_INSERITO);
				vo.setRichiedereDifferenzaDl(IGerivConstants.COD_RICHIEDERE_DIFFERENZA_DL_NO);
				set.add(vo);
			}
		}
		return new ArrayList<RichiestaClienteVo>(set);
	}

	/**
	 * Ritorna la lista di RichiestaClienteVo caricata dal db con gli attributi
	 * introdotti dall'utente via form.
	 * 
	 * @param List
	 *            <RichiestaRifornimentoVo> richiestaRifornimento
	 * @param String
	 *            pk
	 * @param String
	 *            quantitaRifornimento
	 * @return List<RichiestaRifornimentoVo>
	 * @throws ParseException
	 */
	private List<RichiestaClienteVo> buildListRichiestaClienteVo(
			List<RichiestaClienteVo> richiestaRifornimento, String pk,
			String quantitaRifornimento) throws ParseException {
		List<RichiestaClienteVo> retList = new ArrayList<RichiestaClienteVo>();
		String[] arrPks = pk.split(",");
		String[] arrQtaRichiesta = quantitaRifornimento.split(",");
		List<String> listPks = new ArrayList<String>();
		for (String pk1 : arrPks) {
			if (!listPks.contains(pk1.trim())) {
				listPks.add(pk1.trim());
			}
		}
		for (RichiestaClienteVo vo : richiestaRifornimento) {
			String pkVal = vo.getPk().toString().trim();
			for (int i = 0; i < arrQtaRichiesta.length; i++) {
				if (pkVal.equals(listPks.get(i).toString())) {
					vo.setQuantitaRichiesta(new Integer(arrQtaRichiesta[i]
							.trim()));
					retList.add(vo);
					break;
				}
			}
		}
		return retList;
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
		return super.getTitle() + getText("igeriv.prenotazioni.clienti");
	}
}
