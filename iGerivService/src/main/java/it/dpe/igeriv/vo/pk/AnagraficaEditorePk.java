package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class AnagraficaEditorePk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9109")
	private Integer codFiegDl;
	@Column(name = "coded9109")
	private Integer codEditore;
}
