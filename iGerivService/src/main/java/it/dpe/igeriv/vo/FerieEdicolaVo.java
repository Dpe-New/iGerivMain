package it.dpe.igeriv.vo;


import it.dpe.igeriv.vo.pk.FerieEdicolePk;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9228", schema = "")
public class FerieEdicolaVo  extends BaseVo{
	private static final long serialVersionUID = 1L;
	@Id
	private FerieEdicolePk pk;
	
}
