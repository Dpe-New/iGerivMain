package it.dpe.jms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Bolla Prodotti Non Editoriali Dettaglio
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class BollaProdottiVariDettaglioJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = 3682824137824401391L;
	private Long idDocumento;
	private Integer progressivo;
	private Integer codiceCausale;
	private Integer magazzinoDa;
	private Integer magazzinoA;
	private Integer quantita;
	private Float prezzo;
	private Float scontoValore;
	private Float scontoPercentuale;
	private String codiceProdottoFornitore;
}
