package it.dpe.jms.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class VenditaJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = -1385722404039435502L;
	private String crivw;
	private String importoTotale;
	private List<VenditaDetailJmsMessage> dettagli;
}
