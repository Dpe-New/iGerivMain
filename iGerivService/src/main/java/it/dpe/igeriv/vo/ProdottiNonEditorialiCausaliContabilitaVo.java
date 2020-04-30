package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiCausaliContabilitaPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TBL_9509", schema = "")
public class ProdottiNonEditorialiCausaliContabilitaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ProdottiNonEditorialiCausaliContabilitaPk pk;
	@Column(name = "CCDE9509")
	private String descrizione;
	@Column(name = "CODIV19509")
	private Integer tipoDocumento;
	@Column(name = "CODIV29509")
	private Integer tipoIva;
	@Column(name = "CODIV39509")
	private Integer tipoNumerazione;
	@Column(name = "COSO9509")
	private Integer coso;
	@Column(name = "PERC9509")
	private Integer percentualeIva;
	@Column(name = "PTA9509")
	private Integer tipologiaMovimentiEstrattoConto;
}
