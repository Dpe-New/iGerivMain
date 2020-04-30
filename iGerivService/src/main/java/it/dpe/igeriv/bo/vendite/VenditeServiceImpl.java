package it.dpe.igeriv.bo.vendite;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;
import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.ws.epipoli.EpipoliWebServices;
import it.dpe.igeriv.dto.ConsumaCodiceB2CResponse;
import it.dpe.igeriv.dto.IdVenditaProdottNonEditorialeDto;
import it.dpe.igeriv.dto.IntervalloVenditaDto;
import it.dpe.igeriv.dto.PositionSizeDto;
import it.dpe.igeriv.dto.ProdottoClientVenditeDto;
import it.dpe.igeriv.dto.PubblicazioneLocalVenditeDto;
import it.dpe.igeriv.dto.RichiestaClienteDto;
import it.dpe.igeriv.dto.VenditaDettaglioDto;
import it.dpe.igeriv.dto.VenditaDto;
import it.dpe.igeriv.dto.VenditaRiepilogoDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.dto.VendutoGiornalieroDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.vo.BarraSceltaRapidaDestraVo;
import it.dpe.igeriv.vo.BarraSceltaRapidaProdottiVariVo;
import it.dpe.igeriv.vo.BarraSceltaRapidaSinistraVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.MessaggioRegistratoreCassaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.RegistratoreCassaVo;
import it.dpe.igeriv.vo.RichiestaAggiornamentoBarcodeVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.VenditaDettaglioVo;
import it.dpe.igeriv.vo.VenditaVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiBollaDettaglioPk;
import it.dpe.igeriv.vo.pk.VenditaDettaglioPk;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

@Service("VenditeService")
class VenditeServiceImpl extends BaseServiceImpl implements VenditeService {
	private final VenditeRepository repository;
	private final ProdottiService prodottiService;
	private final PubblicazioniService pubblicazioniService;
	private final ClientiService<ClienteEdicolaVo> clientiService;
	private final EdicoleService edicoleService;
	private final IGerivUtils iGerivUtils;
	private final EpipoliWebServices epipoliWebServices;
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	VenditeServiceImpl(VenditeRepository repository, ProdottiService prodottiService, PubblicazioniService pubblicazioniService, ClientiService<ClienteEdicolaVo> clientiService, EdicoleService edicoleService, IGerivUtils iGerivUtils,EpipoliWebServices epipoliWebServices) {
		super(repository);
		this.repository = repository;
		this.prodottiService = prodottiService;
		this.pubblicazioniService = pubblicazioniService;
		this.clientiService = clientiService;
		this.edicoleService = edicoleService;
		this.iGerivUtils = iGerivUtils;
		this.epipoliWebServices = epipoliWebServices;
	}

	@Override
	public List<VenditaDettaglioDto> getContiVendite(List<Long> idConti, String codUtente) {
		return repository.getContiVendite(idConti, codUtente);
	}

	@Override
	public List<IdVenditaProdottNonEditorialeDto> getLastContiVendita(Integer codFiegDlMaster, Integer codEdicolaMaster, String codUtente, Date dateInizio, Date dateFine, boolean limitResults) {
		return repository.getLastContiVendita(codFiegDlMaster, codEdicolaMaster, codUtente, dateInizio, dateFine, limitResults);
	}

	@Override
	public List<VenditaRiepilogoDto> getVenditeRiepilogo(Integer codFiegDl, Integer codEdicola, String codUtente, Timestamp dataDa, Timestamp dataA, Integer codRaggruppamento) {
		return repository.getVenditeRiepilogo(codFiegDl, codEdicola, codUtente, dataDa, dataA, codRaggruppamento);
	}
	
	@Override
	public void deleteBarraMenuSceltaRapida(Integer[] codFiegDl, Integer[] codEdicola) {
		repository.deleteBarraMenuSceltaRapida(codFiegDl, codEdicola);
	}

	@Override
	public VendutoGiornalieroDto getVendutoGionaliero(Integer codFiegDl, Integer codEdicola) {
		return repository.getVendutoGionaliero(codFiegDl, codEdicola);
	}

	@Override
	public VenditaVo getContoVendite(Long codContoVendite) {
		return repository.getContoVendite(codContoVendite);
	}

