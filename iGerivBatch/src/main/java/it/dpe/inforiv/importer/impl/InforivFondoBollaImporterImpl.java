package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dto.BollaResaDettaglioDto;
import it.dpe.igeriv.dto.PubblicazionePiuVendutaDto;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.ContoDepositoVo;
import it.dpe.igeriv.vo.FondoBollaDettaglioVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.TipoFondoBollaVo;
import it.dpe.igeriv.vo.pk.FondoBollaDettaglioPk;
import it.dpe.inforiv.dto.input.InforivFondoBollaDto;
import it.dpe.inforiv.importer.InforivImporter;
import lombok.extern.log4j.Log4j;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
@Component("InforivFondoBollaImporter")
@Log4j
public class InforivFondoBollaImporterImpl extends InforivBaseImporter implements InforivImporter<InforivFondoBollaDto> {
	private final IGerivBatchService bo;
	private final PubblicazioniService pubblicazioniService;
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	InforivFondoBollaImporterImpl(IGerivBatchService bo,PubblicazioniService pubblicazioniService) {
		this.bo = bo;
		this.pubblicazioniService = pubblicazioniService;
	}
	
	@Override
	public void importData(List<InforivFondoBollaDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		log.info("Inizio importazione FB");
		Group<InforivFondoBollaDto> group = group(list, by(on(InforivFondoBollaDto.class).getDataBolla()), by(on(InforivFondoBollaDto.class).getTipoBolla()), by(on(InforivFondoBollaDto.class).getNumeroBolla()));
		
		if (group.subgroups().size() > 0) {
			for (Group<InforivFondoBollaDto> subgroup : group.subgroups()) {
				for(String key : subgroup.keySet()){
					Group<InforivFondoBollaDto> subgroup2 =  subgroup.findGroup(key);
					List<InforivFondoBollaDto> listBolla = subgroup2.findAll();
					//List<InforivFondoBollaDto> listBolla = subgroup.findAll();
					Integer codFiegDl = listBolla.get(0).getCodFiegDl();
					Timestamp dataBolla = new Timestamp(listBolla.get(0).getDataBolla().getTime());
					String tipoBolla = listBolla.get(0).getTipoBolla();
					bo.deleteFondoBollaEdicolaInforiv(codFiegDl, codEdicolaWeb, dataBolla, tipoBolla);
					Integer posizioneRiga = bo.getLastPosizioneRigaBolla(codFiegDl, codEdicolaWeb, dataBolla, tipoBolla);
					posizioneRiga = (posizioneRiga == null) ? 1 : posizioneRiga;
					Integer posizioneRigaOrg = posizioneRiga;
					List<FondoBollaDettaglioVo> fondoDettagli = new ArrayList();
					
					for (InforivFondoBollaDto dto : listBolla) {
						try {
							AbbinamentoIdtnInforivVo abii = bo.getAbbinamentoIdtnInforiv(codFiegDl, dto.getIdProdotto());
							if (abii == null) {
								throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.idtn.non.trovato"), dto.getIdProdotto()));
							}
							Integer idtn = abii.getIdtn();
							FondoBollaDettaglioVo bvo = buildFondoBollaDettaglio(idtn, codFiegDl, codEdicolaWeb, new Timestamp(dto.getDataBolla().getTime()), dto.getTipoBolla(), posizioneRiga++, dto);
							fondoDettagli.add(bvo);
							//bo.saveBaseVo(bvo);
							
							//Accredito Conto Deposito : Ticket 0000218					
							if(dto.getTipoMovimento() == IGerivConstants.COD_TIPO_MOVIMENTO_ACCREDITO){
								ContoDepositoVo cd = pubblicazioniService.getPubblicazioneInContoDeposito(codFiegDl, codEdicolaWeb, idtn);
								if(cd!=null)bo.deleteVo(cd);
							}
							
						} catch (InvalidRecordException e) {
							ImportException err = new ImportException(e);
							err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
							listErrori.add(err);
						}
					}
					
					// Sort
					fondoDettagli = sort(fondoDettagli, on(FondoBollaDettaglioVo.class).getSortCriteria());
					
					for (FondoBollaDettaglioVo bvo: fondoDettagli) {
						bvo.getPk().setPosizioneRiga(++posizioneRigaOrg);
						//log.info(String.format("fondo bolla %d d %s", bvo.getPk().getPosizioneRiga(), bvo.getTipoFondoBollaVo().getTipoRecordFondoBolla(), bvo.getTitolo()));
						bo.saveBaseVo(bvo);
					}
				}
			}
		}
		log.info("Fine importazione FB");
	}

	/**
	 * @param idtn
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataBolla
	 * @param tipoBolla
	 * @param posizioneRiga
	 * @param dto
	 * @return
	 */
	private FondoBollaDettaglioVo buildFondoBollaDettaglio(Integer idtn, Integer codFiegDl, Integer codEdicola, Timestamp dataBolla, String tipoBolla, int posizioneRiga, InforivFondoBollaDto dto) {
		FondoBollaDettaglioVo vo = new FondoBollaDettaglioVo();
		FondoBollaDettaglioPk pk = new FondoBollaDettaglioPk();
		pk.setCodEdicola(codEdicola);
		pk.setCodFiegDl(codFiegDl);
		pk.setDtBolla(dataBolla);
		pk.setTipoBolla(tipoBolla);
		pk.setPosizioneRiga(posizioneRiga);
		vo.setPk(pk);
		vo.setQuantitaConsegnata(dto.getCopie());
		vo.setIdtn(idtn);
		vo.setPrezzoLordo(new BigDecimal(dto.getPrezzoCopertina()));
		vo.setPrezzoNetto(new BigDecimal(dto.getPrezzoNetto()));
		StoricoCopertineVo copertina = bo.getStoricoCopertinaByPk(codFiegDl, idtn);
		vo.setTitolo(copertina.getAnagraficaPubblicazioniVo().getTitolo());
		vo.setSottoTitolo(copertina.getSottoTitolo());
		TipoFondoBollaVo atfbvo = bo.getAbbinamentoTipoMovimentoFondoBollaInforiv(dto.getTipoMovimento()).getTipoFondoBollaVo();
		vo.setTipoFondoBollaVo(atfbvo);
		return vo;
	}

}
