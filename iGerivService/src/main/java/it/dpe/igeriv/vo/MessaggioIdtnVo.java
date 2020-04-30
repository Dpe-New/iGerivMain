package it.dpe.igeriv.vo;

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
@Table(name = "tbl_9642", schema = "")
public class MessaggioIdtnVo extends BaseVo {
	private static final long serialVersionUID = -2213194396935668310L;
	@Id
	@Column(name = "idmsgidtn9642")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQUENZA_9642", allocationSize = 1)
	private Integer idMessaggioIdtn;
	@Column(name = "coddl9642")
	private Integer codDl;
	@Column(name = "idtn9642")
	private Integer idtn;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coddl9642", updatable = false, insertable = false, referencedColumnName = "coddl9107")
	private AnagraficaAgenziaVo anagraficaAgenziaVo;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "coddl9642", updatable = false, insertable = false, referencedColumnName = "coddl9607"), @JoinColumn(name = "idtn9642", updatable = false, insertable = false, referencedColumnName = "idtn9607") })
	private StoricoCopertineVo storicoCopertineVo;
	@Column(name = "testo9642")
	private String testo;
	@Column(name = "aleg19642")
	private String attachmentName1;
	@Column(name = "aleg29642")
	private String attachmentName2;
	@Column(name = "aleg39642")
	private String attachmentName3;
}
