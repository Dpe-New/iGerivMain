package it.dpe.igeriv.vo;

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

/**
 * Tabella arretrati
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "TBL_9634", schema = "")
public class ArretratiVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codri9634")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9634", allocationSize = 1)
	private Integer codiceArretrato;
	@Column(name = "coddl9634")
	private Integer codFiegDl;
	@Column(name = "crivw9634")
	private Integer codEdicola;
	@Column(name = "datbc9634")
	private Timestamp dtBolla;
	@Column(name = "tipbc9634")
	private String tipoBolla;
	@Column(name = "posiz9634")
	private Integer posizioneRiga;
	@Column(name = "idtn9634")
	private Integer idtn;
	@Column(name = "quant9634")
	private Integer quantita;
	@Column(name = "spuns9634")
	private Boolean confermaSi;
	@Column(name = "spunn9634")
	private Boolean confermaNo;
	@Column(name = "iortr9634")
	private Integer indicatoreTrasmessoDl;
	@Column(name = "devas9634")
	private Timestamp dataEvasione;
	@Column(name = "dsped9634")
	private Timestamp dataSpedizioneConferma;
	@Column(name = "quane9634")
	private Integer quantitaEvasa;
	@Column(name = "note9634")
	private String note;
	@Column(name = "cpu9634")
	private Integer cpu;
	@Column(name = "num9634")
	private String numeroCopertina;
	@Column(name = "dus9634")
	private Timestamp dataUscita;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9634", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9634", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo copertina;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9634", updatable = false, insertable = false, referencedColumnName = "coddl9606"),
		@JoinColumn(name = "cpu9634", updatable = false, insertable = false, referencedColumnName = "cpu9606")
	})
	private AnagraficaPubblicazioniVo anagraficaPubblicazione;
}
