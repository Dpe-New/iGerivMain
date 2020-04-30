package it.dpe.igeriv.vo;

import it.dpe.igeriv.vo.pk.EdicolaDlPk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9790", schema = "")
public class EdicolaDlVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	private EdicolaDlPk pk;
	@Column(name = "nomeriva9790")
	private String ragioneSocialeDlPrimaRiga;
	@Column(name = "nomerivb9790")
	private String ragioneSocialeDlSecondaRiga;
	@Column(name = "viariva9790")
	private String indirizzoDlPrimaRiga;
	@Column(name = "viarivb9790")
	private String indirizzoDlSecondaRiga;
	@Column(name = "locariva9790")
	private String localitaDlPrimaRiga;
	@Column(name = "locarivb9790")
	private String localitaDlSecondaRiga;
	@Column(name = "paeseriv9790")
	private Integer codNazione;
	@Column(name = "provriv9790")
	private String siglaProvincia;
	@Column(name = "capriv9790")
	private String cap;
	@Column(name = "cofi9790")
	private String codiceFiscale;
	@Column(name = "piva9790")
	private String piva;
	@Column(name = "teleriv9790")
	private String telefono;
	@Column(name = "faxriv9790")
	private String fax;
	@Column(name = "emailriv9790")
	private String email;
	@Column(name = "attiva9790")
	private Boolean attiva;
}
