package it.dpe.igeriv.web.extremecomponents;

import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.dto.BollaResaBaseDto;
import it.dpe.igeriv.dto.EmailRivenditaDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.dto.MessaggioDto;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.RichiestaRifornimentoDto;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaDettaglioVo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;

import com.google.common.base.Strings;

/**
 * @author romanom
 *
 */
public class DpeRowInterceptor implements RowInterceptor {
	
	public void modifyRowAttributes(TableModel model, Row row) { 
    	Object currentRowBean = model.getCurrentRowBean();
		String onclick = row.getOnclick();
		if (onclick != null && !onclick.equals(DPEWebContants.BLANK) && onclick.contains("(") && onclick.contains(")")) {
	    	try {
				List<String> paramList = null;
				String jsFunctionName = onclick.substring(0, onclick.indexOf("("));
				String jsParams = onclick.substring(onclick.indexOf("(") + 1, onclick.indexOf(")"));
				paramList = new ArrayList<String>(); 
				StringTokenizer st = new StringTokenizer(jsParams,",");
				if (currentRowBean != null) {
					while (st.hasMoreTokens()) {
						String token = st.nextToken();
						String prop = BeanUtils.getProperty(currentRowBean, token);
						if (prop != null && !prop.equals(DPEWebContants.BLANK)) {
							String property = prop.replaceAll("'", "\\\\'").replaceAll(" ", "&nbsp;");
							paramList.add("'" + property + "'");
						}
					}
					if (!paramList.isEmpty()) {
						String params = paramList.toString().substring(1, (paramList.toString().length() - 1));
						onclick = jsFunctionName + "(" + params + ")";
						row.setOnclick(onclick);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (currentRowBean instanceof BollaDettaglioDto) {
			BollaDettaglioDto bollaDettaglioDto = (BollaDettaglioDto) currentRowBean;
			String style = (row.getStyle() != null && row.getStyle().indexOf(";background-color") != -1) ? row.getStyle().substring(0, row.getStyle().indexOf(";background-color")) : row.getStyle();
			row.setStyle(style);
			String iv = bollaDettaglioDto.getIndicatoreValorizzare() != null ? bollaDettaglioDto.getIndicatoreValorizzare().toString() : "";
			Integer tipoControlloPubblicazioniRespinte = model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_TIPO_CONTROLLO_PUBBLICAZIONI_RESPINTE) != null ? new Integer(model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_TIPO_CONTROLLO_PUBBLICAZIONI_RESPINTE).toString()) : null;
			String resp = (!Strings.isNullOrEmpty(bollaDettaglioDto.getMotivoResaRespinta()) && tipoControlloPubblicazioniRespinte != null && tipoControlloPubblicazioniRespinte > 0) ? "true" : "false";
			row.addAttribute("iv", iv);
			row.addAttribute("pv", bollaDettaglioDto.getIndicatorePrezzoVariato() != null ? bollaDettaglioDto.getIndicatorePrezzoVariato() : "");
			row.addAttribute("idtn", bollaDettaglioDto.getIdtn());
			if (!Strings.isNullOrEmpty(iv)) {
				row.addAttribute("dpa", bollaDettaglioDto.getDataFatturazionePrevista() != null ? DateUtilities.getTimestampAsString(bollaDettaglioDto.getDataFatturazionePrevista(), DateUtilities.FORMATO_DATA_SLASH) : "");
			}
			if (!Strings.isNullOrEmpty(resp)) {
				row.addAttribute("resp", resp);
			}
			/*if (bollaDettaglioDto.getContoDepositoInforete() != null 
					&& !bollaDettaglioDto.getContoDeposito().equals(bollaDettaglioDto.getContoDepositoInforete())) {
				row.addAttribute("cddif", bollaDettaglioDto.getContoDeposito() + "|" + bollaDettaglioDto.getContoDepositoInforete());
			} else {
				row.addAttribute("cddif", "");
			}
			Boolean isInGruppoScontoBase = model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_EDICOLA_IN_GRUPPO_SCONTO_BASE) != null ? (Boolean)(model.getContext().getSessionAttribute(IGerivConstants.SESSION_VAR_EDICOLA_IN_GRUPPO_SCONTO_BASE)) : false;
			if (bollaDettaglioDto.getSconto() != null && bollaDettaglioDto.getScontoInforete() != null 
					&& !bollaDettaglioDto.getSconto().equals(bollaDettaglioDto.getScontoInforete()) && isInGruppoScontoBase) {
				row.addAttribute("scdif", bollaDettaglioDto.getScontoFormat() + "|" + bollaDettaglioDto.getScontoInforeteFormat());
			} else {
				row.addAttribute("scdif", "");
			}*/
		} else if (currentRowBean instanceof BollaResaBaseDto) {
			BollaResaBaseDto dto = (BollaResaBaseDto) currentRowBean;
			row.addAttribute("pk", dto.getPk().toString());
		} else if (currentRowBean instanceof PubblicazioneDto) {
			PubblicazioneDto dto = (PubblicazioneDto) currentRowBean;
			row.addAttribute("iv", (dto.getContoDeposito() != null && dto.getContoDeposito() > 0) ? "2" : "");
			row.addAttribute("idtn", dto.getIdtn());
		} else if (currentRowBean instanceof ProdottiNonEditorialiBollaDettaglioVo) {
			ProdottiNonEditorialiBollaDettaglioVo pnevo = (ProdottiNonEditorialiBollaDettaglioVo) currentRowBean;
			row.addAttribute("prodottoDl", pnevo.getProdotto() != null ? pnevo.getProdotto().getProdottoDl() : "");
		} else if (currentRowBean instanceof FondoBollaDettaglioDto) {
			FondoBollaDettaglioDto fbd = (FondoBollaDettaglioDto) currentRowBean;
			row.addAttribute("iv", "");
			row.addAttribute("pv", "");
			row.addAttribute("fb", "true");
			boolean isContoDeposito = fbd.getTipoRecordFondoBolla() != null && fbd.getTipoRecordFondoBolla().equals(IGerivConstants.TIPO_RECORD_FONDO_BOLLA_CONTO_DEPOSITO) ? true : false;
			row.addAttribute("fbcd", isContoDeposito);
			/*boolean qtaFatturataInContoDepositoDiversaDaVenduto = !fbd.getQuantitaConsegnata().equals(fbd.getVendutoBollePrecedenti());
			if (isContoDeposito && qtaFatturataInContoDepositoDiversaDaVenduto) {
				row.addAttribute("qfdv", true);
			} else {
				row.addAttribute("qfdv", "");
			}*/
		} else if (currentRowBean instanceof MessaggioVo) {
			row.addAttribute("an", ((MessaggioVo) currentRowBean).getAttachmentName1());
			row.addAttribute("an1", ((MessaggioVo) currentRowBean).getAttachmentName2());
			row.addAttribute("an2", ((MessaggioVo) currentRowBean).getAttachmentName3());
		} else if (currentRowBean instanceof MessaggioDto) {
			row.addAttribute("an", ((MessaggioDto) currentRowBean).getAttachmentName1());
			row.addAttribute("an1", ((MessaggioDto) currentRowBean).getAttachmentName2());
			row.addAttribute("an2", ((MessaggioDto) currentRowBean).getAttachmentName3());
		} else if (currentRowBean instanceof EmailRivenditaDto) {
			row.addAttribute("an", ((EmailRivenditaDto) currentRowBean).getAllegato());
		} else if (currentRowBean instanceof RichiestaRifornimentoDto) {
			RichiestaRifornimentoDto dto = (RichiestaRifornimentoDto) currentRowBean;
			row.addAttribute("rigaEvasione", dto.isRigaEvasione());
		} else if (currentRowBean instanceof EstrattoContoEdicolaDettaglioVo) {
			EstrattoContoEdicolaDettaglioVo dto = (EstrattoContoEdicolaDettaglioVo) currentRowBean;
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
		} else {
			String style = (row.getStyle() != null && row.getStyle().indexOf(";background-color") != -1) ? row.getStyle().substring(0, row.getStyle().indexOf(";background-color")) : row.getStyle();
			row.setStyle(style);
		}
		
		String href = (row.getAttribute("href") != null) ? (String)row.getAttribute("href") : DPEWebContants.BLANK;
		if (!href.equals(DPEWebContants.BLANK)) {
			StringBuffer sbHrefParams = new StringBuffer();
			StringBuffer sbHrefValues = new StringBuffer();
			String hrefParams = href.substring(href.indexOf("?") + 1);
			StringTokenizer st = new StringTokenizer(hrefParams,"&");
			int count = 0;
			while (st.hasMoreTokens()) {
				String field = st.nextToken().trim();
				StringTokenizer st1 = new StringTokenizer(field,"=");
				while (st1.hasMoreTokens()) {
					String token = st1.nextToken().trim();
					if ((count % 2) != 0) {
						sbHrefParams.append("=");
						String property = token;
						try {
							property = BeanUtils.getProperty(currentRowBean, token);
						} catch (Exception e) {
							//throw new RuntimeException(e);
						}
						sbHrefParams.append(property);
						sbHrefValues.append(property);
					} else {
						if (count > 0) {
							sbHrefParams.append("&");
						}
						sbHrefParams.append(token);
					}
					count++;
				}
			}
			String firstPartHref = href.substring(0, href.indexOf("?") + 1);
			row.addAttribute("href1", firstPartHref + sbHrefParams.toString());
		}
    }

	@Override
	public void addRowAttributes(TableModel tableModel, Row row) {
		
	} 

}
