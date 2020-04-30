package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.RichiestaAggiornamentoBarcodePk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Richiesta Aggiornamento Barcode
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9651", schema = "")
public class RichiestaAggiornamentoBarcodeVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private RichiestaAggiornamentoBarcodePk pk;
	@Column(name = "crivw9651")
	private Integer codEdicola;
	@Column(name = "flagg9651")
	private Boolean richiestaEseguita;
	@Column(name = "datinv9651")
	private Timestamp dataInvioRichiesta;
	@Column(name = "datese9651")
	private Timestamp dataEsecuzione;
}	
