package it.dpe.igeriv.vo;

import it.dpe.igeriv.dto.VisitorDto;
import it.dpe.igeriv.util.StringUtility;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mromano
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "tbl_9643", schema = "")
public class MessaggioClienteVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "codme9643")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
	@SequenceGenerator(name = "SEQ_GEN", sequenceName = "SEQUENZA_9643", allocationSize = 1)
	private Long codice;
	@Column(name = "crivw9643")
	private Integer codEdicola;
	@Column(name = "ccli9643")
	private Long codCliente;
	@Column(name = "datin9643")
	private Timestamp dataMessaggio;
	@Column(name = "subje9643")
	private String oggetto;
	@Column(name = "bodym9643")
	private String messaggio;
	@Column(name = "aleg19643")
	private String allegato1;
	@Column(name = "aleg29643")
	private String allegato2;
	@Column(name = "aleg39643")
	private String allegato3;
	@Column(name = "letto9643")
	private Boolean letto;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ccli9643", updatable = false, insertable = false, referencedColumnName = "ccli9305")
	private ClienteEdicolaVo cliente;
	
	public String getMessaggioShort() {
		String msg = null;
		if (messaggio != null) {
			msg = StringUtility.removeHTML(messaggio.trim());
			if (msg.length() > 50) {
				msg = msg.substring(0, 50) + "...";
			}
		}
		return msg;
	}
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
	
	@Override
	public void accept(VisitorVo visitor) {
		visitor.visit(this);
	}
}
