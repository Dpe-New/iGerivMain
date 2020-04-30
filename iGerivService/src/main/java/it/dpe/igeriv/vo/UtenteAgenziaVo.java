package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_UTENTE_AGENZIA_BY_ID_JOINS, query = "from UtenteAgenziaVo vo join fetch vo.agenzia join fetch vo.dlGruppoModuliVo join fetch vo.dlGruppoModuliVo.gruppoModuli join fetch vo.dlGruppoModuliVo.gruppoModuli.moduli where vo.codUtente = :codUtente")
@Table(name = "tbl_9107B", schema = "")
public class UtenteAgenziaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codutdl9107B")
	private Integer codUtente;
	@Column(name = "coddl9107B")
	private Integer codFiegDl;
	@Column(name = "passwdl9107B")
	private String passwordDl;
	@Column(name = "capwd9107B")
	private Boolean changePassword;
	@Column(name = "codgr9107B")
	private Integer codGruppo;
	@Column(name = "emaildl9107B")
	private String email;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "coddl9107B", updatable = false, insertable = false, referencedColumnName = "coddl9107")
	private AnagraficaAgenziaVo agenzia;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "codgr9107B", updatable = false, insertable = false, referencedColumnName = "codgr9212"),
		@JoinColumn(name = "coddl9107B", updatable = false, insertable = false, referencedColumnName = "coddl9212")
	})
	private DlGruppoModuliVo dlGruppoModuliVo;
}
