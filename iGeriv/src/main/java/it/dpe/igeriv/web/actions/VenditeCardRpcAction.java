package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.DESCENDING;
import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.selectUnique;
import static ch.lambdaj.Lambda.sort;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.json.annotations.SMDMethod;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.card.CardService;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.dto.ConsumaCodiceB2CResponse;
import it.dpe.igeriv.dto.GiacenzaPneDto;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.PubblicazioneLocalVenditeDto;
import it.dpe.igeriv.dto.VenditaDettaglioDto;
import it.dpe.igeriv.dto.VenditeCardResultDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.dto.VendutoGiornalieroDto;
import it.dpe.igeriv.exception.ConfirmRiassociareSmartCardEdicolaException;
import it.dpe.igeriv.exception.EdicolaNonAutorizzataAggiornamentoBarcodeException;
import it.dpe.igeriv.exception.IGerivAssociateBarcodeBusinessException;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.IGerivNessunaEdicolaAutorizzataAggiornaBarcodeException;
import it.dpe.igeriv.exception.IGerivRaggiuntoLimiteRichiesteAggiornaBarcodeException;
import it.dpe.igeriv.exception.IGerivRichiestaGiaPresenteAggiornaBarcodeException;
import it.dpe.igeriv.exception.IGerivSendRequestAssociateBarcodeBusinessException;
import it.dpe.igeriv.exception.SmartCardEdicolaGiaAssociataException;
import it.dpe.igeriv.exception.TesseraNonAbilitataBusinessException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivConstants.SQLType;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.vendite.inputmodulelistener.VenditeInputModuleListener;
import it.dpe.igeriv.vendite.inputmodulelistener.impl.MinicardPLGInputModuleListener;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.ControlloLetturaMessaggioVo;
import it.dpe.igeriv.vo.MessaggioRegistratoreCassaVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.ModuloInputVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.RichiestaAggiornamentoBarcodeVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.VenditaVo;
import it.dpe.igeriv.vo.pk.ControlloLetturaMessaggioPk;
import it.dpe.igeriv.vo.pk.MessaggioPk;
import it.dpe.igeriv.vo.pk.MessaggioRegistratoreCassaPk;
import it.dpe.igeriv.vo.pk.ParametriEdicolaPk;
import it.dpe.igeriv.vo.pk.RichiestaAggiornamentoBarcodePk;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione delle vendite.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("VenditeCardRpcAction")
public class VenditeCardRpcAction<T extends BaseVo> extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(getClass());
	private final ClientiService<ClienteEdicolaVo> clientiService;
	private final EdicoleService edicoleService;
	private final VenditeService venditeService;
	private final ProdottiService prodottiService;
	private final PubblicazioniService pubblicazioniService;
	private final CardService cardService;
	private final IGerivUtils iGerivUtils;
	private final String giorniBolle;
	
	public VenditeCardRpcAction() {
		this(null,null,null,null,null,null,null,null);
	}
	
	@Autowired
	public VenditeCardRpcAction(
			ClientiService<ClienteEdicolaVo> clientiService, 
			EdicoleService edicoleService,
			VenditeService venditeService,
			ProdottiService prodottiService,
			PubblicazioniService pubblicazioniService,
			CardService cardService,
			IGerivUtils iGerivUtils,
			@Value("${igeriv.num.max.bolle.manutenzione.dl}") String giorniBolle) {
		this.clientiService = clientiService;
		this.edicoleService = edicoleService;
		this.venditeService = venditeService;
		this.prodottiService = prodottiService;
		this.pubblicazioniService = pubblicazioniService;
		this.cardService = cardService;
		this.iGerivUtils = iGerivUtils;
		this.giorniBolle = giorniBolle;
	}
	
	@SMDMethod
	public String validateDatiFiscaliCliente(Long codCliente) {
		if (codCliente != null) {
			ClienteEdicolaVo cliente = clientiService.getClienteEdicola(getAuthUser().getArrId(), codCliente);
			if ((Strings.isNullOrEmpty(cliente.getCodiceFiscale()) && Strings.isNullOrEmpty(cliente.getPiva())) 
					|| Strings.isNullOrEmpty(cliente.getIndirizzo()) || cliente.getTipoLocalita() == null || cliente.getLocalita() == null 
					|| cliente.getNumeroCivico() == null || cliente.getProvincia() == null ) {
				return MessageFormat.format(getText("igeriv.confirm.dati.fiscali.cliente"), new Object[]{cliente.getNomeCognome()});
			}
		}
		return "";
	}
	
	@SMDMethod
	public void setRicercaProdottiVari(String ricercaProdottiVari) {
		getAuthUser().setRicercaProdottiVari(Boolean.valueOf(ricercaProdottiVari));
	}
	
	@SuppressWarnings("unchecked")
	@SMDMethod
	public void deleteParamRegCassa() {
		ParametriEdicolaDto dto = new ParametriEdicolaDto();
		dto.setCodEdicola(getAuthUser().getCodEdicolaMaster());
		dto.setCodParametro(IGerivConstants.COD_PARAMETRO_REGISTRATORE_CASSA);
		dto.setDefaultValue("-1");
		dto.setSqlType(SQLType.INTEGER);
		dto.setValue("-1");
		Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
		mapParam.put(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_REGISTRATORE_CASSA, dto);
	}
	
	@SMDMethod 
	public Boolean getClienteConEstrattoConto(Long codCliente) {
		if (codCliente != null) {
			ClienteEdicolaVo clienteEdicola = clientiService.getClienteEdicola(getAuthUser().getArrId(), codCliente);
			if (clienteEdicola != null) {
				Integer tipoEstrattoConto = clienteEdicola.getTipoEstrattoConto();
				return (tipoEstrattoConto != null && tipoEstrattoConto > 0);
			}
		}
		return false;
	}
	
	@SMDMethod
	public Integer getLastNumFatturaEmessaDaUtente() {
		return clientiService.getLastFatturaUtenteEdicola(getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
	}
	
	@SMDMethod
	public void setMessaggioRegCassaVisto(Integer codRegCassa) {
		MessaggioRegistratoreCassaVo vo = new MessaggioRegistratoreCassaVo();
		MessaggioRegistratoreCassaPk pk = new MessaggioRegistratoreCassaPk();
		pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
		pk.setCodRegCassa(codRegCassa);
		vo.setPk(pk);
		vo.setMessaggioVisto(true);
		clientiService.saveBaseVo(vo);
	}
	
	@SMDMethod
	public Boolean setUserRegCassaLocalDir(String userRegCassaLocalDir) {
		try {
			ParametriEdicolaVo vo = edicoleService.getParametroEdicola(getAuthUser().getCodEdicolaMaster(), IGerivConstants.COD_PARAMETRO_PATH_LOCALE_REGISTRATORE_CASSA);
			if (vo == null) {
				vo = new ParametriEdicolaVo();
				ParametriEdicolaPk pk = new ParametriEdicolaPk();
				pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
				pk.setCodParametro(IGerivConstants.COD_PARAMETRO_PATH_LOCALE_REGISTRATORE_CASSA);
				vo.setPk(pk);
			}
			vo.setValue(userRegCassaLocalDir);
			edicoleService.saveBaseVo(vo);
			sessionMap.put("userRegCassaLocalDir", userRegCassaLocalDir);
		} catch (Throwable e) {
			return false;
		}
		return true;
	}
	
	@SMDMethod 
	public VenditeCardResultDto getVendutoGiornaliero() {
		VenditeCardResultDto dto = new VenditeCardResultDto();
		dto.setType(VenditeCardResultDto.ResultType.VENDUTO_GIORNALIERO);
		String msg = getText("label.daily_cashing_dialog.Message"); 
		VendutoGiornalieroDto vendutoDto = venditeService.getVendutoGionaliero(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster());
		String totaleGeneraleFormat = (vendutoDto.getTotaleGenerale() != null) ? it.dpe.igeriv.util.NumberUtils.formatNumber(vendutoDto.getTotaleGenerale()) : it.dpe.igeriv.util.NumberUtils.formatNumber(BigDecimal.ZERO);
		String totalePubblicazioniFormat = (vendutoDto.getTotalePubblicazioni() != null) ? it.dpe.igeriv.util.NumberUtils.formatNumber(vendutoDto.getTotalePubblicazioni()) : it.dpe.igeriv.util.NumberUtils.formatNumber(BigDecimal.ZERO);
		String totaleProdottiFormat = (vendutoDto.getTotaleProdotti() != null) ? it.dpe.igeriv.util.NumberUtils.formatNumber(vendutoDto.getTotaleProdotti()) : it.dpe.igeriv.util.NumberUtils.formatNumber(BigDecimal.ZERO);
		msg = MessageFormat.format(msg, IGerivConstants.EURO_SIGN_HTML + " " + totaleGeneraleFormat, IGerivConstants.EURO_SIGN_HTML + " " + totalePubblicazioniFormat, IGerivConstants.EURO_SIGN_HTML + " " + totaleProdottiFormat);
		dto.setResult(msg);
		return dto;
	}
	
	@Deprecated
	@SMDMethod 
	public String getGiacenza(Boolean isProdottoNonEditoriale, String titolo, Integer qtaInConto, Integer idtnOCodProdotto, Integer coddl) {
		String msg = getText("label.giacenza_dialog.Message"); 
		Long giac = 0l;
		if (isProdottoNonEditoriale != null && isProdottoNonEditoriale) {
			List<GiacenzaPneDto> giacenzaPne = prodottiService.getGiacenzaPne(getAuthUser().getCodEdicolaMaster(), null, null, new Long(idtnOCodProdotto));
			giac = (giacenzaPne != null && !giacenzaPne.isEmpty()) ? giacenzaPne.get(0).getGiacenza() : 0l;
		} else {
			giac = pubblicazioniService.getGiacenza(coddl, getAuthUser().isMultiDl() ? iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId()) : getAuthUser().getId(), idtnOCodProdotto, getAuthUser().getDataStorico());
		}
		if (giac != null && qtaInConto != null) {
			giac = giac - qtaInConto;
		}
		msg = MessageFormat.format(msg, new Object[]{titolo, giac});
		return msg;
	}
	
	@SMDMethod 
	public VenditeCardResultDto associaBarcodePubblicazione(Integer idtn, Integer coddl, String barcode) {
		VenditeCardResultDto vc = new VenditeCardResultDto();
		String msg = null;
		StoricoCopertineVo cop = null;
		try {
			Timestamp sysdate = venditeService.getSysdate();
			StoricoCopertineVo copertinaPrecedente = pubblicazioniService.getStoricoCopertinaByBarcode(coddl, barcode);
			if (copertinaPrecedente != null && copertinaPrecedente.getCodEdicolaCorrezioneBarcode() != null && !copertinaPrecedente.getCodEdicolaCorrezioneBarcode().equals(getAuthUser().getCodEdicolaMaster())) {
				throw new EdicolaNonAutorizzataAggiornamentoBarcodeException();
			}
			cop = pubblicazioniService.getStoricoCopertinaByPk(coddl, idtn);
			cop.setCodiceBarrePrecedente(cop.getCodiceBarre());
			cop.setCodiceBarre(barcode);
			cop.setCodEdicolaCorrezioneBarcode(getAuthUser().getCodEdicolaMaster());
			cop.setDataCorrezioneBarcode(sysdate);
			if (copertinaPrecedente != null) {
				copertinaPrecedente.setCodiceBarre(copertinaPrecedente.getCodiceBarrePrecedente());
			}
			RichiestaAggiornamentoBarcodeVo richiesta = venditeService.getRichiestaAggiornamentoBarcodeVo(coddl, idtn, barcode, false);
			if (richiesta != null) {
				richiesta.setRichiestaEseguita(true);
				richiesta.setDataEsecuzione(sysdate);
			}
			venditeService.saveAssociazioneBarcodePubblicazione(cop, copertinaPrecedente, richiesta, new Integer(giorniBolle));
			msg = cop != null ? MessageFormat.format(getText("igeriv.alert.barcode.associato"), new Object[]{cop.getAnagraficaPubblicazioniVo().getTitolo()}) : getText("igeriv.errore.esecuzione.procedura");
		} catch (EdicolaNonAutorizzataAggiornamentoBarcodeException e) {
			msg = getText("igeriv.alert.error.edicola.non.autorizzata.aggiornare.barcode");
			vc.setType(VenditeCardResultDto.ResultType.CONFIRM_INVIA_MESSAGGIO_ASSOCIA_BARCODE);
		} catch (Exception e) {
			msg = cop != null ? MessageFormat.format(getText("igeriv.alert.error.barcode.associato"), new Object[]{barcode, cop.getAnagraficaPubblicazioniVo().getTitolo()}) : getText("igeriv.errore.esecuzione.procedura");
			vc.setType(VenditeCardResultDto.ResultType.EXCEPTION);
		}
		vc.setResult(msg);
		return vc;
	}
	
	@SMDMethod 
	public VenditeCardResultDto sendRequestAggiornaBarcode(Integer idtn, Integer coddl, String barcode) {
		VenditeCardResultDto vc = new VenditeCardResultDto();
		String msg = null;
		try {
			PubblicazioneDto cop = pubblicazioniService.getCopertinaByIdtn(coddl, idtn);
			List<AbbinamentoEdicolaDlVo> edicoleAutorizzateAggiornaBarcode = edicoleService.getEdicoleAutorizzateAggiornamentoBarcode(coddl);
			Integer codEdicolaAutorizzataAggiornaBarcode = cop.getCodEdicolaCorrezioneBarcode() != null ? cop.getCodEdicolaCorrezioneBarcode() : (edicoleAutorizzateAggiornaBarcode != null && !edicoleAutorizzateAggiornaBarcode.isEmpty()) ? edicoleAutorizzateAggiornaBarcode.get(0).getCodDpeWebEdicola() : null;
			if (codEdicolaAutorizzataAggiornaBarcode == null) {
				throw new IGerivNessunaEdicolaAutorizzataAggiornaBarcodeException();
			}
			RichiestaAggiornamentoBarcodeVo richiesta = venditeService.getRichiestaAggiornamentoBarcodeVo(coddl, idtn, barcode, false);
			if (richiesta != null) {
				throw new IGerivRichiestaGiaPresenteAggiornaBarcodeException();
			}
			Date dataIni = DateUtilities.floorDay(new Date());
			Date dataFine = DateUtilities.ceilDay(new Date());
			int countRichieste = venditeService.getCountRichiesteAggiornamentoNelPeriodo(coddl, getAuthUser().getCodEdicolaMaster(), dataIni, dataFine).intValue();
			if (countRichieste >= IGerivConstants.NUM_MAX_RICHIESTE_AGGIORNAMENTO_BARCODE) {
				throw new IGerivRaggiuntoLimiteRichiesteAggiornaBarcodeException();
			}
			String msgRichiediAggiornamentoBarcode = MessageFormat.format(getText("igeriv.messagge.richiesta.aggiornamento.barcode"), barcode, cop.getTitolo(), cop.getSottoTitolo(), cop.getNumeroCopertina(), cop.getDataUscitaFormat());
			MessaggioVo vo = buildMessaggio(coddl, codEdicolaAutorizzataAggiornaBarcode, msgRichiediAggiornamentoBarcode);
			RichiestaAggiornamentoBarcodeVo ravo = new RichiestaAggiornamentoBarcodeVo();
			RichiestaAggiornamentoBarcodePk pk = new RichiestaAggiornamentoBarcodePk();
			pk.setCodFiegDl(coddl);
			pk.setIdtn(idtn);
			pk.setCodiceBarre(barcode);
			ravo.setPk(pk);
			ravo.setRichiestaEseguita(false);
			ravo.setCodEdicola(getAuthUser().getCodEdicolaMaster());
			ravo.setDataInvioRichiesta(new Timestamp(new Date().getTime()));
			venditeService.saveBaseVo(vo);
			venditeService.saveBaseVo(ravo);
			msg = getText("igeriv.richiesta.aggiornamento.barcode.inviata");
		} catch (IGerivRaggiuntoLimiteRichiesteAggiornaBarcodeException e) {
			msg = MessageFormat.format(getText("igeriv.errore.raggiunto.limite.richieste.aggiornamento.barcode"), IGerivConstants.NUM_MAX_RICHIESTE_AGGIORNAMENTO_BARCODE);
			vc.setType(VenditeCardResultDto.ResultType.EXCEPTION);
		} catch (IGerivNessunaEdicolaAutorizzataAggiornaBarcodeException e) {
			msg = getText("igeriv.errore.nessuna.edicola.abilitata.aggiornamento.barcode");
			vc.setType(VenditeCardResultDto.ResultType.EXCEPTION);
		} catch (IGerivRichiestaGiaPresenteAggiornaBarcodeException e) {
			msg = getText("igeriv.errore.richiesta.aggiornamento.barcode.gia.presente");
			vc.setType(VenditeCardResultDto.ResultType.EXCEPTION);
		} catch (Exception e) {
			msg = getText("igeriv.errore.esecuzione.procedura");
			vc.setType(VenditeCardResultDto.ResultType.EXCEPTION);
		}
		vc.setResult(msg);
		return vc;
	}
	
	@SMDMethod
	public String saveMessageEdicola(Integer coddl, String strDataMessaggio) throws ParseException {
		ControlloLetturaMessaggioVo vo = new ControlloLetturaMessaggioVo();
		ControlloLetturaMessaggioPk pk = new ControlloLetturaMessaggioPk();
		pk.setCodFiegDl(coddl);
		pk.setCodiceEdicola(getAuthUser().getCodEdicolaMaster());
		Timestamp dataMessaggio = DateUtilities.parseDate(strDataMessaggio.replaceAll("\\.", ":"), DateUtilities.FORMATO_DATA_SLASH_SHORT_HHMMSSMS);
		pk.setDtMessaggio(dataMessaggio);
		vo.setPk(pk);
		vo.setMessaggioLetto(IGerivConstants.COD_MESSAGGIO_LETTO);
		venditeService.saveBaseVo(vo);
		return null;
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
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	@SMDMethod 
	public Map<String, Object> getGiacenzaPubblicazione(Integer idtn, Integer coddl) {
		Map<String, Object> result = new HashMap<String, Object>();
		int giac = pubblicazioniService.getGiacenza(coddl, getAuthUser().isMultiDl() ? iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId()) : getAuthUser().getId(), idtn, getAuthUser().getDataStorico()).intValue();
		result.put("giac", giac);
		PubblicazioneDto copertina = pubblicazioniService.getCopertinaByIdtn(coddl, idtn);
		Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
		ParametriEdicolaDto paramHasRichiestaRifornimentoNelleVendite = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_RICHIESTA_RIFORNIMENTO_NELLE_VENIDTE) ? mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_RICHIESTA_RIFORNIMENTO_NELLE_VENIDTE) : null;
		if (paramHasRichiestaRifornimentoNelleVendite != null && Boolean.parseBoolean(paramHasRichiestaRifornimentoNelleVendite.getParamValue()) && copertina.getDataUscita() != null && copertina.getNumGiorniDaDataUscitaPerRichiestaRifornimento() > 0) {
    		Date dataLimiteRichiesteRifo = DateUtilities.ceilDay(DateUtilities.aggiungiGiorni(copertina.getDataUscita(), copertina.getNumGiorniDaDataUscitaPerRichiestaRifornimento()));
    		if (pubblicazioniService.getSysdate().before(dataLimiteRichiesteRifo)) {
    			if (getAuthUser().getVenditeEsauritoControlloGiacenzaDL() && copertina.getGiancezaPressoDl() <= 0) {
    				result.put("richiedereRifornimenti", "false");
        			result.put("puoRichiedereRifornimenti", "false");
    			}
    			else {
    				result.put("richiedereRifornimenti", (giac <= 1 ? "true" : "false"));
    				result.put("puoRichiedereRifornimenti", "true");
    			}
    		}
    	}
		return result;
	}
	
	@SMDMethod 
	public VenditeCardResultDto chiudiConto(List<VenditeParamDto> params, String idConto, String codCliente, String importoTotale, String totaleScontrino, String contoScontrinato, String pagato, String idScontrino, String dataScontrino, Integer numeroFattura) {
  		VenditeCardResultDto result = new VenditeCardResultDto();
		
		//result.setType(VenditeCardResultDto.ResultType.CONTO_CHIUSO);
		
		try {
			VenditaVo venditaEffettuata = venditeService.chiudiConto(params, idConto, codCliente, importoTotale, totaleScontrino, contoScontrinato, pagato, idScontrino, dataScontrino, numeroFattura, getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodUtente(), getAuthUser().getCodEdicolaDl(), getAuthUser().getArrId(), getAuthUser().isMultiDl(), getAuthUser().getArrCodFiegDl(), getAuthUser().getId());
			switch (venditaEffettuata.getEnumEsitoVendita()) {
			case "CONTO_CHIUSO":
				result.setType(VenditeCardResultDto.ResultType.CONTO_CHIUSO);
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("msgAttivazioneWS", venditaEffettuata.getWsEpipoliMessaggioPopup());
				result.setMsgParams(map1);
				break;
			case "VENDITA_PRODOTTI_DIGITALI":
				result.setType(VenditeCardResultDto.ResultType.VENDITA_PRODOTTI_DIGITALI);
				Map<String, String> map2 = new HashMap<String, String>();
				
				map2.put("msgAttivazioneWS", 	venditaEffettuata.getWsEpipoliMessaggioPopup());
				
				String idRichiestaWS = "";
				List<ConsumaCodiceB2CResponse> listCodiciResp = venditaEffettuata.getResponseWS();
				for(ConsumaCodiceB2CResponse iter :listCodiciResp){
					idRichiestaWS += iter.getConsumaCodiceResponse().get(0).getIdRichiesta()+"|";
					map2.put("esitoAttivazioneWS", 	iter.getConsumaCodiceResponse().get(0).getEsito());
				}
				map2.put("idRichiestaWS", idRichiestaWS);
				
				result.setMsgParams(map2);
				result.setVenditaProdottiDigitali(venditaEffettuata);
				break;	
			default:
				break;
			}
			
			
		} catch (Exception e) {
			log.error("Errore nella chiusura conto delle Vendite", e);
			result.setType(VenditeCardResultDto.ResultType.EXCEPTION);
			result.setExceptionMessage(getText("gp.errore.chiusura.conto"));
		}
		return result;
	}
	
	/**
	 * Metodo chiamato dalla pagina dell'evasione degli ordini dei clienti cliccando sul bottone "Aggiungi Altre Vendite"
	 * Richiama il conto e setta la giacenza.
	 * 
	 * @param idConto
	 * @return
	 */
	@SMDMethod 
	public List<VenditaDettaglioDto> getConto(Long idConto) {
		List<VenditaDettaglioDto> contiVendite = venditeService.getContiVendite(Arrays.asList(new Long[]{idConto}), (getAuthUser().isMultiDl() ? null : getAuthUser().getCodUtente()));
		return contiVendite;
	}
	
	/**
	 * Metodo chiamato nella pagina vendite cliccando sul bottone "Visualizza Conti": mostra gli ultimi n conti.
	 * 
	 * @return
	 */
	@SMDMethod 
	public VenditeCardResultDto getConti() {
		VenditeCardResultDto vc = new VenditeCardResultDto();
		vc.setType(VenditeCardResultDto.ResultType.CONTO_VENDITE);
		vc.setResult(venditeService.getStoricoConti(getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().isMultiDl(), getAuthUser().getCodUtente()));
		return vc;
	}
	
	@SMDMethod 
	public VenditeCardResultDto deleteConto(Long codConto) {
		VenditeCardResultDto vc = new VenditeCardResultDto();
		
		//GIFT CARD 
		//Viene controllata la presenza di prodotti digitali all'interno del conto che si tenda di riaprire.
		//La cancellazione di questo tipo di conto non può essere effettuata
		try {
			
			List<Long> listCodVendita = new ArrayList<Long>();
			listCodVendita.add(codConto);
			List<VenditaDettaglioDto> listVenditaDettaglio = venditeService.getContiVendite(listCodVendita, (getAuthUser().isMultiDl() ? null : getAuthUser().getCodUtente()));
			if(listVenditaDettaglio!=null && listVenditaDettaglio.size()>0){
				for(VenditaDettaglioDto it : listVenditaDettaglio){
					if(it.getFlagProDigitale()!=null && 
							!it.getFlagProDigitale().equals("") && 
									it.getFlagProDigitale().equals("S"))
						throw new Exception();
				}
			}
			
		} catch (Exception e) {
			vc.setType(VenditeCardResultDto.ResultType.EXCEPTION);
			vc.setExceptionMessage(getText("msg.errore.cancellazione.conto.prodotti.digitali"));
			return vc;
		}
		
				
		try {
			List<Long> listCodVendita = new ArrayList<Long>();
			listCodVendita.add(codConto);
			List<VenditaDettaglioDto> listVenditaDettaglio = venditeService.getContiVendite(listCodVendita, (getAuthUser().isMultiDl() ? null : getAuthUser().getCodUtente()));
			Map<Integer, Integer> mapIdtnCoddl = new HashMap<Integer, Integer>();
			for (VenditaDettaglioDto dto : listVenditaDettaglio) {
				mapIdtnCoddl.put(dto.getIdtn(), dto.getCoddl());
			}
			List<Long> listIdProd = extract(select(listVenditaDettaglio, having(on(VenditaDettaglioDto.class).getProdottoNonEditoriale(), equalTo(true))), on(VenditaDettaglioDto.class).getIdProdotto());
			VenditaVo contoVendite = venditeService.getContoVendite(codConto);
			venditeService.deleteVenditaVo(contoVendite);
			Map<String, Long> mapGiacenze = new HashMap<String, Long>();
			for (Map.Entry<Integer, Integer> entry : mapIdtnCoddl.entrySet()) {
				Integer idtn = entry.getKey();
				Integer coddl = entry.getValue();
				Long giac = pubblicazioniService.getGiacenza(coddl, getAuthUser().isMultiDl() ? (coddl == 0) ? getAuthUser().getCodFiegDlMaster() : iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(coddl, getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId()) : getAuthUser().getCodEdicolaMaster(), idtn, getAuthUser().getDataStorico());
				mapGiacenze.put("pub_" + idtn, giac);
			}
			for (Long codProd : listIdProd) {
				List<GiacenzaPneDto> giacenzaPne = prodottiService.getGiacenzaPne(getAuthUser().getCodEdicolaMaster(), null, null, codProd);
				Long giac = (giacenzaPne != null && !giacenzaPne.isEmpty()) ? giacenzaPne.get(0).getGiacenza() : 0l;
				mapGiacenze.put("pne_" + codProd, giac);
			}
			vc.setResult(mapGiacenze);
		} catch (Throwable e) {
			vc.setType(VenditeCardResultDto.ResultType.EXCEPTION);
			vc.setExceptionMessage(getText("msg.errore.invio.richiesta.html"));
		}
		return vc;
	}
	
	@SuppressWarnings("unchecked")
	@SMDMethod 
	public VenditeCardResultDto associaIGerivCard(String barcode, String codCliente, String byPassClienteCheck) {
		VenditeCardResultDto dto = new VenditeCardResultDto();
		try {
			ModuloInputVo moduloInputIGerivCard = selectUnique((List<ModuloInputVo>) context.getAttribute("moduliInput"), having(on(ModuloInputVo.class).getClasse(), equalTo("IGerivCardInputModuleListener")));
			if (!moduloInputIGerivCard.getPattern().matcher(barcode).matches()) {
				dto.setType(VenditeCardResultDto.ResultType.EXCEPTION);
				dto.setExceptionMessage(MessageFormat.format(getText("label.pubblication_input_module_listener.IGeriv_Client_Card_Not_Matching"), new Object[]{barcode}));
				return dto;	
			}
			ClienteEdicolaVo cliente = clientiService.getClienteEdicola(getAuthUser().getArrId(), new Long(codCliente));
			try {
				cardService.associaIGerivCard(barcode, new Long(codCliente), getAuthUser().getCodEdicolaMaster(), Boolean.parseBoolean(byPassClienteCheck));
				dto.setResult(MessageFormat.format(getText("label.pubblication_input_module_listener.IGeriv_Card_Associated"), new Object[]{barcode}));
				dto.setContoNome(MessageFormat.format(IGerivMessageBundle.get("igeriv.conto.del.cliente.tessera.o.nome"), (cliente != null && !NumberUtils.isNumber(cliente.getNome())) ? cliente.getNome() + " " + cliente.getCognome() : IGerivMessageBundle.get("igeriv.tessera.numero") + " " + barcode));
				dto.setCodCliente(new Long(codCliente));
			} catch (ConfirmRiassociareSmartCardEdicolaException e) {
				dto.setType(VenditeCardResultDto.ResultType.CONFIRM_RIASSOCIARE_IGERIV_CARD);
				dto.setExceptionMessage(MessageFormat.format(getText("label.pubblication_input_module_listener.IGeriv_Client_Card_Already_Associated2"), new Object[]{cliente.getNome()}));
			} catch (SmartCardEdicolaGiaAssociataException e) {
				dto.setType(VenditeCardResultDto.ResultType.EXCEPTION);
				dto.setExceptionMessage(MessageFormat.format(getText("label.pubblication_input_module_listener.IGeriv_Card_Already_Associated"), new Object[]{barcode}));
			} catch (Exception e) {
				dto.setType(VenditeCardResultDto.ResultType.EXCEPTION);
				dto.setExceptionMessage(getText("msg.errore.invio.richiesta.html"));
			}
		} catch (Exception e) {
			dto.setType(VenditeCardResultDto.ResultType.EXCEPTION);
			dto.setExceptionMessage(getText("msg.errore.invio.richiesta.html"));
		}
		return dto;
	}
	
	
	@SMDMethod 
	public VenditeCardResultDto reportVenditeRicariche(VenditeParamDto params) {
		 VenditeCardResultDto dto = null;  
		 try {
			 VenditeInputModuleListener venditeInputModuleListener = (VenditeInputModuleListener) SpringContextManager.getService("MinicardPLGInputModuleListener");
	         params.setIdEdicola(getAuthUser().getCodEdicolaMaster());
	         params.setCodFiegDl(getAuthUser().getCodFiegDlMaster());
             params.setCodEdicolaDl(getAuthUser().getCodEdicolaDl());
	         params.setArrCodFiegDl(getAuthUser().getArrCodFiegDl());
	         params.setArrIdEdicola(getAuthUser().getArrId());
	         params.setDataStorico(getAuthUser().getDataStorico());
	         params.setMultiDl(getAuthUser().isMultiDl());
	         params.setDlInforiv(getAuthUser().isDlInforiv());
	         dto = venditeInputModuleListener.execute(params);
		 } catch (IGerivBusinessException e) {
			dto = new VenditeCardResultDto();
			dto.setType(VenditeCardResultDto.ResultType.EXCEPTION);
			dto.setExceptionMessage(e.getMessage());
		}
        return dto;
	}
	
	@SuppressWarnings("unchecked")
	@SMDMethod 
	public VenditeCardResultDto getRows(VenditeParamDto params) {
		VenditeCardResultDto dto = null;
		if (params.getIdConto() != null && !params.getIdConto().equals("")) {
			VenditaVo contoVendite = venditeService.getContoVendite(new Long(params.getIdConto()));
			dto = new VenditeCardResultDto();
			dto.setType(VenditeCardResultDto.ResultType.VENDUTO_CONTO);
			dto.setIdConto("" + contoVendite.getCodVendita());
			dto.setResult(contoVendite.getListVenditaDettaglio());
		} else {
			try {  
		        for (ModuloInputVo modulo : (List<ModuloInputVo>) context.getAttribute("moduliInput")) {
		            String inputText = params.getInputText();
					if (modulo.getPattern().matcher(inputText).matches()) {
		                VenditeInputModuleListener venditeInputModuleListener = (VenditeInputModuleListener) SpringContextManager.getService(modulo.getClasse());
		                if (getAuthUser().getHasLivellamenti() && !Strings.isNullOrEmpty(params.getCodiceVenditaReteEdicole())) {
		                	venditeInputModuleListener = (VenditeInputModuleListener) SpringContextManager.getService("ReteEdicoleInputModuleListener");
		                }
	                	int codFiegDl = getAuthUser().getCodFiegDlMaster();
		                boolean checkCard = getAuthUser().getCheckConsegneGazzetta() != null
		                		&& getAuthUser().getCheckConsegneGazzetta()
		                		&& codFiegDl == IGerivConstants.MENTA_CODE
		                		&& venditeInputModuleListener instanceof MinicardPLGInputModuleListener;

		                if (checkCard) {
		                	int codEdicolaDl = getAuthUser().getCodEdicolaDl();
		                	int codEdicolaWeb = getAuthUser().getCodDpeWebEdicola();
		    				if (params.getOperation().equals("CONSEGNA")) {
		                		// se la rivendita ha un venduto card pari al fornito non permetto più le consegne delle card
		    					Timestamp now = DateUtilities.floorDay(new Timestamp(new Date().getTime()));
		    					try {
			    					int copieCardCons = venditeService.getCopieConsegnateGazzettaCard(codFiegDl, codEdicolaDl, now);
			    					int copieDistr = venditeService.getDistribuitoGazzetta(codFiegDl, codEdicolaWeb, now);
			    					if (copieCardCons >= copieDistr) {
			    						throw new IGerivBusinessException
			    							("Le copie che hai consegnato agli abbonati ha raggiunto il tuo distribuito");
			    					}
		    					} catch (RuntimeException e1) {
		    						throw new IGerivBusinessException(IGerivMessageBundle.get("igeriv.rtae.non.disponibile"));
		    					}
		                	}
		                }
		                params.setIdEdicola(getAuthUser().getCodEdicolaMaster());
		                params.setCodFiegDl(getAuthUser().getCodFiegDlMaster());
		                params.setCodEdicolaDl(getAuthUser().getCodEdicolaDl());
		                params.setCodUtente(getAuthUser().getCodUtente());
		                params.setArrCodFiegDl(getAuthUser().getArrCodFiegDl());
		                params.setArrIdEdicola(getAuthUser().getArrId());
		                params.setDataStorico(getAuthUser().getDataStorico());
		                params.setMultiDl(getAuthUser().isMultiDl());
		                params.setDlInforiv(getAuthUser().isDlInforiv());
		                params.setAbilitataCorrezioneBarcode(getAuthUser().getAbilitataCorrezioneBarcode());
		                params.setHasEdicoleAutorizzateAggiornaBarcode(getAuthUser().getHasEdicoleAutorizzateAggiornaBarcode());
		                params.setVenditeEsauritoControlloGiacenzaDL(getAuthUser().getVenditeEsauritoControlloGiacenzaDL());
		                Map<String, ParametriEdicolaDto> mapParam = (Map<String, ParametriEdicolaDto>) sessionMap.get(IGerivConstants.SESSION_VAR_MAP_PARAMETRI_EDICOLA);
		                params.setToken(mapParam.containsKey(IGerivConstants.SESSION_VAR_TOKEN) ? mapParam.get(IGerivConstants.SESSION_VAR_TOKEN).toString() : null);
		                ParametriEdicolaDto paramHasRichiestaRifornimentoNelleVendite = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_RICHIESTA_RIFORNIMENTO_NELLE_VENIDTE) ? mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_RICHIESTA_RIFORNIMENTO_NELLE_VENIDTE) : null;
		                params.setHasRichiestaRifornimentoNelleVendite(paramHasRichiestaRifornimentoNelleVendite != null ? new Boolean(paramHasRichiestaRifornimentoNelleVendite.getParamValue()) : false);
		               	//Ticket 0000264	                
		                ParametriEdicolaDto paramModalitaRicerca = mapParam.containsKey(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_RICERCA_MODALITA_CONTENUTO) ? mapParam.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_RICERCA_MODALITA_CONTENUTO) : null;
		                params.setHarModalitaRicercaContenuto(paramModalitaRicerca != null ? new Boolean(paramModalitaRicerca.getParamValue()) : false);
		              
		                if (params.getFindPrezzoEdicola()) {
		                	params.setGruppoSconto(getAuthUser().getGruppoSconto());
		                }
		                dto = venditeInputModuleListener.execute(params);
		                break;
		            }
		        }
			} catch (IGerivAssociateBarcodeBusinessException e) {
				dto = new VenditeCardResultDto();
				dto.setType(VenditeCardResultDto.ResultType.CONFIRM_ASSOCIA_BARCODE);
				dto.setExceptionMessage(e.getMessage());
			} catch (IGerivSendRequestAssociateBarcodeBusinessException e) {
				dto = new VenditeCardResultDto();
				dto.setType(VenditeCardResultDto.ResultType.CONFIRM_INVIA_MESSAGGIO_ASSOCIA_BARCODE);
				dto.setExceptionMessage(e.getMessage());
			} catch (TesseraNonAbilitataBusinessException e) {
				dto = new VenditeCardResultDto();
				dto.setType(VenditeCardResultDto.ResultType.CONFIRM_ABILITARE_IGERIV_CARD);
				dto.setExceptionMessage(e.getMessage());
			} catch (IGerivBusinessException e) {
				dto = new VenditeCardResultDto();
				dto.setType(VenditeCardResultDto.ResultType.EXCEPTION);
				dto.setExceptionMessage(e.getMessage());
			} finally {
				if (dto != null && !Strings.isNullOrEmpty(dto.getToken())) {
					sessionMap.put(IGerivConstants.SESSION_VAR_TOKEN, dto.getToken());
				}
			}
			if (dto != null && dto.getResult() != null && dto.getResult() instanceof PubblicazioneDto) {
				int progressivo = Integer.parseInt(params.getProgressivo());
				((PubblicazioneDto) dto.getResult()).setProgressivo(++progressivo);
			}
		}
        return dto;
	}
	
	@SMDMethod 
	public VenditeCardResultDto getMostRecentPubblicazioneByCpuCoddl(String cpu, String coddl, Integer quantita) {
		VenditeCardResultDto result = new VenditeCardResultDto();
		Integer cpuInt = new Integer(cpu);
		PubblicazioneDto copertina = pubblicazioniService.getLastPubblicazioneDto(new Integer(coddl), cpuInt);
		if (getAuthUser().getEdicolaDeviettiTodis()) {
			 List<PubblicazioneDto> list = pubblicazioniService.getCopertine(true, false, false, getAuthUser().getCodEdicolaMaster(), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null, null, null, null, null, cpuInt, null, false, getAuthUser().getDataStorico(), getAuthUser().getGruppoSconto(), false, getAuthUser().getCodFiegDl(),null,null,null,null);
			 if (list != null && !list.isEmpty()) {
				 copertina = (PubblicazioneDto) sort(list, on(PubblicazioneDto.class).getDataUscita(), DESCENDING).get(0);
			 }
		}
		if (copertina != null) {
			if (copertina.getPeriodicitaInt().equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO) && copertina.getPeriodicitaInt() != null && copertina.getCodInizioQuotidiano() != null && copertina.getCodFineQuotidiano() != null) {
				copertina = getQuotidiano(coddl, copertina);
			}
			if (copertina != null) {
				copertina.setQuantita(quantita);
				copertina.setGiacenzaIniziale(pubblicazioniService.getGiacenza(copertina.getCoddl(), getAuthUser().isMultiDl() ? iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(copertina.getCoddl(), getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId()) : getAuthUser().getArrId()[0], copertina.getIdtn(), getAuthUser().getDataStorico()).intValue());
			}
		}
        result.setResult(copertina);
		result.setType(VenditeCardResultDto.ResultType.VENDITE_IDTN);
		return result;
	}

	/**
	 * @param coddl
	 * @param copertina
	 * @return
	 */
	private PubblicazioneDto getQuotidiano(String coddl, PubblicazioneDto copertina) {
		Timestamp sysdate = DateUtilities.floorDay(pubblicazioniService.getSysdate());
		if (!copertina.getDataUscita().equals(sysdate)) {
			return pubblicazioniService.getQuotidianoByDataUscita(new Integer(coddl), copertina.getCodInizioQuotidiano(), copertina.getCodFineQuotidiano(), sysdate);
		}
		return copertina;
	}
	
	@SMDMethod
	public List<PubblicazioneLocalVenditeDto> getLocalDataVendite() {
		return venditeService.getLocalDataVendite(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId());
	}
	
	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.visualizza.edicole");
	}
	
}
