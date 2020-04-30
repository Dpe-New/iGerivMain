package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiRichiesteRifornimentoDettaglioPk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Richiesta Rifornimento Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9533", schema = "")
@NamedQueries( {
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_RICHIESTE_RIFORNIMENTO_TEMP, query = "update ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo vo set vo.statoEsportazione = :statoEsportazione, vo.dataEsportazioneVersoDl = :dataEsportazioneVersoDl where vo.pk.codRichiestaRifornimento = :codRichiestaRifornimento and vo.pk.codProdottoInterno = :codProdottoInterno")
})
public class ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ProdottiNonEditorialiRichiesteRifornimentoDettaglioPk pk;
	@Column(name = "quaric9533")
	private Integer quatitaRichiesta;
	@Column(name = "quaev9533")
	private Integer quatitaEvasa;
	@Column(name = "stato9533")
	private String stato;
	@Column(name = "risp9533")
	private String rispostaDl;
	@Column(name = "dultagg9533")
	private Timestamp dataUltAggiornamento;
	@Column(name = "note9533")
	private String note;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "crivw9533", updatable = false, insertable = false, referencedColumnName = "crivw9532"),
			@JoinColumn(name = "ordine9533", updatable = false, insertable = false, referencedColumnName = "ordine9532") 
	})
	private ProdottiNonEditorialiRichiesteRifornimentoVo richiesteRifornimento;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "crivw9533", updatable = false, insertable = false, referencedColumnName = "crivw9506"),
			@JoinColumn(name = "cpro9533", updatable = false, insertable = false, referencedColumnName = "cpro9506") 
	})
	private ProdottiNonEditorialiVo prodotto;
	@ManyToOne
	@JoinColumn(name = "crivw9533", insertable = true, updatable = true, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola;
	@Column(name = "stesp9533")
	private Integer statoEsportazione;
	@Column(name = "dtesp9533")
	private Timestamp dataEsportazioneVersoDl;
	@Column(name = "corid9533")
	private String correlationId;
}
