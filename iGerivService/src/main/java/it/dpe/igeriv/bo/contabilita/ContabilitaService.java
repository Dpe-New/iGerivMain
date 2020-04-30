package it.dpe.igeriv.bo.contabilita;


import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.EstrattoContoDinamicoDto;
import it.dpe.igeriv.dto.EstrattoContoDto;
import it.dpe.igeriv.dto.FatturazioneDto;
import it.dpe.igeriv.dto.FattureDLDto;
import it.dpe.igeriv.dto.VendutoEstrattoContoDto;
import it.dpe.igeriv.vo.CausaleMovimentiEstrattoContoVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaVo;
import it.dpe.igeriv.vo.FattureEdicolaPdfVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiProgressiviFatturazioneVo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Interfaccia contabilita
 * 
 * @author mromano
 *
 */
public interface ContabilitaService extends BaseService {
	
	/**
	 * @param dtEstrattoConto
	 * @return
	 */
	public List<EstrattoContoEdicolaDettaglioVo> getListEstrattoContoDettaglio(Integer codFiegDl, Integer codEdicola, Timestamp dtEstrattoConto);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtEstrattoConto
	 * @return
	 */
	public List<VendutoEstrattoContoDto> getListVendutoEstrattoConto(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dataDa, Timestamp dataA);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtEstrattoConto
	 * @return
	 */
	public EstrattoContoDto getEstrattoConto(Integer codFiegDl, Integer codEdicola, Timestamp dtEstrattoConto);
	
	/**
	 * @param dataInizioEstrattoContoPdf 
	 * @return
	 */
	public List<EstrattoContoDto> getListDateEstrattoConto(Integer codFiegDl, Integer codEdicola, java.sql.Date dataInizioEstrattoContoPdf);
	
	/**
	 * @param dataInizioEstrattoContoPdf 
	 * @return
	 */
	public List<EstrattoContoDto> getListDateEstrattoContoPdf(Integer codFiegDl, Integer codEdicola);
	
	/**
	 * @param codDlInt
	 * @param dtFatturazione
	 * @param trimestre
	 * @return
	 */
	public List<FatturazioneDto> getFatturazioneEdicole(Integer codDlInt, Date dtFatturazione, Integer trimestre);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dataEstrattoConto
	 * @return
	 */
	public EstrattoContoEdicolaVo getEstrattoContoEdicolaVo(Integer codFiegDl, Integer codEdicolaWeb, Date dataEstrattoConto);
	
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
	public Integer getLastProgressivoEstrattoConto(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto);
	
	/**
	 * @param codCausale
	 * @return
	 */
	public CausaleMovimentiEstrattoContoVo getCausaleMovimentoEstrattoConto(Integer codCausale);

	/**
	 * @param codCliente
	 * @param dataA
	 * @return
	 */
	public boolean existsEstrattoContoChiuso(Long codCliente, Timestamp dataA);
	
	/**
	 * @param codEdicolaMaster
	 * @param tipoDocumento
	 * @param data
	 * @return
	 */
	public ProdottiNonEditorialiProgressiviFatturazioneVo getNextProgressivoFatturazioneVo(Integer codEdicolaMaster, Integer tipoDocumento, java.util.Date data);
	
	/**
	 * @param codEdicolaMaster
	 * @param tipoDocumentoFatturaEmessa
	 * @param data
	 * @return
	 */
	public Long getNextProgressivoFatturazione(Integer codEdicolaMaster, Integer tipoDocumentoFatturaEmessa, Date data);
	
	/**
	 * @param codEdicola
	 * @param tipoDocumentoEstrattoConto
	 * @param data
	 * @param progressivo
	 */
	public void setNextProgressivoFatturazione(Integer codEdicola, Integer tipoDocumentoEstrattoConto, Date data, Long progressivo);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @return
	 */
	public List<EstrattoContoDinamicoDto> getEstrattoContoDinamico(Integer codFiegDl, Integer codEdicolaWeb);
	
	/**
	 * @param codDl
	 * @param codEdicola
	 * @param dataECFinale
	 */
	public void deleteEstrattoContoFinoAllaData(Integer codDl, Integer codEdicola, Timestamp dataECFinale);
	
	public List<FattureEdicolaPdfVo> getFattureDL(Integer codFiegDl, Integer codEdicolaWeb);
	public List<FattureEdicolaPdfVo> getFattureDL(Integer codFiegDl, Integer codEdicolaWeb, Date from);
	public List<FattureDLDto> getFattureDLDto(Integer codFiegDl, Integer codEdicolaWeb);
	public List<FattureDLDto> getFattureDLDto(Integer codFiegDl, Integer codEdicolaWeb, Date from);

}
