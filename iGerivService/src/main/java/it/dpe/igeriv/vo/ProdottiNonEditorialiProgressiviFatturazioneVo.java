package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiProgressiviFatturazionePk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Progressivi di Fatturazione per Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9502", schema = "")
public class ProdottiNonEditorialiProgressiviFatturazioneVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ProdottiNonEditorialiProgressiviFatturazionePk pk;
	@Column(name = "prog9502")
	private Long progressivo;
	@Column(name = "daum9502")
	private Timestamp dataUltMovimentazione;
	@Column(name = "vers9502")
	private Integer versione;
}
