package it.dpe.jms.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Bolla Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class BollaProdottiVariJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = -1982522220835147587L;
	private Long idDocumento;
	private String numeroDocumento;
	private Integer codEdicolaDl;
	private String numeroOrdine;
	private Integer codiceFornitore;
	private Timestamp dataDocumento;
	private List<BollaProdottiVariDettaglioJmsMessage> dettagli;
}
