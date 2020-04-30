package it.dpe.igeriv.dto;

import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.google.common.base.Strings;

/**
 * @author romanom
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class IdVenditaProdottNonEditorialeDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long codVendita;
	private Long idDocumentoProdottiVari;
	private Integer trasferitaGestionale;
	private Timestamp dataVendita;
	private Timestamp dataEstrattoConto;
	private Long codCliente;
	private String nome;
	private String cognome;
	@Getter(AccessLevel.NONE)
	private String idScontrino;
	private Boolean fatturaEmessa;
	@Getter(AccessLevel.NONE)
	private Boolean fatturaContoUnico; 
	@Getter(AccessLevel.NONE)
	private Integer numeroFattura;
	
	public String getIdScontrino() {
		return idScontrino == null ? "" : idScontrino;
	}

	public Boolean getFatturaContoUnico() {
		return fatturaContoUnico == null ? false : fatturaContoUnico;
	}

	public Integer getNumeroFattura() {
		return numeroFattura == null ? 0 : numeroFattura;
	}
	
	public String getNomeCognomeCliente() {
		return (!Strings.isNullOrEmpty(getNome()) ? getNome() : "")  + (!Strings.isNullOrEmpty(getCognome()) ? " " + getCognome() : "");
	}
	
}
