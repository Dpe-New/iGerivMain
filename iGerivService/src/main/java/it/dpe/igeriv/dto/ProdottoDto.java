package it.dpe.igeriv.dto;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

import org.apache.commons.lang.WordUtils;

import com.google.common.base.Strings;

@Getter
@Setter
public class ProdottoDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Long codProdottoInterno;
	private String codProdottoEsterno;
	private String descrizione;
	private String descrizioneB;
	@Getter(AccessLevel.NONE)
	private String categoria;
	@Getter(AccessLevel.NONE)
	private String sottocategoria;
	private String barcode;
	@Getter(AccessLevel.NONE)
	private Float prezzo;
	private Float sconto;
	private Integer giacenza;
	private String unitaMisura;
	private Integer aliquota;
	private Float unitaMinimaIncremento;
	private String nomeImmagine;
	private Long codCategoria;
	private Long codSubCategoria;
	private String codiceProdottoFornitore;
	private Float ultimoPrezzoAcquisto;
	private Integer posizione;
	@Getter(AccessLevel.NONE)
	private Integer giacenzaProdotto;
	@Getter(AccessLevel.NONE)
	private Integer giacenzaInizialeProdotto;
	private String immagine;
	@Getter(AccessLevel.NONE)
	private Boolean prodottoDl;
	@Getter(AccessLevel.NONE)
	private Float percentualeResaSuDistribuito;
	private Boolean escludiDalleVendite;
	//GIFT CARD EPIPOLI
	@Getter(AccessLevel.NONE)
	private Boolean prodottoDigitale;
	private String isProdottoDigitale;
	private Integer codiceFornitore;

	
	
	public String getCategoria() {
		return !Strings.isNullOrEmpty(categoria) ? WordUtils.capitalizeFully(categoria) : "";
	}

	public String getSottocategoria() {
		return !Strings.isNullOrEmpty(sottocategoria) ? WordUtils.capitalizeFully(sottocategoria) : "";
	}

	public Float getPrezzo() {
		return prezzo == null ? 0 : prezzo;
	}

	public Integer getAliquota() {
		return (aliquota == null) ? 0 : aliquota;
	}

	public Integer getGiacenzaProdotto() {
		return giacenzaProdotto == null ? 0 : giacenzaProdotto;
	}

	public Integer getGiacenzaInizialeProdotto() {
		return giacenzaInizialeProdotto == null ? 0 : giacenzaInizialeProdotto;
	}

	public Float getPrezzoConAliquota() {
		return (getUltimoPrezzoAcquisto() != null && getAliquota() != null) ? (getUltimoPrezzoAcquisto() + ((getUltimoPrezzoAcquisto() / 100) * getAliquota())) : 0;
	}
	
	public String getPrezzoFormat() {
		return (prezzo != null) ? IGerivConstants.EURO_SIGN_HTML + " " + NumberUtils.formatNumber(prezzo) : "";
	}
	
	public Float getPercentualeResaSuDistribuito() {
		return percentualeResaSuDistribuito == null ? 0f : percentualeResaSuDistribuito;
	}
	
	public Boolean getProdottoDl() {
		return prodottoDl == null ? false : prodottoDl;
	}
	
	public Boolean getProdottoDigitale() {
		return isProdottoDigitale.equals("N") ? false : true;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			return ((ProdottoDto) obj).getCodProdottoInterno().equals(this.getCodProdottoInterno());
		}
		return super.equals(obj);
	}

	
}
