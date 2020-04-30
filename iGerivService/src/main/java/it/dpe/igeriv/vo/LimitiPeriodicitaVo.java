package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.LimitiPeriodicitaPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Limniti per Periodicità
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9108", schema = "")
public class LimitiPeriodicitaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private LimitiPeriodicitaPk pk;
	@Column(name = "maxre9108")
	private Integer numMaxCopertineVecchieResa;
	@Column(name = "maxst9108")
	private Integer numMaxStoriaCopertine;
	@Column(name = "resnu9108")
	private String permetteResaNumeroNuovo;
	@Column(name = "masvi9108")
	private Integer maxStatisticaVisualizzare;
	@Column(name = "resacd9108")
	private String permettePubblicazioniInContoDeposito;
	@Column(name = "ngval9108")
	private Integer giorniValiditaRichiesteRifornimento;
	@Column(name = "ngvri9108")
	private Integer numGiorniDaDataUscitaPerRichiestaRifornimento;
}
