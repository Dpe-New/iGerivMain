package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

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
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_DATA_FESTIVITA, query = "from AbbonatiDateFestivitaVo vo where vo.dataFestivita = :dataFestivita")
})
@Table(name = "tbl_9802", schema = "")
public class AbbonatiDateFestivitaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "datfe9802")
	private Timestamp dataFestivita;
}
