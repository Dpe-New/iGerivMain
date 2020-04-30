package it.dpe.igeriv.bo.inforiv;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.Edicola;
import it.dpe.igeriv.dto.ImportazioneInforivReplyDto;
import it.dpe.igeriv.exception.EdicolaGiaEsistenteException;
import it.dpe.igeriv.exception.EdicolaInforivFtpException;
import it.dpe.igeriv.vo.AbbinamentoTipoMovimentoFondoBollaInforivVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.inforiv.dto.output.InforivMancanzeEccedenzeDto;
import it.dpe.inforiv.dto.output.InforivResaDichiarataDto;
import it.dpe.inforiv.dto.output.InforivTotaleBollaConsegnaAccertataDto;
import it.dpe.inforiv.dto.output.InforivTotaleBollaResaEdicolaDto;
import it.dpe.inforiv.dto.output.InforivVariazioniServizioDto;
import it.dpe.inforiv.dto.output.InforivVenditeDto;

import java.io.IOException;
import java.net.SocketException;
import java.util.Date;
import java.util.List;
 
/**
 * @author romanom
 *
 */
public interface InforivExportService extends BaseService {
	
	/**
	 * @param codFiegDl
	 * @return
	 */
	public List<InforivTotaleBollaConsegnaAccertataDto> getBolleRiassuntoDaTrasmettereDl(Integer codFiegDl);
	
	/**
	 * @param codFiegDl
	 */
	public void updateBolleRiassuntoTrasmesseDl(Integer codFiegDl);
	
	/**
	 * @param codFiegDl
	 */
	public void updateBolleResaRiassuntoTrasmesseDl(Integer codFiegDl);
	
	/**
	 * @param codFiegDl
	 * @param tipoBolla 
	 * @param dataBolla 
	 * @return
	 */
	public List<InforivMancanzeEccedenzeDto> getBollaDettaglioConDifferenze(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla);
	
	/**
	 * @param codFiegDl
	 * @return
	 */
	public List<InforivTotaleBollaResaEdicolaDto> getBolleResaRiassuntoDaTrasmettereDl(Integer codFiegDl);

	/**
	 * @param codFiegDl
	 * @param codEdicola 
	 * @param tipoBolla 
	 * @param dataBolla 
	 * @return
	 */
	public List<InforivResaDichiarataDto> getBollaResaDettaglio(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla);

	/**
	 * @param codFiegDl
	 * @param codEdicola 
	 * @param tipoBolla 
	 * @param dataBolla 
	 * @return
	 */
	public List<InforivResaDichiarataDto> getBollaResaDettaglioFuoriVoce(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla);

	/**
	 * @param codFiegDl
	 * @param codEdicola 
	 * @param tipoBolla 
	 * @param dataBolla 
	 * @return
	 */
	public List<InforivResaDichiarataDto> getBollaResaDettaglioRichiamoPersonalizzato(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla);
	
	/**
	 * @param codFiegDl
	 */
	public void updateBollaResaRiassuntoTrasmesseDl(Integer codFiegDl);
	
	/**
	 * @param codFiegDl
	 * @return
	 */
	public List<InforivVenditeDto> getVenditeDaTrasmettereDl(Integer codFiegDl);
	
	/**
	 * @param codFiegDl
	 * @return
	 */
	public List<RichiestaRifornimentoVo> getRichiesteRifornimenti(Integer codFiegDl);
	
	/**
	 * @param codFiegDl
	 * @return
	 */
	public List<InforivVariazioniServizioDto> getVariazioni(Integer codFiegDl);

	/**
	 * @param codFiegDl
	 */
	public void updateVenditeTrasmesse(Integer codFiegDl);

	/**
	 * @param codFiegDl
	 */
	public void updateRichiesteRifornimenti(Integer codFiegDl);

	/**
	 * @param codFiegDl
	 */
	public void updateVariazioni(Integer codFiegDl);

	/**
	 * @param tipoMovimento
	 */
	public AbbinamentoTipoMovimentoFondoBollaInforivVo getAbbinamentoTipoMovimentoFondoBollaInforiv(Integer tipoMovimentoInforiv);
	
	/**
	 * @param edicola
	 * @param isNetEdicola
	 * @param giorniTrial
	 * @param igerivUrl
	 * @param validateFtpConection
	 * @return
	 * @throws EdicolaGiaEsistenteException
	 * @throws SocketException
	 * @throws IOException
	 * @throws EdicolaInforivFtpException
	 */
	public ImportazioneInforivReplyDto importEdicola(Edicola edicola, boolean isNetEdicola, Integer giorniTrial, String igerivUrl, boolean validateFtpConection, boolean edicolaIGerivInforivDl) throws EdicolaGiaEsistenteException, SocketException, IOException, EdicolaInforivFtpException;
	
}
