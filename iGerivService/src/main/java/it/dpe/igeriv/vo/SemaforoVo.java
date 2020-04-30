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
@Table(name = "tbl_9560", schema = "")
public class SemaforoVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codpr9560")
	private Integer codProcesso;
	@Column(name = "dtini9560")
	private Timestamp dtInizio;
	@Column(name = "dtfin9560")
	private Timestamp dtFine;
	
	public boolean isAvailable() {
		if (getDtInizio() == null && getDtFine() == null) {
			return true;
		} else if ((getDtInizio() != null && getDtFine() == null) || (getDtInizio() != null && getDtFine() != null && getDtFine().before(getDtInizio()))) {
			return false;
		}
		return true;
	}

}
