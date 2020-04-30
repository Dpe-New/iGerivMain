package it.dpe.igeriv.vo;

import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.pk.MancanzaPk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella delle mancanze segnalate dall'edicola.
 * 
 * @author romanom
 *
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9613", schema = "")
public class MancanzaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private MancanzaPk pk;
	@Column(name = "datbc9613")
	private Timestamp dtBolla;
	@Column(name = "tipbc9613")
	private String tipoBolla;
	@Column(name = "quant9613")
	private Integer quantita;
	@Column(name = "darisp9613")
	private Timestamp dtRisposta;
	@Column(name = "stm9613")
	private Integer stato;
	@Column(name = "mess9613")
	private String note;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9613", updatable = false, insertable = false, referencedColumnName = "coddl9607"),
		@JoinColumn(name = "idtn9613", updatable = false, insertable = false, referencedColumnName = "idtn9607")
	})
	private StoricoCopertineVo copertina;
	
	public String getStatoDesc() {
		if (stato != null) {
			if (stato.equals(IGerivConstants.COD_STATO_MANCANZE_INSERITO)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_INSERITO_TEXT);
			} else if (stato.equals(IGerivConstants.COD_STATO_MANCANZE_SOSPESO)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_SOSPESO_TEXT);
			} else if (stato.equals(IGerivConstants.COD_STATO_MANCANZE_ACCREDITATA)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_ACCREDITATA_TEXT);
			} else if (stato.equals(IGerivConstants.COD_STATO_MANCANZE_NON_ACCREDITATA)) {
				return IGerivMessageBundle.get(IGerivConstants.STATO_MANCANZE_NON_ACCREDITATA_TEXT);
			}
		}
		return "";
	}
}
