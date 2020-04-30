package it.dpe.jms.dto;

/**
 * Fornitori dell'Edicola di Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
public class CategorieJmsMessage extends BaseJmsMessage {
	private static final long serialVersionUID = -2199045176974858000L;
	private Integer descrizioneLivello1;
	private Integer descrizioneLivello2;
	private String descrizione;

	public Integer getDescrizioneLivello1() {
		return descrizioneLivello1;
	}

	public void setDescrizioneLivello1(Integer descrizioneLivello1) {
		this.descrizioneLivello1 = descrizioneLivello1;
	}

	public Integer getDescrizioneLivello2() {
		return descrizioneLivello2;
	}

	public void setDescrizioneLivello2(Integer descrizioneLivello2) {
		this.descrizioneLivello2 = descrizioneLivello2;
	}

	public String getDescrizione() {
		return descrizione != null ? descrizione.trim() : descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("descrizioneLivello1 = " + descrizioneLivello1 + "\t");
		sb.append("descrizioneLivello2 = " + descrizioneLivello2 + "\t");
		sb.append("descrizione = " + descrizione + "\t");
		return sb.toString();
	}

}
