package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.LavorazioneResaImmaginePk;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Formula;

/**
 * Tabella con le immagini delle lavorazioni delle rese delle edicole dal redic.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9706", schema = "")
public class LavorazioneResaImmagineVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private LavorazioneResaImmaginePk pk;
	@Column(name = "copie9706")
	private Integer copie;
	@Column(name = "nomim9706")
	private String nomeImmagine;
	@Formula(value = "(select t2.titolo9606 from tbl_9607 t1, tbl_9606 t2 where t1.coddl9607 = t2.coddl9606 and t1.cpu9607 = t2.cpu9606 and t1.coddl9607 = coddl9706 and t1.idtn9607 = idtn9706)")
	@Basic(fetch = FetchType.LAZY)
	private String titolo;
	@Formula(value = "(select t1.num9607 from tbl_9607 t1 where t1.coddl9607 = coddl9706 and t1.idtn9607 = idtn9706)")
	@Basic(fetch = FetchType.LAZY)
	private String numeroCopertina;
	@Column(name = "coddl9706")
	private Integer codFiegDl;
}
