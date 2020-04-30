package it.dpe.igeriv.bo.clienti;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.DateEstrattoContoClienteDto;
import it.dpe.igeriv.dto.EstrattoContoClienti;
import it.dpe.igeriv.dto.EstrattoContoFatturaClientiDto;
import it.dpe.igeriv.dto.FileFatturaDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.vo.AnagraficaBancaVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.EmailTemplateVo;
import it.dpe.igeriv.vo.FatturaClienteEdicolaVo;
import it.dpe.igeriv.vo.MetodoPagamentoClienteVo;
import it.dpe.igeriv.vo.PagamentoClientiEdicolaVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

interface ClientiRepository extends BaseRepository {
	
	/**
	 * @param <T>
	 * @param codCliente
	 * @return
	 */
	public ClienteEdicolaVo getCienteEdicolaByCodice(Long codCliente);
	
	/**
	 * @param <T>
	 * @param codCliente
	 * @return
	 */
	public ClienteEdicolaVo getCienteEdicolaByEmail(String email);
	
	/**
	 * @param <T>
	 * @param codCliente
	 * @return
	 */
	public ClienteEdicolaVo getCienteEdicolaByCodiceLogin(Long codCliente);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param piva 
	 * @param codEdicola2 
	 * @param cognome 
	 * @param nome 
	 * @return
	 */
	public List<ClienteEdicolaDto> getClientiEdicola(Integer[] codEdicola, String nome, String cognome, String codiceFiscale, String piva);
	
	/**
	 * 
	 * @param codEdicola
	 * @param nome
	 * @param cognome
	 * @param codCliente
	 * @return
	 */
	public List<ClienteEdicolaDto> getClientiEdicola(Integer[] codEdicola, String nome, String cognome, Long codCliente);
	
	
	/**
	 * @param codEdicola
	 * @param listCodClienti
	 * @return
	 */
	public List<ClienteEdicolaVo> getClientiEdicola(Integer[] codEdicola, List<Long> listCodClienti);
	
	/**
	 * @param <T>
	 * @param codEdicola
	 * @param titolo
	 * @param sottotitolo
	 * @param numero
	 * @param argomento
	 * @param periodicita
	 * @param prezzo
	 * @param codPubblicazione
	 * @param codBarre
	 * @return
	 */
	public Set<ClienteEdicolaDto> getClientiEdicolaByPubblicazione(Integer[] codEdicola, String titolo, String sottotitolo, String numero, String argomento, String periodicita, BigDecimal prezzo, Integer codPubblicazione, String codBarre, boolean conPrenotazioniFisse);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param piva 
	 * @param codEdicola2 
	 * @param cognome 
	 * @param nome 
	 * @return 
	 */
	public List<ClienteEdicolaDto> getClientiConEvasione(Integer[] codEdicola, String nome, String cognome, String codiceFiscale, String piva, boolean conPrenotazioniFisse);
	
	/**
	 * @param codEdicola
	 * @param idCliente
	 * @return
	 */
	public ClienteEdicolaVo getClienteEdicola(Integer[] codEdicola, Long idCliente);
	
	/**
	 * @param codEdicola
	 * @param tipiEstrattoConto
	 * @param dataCompetenza
	 * @param piva 
	 * @param codiceFiscale 
	 * @param cognome 
	 * @param nome 
	 * @param tipoProdottiInEstrattoConto 
	 * @return
	 */
	public List<ClienteEdicolaDto> getClientiEdicolaConEstrattoConto(Integer[] codEdicola, List<Integer> tipiEstrattoConto, Timestamp dataCompetenza, Boolean estrattoContoChiuso, String nome, String cognome, String codiceFiscale, String piva, Integer tipoProdottiInEstrattoConto, List<Long> codClienti);

	/**
	 * @param arrCodEdicola
	 * @param codClienti
	 * @param dataCompetenza
	 * @param tipiEstrattoConto
	 * @return
	 */
	public List<EstrattoContoClienti> getListEstrattoContoClienti(Integer[] arrCodEdicola, List<Long> codClienti, Timestamp dataCompetenza, List<Integer> tipiEstrattoConto, Integer tipoProdotti);

	/**
	 * @param dataDocumento
	 * @param listCodVendita
	 * @param tipoProdotti 
	 */
	public void updateDataEstrattoConto(Timestamp dataDocumento, Integer tipoProdotti, Map<Long, Set<Timestamp>> mapClienteDateCompetezaEc);
	
	/**
	 * @param mapClienteDateCompetezaEc
	 * @param mapClienteNumeroFattura 
	 */
	public void updateFatturaEmessa(Map<Long, Set<Timestamp>> mapClienteDateCompetezaEc, Map<Long, Integer> mapClienteNumeroFattura);

	/**
	 * @param codCliente
	 * @return
	 */
	public List<DateEstrattoContoClienteDto> getDateReportEstrattiContoCliente(Long codCliente);

	/**
	 * @param arrCodEdicola
	 * @param codCliente
	 * @param dataA
	 * @param tipoProdottiInEstrattoConto 
	 * @param numeroFattura 
	 * @return List<? extends EstrattoContoClienti>
	 */
	public List<EstrattoContoClienti> getEstrattoContoClienti(Integer[] arrCodEdicola, Long codCliente, Timestamp dataA, Integer tipoProdottiInEstrattoConto, Integer numeroFattura);

	/**
	 * @param arrCodEdicola
	 * @param nome
	 * @param cognome
	 * @param codiceFiscale
	 * @param piva
	 * @param dataCompetenzaA 
	 * @param dataCompetenzaDa 
	 * @param daPagare 
	 * @return
	 */
	public List<ClienteEdicolaDto> getClientiEdicolaConPagamentiPendenti(Integer[] arrCodEdicola, String nome, String cognome, String codiceFiscale, String piva, Boolean daPagare, Timestamp dataCompetenzaDa, Timestamp dataCompetenzaA);

