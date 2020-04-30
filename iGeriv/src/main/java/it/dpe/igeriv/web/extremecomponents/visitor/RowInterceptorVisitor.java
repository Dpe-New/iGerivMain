package it.dpe.igeriv.web.extremecomponents.visitor;

import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;

import com.google.common.base.Strings;

import it.dpe.igeriv.calc.Calculator;
import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.dto.BollaResaBaseDto;
import it.dpe.igeriv.dto.EmailRivenditaDto;
import it.dpe.igeriv.dto.EstrattoContoDinamicoDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.dto.MessaggioDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.dto.VisitableDto;
import it.dpe.igeriv.dto.VisitorDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.igeriv.vo.MessaggioClienteVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaDettaglioVo;

/**
 * Implementazione di un visitor pattern per astrarre la logica 
 * da applicare sulla org.extremecomponents.table.bean.Row per ogni tipo di Dto.
 * 
 * N.B.: Meglio utilizzare un org.extremecomponents.table.interceptor.RowInterceptor specifico 
 * per ogni org.extreamcomponents.table.bean.Table (da dichiarare nel file dpecomponents.tld)
 * 
 * @author mromano
 *
 */
public class RowInterceptorVisitor implements VisitorDto {
	private TableModel model;
	private Row row;
	private boolean isDlInforiv;
	
	public RowInterceptorVisitor(TableModel model, Row row) {
		this.model = model;
		this.row = row;
		this.isDlInforiv = model.getContext().getSessionAttribute("isDlInforiv") != null ? Boolean.parseBoolean(model.getContext().getSessionAttribute("isDlInforiv").toString()) : false;
	}
	
