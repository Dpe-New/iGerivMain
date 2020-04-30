package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.MessaggioRegistratoreCassaPk;

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
@Table(name = "tbl_9712", schema = "")
public class MessaggioRegistratoreCassaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private MessaggioRegistratoreCassaPk pk;
	@Column(name= "mvisto9712")
	private Boolean messaggioVisto;
}
