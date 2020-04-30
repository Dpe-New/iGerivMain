package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author romanom
 *
 */
@Getter
@Setter
@Entity
@NamedQueries( {
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_MODULI_INPUT, query = "from ModuloInputVo vo where vo.abilitato = true order by vo.priorita asc")
})
@Table(name = "tbl_9211", schema = "")
public class ModuloInputVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codmo9211")
	private Integer codice;
	@Column(name = "desmo9211")
	private String descrizione;
	@Column(name = "clasmo9211")
	private String classe;
	@Column(name = "expmo9211")
	private String espressioneRegolare;
	@Column(name = "abimo9211")
	@Getter(AccessLevel.NONE)
	private Boolean abilitato;
	@Column(name = "primo9211")
	private Integer priorita;
	@Transient
	@Getter(AccessLevel.NONE)
	private Pattern pattern;
	
	public Boolean getAbilitato() {
		return abilitato == null ? false : abilitato;
	}

	public Pattern getPattern() {
		return pattern = (pattern == null) ? (espressioneRegolare != null) ? Pattern.compile(espressioneRegolare) : pattern : pattern;
	}
}
