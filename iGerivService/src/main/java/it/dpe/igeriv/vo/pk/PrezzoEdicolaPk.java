package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class PrezzoEdicolaPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9617")
	private Integer codFiegDl;
	@Column(name = "idtn9617")
	private Integer idtn;
	@Column(name = "gs9617")
	private Integer gruppoSconto;

}
