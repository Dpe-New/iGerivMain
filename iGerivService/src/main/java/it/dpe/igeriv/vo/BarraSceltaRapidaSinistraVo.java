package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.vo.pk.BarraSceltaRapidaSinistraPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Tabella Pubblicazioni Più Vendute
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9810", schema = "")
public class BarraSceltaRapidaSinistraVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private BarraSceltaRapidaSinistraPk pk;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9810", updatable = false, insertable = false, referencedColumnName = "coddl9606"),
		@JoinColumn(name = "cpu9810", updatable = false, insertable = false, referencedColumnName = "cpu9606")
	})
	private AnagraficaPubblicazioniVo anagraficaPubblicazioniVo;
	@Column(name = "pos9810")
	private Integer posizione; 
	@Column(name = "top9810")
	private Integer top;
	@Column(name = "lef9810")
	private Integer left;
	@Column(name = "wid9810")
	private Integer width;
	@Column(name = "hei9810")
	private Integer height;
	@Column(name = "nomimg9810")
	private String nomeImmagine;
	@Column(name = "tipimg9810")
	private Integer tipoImmagine;
	@Transient
	@Getter(AccessLevel.NONE)
	private String immagineDirAlias;
	
	public String getImmagineDirAlias() {
		return immagineDirAlias = tipoImmagine != null ? ((IGerivUtils) SpringContextManager.getService("iGerivUtils")).getImgAlias(tipoImmagine) : "";
	}
	
}
