package it.dpe.jms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VenditaDetailJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = 2828157294071103820L;
	private String progressivo;
	private String idtn;
	private Integer codFiegDlPubblicazione;
	private String importo;
	private String titolo;
	private String sottoTitolo;
	private String numeroCopertina;
	private Integer quantita;
}
