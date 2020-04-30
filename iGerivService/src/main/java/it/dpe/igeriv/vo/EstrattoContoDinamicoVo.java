package it.dpe.igeriv.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mromano
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9637", schema = "")
public class EstrattoContoDinamicoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id9637")
	private Long id;
	@Column(name = "coddl9637")
	private Integer codFiegDl;
	@Column(name = "crivw9637")
	private Integer codEdicola;
	@Column(name = "prog9637")
	private Integer progressivo;
	@Column(name = "dmo9637")
	private Timestamp dataMovimento;
	@Column(name = "cmo9637")
	private Integer causale;
	@Column(name = "nmo9637")
	private String numeroMovimento;
	@Column(name = "impde9637")
	private BigDecimal importoDare;
	@Column(name = "impae9637")
	private BigDecimal importoAvere;
	@Column(name = "mes9637")
	private String descrizione;
}
