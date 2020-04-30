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

import lombok.Getter;
import lombok.Setter;

/**
 * @author romanom
 * 
 */
@Getter
@Setter
@Entity
@NamedQueries( {
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_USER_ADMIN_BY_CODICE_JOINS, query = "from UserAdminVo vo join fetch vo.gruppoModuliVo join fetch vo.gruppoModuliVo.moduli where vo.codUtente = :codUtente"),
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_USER_ADMIN_BY_EMAIL_JOINS, query = "from UserAdminVo vo join fetch vo.gruppoModuliVo join fetch vo.gruppoModuliVo.moduli where vo.email = :email")
})
@Table(name = "tbl_9306", schema = "")
public class UserAdminVo extends BaseVo {
	private static final long serialVersionUID = 1L; 
	@Id
	@Column(name = "codut9306")
	private String codUtente;
	@Column(name = "nomut9306")
	private String nomeUtente;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "codgr9306", updatable = true, insertable = true, referencedColumnName = "codgr9209")
	})
	private GruppoModuliVo gruppoModuliVo;
	@Column(name = "paswd9306", updatable = true, insertable = true)
	private String pwd; 
	@Column(name = "email9306")
	private String email;
	@Column(name = "abili9306")
	private Integer abilitato;
	@Column(name = "capwd9306", updatable = true, insertable = true)
	private Integer changePassword;
	@Column(name = "pwdcr9306")
	private Integer pwdCriptata;
	@Column(name = "logfal9306")
	private Integer failedLoginAttempts;
	@Column(name = "iplogl9306")
	private String ipLoginAddress;
}
