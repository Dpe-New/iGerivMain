package it.dpe.igeriv.bo.clienti;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.contabilita.ContabilitaService;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.DateEstrattoContoClienteDto;
import it.dpe.igeriv.dto.EstrattoContoClienti;
import it.dpe.igeriv.dto.EstrattoContoFatturaClientiDto;
import it.dpe.igeriv.dto.FileFatturaDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AnagraficaBancaVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.EmailTemplateVo;
import it.dpe.igeriv.vo.FatturaClienteEdicolaVo;
import it.dpe.igeriv.vo.MetodoPagamentoClienteVo;
import it.dpe.igeriv.vo.MovimentiEstrattoContoClientiVo;
import it.dpe.igeriv.vo.PagamentoClientiEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiProgressiviFatturazioneVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ClientiService")
class ClientiServiceImpl extends BaseServiceImpl implements ClientiService<ClienteEdicolaVo> {
	private final ClientiRepository repository;
	private final ContabilitaService contabilitaService;
	
	@Autowired
	ClientiServiceImpl(ClientiRepository repository, ContabilitaService contabilitaService) {
		super(repository);
		this.repository = repository;
		this.contabilitaService = contabilitaService;
	}

	@Override
	public ClienteEdicolaVo getCienteEdicolaByCodice(Long codCliente) {
		return repository.getCienteEdicolaByCodice(codCliente);
	}

	@Override
	public ClienteEdicolaVo getCienteEdicolaByEmail(String email) {
		return repository.getCienteEdicolaByEmail(email);
	}

	@Override
	public ClienteEdicolaVo getCienteEdicolaByCodiceLogin(Long codCliente) {
		return repository.getCienteEdicolaByCodiceLogin(codCliente);
	}

	@Override
	public List<ClienteEdicolaDto> getClientiEdicola(Integer[] codEdicola, String nome, String cognome, String codiceFiscale, String piva) {
		return repository.getClientiEdicola(codEdicola, nome, cognome, codiceFiscale, piva);
	}

	@Override
	public List<ClienteEdicolaVo> getClientiEdicola(Integer[] codEdicola, List<Long> listCodClienti) {
		return repository.getClientiEdicola(codEdicola, listCodClienti);
	}

	@Override
	public Set<ClienteEdicolaDto> getClientiEdicolaByPubblicazione(Integer[] codEdicola, String titolo, String sottotitolo, String numero, String argomento, String periodicita, BigDecimal prezzo, Integer codPubblicazione, String codBarre,
			boolean conPrenotazioniFisse) {
		return repository.getClientiEdicolaByPubblicazione(codEdicola, titolo, sottotitolo, numero, argomento, periodicita, prezzo, codPubblicazione, codBarre, conPrenotazioniFisse);
	}

	@Override
	public List<ClienteEdicolaDto> getClientiConEvasione(Integer[] codEdicola, String nome, String cognome, String codiceFiscale, String piva, boolean conPrenotazioniFisse) {
		return repository.getClientiConEvasione(codEdicola, nome, cognome, codiceFiscale, piva, conPrenotazioniFisse);
	}

	@Override
	public ClienteEdicolaVo getClienteEdicola(Integer[] codEdicola, Long idCliente) {
		return repository.getClienteEdicola(codEdicola, idCliente);
	}

	@Override
	public List<ClienteEdicolaDto> getClientiEdicolaConEstrattoConto(Integer[] codEdicola, List<Integer> tipiEstrattoConto, Timestamp dataCompetenza, Boolean estrattoContoChiuso, String nome, String cognome, String codiceFiscale, String piva,
			Integer tipoProdottiInEstrattoConto, List<Long> codClienti) {
		return repository.getClientiEdicolaConEstrattoConto(codEdicola, tipiEstrattoConto, dataCompetenza, estrattoContoChiuso, nome, cognome, codiceFiscale, piva, tipoProdottiInEstrattoConto, codClienti);
	}

	@Override
	public List<EstrattoContoClienti> getListEstrattoContoClienti(Integer[] arrCodEdicola, List<Long> codClienti, Timestamp dataCompetenza, List<Integer> tipiEstrattoConto, Integer tipoProdotti) {
		return repository.getListEstrattoContoClienti(arrCodEdicola, codClienti, dataCompetenza, tipiEstrattoConto, tipoProdotti);
	}

	@Override
	public void updateDataEstrattoConto(Timestamp dataDocumento, Integer tipoProdotti, Map<Long, Set<Timestamp>> mapClienteDateCompetezaEc) {
		repository.updateDataEstrattoConto(dataDocumento, tipoProdotti, mapClienteDateCompetezaEc);
	}

	@Override
	public void updateFatturaEmessa(Map<Long, Set<Timestamp>> mapClienteDateCompetezaEc, Map<Long, Integer> mapClienteNumeroFattura) {
		repository.updateFatturaEmessa(mapClienteDateCompetezaEc, mapClienteNumeroFattura);
	}

