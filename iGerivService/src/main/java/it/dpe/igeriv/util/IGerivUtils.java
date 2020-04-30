package it.dpe.igeriv.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action che serve le immagini delle pubblicazioni delle bolle.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Component("iGerivUtils")
public class IGerivUtils {
	private final Logger log = Logger.getLogger(getClass());
	private final String imgPathDir;
	private final String imgResaPathDir;
	private final String imgMiniaturaPathDir;
	private final String imgMiniaturaEdicolaPathDir;
	private final String imgMiniaturaProdottoNonEditorialeEdicolaPathDir;
	private final String imgProdottiVariDlPathDir;
	private final String pdfDir;
	
	@Autowired
	public IGerivUtils(
			@Value("${img.alias.path.dir}") String imgPathDir, 
			@Value("${edicole.alias.resa.img.dir}") String imgResaPathDir, 
			@Value("${img.alias.miniature.path.dir}") String imgMiniaturaPathDir, 
			@Value("${img.alias.miniature.edicola.path.dir}") String imgMiniaturaEdicolaPathDir, 
			@Value("${img.alias.miniature.edicola.prodotti.vari.path.dir}") String imgMiniaturaProdottoNonEditorialeEdicolaPathDir, 
			@Value("${img.alias.prodotti.vari.dl.path.dir}") String imgProdottiVariDlPathDir, 
			@Value("${path.alias.files.pdf}") String pdfDir) {
		this.imgPathDir = imgPathDir;
		this.imgResaPathDir = imgResaPathDir;
		this.imgMiniaturaPathDir = imgMiniaturaPathDir;
		this.imgMiniaturaEdicolaPathDir = imgMiniaturaEdicolaPathDir;
		this.imgMiniaturaProdottoNonEditorialeEdicolaPathDir = imgMiniaturaProdottoNonEditorialeEdicolaPathDir;
		this.imgProdottiVariDlPathDir = imgProdottiVariDlPathDir;
		this.pdfDir = pdfDir;
	}
	
	/**
	 * Ritorna il crivw del dl corripondente, 
	 * con base negli array <param>codDl</param> e <param>codEdicola</param>
	 * 
	 * @param coddl
	 * @param codDl
	 * @param codEdicola
	 * @return
	 */
	public Integer getCorrispondenzaCodEdicolaMultiDl(Integer coddl, Integer[] codDl, Integer[] codEdicola) {
    	for (int i = 0; i < codDl.length; i++) {
    		if (codDl[i].equals(coddl)) {
    			return codEdicola[i];
    		}
    	}
		return null;
	}
	
	/**
	 * Ritorna l'alias di Apache che punta alla cartella locale dove risiedono le immagini.
	 * I valori sono definiti in igeriv.properties che riporta l'alias definito nel file httpd.conf di Apache
	 * 
	 * @param tipoImmagine
	 * @return
	 */
	public String getImgAlias(Integer tipoImmagine) {
		if (tipoImmagine != null) {
			switch (tipoImmagine.intValue()) {	
				case IGerivConstants.COD_TIPO_IMMAGINE_PDF:
					return pdfDir;
				case IGerivConstants.COD_TIPO_IMMAGINE_RESA:
					return imgResaPathDir;
				case IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA:
					return imgMiniaturaPathDir;
				case IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA_EDICOLA:
					return imgMiniaturaEdicolaPathDir;
				case IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA_PNE_EDICOLA:
					return imgMiniaturaProdottoNonEditorialeEdicolaPathDir;
				case IGerivConstants.COD_TIPO_IMMAGINE_PRODOTTI_VARI_DL:
					return imgProdottiVariDlPathDir;			
				default: 
					return imgPathDir;
			}
		} else {
			return imgPathDir;
		}
	}
	
	/**
	 * @return
	 */
	public Date getVenditeDeleteModuleDeployDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2012);
		cal.set(Calendar.MONTH, 7);
		cal.set(Calendar.DAY_OF_MONTH, 24);
		cal.set(Calendar.HOUR_OF_DAY, 13);
		cal.set(Calendar.MINUTE, 15);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * Costruisce una mappa chiave - valore basandosi su una query string, 
	 * dove il valore viene estratto dal <param>bean</param> 
	 * 
	 * @param bean
	 * @param href
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Map<String, String> buildParamsMap(Object bean, String href) {
		Map<String, String> mapParams = new LinkedHashMap<String, String>();
		if (!Strings.isNullOrEmpty(href)) {
			String token = href.indexOf("?") != -1 ? href.substring(href.indexOf("?") + 1) : href;
			for (String pair : token.split("&")) {
				String[] split = pair.split("=");
				String val = "";
				try {
					val = BeanUtils.getProperty(bean, split[1]);
				} catch (Exception e) {
					log.error("Error getting field " + split[1] + " using BeanUtils.getProperty()", e);
				}
				mapParams.put(split[0], val);
			}
		}
		return mapParams;
	}
	
}
