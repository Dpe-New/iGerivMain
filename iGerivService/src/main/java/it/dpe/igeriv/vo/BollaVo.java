package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.BollaPk;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author romanom
 *
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9609", schema = "")
public class BollaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private BollaPk pk;
	@Column(name = "idtn9609")
	private Integer idTestataNumero;
	@Column(name = "cpu9609")
	private Integer cpuDl;
	@Column(name = "num9609")
	@Getter(AccessLevel.NONE)
	private String numeroPubblicazione;
	@Column(name = "titolo9609")
	private String titolo;
	@Column(name = "sottot9609")
	private String sottoTitolo;
	@Column(name = "ipacc9609")
	private Integer tipoProdotto;
	@Column(name = "icomp9609")
	private Integer indicatoreComponentePaccotto;
	@Column(name = "plorl9609")
	private BigDecimal prezzoLordo;
	@Column(name = "priva9609")
	private BigDecimal percentualeIva;
	@Column(name = "compl9609")
	private BigDecimal compensoCompiegamento;
	@Column(name = "ivalo9609")
	private Integer indicatoreValorizzare;
	@Column(name = "indpv9609")
	private String indicatorePrezzoVariato;
	@Column(name = "irr9609")
	private Integer indicatoreRichiamoResa;
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumns({
		@JoinColumn(name = "coddl9609", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9609", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo storicoCopertineVo;
	@Column(name = "riga9609")
	private Integer riga;
	@Column(name = "pubnu9609")
	private Boolean pubblicazioneNonUscita;

	public String getNumeroPubblicazione() {
		return numeroPubblicazione != null ? numeroPubblicazione.trim() : numeroPubblicazione;
	}
	
}