	@Override
	public void updateBarraSceltaRapidaVo(Integer codFiegDl, Integer codEdicola, PositionSizeDto psDto) {
		repository.updateBarraSceltaRapidaVo(codFiegDl, codEdicola, psDto);
	}
	
	@Override
	public List<VenditaDettaglioDto> getContiVenditeDettaglio(Integer codFiegDl, Integer codEdicola, String codUtente, Timestamp dataDa, Timestamp dataA, Integer tipoProdotto) {
		return repository.getContiVenditeDettaglio(codFiegDl, codEdicola, codUtente, dataDa, dataA, tipoProdotto);
	}
	
	@Override
	public BarraSceltaRapidaSinistraVo getBarraSceltaRapidaSinistra(Integer codEdicola, Integer codFiegDl, Integer cpu) {
		return repository.getBarraSceltaRapidaSinistra(codEdicola, codFiegDl, cpu); 
	}
	
	@Override
	public BarraSceltaRapidaDestraVo getBarraSceltaRapidaDestra(Integer codEdicola, Integer codFiegDl, Integer cpu) {
		return repository.getBarraSceltaRapidaDestra(codEdicola, codFiegDl, cpu);
	}
	
	@Override
	public void saveBarraSceltaRapida(Integer[] arrCoddl, Integer[] arrCodEdicola, String[] codPubblicazioniL, Integer[] arrCoddlL, Integer[] arrCodEdicolaL, String[] nomiImmagineL, String[] tipiImmagineL, String[] codPubblicazioniR, Integer[] arrCoddlR, Integer[] arrCodEdicolaR, String[] nomiImmagineR, String[] tipiImmagineR) {
		repository.deleteBarraMenuSceltaRapida(arrCoddl, arrCodEdicola); 
		saveBarraSceltaRapidaSinistra(codPubblicazioniL, arrCoddlL, arrCodEdicolaL, nomiImmagineL, tipiImmagineL);
		saveBarraSceltaRapidaDestra(codPubblicazioniR, arrCoddlR, arrCodEdicolaR, nomiImmagineR, tipiImmagineR);
	}
	
	@Override
	public void saveBarraSceltaRapidaSinistra(String[] codPubblicazioni, Integer[] arrCoddl, Integer[] arrCodEdicola, String[] nomiImmagine, String[] tipiImmagine) {
		repository.saveBarraSceltaRapidaSinistra(codPubblicazioni, arrCoddl, arrCodEdicola, nomiImmagine, tipiImmagine);
	}
	
	@Override
	public void saveBarraSceltaRapidaDestra(String[] codPubblicazioni, Integer[] arrCoddl, Integer[] arrCodEdicola, String[] nomiImmagine, String[] tipiImmagine) {
		repository.saveBarraSceltaRapidaDestra(codPubblicazioni, arrCoddl, arrCodEdicola, nomiImmagine, tipiImmagine);
	}
	
	@Override
	public void deleteListVenditaDettaglio(Long codVendita) {
		repository.deleteListVenditaDettaglio(codVendita);
	}
	
	@Override
	public RichiestaAggiornamentoBarcodeVo getRichiestaAggiornamentoBarcodeVo(Integer coddl, Integer idtn, String barcode, Boolean richiestaEseguita) {
		return repository.getRichiestaAggiornamentoBarcodeVo(coddl, idtn, barcode, richiestaEseguita);
	}
	
	@Override
	public List<RichiestaAggiornamentoBarcodeVo> getRichiesteEseguiteAggiornamentoBarcodeVo(Integer coddl, Date dataDa) {
		return repository.getRichiesteEseguiteAggiornamentoBarcodeVo(coddl, dataDa);
	}
	
	@Override
	public Long getCountRichiesteAggiornamentoNelPeriodo(Integer coddl, Integer codEdicola, Date dataIni, Date dataFine) {
		return repository.getCountRichiesteAggiornamentoNelPeriodo(coddl, codEdicola, dataIni, dataFine);
	}
	
	@Override
	public List<PubblicazioneLocalVenditeDto> getLocalDataVendite(Integer[] arrCodFiegDl, Integer[] arrId) {
		return repository.getLocalDataVendite(arrCodFiegDl, arrId);
	}
	
