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
 * Tabella di importazione dei ritiri Rtae. 
 * 
 * @author romanom
 *
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9804", schema = "")
@NamedQueries({
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ESPORTAZIONE_RTAE_BY_ID, query = "from RitiriRtaeVo vo where vo.codiceBarre = :barcode and vo.codFiegDl = :codFiegDl and vo.codiceRivenditaDl = :codiceRivenditaDl")
})
public class RitiriRtaeVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idespo9804")
	private Integer idEsportazione;
	@Column(name = "fieg9804")
	private Integer codFiegDl;
	@Column(name = "pdvdl9804")
	private Integer codiceRivenditaDl;
	@Column(name = "crivw9106")
	private Integer codEdicola;
	@Column(name = "barcode9804")
	private String codiceBarre;
	@Column(name = "ediz9804")
	private String codiceEdizione;
	@Column(name = "corit9804")
	private Long copieRitirate;
}
