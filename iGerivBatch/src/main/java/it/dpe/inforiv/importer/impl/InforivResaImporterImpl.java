package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectDistinct;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.BollaResaDettaglioVo;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import it.dpe.igeriv.vo.BollaResaVo;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.DecodificaRichiamiResaVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.pk.BollaResaDettaglioPk;
import it.dpe.igeriv.vo.pk.BollaResaPk;
import it.dpe.igeriv.vo.pk.BollaResaRiassuntoPk;
import it.dpe.inforiv.dto.input.InforivResaDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

/**
 * @author romanom
 */
@Component("InforivResaImporter")
public class InforivResaImporterImpl extends InforivBaseImporter implements InforivImporter<InforivResaDto> {
	private final IGerivBatchService bo;
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	InforivResaImporterImpl(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	@Override 
	public void importData(List<InforivResaDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		try {
			log.info("Inizio importazione RR");
			Group<InforivResaDto> group = group(list, by(on(InforivResaDto.class).getDataBolla()), by(on(InforivResaDto.class).getTipoBolla()), by(on(InforivResaDto.class).getNumeroBolla()));
			for (Group<InforivResaDto> subgroup : group.subgroups()) {
				List<InforivResaDto> listBolla = subgroup.findAll();
				for (InforivResaDto dto : listBolla) {
					try {
						Integer codFiegDl = dto.getCodFiegDl();
						AbbinamentoIdtnInforivVo abii = bo.getAbbinamentoIdtnInforiv(codFiegDl, dto.getIdProdotto());
						if (abii == null) {
							throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.idtn.non.trovato"), dto.getIdProdotto()));
						}
						Integer idtn = abii.getIdtn();
						//BollaResaRiassuntoVo brrvo = buildBollaResaRiassuntoVo(dto, codEdicolaWeb);
						BollaResaVo bvo = buildBollaResaVo(idtn, codFiegDl, dto);
						bo.saveBaseVo(bvo);
						BollaResaDettaglioVo bdvo = buildBollaResaDettaglioVo(idtn, codFiegDl, dto, codEdicolaWeb);
						bo.saveBaseVo(bdvo);
						//bo.saveBaseVo(brrvo);
					} catch (Exception e) {
						ImportException err = new ImportException(e);
						err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
						listErrori.add(err);
					}
				}
			}
			// TESTA RIVENDITA TBL_9620
			Collection<InforivResaDto> dateTipo = selectDistinct(list, new Comparator<InforivResaDto>() {
				@Override
				public int compare(InforivResaDto o1, InforivResaDto o2) {
					if (o1 != null && o2 != null) {
						if (o1.getDataBolla().equals(o2.getDataBolla())) {
							return o1.getTipoBolla().compareTo(o2.getTipoBolla());
						} else {
							return o1.getDataBolla().compareTo(o2.getDataBolla());
						}
					} else {
						return 0;
					}
				}
			});

			for (InforivResaDto dto : dateTipo) {
				try {
					BollaResaRiassuntoVo brrvo = buildBollaResaRiassuntoVo(dto, codEdicolaWeb);
					bo.saveBaseVo(brrvo);
				} catch (Exception e) {
					ImportException err = new ImportException(e);
					err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
					listErrori.add(err);
				}
			}

			
			log.info("Fine importazione RR");
		} catch (Exception e) {
			ImportException err = new ImportException(e);
			err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
			listErrori.add(err);
		}
	}

	/**
	 * @param dto
	 * @param codEdicolaWeb
	 * @return
	 * @throws InvalidRecordException
	 */
	private BollaResaRiassuntoVo buildBollaResaRiassuntoVo(InforivResaDto dto, Integer codEdicolaWeb) throws InvalidRecordException {
		Timestamp dtBolla = new Timestamp(dto.getDataBolla().getTime());
		List<BollaResaRiassuntoVo> list = bo.getBollaResaRiassunto(new Integer[]{dto.getCodFiegDl()}, new Integer[]{codEdicolaWeb}, dtBolla, dto.getTipoBolla());
		BollaResaRiassuntoVo vo = (list == null || list.isEmpty()) ? null : list.get(0);
		if (vo == null) {
			vo = new BollaResaRiassuntoVo();
			//0000175
			vo.setBollaTrasmessaDl(IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA);
			BollaResaRiassuntoPk pk = new BollaResaRiassuntoPk();
			pk.setCodEdicola(codEdicolaWeb);
			pk.setCodFiegDl(dto.getCodFiegDl());
			pk.setDtBolla(dtBolla);
			pk.setTipoBolla(dto.getTipoBolla());
			vo.setPk(pk);
		}
		//Importazione Dati da Inforiv - ERRORE FATALE
		if(vo.getBollaTrasmessaDl()==null)
			vo.setBollaTrasmessaDl(IGerivConstants.INDICATORE_BOLLA_NON_TRASMESSA);
		
		vo.setValoreBolla(new BigDecimal(0));
		vo.setGruppoSconto(codEdicolaWeb);
		return vo;
	}
	
	/**
	 * @param idtn
	 * @param codFiegDl
	 * @param apdto
	 * @param codEdicolaWeb
	 * @return
	 */
	private BollaResaDettaglioVo buildBollaResaDettaglioVo(Integer idtn, Integer codFiegDl, InforivResaDto apdto, Integer codEdicolaWeb) {
		Integer posizione = 0;
		if (apdto.getNumeroRiga8() != null && apdto.getNumeroRiga8() > 0) {
			posizione = apdto.getNumeroRiga8();
		} else {
			posizione = idtn;
		}
		BollaResaDettaglioVo vo = bo.getBollaResaDettaglioVo(codFiegDl, codEdicolaWeb, apdto.getDataBolla(), apdto.getTipoBolla(), posizione);
		if (vo == null) {
			vo = new BollaResaDettaglioVo();
			BollaResaDettaglioPk pk = new BollaResaDettaglioPk();
			pk.setCodEdicola(codEdicolaWeb);
			pk.setCodFiegDl(codFiegDl);
			pk.setDtBolla(new Timestamp(apdto.getDataBolla().getTime()));
			pk.setTipoBolla(apdto.getTipoBolla());
			pk.setPosizioneRiga(posizione);
			vo.setPk(pk);
		}
		vo.setIdtn(idtn);
		vo.setDistribuito(new Long(apdto.getCopie()));
		vo.setPrezzoNetto(new BigDecimal(apdto.getPrezzoNetto()));
		return vo;
	}

	/**
	 * @param idtn
	 * @param codFiegDl
	 * @param apdto
	 * @return
	 */
	private BollaResaVo buildBollaResaVo(Integer idtn, Integer codFiegDl, InforivResaDto apdto) {
		Date dataBolla = apdto.getDataBolla();
		String tipoBolla = apdto.getTipoBolla();
		Integer posizione = 0;
		if (apdto.getNumeroRiga8() != null && apdto.getNumeroRiga8() > 0) {
			posizione = apdto.getNumeroRiga8();
		} else {
			posizione = idtn;
		}
		BollaResaVo vo = bo.getBollaResaVo(codFiegDl, dataBolla, tipoBolla, posizione);
		DecodificaRichiamiResaVo numeroRichiamo = bo.getRichiamoResa(codFiegDl, apdto.getCausaliResa());
		if (vo == null) {
			vo = new BollaResaVo();
			BollaResaPk pk = new BollaResaPk();
			pk.setCodFiegDl(codFiegDl);
			pk.setDtBolla(new Timestamp(dataBolla.getTime()));
			pk.setTipoBolla(tipoBolla);
			pk.setPosizioneRiga(posizione);
			
			vo.setPk(pk);
		}
		vo.setNumeroRichiamo(numeroRichiamo);
		
		//tagr9619	-- Aggiunta Tipo
		vo.setAggiuntaTipo(numeroRichiamo.getPk().getTipoRichiamoResa());
		
		StoricoCopertineVo copertina = bo.getStoricoCopertinaByPk(codFiegDl, idtn);
		vo.setIdTestataNumero(idtn);
		vo.setCpuDl(copertina.getCodicePubblicazione());
		vo.setNumeroPubblicazione(copertina.getNumeroCopertina());
		vo.setTitolo(copertina.getAnagraficaPubblicazioniVo().getTitolo());
		vo.setSottoTitolo(copertina.getSottoTitolo());
		vo.setPrezzoLordo(new BigDecimal(apdto.getPrezzoCopertina()));
		if (apdto.getNumeroRiga8() != null && apdto.getNumeroRiga8() > 0) {
			vo.setRiga(apdto.getNumeroRiga8());
		} else {
			vo.setRiga(apdto.getNumeroRiga());
		}
		 
		if(copertina!=null && numeroRichiamo!=null ){
			// tire9607
			boolean aggiornoDataResa = true;
			boolean aggiornoCausaleResa = true;
			int causaleRichiamo = numeroRichiamo.getPk().getTipoRichiamoResa();
			// PER DISPE Richiamo finale per una rivendita
			if (apdto.getNumeroBolla() != null && 
					apdto.getNumeroBolla().compareTo(1L)>0 &&
					(causaleRichiamo==2 || causaleRichiamo==12)) {
				aggiornoDataResa = false;
				aggiornoCausaleResa = false;
			}
			// PER DISPE 12 = Richiamo resa conto deposito
			if (causaleRichiamo==12) {
				aggiornoCausaleResa = false;
			}
			if (aggiornoCausaleResa) {
				copertina.setTipoRichiamoResa(causaleRichiamo);
			}
			if (aggiornoDataResa) {
				copertina.setDataRichiamoResa(new Timestamp(dataBolla.getTime()));
			}
			bo.saveBaseVo(copertina);
		}	
		
		return vo;
	}

}
