package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.ArgomentoPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Argomenti
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries({ 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ARGOMENTI, query = "from ArgomentoVo vo where vo.pk.codDl = :codDl order by vo.descrizione asc"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ALL_ARGOMENTI, query = "from ArgomentoVo") 
})
@Table(name = "tbl_9217", schema = "")
public class ArgomentoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ArgomentoPk pk;
	@Column(name = "descr9217")
	private String descrizione;
}
