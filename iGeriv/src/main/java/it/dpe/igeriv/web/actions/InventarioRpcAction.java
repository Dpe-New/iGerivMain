package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;

import org.apache.struts2.json.annotations.SMDMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.inventario.InventarioService;
import it.dpe.igeriv.dto.InventarioDto;
import it.dpe.igeriv.dto.InventarioJsonDto;
import it.dpe.igeriv.dto.InventarioResultDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.QuantitaCopieMaggioreDiInseritaException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.NumberUtils;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.InventarioDettaglioVo;
import it.dpe.igeriv.vo.InventarioVo;
import it.dpe.igeriv.vo.pk.InventarioDettaglioPk;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione dell'inventario.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("InventarioRpcAction")
public class InventarioRpcAction<T extends BaseVo> extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private final InventarioService inventarioService;

	public InventarioRpcAction() {
		this.inventarioService = null;
	}
	
	@Autowired
	public InventarioRpcAction(InventarioService inventarioService) {
		this.inventarioService = inventarioService;
	}
	
	@Override
	public void validate() {
	}
	
	
	@SMDMethod
	public BigDecimal getTotaleInventario(Long idInventario) {
		if (idInventario != null && idInventario > 0l) {
			return inventarioService.getTotaleInventario(idInventario);
		}
		return new BigDecimal(0);
	}
	
	@SMDMethod
	public InventarioResultDto addDettaglioToInventario(InventarioDto pubblicazione) throws ParseException {
		InventarioResultDto dto = new InventarioResultDto();
		try {
			if (pubblicazione != null && pubblicazione.getQuantita() != null && pubblicazione.getQuantita() != 0) {
				InventarioJsonDto result = new InventarioJsonDto();
				dto.setType(InventarioResultDto.ResultType.OK);
				BigDecimal importoPub = pubblicazione.getImporto();
				InventarioVo inventarioVo = inventarioService.getInventarioVo(getAuthUser().getCodEdicolaMaster(), pubblicazione.getIdInventario());
				List<InventarioDettaglioVo> dettagli = inventarioVo.getDettagli();
				InventarioDettaglioVo det = selectUnique(dettagli,
						having(on(InventarioDettaglioVo.class).getPk().getIdInventario(), equalTo(pubblicazione.getIdInventario())).and(having(on(InventarioDettaglioVo.class).getPk().getIdtn(), equalTo(pubblicazione.getIdtn()))));
				Integer qta = (det != null ? det.getQuantita() : 0);
				BigDecimal importo = new BigDecimal(0);
				for (InventarioDettaglioVo idvo : dettagli) {
					importo = importo.add(idvo.getImporto());
				}
				if (pubblicazione.getModalita().equals(IGerivConstants.MODALITA_DEFAULT)) {
					qta += pubblicazione.getQuantita();
					importo = importo.add(importoPub);
					if (pubblicazione.getQuantita() > 0) {
						insertDettaglio(pubblicazione, dettagli, qta);
					} else if (pubblicazione.getQuantita() < 0) {
						deleteDettagli(pubblicazione, det, qta);
					}
				}
				result.setTitolo(pubblicazione.getTitolo());
				result.setSottoTitolo(pubblicazione.getSottoTitolo());
				result.setDataUscitaFormat(pubblicazione.getDataUscitaFormat());
				result.setNumeroCopertina(pubblicazione.getNumeroCopertina());
				result.setPrezzoEdicolaFormat(NumberUtils.formatNumber(pubblicazione.getPrezzoEdicola()));
				result.setPrezzoCopertinaFormat(NumberUtils.formatNumber(pubblicazione.getPrezzoCopertina()));
				result.setQuantita(qta.toString());
				result.setImportoTotale(importo.toString());
				result.setIsContoDeposito(pubblicazione.getQuantitaCopieContoDeposito() > 0 ? getText("igeriv.si") : getText("igeriv.no"));
				result.setIsScaduta(pubblicazione.getIsScaduta() ? getText("igeriv.si") : getText("igeriv.no"));
				result.setGiacenza(pubblicazione.getGiacenzaSP().toString());
				dto.setResult(result);
			}
		} catch (IGerivBusinessException e) {
			dto.setType(InventarioResultDto.ResultType.EXCEPTION);
			dto.setExceptionMessage(e.getMessage());
		} catch (Throwable e) {
			dto.setType(InventarioResultDto.ResultType.EXCEPTION);
			dto.setExceptionMessage(getText("msg.errore.invio.richiesta.html"));
		}
		return dto;
	}

	/**
	 * Aggiunge un dettaglio all'inventario
	 * 
	 * @param InventarioDto
	 *            pubblicazione
	 * @param List
	 *            <InventarioDettaglioVo> dettagli
	 * @param Integer
	 *            qta
	 * @throws ParseException
	 */
	private void insertDettaglio(InventarioDto pubblicazione, List<InventarioDettaglioVo> dettagli, Integer qta) throws ParseException {
		InventarioDettaglioVo idvo = new InventarioDettaglioVo();
		InventarioDettaglioPk pk = new InventarioDettaglioPk();
		pk.setIdInventario(pubblicazione.getIdInventario());
		pk.setIdtn(pubblicazione.getIdtn());
		idvo.setPk(pk);
		idvo.setTitolo(pubblicazione.getTitolo());
		idvo.setSottotitolo(pubblicazione.getSottoTitolo());
		idvo.setDataUscita(DateUtilities.parseDate(pubblicazione.getDataUscitaFormat(), DateUtilities.FORMATO_DATA_SLASH));
		idvo.setNumeroCopertina(pubblicazione.getNumeroCopertina());
		idvo.setPrezzoCopertina(pubblicazione.getPrezzoCopertina());
		idvo.setPrezzoEdicola(pubblicazione.getPrezzoEdicola());
		idvo.setQuantita(qta);
		idvo.setIsContoDeposito(pubblicazione.getQuantitaCopieContoDeposito() != null && pubblicazione.getQuantitaCopieContoDeposito() > 0);
		idvo.setIsScaduta(pubblicazione.getIsScaduta());
		inventarioService.saveBaseVo(idvo);
	}

	/**
	 * Rimuove dettagli dall'inventario su quantità richiesta negativa
	 * 
	 * @param InventarioDto
	 *            pubblicazione
	 * @param InventarioDettaglioVo
	 *            det
	 * @param Integer
	 *            qta
	 * @throws QuantitaCopieMaggioreDiInseritaException
	 */
	private void deleteDettagli(InventarioDto pubblicazione, InventarioDettaglioVo det, Integer qta) throws QuantitaCopieMaggioreDiInseritaException {
		if (Math.abs(pubblicazione.getQuantita()) > det.getQuantita()) {
			throw new QuantitaCopieMaggioreDiInseritaException(MessageFormat.format(getText("msg.errore.quantita.inventario.superiore.a.inserita"), pubblicazione.getTitolo(), pubblicazione.getNumeroCopertina()));
		}
		Integer quantita = det.getQuantita() + pubblicazione.getQuantita();
		if (quantita > 0) {
			det.setQuantita(quantita);
			inventarioService.saveBaseVo(det);
		} else {
			inventarioService.deleteVo(det);
			quantita = 0;
		}
	}
	
}
