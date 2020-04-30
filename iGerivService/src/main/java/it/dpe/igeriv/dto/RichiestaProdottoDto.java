package it.dpe.igeriv.dto;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RichiestaProdottoDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long codRichiestaRifornimento;
	private Long codProdottoInterno;
	private String descrizioneProdotto;
	private Integer quatitaRichiesta;
	private Integer quatitaRichiestaTmp;
	private Integer quatitaEvasa;
	private Timestamp dataRichiesta;
	private String stato;
	private String rispostaDl;
	private Timestamp dataInvioRichiestaDl;
	private String note;
	
	public String getStatoDesc() {
		if (stato.equals(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INSERITO)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_INSERITO);
		} else if (stato.equals(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_PRONTO_PER_INVIO)) {
			return  IGerivMessageBundle.get(IGerivConstants.STATO_PRONTO_PER_INVIO);
		} else if (stato.equals(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_EVASA)) {
			if ((getQuatitaRichiesta() != null || getQuatitaRichiestaTmp() != null) && getQuatitaEvasa() != null 
					&& getQuatitaEvasa() < (getQuatitaRichiesta() == null ? getQuatitaRichiestaTmp() : getQuatitaRichiesta())) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_EVASO_PARZIALMENTE);
			}
			return  IGerivMessageBundle.get(IGerivConstants.STATO_EVASO);
		} else if (stato.equals(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_NON_EVADIBILE)) {
			return  IGerivMessageBundle.get(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_NON_EVADIBILE_TEXT);
		} else if (stato.equals(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INVIATO)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_INVIATO_DL);
		} else if (stato.equals(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_EVASA_PARZIALMENTE)) {
			return IGerivMessageBundle.get(IGerivConstants.STATO_EVASO_PARZIALMENTE);
		}
		return "";
	}
	
	public String getPk() {
		return getCodRichiestaRifornimento() + "|" + getCodProdottoInterno();
	}

	public boolean isEnabled() {
		return (getStato() != null && (getStato().equals(IGerivConstants.STATO_EVASIONE_PRODOTTO_NON_EDITORIALE_INSERITO))) ? true : false;
	}
	
	public String getDataInvioRichiestaDlFormat() {
		return getDataInvioRichiestaDl() != null ? DateUtilities.getTimestampAsString(getDataInvioRichiestaDl(), DateUtilities.FORMATO_DATA_SLASH_HHMMSS) : "";
	}
	
	public String getDataRichiestaDesc() {
		return getDataRichiesta() != null ? DateUtilities.getTimestampAsString(getDataRichiesta(), DateUtilities.FORMATO_DATA_SLASH_HHMMSS) : "";
	}
}
