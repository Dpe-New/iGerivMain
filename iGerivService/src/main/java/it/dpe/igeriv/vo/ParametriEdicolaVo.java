package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ParametriEdicolaPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella parametri edicola igeriv
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9710", schema = "")
public class ParametriEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ParametriEdicolaPk pk;
	@Column(name = "valpar9710")
	private String value;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codpar9710", insertable = false, updatable = false, referencedColumnName = "codpar9709")
	private ParametriVo parametro;
	
	@Override
	public boolean equals(Object arg0) {
		if (arg0 != null) {
			return ((ParametriEdicolaVo) arg0).getPk().equals(this.getPk());
		}
		return super.equals(arg0);
	}
	
	
}
