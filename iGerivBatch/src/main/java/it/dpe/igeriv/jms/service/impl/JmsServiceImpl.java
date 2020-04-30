package it.dpe.igeriv.jms.service.impl;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.isIn;
import it.dpe.igeriv.bo.batch.IGerivBatchBo;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.fornitori.FornitoriService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.dto.BollaResaProdottiNonEditorialiDto;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoProdottiDto;
import it.dpe.igeriv.jms.service.JmsService;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.LocalitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCausaliMagazzinoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiFornitoreVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiGiacenzeVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.jms.dto.BollaResaProdottiVariJmsMessage;
import it.dpe.jms.dto.RichiestaRifornimentoJmsMessage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component("JmsService")
public class JmsServiceImpl implements JmsService {
	
	@Autowired
	private IGerivBatchBo bo;
	
	@Autowired
	private ProdottiService prodottiService;
	
	@Autowired
	private FornitoriService fornitoriService;
	
	@Autowired
	private EdicoleService edicoleService;
	
	@Value("${list.cod.web.edicole.test.interfaccia.prodotti.vari}")
	private String edicoleTestProdottiVari;
	
	public IGerivBatchBo getBo() {
		return bo;
	}

	public void setBo(IGerivBatchBo bo) {
		this.bo = bo;
	}

	public ProdottiService getProdottiService() {
		return prodottiService;
	}

	public void setProdottiService(ProdottiService prodottiService) {
		this.prodottiService = prodottiService;
	}

	public FornitoriService getFornitoriService() {
		return fornitoriService;
	}

	public void setFornitoriService(FornitoriService fornitoriService) {
		this.fornitoriService = fornitoriService;
	}
	
	public EdicoleService getEdicoleService() {
		return edicoleService;
	}

	public void setEdicoleService(EdicoleService edicoleService) {
		this.edicoleService = edicoleService;
	}

	@Override
	public List<RichiestaRifornimentoJmsMessage> getListRichiestaRifornimentoJmsMessage() {
		List<RichiestaRifornimentoProdottiDto> richiesteRifornimentoDaEvadere = bo.getRichiesteRifornimentoDaEvadere(true);
		return buildListRichiestaRifornimentoJmsMessage(richiesteRifornimentoDaEvadere);
	}
	
	@Override
	public List<BollaResaProdottiVariJmsMessage> getListReseProdottiVariJmsMessage() {
		List<BollaResaProdottiNonEditorialiDto> richiesteRifornimentoDaEvadere = bo.getReseProdottiVariDaInviare();
		return buildListResaJmsMessage(richiesteRifornimentoDaEvadere);
	}
	
	@Override
	public void updateEsportazioneRichiestaRifornimentoTemp() {
		bo.updateEsportazioneRichiestaRifornimentoTemp();
	}
	
	@Override
	public void updateEsportazioneResaTemp() {
		bo.updateEsportazioneResaTemp();
	}
	
	@Override
	public void updateEsportazioneRichiestaRifornimentoSuccess(String msgId) {
		bo.updateEsportazioneRichiestaRifornimentoSuccess(msgId);
	}
	
	@Override
	public void updateEsportazioneRichiestaRifornimentoError(String msgId) {
		bo.updateEsportazioneRichiestaRifornimentoError(msgId);
	}
	
	@Override
	public void updateEsportazioneResaSuccess(String correlationId, List<Long> listIdDocumento) {
		bo.updateEsportazioneResaSuccessDettagli(correlationId);
		if (listIdDocumento == null) {
			listIdDocumento = bo.getListIdDocumentoBollaDettaglioFromCorrelationId(correlationId);
		}
		bo.updateEsportazioneResaSuccessTesta(listIdDocumento);
	}
	
	@Override
	public void updateEsportazioneResaError(String msgId, List<Long> listIdDocumento) {
		bo.updateEsportazioneResaErrorDettagli(msgId);
		bo.updateEsportazioneResaErrorTesta(listIdDocumento);
	}
	
