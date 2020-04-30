package it.dpe.igeriv.jms.service;

import it.dpe.igeriv.dto.EdicolaDto;
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
import java.util.List;

public interface JmsService {
	
	public <T extends BaseVo> T saveVo(T vo);
	
	public <T extends BaseVo> T createVo(T vo);
	
	List<RichiestaRifornimentoJmsMessage> getListRichiestaRifornimentoJmsMessage();
	
	List<BollaResaProdottiVariJmsMessage> getListReseProdottiVariJmsMessage();

	void updateEsportazioneRichiestaRifornimentoTemp();
	
	void updateEsportazioneResaTemp();
	
	void updateEsportazioneRichiestaRifornimentoSuccess(String msgId);
	
	void updateEsportazioneRichiestaRifornimentoError(String msgId);
	
	void updateEsportazioneResaSuccess(String msgId, List<Long> listIdDocumento);
	
	void updateEsportazioneResaError(String msgId, List<Long> listIdDocumento);

	void updateRichiestaRifornimentoEvasa(Long codRichiestaRifornimento, Long codProdottoInterno, String stato, Integer quatitaEvasa, String note);

	List<EdicolaDto> getEdicoleAttive(Integer codFornitore);

	ProdottiNonEditorialiFornitoreVo getFornitore(Integer codEdicola, Integer codFornitore);

	LocalitaVo getLocalita(String localita);

	ProvinciaVo getProvincia(String provincia);
	
	ProdottiNonEditorialiCategoriaEdicolaVo getCategoriaEdicola(Long codCategoria, Integer codEdicola);

	public ProdottiNonEditorialiSottoCategoriaEdicolaVo getSottoCategoriaEdicola(Long codCategoria, Long codSottoCategoria, Integer codEdicola);

	public ProdottiNonEditorialiPrezziAcquistoVo getProdottiNonEditorialiPrezziAcquistoVo(Integer codEdicola, Integer codFornitore, String codiceProdottoFornitore);

	public Long getNextSeqVal(String sequence);

	public ProdottiNonEditorialiGiacenzeVo getGiacenza(Integer codFornitore, String codProdottoFornitore);

	public List<ProdottiNonEditorialiCausaliMagazzinoVo> getListCausaliMagazzino();

	public AnagraficaEdicolaVo getAnagraficaEdicola(Integer codEdicolaWeb);

	public ProdottiNonEditorialiPrezziAcquistoVo getProdottiNonEditorialeByCodFornitore(Integer codEdicola, Integer codFornitore, String codiceProdottoFornitore);

	public ProdottiNonEditorialiCausaliMagazzinoVo getCausaleMagazzino(Integer codiceCausale);

	public Timestamp getSysdate();

	public ProdottiNonEditorialiBollaVo getProdottiNonEditorialiBolla(Long idDocumento);

	public AnagraficaEdicolaVo getAnagraficaEdicola(Integer codiceDl, Integer codEdicolaDl);

}
