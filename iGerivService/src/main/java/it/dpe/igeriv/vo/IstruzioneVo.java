package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9350", schema = "")
public class IstruzioneVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codist9350")
	private Integer codice;
	@Column(name = "descis9350")
	private String descrizione;
}
