package models;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "edicole")
public class ListEdicolaMappa {
	private List<EdicolaMappaDto> edicole;
	
	
	public List<EdicolaMappaDto> getEdicole() {
		return edicole;
	}
	
	@XmlElement(name="edicola")
	public void setEdicole(List<EdicolaMappaDto> edicole) {
		this.edicole = edicole;
	}

}
