package it.dpe.igeriv.vo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella piano di profilazione edicola
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_7100", schema = "")
public class PianoProfiliEdicolaVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	

	@Id
	@Column(name = "CRIVW7100")
	private Integer codDpeWebEdicola;
	@Id
	@Column(name = "CODDL7100")
	private Integer codFiegDl;
	
	@Column(name = "CODGR7100")
	private Integer codiceGruppo;
	@Id
	@Column(name = "DTATPROF7100")
	private Timestamp dtAttivazioneProfiloEdicola;
	
	@Column(name = "DTSOPROF7100")
	private Timestamp dtSospensioneProfiloEdicola;
	
	@Column(name = "TEST7100")
	private Integer edicolaTest;
		
	@Column(name = "PLUS7100")
	private Integer edicolaPlus;
	
	@Column(name = "FGIMG7100")
	private Integer fgimmagine;
	
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CRIVW7100", updatable = false, insertable = false, referencedColumnName = "crivw9106")
	private AnagraficaEdicolaVo anagraficaEdicolaVo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CODDL7100", updatable = true, insertable = true, referencedColumnName = "coddl9107")	
	private AnagraficaAgenziaVo anagraficaAgenziaVo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CODGR7100", referencedColumnName = "codgr9209", insertable = false, updatable = false)
	private GruppoModuliVo codiceGruppoVo;
	
	
	
	
	
	
}
