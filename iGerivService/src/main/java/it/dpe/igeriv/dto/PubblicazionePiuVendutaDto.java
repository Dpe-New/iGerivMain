package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.util.StringUtility;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Strings;

/**
 * @author romanom
 *
 */
@Getter
@Setter
public class PubblicazionePiuVendutaDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer codicePubblicazione;
	private Integer codFiegDl;
	private Long quantita;
	private String titolo;
	@Getter(AccessLevel.NONE)
	private String nomeImmagine;
	private String nomeImmagineShort;
	private String immagineUltimaCopertina;
	private String barcode;
	private String idtn;
	private Integer codPeriodicita;
	private Integer codInizioQuotidiano;
	private Integer codFineQuotidiano;
	private Integer top;
	private Integer left;
	private Integer width;
	private Integer height;
	private String immagineNonDispobibileMsg;
	@Getter(AccessLevel.NONE)
	private Integer tipoImmagine;
	private String nomeImmagineDefault;
	private BigDecimal prezzoCopertina;
	private String numeroCopertina;
	@Getter(AccessLevel.NONE)
	private Boolean puoRichiedereRifornimenti;
	
	public String getNomeImmagine() {
		return Strings.isNullOrEmpty(nomeImmagine) ? nomeImmagineDefault : nomeImmagine;
	}

	public String getNomeImmagineShort() {
		if (nomeImmagine != null && nomeImmagine.trim().contains(" ")) {
			return nomeImmagine.trim().substring(0, nomeImmagine.indexOf(" "));
		}
		return (nomeImmagine != null) ? nomeImmagine.trim().substring(0, nomeImmagine.lastIndexOf(".")) : nomeImmagineShort;
	}

	public Integer getTipoImmagine() {
		return Strings.isNullOrEmpty(nomeImmagine) ? IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA : tipoImmagine;
	}

	public String getNomeImmagineEscape() {
		return !Strings.isNullOrEmpty(getNomeImmagine()) ? StringUtility.escapeHTML(getNomeImmagine(), false) : "";
	}
	
	public String getImmagineDirAlias() {
		return tipoImmagine != null ? ((IGerivUtils) SpringContextManager.getService("iGerivUtils")).getImgAlias(tipoImmagine) : "";
	}
	
	public String getPrezzoCopertinaFormat() {
		return (prezzoCopertina != null) ? NumberUtils.formatNumber(prezzoCopertina) : "";
	}
	
	public Boolean getPuoRichiedereRifornimenti() {
		return puoRichiedereRifornimenti == null ? false : puoRichiedereRifornimenti;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof PubblicazionePiuVendutaDto) {
			PubblicazionePiuVendutaDto dto = (PubblicazionePiuVendutaDto) obj;
			return this.getCodicePubblicazione().equals(dto.getCodicePubblicazione());
		}
		return super.equals(obj);
	}
	
}
