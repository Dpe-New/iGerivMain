package it.dpe.igeriv.bo.inforiv;

import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.vo.AbbinamentoTipoMovimentoFondoBollaInforivVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.inforiv.dto.output.InforivMancanzeEccedenzeDto;
import it.dpe.inforiv.dto.output.InforivResaDichiarataDto;
import it.dpe.inforiv.dto.output.InforivTotaleBollaConsegnaAccertataDto;
import it.dpe.inforiv.dto.output.InforivTotaleBollaResaEdicolaDto;
import it.dpe.inforiv.dto.output.InforivVariazioniServizioDto;
import it.dpe.inforiv.dto.output.InforivVenditeDto;

import java.util.Date;
import java.util.List;
 
/**
 * @author romanom
 *
 */
public interface InforivExportBo extends BaseRepository {
	
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

}
