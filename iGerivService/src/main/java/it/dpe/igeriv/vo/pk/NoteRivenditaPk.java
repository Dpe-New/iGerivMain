package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class NoteRivenditaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name ="crivw9640")
	private Integer codEdicola;
	@Column(name ="idtn9640")
	private Integer idtn;
}
