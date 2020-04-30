package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

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
 * Tabella utenti
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries( {
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_USER_BY_CODICE, query = "from UserVo vo join fetch vo.dlGruppoModuliVo join fetch vo.dlGruppoModuliVo.gruppoModuli join fetch vo.abbinamentoEdicolaDlVo where vo.codUtente = :codUtente"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_SUB_USER_BY_EMAIL, query = "from UserVo vo where vo.email = :email"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_USER_BY_CODICE_EDICOLA_JOINS, query = "from UserVo vo join fetch vo.abbinamentoEdicolaDlVo join fetch vo.abbinamentoEdicolaDlVo.anagraficaAgenziaVo left join fetch vo.abbinamentoEdicolaDlVo.anagraficaAgenziaVo.agenziaSecondaria join fetch vo.abbinamentoEdicolaDlVo.anagraficaEdicolaVo as ae join fetch vo.dlGruppoModuliVo join fetch vo.dlGruppoModuliVo.gruppoModuli join fetch vo.dlGruppoModuliVo.gruppoModuli.moduli where vo.abbinamentoEdicolaDlVo.codDpeWebEdicola = :codDpeWebEdicola and vo.utenteAmministratore = 1"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_USER_AGENT, query = "update UserVo vo set vo.userAgent = :userAgent where vo.codUtente = :codUtente"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_UPDATE_PWD_EDICOLA, query = "update UserVo vo set vo.pwd = :password, changePassword = :changePassword, pwdCriptata = :pwdCriptata where vo.codUtente = :codEdicola")
})
@Table(name = "tbl_9207", schema = "")
public class UserVo extends BaseVo {
	private static final long serialVersionUID = 1L; 
	@Id
	@Column(name = "codut9207")
	private String codUtente;
	@Column(name = "nomut9207")
	private String nomeUtente;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9207", updatable = true, insertable = true, referencedColumnName = "crivw9206")
	private AbbinamentoEdicolaDlVo abbinamentoEdicolaDlVo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "codgr9207", updatable = true, insertable = true, referencedColumnName = "codgr9212"),
		@JoinColumn(name = "coddl9207", updatable = true, insertable = true, referencedColumnName = "coddl9212")
	})
	private DlGruppoModuliVo dlGruppoModuliVo;
	@Column(name = "paswd9207", updatable = true, insertable = true)
	private String pwd; 
	@Column(name = "email9207")
	private String email;
	@Column(name = "abili9207")
	private Integer abilitato;
	@Column(name = "capwd9207", updatable = true, insertable = true)
	private Integer changePassword;
	@Column(name = "utadm9207", updatable = false, insertable = true)
	private Integer utenteAmministratore;
	@Column(name = "pwdcr9207")
	private Integer pwdCriptata;
	@Column(name = "usrag9207")
	private String userAgent;
	@Transient
	private String pwdDecriptata;
}