	@Override
	public RegistratoreCassaVo getRegistratoreCassa(Integer codRegCassa) {
		return repository.getRegistratoreCassa(codRegCassa);
	}
	
	@Override
	public List<RegistratoreCassaVo> getRegistratoriCassa() {
		return repository.getRegistratoriCassa();
	}
	
	@Override
	public void resetCodClienteVendite(Long codCliente) {
		repository.resetCodClienteVendite(codCliente);
	}
	
	@Override
	public MessaggioRegistratoreCassaVo getMessaggioRegistratoreCassa(Integer codEdicola, Integer codRegCassa) {
		return repository.getMessaggioRegistratoreCassa(codEdicola, codRegCassa);
	}
	
	@Override
	public void deleteVenditaDettaglio(String pk) {
		repository.deleteVenditaDettaglio(pk);
	}
	
	@Override
	public void restoreVenditaDettaglio(String pk) {
		repository.restoreVenditaDettaglio(pk);
	}
	
	@Override
	public void updateVenditePerStornoFattura(Long codCliente, Integer numFattura) {
		repository.updateVenditePerStornoFattura(codCliente, numFattura);
	}
	
	@Override
	public Boolean getHasRitiriCancellati(Integer[] arrId, Long codCliente) {
		return repository.getHasRitiriCancellati(arrId, codCliente);
	}
	
	@Override
	public void saveContoVendite(VenditaVo vendita, ProdottiNonEditorialiBollaVo bpne) {
		if (bpne != null) {
			saveBaseVo(bpne);
		}
		if (vendita != null) {
			saveBaseVo(vendita);
		}
	}
	
	@Override
	public void saveAssociazioneBarcodePubblicazione(StoricoCopertineVo cop, StoricoCopertineVo copertinaPrecedente, RichiestaAggiornamentoBarcodeVo richiesta, int giorniDataBolla) {
		saveBaseVo(cop);
		if (copertinaPrecedente != null) {
			mergeBaseVo(copertinaPrecedente);
		}
		if (richiesta != null) {
			saveBaseVo(richiesta);
		}
	}
	
	@Override
	public void deleteVenditaVo(VenditaVo contoVendite) {
		if (contoVendite != null) {
			if (contoVendite.getContoProdottiVari() != null) {
				prodottiService.deleteProdottiNonEditorialiBollaDettaglio(contoVendite.getContoProdottiVari().getIdDocumento());
				deleteVo(contoVendite.getContoProdottiVari());
			}
			deleteVo(contoVendite);
		}
	}
	
	@Override
	public List<IntervalloVenditaDto> getVenditeIntervalli(Integer codFiegDl, Integer codEdicola, Timestamp dataDa, Timestamp dataA) {
		return repository.getVenditeIntervalli(codFiegDl, codEdicola, dataDa, dataA);
	}
	
