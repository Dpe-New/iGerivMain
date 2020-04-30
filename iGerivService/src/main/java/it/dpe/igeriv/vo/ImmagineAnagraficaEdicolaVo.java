package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.ImmagineAnagraficaEdicolaPk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9113", schema = "")
public class ImmagineAnagraficaEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ImmagineAnagraficaEdicolaPk pk;
	@Column(name = "nfile9113")
	private String nomeImmagine;
	@Column(name = "daulm9113")
	private Timestamp dataUltimaModifica;
	@Column(name = "immag9113")
	private byte[] immagine;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9113", insertable = false, updatable = false, referencedColumnName = "crivw9112")
	private AnagraficaEdicolaAggiuntiviVo anagrafica;
}
