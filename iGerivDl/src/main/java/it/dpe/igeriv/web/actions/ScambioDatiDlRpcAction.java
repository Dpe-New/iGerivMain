package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.dto.EsportazioneDatiDlResultDto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.json.annotations.SMDMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * Classe action del DL per il download dei dati delle rivendite.
 * La pagina JSP contiene un bottone che genera il file di dati sul server e  
 * una Applet che esegue il download del file e l'upload sull'FTP gestionale.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("ScambioDatiDlRpcAction")
public class ScambioDatiDlRpcAction extends RestrictedAccessBaseAction implements RequestAware {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(getClass());
	private final RifornimentiService rifornimentiService;
	private final AgenzieService agenzieService;
	private final String datiPathDir;
	private final String dlOutputDir;
	
	public ScambioDatiDlRpcAction() {
		this(null,null,null,null);
	}
	
	@Autowired
	public ScambioDatiDlRpcAction(RifornimentiService rifornimentiService, AgenzieService agenzieService, @Value("${dl.dati.path.import.log}") String datiPathDir, @Value("${dl.out.path.dir}") String dlOutputDir) {
		this.rifornimentiService = rifornimentiService;
		this.agenzieService = agenzieService;
		this.datiPathDir = datiPathDir;
		this.dlOutputDir = dlOutputDir;
	}
	
	@SMDMethod
	public Map<String, String> esportaRifornimenti() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			EsportazioneDatiDlResultDto result = rifornimentiService.esportaRifornimenti(getAuthUser().getCodFiegDl());
			handleReturnMessage(map, result.getResultMsg());
			if (!Strings.isNullOrEmpty(map.get("file"))) {
				String filePath = datiPathDir + System.getProperty("file.separator") + new DecimalFormat("0000").format(getAuthUser().getCodFiegDl()) + System.getProperty("file.separator") + dlOutputDir;
				writeFile(result.getFileContent(), filePath, map.get("file").toString());
			}
		} catch (Throwable e) {
			log.error("Errore durante l'esportazione dei dati di rifornimento", e);
			map.put("errMsg", getText("gp.errore.imprevisto"));
		}
		return map;
	}
	
	@SMDMethod
	public Map<String, String> esportaAltriDati() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			EsportazioneDatiDlResultDto result = rifornimentiService.esportaAltriDati(getAuthUser().getCodFiegDl());
			handleReturnMessage(map, result.getResultMsg()); 
			if (!Strings.isNullOrEmpty(map.get("file"))) {
				String filePath = datiPathDir + System.getProperty("file.separator") + new DecimalFormat("0000").format(getAuthUser().getCodFiegDl()) + System.getProperty("file.separator") + dlOutputDir;
				writeFile(result.getFileContent(), filePath, map.get("file").toString());
			}
		} catch (Throwable e) {
			log.error("Errore durante l'esportazione di altri dati", e);
			map.put("errMsg", getText("gp.errore.imprevisto"));
		}
		return map;
	}
	
	@SMDMethod
	public Map<String, String> esportaTutto() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			EsportazioneDatiDlResultDto result = rifornimentiService.esportaTutto(getAuthUser().getCodFiegDl());
			handleReturnMessage(map, result.getResultMsg()); 
			if (!Strings.isNullOrEmpty(map.get("file"))) {
				String filePath = datiPathDir + System.getProperty("file.separator") + new DecimalFormat("0000").format(getAuthUser().getCodFiegDl()) + System.getProperty("file.separator") + dlOutputDir;
				writeFile(result.getFileContent(), filePath, map.get("file").toString());
			}
			if (!Strings.isNullOrEmpty(map.get("file1"))) {
				String filePath = datiPathDir + System.getProperty("file.separator") + new DecimalFormat("0000").format(getAuthUser().getCodFiegDl()) + System.getProperty("file.separator") + dlOutputDir;
				writeFile(result.getFileContent(), filePath, map.get("file1").toString());
			}
		} catch (Throwable e) {
			log.error("Errore durante l'esportazione di tutti i dati", e);
			map.put("errMsg", getText("gp.errore.imprevisto"));
		}
		return map;
	}
	
	@SMDMethod
	public String updateDataDownload(String fileName, String tipo) {
		try {
			agenzieService.updateDataDownload(getAuthUser().getCodFiegDl(), fileName, new Integer(tipo));
		} catch (Throwable e) {
			return "error";
		}
		return "success";
	}
	
	/**
	 * Scrive le righe contenute in una lista di String in un file
	 * 
	 * @param List<String> listMsg
	 * @param String fileName
	 * @throws IOException 
	 */
	private void writeFile(List<String> listMsg, String dirName, String fileName) throws IOException {
		FileOutputStream fos = null;
		OutputStreamWriter out = null;
		try {
			if (listMsg != null && !listMsg.isEmpty()) {
				File fileDir = new File(dirName);
				if (!fileDir.isDirectory()) {
					fileDir.mkdirs();
				}
				File file = new File(dirName + System.getProperty("file.separator") + fileName);
				fos = new FileOutputStream(file, true);
				out = new OutputStreamWriter(fos, "UTF-8"); 
				for (String line : listMsg) {
		        	if (!Strings.isNullOrEmpty(line)) {
		        		out.append(line);
		        		out.append(System.getProperty("line.separator"));
		        	}
				} 
			}
		} finally {
	    	try {
	    		if (out != null) {
	    			out.close();
		    	}
		    	if (fos != null) {
		    		fos.close();
		    	}
	    	} catch (Throwable e) {
	    		log.error("Errore durante la chiusura del file " + datiPathDir + System.getProperty("file.separator") + fileName , e);
	    	}
	    }
	}
	
	private void handleReturnMessage(Map<String, String> map, String result) {
		if (result != null) {
			String[] args = result.split("\\|");
			boolean hasErr = args[0].equals("0") ? false : true;
			String msg = args.length > 1 ? args[1] : map.put("errMsg", getText("igeriv.nessun.dato.da.esportare"));
			if (hasErr) {
				String errorMsg = getText("igeriv.errore.esecuzione.procedura") + "<br/>" + msg;
				if (msg != null && msg.contains("ORA-20001")) {
					errorMsg = getText("msg.dl.non.autotizzato");
				} else if (msg != null && msg.contains("ORA-20002")) {
					errorMsg = getText("msg.file.dati.gia.esistente");
					String fileName = msg.substring(msg.indexOf("file=") + 5);
					map.put("file", fileName);
				} 
				map.put("errMsg", errorMsg);
			} else if (args.length > 1) {				
				map.put("file", msg);
				if (args.length > 2) {
					map.put("file1", args[2]);
				}
			}
		}
	}
	
}
