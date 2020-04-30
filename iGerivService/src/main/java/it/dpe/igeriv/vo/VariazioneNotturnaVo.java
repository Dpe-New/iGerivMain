package it.dpe.igeriv.vo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mromano
 *
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9612B", schema = "")
public class VariazioneNotturnaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idvar9612B")
	private Integer idVariazione;
	@Column(name = "coddl9612B")
	private Integer codFiegDl;
	@Column(name = "crivw9612B")
	private Integer codEdicola;
	@Column(name = "datbc9612B")
	private Timestamp dtBolla;
	@Column(name = "tipbc9612B")
	private String tipoBolla;
	@Column(name = "idtn9612B")
	private Integer idtn;
	@Column(name = "quant9612B")
	private Integer quantita;
}
