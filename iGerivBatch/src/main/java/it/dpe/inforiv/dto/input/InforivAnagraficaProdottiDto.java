package it.dpe.inforiv.dto.input;

import it.dpe.igeriv.exception.InvalidRecordException;
import it.dpe.igeriv.resources.IGerivMessageBundle;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatNumber;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;
import com.ancientprogramming.fixedformat4j.annotation.Sign;

/**
 * @author romanom
 * 
 */
@Component("InforivAnagraficaProdottiDto")
@Scope("prototype")
@Record
public class InforivAnagraficaProdottiDto extends InforivBaseDto implements InforivDto {
	private static final long serialVersionUID = 1L;
	private static final int lineLength = 156;
	private boolean isAnagraficaProdotto = true;
	private String tipoRecord;
	private Integer codFiegDl;
	private Integer codEdicola;
	private String idProdotto;
	private Integer codiceTestata;
	private String numeroCopertina;
	private Date dataUscita;
	private String titolo;
	private String sottotitolo;
	private String periodicita;
	private Float prezzoCopertina;
	private Float prezzoNetto;
	private Integer codiceTestataInforete;
	private Integer varianteProdottoInforete;
	private String numeroInforeteOAddon;
	private String barcode;
	private String tipoProdotto;
	private Integer segmentoMercato;
	private Integer codiceEditore;
	private Integer numeroUsciteOpera;
	private Integer giorniPermanenzaEdicola;
	private String scontoParticolare;
	private String barcodeCompleto;

	@Field(offset = 1, length = 2)
	public String getTipoRecord() {
		return tipoRecord != null ? tipoRecord.trim() : tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}
	
	@Field(offset = 3, length = 3)
	public Integer getCodFiegDl() {
		return codFiegDl;
	}

	public void setCodFiegDl(Integer codFiegDl) {
		this.codFiegDl = codFiegDl;
	}
	
	@Field(offset = 6, length = 4)
	public Integer getCodEdicola() {
		return codEdicola;
	}

	public void setCodEdicola(Integer codEdicola) {
		this.codEdicola = codEdicola;
	}
	
	@Field(offset = 10, length = 18)
	public String getIdProdotto() {
		return idProdotto != null ? idProdotto.trim() : idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}
	
	@Field(offset = 28, length = 9)
	public Integer getCodiceTestata() {
		return codiceTestata;
	}

	public void setCodiceTestata(Integer codiceTestata) {
		this.codiceTestata = codiceTestata;
	}
	
	@Field(offset = 37, length = 8)
	public String getNumeroCopertina() {
		return numeroCopertina != null ? numeroCopertina.trim() : numeroCopertina;
	}

	public void setNumeroCopertina(String numeroCopertina) {
		this.numeroCopertina = numeroCopertina;
	}
	
