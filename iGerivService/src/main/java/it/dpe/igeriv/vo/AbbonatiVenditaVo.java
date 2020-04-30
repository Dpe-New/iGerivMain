package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.AbbonatiVenditaPk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_VENDITA_ABBONATI, query = "from AbbonatiVenditaVo vo where vo.pk.idTessera = :idTessera and vo.pk.dataProdottoVenduto = :dataProdottoVenduto")
})
@Table(name = "tbl_9803", schema = "")
public class AbbonatiVenditaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private AbbonatiVenditaPk pk;
	@Column(name = "daven9803")
	private Timestamp dataVendita;
	@Column(name = "codzo9803")
	private String codZona;
	@Column(name = "copie9803")
	private Integer copieConsegnate;
	@Column(name = "coriv9803")
	private Integer codRivenditaDl;
	@Column(name = "itgp9803")
	private Integer recordTrasferitoDl;
	@Column(name = "dtgp9803")
	private Timestamp dataTrasferimentoDl;
	@Column(name = "itges9803")
	private Integer recordTrasferitoGestionale;
	@Column(name = "dtges9803")
	private Timestamp dataTrasferimentoGestionale;
}
