package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.bolle.BolleService;
import it.dpe.igeriv.bo.card.CardService;
import it.dpe.igeriv.bo.clienti.ClientiService;
import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.fornitori.FornitoriService;
import it.dpe.igeriv.bo.inventario.InventarioService;
import it.dpe.igeriv.bo.istruzione.IstruzioneService;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.bo.note.NoteService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.bo.professioni.ProfessioniService;
import it.dpe.igeriv.bo.promemoria.PromemoriaService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.dto.CategoriaDto;
import it.dpe.igeriv.dto.ClienteEdicolaDto;
import it.dpe.igeriv.dto.DateTipiBollaDto;
import it.dpe.igeriv.dto.GiroDto;
import it.dpe.igeriv.dto.InventarioDto;
import it.dpe.igeriv.dto.KeyValueDto;
import it.dpe.igeriv.dto.ParametriEdicolaDto;
import it.dpe.igeriv.dto.ProdottoDto;
import it.dpe.igeriv.dto.ProfiloDto;
import it.dpe.igeriv.dto.PubblicazionePiuVendutaDto;
import it.dpe.igeriv.dto.SottoCategoriaDto;
import it.dpe.igeriv.exception.ConfirmRiassociareSmartCardEdicolaException;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.IGerivRuntimeException;
import it.dpe.igeriv.exception.SmartCardEdicolaGiaAssociataException;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.EncryptUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.BollaResaFuoriVoceVo;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.IstruzioneVo;
import it.dpe.igeriv.vo.ModuloInputVo;
import it.dpe.igeriv.vo.NoteRivenditaPerCpuVo;
import it.dpe.igeriv.vo.NoteRivenditaVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo;
import it.dpe.igeriv.vo.PagamentoClientiEdicolaVo;
import it.dpe.igeriv.vo.PianoProfiliEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCausaliContabilitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiFornitoreVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;
import it.dpe.igeriv.vo.ProfessioneVo;
import it.dpe.igeriv.vo.ProvinciaVo;
import it.dpe.igeriv.vo.StoricoEmailVo;
import it.dpe.igeriv.vo.TipoLocalitaVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.pk.NoteRivenditaPerCpuPk;
import it.dpe.igeriv.vo.pk.NoteRivenditaPk;
import it.dpe.service.mail.MailingListService;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.util.ServletContextAware;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.hibernate.dialect.Ingres10Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe action che ritorna stringhe in formato JSON.
 * I metodi di questa classse sono chiamati in modo asincrono via ajax e ritornano oggetti JSON
 * utilizzati direttamente dal javascript.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("jsonAction")
@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
public class JSONAction<T extends BaseVo> extends RestrictedAccessBaseAction implements State, RequestAware, ServletContextAware {
	private static final long serialVersionUID = 1L;
	private final ClientiService<ClienteEdicolaVo> clientiService;
	private final AccountService accountService;
	private final ProdottiService prodottiService;
	private final MessaggiService messaggiService;
	private final FornitoriService fornitoriService;
	private final NoteService noteService;
	private final CardService cardService;
	private final InventarioService inventarioService;
	private final BolleService bolleService;
	private final MenuService menuService;
	private final ProfessioniService professioniService;
	private final IstruzioneService istruzioneService;
	private final PromemoriaService promemoriaService;
	private final EdicoleService edicoleService;
	private final VenditeService venditeService;
	private final PubblicazioniService pubblicazioniService;
	private final IGerivUtils iGerivUtils;
	private final MailingListService mailingListService;
	private final AgenzieService agenzieService;

	private final PasswordEncoder passwordEncoder;
	private final ReflectionSaltSource saltSource;
	private final String pathEstrattiContoClienti;
	private final String imgMiniatureEdicolaPathDir;
	private final String numGiorniPromozioneiGerivStarter;
	private final String valoreMarcaBollo;
	private final String imgMiniaturePathDir;
	private final Integer venditeIconWidth;
	private final Integer venditeIconHeight;
	
