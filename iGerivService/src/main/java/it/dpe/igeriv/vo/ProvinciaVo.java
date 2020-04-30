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
@NamedQueries({ @NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_PROVINCIE, query = "from ProvinciaVo vo") })
@Table(name = "tbl_9220", schema = "")
public class ProvinciaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "provi9220")
	private Integer codProvincia;
	@Column(name = "descr9220")
	private String descrizione;
}
