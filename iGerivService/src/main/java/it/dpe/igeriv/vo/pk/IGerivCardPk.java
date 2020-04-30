package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class IGerivCardPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "crivw9307")
	private Integer codEdicola;
	@Column(name = "card9307")
	private String card;
}
