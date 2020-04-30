package it.dpe.igeriv.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseDto implements Serializable, VisitableDto {
	private static final long serialVersionUID = 1L;
	@XmlTransient
	private Date created;
	@XmlTransient
	private Date updated;
    
	public BaseDto() {
    	updated = created = new java.util.Date();
    }
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
	
}
