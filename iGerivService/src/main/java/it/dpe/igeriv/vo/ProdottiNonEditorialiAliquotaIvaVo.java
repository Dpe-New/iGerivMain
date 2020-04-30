package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mromano
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9547", schema = "")
public class ProdottiNonEditorialiAliquotaIvaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "aliq9547")
	private Integer aliquota;
	@Column(name = "repa9547")
	private String reparto;
	@Column(name = "disa9547")
	private Boolean disabilitato;

	@Override
	public String toString() {
		return getAliquota().toString();
	}

}