	@Override
	public List<DateEstrattoContoClienteDto> getDateReportEstrattiContoCliente(Long codCliente) {
		return repository.getDateReportEstrattiContoCliente(codCliente);
	}

	@Override
	public List<EstrattoContoClienti> getEstrattoContoClienti(Integer[] arrCodEdicola, Long codCliente, Timestamp dataA, Integer tipoProdottiInEstrattoConto, Integer numeroFattura) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClienteEdicolaDto> getClientiEdicolaConPagamentiPendenti(Integer[] arrCodEdicola, String nome, String cognome, String codiceFiscale, String piva, Boolean daPagare, Timestamp dataCompetenzaDa, Timestamp dataCompetenzaA) {
		return repository.getClientiEdicolaConPagamentiPendenti(arrCodEdicola, nome, cognome, codiceFiscale, piva, daPagare, dataCompetenzaDa, dataCompetenzaA);
	}

	@Override
	public List<MetodoPagamentoClienteVo> getMetodiPagamentoCliente() {
		return repository.getMetodiPagamentoCliente();
	}

	@Override
	public void insertPagamentoClienti(Timestamp dataCompetenza, Timestamp dataDocumento, Map<Long, BigDecimal> mapClienteImporto, Integer tipoProdotti, Map<Long, byte[]> mapClienteEstrattContoXml, Map<Long, Integer> mapClienteNumeroEC,
			Integer tipoDocumento) {
		repository.insertPagamentoClienti(dataCompetenza, dataDocumento, mapClienteImporto, tipoProdotti, mapClienteEstrattContoXml, mapClienteNumeroEC, tipoDocumento);
	}

	@Override
	public void updatePagamentoClienti(List<Long> listCodCliente, List<Timestamp> listDataCompetenzaEstrattoConto, Boolean pagato) {
		repository.updatePagamentoClienti(listCodCliente, listDataCompetenzaEstrattoConto, pagato);
	}
	
	@Override
	public List<FileFatturaDto> getFattureClienti(Integer codEdicola, Timestamp dataDa, Timestamp dataA, String nome, String cognome, String codiceFiscale, String piva) {
		return repository.getFattureClienti(codEdicola, dataDa, dataA, nome, cognome, codiceFiscale, piva);
	}
	
	@Override
	public void deleteEstrattoContoCliente(Long codCliente, Timestamp dtCompetenza, Integer tipoDocumento, Integer codEdicola, Long numeroEC) {
		if (numeroEC != null) {
			ProdottiNonEditorialiProgressiviFatturazioneVo prog = contabilitaService.getNextProgressivoFatturazioneVo(codEdicola, IGerivConstants.TIPO_DOCUMENTO_ESTRATTO_CONTO, DateUtilities.getDateFirstDayYear());
			if (prog != null && prog.getPk().getData().getTime() == DateUtilities.floorDay(DateUtilities.getDateFirstDayYear()).getTime() && numeroEC.equals(prog.getProgressivo().intValue())) {
				contabilitaService.setNextProgressivoFatturazione(codEdicola, IGerivConstants.TIPO_DOCUMENTO_ESTRATTO_CONTO, DateUtilities.getDateFirstDayYear(), numeroEC - 1);
			}
		}
		repository.deleteEstrattoContoCliente(codCliente, dtCompetenza, tipoDocumento);
	}
	
	@Override
	public void deletePagamentiCliente(Long codCliente) {
		repository.deletePagamentiCliente(codCliente);
	}
	
	@Override
	public void deleteCliente(Long codCliente) {
		repository.deleteCliente(codCliente);
	}
	
	@Override
	public List<AnagraficaBancaVo> getBanche() {
		return repository.getBanche();
	}
	
	@Override
	public List<PagamentoClientiEdicolaVo> getPagamentoClientiEdicola(List<Long> listCodClienti, Timestamp dtCompetenza, Integer tipoDocumento) {
		return repository.getPagamentoClientiEdicola(listCodClienti, dtCompetenza, tipoDocumento);
	}
	
	@Override
	public List<PubblicazioneDto> getRitiriCliente(Integer[] arrId, String titolo, Timestamp dataDa, Timestamp dataA, Long codCliente) {
		return repository.getRitiriCliente(arrId, titolo, dataDa, dataA, codCliente);
	}
	
	@Override
	public List<PubblicazioneDto> getRitiriClienteCancellati(Integer[] arrId, Long codCliente) {
		return repository.getRitiriClienteCancellati(arrId, codCliente);
	}
	
	@Override
	public Timestamp getUltimaDataCompetenza(Integer[] codEdicola, List<Long> codClienti, Timestamp dataCompetenza, List<Integer> tipiEstrattoConto) {
		return repository.getUltimaDataCompetenza(codEdicola, codClienti, dataCompetenza, tipiEstrattoConto);
	}
	
	@Override
	public FatturaClienteEdicolaVo getFatturaClienteEdicola(Long codCliente, Integer codEdicola, Integer numeroFattura) {
		return repository.getFatturaClienteEdicola(codCliente, codEdicola, numeroFattura);
	}
	
