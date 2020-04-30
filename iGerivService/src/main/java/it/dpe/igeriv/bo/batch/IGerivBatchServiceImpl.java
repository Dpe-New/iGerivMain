package it.dpe.igeriv.bo.batch;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoProdottiDto;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.AbbinamentoTipoMovimentoFondoBollaInforivVo;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.AnagraficaPubblicazioniVo;
import it.dpe.igeriv.vo.ArgomentoVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.BollaDettaglioVo;
import it.dpe.igeriv.vo.BollaResaDettaglioVo;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import it.dpe.igeriv.vo.BollaResaVo;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.BollaStatisticaStoricoVo;
import it.dpe.igeriv.vo.BollaVo;
import it.dpe.igeriv.vo.DecodificaRichiamiResaVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.EmailRivenditaVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.ImmaginePubblicazioneVo;
import it.dpe.igeriv.vo.InforivFtpFileVo;
import it.dpe.igeriv.vo.LavorazioneResaVo;
import it.dpe.igeriv.vo.MessaggiBollaVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.PeriodicitaTrascodificaInforeteVo;
import it.dpe.igeriv.vo.PeriodicitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.ResaRiscontrataVo;
import it.dpe.igeriv.vo.RichiestaFissaClienteEdicolaVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mromano
 * 
 */
@Service("IGerivBatchService")
class IGerivBatchServiceImpl implements IGerivBatchService {
	private final IGerivBatchBo bo;
	
	@Autowired
	IGerivBatchServiceImpl(IGerivBatchBo bo) {
		this.bo = bo;
	}
	
	@Override
	public BaseVo saveBaseVo(BaseVo vo) {
		return bo.saveBaseVo(vo);
	}

	@Override
	public BaseVo mergeBaseVo(BaseVo lrvo) {
		return bo.mergeBaseVo(lrvo);
	}

	@Override
	public void cleanupNonUsedImagesProdottiVari() {
		bo.cleanupNonUsedImagesProdottiVari();
	}

	@Override
	public void cleanupOldImagesPubblicazioni() {
		bo.cleanupOldImagesPubblicazioni();
	}

	@Override
	public void deleteBolleProdottiVari() {
		bo.deleteBolleProdottiVari();
	}

	@Override
	public List<ProdottiNonEditorialiCategoriaEdicolaVo> getProdottiNonEditorialiCategorieEdicolaVo(Integer codEdicola) {
		return bo.getProdottiNonEditorialiCategorieEdicolaVo(codEdicola);
	}

	@Override
	public List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> getProdottiNonEditorialiSottoCategorieEdicolaVo(Long codCategoria, Integer codEdicola) {
		return bo.getProdottiNonEditorialiSottoCategorieEdicolaVo(codCategoria, codEdicola);
	}

	@Override
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicola(Long categoria, Long sottocategoria, Integer codEdicola) {
		return bo.getProdottiNonEditorialiEdicola(categoria, sottocategoria, codEdicola);
	}

	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleNonSospese() {
		return bo.getEdicoleNonSospese();
	}
	
	@Override
	public List<EdicolaDto> getEdicoleInforiv() {
		return bo.getEdicoleInforiv();
	}

	@Override
	public ParametriEdicolaVo getParametroEdicola(Integer codDpeWebEdicola, Integer codParametroEdicolaGiorniStoricoBolleProdottiVari) {
		return bo.getParametroEdicola(codDpeWebEdicola, codParametroEdicolaGiorniStoricoBolleProdottiVari);
	}

	@Override
	public void deleteBolleProdottiVariEdicolaBeforeDate(Integer codDpeWebEdicola, Date time) {
		bo.deleteBolleProdottiVariEdicolaBeforeDate(codDpeWebEdicola, time);
	}

