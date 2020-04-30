package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.pk.RichiestaClientePk;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.apache.commons.lang.math.NumberUtils;

@Data
@EqualsAndHashCode(callSuper=false)
public class RichiestaClienteDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private RichiestaClientePk pk;
	private String titolo;
	private String sottoTitolo;
	@Getter(AccessLevel.NONE)
	private String nome;
	@Getter(AccessLevel.NONE)
	private String cognome;
	private String numeroCopertina;
	private Timestamp dataUscita;
	private Integer provenienza;
	private Integer statoEvasione;
	private Date dataOrdine;
	private Timestamp dataUltimaRisposta;
	private Integer quantitaRichiesta;
	private Integer quantitaEvasa;
	private BigDecimal prezzoCopertina;
	private Integer messaggioCodificato; 
	private String messagioLibero;
	private Integer codiceUltimaRisposta;
	private Integer quantitaConsegnare;
	private String telefono;
	private Integer ultRisposta;
	private Integer codEdicola;
	private Long codCliente;
	private Integer codDl;
	private Integer idtn;
	private String email;
	private Timestamp dtSospensionePrenotazioniDa;
	private Timestamp dtSospensionePrenotazioniA;
	private Integer tipoEstrattoConto;
	
	public String getNome() {
		if (nome != null && nome.startsWith("52") && nome.length() == 10 && NumberUtils.isNumber(nome)) {
			return IGerivMessageBundle.get("igeriv.tessera.numero");
		}
		return nome;
	}

	public String getCognome() {
		if (nome != null && nome.startsWith("52") && nome.length() == 10 && NumberUtils.isNumber(nome)) {
			return nome;
		}
		return cognome;
	}

	public Integer getQuantitaDaEvadere() {
		return (quantitaRichiesta != null && quantitaEvasa != null) ? quantitaRichiesta - quantitaEvasa : 0;
	}
	
	public BigDecimal getImportoConsegnare() {
		return (quantitaConsegnare != null && prezzoCopertina != null) ? prezzoCopertina.multiply(new BigDecimal(quantitaConsegnare)) : new BigDecimal(0);
	}
	
	public String getFake() {
		return "";
	}
	
	public String getNomeCognomeCliente() {
		return getNome() + " " + getCognome();
	}
	
	public Boolean isDateBetweenDateSospensione(Timestamp date) {
		return getDtSospensionePrenotazioniDa() != null && getDtSospensionePrenotazioniA() != null ? date.after(getDtSospensionePrenotazioniDa()) && date.before(getDtSospensionePrenotazioniA()) : false;
	}
	
	public String getDataUscitaFormat() {
		return (dataUscita != null) ? DateUtilities.getTimestampAsString(dataUscita, DateUtilities.FORMATO_DATA_SLASH) : "";
	}
	
	public String getStatoEvasioneDesc() {
		if (statoEvasione.equals(IGerivConstants.COD_STATO_EVASIONE_COMPLETA)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_COMPLETA);
		} else if (statoEvasione.equals(IGerivConstants.COD_STATO_EVASIONE_INSERITO)) {
			return  IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_INSERITO);
		} else if (statoEvasione.equals(IGerivConstants.COD_STATO_EVASIONE_PARZIALE)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_PARZIALE);
		} 
		return null;
	}
	
	public String getProvenienzaDesc() {
		if (getProvenienza().equals(IGerivConstants.COD_PROVENIENZA_RICHIESTA_AUTOMATICA)) {
			return IGerivMessageBundle.get(IGerivConstants.PROVENIENZA_RICHIESTA_AUTOMATICA);
		} else if (getProvenienza().equals(IGerivConstants.COD_PROVENIENZA_RICHIESTA_CLIENTE)) {
			return IGerivMessageBundle.get(IGerivConstants.PROVENIENZA_RICHIESTA_CLIENTE);
		} else if (getProvenienza().equals(IGerivConstants.COD_PROVENIENZA_RICHIESTA_EDICOLA)) {
			return IGerivMessageBundle.get(IGerivConstants.PROVENIENZA_RICHIESTA_EDICOLA);
		}
		return null;
	}
}