	@Override
	public void updateRichiestaRifornimentoEvasa(Long codRichiestaRifornimento, Long codProdottoInterno, String stato, Integer quatitaEvasa, String note) {
		bo.updateRichiestaRifornimentoEvasa(codRichiestaRifornimento, codProdottoInterno, stato, quatitaEvasa, note);
	}
	
	private List<RichiestaRifornimentoJmsMessage> buildListRichiestaRifornimentoJmsMessage(List<RichiestaRifornimentoProdottiDto> richiesteRifornimentoDaEvadere) {
		List<RichiestaRifornimentoJmsMessage> list = new ArrayList<RichiestaRifornimentoJmsMessage>();
		for (RichiestaRifornimentoProdottiDto rf : richiesteRifornimentoDaEvadere) {
			RichiestaRifornimentoJmsMessage rrj = new RichiestaRifornimentoJmsMessage();
			rrj.setCodEdicolaDl(rf.getCodEdicolaDl());
			rrj.setCodFornitore(rf.getCodFornitore());
			rrj.setCodiceProdottoFornitore(new Integer(rf.getCodiceProdottoFornitore()));
			rrj.setCodProdottoInterno(rf.getCodProdottoInterno());
			rrj.setNumeroDocumento(rf.getCodRichiestaRifornimento().toString());
			rrj.setDataRichiesta(rf.getDataRichiesta());
			rrj.setQuatitaRichiesta(rf.getQuatitaRichiesta());
			rrj.setTipoMessaggio(JmsConstants.TIPO_MESSAGGIO_RICHIESTA_RIFORNIMENTO);
			rrj.setCodiceContabileCliente(rf.getCodiceContabileCliente());
			rrj.setCodProdottoInterno(rf.getCodProdottoInterno());
			rrj.setFormazionePacco(rf.getFormazionePacco());
			list.add(rrj);
		}
		return list;
	}
	
	private List<BollaResaProdottiVariJmsMessage> buildListResaJmsMessage(List<BollaResaProdottiNonEditorialiDto> listDto) {
		List<BollaResaProdottiVariJmsMessage> list = new ArrayList<BollaResaProdottiVariJmsMessage>();
		for (BollaResaProdottiNonEditorialiDto rf : listDto) {
			BollaResaProdottiVariJmsMessage rrj = new BollaResaProdottiVariJmsMessage();
			rrj.setCodEdicolaDl(rf.getCodEdicolaDl());
			rrj.setCodFornitore(rf.getCodFornitore());
			rrj.setCodiceProdottoFornitore(new Integer(rf.getCodiceProdottoFornitore()));
			rrj.setCodProdottoInterno(rf.getCodProdottoInterno());
			rrj.setNumeroDocumento(rf.getNumeroDocumento());
			rrj.setDataRegistrazione(rf.getDataRegistrazione());
			rrj.setQuantita(rf.getQuantita());
			rrj.setTipoMessaggio(JmsConstants.TIPO_MESSAGGIO_RESA_PRODOTTI_VARI);
			rrj.setCodiceContabileCliente(rf.getCodiceContabileCliente());
			rrj.setCodProdottoInterno(rf.getCodProdottoInterno());
			rrj.setFormazionePacco(rf.getFormazionePacco());
			list.add(rrj);
		}
		return list;
	}
	
	@Override
	public List<EdicolaDto> getEdicoleAttive(Integer codFornitore) {
		List<EdicolaDto> edicoleAttive = bo.getEdicoleAttive(codFornitore);
		if (!Strings.isNullOrEmpty(edicoleTestProdottiVari) && !edicoleTestProdottiVari.trim().equals("0")) {
			String[] arrCodWebEdicola = edicoleTestProdottiVari.split(",");
			List<Integer> listCodWebEdicole = new ArrayList<Integer>();
			for (String cod : arrCodWebEdicola) {
				listCodWebEdicole.add(new Integer(cod.trim()));
			}
			return select(edicoleAttive, having(on(EdicolaDto.class).getCodEdicolaWeb(), isIn(listCodWebEdicole)));
		}
		return edicoleAttive;
	}
	
