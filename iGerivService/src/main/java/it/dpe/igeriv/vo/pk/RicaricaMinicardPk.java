package it.dpe.igeriv.vo.pk;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Embeddable
public class RicaricaMinicardPk implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "idtes9853")
	private Integer idTessera;
	@Column(name = "datri9853")
	private Timestamp idRicarica;
	
	@Override
	public String toString() {
		return idTessera + "|" + idRicarica;
	}
}
