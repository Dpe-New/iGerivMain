package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class ArgomentoPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9217")
	private Integer codDl;
	@Column(name = "segm9217")
	private Integer segmento;
	
	@Override
	public String toString() {
		return getCodDl() + "|" + getSegmento();
	}

}