	@Override
	public Integer getLastFatturaUtenteEdicola(Integer codEdicolaMaster, String codUtente) {
		return repository.getLastFatturaUtenteEdicola(codEdicolaMaster, codUtente);
	}
	
	@Override
	public List<EstrattoContoFatturaClientiDto> getListEstrattiContoFattureClienti(Long codCliente, Timestamp dataDa, Timestamp dataA, Integer tipoDocumento) {
		return repository.getListEstrattiContoFattureClienti(codCliente, dataDa, dataA, tipoDocumento);
	}
	
	@Override
	public void chiudiMovimentiEstrattoContoClientiVo(Long codCliente, Timestamp dataUpdate, Timestamp dataA) {
		repository.chiudiMovimentiEstrattoContoClientiVo(codCliente, dataUpdate, dataA);
	}
	
	@Override
	public void chiudiMovimentiVenditeEstrattoContoClientiVo(Integer codEdicola, Long codCliente, Timestamp dataUpdate, Timestamp dataA) {
		repository.chiudiMovimentiVenditeEstrattoContoClientiVo(codEdicola, codCliente, dataUpdate, dataA);
	}
	
	@Override
	public void deleteRichiesteCliente(Integer[] codEdicola, Integer[] codFiegDl, Long codCliente) {
		repository.deleteRichiesteCliente(codEdicola, codFiegDl, codCliente);
	}
	
	@Override
	public void resetCodClienteVendite(Long codCliente) {
		repository.resetCodClienteVendite(codCliente);
	}
	
	@Override
	public void deleteMessaggiCliente(Long codCliente) {
		repository.deleteMessaggiCliente(codCliente);
	}
	
	@Override
	public void deleteClienteWithDependencies(Long codCliente, Integer[] arrCodDl, Integer[] arrCodEdicola) {
		deleteRichiesteCliente(arrCodEdicola, arrCodDl, codCliente);
		deletePagamentiCliente(codCliente);
		resetCodClienteVendite(codCliente);
		deleteMessaggiCliente(codCliente);
		deleteCliente(codCliente);
	}
	
	@Override
	public void saveDataEstrattoAndPagamento(Timestamp dataDocumento, Timestamp dataCompetenza, Integer tipoProdotti, Map<Long, BigDecimal> mapClienteImporto, Map<Long, Set<Timestamp>> mapClienteDateCompetezaEc, Map<Long, byte[]> mapClienteEstrattContoXml, Map<Long, Integer> mapClienteNumeroEC, Integer tipoDocumento) {
		updateDataEstrattoConto(dataCompetenza, tipoProdotti, mapClienteDateCompetezaEc);
		insertPagamentoClienti(dataCompetenza, dataDocumento, mapClienteImporto, tipoProdotti, mapClienteEstrattContoXml, mapClienteNumeroEC, tipoDocumento);
	}
	
	@Override
	public void saveFatturaEmessaAndPagamento(Timestamp dataDocumento, Timestamp dataCompetenza, Integer tipoProdotti, Map<Long, BigDecimal> mapClienteImporto, Map<Long, Set<Timestamp>> mapClienteDateCompetezaEc, Map<Long, Integer> mapClienteNumeroFattura, Integer tipoDocumento) {
		updateFatturaEmessa(mapClienteDateCompetezaEc, mapClienteNumeroFattura);
		insertPagamentoClienti(dataCompetenza, dataDocumento, mapClienteImporto, tipoProdotti, null, mapClienteNumeroFattura, tipoDocumento);
	}
	
	@Override
	public void saveMovimentiEstrattoContoClientiVo(Integer codEdicola, Long codCliente, Timestamp dataUpdate, Timestamp dataA, MovimentiEstrattoContoClientiVo vo) {
		chiudiMovimentiEstrattoContoClientiVo(codCliente, dataUpdate, dataA);
		chiudiMovimentiVenditeEstrattoContoClientiVo(codEdicola, codCliente, dataUpdate, dataA);
		if (vo != null) {
			saveBaseVo(vo);
		}
	}
	
	@Override
	public List<EmailTemplateVo> getEmailTemplates(Integer codEdicola) {
		return repository.getEmailTemplates(codEdicola);
	}
	
	@Override
	public EmailTemplateVo getEmailTemplate(Integer codTemplate) {
		return repository.getEmailTemplate(codTemplate);
	}

	@Override
	public List<ClienteEdicolaDto> getClientiEdicola(Integer[] codEdicola,
			String nome, String cognome, Long codCliente) {
		return repository.getClientiEdicola(codEdicola, nome, cognome, codCliente);
	}

	@Override
	public List<ClienteEdicolaDto> getClientiEdicolaScolastica(Integer[] codEdicola, String nome, String cognome, Long codCliente, String numOrdineTxt) {
		return repository.getClientiEdicolaScolastica(codEdicola, nome, cognome, codCliente, numOrdineTxt);
	}
	
}
