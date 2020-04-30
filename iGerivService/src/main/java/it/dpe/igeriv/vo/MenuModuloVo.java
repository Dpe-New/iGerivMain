package it.dpe.igeriv.vo;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivQueryContants;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Strings;

/**
 * Tabella gruppi moduli
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries( {
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_MENU_BY_IDS, query = "from MenuModuloVo vo where vo.id in (:ids)")
})
@Table(name = "tbl_9208", schema = "")
public class MenuModuloVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codmo9208")
	private Integer id;
	@Column(name = "titmo9208")
	private String titolo;
	@Column(name = "desmo9208")
	private String descrizione;
	@Column(name = "urlmo9208")
	private String url;
	@Column(name = "actnm9208")
	private String actionName;
	@Column(name = "codmp9208")
	private Integer idModuloPadre;
	@Column(name = "posmo9208")
	private Integer posizioneItem;
	@Column(name = "posme9208")
	private Integer posizioneMenu;
	@Column(name = "modpa9208")
	@Getter(AccessLevel.NONE)
	private Boolean moduloPadre;
	@Column(name = "livel9208")
	private Integer livello;
	@Transient
	private boolean attivo;
	@Column(name = "codut9208")
	private String codUtente;
	@Column(name = "daulmo9208")
	private Timestamp dtUltimaModifica;
	@Column(name = "propky9208")
	private String propKey;
	
	public Boolean isModuloPadre() {
		return (moduloPadre == null) ? false : moduloPadre;
	}
	
	public String getTitolo18N() {
		if (!Strings.isNullOrEmpty(getPropKey())) {
			return IGerivMessageBundle.get(getPropKey());
		}
		return getTitolo();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null) {
			MenuModuloVo vo = (MenuModuloVo) obj;
			equals = this.getId().equals(vo.getId());
		}
		return equals;
	}
}
