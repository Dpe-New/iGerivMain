package it.dpe.igeriv.web.listener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

//import org.apache.batik.script.Window.GetURLHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.google.common.base.Joiner;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.help.HelpService;
import it.dpe.igeriv.bo.localita.LocalitaService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.bo.statistiche.StatisticheService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.enums.StatoRichiestaPropostaFilterLivellamento;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.RestConstants;
import it.dpe.igeriv.util.Types;
import it.dpe.igeriv.vo.AnagraficaBancaVo;

/**
 * Classe che ascolta l'oggetto javax.servlet.ServletContext,
 * esegue operazioni di inizializzazione e setta variabili 
 * nel contesto "application".
 * 
 * @author romanom
 * 
 */
@Component("IGerivServletContextAware")
public class IGerivServletContextAware implements ServletContextAware {
	private final LocalitaService localitaService;
	private final VenditeService venditeService;
	private final HelpService helpService;
	private final MenuService menuService;
	private final RifornimentiService rifornimentiService;
	private final ClientiService<AnagraficaBancaVo> clientiService;
	private final StatisticheService statisticheService;
	private final PubblicazioniService pubblicazioniService;
	private final ProdottiService prodottiService;
	private final AgenzieService agenzieService;
	private final String millsTaskInterval;
	private final String networkDetectionIntervalMills;
	private final String autoSaveBolleIntervalMills;
	private final String highPriorityMessagesCheckInterval;
	private final String periodoGiorni; 
	private final String giorniScadenzaRichiestaRifornimentoLivellamenti;
	