	@Override
	public Integer getCodDpeWebEdicolaAbbinamentoEdicolaDlVo(Integer codiceRivenditaDl, Integer codFiegDl) {
		return bo.getCodDpeWebEdicolaAbbinamentoEdicolaDlVo(codiceRivenditaDl, codFiegDl);
	}

	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita) {
		return bo.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(codiceDl, codiceRivendita);
	}

	@Override
	public LavorazioneResaVo getLavorazioneResaVo(String zipFile) {
		return bo.getLavorazioneResaVo(zipFile);
	}

	@Override
	public ImmaginePubblicazioneVo getImmaginePubblicazione(String barcode) {
		return bo.getImmaginePubblicazione(barcode);
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getQuotidianoByTitolo(String titolo) {
		return bo.getQuotidianoByTitolo(titolo);
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getPeriodicoByTitolo(String titolo) {
		return bo.getPeriodicoByTitolo(titolo);
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getListAnagraficaPubblicazioneByCodQuotidiano(Integer codFiegDl, Integer codInizioQuotidiano, Integer codFineQuotidiano) {
		return bo.getListAnagraficaPubblicazioneByCodQuotidiano(codFiegDl, codInizioQuotidiano, codFineQuotidiano);
	}

	@Override
	public Timestamp getSysdate() {
		return bo.getSysdate();
	}

	@Override
	public List<AnagraficaAgenziaVo> getAgenzieInforiv() {
		return bo.getAgenzieInforiv();
	}

	@Override
	public AnagraficaAgenziaVo getAgenziaByCodice(Integer codFiegDl) {
		return bo.getAgenziaByCodice(codFiegDl);
	}

	@Override
	public <T extends BaseVo> List<T> saveVoList(List<T> list) {
		return bo.saveVoList(list);
	}

	@Override
	public Long getNextSeqVal(String seqUtentiEdicola) {
		return bo.getNextSeqVal(seqUtentiEdicola);
	}

	@Override
	public GruppoModuliVo getGruppoModuliByRole(String roleIgerivBaseAdmin) {
		return bo.getGruppoModuliByRole(roleIgerivBaseAdmin);
	}

	@Override
	public DlGruppoModuliVo getDlGruppoModuliVo(Integer id, Integer codDl) {
		return bo.getDlGruppoModuliVo(id, codDl);
	}

	@Override
	public Integer getLastId(Class<?> clazz, String pkName, String restriction, Object objRestriction) {
		return bo.getLastId(clazz, pkName, restriction, objRestriction);
	}

	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodEdicolaWeb(Integer codEdicola) {
		return bo.getAbbinamentoEdicolaDlVoByCodEdicolaWeb(codEdicola);
	}

	@Override
	public PeriodicitaTrascodificaInforeteVo getPeriodicitaTrascodificaInforete(Integer periodicita) {
		return bo.getPeriodicitaTrascodificaInforete(periodicita);
	}

	@Override
	public AnagraficaPubblicazioniVo getAnagraficaPubblicazioneByPk(Integer codFiegDl, Integer cpu) {
		return bo.getAnagraficaPubblicazioneByPk(codFiegDl, cpu);
	}

	@Override
	public <T extends BaseVo> List<T> getPeriodicita() {
		return bo.getPeriodicita();
	}
	
	@Override
	public ArgomentoVo getArgomentoVo(Integer codDl, Integer segmento) {
		return bo.getArgomentoVo(codDl, segmento);
	}

	@Override
	public AbbinamentoIdtnInforivVo getAbbinamentoIdtnInforiv(Integer codFiegDl, String idProdotto) {
		return bo.getAbbinamentoIdtnInforiv(codFiegDl, idProdotto);
	}

	@Override
	public StoricoCopertineVo getStoricoCopertinaByPk(Integer codFiegDl, Integer idtn) {
		return bo.getStoricoCopertinaByPk(codFiegDl, idtn);
	}

	@Override
	public <T extends BaseVo> void deleteVo(T vo) {
		bo.deleteVo(vo);
	}

	@Override
	public Integer getLastProgressivoEstrattoConto(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto) {
		return bo.getLastProgressivoEstrattoConto(codFiegDl, codEdicolaWeb, dataEstrattoConto);
	}

	@Override
	public EstrattoContoEdicolaDettaglioVo getEstrattoContoEdicolaDettaglioVo(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto, int progressivo) {
		return bo.getEstrattoContoEdicolaDettaglioVo(codFiegDl, codEdicolaWeb, dataEstrattoConto, progressivo);
	}

	@Override
	public EstrattoContoEdicolaVo getEstrattoContoEdicolaVo(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto) {
		return bo.getEstrattoContoEdicolaVo(codFiegDl, codEdicolaWeb, dataEstrattoConto);
	}

	@Override
	public AbbinamentoTipoMovimentoFondoBollaInforivVo getAbbinamentoTipoMovimentoFondoBollaInforiv(Integer tipoMovimento) {
		return bo.getAbbinamentoTipoMovimentoFondoBollaInforiv(tipoMovimento);
	}

	@Override
	public void deleteFondoBollaEdicolaInforiv(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dataBolla, String tipoBolla) {
		bo.deleteFondoBollaEdicolaInforiv(codFiegDl, codEdicolaWeb, dataBolla, tipoBolla);
	}

	@Override
	public Integer getLastPosizioneRigaBolla(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dataBolla, String tipoBolla) {
		return bo.getLastPosizioneRigaBolla(codFiegDl, codEdicolaWeb, dataBolla, tipoBolla);
	}

	@Override
	public BollaVo getBollaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer idtn) {
		return bo.getBollaVo(codFiegDl, dataBolla, tipoBolla, idtn);
	}

	@Override
	public BollaDettaglioVo getDettaglioBolla(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		return bo.getDettaglioBolla(codFiegDl, codEdicola, dataBolla, tipoBolla, idtn);
	}

	@Override
	public MessaggioVo getMessaggioRivenditaVo(Integer codFiegDl, Timestamp dtMessaggio, Integer tipoDestinatario, Integer destinatarioA, Integer destinatarioB) {
		return bo.getMessaggioRivenditaVo(codFiegDl, dtMessaggio, tipoDestinatario, destinatarioA, destinatarioB);
	}

	@Override
	public List<MessaggiBollaVo> getMessaggiBollaEdicola(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dtBolla, String tipo) {
		return bo.getMessaggiBollaEdicola(codFiegDl, codEdicolaWeb, dtBolla, tipo);
	}

	@Override
	public ResaRiscontrataVo getResaRiscontrataVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		return bo.getResaRiscontrataVo(codFiegDl, codEdicola, dataBolla, tipoBolla, idtn);
	}

	@Override
	public BollaResaVo getBollaResaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer idtn) {
		return bo.getBollaResaVo(codFiegDl, dataBolla, tipoBolla, idtn);
	}

	@Override
	public DecodificaRichiamiResaVo getRichiamoResa(Integer codFiegDl, Integer causaliResa) {
		return bo.getRichiamoResa(codFiegDl, causaliResa);
	}

	@Override
	public List<BollaResaRiassuntoVo> getBollaResaRiassunto(Integer[] codFiegDl, Integer[] codEdicolaWeb, Timestamp dtBolla, String tipoBolla) {
		return bo.getBollaResaRiassunto(codFiegDl, codEdicolaWeb, dtBolla, tipoBolla);
	}

	@Override
	public BollaResaDettaglioVo getBollaResaDettaglioVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		return bo.getBollaResaDettaglioVo(codFiegDl, codEdicola, dataBolla, tipoBolla, idtn);
	}

	@Override
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn, Integer codiceRichiesta) {
		return bo.getRichiestaRifornimento(codFiegDl, codEdicolaWeb, idtn, codiceRichiesta);
	}

	@Override
	public BollaStatisticaStoricoVo getBollaStatisticaStoricoVo(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn) {
		return bo.getBollaStatisticaStoricoVo(codFiegDl, codEdicolaWeb, idtn);
	}

	@Override
	public Integer getLastRigaBolla(Integer codFiegDl, Timestamp dataBolla, String tipoBolla) {
		return bo.getLastRigaBolla(codFiegDl, dataBolla, tipoBolla);
	}

	@Override
	public BollaRiassuntoVo getBollaRiassunto(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dtBolla, String tipoBolla) {
		return bo.getBollaRiassunto(codFiegDl, codEdicolaWeb, dtBolla, tipoBolla);
	}

	@Override
	public PeriodicitaVo getPeriodicita(Integer tipoOperazione, Integer codPeriodicita) {
		return bo.getPeriodicita(tipoOperazione, codPeriodicita);
	}
	
	@Override
	public List<RichiestaRifornimentoProdottiDto> getRichiesteRifornimentoDaEvadere(boolean soloRichiesteDl) {
		return bo.getRichiesteRifornimentoDaEvadere(soloRichiesteDl);
	}
	
	@Override
	public EmailRivenditaVo getEmailRivenditaVo(Integer idEmailRivendita) {
		return bo.getEmailRivenditaVo(idEmailRivendita);
	}
	
	@Override
	public InforivFtpFileVo getInforivFtpFile(String nomeFile) {
		return bo.getInforivFtpFile(nomeFile);
	}
	
	@Override
	public List<BollaResaRiassuntoVo> getBollaResaRiassuntoVo(Integer[] codEdicola, Integer[] codFiegDl, Timestamp dataBolla, String tipoBolla) {
		return bo.getBollaResaRiassunto(codFiegDl, codEdicola, dataBolla, tipoBolla);
	}
	
	@Override
	public List<BollaRiassuntoVo> getTestaBolleConsegna(Integer codEdicola, Integer codFiegDl, Integer giorni) {
		return bo.getTestaBolleConsegna(codFiegDl, codEdicola, giorni);
	}
	
	@Override
	public void saveMessaggioWithBlob(MessaggioVo vo, byte[] messaggio) {
		bo.saveMessaggioWithBlob(vo, messaggio);
	}

	@Override
	public void callStoredProcedure_ManutenzionePubblicazioni(Integer codFiegDl) {
		bo.callStoredProcedure_ManutenzionePubblicazioni(codFiegDl);
		
	}

	@Override
	public List<RichiestaFissaClienteEdicolaVo> getRichiesteFisseClienti(Integer codFiegDl, Integer codEdicola, Integer codPubblicazione, Integer idTestataNumero, Timestamp data) {
		return bo.getRichiesteFisseClienti(codFiegDl, codEdicola, codPubblicazione, idTestataNumero, data);
	}
}
