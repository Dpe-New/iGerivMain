package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.Matchers.equalTo;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.IIOException;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.hamcrest.Matchers;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.dto.CategoriaDto;
import it.dpe.igeriv.dto.ProdottoDto;
import it.dpe.igeriv.dto.SottoCategoriaDto;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.FileUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.RtaeConstants;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCausaliContabilitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezzoVenditaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiCategoriaEdicolaPk;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiSottoCategoriaEdicolaPk;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe action per la gestione delle anagrafiche dei prodotti non editoriali.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("prodottiNonEditorialiAction")
@SuppressWarnings({ "rawtypes" })
public class ProdottiNonEditorialiAction<T extends BaseVo> extends
		RestrictedAccessBaseAction implements State {
	private static final long serialVersionUID = 1L;
	private final ProdottiService prodottiService;
	private final EdicoleService edicoleService;
	private final String imgMiniaturePathDir;
	private final String crumbName = getText("igeriv.menu.44");
	private String tableTitle;
	private List<Map<String, List<Map<String, List<ProdottoDto>>>>> listProdotti;
	private List<Map<String, List<Map<String, List<ProdottoDto>>>>> listProdottiEdicola;
	private String idProdottiEdicola;
	private String codProdotto;
	private Long codCategoria;
	private Long codSottoCategoria;
	private String descrizioneProdotto;
	private ProdottiNonEditorialiVo prodottoEdicola;
	private ProdottiNonEditorialiCategoriaEdicolaVo categoria;
	private ProdottiNonEditorialiSottoCategoriaEdicolaVo sottocategoria;
	private List<CategoriaDto> categorie;
	private List<ProdottiNonEditorialiPrezzoVenditaVo> prezzi;
	private String prezziSelected;
	private String validitaSelected;
	private String catTitle;
	private String subCatTitle;
	private File attachment1;
	private String attachment1ContentType;
	private String attachment1FileName;
	private String newProduct;
	private String deleteDependenciesProdotto;
	private String lastProdotto;
	private String prevProdotto;
	private String lastSubcategory; 
	private String sortedProdottiIds; 
	private String sortedCategoryIds;
	private String sortedSubcategoryIds;
	private String descrizioneProdottoAOriginale;
	private String causaleIva;
	
	public ProdottiNonEditorialiAction() {
		this.prodottiService = null;
		this.edicoleService = null;
		this.imgMiniaturePathDir = null;
	}
	
	@Autowired
	public ProdottiNonEditorialiAction(ProdottiService prodottiService, EdicoleService edicoleService, @Value("${img.miniature.edicola.prodotti.vari.path.dir}") String imgMiniaturePathDir) {
		this.prodottiService = prodottiService;
		this.edicoleService = edicoleService;
		this.imgMiniaturePathDir = imgMiniaturePathDir;
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
	}
	
	@Override
	public void validate() {
		if (getActionName() != null && getActionName().contains("saveProdottoEdicola")) {
			if (prodottoEdicola.getDescrizioneProdottoA() == null || prodottoEdicola.getDescrizioneProdottoA().equals("")) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("igeriv.descrizione")) + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
			if (!Strings.isNullOrEmpty(prezziSelected)) {
				try {
					StringTokenizer stPrezzi = new StringTokenizer(prezziSelected, ",");
					while (stPrezzi.hasMoreTokens()) {
						String prezzoStr = stPrezzi.nextToken();
						NumberUtils.parseNumber(prezzoStr, Float.class);
					}
				} catch (Exception e) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("msg.prezzo.invalido") + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
			} 
			if (!Strings.isNullOrEmpty(validitaSelected)) { 
				try {
					StringTokenizer stValidita = new StringTokenizer(validitaSelected, ",");
					while (stValidita.hasMoreTokens()) {
						String validitaStr = stValidita.nextToken();
						DateUtilities.parseDate(validitaStr, DateUtilities.FORMATO_DATA_SLASH).getTime();
					}
				} catch (ParseException e) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("msg.formato.data.invalido"), getText("igeriv.data")) + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
			} else {
				validitaSelected = DateUtilities.getTimestampAsString(new Date(), DateUtilities.FORMATO_DATA_SLASH);
			}
			if (!Strings.isNullOrEmpty(prezziSelected) && !Strings.isNullOrEmpty(validitaSelected)) {
				List<String> prList = select(Arrays.asList(prezziSelected.split(",")), having(on(String.class), Matchers.not(Matchers.equalTo(""))));
				List<String> vaList = select(Arrays.asList(validitaSelected.split(",")), having(on(String.class), Matchers.not(Matchers.equalTo(""))));
				if (prList.size() != vaList.size()) {
					requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("msg.formato.data.invalido"), getText("igeriv.data")) + IGerivConstants.END_EXCEPTION_TAG);
					throw new IGerivRuntimeException();
				}
			}
			if (!Strings.isNullOrEmpty(prodottoEdicola.getBarcode())) { 
				
				//06/10/2017 CDL DINO CHIEDE LA RIMOZIONE DELLA VALIDAZIONE DEL CODICE A BARRE PER INSERIRE DEI PROPRI CODICI
				if(getAuthUser().getCodFiegDlMaster()!= RtaeConstants.COD_FIEG_CDL){
					if(prodottoEdicola.getBarcode().trim().length() == 10)
					{
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("igeriv.codice.barre.riservato"), getText("igeriv.barcode")) + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					}
				}
				
			}
		}
	}
	
	public void validateSaveCategoriaEdicola() {
		if (categoria.getDescrizione() == null || categoria.getDescrizione().equals("")) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("igeriv.descrizione")) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
	}
	
	public void validateSaveSottoCategoriaEdicola() {
		if (sottocategoria.getDescrizione() == null || sottocategoria.getDescrizione().equals("")) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.campo.x.obbligatorio"), getText("igeriv.descrizione")) + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
	}
	
	@BreadCrumb("%{crumbName}")
	@SkipValidation
	public String showProdottiTemplate() throws Exception {
		tableTitle = getText("igeriv.prodotti.non.editoriali");
		return "pneTemplate";
	}
	
	@SkipValidation
	public String showProdotti() throws Exception {
		if (idProdottiEdicola != null && !idProdottiEdicola.equals("")) {
			saveProdottiTemplate();
		}
		listProdotti = prodottiService.getProdottiNonEditorialiTree(getAuthUser().getCodEdicolaMaster());
		listProdottiEdicola = prodottiService.getProdottiNonEditorialiEdicolaTree(getAuthUser().getCodEdicolaMaster());
		return "pneTemplateEdicola";
	}
	
	@SkipValidation
	public String saveProdottiTemplate() throws Exception {
		prodottiService.saveProdottiTemplate(idProdottiEdicola, getAuthUser().getCodEdicolaMaster());
		lastProdotto = "folder_2_" + idProdottiEdicola; 
		return "pneTemplateEdicola";
	}
	
	@SkipValidation
	public String updateProductCategory() throws Exception {
		if (codProdotto != null && !codProdotto.equals("") && codCategoria != null && !codCategoria.equals("") && codSottoCategoria != null && !codSottoCategoria.equals("")) {
			ProdottiNonEditorialiVo vo = prodottiService.getProdottiNonEditorialiVo(new Long(codProdotto), getAuthUser().getCodEdicolaMaster());
			Long codCatPrec = vo.getCodCategoria();
			Long codScatPrec = vo.getCodSottoCategoria();
			try {
				vo.setCodCategoria(codCategoria);
				vo.setCodSottoCategoria(codSottoCategoria);
				prodottiService.saveBaseVo(vo);
			} catch (Exception e) {
				addActionError(getText("msg.errore.invio.richiesta.html"));
			}
			lastProdotto = "folder_2_" + codProdotto;
			ProdottiNonEditorialiVo firstPorduct = prodottiService.getFirstProdottoNonEditorialeEdicola(codCatPrec, codScatPrec, getAuthUser().getCodEdicolaMaster());
			if (firstPorduct != null) {
				prevProdotto = "folder_2_" + firstPorduct.getCodProdottoInterno();
			}
			showProdotti();
		}
		return  "pneTemplateEdicola";
	}	
	
	@SkipValidation
	public String updateCategoryPositions() throws Exception {
		if (sortedCategoryIds != null && !sortedCategoryIds.equals("")) {
			try {
				String[] catStrIds = sortedCategoryIds.split(",");
				List<Long> catIds = new ArrayList<Long>();
				for (String catId : catStrIds) {
					catIds.add(new Long(catId.trim()));
				}
				if (!catIds.isEmpty()) {
					prodottiService.updateCategoryPositions(catIds, getAuthUser().getCodEdicolaMaster());
				}
			} catch (Exception e) {
				addActionError(getText("msg.errore.invio.richiesta.html"));
			}
			lastProdotto = "folder_2_" + codProdotto; 
			showProdotti();
		}
		return  "pneTemplateEdicola";
	}	
	
	@SkipValidation
	public String updateSubCategoryPositions() throws Exception {
		if (sortedSubcategoryIds != null && !sortedSubcategoryIds.equals("")) {
			try {
				String[] scatStrIds = sortedSubcategoryIds.split(",");
				List<Long> catIds = new ArrayList<Long>();
				List<Long> scatIds = new ArrayList<Long>();
				for (String scatId : scatStrIds) {
					String[] scatStrId = scatId.split("_");
					catIds.add(new Long(scatStrId[0].trim()));
					scatIds.add(new Long(scatStrId[1].trim()));
				}
				if (!catIds.isEmpty() && !scatIds.isEmpty()) {
					prodottiService.updateSubCategoryPositions(catIds, scatIds, getAuthUser().getCodEdicolaMaster());
				}
			} catch (Exception e) {
				addActionError(getText("msg.errore.invio.richiesta.html"));
			}
			lastProdotto = "folder_2_" + codProdotto; 
			showProdotti();
		}
		return  "pneTemplateEdicola";
	}	
	
	@SkipValidation
	public String updateProductPositions() throws Exception {
		if (sortedProdottiIds != null && !sortedProdottiIds.equals("")) {
			try {
				String[] prodStrIds = sortedProdottiIds.split(",");
				List<Long> prodIds = new ArrayList<Long>();
				for (String prodId : prodStrIds) {
					prodIds.add(new Long(prodId.trim()));
				}
				if (!prodIds.isEmpty()) {
					prodottiService.updateProductPositions(prodIds);
				}
			} catch (Exception e) {
				addActionError(getText("msg.errore.invio.richiesta.html"));
			}
			lastProdotto = "folder_2_" + codProdotto; 
			showProdotti();
		}
		return  "pneTemplateEdicola";
	}	
	
	@SkipValidation
	public String deleteProdottoEdicola() throws Exception {
		if (codProdotto != null && !codProdotto.equals("")) {
			ProdottiNonEditorialiVo vo = prodottiService.getProdottiNonEditorialiVo(new Long(codProdotto), getAuthUser().getCodEdicolaMaster());
			if (vo != null) {
				Long codCategoria2 = vo.getCodCategoria();
				try {
					Boolean bDeleteDependenciesProdotto = Boolean.valueOf(deleteDependenciesProdotto == null ? "false" : deleteDependenciesProdotto);
					prodottiService.deleteProdottoEdicola(vo, bDeleteDependenciesProdotto);
				} catch (Throwable e) {
					if (e.getCause() != null && e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
						addActionError(MessageFormat.format(getText("error.delete.prodotto.in.bolla.no.delete"), vo.getDescrizioneProdottoA().replaceAll(IGerivConstants.EURO_SIGN_DECIMAL, IGerivConstants.EURO_SIGN)));
					} else {
						addActionError(getText("msg.errore.invio.richiesta.html"));
					}
				}
				ProdottiNonEditorialiVo firstPorduct = prodottiService.getFirstProdottoNonEditorialeEdicola(codCategoria2, vo.getCodSottoCategoria(), getAuthUser().getCodEdicolaMaster());
				if (firstPorduct == null) {
					ProdottiNonEditorialiSottoCategoriaEdicolaVo firstSubcat = prodottiService.getFirstSottocategoriaNonEditorialeEdicola(codCategoria2, getAuthUser().getCodEdicolaMaster());
					lastSubcategory = "sucategory_" + firstSubcat.getPk().getCodCategoria() + "_" + firstSubcat.getPk().getCodSottoCategoria();
				} else {
					lastProdotto = "folder_2_" + firstPorduct.getCodProdottoInterno();
				}
			}
		}
		showProdotti();
		return "pneTemplateEdicola";
	}
	
	@SkipValidation
	public String deleteProdottoEdicolaAjax() throws Exception {
		if (codProdotto != null && !codProdotto.equals("")) {
			ProdottiNonEditorialiVo vo = prodottiService.getProdottiNonEditorialiVo(new Long(codProdotto), getAuthUser().getCodEdicolaMaster());
			if (vo != null) {
				try {
					Boolean bDeleteDependenciesProdotto = Boolean.valueOf(deleteDependenciesProdotto == null ? "false" : deleteDependenciesProdotto);
					prodottiService.deleteProdottoEdicola(vo, bDeleteDependenciesProdotto);
				} catch (Throwable e) {
					if (e.getCause() != null && e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + MessageFormat.format(getText("error.delete.prodotto.in.bolla"), vo.getDescrizioneProdottoA()) + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					} else {
						requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("msg.errore.invio.richiesta.html") + IGerivConstants.END_EXCEPTION_TAG);
						throw new IGerivRuntimeException();
					}
				}
			}
		}
		return "pneTemplateEdicola";
	}
	
	@SkipValidation
	public String deleteCategoria() throws Exception {
		if (codCategoria != null && !codCategoria.equals("")) {
			ProdottiNonEditorialiCategoriaEdicolaVo categoria = prodottiService.getProdottiNonEditorialiCategoriaEdicolaVo(codCategoria, getAuthUser().getCodEdicolaMaster());
			Boolean recursive = Boolean.valueOf(deleteDependenciesProdotto == null ? "false" : deleteDependenciesProdotto);
			try {
				prodottiService.deleteCategoria(codCategoria, getAuthUser().getCodEdicolaMaster(), recursive);
			} catch (Throwable e) {
				if (e.getCause() != null && e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
					addActionError(MessageFormat.format(getText("error.delete.categoria.in.bolla"), categoria.getDescrizione()));
				} else {
					addActionError(getText("msg.errore.invio.richiesta.html"));
				}
			}
		}
		return showProdotti();
	}
	
	@SkipValidation
	public String deleteSottoCategoria() throws Exception {
		if (codCategoria != null && !codCategoria.equals("") && codSottoCategoria != null && !codSottoCategoria.equals("")) {
			Boolean recursive = Boolean.valueOf(deleteDependenciesProdotto == null ? "false" : deleteDependenciesProdotto);
			try {
				prodottiService.deleteSottoCategoria(codCategoria, codSottoCategoria, getAuthUser().getCodEdicolaMaster(), recursive);
			} catch (Throwable e) {
				if (e.getCause() != null && e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
					ProdottiNonEditorialiSottoCategoriaEdicolaVo scvo = prodottiService.getProdottiNonEditorialiSottoCategoriaEdicolaVo(codCategoria, codSottoCategoria, getAuthUser().getCodEdicolaMaster());
					addActionError(MessageFormat.format(getText("error.delete.sotto.categoria.in.bolla"), scvo.getDescrizione()));
				} else {
					addActionError(getText("msg.errore.invio.richiesta.html"));
				}
			}
			ProdottiNonEditorialiSottoCategoriaEdicolaVo firstSubcat = prodottiService.getFirstSottocategoriaNonEditorialeEdicola(codCategoria, getAuthUser().getCodEdicolaMaster());
			if (firstSubcat != null) {
				lastSubcategory = "sucategory_" + firstSubcat.getPk().getCodCategoria() + "_" + firstSubcat.getPk().getCodSottoCategoria();
			}
			lastProdotto = null;
		}
		return showProdotti();
	}
	
	@SkipValidation
	public String editCategoria() {
		tableTitle = getText("igeriv.inserisci.modifica.categoria.prodotto"); 
		if (codCategoria != null && !codCategoria.equals("")) {
			categoria = prodottiService.getProdottiNonEditorialiCategoriaEdicolaVo(codCategoria, getAuthUser().getCodEdicolaMaster());
		} else {
			categoria = new ProdottiNonEditorialiCategoriaEdicolaVo();
			ProdottiNonEditorialiCategoriaEdicolaPk pk = new ProdottiNonEditorialiCategoriaEdicolaPk();
			pk.setCodCategoria(prodottiService.getNextSeqVal(IGerivConstants.SEQ_CATEGORIE_EDICOLA));
			pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
			categoria.setPk(pk);
		}
		return "editCategoriaEdicola";
	}
	
	@SkipValidation
	public String editSottoCategoria() {
		tableTitle = getText("igeriv.inserisci.modifica.sottocategoria.prodotto");
		if (codCategoria != null && !codCategoria.equals("") && codSottoCategoria != null && !codSottoCategoria.equals("")) {
			sottocategoria = prodottiService.getProdottiNonEditorialiSottoCategoriaEdicolaVo(codCategoria, codSottoCategoria, getAuthUser().getCodEdicolaMaster());
		} else {
			sottocategoria = new ProdottiNonEditorialiSottoCategoriaEdicolaVo();
			ProdottiNonEditorialiSottoCategoriaEdicolaPk pk = new ProdottiNonEditorialiSottoCategoriaEdicolaPk();
			pk.setCodCategoria(codCategoria);
			pk.setCodSottoCategoria(prodottiService.getNextSeqVal(IGerivConstants.SEQ_SOTTOCATEGORIE_EDICOLA));
			pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
			sottocategoria.setPk(pk);
		}
		return "editSottoCategoriaEdicola";
	}
	
	@SkipValidation
	public String editProdottoEdicolaContent() {
		editProdottoEdicola();
		return "editProdottoContent";
	}
	
	@SkipValidation
	public String editProdottoEdicola() {
		tableTitle = getText("igeriv.inserisci.modifica.prodotto"); 
		categorie = prodottiService.getCategoriePne(getAuthUser().getCodEdicolaMaster());
		if (codProdotto != null && !codProdotto.equals("")) {
			prodottoEdicola = prodottiService.getProdottiNonEditorialiVo(new Long(codProdotto), getAuthUser().getCodEdicolaMaster());
			descrizioneProdottoAOriginale = prodottoEdicola.getDescrizioneProdottoA();
		} else {
			prodottoEdicola = new ProdottiNonEditorialiVo();
			prodottoEdicola.setEdicola(edicoleService.getAnagraficaEdicola(getAuthUser().getCodEdicolaMaster()));
			prodottoEdicola.setCodProdottoInterno(prodottiService.getNextSeqVal(IGerivConstants.SEQ_PRODOTTO_NON_EDITORIALE));
			newProduct = "true";
			if (catTitle != null) {
				
				//CDL 16/11/2016 errore per descrizione multiple uguali
				//CategoriaDto categoriaSel = selectUnique(categorie, having(on(CategoriaDto.class).getDescrizione(), equalTo(catTitle)));
				//modificato di seguito
				CategoriaDto categoriaSel = null;
				for (CategoriaDto categoriaDto : categorie) {
					if(categoriaDto.getDescrizione().equals(catTitle))
						categoriaSel = categoriaDto;
				}
				
				prodottoEdicola.setCodCategoria(categoriaSel.getCodCategoria());
				List<SottoCategoriaDto> sottocategorie = prodottiService.getSottocategoriePne(categoriaSel.getCodCategoria(), getAuthUser().getCodEdicolaMaster());
				SottoCategoriaDto sottoCat = selectUnique(sottocategorie, having(on(SottoCategoriaDto.class).getDescrizione(), equalTo(subCatTitle)));
				prodottoEdicola.setCodSottoCategoria(sottoCat.getCodSottoCategoria());
				ProdottiNonEditorialiCategoriaEdicolaVo categoria = new ProdottiNonEditorialiCategoriaEdicolaVo();
				categoria.setDescrizione("");
				prodottoEdicola.setCategoria(categoria);
			}
			if (!Strings.isNullOrEmpty(descrizioneProdotto)) {
				prodottoEdicola.setDescrizioneProdottoA(descrizioneProdotto);
			}
		}
		prezzi = prodottoEdicola.getPrezzi();
		if (prezzi != null) {
			Collections.sort(prezzi, new ProdottiNonEditorialiPrezziComparator());
		}
		return "editProdotto";
	}
	
	public String saveProdottoEdicola() throws ParseException, IOException {
		if (!getFieldErrors().isEmpty() && getFieldErrors().containsKey("attachment1")) {
			return INPUT;
		}
		if (prodottoEdicola != null) { 
			try {
				if (attachment1 != null) {
					String path = imgMiniaturePathDir + System.getProperty("file.separator") + StringUtility.buildAttachmentFileName(attachment1FileName, getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
					File outImgDirResized = new File(imgMiniaturePathDir);
					if (!outImgDirResized.isDirectory()) {
						outImgDirResized.mkdirs();
					}
				    File out = new File(path);
					FileUtils.copyFile(attachment1, out);
					FileUtils.resizeImage(out, outImgDirResized, 100, 100);
					prodottoEdicola.setNomeImmagine(out.getName().trim());
				}
				if (attachment1 == null && !Strings.isNullOrEmpty(descrizioneProdottoAOriginale) && !descrizioneProdottoAOriginale.equals(prodottoEdicola.getDescrizioneProdottoA())) {
					prodottoEdicola.setNomeImmagine(null);
				}
				if (!Strings.isNullOrEmpty(causaleIva)) {
					String[] split = causaleIva.split("\\|");
					ProdottiNonEditorialiCausaliContabilitaVo causaleIva = prodottiService.getCausaleContabilita(new Integer(split[0]), new Integer(split[1]));
					prodottoEdicola.setCausaleIva(causaleIva);
				}
				//GIFT CARD - PRODOTTI DIGITALI
				prodottoEdicola.setIsProdottoDigitale("N");
				
				prodottiService.saveProdottoEdicola(prodottoEdicola, prezziSelected, validitaSelected);
				lastProdotto = "folder_2_" + prodottoEdicola.getCodProdottoInterno(); 
			} catch (IIOException e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("struts.messages.error.uploading") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			} catch (Exception e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
		return SUCCESS;
	}
	
	public String saveCategoriaEdicola() throws ParseException, IOException {
		if (categoria != null) {
			if (attachment1 != null) {
				String path = imgMiniaturePathDir + System.getProperty("file.separator") + StringUtility.buildAttachmentFileName(attachment1FileName, getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
				File outImgDirResized = new File(imgMiniaturePathDir);
				if (!outImgDirResized.isDirectory()) {
					outImgDirResized.mkdirs();
				}
			    File out = new File(path);
				FileUtils.copyFile(attachment1, out);
				FileUtils.resizeImage(out, outImgDirResized, 100, 100);
				categoria.setImmagine(out.getName().trim());
			} else {
				String nomeImmagine = FileUtils.createFakeImage(categoria.getDescrizione(), 100, 100, imgMiniaturePathDir, 8, Font.BOLD, 14);
				categoria.setImmagine(nomeImmagine);
			}
			try {
				categoria.getPk().setCodEdicola(getAuthUser().getCodEdicolaMaster());
				prodottiService.saveBaseVo(categoria);
			} catch (Exception e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
		return SUCCESS;
	}
	
	public String saveSottoCategoriaEdicola() throws ParseException, IOException {
		if (sottocategoria != null) { 
			if (attachment1 != null) {
				String path = imgMiniaturePathDir + System.getProperty("file.separator") + StringUtility.buildAttachmentFileName(attachment1FileName, getAuthUser().getCodFiegDlMaster(), getAuthUser().getCodEdicolaMaster(), getAuthUser().getCodUtente());
				File outImgDirResized = new File(imgMiniaturePathDir);
				if (!outImgDirResized.isDirectory()) {
					outImgDirResized.mkdirs();
				}
			    File out = new File(path);
				FileUtils.copyFile(attachment1, out);
				FileUtils.resizeImage(out, outImgDirResized, 100, 100);
				sottocategoria.setImmagine(out.getName().trim());
			} else {
				String nomeImmagine = FileUtils.createFakeImage(sottocategoria.getDescrizione(), 100, 100, imgMiniaturePathDir, 8, Font.BOLD, 14);
				sottocategoria.setImmagine(nomeImmagine);
			}
			try {
				sottocategoria.getPk().setCodEdicola(getAuthUser().getCodEdicolaMaster());
				prodottiService.saveBaseVo(sottocategoria);
				lastSubcategory = "sucategory_" + sottocategoria.getPk().getCodCategoria() + "_" + sottocategoria.getPk().getCodSottoCategoria(); 
				lastProdotto = null;
			} catch (Exception e) {
				requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore") + IGerivConstants.END_EXCEPTION_TAG);
				throw new IGerivRuntimeException();
			}
		}
		return SUCCESS;
	}
	
	/**
	 * @author mromano
	 *
	 */
	private class ProdottiNonEditorialiPrezziComparator implements Comparator<ProdottiNonEditorialiPrezzoVenditaVo> {
		@Override
		public int compare(ProdottiNonEditorialiPrezzoVenditaVo o1, ProdottiNonEditorialiPrezzoVenditaVo o2) {
			if (o1 != null && o2 != null) {
				return -o1.getDataValiditaIniziale().compareTo(o2.getDataValiditaIniziale());
			}
			return 0;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#getParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.lang.String)
	 */
	public Map getParameters(Context arg0, String arg1, String arg2) {
		return arg0.getParameterMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.extremecomponents.table.state.State#saveParameters(org.extremecomponents
	 * .table.context.Context, java.lang.String, java.util.Map)
	 */
	public void saveParameters(Context context, String arg1, Map arg2) {
	}

	@Override
	public String getTitle() {
		String title = getText("igeriv.menu.44");
		return super.getTitle() + title;
	}
	
}
