package it.dpe.jms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VenditeJmsReplyMessage extends BaseJmsMessage {
	private static final long serialVersionUID = 5702060464836032144L;
	private final Long codVendita;
	private final Integer codFornitore;
	private final String operation;
	
	public VenditeJmsReplyMessage(Long codVendita, Integer codFornitore, String operation, String message) {
		super(message);
		this.codVendita = codVendita;
		this.codFornitore = codFornitore;
		this.operation = operation;
	}
}
