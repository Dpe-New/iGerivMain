package it.dpe.igeriv.vo;

import it.dpe.igeriv.dto.VisitorDto;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.EstrattoContoEdicolaDettaglioPk;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

/**
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@NamedQueries({ @NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ESTRATTO_DETTAGLIO_CONTO_EDICOLA, query = "from EstrattoContoEdicolaDettaglioVo vo where vo.pk.codFiegDl = :codFiegDl and vo.pk.codEdicola = :codEdicola and vo.pk.dataEstrattoConto = :dataEstrattoConto order by vo.pk.progressivo asc") })
@Table(name = "tbl_9631", schema = "")
public class EstrattoContoEdicolaDettaglioVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private EstrattoContoEdicolaDettaglioPk pk;
	@Column(name = "tre9631")
	private Integer tipoRecord;
	@Column(name = "dmo9631")
	private Timestamp dataMovimento;
	@Column(name = "nmo9631")
	private String numeroMovimento;
	@Column(name = "tmo9631")
	private Integer tipoMovimento;
	@Column(name = "impde9631")
	private BigDecimal importoDare;
	@Column(name = "impae9631")
	private BigDecimal importoAvere;
	@Column(name = "mes9631")
	private String note;
	@Transient
	private Boolean isBold;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9631", insertable = false, updatable = false, referencedColumnName = "coddl9630"),
		@JoinColumn(name = "crivw9631", insertable = false, updatable = false, referencedColumnName = "crivw9630"),
		@JoinColumn(name = "datec9631", insertable = false, updatable = false, referencedColumnName = "datec9630")
	})
	private EstrattoContoEdicolaVo estrattoConto;

	public String getFake() {
		return "";
	}
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
}
