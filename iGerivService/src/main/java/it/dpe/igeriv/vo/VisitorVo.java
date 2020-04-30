package it.dpe.igeriv.vo;


/**
 * @author mromano
 *
 */
public interface VisitorVo {
	
	public void visit(VenditaVo dto);

	public void visit(MessaggioClienteVo dto);
	
	public void visit(Object dto);
	
}
