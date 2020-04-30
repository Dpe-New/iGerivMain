package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.DlGruppoModuliPk;

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
@Table(name = "tbl_9212", schema = "")
public class DlGruppoModuliVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private DlGruppoModuliPk pk;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "codgr9212", referencedColumnName = "codgr9209", insertable = false, updatable = false)
	private GruppoModuliVo gruppoModuli;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "coddl9212", referencedColumnName = "coddl9107", insertable = false, updatable = false)
	private AnagraficaAgenziaVo dl;
}
