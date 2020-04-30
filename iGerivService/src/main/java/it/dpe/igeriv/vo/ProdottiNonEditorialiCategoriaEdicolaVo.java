package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiCategoriaEdicolaPk;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Categorie Edicola Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9523", schema = "")
public class ProdottiNonEditorialiCategoriaEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ProdottiNonEditorialiCategoriaEdicolaPk pk;
	@Column(name = "descr9523")
	private String descrizione;
	@Column(name = "nomimg9523")
	private String immagine;
	@ManyToOne
	@JoinColumn(name = "crivw9523", insertable = false, updatable = false, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola;
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true, targetEntity = ProdottiNonEditorialiSottoCategoriaEdicolaVo.class, mappedBy = "categoria")
	private List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> sottocategorie;
	@Column(name = "posiz9523")
	private Integer posizione;

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			ProdottiNonEditorialiCategoriaEdicolaVo pc = (ProdottiNonEditorialiCategoriaEdicolaVo) obj;
			return getPk().getCodCategoria().equals(pc.getPk().getCodCategoria());
		}
		return super.equals(obj);
	}
	
}
