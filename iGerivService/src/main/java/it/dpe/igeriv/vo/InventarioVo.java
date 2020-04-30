package it.dpe.igeriv.vo;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9750", schema = "")
public class InventarioVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idinve9750")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9750", allocationSize = 1)
	private Long idInventario;
	@Column(name = "crivw9750")
	private Integer codEdicola;
	@Column(name = "datinv9750")
	private Date dataInventario;
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true, targetEntity = InventarioDettaglioVo.class, mappedBy = "inventario")
	private List<InventarioDettaglioVo> dettagli;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9750", updatable = false, insertable = false, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola;
}
