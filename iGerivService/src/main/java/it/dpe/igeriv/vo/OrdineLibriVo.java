package it.dpe.igeriv.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9400", schema = "")
public class OrdineLibriVo extends BaseVo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "numord9400")
	private Long numeroOrdine;
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="numord9401")
	private List<OrdineLibriDettVo> listDettaglioOrdine;
	
	@Column(name = "coddl9400")
	private Integer codDl;
	
	@Column(name = "crivw9400")
	private Integer codDpeWebEdicola;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9400", updatable = false, insertable = false, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo anagraficaEdicolaVo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ccli9400", insertable = false, updatable = false, referencedColumnName = "ccli9305")
	private ClienteEdicolaVo cliente;
	
	@Column(name = "ccli9400")
	private Long codCliente;
	
	@Column(name = "dtaper9400")
	private Date dataAperturaOrdine;
	
	@Column(name = "dtcomp9400")
	private Date dataChiusuraOrdine;
	
	@Column(name = "note9400")
	private String note;
	@Column(name = "ordtxt9400")
	private String numeroOrdineTxt;
	
	
	
	
}
