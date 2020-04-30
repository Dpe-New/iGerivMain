package it.dpe.igeriv.dto;

import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.xpath.operations.Bool;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdineLibriDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	private Long numeroOrdine;
	private List<OrdineLibriDettVo> listDettaglioOrdine;
	private Integer codDpeWebEdicola;
	private AnagraficaEdicolaVo anagraficaEdicolaVo;
	private ClienteEdicolaVo cliente;
	private Long codCliente;
	private Date dataAperturaOrdine;
	private Date dataChiusuraOrdine;
	private String note;
	private String numeroOrdineTxt;
	
	
	
	private BigDecimal prezzoTotale;
	private Integer libriTotali;
	
	private String dettaglioTracking;
	private String dettaglioPageConsegnaLibri;
	public Boolean dettaglioConsegnaLibri;
	
	public Boolean tracking_attivo;   // TRUE se stato diverso da 3 
	public Boolean tracking_attivo_parziale;   // TRUE se libri interni all'ordine sono stati gia consegnati al cliente
	private Date dataConsegnaAlCliente;
	
}
