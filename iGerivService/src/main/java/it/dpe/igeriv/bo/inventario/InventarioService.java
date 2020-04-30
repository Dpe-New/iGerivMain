package it.dpe.igeriv.bo.inventario;

import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.InventarioDto;
import it.dpe.igeriv.dto.InventarioPresuntoDto;
import it.dpe.igeriv.util.Types;
import it.dpe.igeriv.vo.InventarioVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface InventarioService extends BaseService {
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public List<InventarioDto> getListInventarioDto(Integer codEdicola);
	
	/**
	 * @param Integer codEdicola
	 * @param Long idInventario
	 * @return
	 */
	public InventarioVo getInventarioVo(Integer codEdicola, Long idInventario);
	
	/**
	 * @param Long idInventario 
	 */
	public void deleteInventario(Long idInventario);
	
	/**
	 * @param idInventario
	 * @return
	 */
	public BigDecimal getTotaleInventario(Long idInventario);
	
	/**
	 * @param coddl
	 * @param codEdicola
	 * @param dataUscitaDa
	 * @param dataStorico
	 * @param escludiPubblicazioniSenzaPrezzo 
	 * @param filtroContoDeposito 
	 * @return
	 */
	public List<InventarioPresuntoDto> getInventarioPresunto(Integer[] coddl, Integer[] codEdicola, Date dataUscitaDa, Date dataStorico, Integer gruppoSconto, Types.ContoDepositoType filtroContoDeposito, Types.TipoPubblicazioneType filtroTipoPubblicazione, Boolean escludiPubblicazioniSenzaPrezzo);
	
}
