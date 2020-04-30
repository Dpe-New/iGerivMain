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

public interface IGerivBatchService {
	
	/**
	 * @param vo
	 */
	public BaseVo saveBaseVo(BaseVo vo);
	
	/**
	 * @param lrvo
	 */
	public BaseVo mergeBaseVo(BaseVo lrvo);
	
	/**
	 * Cancella dal file system le immagini i cui nomi non sono presenti nelle tabelle
	 * dei prodotti vari (categorie/sottocategorie/prodotti). 
	 */
	public void cleanupNonUsedImagesProdottiVari();
	
	/**
	 * Cancella dal file system le immagini dell pubblicazioni più vecchie di x giorni
	 */
	public void cleanupOldImagesPubblicazioni();

	/**
	 * Cancella le bolle di carico dei prodotti vari delle edicole precedenti a (oggi - x giorni) 
	 * dove x è il parametro <code>IGerivConstants.COD_PARAMETRO_EDICOLA_GIORNI_STORICO_BOLLE_PRODOTTI_VARI</code> 
	 * presente nella tabella parametri per per ogni edicola.
	 */
	public void deleteBolleProdottiVari();

	/**
	 * @param codEdicola
	 * @return
	 */
	public List<ProdottiNonEditorialiCategoriaEdicolaVo> getProdottiNonEditorialiCategorieEdicolaVo(Integer codEdicola);

	/**
	 * @param codCategoria
	 * @param codEdicola
	 * @return
	 */
	public List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> getProdottiNonEditorialiSottoCategorieEdicolaVo(Long codCategoria, Integer codEdicola);

	/**
	 * @param categoria
	 * @param sottocategoria
	 * @param codEdicola
	 * @return
	 */
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicola(Long categoria, Long sottocategoria, Integer codEdicola);

	/**
	 * @return
	 */
	public List<AbbinamentoEdicolaDlVo> getEdicoleNonSospese();
	
	/**
	 * @return
	 */
	public List<EdicolaDto> getEdicoleInforiv();

	/**
	 * @param codDpeWebEdicola
	 * @param codParametroEdicolaGiorniStoricoBolleProdottiVari
	 * @return
	 */
	public ParametriEdicolaVo getParametroEdicola(Integer codDpeWebEdicola, Integer codParametroEdicolaGiorniStoricoBolleProdottiVari);

	/**
	 * @param codDpeWebEdicola
	 * @param time
	 */
	public void deleteBolleProdottiVariEdicolaBeforeDate(Integer codDpeWebEdicola, Date time);

	/**
	 * @param codiceRivenditaDl
	 * @param codFiegDl
	 * @return
	 */
	public Integer getCodDpeWebEdicolaAbbinamentoEdicolaDlVo(Integer codiceRivenditaDl, Integer codFiegDl);

