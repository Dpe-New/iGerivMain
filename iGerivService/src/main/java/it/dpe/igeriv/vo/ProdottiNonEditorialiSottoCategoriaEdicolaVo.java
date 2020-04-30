package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiSottoCategoriaEdicolaPk;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Sottocategorie Edicola Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9524", schema = "")
public class ProdottiNonEditorialiSottoCategoriaEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ProdottiNonEditorialiSottoCategoriaEdicolaPk pk;
	@Column(name = "descr9524")
	private String descrizione;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "cat9524", updatable = false, insertable = false, referencedColumnName = "cat9523"),
			@JoinColumn(name = "crivw9524", updatable = false, insertable = false, referencedColumnName = "crivw9523")
	})
	private ProdottiNonEditorialiCategoriaEdicolaVo categoria;
	@Column(name = "nomimg9524")
	private String immagine;
	@ManyToOne
	@JoinColumn(name = "crivw9524", insertable = false, updatable = false, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo edicola;
	@OneToMany(fetch=FetchType.LAZY, targetEntity = ProdottiNonEditorialiVo.class, mappedBy = "sottocategoria")
	private List<ProdottiNonEditorialiVo> prodotti;
	@Column(name = "posiz9524")
	private Integer posizione;

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			ProdottiNonEditorialiSottoCategoriaEdicolaVo pc = (ProdottiNonEditorialiSottoCategoriaEdicolaVo) obj;
			return getPk().equals(pc.getPk());
		}
		return super.equals(obj);
	}
	
	public String getDescrizioneNoHtml() {
		return descrizione != null ? StringUtility.stripHtml(descrizione) : descrizione;
	}
	
}
