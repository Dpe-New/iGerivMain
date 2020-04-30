package it.dpe.igeriv.bo;

import it.dpe.igeriv.bo.batch.IGerivBatchBo;
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
import it.dpe.igeriv.vo.LocalitaVo;
import it.dpe.igeriv.vo.MessaggiBollaVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.ParametriEdicolaVo;
import it.dpe.igeriv.vo.PeriodicitaTrascodificaInforeteVo;
import it.dpe.igeriv.vo.PeriodicitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiGiacenzeVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.ResaRiscontrataVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockIgerivBatchBoImpl implements IGerivBatchBo {
	private static final long serialVersionUID = 1L;

	@Override
	public BaseVo saveBaseVo(BaseVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseVo mergeBaseVo(BaseVo lrvo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanupNonUsedImagesProdottiVari() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanupOldImagesPubblicazioni() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteBolleProdottiVari() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ProdottiNonEditorialiCategoriaEdicolaVo> getProdottiNonEditorialiCategorieEdicolaVo(Integer codEdicola) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> getProdottiNonEditorialiSottoCategorieEdicolaVo(Long codCategoria, Integer codEdicola) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicola(Long categoria, Long sottocategoria, Integer codEdicola) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AbbinamentoEdicolaDlVo> getEdicoleNonSospese() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParametriEdicolaVo getParametroEdicola(Integer codDpeWebEdicola, Integer codParametroEdicolaGiorniStoricoBolleProdottiVari) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteBolleProdottiVariEdicolaBeforeDate(Integer codDpeWebEdicola, Date time) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer getCodDpeWebEdicolaAbbinamentoEdicolaDlVo(Integer codiceRivenditaDl, Integer codFiegDl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LavorazioneResaVo getLavorazioneResaVo(String zipFile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmaginePubblicazioneVo getImmaginePubblicazione(String barcode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getQuotidianoByTitolo(String titolo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getPeriodicoByTitolo(String titolo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AnagraficaPubblicazioniVo> getListAnagraficaPubblicazioneByCodQuotidiano(Integer codFiegDl, Integer codInizioQuotidiano, Integer codFineQuotidiano) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getSysdate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AnagraficaAgenziaVo> getAgenzieInforiv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnagraficaAgenziaVo getAgenziaByCodice(Integer codFiegDl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BaseVo> List<T> saveVoList(List<T> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getNextSeqVal(String seqUtentiEdicola) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GruppoModuliVo getGruppoModuliByRole(String roleIgerivBaseAdmin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DlGruppoModuliVo getDlGruppoModuliVo(Integer id, Integer codDl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getLastId(Class<?> clazz, String pkName, String restriction, Object objRestriction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodEdicolaWeb(Integer codEdicola) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PeriodicitaTrascodificaInforeteVo getPeriodicitaTrascodificaInforete(Integer periodicita) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnagraficaPubblicazioniVo getAnagraficaPubblicazioneByPk(Integer codFiegDl, Integer cpu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BaseVo> List<T> getPeriodicita() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbbinamentoIdtnInforivVo getAbbinamentoIdtnInforiv(Integer codFiegDl, String idProdotto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoricoCopertineVo getStoricoCopertinaByPk(Integer codFiegDl, Integer idtn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BaseVo> void deleteVo(T vo) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer getLastProgressivoEstrattoConto(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EstrattoContoEdicolaDettaglioVo getEstrattoContoEdicolaDettaglioVo(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto, int progressivo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EstrattoContoEdicolaVo getEstrattoContoEdicolaVo(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbbinamentoTipoMovimentoFondoBollaInforivVo getAbbinamentoTipoMovimentoFondoBollaInforiv(Integer tipoMovimento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFondoBollaEdicolaInforiv(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dataBolla, String tipoBolla) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer getLastPosizioneRigaBolla(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dataBolla, String tipoBolla) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BollaVo getBollaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer idtn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BollaDettaglioVo getDettaglioBolla(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessaggioVo getMessaggioRivenditaVo(Integer codFiegDl, Timestamp dtMessaggio, Integer tipoDestinatario, Integer destinatarioA, Integer destinatarioB) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessaggiBollaVo> getMessaggiBollaEdicola(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dtBolla, String tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResaRiscontrataVo getResaRiscontrataVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BollaResaVo getBollaResaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer idtn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecodificaRichiamiResaVo getRichiamoResa(Integer codFiegDl, Integer causaliResa) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BollaResaDettaglioVo getBollaResaDettaglioVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn, Integer codiceRichiesta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BollaStatisticaStoricoVo getBollaStatisticaStoricoVo(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getLastRigaBolla(Integer codFiegDl, Timestamp dataBolla, String tipoBolla) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BollaRiassuntoVo getBollaRiassunto(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dtBolla, String tipoBolla) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PeriodicitaVo getPeriodicita(Integer tipoOperazione, Integer codPeriodicita) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RichiestaRifornimentoProdottiDto> getRichiesteRifornimentoDaEvadere(boolean soloRichiesteDl) {
		List<RichiestaRifornimentoProdottiDto> list = new ArrayList<RichiestaRifornimentoProdottiDto>();
		RichiestaRifornimentoProdottiDto dto = new RichiestaRifornimentoProdottiDto();
		dto.setCodEdicolaDl(123);
		dto.setCodFornitore(74);
		dto.setCodiceProdottoFornitore("345");
		dto.setCodProdottoInterno(1l);
		dto.setCodRichiestaRifornimento(1l);
		dto.setDataRichiesta(new Timestamp(new Date().getTime()));
		dto.setQuatitaRichiesta(1);
		dto.setCodiceContabileCliente(1l);
		dto.setCodProdottoInterno(1l);
		list.add(dto);
		
		RichiestaRifornimentoProdottiDto dto1 = new RichiestaRifornimentoProdottiDto();
		dto1.setCodEdicolaDl(124);
		dto1.setCodFornitore(74);
		dto1.setCodiceProdottoFornitore("347");
		dto1.setCodProdottoInterno(2l);
		dto1.setCodRichiestaRifornimento(2l);
		dto1.setDataRichiesta(new Timestamp(new Date().getTime()));
		dto1.setQuatitaRichiesta(2);
		dto1.setCodiceContabileCliente(1l);
		dto1.setCodProdottoInterno(2l);
		list.add(dto1);
		
		RichiestaRifornimentoProdottiDto dto2 = new RichiestaRifornimentoProdottiDto();
		dto2.setCodEdicolaDl(125);
		dto2.setCodFornitore(74);
		dto2.setCodiceProdottoFornitore("349");
		dto2.setCodProdottoInterno(3l);
		dto2.setCodRichiestaRifornimento(4l);
		dto2.setDataRichiesta(new Timestamp(new Date().getTime()));
		dto2.setQuatitaRichiesta(2);
		dto2.setCodiceContabileCliente(1l);
		dto2.setCodProdottoInterno(3l);
		list.add(dto2);
		
		
		RichiestaRifornimentoProdottiDto dto3 = new RichiestaRifornimentoProdottiDto();
		dto3.setCodEdicolaDl(123);
		dto3.setCodFornitore(45);
		dto3.setCodiceProdottoFornitore("111");
		dto3.setCodProdottoInterno(1l);
		dto3.setCodRichiestaRifornimento(1l);
		dto3.setDataRichiesta(new Timestamp(new Date().getTime()));
		dto3.setQuatitaRichiesta(1);
		dto3.setCodiceContabileCliente(1l);
		dto3.setCodProdottoInterno(1l);
		list.add(dto3);
		
		RichiestaRifornimentoProdottiDto dto4 = new RichiestaRifornimentoProdottiDto();
		dto4.setCodEdicolaDl(124);
		dto4.setCodFornitore(45);
		dto4.setCodiceProdottoFornitore("222");
		dto4.setCodProdottoInterno(2l);
		dto4.setCodRichiestaRifornimento(2l);
		dto4.setDataRichiesta(new Timestamp(new Date().getTime()));
		dto4.setQuatitaRichiesta(2);
		dto4.setCodiceContabileCliente(1l);
		dto4.setCodProdottoInterno(2l);
		list.add(dto4);
		
		RichiestaRifornimentoProdottiDto dto5 = new RichiestaRifornimentoProdottiDto();
		dto5.setCodEdicolaDl(125);
		dto5.setCodFornitore(45);
		dto5.setCodiceProdottoFornitore("333");
		dto5.setCodProdottoInterno(3l);
		dto5.setCodRichiestaRifornimento(4l);
		dto5.setDataRichiesta(new Timestamp(new Date().getTime()));
		dto5.setQuatitaRichiesta(2);
		dto5.setCodiceContabileCliente(1l);
		dto5.setCodProdottoInterno(3l);
		list.add(dto5);
		return list;
	}

	@Override
	public void updateEsportazioneRichiestaRifornimentoTemp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateEsportazioneRichiestaRifornimentoSuccess(String msgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateEsportazioneRichiestaRifornimentoError(String msgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRichiestaRifornimentoEvasa(Long codRichiestaRifornimento, Long codProdottoInterno, String stato, Integer quatitaEvasa, String note) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<EdicolaDto> getEdicoleAttive(Integer coddl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalitaVo getLocalita(String localita) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProvinciaVo getProvincia(String provincia) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProdottiNonEditorialiPrezziAcquistoVo getProdottiNonEditorialiPrezziAcquistoVo(Integer codEdicola, Integer codFornitore, String codiceProdottoFornitore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProdottiNonEditorialiGiacenzeVo getGiacenza(Integer codFornitore, String codProdottoFornitore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EmailRivenditaVo getEmailRivenditaVo(Integer idEmailRivendita) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EdicolaDto> getEdicoleInforiv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InforivFtpFileVo getInforivFtpFile(String nomeFile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArgomentoVo getArgomentoVo(Integer codDl, Integer segmento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BollaRiassuntoVo> getTestaBolleConsegna(Integer codFiegDl, Integer codEdicola, Integer giorni) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BollaResaRiassuntoVo> getBollaResaRiassunto(Integer[] codFiegDl, Integer[] codEdicolaWeb, Timestamp dtBolla, String tipoBolla) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseVo createBaseVo(BaseVo vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RichiestaRifornimentoProdottiDto> getReseProdottiVariDaInviare() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateEsportazioneResaTemp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateEsportazioneResaSuccess(String correlationId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateEsportazioneResaError(String correlationId) {
		// TODO Auto-generated method stub
		
	}

}
