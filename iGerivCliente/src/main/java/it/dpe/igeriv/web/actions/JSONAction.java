package it.dpe.igeriv.web.actions;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.selectUnique;
import static ch.lambdaj.Lambda.sum;
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
import it.dpe.igeriv.dto.EstrattoContoClienti;
import it.dpe.igeriv.dto.EstrattoContoClientiPubblicazioniDto;
import it.dpe.igeriv.dto.EstrattoContoClientiXmlDto;
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
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.EncryptUtils;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivUtils;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.util.XmlUtils;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.AnagraficaEdicolaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.BollaResaFuoriVoceVo;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.IstruzioneVo;
import it.dpe.igeriv.vo.ModuloInputVo;
import it.dpe.igeriv.vo.NoteRivenditaPerCpuVo;
import it.dpe.igeriv.vo.NoteRivenditaVo;
import it.dpe.igeriv.vo.PagamentoClientiEdicolaVo;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.util.ServletContextAware;
import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.state.State;
import org.extremecomponents.util.ExtremeUtils;
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
	private final AgenzieService agenzieService;
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
	private Map<String,String> resultMap;
	private List<Map<String,String>> resultListInfoCopertina;
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
	

	//Scolastica
	private Long idNumeroOrdine;
	private String seq;
	
	
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
	
	public String hasPromemoria() {
		resultMap = new LinkedHashMap<String, String>();
		try {
			Boolean hasPromemoria = promemoriaService.hasPromemoria(getAuthUser().getCodEdicolaMaster());
			resultMap.put("hasPromemoria", hasPromemoria.toString());
		} catch (Throwable e) {
			resultMap.put("error", getText("gp.errore.imprevisto"));
		}
		return "successResultMap";	
	}
	
	public String updatePromemoriaLetto() {
		resultMap = new LinkedHashMap<String, String>();
		try {
			if (codPromemoria != null) {
				promemoriaService.updatePromemoriaLetto(codPromemoria);
			}
		} catch (Throwable e) {
			resultMap.put("error", getText("gp.errore.imprevisto"));
		}
		return "successResultMap";	
	}
	
	public String attivaPeriodoProva() {
		resultMap = new LinkedHashMap<String, String>();
		try {
			AbbinamentoEdicolaDlVo ab = edicoleService.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(getAuthUser().getCodFiegDl(), getAuthUser().getCodEdicolaDl());
			Timestamp dtSospensioneEdicola = ab.getDtSospensioneEdicola();
			Timestamp today = new Timestamp(new Date().getTime());
			boolean isEdicolaStarter = sessionMap.get("hasProfiloStarter") != null ? Boolean.parseBoolean(sessionMap.get("hasProfiloStarter").toString()) : false;
			if (!isEdicolaStarter) {
				throw new IGerivBusinessException(getText("igeriv.chiamata.invalida"));
			}
			if (getAuthUser().getRichiestaProva()) {
				throw new IGerivBusinessException(getText("igeriv.periodo.di.prova.gia.richiesto"));
			}
			Timestamp dtSospensioneEdicolaStarter = new Timestamp(DateUtilities.aggiungiGiorni(today, new Integer(numGiorniPromozioneiGerivStarter)).getTime());
			accountService.saveUpgradeAccountToIGerivBaseAdmin(ab, dtSospensioneEdicolaStarter, getAuthUser().getId().toString(), getAuthUser().getCodFiegDlMaster());
		} catch (IGerivBusinessException e) {
			resultMap.put("error", e.getMessage());
		} catch (Throwable e) {
			resultMap.put("error", getText("gp.errore.imprevisto"));
		}
		return "successResultMap";	
	}
	
	
	/**
	 * Esegue i controlli prima di emettere una fattura per il cliente edicola
	 * 
	 * @return String
	 */
	public String validateEmissioneFatturaClientiEdicola() {
		resultMap = new LinkedHashMap<String, String>();
		try {
			String logoFileName = sessionMap.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_LOGOMARCA_EDICOLA) != null ? ((ParametriEdicolaDto) sessionMap.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_LOGOMARCA_EDICOLA)).getParamValue().trim() : "";
			if (!Strings.isNullOrEmpty(logoFileName)) {
				File logo = new File(imgMiniatureEdicolaPathDir + System.getProperty("file.separator") + logoFileName);
				if (!logo.isFile()) {
					resultMap.put("error", getText("igeriv.error.logo.file.not.found"));
				}
			}
		}  catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "successResultMap";	
	}
	
	/**
	 * Verfica se è possibile emettere un estratto conto per una data di competenza
	 * 
	 * @return String
	 */
	public String validateEstrattoContoClientiEdicola() {
		resultMap = new LinkedHashMap<String, String>();
		try {
			String logoFileName = sessionMap.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_LOGOMARCA_EDICOLA) != null ? ((ParametriEdicolaDto) sessionMap.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_LOGOMARCA_EDICOLA)).getParamValue().trim() : "";
			if (!Strings.isNullOrEmpty(logoFileName)) {
				File logo = new File(imgMiniatureEdicolaPathDir + System.getProperty("file.separator") + logoFileName);
				if (!logo.isFile()) {
					resultMap.put("error", getText("igeriv.error.logo.file.not.found"));
				}
			}
			if (!Strings.isNullOrEmpty(codClienti) && !Strings.isNullOrEmpty(dataAStr)) {
				Timestamp dtCompetenza = DateUtilities.parseDate(dataAStr, DateUtilities.FORMATO_DATA_SLASH);
				List<Long> listCodClienti = new ArrayList<Long>();
				for (String codCli : codClienti.split(",")) {
					listCodClienti.add(new Long(codCli));
				}
				List<PagamentoClientiEdicolaVo> list = clientiService.getPagamentoClientiEdicola(listCodClienti, dtCompetenza, IGerivConstants.ESTRATTO_CONTO);
				for (PagamentoClientiEdicolaVo cli : list) {
					resultMap.put("error", MessageFormat.format(getText("igeriv.estratto.conto.gia.esistente"), cli.getCliente().getNomeCognome(), dataAStr));
					break;
				}
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "successResultMap";	
	}
	
	/**
	 * Cancella una lista di ritiri (pubblicazioni o prodotti) dal conto di un cliente
	 * 
	 * @return String
	 */
	public String deleteRitiriCliente() {
		resultMap = new LinkedHashMap<String, String>();
		try {
			if (!Strings.isNullOrEmpty(nome) && !Strings.isNullOrEmpty(pk)) {
				venditeService.deleteVenditaDettaglio(pk);
				resultMap.put("result", MessageFormat.format(getText("igeriv.ritiro.cliente.cancellato"), nome));
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "successResultMap";
	}
	
	/**
	 * Ripristina nel conto del cliente una lista di ritiri (pubblicazioni o prodotti) cancellati precedentemente
	 * 
	 * @return String
	 */
	public String restoreRitiriCliente() {
		resultMap = new LinkedHashMap<String, String>();
		try {
			if (!Strings.isNullOrEmpty(pk)) {
				venditeService.restoreVenditaDettaglio(pk);
				resultMap.put("result", getText("igeriv.ritiri.clienti.ripristinati"));
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "successResultMap";
	}
	
	
	/**
	 * Crea un'immagine fittizia, un quadrato bianco con il titolo formattato.
	 * 
	 * @param String titolo
	 * @return String
	 * @throws IOException
	 */
	private String createFakeImage(String titolo) throws IOException {
		BufferedImage img = new BufferedImage(80, 80, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		String tit = buildTitolo(titolo);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, venditeIconWidth, venditeIconHeight);
		Font scaleFontToFit = new Font("sansserif", Font.BOLD, 14);
		g2.setFont(scaleFontToFit);
		g2.setPaint(Color.BLACK);
		drawTitleString(img, tit, g2);
		File imgTmpDir = new File(imgMiniaturePathDir);
		String titFileName = buildTitoloFileName(titolo);
		String imgName = titFileName + "_fake.jpg";
		File outputfile = new File(imgTmpDir.getAbsolutePath() + "/" + imgName);
		ImageIO.write(img, "jpg", outputfile);
		return imgName;
	}
	
	/**
	 * Disegna il titolo nell'immagine bianca regolando font e dimensioni su quadrato 80 x 80.
	 * 
	 * @param BufferedImage img
	 * @param String tit
	 * @param Graphics2D g2
	 */
	private void drawTitleString(BufferedImage img, String tit, Graphics2D g2) {
		FontMetrics fm = img.getGraphics().getFontMetrics();
		int msg_width = fm.stringWidth(tit);
		if (msg_width > venditeIconWidth) {
			List<String> listTit = new ArrayList<String>();
			if (msg_width > venditeIconWidth) {
				listTit = Arrays.asList(tit.replaceAll(" ", ",").split(","));
				StringBuffer sb = new StringBuffer(listTit.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", ""));
				if (listTit.size() > 1) {
					for (int i = 0; i < listTit.size(); i++) {
						String t = listTit.get(i);
						if (fm.stringWidth(t) > venditeIconWidth) {
							String[] split = t.split("(?<=\\G.{8})");
							String str = Arrays.asList(split).toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
							sb.replace(sb.indexOf(t), (sb.indexOf(t) + t.length()), str);
						}
					}
					listTit = Arrays.asList(sb.toString().split(","));
				} else {
					listTit = Arrays.asList(tit.split("(?<=\\G.{8})"));
				}
			}
			int yPos = (venditeIconHeight / listTit.size());
			for (int i = 0; i < listTit.size(); i++) {
				int msg_y = (i + 1) * yPos;
				g2.drawString(listTit.get(i), 0, msg_y);
			}
		} else {
			int ascent = fm.getMaxAscent();
			int descent = fm.getMaxDescent();
			int msg_x = (int) (img.getWidth() / 2.5 - msg_width / 2);
			int msg_y = img.getHeight() / 2 - descent / 2 + ascent / 2;
			g2.drawString(tit, msg_x, msg_y);
		}
	}
	
	/**
	 * @return
	 */
	public String sendValidationEmail() {
		resultList = new ArrayList<Map<String,String>>();
		Map<String,String> m = new HashMap<String, String>();
		try {
			if (email != null) {
				boolean isPwdCriptata = false;
				String roleName = null;
				String password = null;
				boolean isEdicolaStarter = sessionMap.get("hasProfiloStarter") != null ? Boolean.parseBoolean(sessionMap.get("hasProfiloStarter").toString()) : false;
				String subject = getText("msg.email.validation.link.subject");
				boolean existsAltroUtenteStessaMail = false;
				if (getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA)) {
					UserVo edicolaByEmail = accountService.getEdicolaByEmail(email);
					existsAltroUtenteStessaMail = edicolaByEmail != null;
					if (existsAltroUtenteStessaMail && !Arrays.asList(getAuthUser().getArrId()).contains(edicolaByEmail.getAbbinamentoEdicolaDlVo().getCodDpeWebEdicola())) {
						throw new IGerivBusinessException(getText("error.utente.stessa.mail"));
					}
				} else if (getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA)) {
					ClienteEdicolaVo cienteEdicolaByEmail = clientiService.getCienteEdicolaByEmail(email);
					if (cienteEdicolaByEmail != null && !cienteEdicolaByEmail.getCodCliente().toString().equals(getAuthUser().getCodUtente())) {
						throw new IGerivBusinessException(getText("error.utente.stessa.mail"));
					}
				}
				if (getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA) && !isEdicolaStarter) {
					List<BaseVo> list = new ArrayList<BaseVo>();
					AbbinamentoEdicolaDlVo ab = edicoleService.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(getAuthUser().getCodFiegDl(), getAuthUser().getCodEdicolaDl());
					ab.setPrivacySottoscritta(true);
					ab.setDtUltimoAggiornamento(new Timestamp(new Date().getTime()));
					ab.setIndirizzoIpRichiesta(sessionMap.get(IGerivConstants.SESSION_VAR_REQUEST_IP_ADDRESS).toString());
					AnagraficaEdicolaVo anagraficaEdicola = edicoleService.getAnagraficaEdicola(getAuthUser().getId());
					anagraficaEdicola.setEmail(email);
					UserVo utenteEdicola = accountService.getUtenteEdicola(getAuthUser().getId().toString());
					password = utenteEdicola.getPwd();
					isPwdCriptata = utenteEdicola.getPwdCriptata() != null && utenteEdicola.equals("1");
					roleName = utenteEdicola.getDlGruppoModuliVo().getGruppoModuli().getRoleName();
					list.add(ab);
					list.add(anagraficaEdicola);
					if (!existsAltroUtenteStessaMail) {
						utenteEdicola.setEmail(email);
						list.add(utenteEdicola);
					}
					edicoleService.saveVoList(list);
					getAuthUser().setEmail(email);
				} else if (getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA) && isEdicolaStarter) {
					List<BaseVo> list = new ArrayList<BaseVo>();
					AbbinamentoEdicolaDlVo ab = edicoleService.getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(getAuthUser().getCodFiegDl(), getAuthUser().getCodEdicolaDl());
					ab.setPrivacySottoscritta(true);
					ab.setDtUltimoAggiornamento(new Timestamp(new Date().getTime()));
					ab.setIndirizzoIpRichiesta(sessionMap.get(IGerivConstants.SESSION_VAR_REQUEST_IP_ADDRESS).toString());
					AnagraficaEdicolaVo anagraficaEdicola = edicoleService.getAnagraficaEdicola(getAuthUser().getId());
					anagraficaEdicola.setRagioneSocialeEdicolaPrimaRiga(ragioneSociale);
					anagraficaEdicola.setEmail(email);
					anagraficaEdicola.setTipoLocalita(tipoLocalita);
					anagraficaEdicola.setIndirizzoEdicolaPrimaRiga(indirizzo);
					anagraficaEdicola.setNumeroCivico(numero);
					anagraficaEdicola.setCap(cap);
					anagraficaEdicola.setLocalitaEdicolaPrimaRiga(localita);
					anagraficaEdicola.setProvincia(provincia);
					anagraficaEdicola.setTelefono(telefono);
					anagraficaEdicola.setCellulare(cellulare);
					anagraficaEdicola.setTipoEdicola(tipoEdicola);
					UserVo utenteEdicola = accountService.getUtenteEdicola(getAuthUser().getId().toString());
					password = utenteEdicola.getPwd();
					isPwdCriptata = utenteEdicola.getPwdCriptata() != null && utenteEdicola.equals("1");
					roleName = utenteEdicola.getDlGruppoModuliVo().getGruppoModuli().getRoleName();	
					utenteEdicola.setEmail(email);
					list.add(ab);
					list.add(anagraficaEdicola);
					list.add(utenteEdicola);
					edicoleService.saveVoList(list);
					getAuthUser().setEmail(email);
					subject = getText("msg.email.validation.link.subject.starter");
				} else if (getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA)) {
					ClienteEdicolaVo cliente = clientiService.getCienteEdicolaByCodiceLogin(new Long(getAuthUser().getId()));
					cliente.setEmail(email);
					getAuthUser().setEmail(email);
					ProfessioneVo professione = professioniService.getProfessione(new Integer(lavoro));
					IstruzioneVo istruzione = istruzioneService.getIstruzione(new Integer(scuola));
					cliente.setProfessione(professione);
					cliente.setIstruzione(istruzione);
					cliente.setSesso(sesso);
					cliente.setDataNascita(DateUtilities.parseDate(dtNasc, DateUtilities.FORMATO_DATA));
					cliente.setPrivacySottoscritta(true);
					cliente.setIndirizzoIpRichiesta(sessionMap.get(IGerivConstants.SESSION_VAR_REQUEST_IP_ADDRESS).toString());
					clientiService.saveBaseVo(cliente);
					password = cliente.getPassword();
					isPwdCriptata = cliente.getPwdCriptata() != null && cliente.getPwdCriptata().equals("1");
					roleName = cliente.getGruppoModuliVo().getRoleName();		
				}
				String params = getAuthUser().getId() + "|" + getAuthUser().getCodFiegDl() + "|" + getAuthUser().getCodEdicolaDl().toString() + "|" + getAuthUser().getTipoUtente().toString();
				String hash = EncryptUtils.encrypt(EncryptUtils.getEncrypter(IGerivConstants.ENCRYPTION_TOKEN), params).replace("\r\n", "");
				String link = "http://" + request.getServerName() + ":" + request.getServerPort() + url + "?hash=" + URLEncoder.encode(hash, "UTF-8") + "&hash1=" + URLEncoder.encode(buildPassword(password, isPwdCriptata, roleName, getAuthUser().getTipoUtente()), "UTF-8");
				String message = MessageFormat.format(getText("msg.email.validation.link.body"), link);
				if (getAuthUser().getTipoUtente().equals(IGerivConstants.TIPO_UTENTE_EDICOLA) && isEdicolaStarter) {
					message = MessageFormat.format(getText("msg.email.validation.link.body.starter"), link);
				}
				StoricoEmailVo sto = buildStoricoEmail(message);
				clientiService.saveBaseVo(sto);
				mailingListService.sendEmailWithAttachment(new String[]{email}, subject, message, null, true, null, false, null);
				resultList.add(m);
			}
		} catch (IGerivBusinessException e) {
			m.put("error", e.getMessage());
			resultList.add(m);
		} catch (Throwable e) {
			m.put("error", getText("gp.errore.imprevisto"));
			resultList.add(m);
		}
		return "successResultList";
	}

	/**
	 * @param message
	 * @return
	 */
	private StoricoEmailVo buildStoricoEmail(String message) {
		StoricoEmailVo sto = new StoricoEmailVo();
		sto.setCodEmail(edicoleService.getNextSeqVal(IGerivConstants.SEQ_STORICO_EMAIL));
		sto.setCodEdicola(getAuthUser().getId());
		sto.setCodUtente(new Integer(getAuthUser().getCodUtente()));
		sto.setEmail(email);
		sto.setTipoUtente(getAuthUser().getTipoUtente());
		sto.setTesto(message);
		sto.setDataInvio(new Timestamp(new Date().getTime()));
		return sto;
	}

	/**
	 * Costruisce la password
	 * @param pwd 
	 * @param pwdCriptata 
	 * @param roleName 
	 * 
	 * @return
	 */
	private String buildPassword(String pwd, boolean pwdCriptata, String roleName, Integer tipoUtente) {
		if (!pwdCriptata) {
			List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
			authList.add(new GrantedAuthorityImpl(roleName));
			UserAbbonato ua = new UserAbbonato(getAuthUser().getCodUtente(), pwd, true, true, true, true, authList);
			if (tipoUtente.equals(IGerivConstants.TIPO_UTENTE_CLIENTE_EDICOLA)) {
				ua.setId(new Integer(getAuthUser().getId()));
				ua.setCodUtente(getAuthUser().getCodUtente());
			} else {
				ua.setCodFiegDl(getAuthUser().getCodFiegDl());
				ua.setCodEdicolaDl(getAuthUser().getCodEdicolaDl());
			}
			ua.setTipoUtente(tipoUtente);
			pwd = passwordEncoder.encodePassword(pwd, saltSource.getSalt(ua));
		}
		return pwd;
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
	
	/**
	 * Ritorna la JSON string per riempire la combo "sesso" 
	 * 
	 * @return String
	 */
	public String sesso() { 
		resultMap = new LinkedHashMap<String, String>();
		resultMap.put("-1", getText("igeriv.sesso"));
		resultMap.put("M", getText("igeriv.uomo"));
		resultMap.put("F", getText("igeriv.donna"));
		return "successResultMap";		
	}
	
	/**
	 * Ritorna la JSON string per riempire la combo "lavoro" 
	 * 
	 * @return String
	 */
	public String lavoro() { 
		List<KeyValueDto> professioni = professioniService.getProfessioni();
		resultMap = new LinkedHashMap<String, String>();
		resultMap.put("-1", getText("igeriv.lavoro"));
		for (KeyValueDto prof : professioni) {
			resultMap.put(prof.getKeyInt().toString(), prof.getValue());
		}
		return "successResultMap";		
	}
	
	/**
	 * Ritorna la JSON string per riempire la combo "scuola" 
	 * 
	 * @return String
	 */
	public String scuola() { 
		List<KeyValueDto> scuole = istruzioneService.getIstruzione();
		resultMap = new LinkedHashMap<String, String>();
		resultMap.put("-1", getText("igeriv.scuola"));
		for (KeyValueDto prof : scuole) {
			resultMap.put(prof.getKeyInt().toString(), prof.getValue());
		}
		return "successResultMap";		
	}
	
	/**
	 * Ritorna i giri di un giro tipo
	 * 
	 * @return String
	 */
	public String giri() { 
		resultList = new ArrayList<Map<String,String>>();
		List<GiroDto> giri = messaggiService.getGiri(getAuthUser().getId(), giroTipo);
		for (GiroDto vo : giri) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getCodGiro());
			m.put("label", "" + vo.getCodGiro());
			resultList.add(m);
		}
		return "successResultList";		
	}
	
	/**
	 * Ritorna le zone di una zona tipo
	 * 
	 * @return String
	 */
	public String zone() {
		resultList = new ArrayList<Map<String,String>>();
		List<GiroDto> giri = messaggiService.getZone(getAuthUser().getId(), giroTipo);
		for (GiroDto vo : giri) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getCodGiro());
			m.put("label", "" + vo.getCodGiro());
			resultList.add(m);
		}
		return "successResultList";		
	}
	
	/**
	 * Ritorna le sottocategorie di una categoria
	 * 
	 * @return String
	 */
	public String sottocategorieProdottiDl() {
		resultList = new ArrayList<Map<String,String>>();
		List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> scat = prodottiService.getSottocategorieProdottiDl(getAuthUser().getCodFiegDl(), getAuthUser().getId(), categoria);
		for (ProdottiNonEditorialiSottoCategoriaEdicolaVo vo : scat) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getPk().getCodSottoCategoria());
			m.put("label", vo.getDescrizione());
			resultList.add(m);
		}
		return "successResultList";		
	}
	
	/**
	 * Ritorna i tipi di pagamento
	 * 
	 * @return String
	 */
	public String getTipiPagamento() {
		resultList = new ArrayList<Map<String,String>>();
		List<KeyValueDto> list = (List<KeyValueDto>) sessionMap.get("listTipiPagamento");
		for (KeyValueDto dto : list) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", dto.getKeyInt().toString());
			m.put("label", dto.getValue());
			resultList.add(m);
		}
		return "successResultList";		
	}
	
	/**
	 * Ritorna le sottocategorie di una categoria
	 * 
	 * @return String
	 */
	public String sottocategorieProdotti() {
		resultList = new ArrayList<Map<String,String>>();
		List<SottoCategoriaDto> scat = prodottiService.getSottocategoriePne(categoria, getAuthUser().getCodEdicolaMaster());
		for (SottoCategoriaDto vo : scat) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getCodSottoCategoria());
			m.put("label", vo.getDescrizione());
			resultList.add(m);
		}
		return "successResultList";		
	}
	
	/**
	 * Ritorna i profili del menu
	 * 
	 * @return String
	 */
	public String getProfiliMenu() {
		resultList = new ArrayList<Map<String,String>>();
		List<ProfiloDto> scat = menuService.getProfili();
		for (ProfiloDto vo : scat) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("id", "" + vo.getId());
			m.put("label", vo.getTitolo());
			resultList.add(m);
		}
		return "successResultList";	
	}
	
	/**
	 * Ritorna le categorie prodotti non editoriali
	 * 
	 * @return String
	 */
	public String getCategoriePne() {
		resultList = new ArrayList<Map<String,String>>();
		List<CategoriaDto> scat = prodottiService.getCategoriePne(getAuthUser().getCodEdicolaMaster());
		for (CategoriaDto vo : scat) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("codCategoria", "" + vo.getCodCategoria());
			m.put("descrizione", vo.getDescrizione());
			resultList.add(m);
		}
		return "successResultList";	
	}
	
	/**
	 * Ritorna le sottocategorie prodotti non editoriali
	 * 
	 * @return String
	 */
	public String getSottocategoriePne() {
		resultList = new ArrayList<Map<String,String>>();
		List<SottoCategoriaDto> scat = prodottiService.getSottocategoriePne(categoria, getAuthUser().getCodEdicolaMaster());
		for (SottoCategoriaDto vo : scat) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("codCategoria", "" + vo.getCodCategoria());
			m.put("codSottoCategoria", "" + vo.getCodSottoCategoria());
			m.put("descrizione", vo.getDescrizione());
			resultList.add(m);
		}
		return "successResultList";	
	}
	
	/**
	 * Ritorna le causali iva di un'aliquota per i prodotti non editoriali
	 * 
	 * @return String
	 */
	public String getCausaliIva() {
		resultList = new ArrayList<Map<String,String>>();
		if (!Strings.isNullOrEmpty(aliquota)) {
			List<ProdottiNonEditorialiCausaliContabilitaVo> scat = prodottiService.getCausaliIva(new Integer(aliquota));
			for (ProdottiNonEditorialiCausaliContabilitaVo vo : scat) {
				Map<String,String> m = new HashMap<String, String>();	
				m.put("pk", vo.getPk().toString());
				m.put("descrizione", vo.getDescrizione());
				resultList.add(m);
			}
		}
		return "successResultList";	
	}
	
	/**
	 * Produce il report pdf dell'estratto conto del cliente edicola andando 
	 * a prendere i dati dal file .xml
	 * 
	 * @return String
	 */
	public String getEstrattoContoCliente() {
		try {
			resultList = new ArrayList<Map<String,String>>();
			if (!Strings.isNullOrEmpty(dataAStr) && codCliente != null) {
				String[] split = dataAStr.split("\\|");
				Timestamp dataA = DateUtilities.parseDate(split[2], DateUtilities.FORMATO_DATA_SLASH);
				Integer[] arrCodEdicola = (getAuthUser().isMultiDl() && getAuthUser().isDlInforiv()) ? getAuthUser().getArrId() : new Integer[]{getAuthUser().getId()};
				File xmlFileDir = new File(pathEstrattiContoClienti + System.getProperty("file.separator") + "xml" + System.getProperty("file.separator") + getAuthUser().getCodFiegDlMaster() + System.getProperty("file.separator") + getAuthUser().getCodEdicolaMaster());
				File file = new File(xmlFileDir, MessageFormat.format(IGerivMessageBundle.get("igeriv.estratto_conto_cliente.file.name"), new Object[]{codCliente.toString(), DateUtilities.getTimestampAsString(dataA, DateUtilities.FORMATO_DATA)}) + ".xml");
				EstrattoContoClientiXmlDto unmarshall = (EstrattoContoClientiXmlDto) XmlUtils.unmarshall(EstrattoContoClientiXmlDto.class, file);
				List<EstrattoContoClienti> listEstrattoContoDettaglio = new ArrayList<EstrattoContoClienti>();
				if (unmarshall.getListConsegnePubblicazioni() != null) {
					listEstrattoContoDettaglio.addAll(unmarshall.getListConsegnePubblicazioni());
				}
				if (unmarshall.getListConsegneProdotti() != null) { 
					listEstrattoContoDettaglio.addAll(unmarshall.getListConsegneProdotti());
				} 
				ParametriEdicolaDto paramMarcaBollo = sessionMap.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_VALORE_ESTRATTO_CONTO_MARCA_BOLLO) != null ? ((ParametriEdicolaDto) sessionMap.get(IGerivConstants.SESSION_VAR_PARAMETRO_EDICOLA + IGerivConstants.COD_PARAMETRO_VALORE_ESTRATTO_CONTO_MARCA_BOLLO)) : null;
				BigDecimal maxEcPerMarcaBollo = new BigDecimal(paramMarcaBollo != null && !Strings.isNullOrEmpty(paramMarcaBollo.getParamValue()) && !new BigDecimal(paramMarcaBollo.getParamValue()).equals(new BigDecimal(0)) ? paramMarcaBollo.getParamValue() : "0");
				BigDecimal totale = sum(listEstrattoContoDettaglio, on(EstrattoContoClienti.class).getImporto()).add(sum(listEstrattoContoDettaglio, on(EstrattoContoClienti.class).getImportoLordoPne()));
				BigDecimal valMarcaBollo = Strings.isNullOrEmpty(valoreMarcaBollo) ? new BigDecimal(0) : new BigDecimal(valoreMarcaBollo.replace(",", "."));
				boolean hasMarcaBollo = totale.compareTo(maxEcPerMarcaBollo) == 1;
				if (maxEcPerMarcaBollo.intValue() > 0 && hasMarcaBollo) {
					EstrattoContoClienti marcaBollo = new EstrattoContoClientiPubblicazioniDto();
					marcaBollo.setTitolo(IGerivMessageBundle.get("igeriv.marca.bollo"));
					marcaBollo.setPrezzo(valMarcaBollo);
					marcaBollo.setCopie(1l);
					listEstrattoContoDettaglio.add(marcaBollo);
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DAY_OF_MONTH, IGerivConstants.MAX_NUM_DAYS_TO_DELETE_CONTO_VENDITE);
				Boolean deleteEnabled = DateUtilities.ceilDay(dataA).compareTo(iGerivUtils.getVenditeDeleteModuleDeployDate()) > 0 && dataA.compareTo(cal.getTime()) > 0;
				for (EstrattoContoClienti vo : listEstrattoContoDettaglio) {
					Map<String,String> m = new HashMap<String, String>();	
					m.put("titolo", vo.getTitolo());
					m.put("prezzo", ExtremeUtils.formatNumber("###.##0,00", vo.getPrezzo()));
					m.put("quantita", vo.getCopie().toString());
					m.put("importo", ExtremeUtils.formatNumber("###.##0,00", vo.getImporto()));
					m.put("deleteEnabled", deleteEnabled.toString());
					resultList.add(m);
				}
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "successResultList";			
	}
	
	/**
	 * Ritorna i prodotti non editoriali
	 * 
	 * @return String
	 */
	public String getPne() {
		resultList = new ArrayList<Map<String,String>>();
		List<ProdottoDto> scat = prodottiService.getProdottiNonEditoriali(categoria, sottocategoria);
		for (ProdottoDto vo : scat) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("barcode", vo.getBarcode());
			m.put("descrizione", vo.getDescrizione());
			m.put("descrizioneB", vo.getDescrizioneB());
			m.put("nomeImmagine", vo.getNomeImmagine());
			m.put("unitaMisura", vo.getUnitaMisura());
			m.put("aliquota", "" + vo.getAliquota());
			m.put("codProdottoEsterno", "" + vo.getCodProdottoEsterno());
			m.put("codProdottoInterno", "" + vo.getCodProdottoInterno());
			m.put("unitaMinimaIncremento", "" + vo.getUnitaMinimaIncremento());
			resultList.add(m);
		}
		return "successResultList";	
	}
	
	/**
	 * Ritorna un prodotto non editoriale per barcode
	 * 
	 * @return String
	 */
	public String getPneBarcodeOrDescrizione() {
		resultList = new ArrayList<Map<String,String>>();
		Long lBarcode = it.dpe.igeriv.util.NumberUtils.getAsLong(barcode);
		List<ProdottoDto> prodotti = null;
		if (lBarcode == null && barcode != null && !barcode.equals("")) {
			prodotti = prodottiService.getProdottiNonEditorialiEdicolaByDescrizione(barcode, getAuthUser().getCodEdicolaMaster(), null, null);
		} else if (lBarcode != null) {
			prodotti = prodottiService.getProdottoNonEditorialeByBarcode(getAuthUser().getCodEdicolaMaster(), barcode, null, null);
		}
		if (prodotti != null && !prodotti.isEmpty()) {
			for (ProdottoDto prodotto : prodotti) {
				Map<String,String> m = new HashMap<String, String>();
				m.put("codProdotto", prodotto.getCodProdottoInterno().toString());
				m.put("barcode", prodotto.getBarcode() == null ? "" : prodotto.getBarcode());
				m.put("descrizione", prodotto.getDescrizione());
				m.put("iva", prodotto.getAliquota() != null ? prodotto.getAliquota().toString() : "");
				m.put("codiceProdottoFornitore", prodotto.getCodiceProdottoFornitore() != null ? prodotto.getCodiceProdottoFornitore() : "");
				m.put("ultimoPrezzoAcquisto", prodotto.getUltimoPrezzoAcquisto() != null ? prodotto.getUltimoPrezzoAcquisto().toString() : "");
				m.put("prodottoDl", (prodotto.getProdottoDl() == null) ? "false" : prodotto.getProdottoDl().toString());
				resultList.add(m);
			}
		}
		return "successResultList";	
	}
	
	/**
	 * Ritorna un prodotto non editoriale per pk
	 * 
	 * @return String
	 */
	public String getProdottoById() {
		resultList = new ArrayList<Map<String,String>>();
		Long idProdotto = it.dpe.igeriv.util.NumberUtils.getAsLong(idProdottoFornitore);
		if (idProdotto != null) {
			ProdottiNonEditorialiVo prodotto = prodottiService.getProdottoNonEditorialeVo(idProdotto);
			Map<String,String> m = new HashMap<String, String>();
			m.put("codProdotto", prodotto.getCodProdottoInterno().toString());
			m.put("barcode", prodotto.getBarcode() == null ? "" : prodotto.getBarcode());
			m.put("descrizione", prodotto.getDescrizioneProdottoA());
			m.put("iva", prodotto.getAliquota() != null ? prodotto.getAliquota().toString() : "");
			resultList.add(m);
		}
		return "successResultList";	
	}
	
	/**
	 * Ritorna i clienti dell'edicola
	 * 
	 * @return String
	 */
	public String getClientiEdicola() {
		resultList = new ArrayList<Map<String,String>>();
		List<ClienteEdicolaDto> clienti = clientiService.getClientiEdicola(getAuthUser().getArrId(), null, null, null, null);
		for (ClienteEdicolaDto dto : clienti) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("codCliente", "" + dto.getCodCliente());
			m.put("nome", dto.getNome());
			resultList.add(m);
		}
		return "successResultList";	
	}
	
	/**
	 * @return
	 */
	public String getFornitoriEdicola() {
		resultList = new ArrayList<Map<String,String>>();
		List<ProdottiNonEditorialiFornitoreVo> fornitoriEdicola = fornitoriService.getFornitoriEdicola(getAuthUser().getCodEdicolaMaster(), null, null, true);
		List<KeyValueDto> list = new ArrayList<KeyValueDto>();
		for (ProdottiNonEditorialiFornitoreVo vo : fornitoriEdicola) {
			Map<String,String> m = new HashMap<String, String>();	
			m.put("codFornitore", "" + vo.getPk().getCodFornitore());
			m.put("nome", vo.getNome());
			resultList.add(m);
		}
		return "successResultList";	
	}
	
	/**
	 * Ritorna le iniziali di una stringa (utilizzato per costruire il codice prodotto non editoriale dell'edicola)
	 * 
	 * @return String
	 */
	public String getNameInitials() {
		resultList = new ArrayList<Map<String,String>>();
		Map<String,String> m = new HashMap<String, String>();
		String codiceProdotto = StringUtility.getStringInitials(name, length).toUpperCase();
		while (prodottiService.codiceProdottoEdicolaExists(codiceProdotto, getAuthUser().getCodEdicolaMaster())) {
			codiceProdotto = StringUtility.getStringInitials(name, length).toUpperCase();
		}
		m.put("codiceProdotto", codiceProdotto);
		resultList.add(m);
		return "successResultList";	
	}
	
	/**
	 * @return
	 */
	public String getDateIdInventario() {
		List<InventarioDto> listInventarioDto = inventarioService.getListInventarioDto(getAuthUser().getCodEdicolaMaster());
		resultList = new ArrayList<Map<String,String>>();
		for (InventarioDto dto : listInventarioDto) {
			Map<String,String> m = new HashMap<String, String>();
			m.put("keyStringForSelectBox", dto.getKeyStringForSelectBox());
			m.put("valueStringForSelectBox", dto.getValueStringForSelectBox());
			resultList.add(m);
		}
		return "successResultList";	
	}
	
	/**
	 * @return
	 */
	public String getDateTipoBolla() {
		List<DateTipiBollaDto> listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		List<BollaRiassuntoVo> bolleRiassunto = bolleService.getBolleRiassunto(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null);
		int count = 0;
		for (BollaRiassuntoVo vo : bolleRiassunto) {
			DateTipiBollaDto dto = new DateTipiBollaDto();
			BollaRiassuntoVo brvo = (BollaRiassuntoVo) vo;
			dto.setIdDataTipoBolla(count);
			dto.setDataBollaFormat(DateUtilities.getTimestampAsString(brvo.getPk().getDtBolla(), DateUtilities.FORMATO_DATA));
			dto.setTipoBolla(IGerivConstants.TIPO + " " + brvo.getPk().getTipoBolla());
			dto.setReadonly((brvo.getBollaTrasmessaDl() != null && brvo.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_TRASMESSO_DL) ? false : true);
			dto.setBollaTrasmessaDl(brvo.getBollaTrasmessaDl());
			listDateTipiBolla.add(dto);
			count++;
		}
		Gson gson = new GsonBuilder().create();
		dateTipiBollaJson = gson.toJson(listDateTipiBolla);
		return "successDateTipiBolla";	
	}
	
	/**
	 * @return
	 */
	public String getDateTipoBollaResa() {
		List<DateTipiBollaDto> listDateTipiBolla = new ArrayList<DateTipiBollaDto>();
		Set<BollaResaRiassuntoVo> bolleRiassunto = bolleService.getBolleResaRiassunto(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null);
		int count = 0;
		for (BollaResaRiassuntoVo vo : bolleRiassunto) {
			DateTipiBollaDto dto = new DateTipiBollaDto();
			BollaResaRiassuntoVo brvo = (BollaResaRiassuntoVo) vo;
			dto.setIdDataTipoBolla(count);
			dto.setDataBollaFormat(DateUtilities.getTimestampAsString(brvo.getPk().getDtBolla(), DateUtilities.FORMATO_DATA));
			dto.setTipoBolla(IGerivConstants.TIPO + " " + brvo.getPk().getTipoBolla());
			dto.setReadonly((brvo.getBollaTrasmessaDl() != null && brvo.getBollaTrasmessaDl().intValue() >= IGerivConstants.INDICATORE_RECORD_TRASMESSO_DL) ? false : true);
			dto.setBollaTrasmessaDl(brvo.getBollaTrasmessaDl());
			dto.setGruppoSconto(brvo.getGruppoSconto());
			listDateTipiBolla.add(dto);
			count++;
		}
		Gson gson = new GsonBuilder().create();
		dateTipiBollaJson = gson.toJson(listDateTipiBolla);
		return "successDateTipiBolla";	
	}
	
	
	/**
	 * @return
	 */
	public String getFornitori() {
		List<ProdottiNonEditorialiFornitoreVo> fornitoriEdicola = fornitoriService.getFornitoriEdicola(getAuthUser().getCodEdicolaMaster(), null, null, true);
		List<KeyValueDto> list = new ArrayList<KeyValueDto>();
		for (ProdottiNonEditorialiFornitoreVo vo : fornitoriEdicola) {
			KeyValueDto dto = new KeyValueDto();
			dto.setKey(vo.getPk().getCodFornitore().toString());
			dto.setValue(vo.getNome());
			list.add(dto);
		}
		Gson gson = new GsonBuilder().create();
		dateTipiBollaJson = gson.toJson(list);
		return "successDateTipiBolla";	
	}
	
	/**
	 * @return
	 */
	public String saveNotaRivendita() {
		if (!Strings.isNullOrEmpty(notaFissa) && Boolean.parseBoolean(notaFissa)) {
			NoteRivenditaPerCpuVo vo = noteService.getNoteRivenditaPerCpuVo(cpu, getAuthUser().getCodEdicolaMaster());
			if (vo == null) {
				vo = new NoteRivenditaPerCpuVo();
				NoteRivenditaPerCpuPk pk = new NoteRivenditaPerCpuPk();
				pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
				pk.setCpu(cpu);
				vo.setPk(pk);
			}
			vo.setNote(notaRivendita);
			noteService.saveBaseVo(vo);
			if (!Strings.isNullOrEmpty(idtn)) {
				Integer idtnInt = new Integer(idtn);
				NoteRivenditaVo vo1 = noteService.getNoteRivenditaVo(idtnInt, getAuthUser().getCodEdicolaMaster());
				if (vo1 != null) {
					noteService.deleteVo(vo1);
				}
			}
		} else {
			Integer idtnInt = new Integer(idtn);
			NoteRivenditaVo vo = noteService.getNoteRivenditaVo(idtnInt, getAuthUser().getCodEdicolaMaster());
			if (vo == null) {
				vo = new NoteRivenditaVo();
				NoteRivenditaPk pk = new NoteRivenditaPk();
				pk.setCodEdicola(getAuthUser().getCodEdicolaMaster());
				pk.setIdtn(idtnInt);
				vo.setPk(pk);
			}
			vo.setNote(notaRivendita);
			noteService.saveBaseVo(vo);
			if (cpu != null) {
				NoteRivenditaPerCpuVo vo1 = noteService.getNoteRivenditaPerCpuVo(cpu, getAuthUser().getCodEdicolaMaster());
				if (vo1 != null) {
					noteService.deleteVo(vo1);
				}
			}
		}
		return "successDateTipiBolla";
	}
	
	/**
	 * @return
	 */
	public String associaIGerivCard() {
		resultList = new ArrayList<Map<String,String>>();
		Map<String,String> m = new HashMap<String, String>();
		if (!Strings.isNullOrEmpty(barcode) && codCliente != null) {
			ModuloInputVo moduloInputIGerivCard = selectUnique((List<ModuloInputVo>) context.getAttribute("moduliInput"), having(on(ModuloInputVo.class).getClasse(), equalTo("IGerivCardInputModuleListener")));
			if (!moduloInputIGerivCard.getPattern().matcher(barcode).matches()) {
				m.put("error", MessageFormat.format(getText("label.pubblication_input_module_listener.IGeriv_Client_Card_Not_Matching"), new Object[]{barcode}));
				resultList.add(m);
				return "successResultList";	
			}
			try {
				cardService.associaIGerivCard(barcode, codCliente, getAuthUser().getCodEdicolaMaster(), byPassClienteCheck);
				// TODO 
				// INVIARE EMAIL DI CONFERMA A UTENTE CON INFORMAZIONI SU SMART CARD EDICOLA
				m.put("result", MessageFormat.format(getText("label.pubblication_input_module_listener.IGeriv_Card_Associated"), new Object[]{barcode}));
			} catch (ConfirmRiassociareSmartCardEdicolaException e) {
				ClienteEdicolaVo cliente = clientiService.getClienteEdicola(getAuthUser().getArrId(), new Long(codCliente));
				m.put("confirm", MessageFormat.format(getText("label.pubblication_input_module_listener.IGeriv_Client_Card_Already_Associated"), new Object[]{cliente.getNome()}));
				resultList.add(m);
				return "successResultList";	
			} catch (SmartCardEdicolaGiaAssociataException e) {
				m.put("error", MessageFormat.format(getText("label.pubblication_input_module_listener.IGeriv_Card_Already_Associated"), new Object[]{barcode}));
			} catch (Exception e) {
				m.put("error", getText("msg.errore.invio.richiesta.html"));
			}
 		}
		resultList.add(m);
		return "successResultList";	
	}

	/**
	 * Metodo chiamato via ajax dalla bolla di resa.
	 * Ricerca la pubblicazione partendo dal barcode, esegue le validazioni e 
	 * ritorna una Map di String che sarà convertita dal framework in una stringa in formato JSON, 
	 * usata poi dal javascript per aggiungere la riga alla bolla di resa nei casi di resa fuori voce o resa dimenticata. 
	 * 
	 * @return String
	 */
	public String getPubblicazioneByBarcode() {
		try {
			if (barcode != null && !barcode.equals("")) {
				StringTokenizer st = new StringTokenizer(dataTipoBolla, "|");
				String strData = st.nextToken();
				String tipoBolla = st.nextToken().replaceFirst(IGerivConstants.TIPO, "").trim();
				Boolean bollaEnabled = Boolean.valueOf(st.nextToken().trim());
				Integer gruppoSconto = Integer.valueOf(st.nextToken().trim());
				Timestamp dtBolla = DateUtilities.parseDate(strData, DateUtilities.FORMATO_DATA);
				Integer cpu = pubblicazioniService.getCpuByBarcode(getAuthUser().getCodFiegDl(), barcode);
				if (cpu != null) {
					AnagraficaAgenziaVo agenziaVo = agenzieService.getAgenziaByCodice(getAuthUser().getCodFiegDl());			
					Boolean allowResaDimenticata = bolleService.allowResaDimenticata(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipoBolla, new Integer(cpu), getAuthUser().getNumMaxCpuResaDimenticata(),agenziaVo.getTipoResaNoRifornimentoDimenticato(), null);
					List<BollaResaFuoriVoceVo> list = bolleService.buildNuoviDettagliFuoriVoce(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipoBolla, cpu, gruppoSconto, false, getAuthUser().getDataStorico(),agenziaVo.getTipoResaNoContoDeposito());
					
					//List<BollaResaFuoriVoceVo> list = bolleService.buildNuoviDettagliFuoriVoce(getAuthUser().getCodFiegDl(), getAuthUser().getId(), dtBolla, tipoBolla, cpu, gruppoSconto, false, getAuthUser().getDataStorico());
					BollaResaFuoriVoceVo vo = selectUnique(list, having(on(BollaResaFuoriVoceVo.class).getBarcodeStr(), equalTo(barcode)));
					resultList = new ArrayList<Map<String,String>>();
					Map<String,String> m = new HashMap<String, String>();
					boolean forzaNonRespingere = vo != null && !Strings.isNullOrEmpty(vo.getMotivoResaRespinta()) && (getAuthUser().getTipoControlloPubblicazioniRespinte() != null && getAuthUser().getTipoControlloPubblicazioniRespinte().equals(2) && Boolean.parseBoolean(confirmResult));
					boolean forzaResaInContoDeposito = Strings.isNullOrEmpty(confirmResultCD) ? false : Boolean.parseBoolean(confirmResultCD);
					String errMsgPubblicazioneNonFornitaDalDlDiCompetenza = null;
					/*if (getAuthUser().idEdicolaDeviettiTodis()) {
						List<PubblicazioneDto> listCopertine = igerivServiceDto.getCopertineByTitoloBarcodeCpu(getAuthUser().getArrCodFiegDl(), getAuthUser().getArrId(), null, null, true, false, new Integer(cpu), null, null, null, null, getAuthUser().isDlInforiv());
						if (listCopertine.isEmpty() || (!listCopertine.isEmpty() && !listCopertine.get(0).getCoddl().equals(getAuthUser().getCodFiegDl()))) {
							errMsgPubblicazioneNonFornitaDalDlDiCompetenza = (listCopertine.isEmpty() ? getText("error.pubblicazione.non.fornita.dal.dl.corrente.1") : MessageFormat.format(getText("error.pubblicazione.non.fornita.dal.dl.corrente"), new Object[]{listCopertine.get(0).getTitolo()}));
						}
					}*/
					if (!allowResaDimenticata) {
						m.put("err", getText("error.numero.massimo.resa.dimeticata"));
					} else if (vo != null && !Strings.isNullOrEmpty(vo.getMotivoResaRespinta()) && (vo.getCodMotivoRespinto() != null && !vo.getCodMotivoRespinto().equals(2)) && (getAuthUser().getTipoControlloPubblicazioniRespinte() != null && getAuthUser().getTipoControlloPubblicazioniRespinte().equals(1) && !forzaNonRespingere)) {
						m.put("err", MessageFormat.format(getText("error.numero.resa.respinto"), vo.getTitolo(), vo.getNumeroPubblicazione(), vo.getMotivoResaRespinta()));
					} else if (vo != null && vo.getNumeroInContoDeposito() && !vo.isPermetteContoDeposito()) {
						m.put("err", MessageFormat.format(getText("pubblicazione.in.conto.deposito"), vo.getTitolo(), vo.getNumeroPubblicazione()));
					} else if (vo != null && !Strings.isNullOrEmpty(vo.getMotivoResaRespinta()) && (vo.getCodMotivoRespinto() != null && !vo.getCodMotivoRespinto().equals(2)) && (getAuthUser().getTipoControlloPubblicazioniRespinte() != null && getAuthUser().getTipoControlloPubblicazioniRespinte().equals(2) && !forzaNonRespingere)) {
						m.put("confirm", MessageFormat.format(getText("confirm.numero.resa.respinto.html"), vo.getTitolo(), vo.getNumeroPubblicazione(), vo.getMotivoResaRespinta()));
					} else if (vo != null && vo.getNumeroInContoDeposito() && !forzaResaInContoDeposito) {
						m.put("confirmCD", MessageFormat.format(getText("confirm.resa.pubblicazione.in.conto.deposito.1"), vo.getTitolo(), vo.getNumeroPubblicazione()));
					} else if (errMsgPubblicazioneNonFornitaDalDlDiCompetenza != null) {
						m.put("err", errMsgPubblicazioneNonFornitaDalDlDiCompetenza);
					} else if (vo != null) {
						try {
							BigDecimal prezzoNetto = vo.getPrezzoNetto();
							BigDecimal prezzoLordo = vo.getPrezzoLordo();
							if (vo.getNumeroInContoDeposito()) {
								prezzoNetto = BigDecimal.ZERO;
								prezzoLordo = BigDecimal.ZERO;
								vo.setPrezzoNetto(prezzoNetto);
								vo.setPrezzoLordo(prezzoLordo);
								vo.setResoInContoDeposito(true);
							}
							vo.setReso(1);
							bolleService.saveBaseVo(vo);
							m.put("pk", vo.getPk().toString());
							m.put("cpu", "" + vo.getCpuDl());
							m.put("titolo", vo.getTitolo());
							m.put("sottotitolo", vo.getSottoTitolo());
							m.put("numeroCopertina", vo.getNumeroPubblicazione());
							m.put("distribuito", vo.getDistribuito().toString());
							m.put("giacenza", vo.getGiacenza().toString());
							m.put("dataUscita", DateUtilities.getTimestampAsStringExport(vo.getDataUscita(), DateUtilities.FORMATO_DATA_SLASH));
							m.put("prezzoLordo", prezzoLordo.toString());
							m.put("prezzoNetto", prezzoNetto.toString());
							m.put("tipoRichiamo", vo.getTipoRichiamo());
							m.put("img", (vo.getNomeImmagine() != null) ? vo.getNomeImmagine() : "");
						} catch (Exception e) {
							m.put("err", getText("msg.errore.invio.richiesta"));
						}
					} else {
						m.put("err", getText("msg.copertina.troppo.vecchia"));
					}
					resultList.add(m);
				}
			}
		} catch (Throwable e) {
			requestMap.put("igerivException", IGerivConstants.START_EXCEPTION_TAG + getText("gp.errore.imprevisto") + IGerivConstants.END_EXCEPTION_TAG);
			throw new IGerivRuntimeException();
		}
		return "successResultList";		
	}
	
	
	
	
	
	
	
	
	private String buildTitolo(String titolo) {
		return titolo.toUpperCase().replaceAll("\\b\\s{2,}\\b", " ").replaceAll("[^a-zA-Z0-9 ']", "").trim();
	}
	
	private String buildTitoloFileName(String titolo) {
		return titolo.toUpperCase().replaceAll("\\b\\s{2,}\\b", " ").replaceAll("[^a-zA-Z0-9 ]", "").trim();
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
