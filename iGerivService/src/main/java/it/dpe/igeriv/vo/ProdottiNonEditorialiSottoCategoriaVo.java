package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiSottoCategoriaPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Sottocategorie Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9522", schema = "")
public class ProdottiNonEditorialiSottoCategoriaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ProdottiNonEditorialiSottoCategoriaPk pk;
	@Column(name = "descr9522")
	private String descrizione;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "cat9522", updatable = false, insertable = false, referencedColumnName = "cat9521") 
	})
	private ProdottiNonEditorialiCategoriaVo categoria;
	@Column(name = "nomimg9522")
	private String immagine;
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			ProdottiNonEditorialiSottoCategoriaVo pc = (ProdottiNonEditorialiSottoCategoriaVo) obj;
			return getPk().equals(pc.getPk());
		}
		return super.equals(obj);
	}
	
}
