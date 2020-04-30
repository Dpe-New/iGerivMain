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
 * Tabella rilevamenti
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "TBL_9639", schema = "")
public class RilevamentiVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codri9639")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9639", allocationSize = 1)
	private Integer codiceRilevamento;
	@Column(name = "coddl9639")
	private Integer codFiegDl;
	@Column(name = "crivw9639")
	private Integer codEdicola;
	@Column(name = "datbr9639")
	private Timestamp dtBolla;
	@Column(name = "tipbr9639")
	private String tipoBolla;
	@Column(name = "posiz9639")
	private Integer posizioneRiga;
	@Column(name = "idtn9639")
	private Integer idtn;
	@Column(name = "qdist9639")
	private Integer distribuito;
	@Column(name = "qrile9639")
	private Integer rilevamento;
	@Column(name = "iritr9639")
	private Integer indicatoreTrasmessoDl;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9639", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9639", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo copertina;
}
