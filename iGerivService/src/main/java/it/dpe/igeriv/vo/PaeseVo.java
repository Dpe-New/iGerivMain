package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella peasi
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries({ @NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_PAESI, query = "from PaeseVo vo order by vo.descrizione asc") })
@Table(name = "tbl_9218", schema = "")
public class PaeseVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "paesi9218")
	private Integer codPaese;
	@Column(name = "descr9218")
	private String descrizione;
	@Column(name = "ordine9218")
	private Integer ordine;
}