	private Map<String,String> mapPubblicazionePiuVenduta;
	private List<Map<String,String>> resultList;
	private List<Map<String,String>> resultListProfiliDL;
	private String resultMessage;
	
	
	private Map<String,String> resultMap;
	private Integer cpu;
	private Integer giroTipo;
	private Integer zoneTipo;
	private Long categoria;
	private Long sottocategoria;
	private String idProdottoFornitore;
	private String barcode;
	private String dataTipoBolla;
	private String dateTipiBollaJson;
	private String estrattoContoJson;
	private String name;
	private Integer length;
	private String codFornitore;
	private String email;
	private String url;
	private String nome;
	private String dtNasc;
	private String sesso;
	private String lavoro;
	private String scuola;
	private String confirmResult;
	private String confirmResultCD;
	private String notaRivendita;
	private String idtn;
	private String qta;
	private String pk;
	private Long codCliente;
	private boolean byPassClienteCheck;
	private String notaFissa;
	private String aliquota;
	private String dataAStr;
	private String codClienti;
	private Integer tipoLocalita;
	private String indirizzo;
	private String numero;
	private String cap;
	private String localita;
	private Integer provincia;
	private String telefono;
	private String cellulare;
	private Integer tipoEdicola;
	private String ragioneSociale;
	private Long codPromemoria;
	
	private Integer select_coddl;
	
	private Integer codEdicolaWeb;
	private String dataInserimento;
	private String dataSospensione;
	
	private Integer codFiegDl;
	private String popup_dett_data_da;
	private String popup_dett_data_a;
	private String popup_dett_select_profili;
	private Boolean popup_dett_flag_test;
	private Boolean dett_flag_promo;
	private Boolean dett_flag_plus;
	
	

	
	
	
	public JSONAction() {
		this.clientiService = null;
		this.accountService = null;
		this.prodottiService = null;
		this.messaggiService = null;
		this.fornitoriService = null;
		this.noteService = null;
		this.cardService = null;
		this.inventarioService = null;
		this.bolleService = null;
		this.menuService = null;
		this.professioniService = null;
		this.istruzioneService = null;
		this.promemoriaService = null;
		this.edicoleService = null;
		this.venditeService = null;
		this.pubblicazioniService = null;
		this.iGerivUtils = null;
		this.mailingListService = null;
		this.passwordEncoder = null;
		this.saltSource = null;
		this.pathEstrattiContoClienti = null;
		this.imgMiniatureEdicolaPathDir = null;
		this.numGiorniPromozioneiGerivStarter = null;
		this.valoreMarcaBollo = null;
		this.imgMiniaturePathDir = null;
		this.venditeIconWidth = null;
		this.venditeIconHeight = null;
		this.agenzieService = null;
	}
	
	@Autowired
	public JSONAction(
		ClientiService<ClienteEdicolaVo> clientiService,
		AccountService accountService,
		ProdottiService prodottiService,
		MessaggiService messaggiService,
		FornitoriService fornitoriService,
		NoteService noteService,
		CardService cardService,
		InventarioService inventarioService,
		BolleService bolleService,
		MenuService menuService,
		ProfessioniService professioniService,
		IstruzioneService istruzioneService,
		PromemoriaService promemoriaService,
		EdicoleService edicoleService,
		VenditeService venditeService,
		PubblicazioniService pubblicazioniService,
		IGerivUtils iGerivUtils,
		MailingListService mailingListService,
		PasswordEncoder passwordEncoder,
		ReflectionSaltSource saltSource,
		@Value("${path.estratti.conto.clienti}") String pathEstrattiContoClienti,
		@Value("${img.miniature.edicola.path.dir}") String imgMiniatureEdicolaPathDir,
		@Value("${igeriv.numero.giorni.prova.per.igeriv.starter}") String numGiorniPromozioneiGerivStarter,
		@Value("${igeriv.valore.euro.marca.da.bollo.estratto.conto}") String valoreMarcaBollo,
		@Value("${img.miniature.path.dir}") String imgMiniaturePathDir,
		@Value("${vendite.icon.width}") Integer venditeIconWidth,
		@Value("${vendite.icon.height}") Integer venditeIconHeight,
		AgenzieService agenzieService) {
		this.clientiService = clientiService;
		this.accountService = accountService;
		this.prodottiService = prodottiService;
		this.messaggiService = messaggiService;
		this.fornitoriService = fornitoriService;
		this.noteService = noteService;
		this.cardService = cardService;
		this.inventarioService = inventarioService;
		this.bolleService = bolleService;
		this.menuService = menuService;
		this.professioniService = professioniService;
		this.istruzioneService = istruzioneService;
		this.promemoriaService = promemoriaService;
		this.edicoleService = edicoleService;
		this.venditeService = venditeService;
		this.pubblicazioniService = pubblicazioniService;
		this.iGerivUtils = iGerivUtils;
		this.mailingListService = mailingListService;
		this.passwordEncoder = passwordEncoder;
		this.saltSource = saltSource;
		this.pathEstrattiContoClienti = pathEstrattiContoClienti;
		this.imgMiniatureEdicolaPathDir = imgMiniatureEdicolaPathDir;
		this.numGiorniPromozioneiGerivStarter = numGiorniPromozioneiGerivStarter;
		this.valoreMarcaBollo = valoreMarcaBollo;
		this.imgMiniaturePathDir = imgMiniaturePathDir;
		this.venditeIconWidth = venditeIconWidth;
		this.venditeIconHeight = venditeIconHeight;
		this.agenzieService = agenzieService;
	}
	
