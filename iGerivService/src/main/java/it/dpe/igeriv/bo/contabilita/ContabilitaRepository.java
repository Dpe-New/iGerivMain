package it.dpe.igeriv.bo.contabilita;


import it.dpe.igeriv.bo.BaseRepository;
import it.dpe.igeriv.dto.EstrattoContoDinamicoDto;
import it.dpe.igeriv.dto.EstrattoContoDto;
import it.dpe.igeriv.dto.FatturazioneDto;
import it.dpe.igeriv.dto.VendutoEstrattoContoDto;
import it.dpe.igeriv.vo.CausaleMovimentiEstrattoContoVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaPdfVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaVo;
import it.dpe.igeriv.vo.FattureEdicolaPdfVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiProgressiviFatturazioneVo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

interface ContabilitaRepository extends BaseRepository {
	
	/**
	 * @param dtEstrattoConto
	 * @return
	 */
	List<EstrattoContoEdicolaDettaglioVo> getListEstrattoContoDettaglio(Integer codFiegDl, Integer codEdicola, Timestamp dtEstrattoConto);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtEstrattoConto
	 * @return
	 */
	List<VendutoEstrattoContoDto> getListVendutoEstrattoConto(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dataDa, Timestamp dataA);
	
	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param dtEstrattoConto
	 * @return
	 */
	EstrattoContoDto getEstrattoConto(Integer codFiegDl, Integer codEdicola, Timestamp dtEstrattoConto);
	
	/**
	 * @param dataInizioEstrattoContoPdf 
	 * @return
	 */
	List<EstrattoContoDto> getListDateEstrattoConto(Integer codFiegDl, Integer codEdicola, java.sql.Date dataInizioEstrattoContoPdf);
	
	/**
	 * @param dataInizioEstrattoContoPdf 
	 * @return
	 */
	List<EstrattoContoDto> getListDateEstrattoContoPdf(Integer codFiegDl, Integer codEdicola);
	
	/**
	 * @param codDlInt
	 * @param dtFatturazione
	 * @param trimestre
	 * @return
	 */
	List<FatturazioneDto> getFatturazioneEdicole(Integer codDlInt, Date dtFatturazione, Integer trimestre);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dataEstrattoConto
	 * @return
	 */
	EstrattoContoEdicolaVo getEstrattoContoEdicolaVo(Integer codFiegDl, Integer codEdicolaWeb, Date dataEstrattoConto);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dataEstrattoConto
	 * @param progressivo
	 * @return
	 */
	EstrattoContoEdicolaDettaglioVo getEstrattoContoEdicolaDettaglioVo(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto, int progressivo);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @param dataEstrattoConto
	 * @return
	 */
	Integer getLastProgressivoEstrattoConto(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto);
	
	/**
	 * @param codCausale
	 * @return
	 */
	CausaleMovimentiEstrattoContoVo getCausaleMovimentoEstrattoConto(Integer codCausale);

	/**
	 * @param codCliente
	 * @param dataA
	 * @return
	 */
	boolean existsEstrattoContoChiuso(Long codCliente, Timestamp dataA);
	
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
	Long getNextProgressivoFatturazione(Integer codEdicolaMaster, Integer tipoDocumentoFatturaEmessa, Date data);
	
	/**
	 * @param codEdicola
	 * @param tipoDocumentoEstrattoConto
	 * @param data
	 * @param progressivo
	 */
	void setNextProgressivoFatturazione(Integer codEdicola, Integer tipoDocumentoEstrattoConto, java.util.Date data, Long progressivo);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaWeb
	 * @return
	 */
	List<EstrattoContoDinamicoDto> getEstrattoContoDinamico(Integer codFiegDl, Integer codEdicolaWeb);
	
	/**
	 * @param codDl
	 * @param codEdicola
	 * @param dataECFinale
	 * @return
	 */
	public List<EstrattoContoEdicolaDettaglioVo> getEstrattoContoEdicolaDettaglioVoFinoAllaData(Integer codDl, Integer codEdicola, Timestamp dataECFinale);
	
	/**
	 * @param codDl
	 * @param codEdicola
	 * @param dataECFinale
	 * @return
	 */
	public List<EstrattoContoEdicolaVo> getEstrattoContoEdicolaVoFinoAllaData(Integer codDl, Integer codEdicola, Timestamp dataECFinale);
	
	/**
	 * @param codDl
	 * @param codEdicola
	 * @param dataECFinale
	 * @return
	 */
	public List<EstrattoContoEdicolaPdfVo> getEstrattoContoEdicolaPdfVoFinoAllaData(Integer codDl, Integer codEdicola, Timestamp dataECFinale);

	/**
	 * @param codDl
	 * @param codEdicola
	 * @param dataFatturaFinale
	 * @return
	 */
	public List<FattureEdicolaPdfVo> getFattureEdicolaPdfVoFinoAllaData(Integer codDl, Integer codEdicola, Timestamp dataFatturaFinale);

	public List<FattureEdicolaPdfVo> getFattureDLdallaData(Integer codFiegDl, Integer codEdicolaWeb, Date from);

	
}
