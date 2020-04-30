package it.dpe.igeriv.vo.pk;

import it.dpe.igeriv.util.DateUtilities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class MessaggioPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9601")
	private Integer codFiegDl;
	@Column(name = "datam9601")
	private Timestamp dtMessaggio;
	@Column(name = "tdest9601")
	private Integer tipoDestinatario;
	@Column(name = "desta9601")
	private Integer destinatarioA;
	@Column(name = "destb9601")
	private Integer destinatarioB;

	@Override
	public String toString() {
		return getCodFiegDl() + "|" + DateUtilities.getTimestampAsString(getDtMessaggio(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS) + "|" + getTipoDestinatario() + "|" + getDestinatarioA() + "|" + getDestinatarioB();
	}

}