	/**
	 * Crea un conto vendite con i dati di evasione del/dei cliente/i dell'edicola in modo da aggiornare la giacenza e le vendite
	 * 
	 * @param String codUtente
	 * @param Map<Integer, List<RichiestaClienteDto>> mapEvasione
	 * @param Timestamp dataCompEC 
	 */
	@Override
	public Long saveContoVendite(String codUtente, Map<Long, List<RichiestaClienteDto>> mapEvasione, Timestamp dataCompEC) {
		Long contoVendite = null;
		for (Map.Entry<Long, List<RichiestaClienteDto>> entry : mapEvasione.entrySet()) {
			Long codCliente = entry.getKey();
			List<RichiestaClienteDto> values = entry.getValue();
			int progressivo = 0;
			VenditaVo vendita = new VenditaVo();
			List<VenditaDettaglioVo> listVenditaDettaglioVo = new ArrayList<VenditaDettaglioVo>();
			BigDecimal importoTotale = new BigDecimal(0);
			for (RichiestaClienteDto dto : values) {
				StoricoCopertineVo copertina = pubblicazioniService.getStoricoCopertinaByPk(dto.getCodDl(), dto.getIdtn());
				VenditaDettaglioVo vo = new VenditaDettaglioVo();
				VenditaDettaglioPk vpk = new VenditaDettaglioPk();
				vpk.setVenditaVo(vendita);
				vpk.setProgressivo(progressivo++);
				vo.setPk(vpk);
				vo.setIdtn(dto.getIdtn());
				vo.setCodFiegDl(dto.getCodDl());
				vo.setCodEdicola(dto.getCodEdicola());
				vo.setPrezzoCopertina(copertina.getPrezzoCopertina());
				vo.setSottoTitolo(copertina.getSottoTitolo());
				vo.setTitolo(copertina.getAnagraficaPubblicazioniVo().getTitolo());
				vo.setNumeroCopertina(copertina.getNumeroCopertina());
				vo.setQuantita(dto.getQuantitaEvasa());
				vo.setDeleted(false);
				BigDecimal multiply = copertina.getPrezzoCopertina().multiply(new BigDecimal(dto.getQuantitaEvasa()));
				vo.setImportoTotale(multiply);
				importoTotale = importoTotale.add(multiply);
				listVenditaDettaglioVo.add(vo);
			}
			vendita.setCodEdicola(listVenditaDettaglioVo.get(0).getCodEdicola());
			vendita.setCodFiegDl(listVenditaDettaglioVo.get(0).getCodFiegDl());
			vendita.setCodUtente(codUtente);
			vendita.setPagato(false);
			vendita.setCodCliente(new Long(codCliente));
			contoVendite = getNextSeqVal(IGerivConstants.SEQ_VENDITE);
			vendita.setCodVendita(contoVendite);
			vendita.setDataVendita(getSysdate());
			vendita.setListVenditaDettaglio(listVenditaDettaglioVo);
			vendita.setTrasferitaGestionale(IGerivConstants.INDICATORE_RECORD_NON_TRASFERITO);
			vendita.setImportoTotale(importoTotale);
			vendita.setDataCompetenzaEstrattoContoClienti(dataCompEC);
			vendita.setProvenienzaConto(IGerivConstants.PROVENIENZA_CONTO_EVASIONE_CLIENTI);
			vendita.setDeleted(false);
			vendita.setFatturaEmessa(false);
			saveContoVendite(vendita, null);
		}
		return mapEvasione.size() == 1 ? contoVendite : null;
	}
	
