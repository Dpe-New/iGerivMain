package it.dpe.igeriv.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.rifornimenti.RifornimentiService;
import it.dpe.igeriv.dto.EsportazioneDatiDlResultDto;
import models.ScaricoDatiDto;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;

@Controller
@RequestMapping("/dl")
public class DlScaricoDatiController {
	private final Logger log = Logger.getLogger(getClass());

	@Autowired
	private RifornimentiService rifornimentiService;
	@Autowired
	private AgenzieService agenzieService;

	@Value("${dl.dati.path.import.log}") String datiPathDir; 
	@Value("${dl.out.path.dir}") String dlOutputDir;

	private enum EStatus {
		OK("0", "Creare il file"),
		FILE_ALREADY_EXISTS("1", "File esistente"),
		NO_DATA_TO_EXPORT("2", "Nessun dato da esportare"),
		DL_NOT_AUTHORIZED("11", "DL non autorizzato"),	
		ERROR("12", "Errore"),	
		UNEXPECTED_ERROR("13", "Errore non previsto");

		private final String status;
		private final String msg;

		private EStatus(String status, String msg) {
            this.status = status;
            this.msg = msg;
        }
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/scarico-dati/codDpeWebDl/{codDpeWebDl}/tipoDati/{tipoDati}")
	public ModelAndView getScaricoDati(@PathVariable Integer codDpeWebDl, @PathVariable Integer tipoDati) {
		ScaricoDatiDto dto;
		AnagraficaAgenziaVo anagraficaAgenziaVo = agenzieService.getAgenziaByCodiceDpeWeb(codDpeWebDl);
		if (anagraficaAgenziaVo == null) {
			dto = new ScaricoDatiDto();
			dto.setMsg("Agenzia non esistente");
			dto.setStatus("NOT_OK");
		} else {
			switch (tipoDati) {
				case 10:
				case 20:
					dto = esporta(anagraficaAgenziaVo, tipoDati);
					break;
				default:
					dto = new ScaricoDatiDto();
					dto.setMsg("Parametro tipo dati non valido");
					dto.setStatus("NOT_OK");
			}
		}
		return new ModelAndView("orderViewJson", "dl", dto);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/set-data-download/codFiegDl/{codFiegDl}/tipoDati/{tipoDati}/fileName/{fileName:.+}")
	public ModelAndView setDataDownload(@PathVariable Integer codFiegDl, @PathVariable Integer tipoDati, @PathVariable String fileName) {
		ScaricoDatiDto dto = new ScaricoDatiDto();
		dto.setStatus("OK");
		agenzieService.updateDataDownload(codFiegDl, fileName, tipoDati);
		return new ModelAndView("orderViewJson", "dl", dto);
	}

	public ScaricoDatiDto esporta(AnagraficaAgenziaVo anagraficaAgenziaVo, Integer tipoDati) {
		ScaricoDatiDto dto = new ScaricoDatiDto();
		dto.setCodFiegDl(anagraficaAgenziaVo.getCodFiegDl());
		dto.setUserFtp(anagraficaAgenziaVo.getDlWsFtpUser());
		dto.setPassFtp(anagraficaAgenziaVo.getDlWsFtpPass());
		EsportazioneDatiDlResultDto result = null;
		try {
			switch (tipoDati) {
				case 10:
					result = rifornimentiService.esportaRifornimenti(anagraficaAgenziaVo.getCodFiegDl());
					break;
				case 20:
					result = rifornimentiService.esportaAltriDati(anagraficaAgenziaVo.getCodFiegDl());
					break;
			}
			Map<String,Object> map = handleReturnMessage(result.getResultMsg());
			EStatus status = (EStatus)map.get("status");
			if (status == EStatus.OK) {
				String fileName = (String)map.get("file");
				String filePath = datiPathDir + System.getProperty("file.separator") + new DecimalFormat("0000").format(anagraficaAgenziaVo.getCodFiegDl()) + System.getProperty("file.separator") + dlOutputDir;
				writeFile(result.getFileContent(), filePath, fileName);
				dto.setStatus("OK");
				dto.setFileName(fileName);
			} else if (status == EStatus.FILE_ALREADY_EXISTS) {
				String fileName = (String)map.get("file");
				String filePath = datiPathDir + System.getProperty("file.separator") + new DecimalFormat("0000").format(anagraficaAgenziaVo.getCodFiegDl()) + System.getProperty("file.separator") + dlOutputDir;
				File file = new File(filePath, fileName);
				if (file.exists()) {
					dto.setStatus("OK");
					dto.setFileName(fileName);
				} else {
					dto.setStatus("NOT_OK");
					dto.setMsg(String.format("Ritrasmissione del file %s non esistente controllare tabella 9133", fileName));
				}
			} else {
				dto.setStatus("NOT_OK");
				dto.setMsg(status.msg);
			}

		} catch (Throwable e) {
			log.error("Errore durante l'esportazione di altri dati", e);
			dto.setStatus("NOT_OK");
			dto.setMsg(e.getMessage());
		}
		return dto;
	}

	private Map<String,Object> handleReturnMessage(String result) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (result != null) {
			String[] args = result.split("\\|");
			boolean hasErr = args[0].equals("0") ? false : true;
			if (hasErr) {
				if (args.length > 1) {
					String msg = args[1];
					if (msg.contains("ORA-20001")) {
						map.put("status", EStatus.DL_NOT_AUTHORIZED);
					} else if (msg.contains("ORA-20002")) {
						map.put("status", EStatus.FILE_ALREADY_EXISTS);
						String fileName = msg.substring(msg.indexOf("file=") + 5);
						map.put("file", fileName);
					} else {
						map.put("status", EStatus.ERROR);
						map.put("error", msg);
					}
				} else {
					map.put("status", EStatus.UNEXPECTED_ERROR);
				}
			} else {
				if (args.length > 1) {
					map.put("status", EStatus.OK);
					map.put("file", args[1]);
				} else {
					map.put("status", EStatus.NO_DATA_TO_EXPORT);
				}
			}
		}
		return map;
	}

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

}
 