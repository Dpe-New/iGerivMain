package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.BollaStatisticaStoricoPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9638", schema = "")
public class BollaStatisticaStoricoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private BollaStatisticaStoricoPk pk;
	@Column(name = "qforn9638")
	private Integer fornito;
	@Column(name = "qreso9638")
	private Integer reso;
}
