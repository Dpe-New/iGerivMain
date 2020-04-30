package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

/**
 * Tabella anagrafica tipologia mini card
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries({
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ALL_ANAGRAFICA_TIPOLOGIA_MINICARD, query = "from AnagraficaTipologiaMinicardVo"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_TIPOLOGIA_MINICARD, query = "from AnagraficaTipologiaMinicardVo where codTipologiaMinicard = :codTipologiaMinicard"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_LISTA_ANAGRAFICA_TIPOLOGIA_MINICARD_PER_RIVENDITE, query = "from AnagraficaTipologiaMinicardVo where tipoRicarica <> '" + IGerivConstants.RICARICA_OMAGGIO + "' and codAutorizzazioneRicarica = " + IGerivConstants.COD_AUTORIZZAZIONE_RICARICA_TUTTI + " and stato not in ('" + IGerivConstants.STATO_CONSEGNA_SOSPESA + "','" + IGerivConstants.STATO_CONSEGNA_ANNULLATA + "')")
})
@Table(name = "tbl_9851", schema = "")
@DynamicUpdate
@DynamicInsert
@OptimisticLocking(type=OptimisticLockType.VERSION)
public class AnagraficaTipologiaMinicardVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "tipmi9851")
	private String codTipologiaMinicard;
	@Version
	@Column(name = "versi9851")
	private Integer version;
	@Column(name = "stato9851")
	private String stato;
	@Column(name = "descr9851")
	private String descrizioneTipologia;
	@Column(name = "cauri9851")	
	private Integer codAutorizzazioneRicarica;
	@Column(name = "staco9851")	
	private String statoConsegna;
	@Column(name = "tipri9851")	
	private String tipoRicarica;
	@Column(name = "imric9851")
	private BigDecimal importoRicarica;
	@Column(name = "agriv9851")
	private BigDecimal aggioRivendita;
	@Column(name = "agage9851")
	private BigDecimal aggioAgenzia;
	@Column(name = "coric9851")
	private Integer copieRicarica;
	@Column(name = "copgi9851")
	private Integer copieGiornaliere;
	@Column(name = "giorn9851")
	private Integer durataGiorniRicarica;
	@Column(name = "copri9851")
	private Integer copieMaxRiportare;
	@Column(name = "copmi9851")	
	private Integer copieResidueLimite;
	@Column(name = "codut9851")
	private Integer codUtente;
	@Column(name = "dulmo9851")
	private Timestamp dataUltimaModifica;
	
	@Transient
	public String getStatoDesc() {
		if (stato != null) {
			if (stato.equals(IGerivConstants.STATO_TESSERA_ATTIVA)) {
				return IGerivConstants.STATO_TESSERA_ATTIVA_TEXT;
			} else if (stato.equals(IGerivConstants.STATO_TESSERA_ANNULLATA)) {
				return IGerivConstants.STATO_TESSERA_ANNULLATA_TEXT;
			} else if (stato.equals(IGerivConstants.STATO_TESSERA_EMESSA_NON_ATTIVA)) {
				return IGerivConstants.STATO_TESSERA_EMESSA_NON_ATTIVA_TEXT;
			} else if (stato.equals(IGerivConstants.STATO_TESSERA_SOSPESA)) {
				return IGerivConstants.STATO_TESSERA_SOSPESA_TEXT;
			}
		}
		return "";
	}
	
	@Transient
	public String getCodAutorizzazioneRicaricaDesc() {
		if (codAutorizzazioneRicarica != null) {
			if (codAutorizzazioneRicarica.equals(IGerivConstants.COD_AUTORIZZAZIONE_RICARICA_TUTTI)) {
				return IGerivConstants.COD_AUTORIZZAZIONE_RICARICA_TUTTI_TEXT;
			} else if (stato.equals(IGerivConstants.COD_AUTORIZZAZIONE_RICARICA_GAZZETTA)) {
				return IGerivConstants.COD_AUTORIZZAZIONE_RICARICA_GAZZETTA_TEXT;
			} 
		}
		return "";
	}
	
	@Transient
	public String getStatoConsegnaDesc() {
		if (statoConsegna != null) {
			if (statoConsegna.equals(IGerivConstants.STATO_TESSERA_ATTIVA)) {
				return IGerivConstants.STATO_TESSERA_ATTIVA_TEXT;
			} else if (statoConsegna.equals(IGerivConstants.STATO_TESSERA_SOSPESA)) {
				return IGerivConstants.STATO_TESSERA_SOSPESA_TEXT;
			}
		}
		return "";
	}
	
	@Transient
	public String getTipoRicaricaDesc() {
		if (tipoRicarica != null) {
			if (tipoRicarica.equals(IGerivConstants.RICARICA_PAGAMENTO)) {
				return IGerivConstants.RICARICA_PAGAMENTO_TEXT;
			} else if (tipoRicarica.equals(IGerivConstants.RICARICA_OMAGGIO)) {
				return IGerivConstants.RICARICA_OMAGGIO_TEXT;
			}
		}
		return "";
	}

}
