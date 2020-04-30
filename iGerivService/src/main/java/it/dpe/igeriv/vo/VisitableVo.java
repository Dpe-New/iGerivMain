package it.dpe.igeriv.vo;

/**
 * @author mromano
 *
 */
public interface VisitableVo {
	
	public void accept(VisitorVo visitor);
	
	public String getMethodSignature();

	public void setMethodSignature(String methodSignature);
	
}
