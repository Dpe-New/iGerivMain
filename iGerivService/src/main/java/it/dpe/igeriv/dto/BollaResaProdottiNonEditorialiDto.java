package it.dpe.igeriv.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BollaResaProdottiNonEditorialiDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long idDocumento;
	private String numeroDocumento;
	private Long codProdottoInterno;
	private String codiceProdottoFornitore;
	private Timestamp dataRegistrazione;
	private Integer quantita;
	private Integer codEdicolaDl;
	private String stato;
	private Integer codFornitore;
	private Long codiceContabileCliente;
	private Integer formazionePacco;
}
