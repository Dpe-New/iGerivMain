package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.BollaDettaglioVo;
import it.dpe.igeriv.vo.BollaVo;
import it.dpe.igeriv.vo.ContoDepositoVo;
import it.dpe.igeriv.vo.RichiestaFissaClienteEdicolaVo;
import it.dpe.igeriv.vo.RichiestaClienteVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.pk.BollaDettaglioPk;
import it.dpe.igeriv.vo.pk.BollaPk;
import it.dpe.igeriv.vo.pk.ContoDepositoPk;
import it.dpe.igeriv.vo.pk.RichiestaClientePk;
import it.dpe.inforiv.dto.input.InforivFornitureDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

/**
 * @author romanom
 */
@Component("InforivFornitureImporter")
public class InforivFornitureImporterImpl extends InforivBaseImporter implements InforivImporter<InforivFornitureDto> {
	private final IGerivBatchService bo;
	private final PubblicazioniService pubblicazioniService;
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	InforivFornitureImporterImpl(IGerivBatchService bo, PubblicazioniService pubblicazioniService) {
		this.bo = bo;
		this.pubblicazioniService = pubblicazioniService;
	}
	
	@Override 
	public void importData(List<InforivFornitureDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		try {
			log.info("Inizio importazione FO");
			Group<InforivFornitureDto> group = group(list, by(on(InforivFornitureDto.class).getDataBolla()), by(on(InforivFornitureDto.class).getTipoBolla()), by(on(InforivFornitureDto.class).getNumeroBolla()));
			for (Group<InforivFornitureDto> subgroup : group.subgroups()) {
				List<InforivFornitureDto> listBolla = subgroup.findAll();
				for (InforivFornitureDto dto : listBolla) {
					try {
						Integer codFiegDl = dto.getCodFiegDl();
						AbbinamentoIdtnInforivVo abii = bo.getAbbinamentoIdtnInforiv(codFiegDl, dto.getIdProdotto());
						if (abii == null) {
							throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.idtn.non.trovato"), dto.getIdProdotto()));
						}
						Integer idtn = abii.getIdtn();
						BollaVo bvo = buildBollaVo(idtn, codFiegDl, dto);
						bo.saveBaseVo(bvo);
						BollaDettaglioVo bdvo = buildBollaDettaglioVo(idtn, codFiegDl, dto, codEdicolaWeb);
						bo.saveBaseVo(bdvo);
						
						//Prenotazione clienti
						List<RichiestaFissaClienteEdicolaVo> richiesteClienti = bo.getRichiesteFisseClienti(codFiegDl, codEdicolaWeb, bvo.getCpuDl(), idtn, bvo.getPk().getDtBolla());
						saveRichiesteClienti(codFiegDl, codEdicolaWeb, idtn, bvo, richiesteClienti);
					} catch (Exception e) {
						ImportException err = new ImportException(e);
						err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
						listErrori.add(err);
					}
				}
			}
			log.info("Fine importazione FO");
		} catch (Exception e) {
			ImportException err = new ImportException(e);
			err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
			listErrori.add(err);
		}
	}

	/**
	 * @param codEdicolaWeb 
	 * @param Integer idtn
	 * @param Integer codFiegDl
	 * @param InforivFornitureDto apdto
	 * @return BollaDettaglioVo
	 */
	private BollaDettaglioVo buildBollaDettaglioVo(Integer idtn, Integer codFiegDl, InforivFornitureDto apdto, Integer codEdicolaWeb) {
		Integer posizione = idtn;
		posizione = apdto.getNumeroRiga();
		BollaDettaglioVo vo = bo.getDettaglioBolla(codFiegDl, apdto.getCodEdicola(), apdto.getDataBolla(), apdto.getTipoBolla(), posizione);
		if (vo == null) {
			vo = new BollaDettaglioVo();
			BollaDettaglioPk pk = new BollaDettaglioPk();
			pk.setCodEdicola(codEdicolaWeb);
			pk.setCodFiegDl(codFiegDl);
			pk.setDtBolla(new Timestamp(apdto.getDataBolla().getTime()));
			pk.setTipoBolla(apdto.getTipoBolla());
			pk.setPosizioneRiga(posizione);
			vo.setPk(pk);
		}
		vo.setIdtn(idtn);
		vo.setQuantitaConsegnata(apdto.getFornito());
		vo.setPrezzoLordo(new BigDecimal(apdto.getPrezzoCopertina()));
		vo.setPrezzoNetto(new BigDecimal(apdto.getPrezzoNetto()));
		vo.setSconto(new BigDecimal(apdto.getScontoPuntoVendita()));
		// 30/10/2015 Il campo SPUNT9611 viene impostato a NOT NULL per la gestione della dropdown list all'interno della form Bolla di consegna Visualizza solo non spuntati (SI/NO)
		vo.setSpunta(IGerivConstants.SPUNTA_NO);
		boolean isContoDeposito = apdto.getContoDeposito() != null && apdto.getContoDeposito().equals("S");
		vo.setIndicatoreValorizzare(isContoDeposito ? IGerivConstants.INDICATORE_CONTO_DEPOSITO : IGerivConstants.INDICATORE_VALORIZZARE);
		if (isContoDeposito) {
			ContoDepositoVo cd = pubblicazioniService.getPubblicazioneInContoDeposito(codFiegDl, codEdicolaWeb, idtn);
			if (cd == null) {
				cd = new ContoDepositoVo();
				ContoDepositoPk pk = new ContoDepositoPk();
				pk.setCodDl(codFiegDl);
				pk.setCodEdicola(codEdicolaWeb);
				pk.setIdtn(idtn);
				cd.setPk(pk);
			}
			cd.setQuantita(apdto.getFornito());
			pubblicazioniService.saveBaseVo(cd);
		}
		return vo;
	}

