package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella località
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries({ @NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_LOCALITA, query = "from LocalitaVo vo") })
@Table(name = "tbl_9219", schema = "")
public class LocalitaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "local9219")
	private Integer codLocalita;
	@Column(name = "descr9219")
	private String descrizione;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "provi9219", updatable = false, insertable = false, referencedColumnName = "provi9220")
	private ProvinciaVo provinciaVo;
}
