package it.dpe.igeriv.web.actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.dto.BaseDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaAggiuntiviVo;
import it.dpe.igeriv.vo.ImmagineAnagraficaEdicolaVo;
import it.dpe.igeriv.vo.pk.ImmagineAnagraficaEdicolaPk;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Scope("prototype")
@Component("anagraficaRivenditaAction")
public class AnagraficaRivenditaAction<T extends BaseDto> extends
		RestrictedAccessBaseAction implements State, RequestAware {
	private static final long serialVersionUID = 6609994032055474546L;
	private final String crumbName = getText("igeriv.anagrafica.rivendita");
	private final EdicoleService service;
	private final String imgAnagraficaEdicoleDir;
	private Map<String, String> posizionamentoMap;
	private Map<String, String> stagionalitaMap;
	private Map<String, String> localizzazioneMap;
	private AnagraficaEdicolaAggiuntiviVo anagraficaEdicolaAggiuntiviVo;
	private Boolean condUsoAccettate;
	@Getter(AccessLevel.NONE)
	private Integer codDl;
	private File foto1;
	private String foto1ContentType;
	private String foto1FileName;
	private Boolean showInsertImg;
	@Getter(AccessLevel.NONE)
	private Integer radomInt;
	
	public AnagraficaRivenditaAction() {
		this(null, null);
	}
	
	@Autowired
	public AnagraficaRivenditaAction(EdicoleService service, @Value("${img.immagini.anagrafica.edicole.path.dir}") String imgAnagraficaEdicoleDir) {
		this.service = service;
		this.imgAnagraficaEdicoleDir = imgAnagraficaEdicoleDir;
		this.posizionamentoMap = new LinkedHashMap<>();
		this.posizionamentoMap.put("3", getText("igeriv.aeca.aeroporto"));
		this.posizionamentoMap.put("2", getText("igeriv.aeca.autogrill"));
		this.posizionamentoMap.put("9", getText("igeriv.aeca.bar"));
		this.posizionamentoMap.put("12", getText("igeriv.aeca.campeggio"));
		this.posizionamentoMap.put("6", getText("igeriv.aeca.centro.commerciale"));
		this.posizionamentoMap.put("1", getText("igeriv.aeca.distributore.carburante"));
		this.posizionamentoMap.put("10", getText("igeriv.aeca.hotel"));
		this.posizionamentoMap.put("7", getText("igeriv.aeca.ospedale"));
		this.posizionamentoMap.put("11", getText("igeriv.aeca.ristorante"));
		this.posizionamentoMap.put("4", getText("igeriv.aeca.stazione.ferroviaria"));
		this.posizionamentoMap.put("5", getText("igeriv.aeca.stazione.metropolitana"));
		this.posizionamentoMap.put("8", getText("igeriv.aeca.universita"));
		
		this.stagionalitaMap = new LinkedHashMap<>();
		this.stagionalitaMap.put("1", getText("igeriv.aeca.nessuna.stagionalita"));
		this.stagionalitaMap.put("2", getText("igeriv.aeca.invernale"));
		this.stagionalitaMap.put("3", getText("igeriv.aeca.estiva"));
		this.stagionalitaMap.put("4", getText("igeriv.aeca.invernale.estiva"));

		this.localizzazioneMap = new LinkedHashMap<>();
		this.localizzazioneMap.put("4", getText("igeriv.aeca.centro.citta.centro.storico"));
		this.localizzazioneMap.put("1", getText("igeriv.aeca.frazione"));
		this.localizzazioneMap.put("6", getText("igeriv.aeca.periferia"));
		this.localizzazioneMap.put("8", getText("igeriv.aeca.quartiere.residenziale.popolare"));
		this.localizzazioneMap.put("5", getText("igeriv.aeca.semiperiferia"));
		this.localizzazioneMap.put("3", getText("igeriv.aeca.via.alta.densita.commerciale"));
		this.localizzazioneMap.put("7", getText("igeriv.aeca.via.alta.densita.traffico"));
		this.localizzazioneMap.put("2", getText("igeriv.aeca.zona.industriale"));
	}

	@Override
	public void prepare() throws Exception {
		super.prepare();
	}

	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String show() {
		anagraficaEdicolaAggiuntiviVo = service.getAnagraficaEdicolaAggiuntiviVoByCodEdicolaWeb(getAuthUser().getCodDpeWebEdicola());
		condUsoAccettate = getAuthUser().getCondizioniUsoAccettate();
		if (anagraficaEdicolaAggiuntiviVo == null) {
			anagraficaEdicolaAggiuntiviVo = new AnagraficaEdicolaAggiuntiviVo();
			anagraficaEdicolaAggiuntiviVo.setCodEdicola(getAuthUser().getCodDpeWebEdicola());
			anagraficaEdicolaAggiuntiviVo.setTipo(1);
			anagraficaEdicolaAggiuntiviVo.setSottotipo(1);
			anagraficaEdicolaAggiuntiviVo.setStagionalita(1);
			anagraficaEdicolaAggiuntiviVo.setLocalizzazione(1);
		}
		codDl = getAuthUser().getCodFiegDl();
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String save() {
		try {
			Integer codEdicola = getAuthUser().getCodDpeWebEdicola();
			AbbinamentoEdicolaDlVo abbinamentoEdicolaDlVo = service.getAbbinamentoEdicolaDlVoByCodEdicolaWeb(codEdicola);
			anagraficaEdicolaAggiuntiviVo.setCodEdicola(codEdicola);
			anagraficaEdicolaAggiuntiviVo.setAnagraficaEdicolaVo(abbinamentoEdicolaDlVo.getAnagraficaEdicolaVo());
			anagraficaEdicolaAggiuntiviVo.setDataUltimaModifica(service.getSysdate());
			List<ImmagineAnagraficaEdicolaVo> listImg = new ArrayList<ImmagineAnagraficaEdicolaVo>();
			buildImg(listImg, 1);
			service.saveAnagraficaEdicola(anagraficaEdicolaAggiuntiviVo, listImg);
			abbinamentoEdicolaDlVo.setAnagraficaCompilata(true);
			abbinamentoEdicolaDlVo.setCondizioniUsoAccettate(condUsoAccettate != null);
			service.saveBaseVo(abbinamentoEdicolaDlVo);
			getAuthUser().setAnagraficaCompilata(true);
			getAuthUser().setCondizioniUsoAccettate(condUsoAccettate != null);
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "redirect";
	}

	/**
	 * @param listImg
	 * @param index
	 * @throws IOException
	 */
	private void buildImg(List<ImmagineAnagraficaEdicolaVo> listImg, int index)
			throws IOException {
		if (foto1 != null) {
			File outImgDirResized = new File(imgAnagraficaEdicoleDir);
			if (!outImgDirResized.isDirectory()) {
				outImgDirResized.mkdirs();
			}
			String ext = foto1FileName.indexOf(".") != -1 ? foto1FileName.substring(foto1FileName.lastIndexOf(".")) : foto1FileName;
			String name = "img_anagrafica_" + getAuthUser().getCodFiegDlMaster().toString() + "_" + getAuthUser().getCodEdicolaMaster() + "_" + index + ext;
			File out = new File(imgAnagraficaEdicoleDir + System.getProperty("file.separator") + name);
			FileUtils.copyFile(foto1, out);
			FileUtils.resizeImage(out, new File(imgAnagraficaEdicoleDir), 900, null);
			ImmagineAnagraficaEdicolaVo img = service.getImmagineAnagraficaEdicola(getAuthUser().getCodEdicolaMaster(), index);
			if (img == null) {
				img = new ImmagineAnagraficaEdicolaVo();
				ImmagineAnagraficaEdicolaPk pk = new ImmagineAnagraficaEdicolaPk();
				pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
				pk.setProgressivo(1);
				img.setPk(pk);
			}
			img.setDataUltimaModifica(service.getSysdate());
			img.setNomeImmagine(name);
			img.setImmagine(Files.readAllBytes(Paths.get(out.getAbsolutePath())));
			listImg.add(img);
		}
	}

	@SuppressWarnings("rawtypes")
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	@SuppressWarnings("rawtypes")
	public void saveParameters(Context context, String arg1, Map arg2) {
		
	}

	public Integer getRadomInt() {
		return (int) (Math.random() * 10);
	}

	public Integer getCodDl() {
		return codDl;
	}
	
	
	
	
}