	@Override
	public void visit(BollaDettaglioDto dto) {
		String style = (row.getStyle() != null && row.getStyle().indexOf(";background-color") != -1) ? row.getStyle().substring(0, row.getStyle().indexOf(";background-color")) : row.getStyle();
		row.setStyle(style);
		String iv = dto.getIndicatoreValorizzare() != null ? dto.getIndicatoreValorizzare().toString() : "";
		Integer tipoControlloPubblicazioniRespinte = model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_TIPO_CONTROLLO_PUBBLICAZIONI_RESPINTE) != null ? new Integer(model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_TIPO_CONTROLLO_PUBBLICAZIONI_RESPINTE).toString()) : null;
		String resp = (!Strings.isNullOrEmpty(dto.getMotivoResaRespinta()) && tipoControlloPubblicazioniRespinte != null && tipoControlloPubblicazioniRespinte > 0) ? "true" : "false";
		row.addAttribute("iv", iv);
		row.addAttribute("pv", dto.getIndicatorePrezzoVariato() != null ? dto.getIndicatorePrezzoVariato() : "");
		row.addAttribute("idtn", dto.getIdtn());
		if (!Strings.isNullOrEmpty(iv)) {
			row.addAttribute("dpa", dto.getDataFatturazionePrevista() != null ? DateUtilities.getTimestampAsString(dto.getDataFatturazionePrevista(), DateUtilities.FORMATO_DATA_SLASH) : "");
		}
		if (!Strings.isNullOrEmpty(resp)) {
			row.addAttribute("resp", resp);
		}
		row.addAttribute("idMessaggioIdtn", dto.getIdMessaggioIdtn());
		row.addAttribute("agfc", new Boolean(dto.getAggiuntaFuoriCompetenza() != null && dto.getAggiuntaFuoriCompetenza().equals(true)).toString());
		row.addAttribute("barf", !Strings.isNullOrEmpty(dto.getBarcode()) && dto.getBarcode().startsWith(IGerivConstants.PREFIX_BARCODE_FITTIZIO) ? new Boolean(true).toString() : new Boolean(false).toString());
		row.addAttribute("pnu", dto.getPubblicazioneNonUscita());
	}

	@Override
	public void visit(BollaResaBaseDto dto) {
		row.addAttribute("pk", dto.getPk().toString());
	}
	
	@Override
	public void visit(PubblicazioneDto dto) {
		row.addAttribute("iv", (dto.getContoDeposito() != null && dto.getContoDeposito() > 0) ? "2" : "");
		row.addAttribute("idtn", dto.getIdtn());
		if (isDlInforiv) {
			dto.setGiacenzaCalculator(SpringContextManager.getSpringContext().getBean("GiacenzaEdicolaInforivCalculator", Calculator.class));
		}
	}

	@Override
	public void visit(ProdottiNonEditorialiBollaDettaglioVo dto) {
		row.addAttribute("prodottoDl", dto.getProdotto() != null ? dto.getProdotto().getProdottoDl() : "");
	}

	@Override
	public void visit(FondoBollaDettaglioDto dto) {
		row.addAttribute("iv", "");
		row.addAttribute("pv", "");
		row.addAttribute("fb", "true");
		boolean isContoDeposito = dto.getTipoRecordFondoBolla() != null && dto.getTipoRecordFondoBolla().equals(IGerivConstants.TIPO_RECORD_FONDO_BOLLA_CONTO_DEPOSITO) ? true : false;
		row.addAttribute("fbcd", isContoDeposito);
		row.addAttribute("barf", !Strings.isNullOrEmpty(dto.getBarcode()) && dto.getBarcode().startsWith("00") ? new Boolean(true).toString() : new Boolean(false).toString());
		/*boolean qtaFatturataInContoDepositoDiversaDaVenduto = !fbd.getQuantitaConsegnata().equals(fbd.getVendutoBollePrecedenti());
		if (isContoDeposito && qtaFatturataInContoDepositoDiversaDaVenduto) {
			row.addAttribute("qfdv", true);
		} else {
			row.addAttribute("qfdv", "");
		}*/
	}

	@Override
	public void visit(MessaggioVo dto) {
		row.addAttribute("an", dto.getAttachmentName1());
		row.addAttribute("an1", dto.getAttachmentName2());
		row.addAttribute("an2", dto.getAttachmentName3());
	}

	@Override
	public void visit(MessaggioDto dto) {
		row.addAttribute("an", dto.getAttachmentName1());
		row.addAttribute("an1", dto.getAttachmentName2());
		row.addAttribute("an2", dto.getAttachmentName3());
	}
	
	@Override
	public void visit(MessaggioClienteVo dto) {
		row.addAttribute("an", dto.getAllegato1());
		row.addAttribute("an1", dto.getAllegato2());
		row.addAttribute("an2", dto.getAllegato3());
	}
	
	@Override
	public void visit(EmailRivenditaDto dto) {
		row.addAttribute("an", ((EmailRivenditaDto) dto).getAllegato());
	}

	@Override
	public void visit(RichiestaRifornimentoDto dto) {
		row.addAttribute("rigaEvasione", dto.isRigaEvasione());
	}

	@Override
	public void visit(EstrattoContoEdicolaDettaglioVo dto) {
		if (dto.getNote() != null) {
			if  (dto.getTipoRecord() != null && (dto.getTipoRecord().equals(20) || dto.getTipoRecord().equals(21) || dto.getTipoRecord().equals(22) || dto.getTipoRecord().equals(32))) {
				row.setStyleClass(row.getStyleClass() + " calcTitle");
				dto.setIsBold(true);
			} else if (dto.getTipoRecord() != null && (dto.getTipoRecord().equals(30) || dto.getTipoRecord().equals(31))) {
				row.setStyleClass(row.getStyleClass() + " calcTitleRed");
				dto.setIsBold(true);
			} else {
				String styleClass = (row.getStyleClass() != null && row.getStyleClass().indexOf("calcTitleRed") != -1) ? row.getStyleClass().substring(0, row.getStyleClass().indexOf("calcTitleRed")) : row.getStyleClass();
				styleClass = (row.getStyleClass() != null && row.getStyleClass().indexOf("calcTitle") != -1) ? row.getStyleClass().substring(0, row.getStyleClass().indexOf("calcTitle")) : row.getStyleClass();
				row.setStyleClass(styleClass);
			}
		} else {
			String styleClass = (row.getStyleClass() != null && row.getStyleClass().indexOf("calcTitleRed") != -1) ? row.getStyleClass().substring(0, row.getStyleClass().indexOf("calcTitleRed")) : row.getStyleClass();
			styleClass = (row.getStyleClass() != null && row.getStyleClass().indexOf("calcTitle") != -1) ? row.getStyleClass().substring(0, row.getStyleClass().indexOf("calcTitle")) : row.getStyleClass();
			row.setStyleClass(styleClass);
		}
	}
	
	@Override
	public void visit(EstrattoContoDinamicoDto dto) {
		if (dto.isCalcRow()) {
			row.setStyleClass(row.getStyleClass() + " calcTitleRed");
		}
	}
	
	@Override
	public void visit(Object dto) {
		if (dto instanceof VisitableDto) {
			String style = (row.getStyle() != null && row.getStyle().indexOf(";background-color") != -1) ? row.getStyle().substring(0, row.getStyle().indexOf(";background-color")) : row.getStyle();
			row.setStyle(style);
		}
	}

}
