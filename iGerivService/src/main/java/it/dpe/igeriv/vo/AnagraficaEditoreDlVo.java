package it.dpe.igeriv.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import it.dpe.igeriv.vo.pk.AnagraficaPubblicazioniPk;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_9114", schema = "")
public class AnagraficaEditoreDlVo extends BaseVo{

	private static final long serialVersionUID = 1L;
	
	@Id
	private AnagraficaEditoreDlPk pk;
	
	@Column(name = "PASSW9114")
	private String password;
	@Column(name = "NOMEA9114")
	private String nomeA;
	@Column(name = "NOMEB9114")
	private String nomeB;
	@Column(name = "VIAA9114")
	private String viaA;
	@Column(name = "VIAB9114")
	private String viaB;
	@Column(name = "LOCAA9114")
	private String localitaA;
	@Column(name = "LOCAB9114")
	private String localitaB;
	@Column(name = "PAESE9114")
	private Integer paese;
	@Column(name = "PROV9114")
	private String provincia;
	@Column(name = "CAP9114")
	private String cap;
	@Column(name = "TELE9114")
	private String telefono;
	@Column(name = "FAX9114")
	private String fax;
	@Column(name = "EMAIL9114")
	private String email;
	@Column(name = "URL9114")
	private String url;
	
	
	
	

}