	@Override
	public ProdottiNonEditorialiFornitoreVo getFornitore(Integer codEdicola, Integer codFornitore) {
		return fornitoriService.getFornitore(codEdicola, codFornitore);
	}
	
	@Override
	public LocalitaVo getLocalita(String localita) {
		return bo.getLocalita(localita);
	}
	
	@Override
	public ProvinciaVo getProvincia(String provincia) {
		return bo.getProvincia(provincia);
	}
	
	@Override
	public ProdottiNonEditorialiCategoriaEdicolaVo getCategoriaEdicola(Long codCategoria, Integer codEdicola) {
		return prodottiService.getProdottiNonEditorialiCategoriaEdicolaVo(codCategoria, codEdicola);
	}
	
	@Override
	public ProdottiNonEditorialiSottoCategoriaEdicolaVo getSottoCategoriaEdicola(Long codCategoria, Long codSottoCategoria, Integer codEdicola) {
		return prodottiService.getProdottiNonEditorialiSottoCategoriaEdicolaVo(codCategoria, codSottoCategoria, codEdicola);
	}
	
	@Override
	public ProdottiNonEditorialiPrezziAcquistoVo getProdottiNonEditorialiPrezziAcquistoVo(Integer codEdicola, Integer codFornitore, String codiceProdottoFornitore) {
		return bo.getProdottiNonEditorialiPrezziAcquistoVo(codEdicola, codFornitore, codiceProdottoFornitore);
	}
	
	@Override
	public ProdottiNonEditorialiGiacenzeVo getGiacenza(Integer codFornitore, String codProdottoFornitore) {
		return bo.getGiacenza(codFornitore, codProdottoFornitore);
	}

	public String getEdicoleTestProdottiVari() {
		return edicoleTestProdottiVari;
	}

	public void setEdicoleTestProdottiVari(String edicoleTestProdottiVari) {
		this.edicoleTestProdottiVari = edicoleTestProdottiVari;
	}
	
	@Override
	public List<ProdottiNonEditorialiCausaliMagazzinoVo> getListCausaliMagazzino() {
		return prodottiService.getCausali();
	}
	
	@Override
	public AnagraficaEdicolaVo getAnagraficaEdicola(Integer codEdicolaWeb) {
		return edicoleService.getAnagraficaEdicola(codEdicolaWeb);
	}
	
	@Override
	public ProdottiNonEditorialiPrezziAcquistoVo getProdottiNonEditorialeByCodFornitore(Integer codEdicola, Integer codFornitore, String codiceProdottoFornitore) {
		return prodottiService.getProdottoNonEditorialiPrezzoAcquistoByCodFornitore(codEdicola, codFornitore, codiceProdottoFornitore);
	}
	
	@Override
	public ProdottiNonEditorialiCausaliMagazzinoVo getCausaleMagazzino(Integer codiceCausale) {
		return prodottiService.getCausaleMagazzino(codiceCausale);
	}
	
	@Override
	public <T extends BaseVo> T saveVo(T vo) {
		return prodottiService.saveBaseVo(vo);
	}
	
	@Override
	public ProdottiNonEditorialiBollaVo getProdottiNonEditorialiBolla(Long idDocumento) {
		return prodottiService.getBolleProdottiVariEdicola(idDocumento);
	}
	
	@Override
	public AnagraficaEdicolaVo getAnagraficaEdicola(Integer codiceDl, Integer codEdicolaDl) {
		return edicoleService.getEdicolaByCodFiegDlCodRivDl(codiceDl, codEdicolaDl);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseVo createVo(BaseVo vo) {
		return bo.createBaseVo(vo);
	}

	@Override
	public Long getNextSeqVal(String sequence) {
		return prodottiService.getNextSeqVal(sequence);
	}
	
	@Override
	public Timestamp getSysdate() {
		return bo.getSysdate();
	}
	
}
