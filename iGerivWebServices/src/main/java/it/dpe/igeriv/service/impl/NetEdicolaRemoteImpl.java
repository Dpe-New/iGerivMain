package it.dpe.igeriv.service.impl;

import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.ws.IGerivWSBo;
import it.dpe.igeriv.dto.Edicola;
import it.dpe.igeriv.service.NetEdicolaRemote;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.inforiv.bo.InforivImportBo;

import java.util.ArrayList;
import java.util.List;

import models.AbbonatoDto;
import models.EdicolaDto;
import models.EdicolaMappaDto;
import models.ResultSaveDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementazione dell'interfaccia di servizi Net Edicola. 
 * 
 * @author mromano
 *
 */
@Component("NetEdicolaRemote")
public class NetEdicolaRemoteImpl implements NetEdicolaRemote {
	@Autowired
	private IGerivWSBo boWs;
	
	@Autowired
	private EdicoleService edicoleService;
	
	@Autowired
	private InforivImportBo inforivImportBo;
	
	public IGerivWSBo getBoWs() {
		return boWs;
	}

	public void setBoWs(IGerivWSBo boWs) {
		this.boWs = boWs;
	}
	
	public EdicoleService getEdicoleService() {
		return edicoleService;
	}

	public void setEdicoleService(EdicoleService edicoleService) {
		this.edicoleService = edicoleService;
	}

	public InforivImportBo getInforivImportBo() {
		return inforivImportBo;
	}

	public void setInforivImportBo(InforivImportBo inforivImportBo) {
		this.inforivImportBo = inforivImportBo;
	}

	@Override
	public ResultSaveDto saveCliente(AbbonatoDto abbonato) {
		return null;
	}

	@Override
	public List<EdicolaMappaDto> getEdicoleMappa(Integer capDa, Integer capA) {
		return buildEdicolaMappaDto(boWs.getEdicoleGeomappateByCap(capDa, capA));
	}

	@Override
	public EdicolaDto getEdicola(Integer codEdicola) {
		EdicolaDto dto = null;
		AnagraficaEdicolaVo edicola = edicoleService.getAnagraficaEdicola(codEdicola);
		if (edicola != null) {
			dto = new EdicolaDto();
			dto.setRagioneSocialePrimaParte(edicola.getRagioneSocialeEdicolaPrimaRiga());
			dto.setRagioneSocialeSecondaParte(edicola.getRagioneSocialeEdicolaSecondaRiga());
			dto.setIndirizzo(edicola.getIndirizzoEdicolaPrimaRiga() + " " + edicola.getIndirizzoEdicolaSecondaRiga());
			dto.setLocalita(edicola.getLocalitaEdicolaPrimaRiga() + " " + edicola.getIndirizzoEdicolaSecondaRiga());
			dto.setCap(edicola.getCap());
			dto.setCodProvincia(edicola.getProvincia());
			dto.setEmail(edicola.getEmail());
			dto.setTelefono(edicola.getTelefono());
			dto.setCellulare(edicola.getCellulare());
			dto.setIdRivendita(edicola.getCodEdicola());
		}
		return dto;
	}

	@Override
	public ResultSaveDto saveEdicola(Edicola edicola) {
		return inforivImportBo.importEdicolaNetEdicola(edicola);
	}
	
	/**
	 * @param list
	 * @return
	 */
	private static List<EdicolaMappaDto> buildEdicolaMappaDto(
			List<it.dpe.igeriv.dto.EdicolaDto> list) {
		List<EdicolaMappaDto> retList = new ArrayList<EdicolaMappaDto>();
		for (it.dpe.igeriv.dto.EdicolaDto edicola : list) {
			EdicolaMappaDto dto = new EdicolaMappaDto();
			dto.setLat(edicola.getLatitudine().toString());
			dto.setLon(edicola.getLongitudine().toString());
			dto.setCodice(edicola.getCodEdicolaWeb().toString());
			dto.setLocalita(edicola.getLocalita());
			dto.setProvincia(edicola.getProvincia() != null ? edicola.getProvincia() : "");
			dto.setRagSoc(edicola.getRagioneSociale());
			dto.setVia(edicola.getIndirizzo());
			retList.add(dto);
		}
		return retList;
	}

}
