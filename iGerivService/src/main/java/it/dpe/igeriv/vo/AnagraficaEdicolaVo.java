package it.dpe.igeriv.vo;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder.In;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Formula;

import com.google.common.base.Strings;

/**
 * Tabella Anagrafica Edicole
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9106", schema = "")
public class AnagraficaEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "crivw9106")
	private Integer codEdicola;
	@Column(name = "nomeriva9106")
	private String ragioneSocialeEdicolaPrimaRiga;
	@Column(name = "nomerivb9106")
	@Getter(AccessLevel.NONE)
	private String ragioneSocialeEdicolaSecondaRiga;
	@Column(name = "viariva9106")
	private String indirizzoEdicolaPrimaRiga;
	@Column(name = "viarivb9106")
	@Getter(AccessLevel.NONE)
	private String indirizzoEdicolaSecondaRiga;
	@Column(name = "locariva9106")
	@Getter(AccessLevel.NONE)
	private String localitaEdicolaPrimaRiga;
	@Column(name = "locarivb9106")
	private String localitaEdicolaSecondaRiga;
	@Column(name = "paeseriv9106")
	private Integer codNazione;
	@Column(name = "provriv9106")
	private String siglaProvincia;
	@Column(name = "capriv9106")
	private String cap;
	@Column(name = "caprivi9106")
	private Integer capInt;
	@Column(name = "cofi9106")
	private String codiceFiscale;
	@Column(name = "piva9106")
	private String piva;
	@Column(name = "teleriv9106")
	private String telefono;
	@Column(name = "faxriv9106")
	private String fax;
	@Column(name = "emailriv9106")
	private String email;
	@Column(name = "urlriv9106")
	private String url;
	@Column(name = "datmem9106")
	private Timestamp dtUltimaModifica;
	@Column(name = "datval9106")
	private Timestamp dtValidazione;
	@Formula(value = "(select t1.paswd9207 from tbl_9207 t1 inner join tbl_9206 t2 on t2.crivw9206 = t1.crivw9207 where t2.crivw9206 = crivw9106 and t1.utadm9207 = 1 and t1.pwdcr9207 = 0 and (t1.email9207 is null or t1.email9207 = ''))")
	@Basic(fetch = FetchType.LAZY)
	private String password;
	@Column(name = "coddl9106")
	private Integer coddl;
	@Column(name = "tiplo9305")
	private Integer tipoLocalita;
	@Column(name = "numci9106")
	@Getter(AccessLevel.NONE)
	private String numeroCivico;
	@Column(name = "prov9106")
	private Integer provincia;
	@Column(name = "cell9106")
	private String cellulare;
	@Column(name = "tipoed9106")
	private Integer tipoEdicola;
	@Formula(value = "(select t.coso9206 from tbl_9206 t where t.crivw9206 = crivw9106)")
	@Basic(fetch = FetchType.LAZY)
	private Long codiceContabileCliente;
	@Formula(value = "(select t1.crivdl9206 from tbl_9206 t1 where t1.crivw9206 = crivw9106)")
	@Basic(fetch = FetchType.LAZY)
	private Integer codEdicolaDl;
	@Column(name = "latit9106")
	private Double latitudine;
	@Column(name = "longi9106")
	private Double longitudine;
	@Column(name = "flggeo9106")
	private Integer flggeo9106;
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "crivw9106", updatable = false, insertable = false, referencedColumnName = "crivw9112")	
	private AnagraficaEdicolaAggiuntiviVo anagraficaEdicolaAggiuntiviVo;
	
	public AnagraficaEdicolaVo() {}
	
	public AnagraficaEdicolaVo(Integer codEdicola) {
		this.codEdicola = codEdicola;
	}
	
	public String getRagioneSocialeEdicolaSecondaRiga() {
		return ragioneSocialeEdicolaSecondaRiga == null ? "" : ragioneSocialeEdicolaSecondaRiga;
	}
	
	public String getIndirizzoEdicolaSecondaRiga() {
		return indirizzoEdicolaSecondaRiga == null ? "" : indirizzoEdicolaSecondaRiga;
	}
	
	public String getNumeroCivico() {
		return numeroCivico == null ? "" : numeroCivico;
	}
	
	public String getLocalitaEdicolaPrimaRiga() {
		return localitaEdicolaPrimaRiga == null ? "" : localitaEdicolaPrimaRiga;
	}
	
	public String getIndirizzoViaNumeroCitta() {
		return getIndirizzoEdicolaPrimaRiga() + " " + getIndirizzoEdicolaSecondaRiga() + (!Strings.isNullOrEmpty(getNumeroCivico()) ? ", " : "") + getNumeroCivico() + (!Strings.isNullOrEmpty(getLocalitaEdicolaPrimaRiga()) ? " - " : "") + getLocalitaEdicolaPrimaRiga();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AnagraficaEdicolaVo) {
			return ((AnagraficaEdicolaVo) obj).getCodEdicola().equals(this.getCodEdicola());
		}
		return super.equals(obj);
	}

	
	
}
