package it.dpe.jms.dto;

import java.util.List;

import it.dpe.jms.visitor.JmsMessageVisitor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VenditaJmsMessage extends BaseJmsMessage implements VisitableJmsMessage {
	private static final long serialVersionUID = 7317638181077803647L;
	private String operation;
	private Integer codFiegDl;
	private Integer crivDl;
	private Long idVendita;
	private String correlationId;
	private List<VenditeJmsMessage> vendite;
	
	public VenditaJmsMessage(String operation, Integer codFiegDl, Integer crivDl, Long idVendita, String correlationId, List<VenditeJmsMessage> vendite) {
		this.operation = operation;
		this.codFiegDl = codFiegDl;
		this.crivDl = crivDl;
		this.idVendita = idVendita;
		this.correlationId = correlationId;
		this.vendite = vendite;
	}

	@Override
	public void accept(JmsMessageVisitor visitor) {
		visitor.visit(this);
	}
}