	@Override
	public VenditaVo chiudiConto(List<VenditeParamDto> params, String idConto, String codCliente, String importoTotale, String totaleScontrino, String contoScontrinato, String pagato, String idScontrino, String dataScontrino, Integer numeroFattura, Integer codEdicolaMaster, Integer codFiegDlMaster, String codUtente, Integer codEdicolaDl, Integer[] arrId, boolean isMultiDl, Integer[] arrCodFiegDl, Integer id) {
		Boolean bContoScontrinato = Boolean.valueOf(Strings.isNullOrEmpty(contoScontrinato) ? "false" : contoScontrinato);
		Long codConto = (idConto != null && !idConto.equals("")) ? new Long(idConto) : null;
		if (codConto == null) {
			codConto = getNextSeqVal(IGerivConstants.SEQ_VENDITE);
		}
		VenditaVo vendita = new VenditaVo();
		vendita.setCodEdicola(codEdicolaMaster);
		vendita.setCodFiegDl(codFiegDlMaster);
		vendita.setCodUtente(codUtente);
		vendita.setCodEdicolaDl(codEdicolaDl);
		vendita.setCodVendita(codConto);
		vendita.setDataVendita(new Timestamp(new Date().getTime()));
		Long codiceCliente = Strings.isNullOrEmpty(codCliente) ? null : new Long(codCliente);
		vendita.setCodCliente(codiceCliente);
		vendita.setImportoTotale(!Strings.isNullOrEmpty(importoTotale) ? new BigDecimal(importoTotale) : null);
		vendita.setContoScontrinato(bContoScontrinato);
		boolean pag = true;
		if (!Strings.isNullOrEmpty(codCliente) && NumberUtils.isNumber(codCliente)) {
			ClienteEdicolaVo cliente = clientiService.getClienteEdicola(arrId, new Long(codCliente));
			pag = cliente.getTipoEstrattoConto() != null && cliente.getTipoEstrattoConto() > 0 ? false : true; 
		}
		vendita.setPagato(pag);
		vendita.setIdScontrino(Strings.isNullOrEmpty(idScontrino) ? null : idScontrino);
		vendita.setDataScontrino(Strings.isNullOrEmpty(dataScontrino) ? null : new Timestamp(new Long(dataScontrino)));
		vendita.setFatturaEmessa(Boolean.valueOf(numeroFattura != null && numeroFattura > 0));
		vendita.setFatturaContoUnico(Boolean.valueOf(numeroFattura != null && numeroFattura > 0));
		vendita.setIdFattura((numeroFattura != null && numeroFattura > 0) ? numeroFattura : null);
		vendita.setProvenienzaConto(IGerivConstants.PROVENIENZA_CONTO_VENDITE_DETTAGLIO);
		vendita.setDataCompetenzaEstrattoContoClienti(DateUtilities.floorDay(clientiService.getSysdate()));
		vendita.setDeleted(false);
		
		List<ProdottiNonEditorialiBollaDettaglioVo> listProdottiNonEditorialiBollaDettaglioVo = new ArrayList<ProdottiNonEditorialiBollaDettaglioVo>();
		List<ProdottiNonEditorialiBollaDettaglioVo> listProdottiNonEditoriali_digitali_BollaDettaglioVo = new ArrayList<ProdottiNonEditorialiBollaDettaglioVo>();
		List<VenditaDettaglioVo> listVenditaDettaglioVo = new ArrayList<VenditaDettaglioVo>();
		int count = 0;
		for (VenditeParamDto dto : params) {
			Boolean isProdottoNonEditoriale = dto.getProdottoNonEditoriale();
			if (isProdottoNonEditoriale != null && isProdottoNonEditoriale) {
				ProdottiNonEditorialiVo prodotto = prodottiService.getProdottiNonEditorialiVo(dto.getCodProdottoNonEditoriale(), codEdicolaMaster);
				ProdottiNonEditorialiBollaDettaglioVo vo = buildProdottiNonEditorialiBollaDettaglioVo(dto, prodotto, count++);
				//GIFT CARD EPIPOLI
				if(prodotto.getIsProdottoDigitale().equals("S")){
					listProdottiNonEditoriali_digitali_BollaDettaglioVo.add(vo);
					
				}else{
					listProdottiNonEditorialiBollaDettaglioVo.add(vo);
				}
			} else {
				VenditaDettaglioVo vo = buildVenditaDettaglioVo(vendita, listVenditaDettaglioVo, dto, isMultiDl, codFiegDlMaster, arrCodFiegDl, arrId, id);
				listVenditaDettaglioVo.add(vo);
			}
		}
		
		vendita.setEnumEsitoVendita("CONTO_CHIUSO");
		
		ProdottiNonEditorialiBollaVo bpne = null;
		
		//GIFT CARD EPIPOLI
		if (!listProdottiNonEditoriali_digitali_BollaDettaglioVo.isEmpty()) {
			bpne = buildProdottiNonEditorialiBollaVo(codiceCliente, codEdicolaMaster);
 			forEach(listProdottiNonEditoriali_digitali_BollaDettaglioVo).getPk().setIdDocumento(bpne.getIdDocumento());
			bpne.setDettagli(listProdottiNonEditoriali_digitali_BollaDettaglioVo);
			bpne.setCodUtente(codUtente);
			vendita.setIdDocumentoProdottiVari(bpne.getIdDocumento());
			
				String epipoliMessaggioPopUp = "";
				List<ConsumaCodiceB2CResponse> listResponseWS = new ArrayList<ConsumaCodiceB2CResponse>();
				for(ProdottiNonEditorialiBollaDettaglioVo prod : listProdottiNonEditoriali_digitali_BollaDettaglioVo){
					String epipoliWS_idRichiesta 	= prod.getPk().getIdRichiestaWSEpipoli();
					String epipoliWS_codiceEan 		= (prod.getProdotto()!=null)?prod.getProdotto().getBarcode():null;
					String epipoliWS_valore 		= (prod.getProdotto()!=null)?prod.getProdotto().getPrezzo().toString().replace(",","."):null;
					
					ConsumaCodiceB2CResponse responseWS = epipoliWebServices.consumaCodiceB2C(epipoliWS_codiceEan, epipoliWS_valore, epipoliWS_idRichiesta,vendita.getCodFiegDl(),vendita.getCodEdicolaDl());
					listResponseWS.add(responseWS);
	
					if (responseWS == null) {
						log.error("vittorio responseWS nullo");
					} else {
						if (responseWS.getConsumaCodiceResponse() == null) {
							log.error("vittorio getConsumaCodiceResponse nullo");
						} else {
							
						}
					}
					
					if(responseWS.getConsumaCodiceResponse().get(0).getEsito().equals("OK")){
						epipoliMessaggioPopUp += "\nRichiesta: "+epipoliWS_idRichiesta+ " - Prodotto digitale attivato correttamente";
					}else{
						epipoliMessaggioPopUp += "\nRichiesta: "+epipoliWS_idRichiesta+ " - Errore durante l\'attivazione del prodotto digitale - "+responseWS.getConsumaCodiceResponse().get(0).getErroreDescrizione();
						
						List<ProdottiNonEditorialiBollaDettaglioVo> nuovaLista_prodottiDigitali = new ArrayList<ProdottiNonEditorialiBollaDettaglioVo>();
						for(ProdottiNonEditorialiBollaDettaglioVo pd : bpne.getDettagli()){
							if(!pd.getPk().getIdRichiestaWSEpipoli().equals(epipoliWS_idRichiesta)){
								nuovaLista_prodottiDigitali.add(pd);
							}
						}
						bpne.setDettagli(nuovaLista_prodottiDigitali);
					}
				}
				
			vendita.setWsEpipoliMessaggioPopup(epipoliMessaggioPopUp);
			vendita.setResponseWS(listResponseWS);
			//VENDITA_PRODOTTI_DIGITALI
			vendita.setEnumEsitoVendita("VENDITA_PRODOTTI_DIGITALI");
		}

//		ProdottiNonEditorialiBollaVo bpne = null;
		if (!listProdottiNonEditorialiBollaDettaglioVo.isEmpty()) {
			bpne = buildProdottiNonEditorialiBollaVo(codiceCliente, codEdicolaMaster);
 			forEach(listProdottiNonEditorialiBollaDettaglioVo).getPk().setIdDocumento(bpne.getIdDocumento());
			bpne.setDettagli(listProdottiNonEditorialiBollaDettaglioVo);
			bpne.setCodUtente(codUtente);
			vendita.setIdDocumentoProdottiVari(bpne.getIdDocumento());
		}
		
		
		
		vendita.setListVenditaDettaglio(listVenditaDettaglioVo);
		vendita.setTrasferitaGestionale(IGerivConstants.INDICATORE_RECORD_NON_TRASFERITO);
		saveContoVendite(vendita, bpne);
		return vendita;
	}
	
