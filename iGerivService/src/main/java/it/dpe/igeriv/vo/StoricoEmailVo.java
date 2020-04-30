package it.dpe.igeriv.vo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "tbl_9225", schema = "")
public class StoricoEmailVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codem9225")
	private Long codEmail;
	@Column(name = "crivw9225")
	private Integer codEdicola;
	@Column(name = "codut9225")
	private Integer codUtente;
	@Column(name = "tiput9225")
	private Integer tipoUtente;
	@Column(name = "email9225")
	private String email;
	@Column(name = "testo9225")
	private String testo;
	@Column(name = "datinv9225")
	private Timestamp dataInvio;
}
