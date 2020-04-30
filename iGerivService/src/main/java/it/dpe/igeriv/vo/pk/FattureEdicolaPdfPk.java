package it.dpe.igeriv.vo.pk;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class FattureEdicolaPdfPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9646")
	private Integer codFiegDl;
	@Column(name = "crivw9646")
	private Integer codEdicola;
	@Column(name = "dafat9646")
	private Date dataFattura;
	@Column(name = "nufat9646")
	private Integer numeroFattura;
}