	@Override
	public List<VenditaDto> getStoricoConti(Integer codFiegDlMaster, Integer codEdicolaMaster, boolean isMultiDl, String codUtente) {
		List<VenditaDto> listDto = new ArrayList<VenditaDto>();
		Date date = new Date();
		List<IdVenditaProdottNonEditorialeDto> listCodVenditaCodProdotti = getLastContiVendita(codFiegDlMaster, codEdicolaMaster, (isMultiDl ? null : codUtente), date, date, true);
		if (!listCodVenditaCodProdotti.isEmpty()) {
			List<Long> listCodVendita = extract(listCodVenditaCodProdotti, on(IdVenditaProdottNonEditorialeDto.class).getCodVendita());
			List<VenditaDettaglioDto> listVenditaDettaglio = getContiVendite(listCodVendita, (isMultiDl ? null : codUtente));
			for (IdVenditaProdottNonEditorialeDto codVendita : listCodVenditaCodProdotti) {
				Long idConto = codVendita.getCodVendita();
				Long codCliente = codVendita.getCodCliente();
				String nomeCliente = codVendita.getNome();
				String cognomeCliente = codVendita.getCognome();
				Long idDocProdottiVari = codVendita.getIdDocumentoProdottiVari();
				Timestamp dataVendita = codVendita.getDataVendita();
				Integer trasferitaGestionale = codVendita.getTrasferitaGestionale();
				Timestamp dataEstrattoConto = codVendita.getDataEstrattoConto();
				String idScontrino = codVendita.getIdScontrino();
				String fatturaEmessa = codVendita.getFatturaEmessa() == null ? "false" : codVendita.getFatturaEmessa().toString();
				String fatturaContoUnico = codVendita.getFatturaContoUnico().toString();
				String numeroFattura = codVendita.getNumeroFattura().toString();
				List<VenditaDettaglioDto> list = select(listVenditaDettaglio, having(on(VenditaDettaglioDto.class).getProdottoNonEditoriale(), equalTo(false)).and(having(on(VenditaDettaglioDto.class).getIdVendita(), equalTo(idConto))));
				if (list == null || list.isEmpty()) {
					list = select(listVenditaDettaglio, having(on(VenditaDettaglioDto.class).getProdottoNonEditoriale(), equalTo(true)).and(having(on(VenditaDettaglioDto.class).getIdDocumentoProdottiVari(), equalTo(idDocProdottiVari))));
				} else {
					list.addAll(select(listVenditaDettaglio, having(on(VenditaDettaglioDto.class).getProdottoNonEditoriale(), equalTo(true)).and(having(on(VenditaDettaglioDto.class).getIdDocumentoProdottiVari(), equalTo(idDocProdottiVari)))));
				}
				int count = 1;
				for (VenditaDettaglioDto dto : list) {
					dto.setProgressivo(count++);
					if (dto.getImportoTotale() == null && (dto.getPrezzoCopertina() != null && dto.getQuantita() != null)) {
						dto.setImportoTotale(dto.getPrezzoCopertina().multiply(new BigDecimal(dto.getQuantita())));
					}
				}
				VenditaDto dto = new VenditaDto();
				dto.setCodVendita(idConto);
				dto.setTrasferitaGestionale(trasferitaGestionale);
				dto.setDataVendita(dataVendita);
				dto.setListVenditaDettaglio(list);
				dto.setCodCliente(codCliente == null ? "" : codCliente.toString());
				dto.setContoCliente((Strings.isNullOrEmpty(nomeCliente) || Strings.isNullOrEmpty(cognomeCliente) ? "" : MessageFormat.format(IGerivMessageBundle.get("igeriv.conto.del.cliente.tessera.o.nome"), nomeCliente + " " + cognomeCliente)));
				dto.setDataEstrattoConto(dataEstrattoConto);
				dto.setIdScontrino(idScontrino);
				dto.setFatturaEmessa(fatturaEmessa);
				dto.setFatturaContoUnico(fatturaContoUnico);
				dto.setNumeroFattura(numeroFattura);
				dto.setNomeCliente(codVendita.getNomeCognomeCliente());
				listDto.add(dto);
			}
		}
		return listDto;
	}
	
