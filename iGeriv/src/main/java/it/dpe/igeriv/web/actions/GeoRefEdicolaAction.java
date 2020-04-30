package it.dpe.igeriv.web.actions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.livellamenti.LivellamentiService;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.LivellamentiVo;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@BreadCrumb("%{crumbName}")
@Scope("prototype")
@Component("georefedicolaAction")
public class GeoRefEdicolaAction extends RestrictedAccessBaseAction implements State {
	
	/*
	 * alter table tbl_9106 add FLGGEO9106  number(1)	default 0	not null;
	 */
	
	private static final long serialVersionUID = 1L;
	private final String crumbName = getText("comunicazioni.georef.indirizzo.edicola");
	private String titleGeoRefIndirizzoEdicola;
	private final EdicoleService  serviceEdicole;
	private Map<String, String> anagraficaEdicola;
	private List<Map<String,String>> edicoleVicine;
	private final LivellamentiService<LivellamentiVo> livellamentiService;
	
	private String lat;
	private String lng;
	private String indirizzo;
	private String[] arrayCrivwGeoref;
	
	public GeoRefEdicolaAction(){
		this.serviceEdicole = null;
		this.livellamentiService = null;
	}
	@Autowired
	public GeoRefEdicolaAction(EdicoleService serviceEdicole,LivellamentiService<LivellamentiVo> livellamentiService) {
		this.serviceEdicole = serviceEdicole;
		this.anagraficaEdicola = new LinkedHashMap<String, String>();
		this.lat = "";
		this.lng = "";
		this.indirizzo = "";
		edicoleVicine = new ArrayList<Map<String,String>>();
		this.livellamentiService = livellamentiService;
		
	}
	
	@Override
	public void prepare() throws Exception {
		titleGeoRefIndirizzoEdicola = getText("comunicazioni.georef.indirizzo.edicola.title");
		super.prepare();
	}
	
