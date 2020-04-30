package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.scolastica.ScolasticaService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.vo.OrdineLibriDettVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo.ETrack;
import it.dpe.igeriv.vo.OrdineLibriVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("libriScolasticiVenditaAction")
@SuppressWarnings({"rawtypes"}) 
             
public class LibriScolasticiVenditaAction <T extends BaseDto> extends RestrictedAccessBaseAction implements State{
	private static final long serialVersionUID = 1L;
	private final String crumbName;
	private String ordineFornitore;
	private String nome;
	private String cognome;
	private String actionName;
	private String ordiniSelezionati;
	private String cliente;
	
	private final ScolasticaService scolasticaService;
	private final ClientiService<ClienteEdicolaDto> clientiService;
	
	public LibriScolasticiVenditaAction() {
		this(null, null);
	}
	@Autowired
	public LibriScolasticiVenditaAction(ScolasticaService scolasticaService, ClientiService<ClienteEdicolaDto> clientiService) {
		this.crumbName = getText("igeriv.menu.707");
		this.scolasticaService = scolasticaService;
		this.clientiService = clientiService;
		this.actionName="libriScolasticiVendita_showFilter.action";
	}
	
	@Override
	public void validate() {
		if (ordineFornitore==null || ordineFornitore.trim().isEmpty()) {
			addActionError(getText("error.scolastica.ordinefornitore.vuoto"));
		} 
	}
	
	@SkipValidation
	public String showFilter() {
		requestMap.put("tableTitle", getText("igeriv.scolastica.consegna.libri.cliente"));
		return SUCCESS;
	}
	
	
	@BreadCrumb("%{crumbName}")
	public String showVendite() {
		requestMap.put("tableTitle", getText("igeriv.scolastica.consegna.libri.cliente"));
		
		List<OrdineLibriDettVo> ordini = scolasticaService.getOrdiniFornitore(ordineFornitore, ETrack.RIV, getAuthUser().getCodDpeWebEdicola());
		cliente = getDescCliente();
		requestMap.put("itemsOrdini", ordini);
		requestMap.put("itemsClenti", null);
		return SUCCESS;
	} 
	@BreadCrumb("%{crumbName}")
	public String showClienti() {
		requestMap.put("tableTitle", getText("igeriv.scolastica.consegna.libri.cliente"));
		
		getAuthUser().getArrId();
		List<ClienteEdicolaDto> clientiEdicola =
				clientiService.getClientiEdicola(getAuthUser().getArrId(), nome, cognome, null, null);
		
		cliente = getDescCliente();
		requestMap.put("itemsOrdini", null);
		requestMap.put("itemsClenti", clientiEdicola);
		return SUCCESS;
	} 
	@BreadCrumb("%{crumbName}")
	public String showOrdini() {
		requestMap.put("tableTitle", getText("igeriv.scolastica.consegna.libri.cliente"));
		return SUCCESS;
	} 
	
	@BreadCrumb("%{crumbName}")
	public String showConferma() {
		requestMap.put("tableTitle", getText("igeriv.scolastica.consegna.libri.cliente"));
		
		List<OrdineLibriDettVo> dettOrdiniSel = getOrdiniSelezionati();
		if (dettOrdiniSel==null || dettOrdiniSel.isEmpty()) {
			addActionError("Nessun ordine selezionato");
			return showFilter();
		}
		scolasticaService.updateStatoOrdini(dettOrdiniSel, ETrack.CLI);
		return showFilter();
		
	}
	
	private List<OrdineLibriDettVo> getOrdiniSelezionati() {
		List<OrdineLibriDettVo> ordini = scolasticaService.getOrdiniFornitore(ordineFornitore, ETrack.RIV, getAuthUser().getCodDpeWebEdicola());
		cliente = getDescCliente();
		List<OrdineLibriDettVo> result = new ArrayList<>(); 
		if (ordiniSelezionati==null || ordiniSelezionati.isEmpty()) {
			return null;
		}
		if (ordini==null || ordini.isEmpty()) {
			return null;
		}
		String[] strIds = ordiniSelezionati.split(",");
		for (String strId : strIds) {
			final long id = Long.parseLong(strId);
			OrdineLibriDettVo vo = Iterables.find(ordini, new Predicate<OrdineLibriDettVo>() {
										public boolean apply(OrdineLibriDettVo t) {return t!=null && t.getSeqordine()!=null && t.getSeqordine()==id;};
									});
			if (vo==null) {
				return null;
			}
			result.add(vo);
					
		}
		return result;
	}
	
	private String getDescCliente() {
		OrdineLibriVo vo = scolasticaService.getOrdine(ordineFornitore);
		return (vo!=null && vo.getCliente()!=null) ?
			String.format("Cliente: %s %s", vo.getCliente().getCognome(), vo.getCliente().getNome() )
			: "";
		
	}
	
	
	
	@Override
	public void saveParameters(Context context, String tableId, Map parameterMap) {
	}

	@Override
	public Map getParameters(Context context, String tableId, String stateAttr) {
		return null;
	}

	@Override
	public String getTitle() {
		return super.getTitle() + getText("igeriv.menu.707");
	}
	
}
