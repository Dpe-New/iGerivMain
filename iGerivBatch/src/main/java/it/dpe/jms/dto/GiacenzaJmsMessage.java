package it.dpe.jms.dto;

/**
 * Giacenza Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
public class GiacenzaJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = 9115314907023378221L;
	private Long codProdottoInterno;
	private Integer numeroMagazzino;
	private Integer giacenza;

	public Long getCodProdottoInterno() {
		return codProdottoInterno;
	}

	public void setCodProdottoInterno(Long codProdottoInterno) {
		this.codProdottoInterno = codProdottoInterno;
	}

	public Integer getNumeroMagazzino() {
		return numeroMagazzino;
	}

	public void setNumeroMagazzino(Integer numeroMagazzino) {
		this.numeroMagazzino = numeroMagazzino;
	}

	public Integer getGiacenza() {
		return giacenza == null ? 0 : giacenza;
	}

	public void setGiacenza(Integer giacenza) {
		this.giacenza = giacenza;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("codProdottoInterno = " + codProdottoInterno + "\t");
		sb.append("numeroMagazzino = " + numeroMagazzino + "\t");
		sb.append("giacenza = " + giacenza + "\t");
		return sb.toString();
	}

}
