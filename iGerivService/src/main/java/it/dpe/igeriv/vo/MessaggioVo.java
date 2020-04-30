package it.dpe.igeriv.vo;

import it.dpe.igeriv.dto.VisitorDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.pk.MessaggioPk;

import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author romanom
 *
 */
@Getter
@Setter
@Entity
@NamedQueries({ 
	@NamedQuery(name = IGerivQueryContants.QUERY_NAME_GET_MESSAGGIO_RIVENDITA, query = "from MessaggioVo vo where vo.pk.codFiegDl = :codFiegDl and pk.dtMessaggio = :dtMessaggio and pk.tipoDestinatario = :tipoDestinatario and pk.destinatarioA = :destinatarioA and pk.destinatarioB = :destinatarioB")
})
@Table(name = "tbl_9601", schema = "")
public class MessaggioVo extends BaseVo {
	private static final long serialVersionUID = 1L;
	@Transient
	private final Logger log = Logger.getLogger(getClass());
	@Id
	private MessaggioPk pk;
	@Column(name = "testo9601")
	@Getter(AccessLevel.NONE)
	private String messaggio;
	@Column(name = "tipme9601")
	private Integer tipoMessaggio; 
	@Column(name = "stato9601")
	private Integer statoMessaggio;
	@Column(name = "aleg19601")
	private String attachmentName1;
	@Column(name = "aleg29601")
	private String attachmentName2;
	@Column(name = "aleg39601")
	private String attachmentName3;
	@Column(name = "edico9601")
	@Getter(AccessLevel.NONE)
	private String edicolaLabel;
	@Column(name = "catmes9601")
	private Integer categoria;
	@Column(name = "datas9601")
	private Timestamp dtScadenzaMessaggio;
	@Column(name = "testb9601")
	@Lob
	private Blob messaggioEsteso;
	@Transient
	private String messaggioEscape;
	
	public MessaggioVo() {}
	
	public MessaggioVo(MessaggioVo vo) {
		this.pk = new MessaggioPk();
		this.pk.setCodFiegDl(vo.getPk().getCodFiegDl());
		this.pk.setDestinatarioA(vo.getPk().getDestinatarioA());
		this.pk.setDestinatarioB(vo.getPk().getDestinatarioB());
		this.pk.setDtMessaggio(vo.getPk().getDtMessaggio());
		this.pk.setTipoDestinatario(vo.getPk().getTipoDestinatario());
		this.messaggio = vo.getMessaggio();
		this.tipoMessaggio = vo.getTipoMessaggio();
		this.statoMessaggio = vo.getStatoMessaggio();
		this.attachmentName1 = vo.getAttachmentName1();
		this.attachmentName2 = vo.getAttachmentName2();
		this.attachmentName3 = vo.getAttachmentName3();
		this.edicolaLabel = vo.getEdicolaLabel();
		this.messaggioEscape = vo.getMessaggioEscape();
	}
	

	public String getEdicolaLabel() {
		if(getPk().getTipoDestinatario() != null) {
			if (getPk().getTipoDestinatario().equals(IGerivConstants.COD_TUTTE_LE_EDICOLE)) {
				return IGerivMessageBundle.get("igeriv.tutti");
			} else if (getPk().getTipoDestinatario().equals(IGerivConstants.COD_EDICOLA_SINGOLA)) {
				return edicolaLabel;
			} else if (getPk().getTipoDestinatario().equals(IGerivConstants.COD_GIRO_TIPO)) {
				return IGerivMessageBundle.get("igeriv.giro.tipo") + " " + getPk().getDestinatarioA() + " | " + IGerivMessageBundle.get("igeriv.giro") + " " + getPk().getDestinatarioB();
			} else if (getPk().getTipoDestinatario().equals(IGerivConstants.COD_ZONA_TIPO)) {
				return IGerivMessageBundle.get("igeriv.zona.tipo") + " " + getPk().getDestinatarioA() + " | " + IGerivMessageBundle.get("igeriv.zona") + " " + getPk().getDestinatarioB();
			}
		}
		return "";
	}
	
	public Timestamp getDtMessaggio() {
		return getPk().getDtMessaggio();
	}
	
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
	
	public String getStatoMessaggioDesc() {
		return (getStatoMessaggio() != null && getStatoMessaggio().equals(IGerivConstants.STATO_MESSAGGIO_INVIATO)) ? IGerivMessageBundle.get("igeriv.inviato") : IGerivMessageBundle.get("igeriv.inserito");
	}
	
	public String getTipoMessaggioDesc() {
		String desc = null;
		 if (getTipoMessaggio() != null) {
			 if (getTipoMessaggio().equals(IGerivConstants.TIPO_MESSAGGIO_NORMALE)) {
				 desc = IGerivMessageBundle.get("igeriv.normale");
			 } else if (getTipoMessaggio().equals(IGerivConstants.TIPO_MESSAGGIO_CON_AVVISO)) {
				 desc = IGerivMessageBundle.get("igeriv.allerta");
			 } else if (getTipoMessaggio().equals(IGerivConstants.TIPO_MESSAGGIO_IMMEDIATO)) {
				 desc = IGerivMessageBundle.get("igeriv.emergenza");
			 }
		}
		return desc;
	}
	
	public String getMessaggioLetto() {
		return "";
	}
	
	public String getMessaggio() {
		try {
			if (getMessaggioEsteso() != null) {
				byte[] bdata = getMessaggioEsteso().getBytes(1, (int) getMessaggioEsteso().length());
				return new String(bdata);
			}
		} catch (SQLException e) {
			log.error("Error converting Blob in MessaggioVo.getMessaggio()", e);
		}
		return messaggio;
	}
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
}
