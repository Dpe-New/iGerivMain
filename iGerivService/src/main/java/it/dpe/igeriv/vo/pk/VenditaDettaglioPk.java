package it.dpe.igeriv.vo.pk;

import it.dpe.igeriv.vo.VenditaVo;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenditaDettaglioPk extends BasePk {
	private static final long serialVersionUID = 1L;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "idven9626", referencedColumnName = "idven9625")
	private VenditaVo venditaVo;
	@Column(name = "progv9626")
	private Integer progressivo;
	
	public Long getCodVendita() {
		return getVenditaVo().getCodVendita();
	}
	
	@Override
	public String toString() {
		return getVenditaVo().getCodVendita() + "|" + getProgressivo();
	}

}
