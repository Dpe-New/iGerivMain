package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.vo.pk.FattureEdicolaPdfPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9646", schema = "")
public class FattureEdicolaPdfVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private FattureEdicolaPdfPk pk;
	@Column(name = "nfile9646")
	private String nomeFile;
	
	public String getDataNumeroFatturaStr() {
		return getPk().getDataFattura()==null ? null : String.format("%td-%<tm-%<tY N. %d", getPk().getDataFattura(), getPk().getNumeroFattura());
	}

	public String getKey() {
		return String.format("%td-%<tm-%<tY|%d", getPk().getDataFattura(), getPk().getNumeroFattura());
	}
	
	
}