	@SkipValidation
	public String showGeoRefEdicola() {
		AnagraficaEdicolaVo anagraficaEdicolaVo = serviceEdicole.getAnagraficaEdicola(getAuthUser().getCodDpeWebEdicola());
		if(anagraficaEdicolaVo!=null){
			anagraficaEdicola.put("ragioneSocialeEdicola", anagraficaEdicolaVo.getRagioneSocialeEdicolaPrimaRiga().replaceAll("'", "\\\\'"));
			anagraficaEdicola.put("indirizzoEdicola", anagraficaEdicolaVo.getIndirizzoEdicolaPrimaRiga().replaceAll("'", "\\\\'"));
			anagraficaEdicola.put("localitaEdicola",anagraficaEdicolaVo.getLocalitaEdicolaPrimaRiga().replaceAll("'", "\\\\'"));
			anagraficaEdicola.put("flagReadOnly",anagraficaEdicolaVo.getFlggeo9106().toString());
			anagraficaEdicola.put("latDB",anagraficaEdicolaVo.getLatitudine()!=null?anagraficaEdicolaVo.getLatitudine().toString().replaceAll(",", "."):"");
			anagraficaEdicola.put("lngDB",anagraficaEdicolaVo.getLongitudine()!=null?anagraficaEdicolaVo.getLongitudine().toString().replaceAll(",", "."):"");
			
			String str_keymapDl = "igeriv.key.google.maps."+getAuthUser().getCodFiegDl();
 			String keymapDl = getText(str_keymapDl);
			if(!keymapDl.equals(str_keymapDl)){
				if(!getAuthUser().isEdicolaPromo()){
					sessionMap.put("keyGoogleMaps", keymapDl); 		
				}else{
					return ERROR;
				}	
			}else{
				return ERROR;
			}
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String showGruppoReteEdicole() {
		AnagraficaEdicolaVo anagraficaEdicolaVo = serviceEdicole.getAnagraficaEdicola(getAuthUser().getCodDpeWebEdicola());
		//List<EdicolaDto>  listEdicoleDto = serviceEdicole.getEdicoleInArea(anagraficaEdicolaVo, new Integer("10"));
					
//		for (EdicolaDto dtEdicola : listEdicoleDto) {
//			Map<String,String> m = new HashMap<String, String>();	
//			
//			m.put("codEdicolaWeb", dtEdicola.getCodEdicolaWeb().toString());
//			m.put("ragioneSociale1", dtEdicola.getRagioneSociale1().replace("'", "\\'"));
//			m.put("latitudine", dtEdicola.getLatitudine().toString());
//			m.put("longitudine", dtEdicola.getLongitudine().toString());	
//			edicoleVicine.add(m);
//		}
//		Gson gson = new GsonBuilder().setExclusionStrategies().create();
		//requestMap.put("edicoleVicine", listEdicoleDto);
		
		
		if(anagraficaEdicolaVo!=null){
			anagraficaEdicola.put("ragioneSocialeEdicola", anagraficaEdicolaVo.getRagioneSocialeEdicolaPrimaRiga().replaceAll("'", "\\\\'"));
			anagraficaEdicola.put("indirizzoEdicola", anagraficaEdicolaVo.getIndirizzoEdicolaPrimaRiga().replaceAll("'", "\\\\'"));
			anagraficaEdicola.put("localitaEdicola",anagraficaEdicolaVo.getLocalitaEdicolaPrimaRiga().replaceAll("'", "\\\\'"));
			anagraficaEdicola.put("flagReadOnly",anagraficaEdicolaVo.getFlggeo9106().toString());
			anagraficaEdicola.put("latDB",anagraficaEdicolaVo.getLatitudine()!=null?anagraficaEdicolaVo.getLatitudine().toString().replaceAll(",", "."):"");
			anagraficaEdicola.put("lngDB",anagraficaEdicolaVo.getLongitudine()!=null?anagraficaEdicolaVo.getLongitudine().toString().replaceAll(",", "."):"");
			anagraficaEdicola.put("indirizzoLocalita",anagraficaEdicolaVo.getIndirizzoEdicolaPrimaRiga() +","+anagraficaEdicolaVo.getLocalitaEdicolaPrimaRiga());
			
			String str_keymapDl = "igeriv.key.google.maps."+getAuthUser().getCodFiegDl();
			String keymapDl = getText(str_keymapDl);
			if(!keymapDl.equals(str_keymapDl)){
				if(!getAuthUser().isEdicolaPromo()){
					sessionMap.put("keyGoogleMaps", keymapDl);
				}else{
					return ERROR;
				}			
			}else{
				return ERROR;
			}
		}
		return "showGruppoReteEdicole";
	}
	
	@SkipValidation
	public String saveGeocodingAddress() throws ParseException {
		if(this.lat!=null && this.lng!=null && !this.lat.equals("") && !this.lng.equals("")){
			AnagraficaEdicolaVo anagraficaEdicolaVo = serviceEdicole.getAnagraficaEdicola(getAuthUser().getCodDpeWebEdicola());
			anagraficaEdicolaVo.setLatitudine(new Double(this.lat));
			anagraficaEdicolaVo.setLongitudine(new Double(this.lng));
			anagraficaEdicolaVo.setFlggeo9106(new Integer(1));
			serviceEdicole.saveBaseVo(anagraficaEdicolaVo);
		}
		return showGeoRefEdicola();
	}
	
	@SkipValidation
	public String saveGruppoEdicoleGeoref() throws ParseException {
		System.out.println("SIZE "+this.arrayCrivwGeoref.length);
		
		//Cencello sempre tutte le edicole vicine
		livellamentiService.deleteEdicoleVicine(getAuthUser().getCodDpeWebEdicola());
		
		StringTokenizer stCrivwGeoref = new StringTokenizer(arrayCrivwGeoref[0], ",");
		while (stCrivwGeoref.hasMoreElements()) {
			livellamentiService.saveEdicolaVicine(getAuthUser().getCodDpeWebEdicola(), new Integer(stCrivwGeoref.nextElement().toString()));
		}
		
		return showGruppoReteEdicole();
	}
	
	
	
	@Override
	public Map getParameters(Context arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveParameters(Context arg0, String arg1, Map arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