	@Autowired
	public IGerivServletContextAware(
			LocalitaService localitaService,
			VenditeService venditeService,
			HelpService helpService,
			MenuService menuService,
			RifornimentiService rifornimentiService,
			ClientiService<AnagraficaBancaVo> clientiService,
			StatisticheService statisticheService,
			PubblicazioniService pubblicazioniService, 
			ProdottiService prodottiService, 
			AgenzieService agenzieService,
			@Value("${igeriv.vendite.reg.cassa.mills.polling.task.interval}") String millsTaskInterval,
			@Value("${igeriv.network.detection.interval.mills}") String networkDetectionIntervalMills, 
			@Value("${igeriv.autosave.bolle.interval.mills}") String autoSaveBolleIntervalMills, 
			@Value("${igeriv.high.priority.messages.check.interval.mills}") String highPriorityMessagesCheckInterval,
			@Value("${igeriv.periodo.giorni}") String periodoGiorni,
			@Value("${igeriv.livellamenti.richieste.rifornimento.giorni.scadenza.default}") String giorniScadenzaRichiestaRifornimentoLivellamenti) {
		this.localitaService = localitaService;
		this.venditeService = venditeService;
		this.helpService = helpService;
		this.menuService = menuService;
		this.rifornimentiService = rifornimentiService;
		this.clientiService = clientiService;
		this.statisticheService = statisticheService;
		this.pubblicazioniService = pubblicazioniService;
		this.prodottiService = prodottiService;
		this.agenzieService = agenzieService;
		this.millsTaskInterval = millsTaskInterval;
		this.networkDetectionIntervalMills = networkDetectionIntervalMills;
		this.autoSaveBolleIntervalMills = autoSaveBolleIntervalMills;
		this.highPriorityMessagesCheckInterval = highPriorityMessagesCheckInterval;
		this.periodoGiorni = periodoGiorni;
		this.giorniScadenzaRichiestaRifornimentoLivellamenti = giorniScadenzaRichiestaRifornimentoLivellamenti;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setServletContext(ServletContext arg0) {
		IGerivMessageBundle.initialize();
		setTypes();
		arg0.setAttribute("poweredBy", MessageFormat.format(IGerivMessageBundle.get("dpe.powered.by"), "" + Calendar.getInstance().get(Calendar.YEAR)));
		arg0.setAttribute("tipiLocalita", localitaService.getTipiLocalita());
		arg0.setAttribute("paesi", localitaService.getPaesi());
		arg0.setAttribute("province", localitaService.getProvince());
		arg0.setAttribute("tipiEdicola", getTipiEdicola());
		arg0.setAttribute("moduliInput", menuService.getModuliInput());
		arg0.setAttribute("risposteClientiCodificate", rifornimentiService.getRisposteClientiCodificate());
		arg0.setAttribute("registratoriCassa", venditeService.getRegistratoriCassa());
		arg0.setAttribute("help", helpService.getAllHelp());
		arg0.setAttribute("listBanche", clientiService.getBanche());
		arg0.setAttribute(IGerivConstants.APPLICATION_VAR_CDL_CODE, IGerivConstants.CDL_CODE);
		arg0.setAttribute(IGerivConstants.APPLICATION_VAR_COMUZZI_CODE, IGerivConstants.COMUZZI_CODE);
		Map nameToValueMap = IGerivConstants.getNameToValueMap();
		nameToValueMap.putAll(RestConstants.getNameToValueMap());
		arg0.setAttribute(IGerivConstants.ID, Collections.unmodifiableMap(nameToValueMap));
		arg0.setAttribute(IGerivConstants.PAGE_MONITOR_MAP, statisticheService.getPageMonitorMap());
		arg0.setAttribute(IGerivConstants.SESSION_VAR_NETWORK_DETECTION_INTERVAL_MILLS, new Integer(networkDetectionIntervalMills));
		arg0.setAttribute(IGerivConstants.SESSION_VAR_SAVE_BOLLE_INTERVAL_MILLS, new Integer(autoSaveBolleIntervalMills));
		arg0.setAttribute(IGerivConstants.SESSION_VAR_HIGH_PRIORITY_MESSAGES_CHECK_INTERVAL_MILLS, new Integer(highPriorityMessagesCheckInterval));
		arg0.setAttribute("millsTaskInterval", new Long(millsTaskInterval));
		List<String> listPeriodoGiorni = new ArrayList<String>(Arrays.asList(periodoGiorni.split(",")));
		arg0.setAttribute("listArgomento", pubblicazioniService.getMapArgomentiDl());
		arg0.setAttribute("listPeriodoGiorni", listPeriodoGiorni);
		arg0.setAttribute("listNumberVo1to100", buildNumeroVoltePrenotazioni1to100());
		arg0.setAttribute("listNumberVo0to100", buildNumeroVoltePrenotazioni0to100());
		arg0.setAttribute("listCSVNumber0to100", getNumbers1to100());
		arg0.setAttribute("statiProdottoNonEditoriali", buildStatiProdottoNonEditoriali());
		arg0.setAttribute("statiMancanzePubblicazioni", buildStatiMancanzePubblicazioni());
		arg0.setAttribute("statiContestazioniResa", buildStatiContestazioniResa());
		arg0.setAttribute("statiTipoMessaggi", buildStatiTipoMessaggi());
		arg0.setAttribute("statiPrenotazioniPubblicazioni", buildStatiPrenotazioniPubblicazioniEdicola());
		arg0.setAttribute("statiPrenotazioniPubblicazioniClient", buildStatiPrenotazioniPubblicazioniClienteEdicola());
		arg0.setAttribute("statiPrenotazioniPubblicazioniVariazioni", buildStatiPrenotazioniPubblicazioniVariazioni());
		arg0.setAttribute("statiRifornimentiReteEdicola", buildStatiRichiesteRifornimentiReteEdicola());
		arg0.setAttribute("tipiProdotto", buildTipiProdotto());
		arg0.setAttribute("listTipiEstrattoConto", getListTipiEstrattoConto());
		arg0.setAttribute("listTipiPagamento", getListTipiPagamento());
		arg0.setAttribute("listAgenzieDpe", agenzieService.getListAgenzieDpe());
		arg0.setAttribute("listCausali", prodottiService.getCausali());
		arg0.setAttribute("listAliquoteIva", prodottiService.getListAliquoteIva());
		arg0.setAttribute("listMetodiPagamentoCliente", clientiService.getMetodiPagamentoCliente());
		arg0.setAttribute("listPeriodicita", pubblicazioniService.getPeriodicita());
		arg0.setAttribute("listStatiRichiestaReteEdicola", getStatiRichiestaReteEdicola());
		arg0.setAttribute("listStatiPropostaReteEdicola", getStatiPropostaReteEdicola());
		arg0.setAttribute("giorniScadenzaRichiestaRifornimentoLivellamenti", giorniScadenzaRichiestaRifornimentoLivellamenti);
	}
	
	public List<KeyValueDto> getStatiRichiestaReteEdicola() {
		List<KeyValueDto> list = new ArrayList<>();
		list.add(new KeyValueDto(null, StatoRichiestaPropostaFilterLivellamento.INSERITI.getValue(), IGerivMessageBundle.get("igeriv.inseriti")));
		list.add(new KeyValueDto(null, StatoRichiestaPropostaFilterLivellamento.DA_RITIRARE.getValue(), IGerivMessageBundle.get("igeriv.da.ritirare")));
		list.add(new KeyValueDto(null, StatoRichiestaPropostaFilterLivellamento.RITIRATI.getValue(), IGerivMessageBundle.get("igeriv.ritirati")));
		return list;
	}
	
	public List<KeyValueDto> getStatiPropostaReteEdicola() {
		List<KeyValueDto> list = new ArrayList<>();
		list.add(new KeyValueDto(null, StatoRichiestaPropostaFilterLivellamento.DA_CONSEGNARE.getValue(), IGerivMessageBundle.get("igeriv.da.consegnare")));
		list.add(new KeyValueDto(null, StatoRichiestaPropostaFilterLivellamento.CONSEGNATI.getValue(), IGerivMessageBundle.get("igeriv.consegnati")));
		return list;
	}
	
	private void setTypes() {
		Types.ContoDepositoType.TUTTO.setValue(IGerivMessageBundle.get("igeriv.spunta.auto"));
		Types.ContoDepositoType.ESCLUDI_CONTO_DEPOSITO.setValue(IGerivMessageBundle.get("igeriv.escludi.conto.deposito"));
		Types.ContoDepositoType.SOLO_CONTO_DEPOSITO.setValue(IGerivMessageBundle.get("igeriv.solo.conto.deposito"));
		Types.TipoPubblicazioneType.TUTTO.setValue(IGerivMessageBundle.get("igeriv.spunta.auto"));
		Types.TipoPubblicazioneType.ESCLUDI_PUBBLICAZIONI_SCADUTE.setValue(IGerivMessageBundle.get("igeriv.escludi.scaduti"));
		Types.TipoPubblicazioneType.SOLO_PUBBLICAZIONI_SCADUTE.setValue(IGerivMessageBundle.get("igeriv.solo.scaduti"));
	}
	
	private List<KeyValueDto> getTipiEdicola() {
		List<KeyValueDto> list = new ArrayList<KeyValueDto>();
		KeyValueDto tp1 = new KeyValueDto();
		tp1.setKeyInt(IGerivConstants.COD_TIPO_EDICOLA_CHIOSCO_GIORNALI);
		tp1.setValue(IGerivMessageBundle.get("igeriv.chiosco.giornali"));
		list.add(tp1);
		KeyValueDto tp2 = new KeyValueDto();
		tp2.setKeyInt(IGerivConstants.COD_TIPO_EDICOLA_CHIOSCO_PROMISCUO);
		tp2.setValue(IGerivMessageBundle.get("igeriv.chiosco.promiscuo"));
		list.add(tp2);
		KeyValueDto tp3 = new KeyValueDto();
		tp3.setKeyInt(IGerivConstants.COD_TIPO_EDICOLA_NEGOZIO);
		tp3.setValue(IGerivMessageBundle.get("igeriv.negozio"));
		list.add(tp3);
		return list;
	}
	
	/**
	 * Ritorna una lista di dto con i numeri da 1 a 100.
	 * 
	 * @return List<KeyValueDto>
	 */
	private List<KeyValueDto> buildNumeroVoltePrenotazioni1to100() {
		List<KeyValueDto> listNumbers = new ArrayList<KeyValueDto>();
		KeyValueDto dto = new KeyValueDto();
		dto.setKeyInt(-1);
		dto.setValue(IGerivMessageBundle.get("igeriv.sempre"));
		listNumbers.add(dto);
		for (int i = 1; i <= 100; i++) {
			KeyValueDto dto1 = new KeyValueDto();
			dto1.setKeyInt(i);
			dto1.setValue("" + i);
			listNumbers.add(dto1);
		}
		return listNumbers;
	}
	
	/**
	 * Ritorna una lista di dto con i numeri da 0 a 100.
	 * 
	 * @return List<KeyValueDto>
	 */
	private List<KeyValueDto> buildNumeroVoltePrenotazioni0to100() {
		List<KeyValueDto> listNumbers = new ArrayList<KeyValueDto>();
		for (int i = 0; i <= 100; i++) {
			KeyValueDto dto1 = new KeyValueDto();
			dto1.setKeyInt(i);
			dto1.setValue("" + i);
			listNumbers.add(dto1);
		}
		return listNumbers;
	}
	
	
	/**
	 * Ritorna una lista di dto con i tipi di estratto conto per i clienti dell'edicola.
	 * 
	 * @return List<KeyValueDto>
	 */
	private List<KeyValueDto> getListTipiEstrattoConto() {
		List<KeyValueDto> listNumbers = new ArrayList<KeyValueDto>();
		KeyValueDto dto = new KeyValueDto();
		dto.setKeyInt(null);
		dto.setValue(IGerivMessageBundle.get("igeriv.nessuno"));
		listNumbers.add(dto);
		
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKeyInt(IGerivConstants.TIPO_ESTRATTO_CONTO_CLIENTE_EDICOLA_MENSILE);
		dto1.setValue(IGerivMessageBundle.get("igeriv.mensile"));
		listNumbers.add(dto1);
		
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKeyInt(IGerivConstants.TIPO_ESTRATTO_CONTO_CLIENTE_EDICOLA_SETTIMANALE);
		dto2.setValue(IGerivMessageBundle.get("igeriv.settimanale"));
		listNumbers.add(dto2);
		return listNumbers;
	}
	
	private Object getListTipiPagamento() {
		List<KeyValueDto> listTipiPagamento = new ArrayList<KeyValueDto>();
		KeyValueDto dto = new KeyValueDto();
		dto.setKeyInt(-1);
		dto.setValue(IGerivMessageBundle.get("igeriv.nessuno"));
		listTipiPagamento.add(dto);
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKeyInt(IGerivConstants.COD_TIPO_PAGAMENTO_IN_EDICOLA);
		dto1.setValue(IGerivMessageBundle.get(IGerivConstants.TIPO_PAGAMENTO_IN_EDICOLA));
		listTipiPagamento.add(dto1);
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKeyInt(IGerivConstants.COD_TIPO_PAGAMENTO_ANTICIPATO);
		dto2.setValue(IGerivMessageBundle.get(IGerivConstants.TIPO_PAGAMENTO_ANTICIPATO));
		listTipiPagamento.add(dto2);
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKeyInt(IGerivConstants.COD_TIPO_PAGAMENTO_CONTRO_ASSEGNO);
		dto3.setValue(IGerivMessageBundle.get(IGerivConstants.TIPO_PAGAMENTO_CONTRO_ASSEGNO));
		listTipiPagamento.add(dto3);
		return listTipiPagamento;
	}

	/**
	 * @return
	 */
	public List<KeyValueDto> buildStatiProdottoNonEditoriali() {
		List<KeyValueDto> stati = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INSERITO);
		dto1.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_INSERITO));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_PRONTO_PER_INVIO);
		dto2.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_PRONTO_PER_INVIO));
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKey(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INVIATO);
		dto3.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_INVIATO_DL));
		KeyValueDto dto4 = new KeyValueDto();
		dto4.setKey(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_EVASA);
		dto4.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_EVASO));
		KeyValueDto dto5 = new KeyValueDto();
		dto5.setKey(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_EVASA_PARZIALMENTE);
		dto5.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_EVASO_PARZIALMENTE));
		stati.add(dto1);
		stati.add(dto2);
		stati.add(dto3);
		stati.add(dto4);
		stati.add(dto5);
		return stati;
	}
	
	/**
	 * @return
	 */
	private List<KeyValueDto> buildStatiMancanzePubblicazioni() {
		List<KeyValueDto> stati = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("" + IGerivConstants.COD_STATO_MANCANZE_INSERITO);
		dto1.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_INSERITO_TEXT));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("" + IGerivConstants.COD_STATO_MANCANZE_SOSPESO);
		dto2.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_SOSPESO_TEXT));
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKey("" + IGerivConstants.COD_STATO_MANCANZE_ACCREDITATA);
		dto3.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_ACCREDITATA_TEXT));
		KeyValueDto dto4 = new KeyValueDto();
		dto4.setKey("" + IGerivConstants.COD_STATO_MANCANZE_NON_ACCREDITATA);
		dto4.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_NON_ACCREDITATA_TEXT));
		KeyValueDto dto5 = new KeyValueDto();
		dto5.setKey("" + IGerivConstants.COD_STATO_MANCANZE_PAREGGIATE);
		dto5.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_PAREGGIATE_TEXT));
		stati.add(dto1);
		stati.add(dto2);
		stati.add(dto3);
		stati.add(dto4);
		stati.add(dto5);
		return stati;
	}
	
	/**
	 * @return
	 */
	private List<KeyValueDto> buildStatiContestazioniResa() {
		List<KeyValueDto> stati = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("" + IGerivConstants.COD_STATO_CONTESTAZIONI_RESA_DA_LAVORARE);
		dto1.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_CONTESTAZIONI_RESA_DA_LAVORARE_TEXT));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("" + IGerivConstants.COD_STATO_CONTESTAZIONI_RESA_APPROVATA);
		dto2.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_CONTESTAZIONI_RESA_APPROVATA_TEXT));
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKey("" + IGerivConstants.COD_STATO_CONTESTAZIONI_RESA_RIFIUTATA);
		dto3.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_CONTESTAZIONI_RESA_RIFIUTATA_TEXT));
		KeyValueDto dto4 = new KeyValueDto();
		dto4.setKey("" + IGerivConstants.COD_STATO_CONTESTAZIONI_RESA_ANNULLATA);
		dto4.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_CONTESTAZIONI_RESA_ANNULLATA_TEXT));
		stati.add(dto1);
		stati.add(dto2);
		stati.add(dto3);
		stati.add(dto4);
		return stati;
	}
	
	/**
	 * @return
	 */
	public List<KeyValueDto> buildStatiTipoMessaggi() {
		List<KeyValueDto> stati = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("0");
		dto1.setValue(IGerivMessageBundle.get("igeriv.normale"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("1");
		dto2.setValue(IGerivMessageBundle.get("igeriv.allerta"));
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKey("2");
		dto3.setValue(IGerivMessageBundle.get("igeriv.emergenza"));
		stati.add(dto1);
		stati.add(dto2);
		stati.add(dto3);
		return stati;
	}
	
	/**
	 * @return
	 */
	private List<KeyValueDto> buildStatiPrenotazioniPubblicazioniClienteEdicola() {
		List<KeyValueDto> stati = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("" + IGerivConstants.PRENOTAZIONI_INSERITE);
		dto1.setValue(IGerivMessageBundle.get("igeriv.prenotazioni.inserite"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("" + IGerivConstants.PRENOTAZIONI_EVASE);
		dto2.setValue(IGerivMessageBundle.get("igeriv.prenotazioni.evase"));
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKey("" + IGerivConstants.PRENOTAZIONI_FISSE);
		dto3.setValue(IGerivMessageBundle.get("igeriv.prenotazioni.fisse"));
		KeyValueDto dto4 = new KeyValueDto();
		dto4.setKey("" + IGerivConstants.PRENOTAZIONI_PARZIALMENTE_EVASE);
		dto4.setValue(IGerivMessageBundle.get("igeriv.prenotazioni.evase.parzialmente"));
		stati.add(dto1);
		stati.add(dto2);
		stati.add(dto4);
		stati.add(dto3);
		return stati;
	}
	
	private List<KeyValueDto> buildStatiRichiesteRifornimentiReteEdicola() {
		List<KeyValueDto> stati = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("" + IGerivConstants.COD_STATO_INSERITO_RIFORNIMENTI_RETE_EDICOLE);
		dto1.setValue(IGerivMessageBundle.get("igeriv.rifornimenti.rete.edicola.inserite"));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("" + IGerivConstants.COD_STATO_CHIUSO_RIFORNIMENTI_RETE_EDICOLE);
		dto2.setValue(IGerivMessageBundle.get("igeriv.rifornimenti.rete.edicola.chiuse"));
		stati.add(dto1);
		stati.add(dto2);
		return stati;
	}
	
	/**
	 * @return
	 */
	private List<KeyValueDto> buildStatiPrenotazioniPubblicazioniEdicola() {
		List<KeyValueDto> stati = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("null");
		dto1.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_INSERITO));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey(IGerivConstants.STATO_PRONTO_PER_INVIO_SIGLA);
		dto2.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_PRONTO_PER_INVIO));
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKey(IGerivConstants.STATO_INVIATO_DL_SIGLA);
		dto3.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_INVIATO_DL));
		stati.add(dto1);
		stati.add(dto2);
		stati.add(dto3);
		return stati;
	}
	
	/**
	 * @return
	 */
	private List<KeyValueDto> buildStatiPrenotazioniPubblicazioniVariazioni() {
		List<KeyValueDto> stati = new ArrayList<KeyValueDto>();
		KeyValueDto dto1 = new KeyValueDto();
		dto1.setKey("" + IGerivConstants.COD_STATO_EVASIONE_INSERITO);
		dto1.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_INSERITO));
		KeyValueDto dto2 = new KeyValueDto();
		dto2.setKey("" + IGerivConstants.COD_STATO_EVASIONE_PARZIALE);
		dto2.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_PRONTO_PER_INVIO));
		KeyValueDto dto3 = new KeyValueDto();
		dto3.setKey("" + IGerivConstants.COD_STATO_EVASIONE_COMPLETA);
		dto3.setValue(IGerivMessageBundle.get(IGerivConstants.STATO_INVIATO_DL));
		stati.add(dto1);
		stati.add(dto2);
		stati.add(dto3);
		return stati;
	}
	
	/**
	 * Ritorna i numeri da 0 a 100 separati da virgola.
	 * 
	 * @return String
	 */
	private String getNumbers1to100() {
		List<Integer> listNumbers = new ArrayList<Integer>();
		for (int i = 0; i <= 100; i++) {
			listNumbers.add(i);
		}
		return Joiner.on(",").join(listNumbers);
	}
	
	/**
	 * Costruisce la lista di tipo prodotto
	 */
	private List<KeyValueDto> buildTipiProdotto() {
		List<KeyValueDto> tipiProdotto = new ArrayList<KeyValueDto>();
		KeyValueDto tutti = new KeyValueDto();
		tutti.setKeyInt(3);
		tutti.setValue(IGerivMessageBundle.get("igeriv.tutti"));
		tipiProdotto.add(tutti);
		KeyValueDto soloProdEdit = new KeyValueDto();
		soloProdEdit.setKeyInt(0);
		soloProdEdit.setValue(IGerivMessageBundle.get("igeriv.solo.prodotti.editoriali"));
		tipiProdotto.add(soloProdEdit);
		KeyValueDto soloProdNonEdit = new KeyValueDto();
		soloProdNonEdit.setKeyInt(1);
		soloProdNonEdit.setValue(IGerivMessageBundle.get("igeriv.solo.prodotti.non.editoriali"));
		tipiProdotto.add(soloProdNonEdit);
		KeyValueDto soloVendValore = new KeyValueDto();
		soloVendValore.setKeyInt(2);
		soloVendValore.setValue(IGerivMessageBundle.get("igeriv.solo.vendite.a.valore"));
		tipiProdotto.add(soloVendValore);
		return tipiProdotto;
	}
}
