package it.dpe.igeriv.web.actions;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.dto.BollaResaProdottiVariDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.visitor.ActionVisitor;
import lombok.Getter;
import lombok.Setter;

/**
 * Action per l'esecuzione dei reports.
 * I reports compilati (.jasper) devono essere presenti nel direttorio "/WEB-INF/jasper/" 
 * (configurato nel parametro "location" nella action corrente in struts.xml).
 * La List dataSourceList contiene i beans con i dati da essere inseriti nel form.
 * La Map reportParams contiene i parametri del report.
 * La conversione in xml dei dati è gestita dal framework.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("reportProdottiVariAction")
@SuppressWarnings({ "rawtypes", "unchecked"})
public class ReportProdottiVariAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private final ProdottiService prodottiService;
	private final String pathBolleResaProdottiVariEdicole;
	private final String imgMiniatureEdicolaPathDir;
	private List<BollaResaProdottiVariDto> dettagli;
	private String downloadToken;
	private HashMap reportParams = new HashMap();
	private String fileName;
	private String idDocumento;
	private Integer tipoDocumento;
	
	public ReportProdottiVariAction() {
		this.prodottiService = null;
		this.pathBolleResaProdottiVariEdicole = null;
		this.imgMiniatureEdicolaPathDir = null;
	}
	
	@Autowired
	public ReportProdottiVariAction(
			ProdottiService prodottiService,
		@Value("${path.bolle.resa.prodotti.vari.edicole}") String pathBolleResaProdottiVariEdicole,
		@Value("${img.miniature.edicola.path.dir}") String imgMiniatureEdicolaPathDir) {
		this.prodottiService = prodottiService;
		this.pathBolleResaProdottiVariEdicole = pathBolleResaProdottiVariEdicole;
		this.imgMiniatureEdicolaPathDir = imgMiniatureEdicolaPathDir;
	}
	
	/**
	 * @return
	 */
	public String reportBollaResaProdottiVariPdf() {
		this.idDocumento = !Strings.isNullOrEmpty(this.idDocumento) && this.idDocumento.contains(",") ? this.idDocumento.substring(0, this.idDocumento.indexOf(",")).trim() : this.idDocumento; 
		Long idDocumento = !Strings.isNullOrEmpty(this.idDocumento) ? new Long(this.idDocumento) : null;
		dettagli = prodottiService.getDettagliBollaResaProdottiVariDto(idDocumento);
		if (dettagli != null && !dettagli.isEmpty()) {
			BollaResaProdottiVariDto first = dettagli.get(0);
			Timestamp dataDocumento = first.getDataDocumento();
			reportParams.put("tipoDocumento", getText("igeriv.bolla.resa.prodotti.vari").toUpperCase());
			String dataDocumentoStr = DateUtilities.getTimestampAsString(dataDocumento, DateUtilities.FORMATO_DATA_SLASH);
			reportParams.put("dataDocumento", dataDocumentoStr);
			reportParams.put("numeroDocumento", first.getNumeroDocumento());
			reportParams.put("idDocumento", first.getIdDocumento());
			reportParams.put("codFornitore", first.getCodFornitore());
			reportParams.put("title", MessageFormat.format(getText("igeriv.bolla.resa.prodotti.vari.titolo"), first.getNumeroDocumento().toString(), dataDocumentoStr));
			reportParams.put("paginaLabel", getText("label.print.Page"));
			reportParams.put("coddl", getAuthUser().getCodFiegDlMaster());
			reportParams.put("codedicola", getAuthUser().getCodEdicolaMaster());
			reportParams.put("codutente", getAuthUser().getCodUtente());
			reportParams.put("pathBolleResaProdottiVariEdicole", pathBolleResaProdottiVariEdicole);
			reportParams.put("datiEdicola",  ((getAuthUser().getRagioneSocialeEdicola() != null) ? getAuthUser().getRagioneSocialeEdicola().replaceAll("&nbsp;", " ") : "") + "\n" + ((getAuthUser().getIndirizzoEdicolaPrimaRiga() != null) ? getAuthUser().getIndirizzoEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "") + "\n"  + ((getAuthUser().getCapEdicola() != null) ? getAuthUser().getCapEdicola().replaceAll("&nbsp;", " ") : "")  + " - " + ((getAuthUser().getLocalitaEdicolaPrimaRiga() != null) ? getAuthUser().getLocalitaEdicolaPrimaRiga().replaceAll("&nbsp;", " ") : "") + "\n" + (getAuthUser().getPiva() != null ? getText("dpe.partita.iva") : getText("dpe.codice.fiscale.2")) + ": " + ((getAuthUser().getPiva() != null) ? getAuthUser().getPiva().replaceAll("&nbsp;", " ") : getAuthUser().getCodiceFiscale()));
			reportParams.put("datiFornitore",  first.getNomeFornitore().replaceAll("&nbsp;", " ") + "\n" + (first.getIndirizzo() != null ? first.getIndirizzo().replaceAll("&nbsp;", " ") : "") + "\n" + (first.getCap() != null ? first.getCap().replaceAll("&nbsp;", " ") : "") + " - " + (first.getLocalita() != null ? first.getLocalita().replaceAll("&nbsp;", " ") : "") + "\n" + (first.getPiva() != null ? getText("dpe.partita.iva") : getText("dpe.codice.fiscale.2")) + ": " + ((first.getPiva() != null) ? first.getPiva().replaceAll("&nbsp;", " ") : ""));
			reportParams.put("titoloLabel", getText("igeriv.prodotto"));
			reportParams.put("prezzoLabel", getText("label.print.Table.Price"));
			reportParams.put("copieLabel", getText("igeriv.reso"));
			reportParams.put("codProdottoLabel", getText("igeriv.codice.prodotto.fornitore.abbrev"));
			reportParams.put("importoLabel", getText("igeriv.importo"));
			reportParams.put("totaleLabel", getText("column.calc.total"));
			reportParams.put("edicolaLabel", getText("igeriv.provenienza.evasione.edicola"));
			reportParams.put("fornitoreLabel", getText("igeriv.fornitore"));
			fileName = MessageFormat.format(IGerivMessageBundle.get("igeriv.bolla.resa.prodotti.vari.file.name"), new Object[]{dataDocumentoStr, first.getNumeroDocumento().toString(), DateUtilities.getTimestampAsStringExport(new Date(), DateUtilities.FORMATO_DATA_YYYYMMDDHHMMSS)});
			setCookie();
			return IGerivConstants.ACTION_BOLLA_RESA_PRODOTTI_VARI_PDF;
		}
		return null;
	}
	
	private void setCookie() {
		if (!Strings.isNullOrEmpty(downloadToken)) {
			downloadToken = downloadToken.replaceAll(",", "").trim();
			response.addCookie(new Cookie("fileDownloadToken", downloadToken));
		}
	}
	
	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}

}	
