package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.exception.PubblicazioneInesistenteException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.VenditaDettaglioVo;
import it.dpe.igeriv.vo.VenditaVo;
import it.dpe.igeriv.vo.pk.VenditaDettaglioPk;
import it.dpe.inforiv.dto.output.InforivVenditeDto;
import it.dpe.inforiv.importer.InforivImporter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.lambdaj.group.Group;

/**
 * @author romanom
 */
@Component("InforivVenditeImporter")
public class InforivVenditeImporterImpl extends InforivBaseImporter implements InforivImporter<InforivVenditeDto> {
	private final IGerivBatchService bo;
	
	@Autowired
	InforivVenditeImporterImpl(IGerivBatchService bo) {
		this.bo = bo;
	}
	
	@Override
	public void importData(List<InforivVenditeDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		try {
			Group<InforivVenditeDto> group = group(list, by(on(InforivVenditeDto.class).getIdUnitaVendita()));
			for (Group<InforivVenditeDto> subgroup : group.subgroups()) {
				List<InforivVenditeDto> findAll = subgroup.findAll();
				BigDecimal importoTotale = new BigDecimal(sum(findAll, on(InforivVenditeDto.class).getImporto()));
				InforivVenditeDto inforivVenditeDto = findAll.get(0);
				VenditaVo vendita = new VenditaVo();
				vendita.setCodEdicola(codEdicolaWeb);
				vendita.setCodFiegDl(inforivVenditeDto.getCodFiegDl());
				vendita.setCodUtente(codEdicolaWeb.toString());
				vendita.setCodVendita(bo.getNextSeqVal(IGerivConstants.SEQ_VENDITE));
				vendita.setDataVendita(new Timestamp(new Date().getTime()));
				vendita.setCodCliente(null);
				vendita.setImportoTotale(importoTotale);
				vendita.setContoScontrinato(false);
				vendita.setPagato(true);
				vendita.setIdScontrino(null);
				vendita.setDataScontrino(null);
				vendita.setFatturaEmessa(false);
				vendita.setFatturaContoUnico(false);
				vendita.setIdFattura(null);
				vendita.setProvenienzaConto(IGerivConstants.PROVENIENZA_CONTO_IMPORTAZIONE_INFORIV);
				vendita.setDataCompetenzaEstrattoContoClienti(null);
				vendita.setDeleted(false);
				List<VenditaDettaglioVo> listVenditaDettaglioVo = new ArrayList<VenditaDettaglioVo>();
				int progressivo = 0;
				for (InforivVenditeDto dto : findAll) {
					try {
						listVenditaDettaglioVo.add(buildVenditaDettaglioVo(vendita, dto, codEdicolaWeb, progressivo++));
					} catch (Throwable e) {
						ImportException err = new ImportException(e);
						err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
						listErrori.add(err);
					}
				}
				vendita.setListVenditaDettaglio(listVenditaDettaglioVo);
				vendita.setTrasferitaGestionale(IGerivConstants.INDICATORE_RECORD_TRASFERITO);
				bo.saveBaseVo(vendita);
			}
		} catch (Throwable e) {
			ImportException err = new ImportException(e);
			err.setDescrizione(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.errore.importazione.riga.inforiv"), list.get(0).getTipoRecord(), list.get(0).getRecord(), e.getLocalizedMessage()));
			listErrori.add(err);
		}
	}

	/**
	 * Costruisce il dettaglio della vendita
	 * 
	 * @param vendita
	 * @param dto
	 * @param codEdicolaWeb 
	 * @param progressivo 
	 * @return VenditaDettaglioVo
	 * @throws PubblicazioneInesistenteException 
	 */
	private VenditaDettaglioVo buildVenditaDettaglioVo(VenditaVo vendita, InforivVenditeDto dto, Integer codEdicolaWeb, int progressivo) throws PubblicazioneInesistenteException {
		AbbinamentoIdtnInforivVo abii = buildIdtn(dto.getIdProdotto(), dto.getCodFiegDl(), bo);
		StoricoCopertineVo scvo = bo.getStoricoCopertinaByPk(dto.getCodFiegDl(), abii.getIdtn());
		if (scvo == null) {
			throw new PubblicazioneInesistenteException(MessageFormat.format(IGerivMessageBundle.get("imp.file.inforiv.vendite.pubblicazione.non.trovata.exception"), codEdicolaWeb.toString(), dto.getCodFiegDl().toString(), dto.getIdProdotto(), dto.getDescrizione()));
		}
		VenditaDettaglioVo vo = new VenditaDettaglioVo();
		VenditaDettaglioPk pk = new VenditaDettaglioPk();
		pk.setVenditaVo(vendita);
		pk.setProgressivo(progressivo);
		vo.setPk(pk);
		vo.setIdtn(abii.getIdtn());
		vo.setCodFiegDl(dto.getCodFiegDl());
		vo.setCodEdicola(codEdicolaWeb);
		vo.setPrezzoCopertina(dto.getPrezzoCopertina());
		vo.setSottoTitolo(scvo.getSottoTitolo());
		vo.setTitolo(dto.getDescrizione());
		vo.setNumeroCopertina(scvo.getNumeroCopertina());
		vo.setImportoTotale(new BigDecimal(dto.getImporto()));
		vo.setQuantita(dto.getCopieVendute());
		vo.setDeleted(false);
		return vo;
	}
	
}
