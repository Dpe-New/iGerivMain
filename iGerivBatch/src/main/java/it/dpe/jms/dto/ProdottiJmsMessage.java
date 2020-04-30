package it.dpe.jms.dto;

import com.google.common.base.Strings;

/**
 * Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
public class ProdottiJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = -6517659794408272873L;
	private Long codProdottoInterno;
	private String codProdottoEsterno;
	private String descrizioneProdottoA;
	private String descrizioneProdottoB;
	private Integer codCategoria;
	private Integer codSottoCategoria;
	private String barcode;
	private String unitaMisura;
	private Integer aliquota;
	private Float unitaMinimaIncremento;
	private byte[] immagine;
	private String obsoleto;
	private Integer formazionePacco;
	private Float percentualeResaSuDistribuito;
	private Float prezzoVenditaConsigliato;
	
	public Long getCodProdottoInterno() {
		return codProdottoInterno;
	}

	public void setCodProdottoInterno(Long codProdottoInterno) {
		this.codProdottoInterno = codProdottoInterno;
	}

	public String getCodProdottoEsterno() {
		return codProdottoEsterno != null ? codProdottoEsterno.trim() : codProdottoEsterno;
	}

	public void setCodProdottoEsterno(String codProdottoEsterno) {
		this.codProdottoEsterno = codProdottoEsterno;
	}

	public String getDescrizioneProdottoA() {
		return descrizioneProdottoA != null ? descrizioneProdottoA.trim() : descrizioneProdottoA;
	}

	public void setDescrizioneProdottoA(String descrizioneProdottoA) {
		this.descrizioneProdottoA = descrizioneProdottoA;
	}

	public String getDescrizioneProdottoB() {
		return descrizioneProdottoB != null ? descrizioneProdottoB.trim() : descrizioneProdottoB;
	}

	public void setDescrizioneProdottoB(String descrizioneProdottoB) {
		this.descrizioneProdottoB = descrizioneProdottoB;
	}

	public Integer getCodCategoria() {
		return codCategoria;
	}

	public void setCodCategoria(Integer codCategoria) {
		this.codCategoria = codCategoria;
	}

	public Integer getCodSottoCategoria() {
		return codSottoCategoria == null ? 0 : codSottoCategoria;
	}

	public void setCodSottoCategoria(Integer codSottoCategoria) {
		this.codSottoCategoria = codSottoCategoria;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getUnitaMisura() {
		return unitaMisura;
	}

	public void setUnitaMisura(String unitaMisura) {
		this.unitaMisura = unitaMisura;
	}

	public Integer getAliquota() {
		return aliquota;
	}

	public void setAliquota(Integer aliquota) {
		this.aliquota = aliquota;
	}

	public Float getUnitaMinimaIncremento() {
		return unitaMinimaIncremento;
	}

	public void setUnitaMinimaIncremento(Float unitaMinimaIncremento) {
		this.unitaMinimaIncremento = unitaMinimaIncremento;
	}
	
	public byte[] getImmagine() {
		return immagine;
	}

	public void setImmagine(byte[] immagine) {
		this.immagine = immagine;
	}
	
	public String getObsoleto() {
		return obsoleto;
	}

	public void setObsoleto(String obsoleto) {
		this.obsoleto = obsoleto;
	}
	
	public Boolean isObsoleto() {
		return (!Strings.isNullOrEmpty(getObsoleto()) && getObsoleto().equals("S")) ? true : false;
	}
	
	public Integer getFormazionePacco() {
		return formazionePacco;
	}

	public void setFormazionePacco(Integer formazionePacco) {
		this.formazionePacco = formazionePacco;
	}
	
	public Float getPercentualeResaSuDistribuito() {
		return percentualeResaSuDistribuito;
	}

	public void setPercentualeResaSuDistribuito(Float percentualeResaSuDistribuito) {
		this.percentualeResaSuDistribuito = percentualeResaSuDistribuito;
	}
	
	public Float getPrezzoVenditaConsigliato() {
		return prezzoVenditaConsigliato;
	}

	public void setPrezzoVenditaConsigliato(Float prezzoVenditaConsigliato) {
		this.prezzoVenditaConsigliato = prezzoVenditaConsigliato;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("codProdottoInterno = " + codProdottoInterno + "\t");
		sb.append("codProdottoEsterno = " + codProdottoEsterno + "\t");
		sb.append("descrizioneProdottoA = " + descrizioneProdottoA + "\t");
		sb.append("descrizioneProdottoB = " + descrizioneProdottoB + "\t");
		sb.append("codCategoria = " + codCategoria + "\t");
		sb.append("codSottoCategoria = " + codSottoCategoria + "\t");
		sb.append("barcode = " + barcode + "\t");
		sb.append("unitaMisura = " + unitaMisura + "\t");
		sb.append("aliquota = " + aliquota + "\t");
		sb.append("unitaMinimaIncremento = " + unitaMinimaIncremento + "\t");
		sb.append("obsoleto = " + obsoleto + "\t");
		sb.append("formazionePacco = " + formazionePacco + "\t");
		sb.append("percentualeResaSuDistribuito = " + percentualeResaSuDistribuito + "\t");
		sb.append("prezzoVenditaConsigliato = " + prezzoVenditaConsigliato + "\t");
		return sb.toString();
	}

}
