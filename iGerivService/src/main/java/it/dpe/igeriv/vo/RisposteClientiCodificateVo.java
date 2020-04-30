package it.dpe.igeriv.vo;

import it.dpe.igeriv.util.IGerivQueryContants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@NamedQueries( { 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_RISPOSTE_CLIENTI_CODIFICATE, query = "from RisposteClientiCodificateVo")
})
@Table(name = "tbl_9311", schema = "")
public class RisposteClientiCodificateVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codr9311")
	private Integer codRisposta;
	@Column(name = "dese9311")
	private String descrizioneEstesa;
	@Column(name = "dess9311")
	private String descrizioneSintetica;
	@Column(name = "tipo9311")
	private Integer tipoRisposta;
}
