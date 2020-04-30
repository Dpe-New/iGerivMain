package it.dpe.igeriv.vo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9780", schema = "")
public class BonusInvitaUnCollegaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idbonu9780")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9780", allocationSize = 1)
	private Long codBonus;
	@Column(name = "crivwr9780")
	private Integer codDpeWebEdicola;
	@Column(name = "coddln9780")
	private Integer codDl;
	@Column(name = "cridln9780")
	private Integer codEdicolaDl;
	@Column(name = "emailn9780")
	private String email;
	@Column(name = "ragsoc9780")
	private String ragioneSociale;
	@Column(name = "daric9780")
	private Timestamp dataRichiesta;
	@Column(name = "datat9780")
	private Timestamp dataAttivazione;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivwr9780", updatable = false, insertable = false, referencedColumnName = "crivw9206")
	private AbbinamentoEdicolaDlVo edicola;
}
