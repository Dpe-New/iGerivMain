package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.PeriodicitaPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Periodicità
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries({ 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_PERIODICITA, query = "from PeriodicitaVo vo") 
})
@Table(name = "tbl_9216", schema = "")
public class PeriodicitaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private PeriodicitaPk pk;
	@Column(name = "descr9216")
	private String descrizione;
}
