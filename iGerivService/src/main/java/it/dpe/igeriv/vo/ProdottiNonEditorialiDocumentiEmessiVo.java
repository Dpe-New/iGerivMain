package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivConstants;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9551", schema = "")
public class ProdottiNonEditorialiDocumentiEmessiVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "iddoc9551")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName=IGerivConstants.SEQ_REPORT_BOLLE_RESA_PRODOTTI_VARI, allocationSize = 1)
	private Long idReport;
	@Column(name = "iddoe9551")
	private Long idDocumento;
	@Column(name = "tidoc9551")
	private Integer tipoDocumento;
	@Column(name = "crivw9551")
	private Integer codEdicola;
	@Column(name = "codut9551")
	private String codUtente;
	@Column(name = "cforn9551")
	private Long codFornitore;
	@Column(name = "nfifa9551")
	private String nomeFile;
	@Column(name = "dadoc9551")
	private Timestamp dataDocumento;
	@Column(name = "nudoc9551")
	private Integer numeroDocumento;
	@Column(name = "docue9551")
	private byte[] documento;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "cforn9551", insertable = false, updatable = false, referencedColumnName = "cforn9500"),
		@JoinColumn(name = "crivw9551", insertable = false, updatable = false, referencedColumnName = "crivw9500")
	})
	private ProdottiNonEditorialiFornitoreVo fornitore;
}