	/**
	 * @return
	 */
	public List<MetodoPagamentoClienteVo> getMetodiPagamentoCliente();

	/**
	 * @param dataCompetenza
	 * @param dataDocumento 
	 * @param tipoProdotti 
	 * @param mapClienteEstrattContoXml 
	 * @param mapClienteNumeroEC 
	 * @param mapCodVenditaClienti
	 */
	public void insertPagamentoClienti(Timestamp dataCompetenza, Timestamp dataDocumento, Map<Long, BigDecimal> mapClienteImporto, Integer tipoProdotti, Map<Long, byte[]> mapClienteEstrattContoXml, Map<Long, Integer> mapClienteNumeroEC, Integer tipoDocumento);
	
	/**
	 * @param listCodCliente
	 * @param listDataCompetenzaEstrattoConto
	 * @param pagato
	 */
	public void updatePagamentoClienti(List<Long> listCodCliente, List<Timestamp> listDataCompetenzaEstrattoConto, Boolean pagato);

	/**
	 * @param codEdicola
	 * @param dataDa
	 * @param dataA
	 * @param piva 
	 * @param codiceFiscale 
	 * @param cognome 
	 * @param nome 
	 * @return
	 */
	public List<FileFatturaDto> getFattureClienti(Integer codEdicola, Timestamp dataDa, Timestamp dataA, String nome, String cognome, String codiceFiscale, String piva);
		

	/**
	 * @param codCliente
	 */
	public void deletePagamentiCliente(Long codCliente);
	
	/**
	 * @param codCliente
	 */
	public void deleteCliente(Long codCliente);

	/**
	 * @return
	 */
	public List<AnagraficaBancaVo> getBanche();
	
	/**
	 * @param codEdicola
	 * @param codClienti
	 * @param dataCompetenza
	 * @param tipiEstrattoConto
	 * @return
	 */
	public Timestamp getUltimaDataCompetenza(Integer[] codEdicola, List<Long> codClienti, Timestamp dataCompetenza, List<Integer> tipiEstrattoConto);

	/**
	 * @param listCodClienti
	 * @param dtCompetenza
	 * @param tipoDocumento
	 * @return
	 */
	public List<PagamentoClientiEdicolaVo> getPagamentoClientiEdicola(List<Long> listCodClienti, Timestamp dtCompetenza, Integer tipoDocumento);

	/**
	 * @param arrId
	 * @param titolo
	 * @param dataDa
	 * @param dataA
	 * @param codCliente
	 * @return
	 */
	public List<PubblicazioneDto> getRitiriCliente(Integer[] arrId, String titolo, Timestamp dataDa, Timestamp dataA, Long codCliente);
	
	/**
	 * @param arrId
	 * @param codCliente
	 * @return
	 */
	public List<PubblicazioneDto> getRitiriClienteCancellati(Integer[] arrId, Long codCliente);
	
	/**
	 * @param codCliente
	 * @param dtCompetenza 
	 * @param tipoDocumento
	 */
	public void deleteEstrattoContoCliente(Long codCliente, Timestamp dtCompetenza, Integer tipoDocumento);
	
	/**
	 * @param codCliente
	 * @param codEdicola
	 * @param numeroFattura
	 * @return
	 */
	public FatturaClienteEdicolaVo getFatturaClienteEdicola(Long codCliente, Integer codEdicola, Integer numeroFattura);

	/**
	 * @param codEdicolaMaster
	 * @param codUtente
	 * @return
	 */
	public Integer getLastFatturaUtenteEdicola(Integer codEdicolaMaster, String codUtente);
	
	/**
	 * @param codCliente
	 * @param dataDa
	 * @param dataA
	 * @param tipoDocumento 
	 * @return
	 */
	public List<EstrattoContoFatturaClientiDto> getListEstrattiContoFattureClienti(Long codCliente, Timestamp dataDa, Timestamp dataA, Integer tipoDocumento);
	
	/**
	 * @param codCliente
	 * @param dataA
	 * @param dataA 
	 * @return
	 */
	public void chiudiMovimentiEstrattoContoClientiVo(Long codCliente, Timestamp dataUpdate, Timestamp dataA);

	/**
	 * @param codEdicola
	 * @param codCliente
	 * @param dataA
	 */
	public void chiudiMovimentiVenditeEstrattoContoClientiVo(Integer codEdicola, Long codCliente, Timestamp dataUpdate, Timestamp dataA);
	
	/**
	 * @param codEdicola
	 * @param codFiegDl
	 * @param codCliente
	 */
	public void deleteRichiesteCliente(final Integer[] codEdicola, final Integer[] codFiegDl, final Long codCliente);
	
	/**
	 * @param codCliente
	 */
	public void resetCodClienteVendite(Long codCliente);
	
	/**
	 * @param codCliente
	 */
	public void deleteMessaggiCliente(Long codCliente);

	/**
	 * @param codEdicola
	 * @return
	 */
	public List<EmailTemplateVo> getEmailTemplates(Integer codEdicola);

	/**
	 * @param codTemplate
	 * @return
	 */
	public EmailTemplateVo getEmailTemplate(Integer codTemplate);
	
	/**
	 * 
	 * @param codEdicola
	 * @param nome
	 * @param cognome
	 * @param codCliente
	 * @param numeroOrdine
	 * @return
	 */
	public List<ClienteEdicolaDto> getClientiEdicolaScolastica(Integer[] codEdicola, String nome, String cognome, Long codCliente,String numOrdineTxt);

}
