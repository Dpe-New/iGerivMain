package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.MessaggiBollaPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella dei messaggi in bolla.
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@NamedQueries( {
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_MESSAGGI_BOLLA, query = "from MessaggiBollaVo vo where vo.pk.codFiegDl in (:codFiegDl) and vo.pk.codEdicola in (:codEdicola) and vo.pk.dtBolla = :dtBolla and vo.pk.tipoBolla = :tipoBolla order by vo.pk.codEdicola, vo.pk.tipoMessaggio, vo.pk.progressivo")
})
@Table(name = "tbl_9615", schema = "")
public class MessaggiBollaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private MessaggiBollaPk pk;
	@Column(name = "messa9615")
	private String messaggio;
}
