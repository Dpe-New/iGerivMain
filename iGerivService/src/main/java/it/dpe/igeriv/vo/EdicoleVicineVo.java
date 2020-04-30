package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.EdicoleVicinePk;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9205", schema = "")
public class EdicoleVicineVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private EdicoleVicinePk pk;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crivw9205", insertable = false, updatable = false, referencedColumnName = "crivw9206")
	private AbbinamentoEdicolaDlVo edicola;
}