	@Override
	public String input() {
		return INPUT;
	}

	@Override
	public void validate() {
	}

	@SkipValidation
	public String showFilter() {
		return SUCCESS;
	}
	
	/**
	 * Ritorna la JSON string per riempire la combo "tipo di località" 
	 * 
	 * @return String
	 */
	public String tipoLocalita() { 
		resultMap = new LinkedHashMap<String, String>();
		List<TipoLocalitaVo> tipiLocalita = (List<TipoLocalitaVo>) context.getAttribute("tipiLocalita");
		for (TipoLocalitaVo tp : tipiLocalita) {
			resultMap.put(tp.getTipoLocalita().toString(), tp.getDescrizione());
		}
		return "successResultMap";	
	}
	
	/**
	 * Ritorna la JSON string per riempire la combo "provincia" 
	 * 
	 * @return String
	 */
	public String province() { 
		resultMap = new LinkedHashMap<String, String>();
		List<ProvinciaVo> province = (List<ProvinciaVo>) context.getAttribute("province");
		resultMap.put("-1", getText("dpe.provincia"));
		for (ProvinciaVo tp : province) {
			resultMap.put(tp.getCodProvincia().toString(), tp.getDescrizione());
		}
		return "successResultMap";	
	}
	
	/**
	 * Ritorna la JSON string per riempire la combo "Tipi Edicola" 
	 * 
	 * @return String
	 */
	public String tipiEdicola() { 
		resultMap = new LinkedHashMap<String, String>();
		List<KeyValueDto> tipoEdicola = (List<KeyValueDto>) context.getAttribute("tipiEdicola");
		resultMap.put("-1", getText("dpe.tipo.edicola"));
		for (KeyValueDto tp : tipoEdicola) {
			resultMap.put(tp.getKeyInt().toString(), tp.getValue());
		}
		return "successResultMap";	
	}
	
	private String buildTitolo(String titolo) {
		return titolo.toUpperCase().replaceAll("\\b\\s{2,}\\b", " ").replaceAll("[^a-zA-Z0-9 ']", "").trim();
	}
	