	@Field(offset = 45, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataUscita() {
		return dataUscita;
	}

	public void setDataUscita(Date dataUsicta) {
		this.dataUscita = dataUsicta;
	}
	
	@Field(offset = 53, length = 20)
	public String getTitolo() {
		return titolo != null ? titolo.trim() : titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	
	@Field(offset = 73, length = 20)
	public String getSottotitolo() {
		return sottotitolo != null ? sottotitolo.trim() : sottotitolo;
	}

	public void setSottotitolo(String sottotitolo) {
		this.sottotitolo = sottotitolo;
	}
	
	@Field(offset = 93, length = 2)
	public String getPeriodicita() {
		return periodicita != null ? periodicita.trim() : periodicita;
	}

	public void setPeriodicita(String periodicita) {
		this.periodicita = periodicita;
	}
	
	@Field(offset = 95, length = 6)
	@FixedFormatDecimal(decimals = 2)
	public Float getPrezzoCopertina() {
		return prezzoCopertina;
	}

	public void setPrezzoCopertina(Float prezzoCopertina) {
		this.prezzoCopertina = prezzoCopertina;
	}
	
	@Field(offset = 101, length = 11)
	@FixedFormatDecimal(decimals = 6)
	@FixedFormatNumber(sign = Sign.APPEND, positiveSign = ' ')
	public Float getPrezzoNetto() {
		return prezzoNetto;
	}

	public void setPrezzoNetto(Float prezzoNetto) {
		this.prezzoNetto = prezzoNetto;
	}
	
	@Field(offset = 112, length = 7)
	public Integer getCodiceTestataInforete() {
		return codiceTestataInforete;
	}

	public void setCodiceTestataInforete(Integer codiceTestataInforete) {
		this.codiceTestataInforete = codiceTestataInforete;
	}
	
	@Field(offset = 119, length = 2)
	public Integer getVarianteProdottoInforete() {
		return varianteProdottoInforete;
	}

	public void setVarianteProdottoInforete(Integer varianteProdottoInforete) {
		this.varianteProdottoInforete = varianteProdottoInforete;
	}
	
	@Field(offset = 121, length = 5)
	public String getNumeroInforeteOAddon() {
		return numeroInforeteOAddon != null ? numeroInforeteOAddon.trim() : numeroInforeteOAddon;
	}

	public void setNumeroInforeteOAddon(String numeroInforeteOAddon) {
		this.numeroInforeteOAddon = numeroInforeteOAddon;
	}
	
	@Field(offset = 126, length = 13)
	public String getBarcode() {
		return barcode != null ? barcode.trim() : barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	@Field(offset = 139, length = 1)
	public String getTipoProdotto() {
		return tipoProdotto != null ? tipoProdotto.trim() : tipoProdotto;
	}

	public void setTipoProdotto(String tipoProdotto) {
		this.tipoProdotto = tipoProdotto;
	}
	
	@Field(offset = 140, length = 3)
	public Integer getSegmentoMercato() {
		return segmentoMercato;
	}

	public void setSegmentoMercato(Integer segmentoMercato) {
		this.segmentoMercato = segmentoMercato;
	}
	
	@Field(offset = 143, length = 7)
	public Integer getCodiceEditore() {
		return codiceEditore;
	}

	public void setCodiceEditore(Integer codiceEditore) {
		this.codiceEditore = codiceEditore;
	}
	
	@Field(offset = 150, length = 3)
	public Integer getNumeroUsciteOpera() {
		return numeroUsciteOpera;
	}

	public void setNumeroUsciteOpera(Integer numeroUsciteOpera) {
		this.numeroUsciteOpera = numeroUsciteOpera;
	}
	
	@Field(offset = 153, length = 3)
	public Integer getGiorniPermanenzaEdicola() {
		return giorniPermanenzaEdicola;
	}

	public void setGiorniPermanenzaEdicola(Integer giorniPermanenzaEdicola) {
		this.giorniPermanenzaEdicola = giorniPermanenzaEdicola;
	}
	
	@Field(offset = 156, length = 1)
	public String getScontoParticolare() {
		return scontoParticolare != null ? scontoParticolare.trim() : scontoParticolare;
	}

	public void setScontoParticolare(String scontoParticolare) {
		this.scontoParticolare = scontoParticolare;
	}
	
	@Field(offset = 157, length = 18)
	public String getBarcodeCompleto() {
		return barcodeCompleto != null ? barcodeCompleto.trim() : barcodeCompleto;
	}

	public void setBarcodeCompleto(String barcodeCompleto) {
		this.barcodeCompleto = barcodeCompleto;
	}
	
	public boolean isAnagraficaProdotto() {
		return isAnagraficaProdotto;
	}

	public void setAnagraficaProdotto(boolean isAnagraficaProdotto) {
		this.isAnagraficaProdotto = isAnagraficaProdotto;
	}

	@Override
	public void validate(String riga) throws InvalidRecordException {
		super.validate(riga);
		/*if (riga.length() != lineLength) {
			throw new InvalidRecordException(MessageFormat.format(IGerivMessageBundle.get("dpe.validation.msg.lunghezza.errata.riga"), riga.length(), lineLength));
		} else*/ if (getCodFiegDl().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.codice.dl.nullo"));
		} else if (getIdProdotto().trim().equals("")) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.idtn.nullo"));
		} else if (getCodiceTestata().equals(0)) {
			throw new InvalidRecordException(IGerivMessageBundle.get("dpe.validation.msg.cpu.nullo"));
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof InforivAnagraficaProdottiDto) {
			InforivAnagraficaProdottiDto dto = (InforivAnagraficaProdottiDto) obj;
			if (isAnagraficaProdotto()) {
				return this.getCodFiegDl().equals(dto.getCodFiegDl()) && this.getCodiceTestata().equals(dto.getCodiceTestata());
			} else {
				return this.getCodFiegDl().equals(dto.getCodFiegDl()) && this.getIdProdotto().equals(dto.getIdProdotto());
			}
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + ((getCodFiegDl() == null) ? 0 : getCodFiegDl());
		if (isAnagraficaProdotto()) {
			hash = 31 * hash + ((getCodiceTestata() == null) ? 0 : getCodiceTestata());
		} else {
			hash = 31 * hash + ((getIdProdotto() == null) ? 0 : getIdProdotto().hashCode());
		}
		return hash;
	}

}
