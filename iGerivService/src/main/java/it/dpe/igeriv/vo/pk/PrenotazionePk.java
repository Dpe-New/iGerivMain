package it.dpe.igeriv.vo.pk;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class PrenotazionePk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9129")
	private Integer codDl;
	@Column(name = "cpu9129")
	private Integer codicePubblicazione;
	@Column(name = "crivw9129")
	private Integer codEdicola;
	@Column(name = "dari9129")
	private Date dataRichiesta;
}
