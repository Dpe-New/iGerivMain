package it.dpe.igeriv.web.listener;

import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.help.HelpService;
import it.dpe.igeriv.bo.localita.LocalitaService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.bo.statistiche.StatisticheService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.Types;
import it.dpe.igeriv.vo.AnagraficaBancaVo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

/**
 * Classe che ascolta l'oggetto javax.servlet.ServletContext,
 * esegue operazioni di inizializzazione e setta variabili 
 * nel contesto "application".
 * 
 * @author romanom
 * 
 */
@Component("IGerivServletContextAware")
public class IGerivServletContextAware implements ServletContextAware {
	private final LocalitaService localitaService;
	private final RifornimentiService rifornimentiService;
	private final ClientiService<AnagraficaBancaVo> clientiService;
	
	@Autowired
	public IGerivServletContextAware(
			LocalitaService localitaService,
			VenditeService venditeService,
			HelpService helpService,
			MenuService menuService,
			RifornimentiService rifornimentiService,
			ClientiService<AnagraficaBancaVo> clientiService,
			StatisticheService statisticheService) {
		this.localitaService = localitaService;
		this.rifornimentiService = rifornimentiService;
		this.clientiService = clientiService;
	}
	
	@Override
	public void setServletContext(ServletContext arg0) {
		IGerivMessageBundle.initialize();
		Types.ContoDepositoType.TUTTO.setValue(IGerivMessageBundle.get("igeriv.spunta.auto"));
		arg0.setAttribute("poweredBy", MessageFormat.format(IGerivMessageBundle.get("dpe.powered.by"), "" + Calendar.getInstance().get(Calendar.YEAR)));
		arg0.setAttribute("tipiLocalita", localitaService.getTipiLocalita());
		arg0.setAttribute("paesi", localitaService.getPaesi());
		arg0.setAttribute("province", localitaService.getProvince());
		arg0.setAttribute("tipiEdicola", getTipiEdicola());
		arg0.setAttribute("risposteClientiCodificate", rifornimentiService.getRisposteClientiCodificate());		
		arg0.setAttribute("listBanche", clientiService.getBanche());
		arg0.setAttribute("giorniSettimana", buildGiorniSettimana());
		arg0.setAttribute("CDL_CODE", 226);
		arg0.setAttribute(IGerivConstants.ID, IGerivConstants.getNameToValueMap());
	}
	
	private List<KeyValueDto> buildGiorniSettimana() {
		List<KeyValueDto> list = new ArrayList<KeyValueDto>();
		String[] giorni = IGerivMessageBundle.get("plg.calendario.giorni.settimana").replaceAll("'", "").split(",");
		int i = 0;
		for (String giorno : giorni) {
			KeyValueDto tp1 = new KeyValueDto();
			tp1.setKeyInt(++i);
			tp1.setValue(giorno);
			list.add(tp1);
		}
		return list;
	}
	
	private List<KeyValueDto> getTipiEdicola() {
		List<KeyValueDto> list = new ArrayList<KeyValueDto>();
		KeyValueDto tp1 = new KeyValueDto();
		tp1.setKeyInt(IGerivConstants.COD_TIPO_EDICOLA_CHIOSCO_GIORNALI);
		tp1.setValue(IGerivMessageBundle.get("igeriv.chiosco.giornali"));
		list.add(tp1);
		KeyValueDto tp2 = new KeyValueDto();
		tp2.setKeyInt(IGerivConstants.COD_TIPO_EDICOLA_CHIOSCO_PROMISCUO);
		tp2.setValue(IGerivMessageBundle.get("igeriv.chiosco.promiscuo"));
		list.add(tp2);
		KeyValueDto tp3 = new KeyValueDto();
		tp3.setKeyInt(IGerivConstants.COD_TIPO_EDICOLA_NEGOZIO);
		tp3.setValue(IGerivMessageBundle.get("igeriv.negozio"));
		list.add(tp3);
		return list;
	}
	
}
