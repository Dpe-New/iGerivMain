package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ContestazioneResaPk;

import java.sql.Timestamp;

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
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9628", schema = "")
public class ContestazioneResaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ContestazioneResaPk pk;
	@Column(name = "dare9628")
	private Timestamp dtBolla;
	@Column(name = "tire9628")
	private String tipoBolla;
	@Column(name = "crivw9628")
	private Integer codEdicola;
	@Column(name = "datr9628")
	private Timestamp dataRegistrazione;
	@Column(name = "str9628")
	private Integer stato;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9628", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9628", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo copertina;
	@Column(name = "qua9628")
	private Integer quantita;
	@Column(name = "quaapp9628")
	private Integer quantitaApprovata;
	@Column(name = "impval9628")
	private Float importoValore;
	@Column(name = "impapp9628")
	private Float importoValoreApprovato;
	@Column(name = "dataec9628")
	private Timestamp dataEstrattoConto;
	@Column(name = "cauec9628")
	private Integer causale;
	@Column(name = "motrif9628")
	private String motivazioneRifiuto;
	@Column(name = "notech9628")
	private String noteChiusura;
}
