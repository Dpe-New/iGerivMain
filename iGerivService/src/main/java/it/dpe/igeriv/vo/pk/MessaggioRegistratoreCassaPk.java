package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class MessaggioRegistratoreCassaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "codreg9712")
	private Integer codRegCassa;
	@Column(name = "crivw9712")
	private Integer codEdicola;
}
