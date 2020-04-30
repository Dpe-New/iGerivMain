package it.dpe.igeriv.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ContoDepositoDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private String titolo;
	private Integer codicePubblicazione;
	private String numeroCopertina;
	private Timestamp dataUscita;
	private Integer quantitaContoDeposito;
	private BigDecimal prezzoCopertina;
	private BigDecimal prezzoEdicola;
	private Integer resoBolla;
	private Integer resoFuoriVoce;
	private Integer resoRichiamoPersonalizzato;
	private String immagine;
	private Timestamp dataFatturazionePrevista;
	
	public Integer getVenduto() {
		int q1 = (getQuantitaContoDeposito() != null) ? getQuantitaContoDeposito() : 0;
		int q2 = (getQuantitaReso() != null) ? getQuantitaReso() : 0;
		return q1 - q2;
	}
	
	public BigDecimal getImporto() {
		int q1 = (getVenduto() != null) ? getVenduto() : 0;
		BigDecimal q2 = (getPrezzoEdicola() != null) ? getPrezzoEdicola() : new BigDecimal(0);
		return new BigDecimal(q1).multiply(q2);
	}
	
	public BigDecimal getImportoL() {
		int q1 = (getVenduto() != null) ? getVenduto() : 0;
		BigDecimal q2 = (getPrezzoCopertina() != null) ? getPrezzoCopertina() : new BigDecimal(0);
		return new BigDecimal(q1).multiply(q2);
	}
	
	public Integer getQuantitaReso() {
		Integer r1 = (getResoBolla() != null) ? getResoBolla() : 0;
		Integer r2 = (getResoFuoriVoce() != null) ? getResoFuoriVoce() : 0;
		Integer r3 = (getResoRichiamoPersonalizzato() != null) ? getResoRichiamoPersonalizzato() : 0;
		return r1 + r2 + r3;
	}
	
	public String getFake() {
		return "";
	}

}
