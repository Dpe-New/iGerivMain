package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.BollaResaPk;

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
@Table(name = "tbl_9619", schema = "")
public class BollaResaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private BollaResaPk pk;
	@Column(name = "tagr9619")
	private Integer aggiuntaTipo;
	@Column(name = "idtn9619")
	private Integer idTestataNumero;
	@Column(name = "cpu9619")
	private Integer cpuDl;
	@Column(name = "num9619")
	@Getter(AccessLevel.NONE)
	private String numeroPubblicazione;
	@Column(name = "titolo9619")
	private String titolo;
	@Column(name = "sottot9619")
	private String sottoTitolo;
	@Column(name = "plor9619")
	private BigDecimal prezzoLordo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9619", updatable = false, insertable = false, referencedColumnName = "coddl9624"),
		@JoinColumn(name = "tagr9619", updatable = false, insertable = false, referencedColumnName = "trr9624")
	})
	private DecodificaRichiamiResaVo numeroRichiamo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9619", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9619", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo storicoCopertineVo;
	@Column(name = "riga9619")
	private Integer riga;
	@Column(name = "cesta9619")
	private String cesta;
	
	public String getNumeroPubblicazione() {
		return numeroPubblicazione != null ? numeroPubblicazione.trim() : numeroPubblicazione;
	}
}
