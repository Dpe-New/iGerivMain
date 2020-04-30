package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ProdottoNonEditorialePrezzoVenditaPk;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Prezzi Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9508", schema = "")
public class ProdottiNonEditorialiPrezzoVenditaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ProdottoNonEditorialePrezzoVenditaPk pk;
	@Column(name = "dvi9508")
	private Date dataValiditaIniziale;
	@Column(name = "prezzo9508")
	private Float prezzoLisitino;
	@Column(name = "scontov9508")
	private Float scontoValore;
	@Column(name = "sconto9508")
	private Float scontoPercentuale;
	@ManyToOne
	@JoinColumn(name = "crivw9508", insertable = false, updatable = false, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola;
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "crivw9508", insertable = false, updatable = false, referencedColumnName = "crivw9506"),
		@JoinColumn(name = "cpro9508", insertable = false, updatable = false, referencedColumnName = "cpro9506")
	})
	private ProdottiNonEditorialiVo prodotto; 
}
