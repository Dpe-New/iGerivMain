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
 * Tabella località
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries({ @NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_TIPO_LOCALITA, query = "from TipoLocalitaVo vo") })
@Table(name = "tbl_9221", schema = "")
public class TipoLocalitaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "tiplo9221")
	private Integer tipoLocalita;
	@Column(name = "descr9221")
	private String descrizione;
}
