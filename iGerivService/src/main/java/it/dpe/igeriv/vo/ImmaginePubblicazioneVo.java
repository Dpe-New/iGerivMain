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
@Table(name = "tbl_9700", schema = "")
public class ImmaginePubblicazioneVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "barcode9700")
	private String barcode;
	@Column(name = "nomei9700")
	private String nome;
}
