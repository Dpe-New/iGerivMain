package it.dpe.igeriv.dto;

import it.dpe.igeriv.enums.StatoRichiestaRifornimentoLivellamento;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.pk.BasePk;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper=false)
public class RichiestaRifornimentoDto extends BaseDto implements PubblicazioneFornito {
	private static final long serialVersionUID = 1L;
	private BasePk pk;
	private Integer coddl;
	private String titolo;
	private Integer editore;
	private String sottoTitolo;
	private Integer idtn;
	private Integer codicePubblicazione;
	private String numeroCopertina;
	private BigDecimal prezzoCopertina;
	private Timestamp dataUscita;
	private Integer codRivendita;
	private String ragioneSocialeRivendita;
	private Timestamp dataOrdine;
	private Timestamp dataRispostaDl;
	private Timestamp dataScadenza;
	private String descCausaleNonEvadibilita;
	private Integer quantitaRichiesta;
	private Integer quantitaRichiestaGestioneClienti;
	private Integer quantitaEvasa;
	private String stato;
	private Integer statoEvasione;
	private String noteVendita;
	private Integer prenotazione;
	private Integer codInizioQuotidiano;
	private Integer codFineQuotidiano;
	private Integer numCopertinePrecedentiPerRifornimenti;
	private Integer fornitoBolla;
	private Integer fornitoFondoBolla;
	private Integer fornitoStorico;
	private Integer fornitoEstrattoConto;
	private Integer reso;
	private Integer venduto;
	private Long giacenza;
	private Integer vendite;
	private Integer richiedereDifferenzaDl;
	private Integer giorniValiditaRichiesteRifornimento;
	private boolean enabled = true;
	private boolean isRigaEvasione;
	private Timestamp dataBollaAddebito;
	@Getter(AccessLevel.NONE)
	private Integer codiceInforete;
	@Getter(AccessLevel.NONE)
	private String numeroCopertinaInforete;
	private Long idRichiestaLivellamento;
	private StatoRichiestaRifornimentoLivellamento statoRichiestaRifornimentoLivellamento;
	@Getter(AccessLevel.NONE)
	private Boolean livellamentoEditable;
	private String immagine;
	private Integer indicatorePaccotto;
	private Integer richiestaSospesa;
	
	
	
	
	
	public String getStatoDesc() {
		if (stato == null) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_INSERITO);
		} else if (stato.equals(IGerivConstants.STATO_INVIATO_DL_SIGLA)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_INVIATO_DL);
		} else if (stato.equals(IGerivConstants.STATO_PRONTO_PER_INVIO_SIGLA)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_PRONTO_PER_INVIO);
		}
		return null;
	}
	
	@Override
	public Integer getFornito() {
		return (fornitoBolla != null ? fornitoBolla : 0) 
				+ (fornitoFondoBolla != null ? fornitoFondoBolla : 0) 
				+ (fornitoEstrattoConto != null ? fornitoEstrattoConto : 0)
				+ (fornitoStorico != null ? fornitoStorico : 0);
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
	
	public String getDataScadenzaRichiesta() {
		if (getGiorniValiditaRichiesteRifornimento() != null && getDataOrdine() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDataOrdine());
			cal.add(Calendar.DAY_OF_MONTH, getGiorniValiditaRichiesteRifornimento());
			return DateUtilities.getTimestampAsString(cal.getTime(), DateUtilities.FORMATO_DATA_SLASH);
		}
		return "";
	}
	
	/*public Timestamp getDataScadenzaRichiesta() {
		if (getGiorniValiditaRichiesteRifornimento() != null && getDataOrdine() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDataOrdine());
			cal.add(Calendar.DAY_OF_MONTH, getGiorniValiditaRichiesteRifornimento());
			return new Timestamp(cal.getTimeInMillis());
		}
		return null;
	}*/
	
	public Integer getCodiceInforete() {
		return codiceInforete == null ? 0 : codiceInforete;
	}
	
	public String getNumeroCopertinaInforete() {
		return numeroCopertinaInforete == null ? "" : numeroCopertinaInforete;
	}
	
	@Override
	public String getCodiceInforeteNumeroCopertinaInforete() {
		//return getCodiceInforete().toString() + getNumeroCopertinaInforete();
		return "0";
	}
	
	@Override
	public String getCodicePubblicazioneeNumeroCopertina() {
		return getCodicePubblicazione().toString() + getNumeroCopertina();
	}
	
	@Override
	public Boolean isEditoreComune() {
		return getEditore() != null && (
				getEditore().equals(16) 			// M-Dis
				|| getEditore().equals(246) 		// To-Dis
				/*|| getEditore().equals(287) 		// NUOVA MERATE PRESS
				|| getEditore().equals(360) 		// SOCIETA' PRELUM
				|| getEditore().equals(403) 		// PUBLICHIERI
				|| getEditore().equals(998) 		// PUBLICHIERI
				|| getEditore().equals(999)			// To-Dis*/ 
				);
	}

	public Boolean getLivellamentoEditable() {
		return livellamentoEditable == null ? true : livellamentoEditable;
	}
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}

	
}
