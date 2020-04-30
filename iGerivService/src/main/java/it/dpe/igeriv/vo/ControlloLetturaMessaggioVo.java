package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.ControlloLetturaMessaggioPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@NamedQueries({ 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_CONTROLLO_MESSAGGIO_RIVENDITA, query = "from ControlloLetturaMessaggioVo vo where vo.pk.codiceEdicola = :codiceEdicola and vo.pk.codFiegDl = :codFiegDl and vo.pk.dtMessaggio = :dtMessaggio")
})
@Table(name = "tbl_9602", schema = "")
public class ControlloLetturaMessaggioVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private ControlloLetturaMessaggioPk pk;
	@Column(name = "letto9602")
	private Integer messaggioLetto;
}
