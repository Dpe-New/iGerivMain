package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class AbbinamentiIdtnInforivPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9605")
	private Integer codDl;
	@Column(name = "idtni9605")
	private String idtnInforete;
}
