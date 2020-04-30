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
@Table(name = "tbl_9308", schema = "")
public class MetodoPagamentoClienteVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "tipopag9308")
	private Integer codMetodoPagamento;
	@Column(name = "descpag9308")
	private String descrizione;
}
