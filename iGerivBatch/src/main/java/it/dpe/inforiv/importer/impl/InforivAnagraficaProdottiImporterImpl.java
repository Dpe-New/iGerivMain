package it.dpe.inforiv.importer.impl;

import static ch.lambdaj.Lambda.forEach;
import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.exception.ImportException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.AnagraficaPubblicazioniVo;
import it.dpe.igeriv.vo.ArgomentoVo;
import it.dpe.igeriv.vo.PeriodicitaTrascodificaInforeteVo;
import it.dpe.igeriv.vo.PeriodicitaVo;
import it.dpe.igeriv.vo.PrezzoEdicolaVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.pk.AnagraficaPubblicazioniPk;
import it.dpe.igeriv.vo.pk.ArgomentoPk;
import it.dpe.igeriv.vo.pk.PrezzoEdicolaPk;
import it.dpe.igeriv.vo.pk.StoricoCopertinePk;
import it.dpe.inforiv.dto.input.InforivAnagraficaProdottiDto;
import it.dpe.inforiv.importer.InforivImporter;
import it.dpe.inforiv.util.SegmentiDiMercatoInforete;
import it.dpe.inforiv.util.ValidationCodiceBarre;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 */
@Component("InforivAnagraficaProdottiImporter")
public class InforivAnagraficaProdottiImporterImpl extends InforivBaseImporter implements InforivImporter<InforivAnagraficaProdottiDto> {
	private final IGerivBatchService bo;
	private final SegmentiDiMercatoInforete segmentiDiMercatoInforete;
	private final ValidationCodiceBarre validationCodiceBarre;
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	InforivAnagraficaProdottiImporterImpl(IGerivBatchService bo, SegmentiDiMercatoInforete segmentiDiMercatoInforete,ValidationCodiceBarre validationCodiceBarre) {
		this.bo = bo;
		this.segmentiDiMercatoInforete = segmentiDiMercatoInforete;
		this.validationCodiceBarre = validationCodiceBarre;
	}
	
	@Override
	public void importData(List<InforivAnagraficaProdottiDto> list, List<ImportException> listErrori, Integer codEdicolaWeb, Map<String, String> parameters) {
		log.info("Inizio importazione AP");
		Set<InforivAnagraficaProdottiDto> set = new HashSet<InforivAnagraficaProdottiDto>(list);
		for (InforivAnagraficaProdottiDto apdto : set) {
			Integer codFiegDl = apdto.getCodFiegDl();
			Integer cpu = apdto.getCodiceTestata();
			AnagraficaPubblicazioniVo apvo = buildAnagraficaPubblicazione(codFiegDl, cpu, apdto);
			bo.saveBaseVo(apvo); 
		}
		if (list != null && !list.isEmpty()) {
			forEach(list).setAnagraficaProdotto(false);
			set = new HashSet<InforivAnagraficaProdottiDto>(list);
			for (InforivAnagraficaProdottiDto apdto : set) {
				Integer codFiegDl = apdto.getCodFiegDl();
				Integer cpu = apdto.getCodiceTestata();
				AbbinamentoIdtnInforivVo abii = buildIdtn(apdto.getIdProdotto(), codFiegDl, bo);
				//Build StoricoCopertine tbl_9607
				StoricoCopertineVo scvo = buildStoricoCopertina(apdto, codFiegDl, cpu, abii.getIdtn());
				bo.saveBaseVo(scvo);
				bo.saveBaseVo(abii);
				if (apdto.getPrezzoNetto() != null && apdto.getPrezzoNetto() < 9999f) {
					PrezzoEdicolaVo pevo = new PrezzoEdicolaVo();
					PrezzoEdicolaPk pk = new PrezzoEdicolaPk();
					pk.setCodFiegDl(codFiegDl);
					pk.setGruppoSconto(codEdicolaWeb);
					pk.setIdtn(abii.getIdtn());
					pevo.setPk(pk);
					pevo.setPrezzoNetto(new BigDecimal(apdto.getPrezzoNetto()));
					bo.saveBaseVo(pevo);
				}
			}
		}
		log.info("Fine importazione AP");
	}

