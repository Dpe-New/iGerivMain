package it.dpe.igeriv.dto;

import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.igeriv.vo.MessaggioClienteVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaDettaglioVo;

public interface VisitorDto {
	
	public void visit(BollaDettaglioDto dto);
	
	public void visit(BollaResaBaseDto dto);
	
	public void visit(PubblicazioneDto dto);
	
	public void visit(ProdottiNonEditorialiBollaDettaglioVo dto);
	
	public void visit(FondoBollaDettaglioDto dto);
	
	public void visit(MessaggioVo dto);
	
	public void visit(MessaggioDto dto);
	
	public void visit(MessaggioClienteVo dto);
	
	public void visit(EmailRivenditaDto dto);
	
	public void visit(RichiestaRifornimentoDto dto);
	
	public void visit(EstrattoContoEdicolaDettaglioVo dto);
	
	public void visit(EstrattoContoDinamicoDto dto);
	
	public void visit(Object dto);
	
}
