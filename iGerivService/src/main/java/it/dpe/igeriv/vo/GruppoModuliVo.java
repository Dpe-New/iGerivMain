package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Tabella gruppi moduli
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 */
@Getter
@Setter
@Entity
@NamedQueries( {
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_ID, query = "from GruppoModuliVo vo left join fetch vo.moduli where vo.id = :id"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_ROLE, query = "from GruppoModuliVo vo where vo.roleName = :roleName and vo.configurabileDl = false"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_GRUPPO_CONFIGURABILI_DAL_DL, query = "from GruppoModuliVo vo where vo.configurabileDl = true"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_ROLE_JOINS, query = "from GruppoModuliVo vo join fetch vo.moduli where vo.roleName = :roleName and vo.configurabileDl = false"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_ROLE_JOINS_EXCLUDE_CONTAINER_MENUS, query = "from GruppoModuliVo vo join fetch vo.moduli where vo.roleName = :roleName and vo.configurabileDl = false"),
		@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_USER_TYPE, query = "from GruppoModuliVo vo where vo.tipoProfilo = :tipoProfilo and vo.configurabileDl = false")
})
@Table(name = "tbl_9209", schema = "")
public class GruppoModuliVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codgr9209")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName="SEQUENZA_9209", allocationSize = 1)
	private Integer id;
	@Column(name = "titgr9209")
	private String titolo;
	@Column(name = "desgr9209")
	private String descrizione;
	@Column(name = "ruolo9209")
	private String roleName;
	@ManyToMany(fetch=FetchType.LAZY, targetEntity = MenuModuloVo.class) 
	@JoinTable(name = "tbl_9210", 
			joinColumns = { 
				@JoinColumn(name="codgr9210")               
			},    
			inverseJoinColumns = {      
				@JoinColumn(name="codmo9210")    
			}  
	)
	private List<MenuModuloVo> moduli;
	@Column(name = "tippr9209")
	private Integer tipoProfilo;
	@Column(name = "codut9209")
	private String codUtente;
	@Column(name = "daulmo9209")
	private Timestamp dtUltimaModifica;
	@Column(name = "grmddl9209")
	private Boolean configurabileDl;
	@Column(name = "fgimg9209")
	private Boolean viewImageByProfile;
	
	//add 14/02/2017
	@Column(name = "fgxls9209")
	private Boolean isEnabledExportXLS; // esportazione in xls
	@Column(name = "fgxlspk9209")
	private Boolean isEnabledPKInExportXLS; // esportazione in xls i dati inerenti la pk
	@Column(name = "fgpdf9209")
	private Boolean isEnabledExportPDF; // esportazione in pdf
	@Column(name = "fgpdfpk9209")
	private Boolean isEnabledPKInExportPDF; // esportazione in pdf i dati inerenti la pk
	@Column(name = "fgpdftif9209")
	private Boolean isEnabledExportPDFToTIFF; // esportazione del pdf in tif
	@Column(name = "fgbocordonly9209")
	private Boolean isBollaConsegnaReadOnly; // Bolla di consegna read only 0=No 1=Si
	@Column(name = "fgborerdonly9209")
	private Boolean isBollaResaReadOnly; // Bolla di resa read only 0=No 1=Si
	
	
	
}