	/**
	 * @param apdto
	 * @param codFiegDl
	 * @param cpu
	 * @param idtn
	 * @return
	 */
	private StoricoCopertineVo buildStoricoCopertina(InforivAnagraficaProdottiDto apdto, Integer codFiegDl, Integer cpu, Integer idtn) {
		StoricoCopertineVo scvo = bo.getStoricoCopertinaByPk(codFiegDl, idtn);
		if (scvo == null) {
			scvo = new StoricoCopertineVo();
			StoricoCopertinePk pk = new StoricoCopertinePk();
			pk.setCodDl(codFiegDl);
			pk.setIdtn(idtn);
			scvo.setPk(pk);
		}
		scvo.setCodicePubblicazione(cpu);
		scvo.setComplementoDistribuzione(IGerivConstants.CDDT_MAX_VALUE - new Integer(DateUtilities.getTimestampAsString(apdto.getDataUscita(), DateUtilities.FORMATO_DATA_YYMMDD)));
		scvo.setNumeroCopertina(apdto.getNumeroCopertina());
		scvo.setDataUscita(new Timestamp(apdto.getDataUscita().getTime()));
		scvo.setSottoTitolo(apdto.getSottotitolo());
		scvo.setPrezzoCopertina(new BigDecimal(apdto.getPrezzoCopertina()));
		scvo.setCodiceInforete(new Integer(apdto.getCodiceTestataInforete().toString() + apdto.getVarianteProdottoInforete().toString()));
		scvo.setNumeroCopertinaInforete(apdto.getNumeroInforeteOAddon());
		if (apdto.getBarcodeCompleto()!=null && !apdto.getBarcodeCompleto().equals("")) {
			scvo.setCodiceBarre(apdto.getBarcodeCompleto());
		} else {
			scvo.setCodiceBarre(validationCodiceBarre.validateAndInsertCodiceBarre(apdto.getBarcode(),apdto.getNumeroInforeteOAddon()));
		}
		//scvo.setCodiceBarre(apdto.getBarcode() + apdto.getNumeroInforeteOAddon());
		return scvo;
	}

	/**
	 * @param codFiegDl
	 * @param cpu
	 * @param apdto
	 * @param codEdicolaWeb 
	 * @param dataStorico 
	 * @param codEdicolaWeb 
	 * @return
	 */
	private AnagraficaPubblicazioniVo buildAnagraficaPubblicazione(Integer codFiegDl, Integer cpu, InforivAnagraficaProdottiDto apdto) {
		AnagraficaPubblicazioniVo apvo = bo.getAnagraficaPubblicazioneByPk(codFiegDl, cpu);
		if (apvo == null) { 
			apvo = new AnagraficaPubblicazioniVo();
			AnagraficaPubblicazioniPk pk = new AnagraficaPubblicazioniPk();
			pk.setCodFiegDl(codFiegDl);
			pk.setCodicePubblicazione(cpu);
			apvo.setPk(pk);
		}
		apvo.setTitolo(apdto.getTitolo());
		apvo.setPeriodicitaInforete(new Integer(apdto.getPeriodicita()));
		PeriodicitaTrascodificaInforeteVo trascodificaPeriodicitaVo = bo.getPeriodicitaTrascodificaInforete(new Integer(apdto.getPeriodicita()));
		PeriodicitaVo periodicita = null;
		
		//System.out.println(apdto.getBarcode()+" - "+apdto.getTitolo());
		
		if (apdto.getTipoProdotto().equals(3)) {
			periodicita = bo.getPeriodicita(IGerivConstants.TIPO_OPERAZIONE_GESDIS, IGerivConstants.COD_PERIODICITA_ENCICLOPEDIA);
		} else if (trascodificaPeriodicitaVo != null && trascodificaPeriodicitaVo.getPeriodicita() != null) {
			periodicita = trascodificaPeriodicitaVo.getPeriodicita();
		} else {
			periodicita = bo.getPeriodicita(IGerivConstants.TIPO_OPERAZIONE_GESDIS, IGerivConstants.COD_PERIODICITA_NUMERO_UNICO);
		}
		apvo.setPeriodicitaVo(periodicita);
		apvo.setNumCopertinePrecedentiPerRifornimenti(IGerivConstants.NUM_MAX_COPERTINE_PRECEDENTI_PER_RIFORNIMENTI);
		apvo.setSegmentoMercatoInforete(apdto.getSegmentoMercato());
		apvo.setArgomentoDl(apdto.getSegmentoMercato());
		
		ArgomentoVo argomentoVo = bo.getArgomentoVo(apdto.getCodFiegDl(), apdto.getSegmentoMercato());
		if (argomentoVo == null) {
			argomentoVo = new ArgomentoVo();
			ArgomentoPk pk = new ArgomentoPk();
			pk.setCodDl(apdto.getCodFiegDl());
			pk.setSegmento(apdto.getSegmentoMercato());
			argomentoVo.setPk(pk);
			argomentoVo.setDescrizione(segmentiDiMercatoInforete.get(apdto.getSegmentoMercato()));
			bo.saveBaseVo(argomentoVo);
		}
		apvo.setArgomentoVo(argomentoVo);
		apvo.setCodFornitore(apdto.getCodiceEditore());
		return apvo;
	}
	
}
