package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.DecodificaRichiamiResaPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella di decodifica richiami resa.
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9624", schema = "")
public class DecodificaRichiamiResaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private DecodificaRichiamiResaPk pk;
	@Column(name = "descr9624")
	private String descrizione;
	@Column(name = "descrb9624")
	private String descrizioneBreve;
}
