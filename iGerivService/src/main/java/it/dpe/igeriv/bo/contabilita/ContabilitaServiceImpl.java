package it.dpe.igeriv.bo.contabilita;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.EstrattoContoDinamicoDto;
import it.dpe.igeriv.dto.EstrattoContoDto;
import it.dpe.igeriv.dto.FatturazioneDto;
import it.dpe.igeriv.dto.FattureDLDto;
import it.dpe.igeriv.dto.VendutoEstrattoContoDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.vo.CausaleMovimentiEstrattoContoVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaPdfVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaVo;
import it.dpe.igeriv.vo.FattureEdicolaPdfVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiProgressiviFatturazioneVo;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ContabilitaService")
class ContabilitaServiceImpl extends BaseServiceImpl implements ContabilitaService {
	private final ContabilitaRepository repository;
	
	@Autowired
	ContabilitaServiceImpl(ContabilitaRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<EstrattoContoDto> getListDateEstrattoConto(Integer codFiegDl, Integer codEdicola, Date dataInizioEstrattoContoPdf) {
		return repository.getListDateEstrattoConto(codFiegDl, codEdicola, dataInizioEstrattoContoPdf);
	}

	@Override
	public List<EstrattoContoDto> getListDateEstrattoContoPdf(Integer codFiegDl, Integer codEdicola) {
		return repository.getListDateEstrattoContoPdf(codFiegDl, codEdicola);
	}
	
	@Override
	public List<FatturazioneDto> getFatturazioneEdicole(Integer codDlInt, java.util.Date dtFatturazione, Integer trimestre) {
		return repository.getFatturazioneEdicole(codDlInt, dtFatturazione, trimestre);
	}
	
	@Override
	public EstrattoContoEdicolaVo getEstrattoContoEdicolaVo(Integer codFiegDl, Integer codEdicolaWeb, java.util.Date dataEstrattoConto) {
		return repository.getEstrattoContoEdicolaVo(codFiegDl, codEdicolaWeb, dataEstrattoConto);
	}
	
	@Override
	public EstrattoContoEdicolaDettaglioVo getEstrattoContoEdicolaDettaglioVo(Integer codFiegDl, Integer codEdicolaWeb, Date dataEstrattoConto, int progressivo) {
		return repository.getEstrattoContoEdicolaDettaglioVo(codFiegDl, codEdicolaWeb, dataEstrattoConto, progressivo);
	}
	
	@Override
	public Integer getLastProgressivoEstrattoConto(Integer codFiegDl, Integer codEdicolaWeb, Date dataEstrattoConto) {
		return repository.getLastProgressivoEstrattoConto(codFiegDl, codEdicolaWeb, dataEstrattoConto);
	}
	
	@Override
	public CausaleMovimentiEstrattoContoVo getCausaleMovimentoEstrattoConto(Integer codCausale) {
		return repository.getCausaleMovimentoEstrattoConto(codCausale);
	}
	
	@Override
	public boolean existsEstrattoContoChiuso(Long codCliente, Timestamp dataA) {
		return repository.existsEstrattoContoChiuso(codCliente, dataA);
	}
	
	@Override
	public ProdottiNonEditorialiProgressiviFatturazioneVo getNextProgressivoFatturazioneVo(Integer codEdicolaMaster, Integer tipoDocumento, java.util.Date data) {
		return repository.getNextProgressivoFatturazioneVo(codEdicolaMaster, tipoDocumento, data);
	}
	
	@Override
	public Long getNextProgressivoFatturazione(Integer codEdicolaMaster, Integer tipoDocumentoFatturaEmessa, java.util.Date data) {
		return repository.getNextProgressivoFatturazione(codEdicolaMaster, tipoDocumentoFatturaEmessa, data);
	}
	
	@Override
	public void setNextProgressivoFatturazione(Integer codEdicola, Integer tipoDocumentoEstrattoConto, java.util.Date data, Long progressivo) {
		repository.setNextProgressivoFatturazione(codEdicola, tipoDocumentoEstrattoConto, data, progressivo);
	}

	@Override
	public List<EstrattoContoEdicolaDettaglioVo> getListEstrattoContoDettaglio(Integer codFiegDl, Integer codEdicola, Timestamp dtEstrattoConto) {
		return repository.getListEstrattoContoDettaglio(codFiegDl, codEdicola, dtEstrattoConto);
	}

	@Override
	public List<VendutoEstrattoContoDto> getListVendutoEstrattoConto(Integer[] codFiegDl, Integer[] codEdicola, Timestamp dataDa, Timestamp dataA) {
		return repository.getListVendutoEstrattoConto(codFiegDl, codEdicola, dataDa, dataA);
	}

	@Override
	public EstrattoContoDto getEstrattoConto(Integer codFiegDl, Integer codEdicola, Timestamp dtEstrattoConto) {
		return repository.getEstrattoConto(codFiegDl, codEdicola, dtEstrattoConto);
	}
	
	@Override
	public List<EstrattoContoDinamicoDto> getEstrattoContoDinamico(Integer codFiegDl, Integer codEdicolaWeb) {
		return repository.getEstrattoContoDinamico(codFiegDl, codEdicolaWeb);
	}
	
	@Override
	public void deleteEstrattoContoFinoAllaData(Integer codDl, Integer codEdicola, Timestamp dataECFinale) {
		List<EstrattoContoEdicolaPdfVo> ecPdfList = repository.getEstrattoContoEdicolaPdfVoFinoAllaData(codDl, codEdicola, dataECFinale);
		repository.deleteVoList(ecPdfList);
		ecPdfList.clear();
		ecPdfList = null;
		
		List<FattureEdicolaPdfVo> fatturePdfList = repository.getFattureEdicolaPdfVoFinoAllaData(codDl, codEdicola, dataECFinale);
		repository.deleteVoList(fatturePdfList);
		fatturePdfList.clear();
		fatturePdfList = null;
		
		List<EstrattoContoEdicolaDettaglioVo> ecDettaglioList = repository.getEstrattoContoEdicolaDettaglioVoFinoAllaData(codDl, codEdicola, dataECFinale);
		repository.deleteVoList(ecDettaglioList);
		ecDettaglioList.clear();
		ecDettaglioList = null;
		
		List<EstrattoContoEdicolaVo> ecList = repository.getEstrattoContoEdicolaVoFinoAllaData(codDl, codEdicola, dataECFinale);
		repository.deleteVoList(ecList);
		ecList.clear();
		ecList = null;
	}
	
	public List<FattureEdicolaPdfVo> getFattureDL(Integer codFiegDl, Integer codEdicolaWeb) {
		return getFattureDL(codFiegDl, codEdicolaWeb, DateUtilities.togliAnni(new java.util.Date(), 2));
	}
	public List<FattureEdicolaPdfVo> getFattureDL(Integer codFiegDl, Integer codEdicolaWeb, java.util.Date from) {
		return repository.getFattureDLdallaData(codFiegDl, codEdicolaWeb, from);
	}
	
	public List<FattureDLDto> getFattureDLDto(Integer codFiegDl, Integer codEdicolaWeb) {
		return getFattureDLDto(codFiegDl, codEdicolaWeb, DateUtilities.togliAnni(new java.util.Date(), 2));
	}
	public List<FattureDLDto> getFattureDLDto(Integer codFiegDl, Integer codEdicolaWeb, java.util.Date from) {
		List<FattureDLDto> result = new ArrayList<>();
		List<FattureEdicolaPdfVo> vos = repository.getFattureDLdallaData(codFiegDl, codEdicolaWeb, from);
		for (FattureEdicolaPdfVo vo : vos) {
			result.add(new FattureDLDto(vo));
		}
		return result;
	}

}
