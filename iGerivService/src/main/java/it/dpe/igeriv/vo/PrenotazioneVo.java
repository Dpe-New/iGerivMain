package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.pk.PrenotazionePk;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella delle variazioni prenotazione.
 * 
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@NamedQueries( {
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_PRENOTAZIONE_BY_ID, query = "from PrenotazioneVo vo where vo.pk.codDl = :codDl and vo.pk.codicePubblicazione = :codicePubblicazione and vo.pk.codEdicola = :codEdicola")
})
@Table(name = "tbl_9129", schema = "")
public class PrenotazioneVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private PrenotazionePk pk;
	@Column(name = "quar9129")
	private Integer quantitaRichiesta;
	@Column(name = "moti9129")
	private String motivoRichiesta;
	@Column(name = "itdl9129")
	private Integer indicatoreTrasmessoDl;
	@Column(name = "datr9129")
	private Timestamp dataUltimaTrasmissioneDl;
	@Column(name = "codut9129")
	private Integer codUtente;
	@Transient
	private boolean enabled = true;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "coddl9129", updatable = false, insertable = false, referencedColumnName = "coddl9606"),
		@JoinColumn(name = "cpu9129", updatable = false, insertable = false, referencedColumnName = "cpu9606")
	})
	private AnagraficaPubblicazioniVo pubblicazione;
}
