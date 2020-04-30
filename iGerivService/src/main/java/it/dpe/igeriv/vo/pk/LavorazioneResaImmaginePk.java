package it.dpe.igeriv.vo.pk;

import it.dpe.igeriv.vo.LavorazioneResaVo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LavorazioneResaImmaginePk extends BasePk {
	private static final long serialVersionUID = 1L;
	@Column(name = "idtn9706")
	private Integer idtn;
	@Column(name = "dalav9706")
	private Timestamp dataOraLavorazione;
	@ManyToOne
	@JoinColumn(name = "nomfil9706", updatable = true, insertable = true, referencedColumnName = "nomfil9705")
	private LavorazioneResaVo lavorazioneResaVo;
}
