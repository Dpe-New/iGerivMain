package it.dpe.igeriv.vo.pk;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class VenditaMinicardPk implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "idtes9854")
	private Integer idTessera;
	@Column(name = "dapro9854")
	private Date dataProdottoVenduto;
	
	@Override
	public String toString() {
		return idTessera + "|" + dataProdottoVenduto;
	}
	
}
