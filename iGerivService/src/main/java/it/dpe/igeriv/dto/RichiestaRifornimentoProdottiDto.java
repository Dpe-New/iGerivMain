package it.dpe.igeriv.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RichiestaRifornimentoProdottiDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long codRichiestaRifornimento;
	private Long codProdottoInterno;
	private String codiceProdottoFornitore;
	private Timestamp dataRichiesta;
	private Integer quatitaRichiesta;
	private String note;
	private Integer codEdicolaDl;
	private String stato;
	private String rispostaDl;
	private Timestamp dataUltAggiornamento;
	private Integer codFornitore;
	private Long codiceContabileCliente;
	private Integer formazionePacco;
}
