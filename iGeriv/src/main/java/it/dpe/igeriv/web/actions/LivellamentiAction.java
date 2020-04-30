package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.on;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.livellamenti.LivellamentiService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.dto.LivellamentiDto;
import it.dpe.igeriv.enums.StatoRichiestaLivellamento;
import it.dpe.igeriv.enums.StatoRichiestaPropostaFilterLivellamento;
import it.dpe.igeriv.enums.StatoRichiestaRifornimentoLivellamento;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.LivellamentiVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoLivellamentiVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.pk.MessaggioPk;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione dei livellamenti edicole.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("livellamentiAction")
@SuppressWarnings({ "rawtypes" })
public class LivellamentiAction extends RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final String crumbNameRifo = getText("igeriv.richieste.rifornimento.rete.edicola");
	private final String crumbNameProp = getText("igeriv.proposte.vendita.rete.edicola");
	private final LivellamentiService<LivellamentiVo> livellamentiService;
	private final MessaggiService messaggiService;
	private final EdicoleService edicoleService;
	private String listIdsLivellamenti;
	private List<LivellamentiVo> listLivellamenti;
	private List<RichiestaRifornimentoLivellamentiVo> listRichiesteRifornimenti;
	private String spunte;
	private String strDataDa;
	private String strDataA;
	private Integer stato;
	
	public LivellamentiAction() {
		this(null,null,null);
	}
	
	@Autowired
	public LivellamentiAction(LivellamentiService<LivellamentiVo> livellamentiService, MessaggiService messaggiService, EdicoleService edicoleService) {
		this.livellamentiService = livellamentiService;
		this.messaggiService = messaggiService;
		this.edicoleService = edicoleService;
	}
	
	@Override
	public void validate() {
		if (Strings.isNullOrEmpty(strDataDa)) {
			strDataDa = DateUtilities.getTimestampAsString(DateUtilities.togliGiorni(new Timestamp(new Date().getTime()), 15), DateUtilities.FORMATO_DATA_SLASH);
		} 
		if (Strings.isNullOrEmpty(strDataA)) {
			strDataA = DateUtilities.getTimestampAsString(new Timestamp(new Date().getTime()), DateUtilities.FORMATO_DATA_SLASH);
		} 
		if (!Strings.isNullOrEmpty(strDataDa) && !Strings.isNullOrEmpty(strDataA)) {
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
	public String showAvvisoLivellamenti() {
		return "showAvvisoLivellamenti";
	}
	
	@SkipValidation
	public String showAvvisoAccettazioneLivellamenti() {
		return "showAvvisoAccettazioneLivellamenti";
	}
	
	@BreadCrumb("%{crumbNameRifo}")
	@SkipValidation
	public String showFilterRichieste() {
		return "showRichiesteLivellamenti";
	}
	
	@BreadCrumb("%{crumbNameProp}")
	@SkipValidation
	public String showFilterProposte() {
		return "showProposteLivellamenti";
	}
	
	@BreadCrumb("%{crumbNameRifo}")
	public String showRichieste() throws ParseException {
		Timestamp dataDa = Strings.isNullOrEmpty(strDataDa) ? null : DateUtilities.floorDay(DateUtilities.parseDate(strDataDa, DateUtilities.FORMATO_DATA_SLASH));
		Timestamp dataA = Strings.isNullOrEmpty(strDataA) ? null : DateUtilities.ceilDay(DateUtilities.parseDate(strDataA, DateUtilities.FORMATO_DATA_SLASH));
		//listLivellamenti = livellamentiService.getRichiesteRifornimentoLivellamentiByDateStato(getAuthUser().getCodEdicolaMaster(), dataDa, dataA, StatoRichiestaPropostaFilterLivellamento.values()[stato]);
		listRichiesteRifornimenti = livellamentiService.getRichiesteRifornimentiLivellamentiByDateStato(getAuthUser().getCodEdicolaMaster(), dataDa, dataA, StatoRichiestaRifornimentoLivellamento.values()[stato]);
		
		requestMap.put("richieste", listRichiesteRifornimenti);
		return "showRichiesteLivellamenti";
	}
	
	@BreadCrumb("%{crumbNameProp}")
	public String showProposte() throws ParseException {
		Timestamp dataDa = Strings.isNullOrEmpty(strDataDa) ? null : DateUtilities.floorDay(DateUtilities.parseDate(strDataDa, DateUtilities.FORMATO_DATA_SLASH));
		Timestamp dataA = Strings.isNullOrEmpty(strDataA) ? null : DateUtilities.ceilDay(DateUtilities.parseDate(strDataA, DateUtilities.FORMATO_DATA_SLASH));
		listLivellamenti = livellamentiService.getProposteRifornimentoLivellamentiByDateStato(getAuthUser().getCodEdicolaMaster(), dataDa, dataA, StatoRichiestaPropostaFilterLivellamento.values()[stato]);
		requestMap.put("proposte", listLivellamenti);
		return "showProposteLivellamenti";
	}
	
	//EDICOLA VENDITRICE FORM PER LA SCELTA DELLE PUBBLICAZIONI CHE SI SCEGLIE O MENO DI VENDERE IN RETE EDICOLA
	@SkipValidation
	public String showLivellamenti() {
		List<Long> listIds = buildListIdLivellamentiList(listIdsLivellamenti);
		listLivellamenti = livellamentiService.getLivellamentiByIdLivellamenti(listIds);
		return "showLivellamenti";
	}
	
	//EDICOLA RICHIEDENTE RICEVE MESSAGGIO DI AGGIORNAMENTO STATO RICHIESTA RETE EDICOLA
	@SkipValidation
	public String showAggiornamentoRichiestaRifornimentiLivellamenti() {
		List<Long> listIds = buildListIdLivellamentiList(listIdsLivellamenti);
		listLivellamenti = livellamentiService.getLivellamentiByIdRichiestaLivellamento(listIds);
		return "showAggiornamentoRichiestaRifornimentiLivellamenti";
	}
	
	//EDICOLA RICHIEDENTE FASE DI ACCETTAZIONE PROPOSTA VENDITE
	@SkipValidation
	public String showAccettazioneLivellamenti() {
		List<Long> listIds = buildListIdLivellamentiList(listIdsLivellamenti);
		//VERIFICO SE CI SONO RICHIESTA ACCETTATE
		List<LivellamentiVo> listLivellamentiAccettati = livellamentiService.getAllLivellamentiAccettatiByIdRichiestaLivellamento(listIds);
		if(listLivellamentiAccettati!=null && !listLivellamentiAccettati.isEmpty()){
			
			if(listLivellamentiAccettati.get(0).getRichiesta().getStato().equals(StatoRichiestaRifornimentoLivellamento.DA_RITIRARE)){
				//DA RITIRATE
				listLivellamenti = livellamentiService.getLivellamentiAccettatiDaRitirareByIdRichiesta(listIds);
				return "showRichiesteDaRitirareInReteEdicola";
			}else if(listLivellamentiAccettati.get(0).getRichiesta().getStato().equals(StatoRichiestaRifornimentoLivellamento.RITIRATI)){
				//RITIRATE
				listLivellamenti = livellamentiService.getLivellamentiAccettatiRitiratiByIdRichiesta(listIds);
				return "showRichiesteRitirateInReteEdicola";
			}else{
				//INSERITE DA ACCETTARE - SE ESISTONO RICHIESTE ACCETTATE DA CONFERMARE DA PARTE DELL'EDICOLA RICHIEDENTE
				listLivellamenti = livellamentiService.getAllLivellamentiAccettatiByIdRichiestaLivellamento(listIds);
				return "showRichiesteDaAccettareInReteEdicola"; //showAccettazioneLivellamenti
			}
		}else{
			//INSERITE
			listLivellamenti = livellamentiService.getLivellamentiByIdRichiestaLivellamento(listIds);
			return "showRichiesteEffettuateInReteEdicola";
		}
	}

	//EDICOLA VENDITRICE EFFETTUA LA SUA SCELTA DI ACCETTAZIONE O MENO DELLA PUBBLICAZIONE RICHIESTA IN VENDITA
	@SkipValidation
	public String saveLivellamenti() {
		try {
			if (!Strings.isNullOrEmpty(listIdsLivellamenti)) {
				List<Long> listIdLivellamentiAccettati = new ArrayList<>();
				if (!Strings.isNullOrEmpty(spunte)) {
					listIdLivellamentiAccettati = buildListIdLivellamentiList(spunte);
					if(!listIdLivellamentiAccettati.isEmpty()){
						listLivellamenti = livellamentiService.getLivellamentiByIdLivellamenti(listIdLivellamentiAccettati);
						forEach(listLivellamenti).setStatoVendita(StatoRichiestaLivellamento.ACCETTATO);
						forEach(listLivellamenti).setDataConferma(livellamentiService.getSysdate());
					}
				}
				
				List<Long> listAllIdLivellamenti = buildListIdLivellamentiList(listIdsLivellamenti);
				listAllIdLivellamenti.removeAll(listIdLivellamentiAccettati);
				List<LivellamentiVo> listLivellamentiNonAccettati = null;
				if (!listAllIdLivellamenti.isEmpty()) {
					listLivellamentiNonAccettati = livellamentiService.getLivellamentiByIdLivellamenti(listAllIdLivellamenti);
					forEach(listLivellamentiNonAccettati).setStatoVendita(StatoRichiestaLivellamento.NON_ACCETTATO);
					Timestamp sysdate = livellamentiService.getSysdate();
					forEach(listLivellamentiNonAccettati).setDataConferma(sysdate);
				}
				
				List<RichiestaRifornimentoLivellamentiVo> richieste = null;
				if (listLivellamenti != null && !listLivellamenti.isEmpty()) {
					List<Long> idRich = extract(listLivellamenti, on(LivellamentiVo.class).getRichiesta().getIdRichiestaLivellamento());
					richieste = livellamentiService.getRichiesteRifornimentoLivellamentiByIds(idRich);
					forEach(richieste).setEdicolaAccettataVenduto(edicoleService.getAnagraficaEdicola(getAuthUser().getCodEdicolaMaster()));
				}
				
				livellamentiService.saveAccettazioneLivellamenti(listLivellamenti, listLivellamentiNonAccettati, richieste);
				
				if (listLivellamenti != null && !listLivellamenti.isEmpty()) {
					sendMessaggioAggiornamenti();
				}
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}
	
	@SkipValidation
	public String saveAccettazioneLivellamenti() {
		try {
			if (!Strings.isNullOrEmpty(listIdsLivellamenti)) {
				
				//VENGONO SETTATE LE PUBBLICAZIONI ACCETTATE DALL'EDICOLA RICHIEDENTE
				List<Long> listIdLivellamentiAccettati = new ArrayList<>();
				if (!Strings.isNullOrEmpty(spunte)) {
					listIdLivellamentiAccettati = buildListIdLivellamentiList(spunte);
					if(!listIdLivellamentiAccettati.isEmpty()){
						listLivellamenti = livellamentiService.getLivellamentiByIdLivellamenti(listIdLivellamentiAccettati);
						forEach(listLivellamenti).setStatoRichiesta(StatoRichiestaLivellamento.ACCETTATO);
						forEach(listLivellamenti).setDataAccettazione(livellamentiService.getSysdate());
						
						
						//livellamentiService.saveAccettazioneLivellamenti(listLivellamenti, null, null);

						//VENGONO SETTATE LE PUBBLICAZIONI NON ACCETTATE DALL'EDICOLA RICHIEDENTE
						List<LivellamentiVo> listLivellamentiNonAccettati = null;
						List<Long> listIdRichieste = buildListIdLivellamentiList(listIdsLivellamenti);
						listLivellamentiNonAccettati = livellamentiService.getAllLivellamentiNonAccettatiByIdRichiestaLivellamento(listIdRichieste,listIdLivellamentiAccettati);
						if (!listLivellamentiNonAccettati.isEmpty()) {
							if(!listLivellamentiNonAccettati.isEmpty()){
								forEach(listLivellamentiNonAccettati).setStatoRichiesta(StatoRichiestaLivellamento.NON_ACCETTATO);
								forEach(listLivellamentiNonAccettati).setDataAccettazione(livellamentiService.getSysdate());
							}
						}
						//Chiudi il dettaglio della richiesta in tbl_9131B set tutte le dataa9131b cosi da chiudere il dettaglio
						//livellamentiService.saveAccettazioneLivellamenti(null, listLivellamentiNonAccettati, null);
						 
						//Chiudi richiesta in tbl_9131A stato9131a
						List<RichiestaRifornimentoLivellamentiVo> listRichiesteDaChiudere = livellamentiService.getRichiesteRifornimentoLivellamentiByIds(listIdRichieste);
						if(!listRichiesteDaChiudere.isEmpty()){
							forEach(listRichiesteDaChiudere).setStato(StatoRichiestaRifornimentoLivellamento.DA_RITIRARE);
						}
						
						boolean val_quantitaRichieste = true;
						if(listRichiesteDaChiudere!=null && !listRichiesteDaChiudere.isEmpty()){
							RichiestaRifornimentoLivellamentiVo richiesta = listRichiesteDaChiudere.get(0);
							if(listLivellamenti.size()>richiesta.getQuantitaRichiesta().intValue())
								val_quantitaRichieste = false;
								
						}
						
						
						if(val_quantitaRichieste){
							livellamentiService.saveAccettazioneLivellamenti(listLivellamenti, null, null);
							livellamentiService.saveAccettazioneLivellamenti(null, listLivellamentiNonAccettati, listRichiesteDaChiudere);
						}else{
							//response.
							//throw new IGerivValidazioneReteEdicolaAccettazioneException(IGerivMessageBundle.get("igeriv.livellamenti.error.codice.livellamento.errato"));
							//throw new IGerivRuntimeException(IGerivMessageBundle.get("igeriv.livellamenti.error.numeri.accettati.maggiore.richiesta"));
							throw new Throwable(IGerivMessageBundle.get("igeriv.livellamenti.error.nuovo.accettazione.errata"));
							//throw new CodiceLivellamentoErratoException(IGerivMessageBundle.get("igeriv.livellamenti.error.quantita.errato"));
							
						}						

						if (val_quantitaRichieste && listLivellamenti != null && !listLivellamenti.isEmpty()) {
							sendMessaggioConfermaVendita(listIdRichieste);
						}
						
					}
				}
				
			}
		
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + e.getMessage() + IGerivConstants.END_EXCEPTION_TAG);
			//requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "blank";
	}

	
	
	private void sendMessaggioAggiornamenti() {
		Integer codEdicolaVenditrice = listLivellamenti.get(0).getEdicola().getCodEdicola();
		Integer codEdicolaRichiedente = listLivellamenti.get(0).getRichiesta().getEdicolaRichiedente().getCodEdicola();
		Integer codDLEdicolaRichiedente = listLivellamenti.get(0).getRichiesta().getEdicolaRichiedente().getCoddl();
		messaggiService.saveBaseVo(buildMessaggio(codDLEdicolaRichiedente, codEdicolaRichiedente ,getText("igeriv.message.vendita.rete.edicole.aggiornamento")));
	}
	
	/**
	 * Invio messaggio di conferma della vendita all'edicola venditrice
	 */
	private void sendMessaggioConfermaVendita() {
		Integer codEdicolaVenditrice = listLivellamenti.get(0).getEdicola().getCodEdicola();
		AbbinamentoEdicolaDlVo edVend = edicoleService.getAbbinamentoEdicolaDlVoByCodEdicolaWeb(codEdicolaVenditrice);
		AnagraficaEdicolaVo edRich = edicoleService.getAnagraficaEdicola(getAuthUser().getCodEdicolaMaster());
		Set<RichiestaRifornimentoLivellamentiVo> richieste = new HashSet<>(extract(listLivellamenti, on(LivellamentiVo.class).getRichiesta()));
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		for (RichiestaRifornimentoLivellamentiVo ric : richieste) {
			StoricoCopertineVo vo = ric.getStoricoCopertineVo();
			sb.append("<li>");
			sb.append(vo.getAnagraficaPubblicazioniVo().getTitolo());
			sb.append("&nbsp;&nbsp;&nbsp;");
			sb.append(getText("igeriv.del"));
			sb.append("&nbsp;");
			sb.append(DateUtilities.getTimestampAsString(vo.getDataUscita(), DateUtilities.FORMATO_DATA_SLASH));
			sb.append("&nbsp;&nbsp;&nbsp;");
			sb.append(getText("igeriv.numero"));
			sb.append("&nbsp;");
			sb.append(vo.getNumeroCopertina());
			sb.append("&nbsp;");
			sb.append(getText("igeriv.quantita"));
			sb.append("&nbsp;");
			sb.append(ric.getQuantitaRichiesta());
			sb.append("</li>");
		}
		sb.append("</ul>");
		messaggiService.saveBaseVo(buildMessaggio(edVend.getAnagraficaAgenziaVo().getCodFiegDl(), codEdicolaVenditrice, MessageFormat.format(getText("igeriv.message.conferma.vendita.rete.edicole"), edRich.getRagioneSocialeEdicolaPrimaRiga(), sb.toString())));
	}
	
	
	
	private void sendMessaggioConfermaVendita(List<Long> listIdRichiesteAccettate) {
		List<LivellamentiDto> listLivellamentiAccettati= livellamentiService.getLivellamentiDtoAccettatiDaRitirareByIdRichiesta(listIdRichiesteAccettate.get(0));
		
		for (LivellamentiDto ric : listLivellamentiAccettati) {
			StringBuilder sb = new StringBuilder();
			sb.append("<ul>");
			sb.append("<li>");
			sb.append(ric.getTitolo());
			sb.append("&nbsp;&nbsp;&nbsp;");
			sb.append(getText("igeriv.del"));
			sb.append("&nbsp;");
			sb.append(DateUtilities.getTimestampAsString(ric.getDataUscita(), DateUtilities.FORMATO_DATA_SLASH));
			sb.append("&nbsp;&nbsp;&nbsp;");
			sb.append(getText("igeriv.numero"));
			sb.append("&nbsp;");
			sb.append(ric.getNumeroCopertina());
			sb.append("&nbsp;");
			sb.append(getText("igeriv.quantita"));
			sb.append("&nbsp;");
			sb.append(ric.getQuantitaRichiesta());
			sb.append("</li>");
			sb.append("</ul>");
			messaggiService.saveBaseVo(buildMessaggio(ric.getCoddl(), ric.getCodEdicola(), MessageFormat.format(getText("igeriv.message.conferma.vendita.rete.edicole"), ric.getRagioneSocialeEdicolaPrimaRiga(), sb.toString())));
			
		}
		
		
	
	}
	
	
	
	
	/**
	 * Costruisce il messaggio istantaneo per l'edicola
	 * 
	 * @param codiceDl
	 * @param codWebEdicola
	 * @param messaggio
	 * @param attachments 
	 * @return
	 */
	private MessaggioVo buildMessaggio(Integer codiceDl, Integer codWebEdicola, String messaggio) {
		MessaggioVo vo = new MessaggioVo();
		MessaggioPk pk = new MessaggioPk();
		pk.setCodFiegDl(codiceDl);
		pk.setDestinatarioA(codWebEdicola);
		pk.setDestinatarioB(0);
		pk.setDtMessaggio(new Timestamp(new Date().getTime()));
		pk.setTipoDestinatario(IGerivConstants.COD_EDICOLA_SINGOLA);
		vo.setPk(pk);
		vo.setMessaggio(messaggio);
		vo.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_INVIATO);
		vo.setTipoMessaggio(IGerivConstants.TIPO_MESSAGGIO_IMMEDIATO);
		vo.setCategoria(IGerivConstants.CATEGORIA_MESSAGGIO_LIVELLAMENTI);
		return vo;
	}
	
	/**
	 * @param str
	 * @return
	 */
	private List<Long> buildListIdLivellamentiList(String str) {
		List<Long> listIds = new ArrayList<Long>();
		if (!Strings.isNullOrEmpty(str)) {
			for (String s : str.split(",")) {
				listIds.add(new Long(s.trim()));
			}
		}
		return listIds;
	}
	
	@Override
	public void saveParameters(Context context, String tableId, Map parameterMap) {
		
	}

	@Override
	public Map getParameters(Context context, String tableId, String stateAttr) {
		return context.getParameterMap();
	}

}
