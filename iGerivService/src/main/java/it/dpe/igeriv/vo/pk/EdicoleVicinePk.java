package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class EdicoleVicinePk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "codgr9205")
	private Integer codiceGruppo;
	@Column(name = "crivw9205")
	private Integer codiceEdicola;
}
