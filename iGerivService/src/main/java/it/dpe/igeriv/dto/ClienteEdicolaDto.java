package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;


import it.dpe.igeriv.util.StringUtility;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.math.NumberUtils;

@Getter
@Setter
public class ClienteEdicolaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	@Getter
	private Long codCliente;
	private Long codVendita;
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String nome;
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String cognome;
	private String localitaDesc;
	private String provinciaDesc;
	private String cap;
	private String telefono;
	private String email;
	private Long qtaOrdiniRichiesta;
	@Getter(AccessLevel.NONE)
	private Long qtaOrdiniEvasa;
	private BigDecimal importoTotale;
	private Timestamp dataCompetenzaEstrattoConto;
	private String metodoPagamento;
	@Getter(AccessLevel.NONE)
	private BigDecimal totaleEstrattoConto;
	@Getter(AccessLevel.NONE)
	private BigDecimal totaleEstrattoContoPne;
	private Integer tipoDocumento;
	private Integer tipoEstrattoConto;
	private Timestamp dtSospensionePrenotazioniDa;
	private Timestamp dtSospensionePrenotazioniA;
	private Timestamp dataDocumento;
	private Integer numeroDocumento;
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Integer dettagioAnagrafica;
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Integer dettaglioOrdini;
	private Boolean ordiniPresenti = true;
	private Boolean ordIncompleti = true;
	private Boolean ordDaConsegnare = true;
	
	
	
	
	
	public String getNome() {
		return nome == null ? "" : nome;
	}
	
	public String getNomeSort() {
		return getNome().toUpperCase();
	}

	public void setNome(String nome) {
		String nc = "";
		if (!NumberUtils.isNumber(nome)) {
			nc = nome;
		} else {
			nc = IGerivMessageBundle.get("igeriv.tessera.numero") + " " + nome;
		}
		this.nome = nc;
	}

	public String getCognome() {
		return cognome == null ? "" : cognome;
	}

	public void setCognome(String cognome) {
		String nc = "";
		if (!NumberUtils.isNumber(cognome)) {
			nc = cognome;
		} else {
			nc = "";
		}
		this.cognome = nc;
	}
	
	public Long getQtaOrdiniEvasa() {
		return qtaOrdiniEvasa == null ? 0l : qtaOrdiniEvasa;
	}
	
	public Long getQtaOrdiniDaEvadere() {
		return (qtaOrdiniRichiesta != null) ? qtaOrdiniRichiesta - getQtaOrdiniEvasa() : 0l;
	}
	
	public String getFake() {
		return "";
	}
	
	public String getNomeCognome() {
		return getNome() + " " + getCognome();
	}
	
	public String getNomeCognomeEscaped() {
		return StringUtility.unescapeHTML(getNomeCognome());
	}
	
	public BigDecimal getTotaleEstrattoConto() {
		return totaleEstrattoConto == null ? new BigDecimal(0) : totaleEstrattoConto;
	}

	public BigDecimal getTotaleEstrattoContoPne() {
		return totaleEstrattoContoPne == null ? new BigDecimal(0) : totaleEstrattoContoPne;
	}

	public BigDecimal getTotaleEstrattoContoPubb() {
		return (getTotaleEstrattoConto() != null && !getTotaleEstrattoConto().equals(new BigDecimal(0)) && getTotaleEstrattoContoPne() != null && !getTotaleEstrattoContoPne().equals(new BigDecimal(0))) ? getTotaleEstrattoConto().subtract(getTotaleEstrattoContoPne()) : getTotaleEstrattoConto();
	}

	public Boolean hasProdottiVariInEstratto() {
		return (getTotaleEstrattoContoPne() != null && getTotaleEstrattoContoPne().doubleValue() > 0d) ? true : false;
	}
	
	public Boolean isDateBetweenDateSospensione(Timestamp date) {
		return getDtSospensionePrenotazioniDa() != null && getDtSospensionePrenotazioniA() != null ? date.after(getDtSospensionePrenotazioniDa()) && date.before(getDtSospensionePrenotazioniA()) : false;
	}

	public String getTipoDocumentoDesc() {
		if (getTipoDocumento() != null) {
			if (getTipoDocumento().equals(IGerivConstants.FATTURA)) {
				return IGerivMessageBundle.get("igeriv.fattura");
			} else if (getTipoDocumento().equals(IGerivConstants.STORNO_FATTURA)) {
				return IGerivMessageBundle.get("igeriv.storno.fattura");
			} else if (getTipoDocumento().equals(IGerivConstants.BOLLETTINA_CONSEGNA)) {
				return IGerivMessageBundle.get("igeriv.bollettina.consegna");
			} else if (getTipoDocumento().equals(IGerivConstants.ESTRATTO_CONTO)) {
				return IGerivMessageBundle.get("igeriv.data.estratto.conto");
			}
		}
		return "";
	}
	
	public String getTipoEstrattoContoDesc() {
		if (getTipoEstrattoConto() != null) {
			if (getTipoEstrattoConto().equals(IGerivConstants.TIPO_ESTRATTO_CONTO_CLIENTE_EDICOLA_MENSILE)) {
				return IGerivMessageBundle.get("igeriv.senza.estratto.mensile.short");
			} else if (getTipoEstrattoConto().equals(IGerivConstants.TIPO_ESTRATTO_CONTO_CLIENTE_EDICOLA_SETTIMANALE)) {
				return IGerivMessageBundle.get("igeriv.senza.estratto.settimanale.short");
			} else {
				return IGerivMessageBundle.get("igeriv.nessuno");
			}
		}
		return "";
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			return ((ClienteEdicolaDto) obj).codCliente.equals(codCliente);
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return codCliente.intValue();
	}
	
}
