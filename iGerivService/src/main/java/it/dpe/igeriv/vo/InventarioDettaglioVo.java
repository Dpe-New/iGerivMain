package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.vo.pk.InventarioDettaglioPk;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9751", schema = "")
public class InventarioDettaglioVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private InventarioDettaglioPk pk;
	@Column(name = "titolo9751")
	private String titolo;
	@Column(name = "sottot9751")
	private String sottotitolo;
	@Column(name = "numero9751")
	private String numeroCopertina;
	@Column(name = "dataus9751")
	private Timestamp dataUscita;
	@Column(name = "prezzo9751")
	private BigDecimal prezzoCopertina;
	@Column(name = "preedi9751")
	private BigDecimal prezzoEdicola;
	@Column(name = "quanti9751")
	private Integer quantita;
	@Column(name = "contod9751")
	private Boolean isContoDeposito;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "idinve9751", updatable = false, insertable = false, referencedColumnName = "idinve9750")
	private InventarioVo inventario;
	@Transient
	@Getter(AccessLevel.NONE)
	private BigDecimal importo;
	@Transient
	@Getter(AccessLevel.NONE)
	private BigDecimal importoLordo; 
	@Column(name = "scadut9751")
	private Boolean isScaduta;
	
	public BigDecimal getImporto() {
		importo = new BigDecimal(0);
		if (getQuantita() != null && getPrezzoEdicola() != null) {
			importo = new BigDecimal(getQuantita()).multiply(getPrezzoEdicola());
		}
		return importo;
	}
	
	public BigDecimal getImportoLordo() {
		importoLordo = new BigDecimal(0);
		if (getQuantita() != null && getPrezzoEdicola() != null) {
			importoLordo = new BigDecimal(getQuantita()).multiply(getPrezzoCopertina());
		}
		return importoLordo;
	}
	
	public String getSortCriteria() {
		return getIsContoDeposito().toString() + getIsScaduta().toString() + getTitolo();
	}
	
	public String getDataUscitaFormat() {
		if (getDataUscita() != null) {
			return DateUtilities.getTimestampAsString(getDataUscita(), DateUtilities.FORMATO_DATA_SLASH);
		}
		return "";
	}
}