	/**
	 * @return
	 */
	private ProdottiNonEditorialiBollaVo buildProdottiNonEditorialiBollaVo(Long codiceCliente, Integer codEdicolaMaster) {
		ProdottiNonEditorialiBollaVo bpne = new ProdottiNonEditorialiBollaVo();
		bpne.setIdDocumento(prodottiService.getNextSeqVal("SEQUENZA_9545"));
		bpne.setEdicola(edicoleService.getAnagraficaEdicola(codEdicolaMaster));
		bpne.setIndicatoreEmessoRicevuto(IGerivConstants.INDICATORE_DOCUMENTO_EMESSO);
		bpne.setCodiceFornitore(codiceCliente != null ? codiceCliente.intValue() : IGerivConstants.COD_CLIENTE_ANONIMO);
		bpne.setDataRegistrazione(getSysdate());
		bpne.setDeleted(false);
		return bpne;
	}

	/**
	 * @param dto
	 * @param prodotto 
	 * @param count 
	 * @return
	 */
	private ProdottiNonEditorialiBollaDettaglioVo buildProdottiNonEditorialiBollaDettaglioVo(VenditeParamDto dto, ProdottiNonEditorialiVo prodotto, int count) {
		ProdottiNonEditorialiBollaDettaglioVo vo = new ProdottiNonEditorialiBollaDettaglioVo();
		ProdottiNonEditorialiBollaDettaglioPk pk = new ProdottiNonEditorialiBollaDettaglioPk();
		pk.setProgressivo(count);
		vo.setPk(pk);
		vo.setProdotto(prodotto);
		vo.setCausale(prodottiService.getCausaleMagazzino(IGerivConstants.CODICE_CAUSALE_VENDITA));
		vo.setMagazzinoDa(IGerivConstants.COD_MAGAZZINO_INTERNO);
		vo.setMagazzinoA(IGerivConstants.COD_MAGAZZINO_ESTERNO);
		vo.setQuantita(dto.getQuantita());
		vo.setPrezzo(dto.getImportoBigDecimal().floatValue());
		vo.setDeleted(false);
		return vo;
	}

