package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class NoteRivenditaPerCpuPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name ="crivw9641")
	private Integer codEdicola;
	@Column(name ="cpu9641")
	private Integer cpu;
}