	/**
	 * @param Integer idtn
	 * @param Integer codFiegDl
	 * @param InforivFornitureDto apdto
	 * @return BollaVo
	 */
	private BollaVo buildBollaVo(Integer idtn, Integer codFiegDl, InforivFornitureDto apdto) {
		Date dataBolla = apdto.getDataBolla();
		String tipoBolla = apdto.getTipoBolla();
		Integer posizione = idtn;
		posizione = apdto.getNumeroRiga();
		BollaVo vo = bo.getBollaVo(codFiegDl, dataBolla, tipoBolla, posizione);
		if (vo == null) {
			vo = new BollaVo();
			BollaPk pk = new BollaPk();
			pk.setCodFiegDl(codFiegDl);
			pk.setDtBolla(new Timestamp(dataBolla.getTime()));
			pk.setTipoBolla(tipoBolla);
			pk.setPosizioneRiga(posizione);
			vo.setPk(pk);
		}
		vo.setIdTestataNumero(idtn);
		StoricoCopertineVo copertina = bo.getStoricoCopertinaByPk(codFiegDl, idtn);
		
		vo.setCpuDl(copertina.getCodicePubblicazione());
		vo.setNumeroPubblicazione(copertina.getNumeroCopertina());
		vo.setTitolo(copertina.getAnagraficaPubblicazioniVo().getTitolo());
		vo.setSottoTitolo(copertina.getSottoTitolo());
		vo.setPrezzoLordo(new BigDecimal(apdto.getPrezzoCopertina()));
		vo.setPercentualeIva(apdto.getPercentualeDefiscalizzazione());
		vo.setRiga(apdto.getNumeroRiga());
		
		//DATA ADDEBITO C/D
		
		if(copertina!=null && apdto.getDataAddebitoContoDeposito()!=null){	
			copertina.setDataFatturazionePrevista(new Timestamp(apdto.getDataAddebitoContoDeposito().getTime()));
			bo.saveBaseVo(copertina);
		}
		return vo;
	}

	/**
	 * @param Integer codFiegDl
	 * @param Integer codEdicolaWeb
	 * @param Integer idtn
	 * @param BollaVo bvo
	 * @param List<RichiestaFissaClienteEdicolaVo> richieste
	 */
	private void saveRichiesteClienti (Integer codFiegDl, Integer codEdicolaWeb, Integer idtn, BollaVo bvo, List<RichiestaFissaClienteEdicolaVo> richieste) {
		for (RichiestaFissaClienteEdicolaVo richVo: richieste) {
			RichiestaClienteVo vo = new RichiestaClienteVo();
			RichiestaClientePk pk = new RichiestaClientePk();
			vo.setPk(pk);
			pk.setCodCliente(richVo.getPk().getCodCliente());
			pk.setCodDl(codFiegDl);
			pk.setCodEdicola(codEdicolaWeb);
			pk.setDataInserimento(bvo.getPk().getDtBolla());
			pk.setProvenienza(3);
			pk.setIdtn(idtn);
			vo.setQuantitaRichiesta(richVo.getQuantitaRichiesta());
			vo.setQuantitaEvasa(0);
			vo.setStatoEvasione(0);
			vo.setRichiedereDifferenzaDl(0);
			bo.saveBaseVo(vo);
		}
	}
}