	/**
	 * @param vendita
	 * @param listVenditaDettaglioVo
	 * @param dto
	 * @param isMultiDl 
	 * @param codFiegDlMaster 
	 * @param arrCodFiegDl 
	 * @param arrId 
	 * @param id 
	 * @return VenditaDettaglioVo
	 */
	private VenditaDettaglioVo buildVenditaDettaglioVo(VenditaVo vendita, List<VenditaDettaglioVo> listVenditaDettaglioVo, VenditeParamDto dto, boolean isMultiDl, Integer codFiegDlMaster, Integer[] arrCodFiegDl, Integer[] arrId, Integer id) {
		VenditaDettaglioVo vo = new VenditaDettaglioVo();
		VenditaDettaglioPk pk = new VenditaDettaglioPk();
		pk.setVenditaVo(vendita);
		pk.setProgressivo(new Integer(dto.getProgressivo()));
		vo.setPk(pk);
		vo.setIdtn((dto.getIdtn() != null && !dto.getIdtn().equals("")) ? new Integer(dto.getIdtn()) : null);
		vo.setCodFiegDl((isMultiDl && dto.getCodFiegDlPubblicazione() != null && dto.getCodFiegDlPubblicazione() > 0) ? dto.getCodFiegDlPubblicazione() : codFiegDlMaster);
		vo.setCodEdicola((isMultiDl && dto.getCodFiegDlPubblicazione() != null && dto.getCodFiegDlPubblicazione() > 0) ? iGerivUtils.getCorrispondenzaCodEdicolaMultiDl(dto.getCodFiegDlPubblicazione(), arrCodFiegDl, arrId) : id);
		BigDecimal prezzoCopertina = new BigDecimal(dto.getImporto());
		vo.setPrezzoCopertina(prezzoCopertina);
		vo.setSottoTitolo(dto.getSottoTitolo());
		vo.setTitolo(dto.getTitolo());
		vo.setNumeroCopertina(dto.getNumeroCopertina());
		vo.setImportoTotale(prezzoCopertina.multiply(new BigDecimal(dto.getQuantita())));
		vo.setQuantita(dto.getQuantita());
		vo.setTrasferitaGestionale(false);
		vo.setDeleted(false);
		return vo;
	}
	
	@Override
	public List<ProdottoClientVenditeDto> getProdottiClientVendite(Integer[] coddl,Integer[] codEdicola) {
		return repository.getProdottiClientVendite(coddl,codEdicola);
	}
	
	@Override
	public PositionSizeDto getBarraSceltaRapidaProdottiVariPositionSizeDto(Integer codFiegDl, Integer codEdicola) {
		return repository.getBarraSceltaRapidaProdottiVariPositionSizeDto(codFiegDl, codEdicola);
	}
	
	@Override
	public BarraSceltaRapidaProdottiVariVo getBarraSceltaRapidaProdottiVariVo(Integer codFiegDl, Integer codEdicola) {
		return repository.getBarraSceltaRapidaProdottiVariVo(codFiegDl, codEdicola);
	}
	
	@Override
	public int getCopieConsegnateGazzettaCard(Integer codFiegDl, Integer codEdicolaDl, Timestamp dataUscita) {
		return repository.getCopieConsegnateGazzettaCard(codFiegDl, codEdicolaDl, dataUscita);
	}
	
	@Override
	public int getDistribuitoGazzetta(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dataUscita) {
		return repository.getDistribuitoGazzetta(codFiegDl, codEdicolaWeb, dataUscita);
	}
}