	private String buildTitoloFileName(String titolo) {
		return titolo.toUpperCase().replaceAll("\\b\\s{2,}\\b", " ").replaceAll("[^a-zA-Z0-9 ]", "").trim();
	}
	
	
	public String getProfiliDL(){
		resultListProfiliDL = new ArrayList<Map<String,String>>();
		try {
			if(select_coddl != null ){
				
				
				List<DlGruppoModuliVo> listResultGruppoModuli = menuService.getListDlGruppoModuliVo(select_coddl, false);
				if(!listResultGruppoModuli.isEmpty()){
					
					//Valore null
					Map<String,String> m_null = new HashMap<String, String>();
					m_null.put("key", "");
					m_null.put("value", "");
					resultListProfiliDL.add(m_null);
					
					for (DlGruppoModuliVo dlGruppoModuliVo : listResultGruppoModuli) {
						Map<String,String> m = new HashMap<String, String>();
						m.put("key", dlGruppoModuliVo.getGruppoModuli().getId().toString());
						m.put("value", dlGruppoModuliVo.getGruppoModuli().getDescrizione());
						resultListProfiliDL.add(m);	
					}
				}
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "successResultListProfiliDL";
	}
	
	public String memorizzaDataInserimentoDataSospensione(){
		
		Timestamp timestamp_DtInserimento = null;
		Timestamp timestamp_DtSospensione = null;
		
		try{
		    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		    
		    Date parsedDate_Inserimento = dateFormat.parse(dataInserimento);
		    timestamp_DtInserimento = new java.sql.Timestamp(parsedDate_Inserimento.getTime());
		
		    if(dataSospensione!=null && !dataSospensione.equals("")){
		    	Date parsedDate_Sospensione = dateFormat.parse(dataSospensione);
		    	timestamp_DtSospensione = new java.sql.Timestamp(parsedDate_Sospensione.getTime());
			}
		    accountService.saveDtInserimentoDtSospensioneEdicola(codFiegDl, codEdicolaWeb, timestamp_DtInserimento, timestamp_DtSospensione);
		    		    
		}catch(Exception e){
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		
		return "successResultMessage";
	}
	
	public String memorizzaPianoProfilazione(){
		
		
		Timestamp timestamp_dett_data_da = null;
		Timestamp timestamp_dett_data_a = null;
		
		try{
		    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		    Date parsedDate_dett_data_da = dateFormat.parse(popup_dett_data_da);
		    timestamp_dett_data_da = new java.sql.Timestamp(parsedDate_dett_data_da.getTime());
		    if(popup_dett_data_a!=null && !popup_dett_data_a.equals("")){
		    	Date parsedDate_dett_data_a = dateFormat.parse(popup_dett_data_a);
		    	timestamp_dett_data_a = new java.sql.Timestamp(parsedDate_dett_data_a.getTime());
			}
		    PianoProfiliEdicolaVo profiloEdicola = new PianoProfiliEdicolaVo();
		    profiloEdicola.setCodDpeWebEdicola(codEdicolaWeb);
		    
		    //AnagraficaAgenziaVo agenziaVo = agenzieService.getAgenziaByCodice(getAuthUser().getCodFiegDl());
		    //profiloEdicola.setAnagraficaAgenziaVo(agenziaVo);
		    profiloEdicola.setCodFiegDl(getAuthUser().getCodFiegDl());
		    
		    //GruppoModuliVo gruppoModuliVo = menuService.getGruppoModuli(new Integer(popup_dett_select_profili));
		    //profiloEdicola.setCodiceGruppoVo(gruppoModuliVo);
		    profiloEdicola.setCodiceGruppo(new Integer(popup_dett_select_profili));
		    
		    Timestamp timestamp_dett_data_da_2 = DateUtilities.parseDate(popup_dett_data_da, DateUtilities.FORMATO_DATA_SLASH);
		    profiloEdicola.setDtAttivazioneProfiloEdicola(timestamp_dett_data_da_2);
		    //profiloEdicola.setDtSospensioneProfiloEdicola(timestamp_dett_data_a);
		    //profiloEdicola.setEdicolaTest((popup_dett_flag_test)? new Integer(1):new Integer(0));
		    //profiloEdicola.setEdicolaPromo((dett_flag_promo)? new Integer(1):new Integer(0));
		    //profiloEdicola.setEdicolaPlus((dett_flag_plus)? new Integer(1):new Integer(0));
		    profiloEdicola.setFgimmagine(new Integer(1));
		    
		    accountService.saveProfilazioneEdicola(profiloEdicola);
		    
		    
		}catch(Exception e){
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		
		return "successResultMessage";
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
		return super.getTitle() + getText("igeriv.visualizza.edicole");
	}
	
}