	/**
	 * @param codiceDl
	 * @param codiceRivendita
	 * @return
	 */
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita);

	/**
	 * @param zipFile
	 * @return
	 */
	public LavorazioneResaVo getLavorazioneResaVo(String zipFile);

	/**
	 * @param barcode
	 * @return
	 */
	public ImmaginePubblicazioneVo getImmaginePubblicazione(String barcode);

	/**
	 * @param titolo
	 * @return
	 */
	public List<AnagraficaPubblicazioniVo> getQuotidianoByTitolo(String titolo);

	/**
	 * @param titolo
	 * @return
	 */
	public List<AnagraficaPubblicazioniVo> getPeriodicoByTitolo(String titolo);

	/**
	 * @param codFiegDl
	 * @param codInizioQuotidiano
	 * @param codFineQuotidiano
	 * @return
	 */
	public List<AnagraficaPubblicazioniVo> getListAnagraficaPubblicazioneByCodQuotidiano(Integer codFiegDl, Integer codInizioQuotidiano, Integer codFineQuotidiano);

	/**
	 * @return
	 */
	public Timestamp getSysdate();

	/**
	 * @return
	 */
	public List<AnagraficaAgenziaVo> getAgenzieInforiv();

	/**
	 * @param codFiegDl
	 * @return
	 */
	public AnagraficaAgenziaVo getAgenziaByCodice(Integer codFiegDl);

	/**
	 * @param gruppiModuloDl
	 */
	public <T extends BaseVo> List<T> saveVoList(List<T> list);

	/**
	 * @param seqUtentiEdicola
	 * @return
	 */
	public Long getNextSeqVal(String seqUtentiEdicola);

	/**
	 * @param roleIgerivBaseAdmin
	 * @return
	 */
	public GruppoModuliVo getGruppoModuliByRole(String roleIgerivBaseAdmin);

	/**
	 * @param id
	 * @param codDl
	 * @return
	 */
	public DlGruppoModuliVo getDlGruppoModuliVo(Integer id, Integer codDl);

	/**
	 * @param class1
	 * @param string
	 * @param eq
	 * @return
	 */
	public Integer getLastId(Class<?> clazz, String pkName, String restriction, Object objRestriction);

	/**
	 * @param codEdicola
	 * @return
	 */
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodEdicolaWeb(Integer codEdicola);

	/**
	 * @param periodicita
	 * @return
	 */
	public PeriodicitaTrascodificaInforeteVo getPeriodicitaTrascodificaInforete(Integer periodicita);

	/**
	 * @param codFiegDl
	 * @param cpu
	 * @return
	 */
	public AnagraficaPubblicazioniVo getAnagraficaPubblicazioneByPk(Integer codFiegDl, Integer cpu);

	/**
	 * @param tipoOperazioneGesdis
	 * @param codPeriodicitaEnciclopedia
	 * @return
	 */
	public <T extends BaseVo> List<T> getPeriodicita();
	
	/**
	 * @param codDl
	 * @param segmento
	 * @return
	 */
	public ArgomentoVo getArgomentoVo(Integer codDl, Integer segmento);
	
	/**
	 * @param codFiegDl
	 * @param idProdotto
	 * @return
	 */
	public AbbinamentoIdtnInforivVo getAbbinamentoIdtnInforiv(Integer codFiegDl, String idProdotto);

	/**
	 * @param codFiegDl
	 * @param idtn
	 * @return
	 */
	public StoricoCopertineVo getStoricoCopertinaByPk(Integer codFiegDl, Integer idtn);

	/**
	 * @param bvo
	 */
	public <T extends BaseVo> void deleteVo(T vo);

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dataEstrattoConto
	 * @return
	 */
	public Integer getLastProgressivoEstrattoConto(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto);

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dataEstrattoConto
	 * @param progressivo
	 * @return
	 */
	public EstrattoContoEdicolaDettaglioVo getEstrattoContoEdicolaDettaglioVo(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto, int progressivo);

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dataEstrattoConto
	 * @return
	 */
	public EstrattoContoEdicolaVo getEstrattoContoEdicolaVo(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto);

	/**
	 * @param tipoMovimento
	 * @return
	 */
	public AbbinamentoTipoMovimentoFondoBollaInforivVo getAbbinamentoTipoMovimentoFondoBollaInforiv(Integer tipoMovimento);

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dataBolla
	 * @param tipoBolla
	 */
	public void deleteFondoBollaEdicolaInforiv(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dataBolla, String tipoBolla);

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dataBolla
	 * @param tipoBolla
	 * @return
	 */
	public Integer getLastPosizioneRigaBolla(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dataBolla, String tipoBolla);

	/**
	 * @param codFiegDl
	 * @param dataBolla
	 * @param tipoBolla
	 * @param idtn
	 * @return
	 */
	public BollaVo getBollaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer idtn);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataBolla
	 * @param tipoBolla
	 * @param idtn
	 * @return
	 */
	public BollaDettaglioVo getDettaglioBolla(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn);

	/**
	 * @param codFiegDl
	 * @param dtMessaggio
	 * @param codEdicolaSingola
	 * @param codEdicolaWeb
	 * @param i
	 * @return
	 */
	public MessaggioVo getMessaggioRivenditaVo(Integer codFiegDl, Timestamp dtMessaggio, Integer tipoDestinatario, Integer destinatarioA, Integer destinatarioB);

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dtBolla
	 * @param tipo
	 * @return
	 */
	public List<MessaggiBollaVo> getMessaggiBollaEdicola(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dtBolla, String tipo);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataBolla
	 * @param tipoBolla
	 * @param idtn
	 * @return
	 */
	public ResaRiscontrataVo getResaRiscontrataVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn);

	/**
	 * @param codFiegDl
	 * @param dataBolla
	 * @param tipoBolla
	 * @param idtn
	 * @return
	 */
	public BollaResaVo getBollaResaVo(Integer codFiegDl, Date dataBolla, String tipoBolla, Integer idtn);

	/**
	 * @param codFiegDl
	 * @param causaliResa
	 * @return
	 */
	public DecodificaRichiamiResaVo getRichiamoResa(Integer codFiegDl, Integer causaliResa);

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dtBolla
	 * @param tipoBolla
	 * @return
	 */
	public List<BollaResaRiassuntoVo> getBollaResaRiassunto(Integer[] codFiegDl, Integer[] codEdicolaWeb, Timestamp dtBolla, String tipoBolla);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dataBolla
	 * @param tipoBolla
	 * @param idtn
	 * @return
	 */
	public BollaResaDettaglioVo getBollaResaDettaglioVo(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla, Integer idtn);

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param idtn
	 * @param codiceRichiesta
	 * @return
	 */
	public RichiestaRifornimentoVo getRichiestaRifornimento(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn, Integer codiceRichiesta);

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param idtn
	 * @return
	 */
	public BollaStatisticaStoricoVo getBollaStatisticaStoricoVo(Integer codFiegDl, Integer codEdicolaWeb, Integer idtn);

	/**
	 * @param codFiegDl
	 * @param dataBolla
	 * @param tipoBolla
	 * @return
	 */
	public Integer getLastRigaBolla(Integer codFiegDl, Timestamp dataBolla, String tipoBolla);

	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dtBolla
	 * @param tipoBolla
	 * @return
	 */
	public BollaRiassuntoVo getBollaRiassunto(Integer codFiegDl, Integer codEdicolaWeb, Timestamp dtBolla, String tipoBolla);

	/**
	 * @param tipoOperazioneGesdis
	 * @param codPeriodicitaEnciclopedia
	 * @return
	 */
	public PeriodicitaVo getPeriodicita(Integer tipoOperazione, Integer codPeriodicita);
	
	/**
	 * @return
	 */
	public List<RichiestaRifornimentoProdottiDto> getRichiesteRifornimentoDaEvadere(boolean soloRichiesteDl);
	
	/**
	 * @param idEmailRivendita
	 * @return
	 */
	public EmailRivenditaVo getEmailRivenditaVo(Integer idEmailRivendita);
	
	/**
	 * @param nomeFile
	 * @return
	 */
	public InforivFtpFileVo getInforivFtpFile(String nomeFile);
	
	/**
	 * @param giorni
	 * @return
	 */
	public List<BollaRiassuntoVo> getTestaBolleConsegna(Integer codEdicola, Integer codFiegDl, Integer giorni);
	
	/**
	 * @param dataBolla
	 * @param tipoBolla
	 * @return
	 */
	public List<BollaResaRiassuntoVo> getBollaResaRiassuntoVo(Integer[] codEdicola, Integer[] codFiegDl, Timestamp dataBolla, String tipoBolla);
	
	/**
	 * 
	 */
	public void saveMessaggioWithBlob(MessaggioVo vo, byte[] messaggio);
	
	/**
	 * 
	 * @param codFiegDl
	 */
	public void callStoredProcedure_ManutenzionePubblicazioni(Integer codFiegDl);
	
	
	/**
	 * 
	 * @param codFiegDl
	 * @param codEdicola
	 * @param codPubblicazione
	 * @param idTestataNumero
	 * @param data
	 */
	public List<RichiestaFissaClienteEdicolaVo> getRichiesteFisseClienti(Integer codFiegDl, Integer codEdicola, Integer codPubblicazione, Integer idTestataNumero, Timestamp data);

}
