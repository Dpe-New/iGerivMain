package models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dl")
public class ScaricoDatiDto implements Serializable {
	private static final long serialVersionUID = -4302488992570804097L;
	private Integer codFiegDl;
	private String userFtp;
	private String passFtp;
	private String fileName;
	private String msg;
	private String status;

	public Integer getCodFiegDl() {
		return codFiegDl;
	}
	@XmlElement
	public void setCodFiegDl(Integer codFiegDl) {
		this.codFiegDl = codFiegDl;
	}

	public String getUserFtp() {
		return userFtp;
	}
	@XmlElement
	public void setUserFtp(String userFtp) {
		this.userFtp = userFtp;
	}

	public String getPassFtp() {
		return passFtp;
	}
	@XmlElement
	public void setPassFtp(String passFtp) {
		this.passFtp = passFtp;
	}

	public String getFileName() {
		return fileName;
	}
	@XmlElement
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMsg() {
		return msg;
	}
	@XmlElement
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}
	@XmlElement
	public void setStatus(String status) {
		this.status = status;
	}
}
