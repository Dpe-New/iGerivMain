package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Prodotti Non Editoriali
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9501", schema = "")
public class ProdottiNonEditorialiGenericaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "cpro9501")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9501", allocationSize = 1)
	private Long codProdottoInterno;
	@Column(name = "cproe9501")
	private String codProdottoEsterno;
	@Column(name = "desc9501")
	private String descrizioneProdottoA;
	@Column(name = "descb9501")
	private String descrizioneProdottoB;
	@Column(name = "cat9501")
	private Long codCategoria;
	@Column(name = "scat9501")
	private Long codSottoCategoria;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "cat9501", updatable = false, insertable = false, referencedColumnName = "cat9521") 
	})
	private ProdottiNonEditorialiCategoriaVo categoria;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "cat9501", updatable = false, insertable = false, referencedColumnName = "cat9522"),
			@JoinColumn(name = "scat9501", updatable = false, insertable = false, referencedColumnName = "scat9522")
	})
	private ProdottiNonEditorialiSottoCategoriaVo sottocategoria;
	@Column(name = "barco9501")
	private String barcode;
	@Column(name = "um9501")
	private String unitaMisura;
	@Column(name = "aliq9501")
	private Integer aliquota;
	@Column(name = "unmin9501")
	private Float unitaMinimaIncremento;
	@Column(name = "nomimg9501")
	private String nomeImmagine;
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			Long cpi = null;
			if (obj instanceof ProdottiNonEditorialiVo) {
				cpi = ((ProdottiNonEditorialiVo) obj).getCodProdottoInterno();
			} else {
				cpi = ((ProdottiNonEditorialiGenericaVo) obj).getCodProdottoInterno();
			}
			return getCodProdottoInterno().equals(cpi);
		}
		return super.equals(obj);
	}
	
}
