package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class EdicolaDlPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "crivdl9790")
	private Integer codRivDl;
	@Column(name = "coddl9790")
	private Integer codDl;
}
