package it.dpe.igeriv.vo.pk;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class EstrattoContoEdicolaPdfPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9633")
	private Integer codFiegDl;
	@Column(name = "crivw9633")
	private Integer codEdicola;
	@Column(name = "datec9633")
	private Date dataEstrattoConto;
}
