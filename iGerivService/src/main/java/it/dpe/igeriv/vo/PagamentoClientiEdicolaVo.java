package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.PagamentoClientiEdicolaPk;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9632", schema = "")
public class PagamentoClientiEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private PagamentoClientiEdicolaPk pk;
	@Column(name = "paga9632")
	private Boolean pagato;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ccli9632", updatable = false, insertable = false, nullable = true, referencedColumnName = "ccli9305")
	private ClienteEdicolaVo cliente;
	@Column(name = "imto9632")
	private BigDecimal importoTotale;
	@Column(name = "ddoc9632")
	private Timestamp dataDocumento;
	@Column(name = "tipe9632")
	private Integer tipoProdottiInEstrattoConto;
	@Column(name = "ecxmlf9632")
	@Basic(fetch = FetchType.LAZY)
	private byte[] estrattoContoXml;
	@Column(name = "ndoc9632")
	private Integer numeroDocumento;
}
