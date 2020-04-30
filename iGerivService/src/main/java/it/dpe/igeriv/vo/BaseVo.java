package it.dpe.igeriv.vo;

import it.dpe.igeriv.dto.VisitableDto;
import it.dpe.igeriv.dto.VisitorDto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

/**
 * @author romanom
 *
 */
@MappedSuperclass
public abstract class BaseVo implements Serializable, VisitableDto, VisitableVo {
	private static final long serialVersionUID = 1L;
	@Transient
    private Date created;
	@Transient
    private Date updated;
	@Transient
    private String methodSignature;
    
	@PrePersist
    protected void onCreate() {
    	updated = created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
    	updated = new Date();
    }
    
	@Transient
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Transient
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	@Override
	public void accept(VisitorDto visitor) {
		visitor.visit(this);
	}
	
	@Override
	public void accept(VisitorVo visitor) {
		visitor.visit(this);
	}

	@Override
	@Transient
	public String getMethodSignature() {
		return methodSignature;
	}
	
	@Override
	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}
	
}
