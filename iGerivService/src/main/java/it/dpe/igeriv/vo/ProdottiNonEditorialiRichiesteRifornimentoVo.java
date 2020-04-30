package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
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
@Table(name = "tbl_9532", schema = "")
@NamedQueries( {
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_RICHIESTE_RIFORNIMENTO_DATA_INVIO, query = "update ProdottiNonEditorialiRichiesteRifornimentoVo vo set vo.dataInvioRichiestaDl = :dataInvioRichiestaDl where vo.codRichiestaRifornimento = :codRichiestaRifornimento")
})
public class ProdottiNonEditorialiRichiesteRifornimentoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ordine9532")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9532", allocationSize = 1)
	private Long codRichiestaRifornimento;
	@Column(name = "cforn9532")
	private Integer codFornitore;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crivw9532", updatable = true, insertable = true, referencedColumnName = "crivw9206")
	private AbbinamentoEdicolaDlVo edicola;
	@Column(name = "dataord9532")
	private Timestamp dataRichiesta;
	@Column(name = "stato9532")
	private String stato;
	@Column(name = "datatra9532")
	private Timestamp dataInvioRichiestaDl;
	@Column(name = "dultagg9532")
	private Timestamp dataUltAggiornamento;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "cforn9532", updatable = false, insertable = false, referencedColumnName = "cforn9500"),
		@JoinColumn(name = "crivw9532", updatable = false, insertable = false, referencedColumnName = "crivw9500")
	})
	private ProdottiNonEditorialiFornitoreVo fornitore;
}
