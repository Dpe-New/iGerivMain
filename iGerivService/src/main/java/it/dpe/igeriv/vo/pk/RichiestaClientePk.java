package it.dpe.igeriv.vo.pk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class RichiestaClientePk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "crivw9310")
	private Integer codEdicola;
	@Column(name = "ccli9310")
	private Long codCliente;
	@Column(name = "ipro9310")
	private Integer provenienza;
	@Column(name = "daor9310")
	private Timestamp dataInserimento;
	@Column(name = "codl9310")
	private Integer codDl;
	@Column(name = "idtn9310")
	private Integer idtn;
	
	@Override
	public String toString() {
		return getCodEdicola() + "|" + getCodCliente() + "|" + getProvenienza() + "|" + getDataInserimento() + "|" + getCodDl() + "|" + getIdtn();
	}
	
	@Override
	public int hashCode() {
		return getCodEdicola() + getCodDl() + getIdtn() + getProvenienza();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof RichiestaClientePk) {
			RichiestaClientePk obj2 = (RichiestaClientePk)obj;
			return this.toString().equals(obj2.toString());
		}
		return super.equals(obj);
	}

}
