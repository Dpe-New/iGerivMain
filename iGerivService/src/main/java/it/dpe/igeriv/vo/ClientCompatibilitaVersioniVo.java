package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * Tabella compatibilita' versioni client
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9528", schema = "")
public class ClientCompatibilitaVersioniVo extends BaseVo {
	private static final long serialVersionUID = -6670587303689584691L;
	@Id
	@Column(name = "codcom9528")
	private Integer idCompatibilita;
	@Column(name = "osdev9528")
	private String os;
	@Column(name = "clivda9528")
	private Float clientVersionDa;
	@Column(name = "clivea9528")
	private Float clientVersionA;
}
