package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.vo.VenditaDettaglioVo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenditaDettaglioDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long idVendita;
	private Timestamp dataVendita;
	private Timestamp dataUscita;
	private Integer progressivo;
	private Integer idtn;
	private Integer codFiegDl;
	private Integer codEdicola;
	private BigDecimal prezzoCopertina;
	private String titolo;
	private String sottoTitolo;
	private String numeroCopertina;
	private Integer quantita;
	private BigDecimal importoTotale;
	private Boolean prodottoNonEditoriale;
	private Long idProdotto;
	private String barcode;
	
	//GIFT CARD
	private String flagProDigitale = "N";
	private Integer trasferitaGestionale;
	private Long idDocumentoProdottiVari;
	@Getter(AccessLevel.NONE)
	private String giacenzaIniziale;
	private Integer aliquota;
	private String ragSocClientePrimaRiga;
	private String ragSocClienteSecondaRiga;
	
	public String getPrezzoCopertinaFormat() {
		return (prezzoCopertina != null) ? NumberUtils.formatNumber(prezzoCopertina) : "";
	}
	
	public String getImportoFormat() {
		return (importoTotale != null) ? NumberUtils.formatNumber(importoTotale) : "";
	}
	
	public BigDecimal getTotale() {
		return (getQuantita() != null && getPrezzoCopertina() != null) ? getPrezzoCopertina().multiply(new BigDecimal(getQuantita())) : null;
	}
	
	public String getOraVendita() {
		Timestamp dataVendita = getDataVendita();
		String timestampAsString = DateUtilities.getTimestampAsString(dataVendita, "HH:mm:ss");
		return timestampAsString;
	}
	
	public String getGiacenzaIniziale() {
		return giacenzaIniziale == null ? "" : giacenzaIniziale;
	}

	public String getDataUscitaFormat() {
		return (dataUscita != null) ? DateUtilities.getTimestampAsString(dataUscita, DateUtilities.FORMATO_DATA_SLASH) : "";
	}
	
	public Integer getCoddl() {
		return getCodFiegDl();
	}
	
	public String getRagSocClientePrimaRiga() {
		return ragSocClientePrimaRiga;
	}
	
	public void setRagSocClientePrimaRiga(String ragSocClientePrimaRiga) {
		this.ragSocClientePrimaRiga = ragSocClientePrimaRiga;
	}
	
	public String getRagSocClienteSecondaRiga() {
		return ragSocClienteSecondaRiga;
	}
	
	public void setRagSocClienteSecondaRiga(String ragSocClienteSecondaRiga) {
		this.ragSocClienteSecondaRiga = ragSocClienteSecondaRiga;
	}

	public String getRagioneSocialeCliente() {
		StringBuilder result = new StringBuilder();
		
		if (ragSocClientePrimaRiga != null && !ragSocClientePrimaRiga.isEmpty()) {
			result.append(ragSocClientePrimaRiga);
		}
		if (ragSocClienteSecondaRiga != null && !ragSocClienteSecondaRiga.isEmpty()) {
			if (result.length() > 0) {
				result.append(" ");
			}
			result.append(ragSocClienteSecondaRiga);
		}
		
		return result.length() == 0 ? null : result.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equal = false;
		if (obj != null && obj instanceof VenditaDettaglioVo) {
			VenditaDettaglioVo vo = (VenditaDettaglioVo) obj;
			if (progressivo.equals(vo.getProgressivo()) && idVendita.equals(getIdVendita())) {
				equal = true;
			}
		}
		return equal;
	}
	
}
