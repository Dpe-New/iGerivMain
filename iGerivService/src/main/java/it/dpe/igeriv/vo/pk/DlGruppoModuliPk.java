package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class DlGruppoModuliPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "codgr9212")
	private Integer codGruppo;
	@Column(name = "coddl9212")
	private Integer codDl;
}
