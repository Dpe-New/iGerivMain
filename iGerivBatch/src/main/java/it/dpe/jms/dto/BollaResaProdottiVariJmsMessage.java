package it.dpe.jms.dto;

import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BollaResaProdottiVariJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = -2684186292250199506L;
	private Long idDocumento;
	private String numeroDocumento;
	private Long codProdottoInterno;
	private Integer codiceProdottoFornitore;
	private Timestamp dataRegistrazione;
	@Getter(AccessLevel.NONE)
	private Integer quantita;
	private String note;
	private Integer codEdicolaDl;
	private String stato;
	private String rispostaDl;
	private Timestamp dataUltAggiornamento;
	private Integer codFornitore;
	private Long codiceContabileCliente;
	private String correlationId;
	private Integer formazionePacco;
	
	public Integer getQuantita() {
		return quantita == null ? 0 : quantita;
	}

}
