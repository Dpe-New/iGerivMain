package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class CorrispondenzaMenuPorfiloPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "CODGR9210")
	private Integer idGruppo;
	@Column(name = "CODMO9210")
	private Integer idModulo;
}
