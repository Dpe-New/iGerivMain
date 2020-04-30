package it.dpe.ws.client.dto.abbonati;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ritiro")
public class RitiriParamDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String tessera;
	private String titolo;
	private String idEditore;
	private String codFiegDl;
	private String idRivendita;
	private String idProdotto;
	private String edizione;
	private String barcode;
	private Integer codTipologiaMinicard;
	private String operation;
	
	public String getTessera() {
		return tessera;
	}

	public void setTessera(String tessera) {
		this.tessera = tessera;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getIdEditore() {
		return idEditore;
	}

	public void setIdEditore(String idEditore) {
		this.idEditore = idEditore;
	}
	
	public String getCodFiegDl() {
		return codFiegDl;
	}

	public void setCodFiegDl(String codFiegDl) {
		this.codFiegDl = codFiegDl;
	}

	public String getIdRivendita() {
		return idRivendita;
	}

	public void setIdRivendita(String idRivendita) {
		this.idRivendita = idRivendita;
	}

	public String getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getEdizione() {
		return edizione;
	}

	public void setEdizione(String edizione) {
		this.edizione = edizione;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public Integer getCodTipologiaMinicard() {
		return codTipologiaMinicard;
	}

	public void setCodTipologiaMinicard(Integer codTipologiaMinicard) {
		this.codTipologiaMinicard = codTipologiaMinicard;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
}
