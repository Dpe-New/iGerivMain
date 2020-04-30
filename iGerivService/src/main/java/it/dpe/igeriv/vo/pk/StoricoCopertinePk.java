package it.dpe.igeriv.vo.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class StoricoCopertinePk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "coddl9607")
	private Integer codDl;
	@Column(name = "idtn9607")
	private Integer idtn;
	
	@Override
	public String toString() {
		return getCodDl() + "|" + getIdtn();
	}
}
