package it.dpe.ws.client.dto.abbonati;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.rtae.dto.BaseDto;

@XmlRootElement(name = "Ricarica", namespace="http://it.dpe.rtae/ricaricabili/schemas") 
@XmlAccessorType(XmlAccessType.FIELD)
public class AnagraficaTipologiaMinicardDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer codTipologiaMinicard;
	private String tipologiaMinicardEditore;
	private String descrizioneTipologia;
	private Integer codAutorizzazioneRicarica;
	private BigDecimal importoRicarica;
	private Integer copieRicarica;
	private Integer durataGiorniRicarica;
	private String titoloPrimaParte;
	private String stato;
	private String statoConsegna;
	private String tipoRicarica;
	private BigDecimal aggioRivendita;
	private BigDecimal aggioAgenzia;
	private Integer copieGiornaliere;
	private Integer copieMaxRiportare;
	private Integer copieResidueLimite;
	private Integer codUtente;
	private Date dataUltimaModifica;
	
	public Integer getCodTipologiaMinicard() {
		return codTipologiaMinicard;
	}

	public void setCodTipologiaMinicard(Integer codTipologiaMinicard) {
		this.codTipologiaMinicard = codTipologiaMinicard;
	}

	public String getTipologiaMinicardEditore() {
		return tipologiaMinicardEditore;
	}

	public void setTipologiaMinicardEditore(String tipologiaMinicardEditore) {
		this.tipologiaMinicardEditore = tipologiaMinicardEditore;
	}

	public String getDescrizioneTipologia() {
		return descrizioneTipologia;
	}

	public void setDescrizioneTipologia(String descrizioneTipologia) {
		this.descrizioneTipologia = descrizioneTipologia;
	}

	public BigDecimal getImportoRicarica() {
		return importoRicarica;
	}

	public void setImportoRicarica(BigDecimal importoRicarica) {
		this.importoRicarica = importoRicarica;
	}

	public Integer getCopieRicarica() {
		return copieRicarica;
	}

	public void setCopieRicarica(Integer copieRicarica) {
		this.copieRicarica = copieRicarica;
	}

	public Integer getDurataGiorniRicarica() {
		return durataGiorniRicarica;
	}

	public void setDurataGiorniRicarica(Integer durataGiorniRicarica) {
		this.durataGiorniRicarica = durataGiorniRicarica;
	}
	
	public String getImportoRicaricaFormat() {
		return NumberUtils.formatImporto(importoRicarica);
	}

	public Integer getCodAutorizzazioneRicarica() {
		return codAutorizzazioneRicarica;
	}

	public void setCodAutorizzazioneRicarica(Integer codAutorizzazioneRicarica) {
		this.codAutorizzazioneRicarica = codAutorizzazioneRicarica;
	}

	public String getTitoloPrimaParte() {
		return titoloPrimaParte;
	}

	public void setTitoloPrimaParte(String titoloPrimaParte) {
		this.titoloPrimaParte = titoloPrimaParte;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getStatoConsegna() {
		return statoConsegna;
	}

	public void setStatoConsegna(String statoConsegna) {
		this.statoConsegna = statoConsegna;
	}

	public String getTipoRicarica() {
		return tipoRicarica;
	}

	public void setTipoRicarica(String tipoRicarica) {
		this.tipoRicarica = tipoRicarica;
	}

	public BigDecimal getAggioRivendita() {
		return aggioRivendita;
	}

	public void setAggioRivendita(BigDecimal aggioRivendita) {
		this.aggioRivendita = aggioRivendita;
	}

	public BigDecimal getAggioAgenzia() {
		return aggioAgenzia;
	}

	public void setAggioAgenzia(BigDecimal aggioAgenzia) {
		this.aggioAgenzia = aggioAgenzia;
	}

	public Integer getCopieGiornaliere() {
		return copieGiornaliere;
	}

	public void setCopieGiornaliere(Integer copieGiornaliere) {
		this.copieGiornaliere = copieGiornaliere;
	}

	public Integer getCopieMaxRiportare() {
		return copieMaxRiportare;
	}

	public void setCopieMaxRiportare(Integer copieMaxRiportare) {
		this.copieMaxRiportare = copieMaxRiportare;
	}

	public Integer getCopieResidueLimite() {
		return copieResidueLimite;
	}

	public void setCopieResidueLimite(Integer copieResidueLimite) {
		this.copieResidueLimite = copieResidueLimite;
	}

	public Integer getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(Integer codUtente) {
		this.codUtente = codUtente;
	}
	
	public Date getDataUltimaModifica() {
		return this.dataUltimaModifica;
	}

	public void setDataUltimaModifica(Date dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}
	
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
	
	public String getCodAutorizzazioneRicaricaDesc() {
		if (codAutorizzazioneRicarica != null) {
			if (codAutorizzazioneRicarica.equals(IGerivConstants.COD_AUTORIZZAZIONE_RICARICA_TUTTI)) {
				return IGerivConstants.COD_AUTORIZZAZIONE_RICARICA_TUTTI_TEXT;
			} else if (stato.equals(IGerivConstants.COD_AUTORIZZAZIONE_RICARICA_EDITORE)) {
				return IGerivConstants.COD_AUTORIZZAZIONE_RICARICA_EDITORE_TEXT;
			} 
		}
		return "";
	}
	
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
