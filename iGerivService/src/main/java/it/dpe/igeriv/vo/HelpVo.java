package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@NamedQueries({ @NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_ALL_HELP, query = "from HelpVo vo join fetch vo.videoHelp") })
@Table(name = "tbl_9213", schema = "")
public class HelpVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codhlp9213")
	private Integer codice;
	@Column(name = "actnm9213")
	private String action;
	@Column(name = "testo9213")
	private String help;
	@ManyToMany(fetch=FetchType.LAZY, targetEntity = VideoHelpVo.class) 
	@JoinTable(name = "tbl_9214", 
			joinColumns = { 
				@JoinColumn(name="codhlp9214")               
			},    
			inverseJoinColumns = {      
				@JoinColumn(name="codvid9214")    
			}  
	)
	private List<VideoHelpVo> videoHelp;
}
