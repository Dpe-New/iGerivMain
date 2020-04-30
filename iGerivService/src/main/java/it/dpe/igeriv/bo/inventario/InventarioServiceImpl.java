package it.dpe.igeriv.bo.inventario;

import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.dto.InventarioDto;
import it.dpe.igeriv.dto.InventarioPresuntoDto;
import it.dpe.igeriv.util.Types;
import it.dpe.igeriv.util.Types.ContoDepositoType;
import it.dpe.igeriv.vo.InventarioVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("InventarioService")
class InventarioServiceImpl extends BaseServiceImpl implements InventarioService {
	private final InventarioRepository repository;
	
	@Autowired
	InventarioServiceImpl(InventarioRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public List<InventarioDto> getListInventarioDto(Integer codEdicola) {
		return repository.getListInventarioDto(codEdicola);
	}
	
	@Override
	public InventarioVo getInventarioVo(Integer codEdicola, Long idInventario) {
		return repository.getInventarioVo(codEdicola, idInventario);
	}

	@Override
	public void deleteInventario(Long idInventario) {
		repository.deleteInventario(idInventario);
	}
	
	@Override
	public BigDecimal getTotaleInventario(Long idInventario) {
		return repository.getTotaleInventario(idInventario);
	}
	
	@Override
	public List<InventarioPresuntoDto> getInventarioPresunto(Integer[] coddl, Integer[] codEdicola, Date dataUscitaDa, Date dataStorico, Integer gruppoSconto, ContoDepositoType filtroContoDeposito, Types.TipoPubblicazioneType filtroTipoPubblicazione, Boolean escludiPubblicazioniSenzaPrezzo) {
		return repository.getInventarioPresunto(coddl, codEdicola, dataUscitaDa, dataStorico, gruppoSconto, filtroContoDeposito, filtroTipoPubblicazione, escludiPubblicazioniSenzaPrezzo);
	}
	
}
