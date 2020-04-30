package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Categorie Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9521", schema = "")
public class ProdottiNonEditorialiCategoriaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "cat9521")
	private Long codCategoria;
	@Column(name = "descr9521")
	private String descrizione;
	@Column(name = "nomimg9521")
	private String immagine;
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			ProdottiNonEditorialiCategoriaVo pc = (ProdottiNonEditorialiCategoriaVo) obj;
			return getCodCategoria().equals(pc.getCodCategoria());
		}
		return super.equals(obj);
	}
	
}
