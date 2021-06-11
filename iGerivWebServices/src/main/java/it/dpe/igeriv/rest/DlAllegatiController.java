package it.dpe.igeriv.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.rest.dto.ResultRestDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.MessaggioVo;
import it.dpe.igeriv.vo.pk.MessaggioPk;
import lombok.extern.log4j.Log4j;
import models.ScaricoDatiDto;

@RestController
@RequestMapping("/dl")
public class DlAllegatiController {

	@Value("${upload.path.dir}") String pathAllegati; 

	@Autowired
	private AgenzieService agenzieService;
	@Autowired
	private MessaggiService messaggiService;
	
	public DlAllegatiController() {
		
	}
	
	@RequestMapping(value="/allegati", method=RequestMethod.POST)
	public ResultRestDto allegati(	@RequestParam("fileAllegato") 	MultipartFile	fileAllegato, 
									@RequestParam("codDL") 			Integer			codDL, 
									@RequestParam("oggetto") 		String			oggetto, 
									@RequestParam("testo") 			String			testo)
	{
		
		ResultRestDto result = new ResultRestDto();
		result.setCode(0);
		result.setOk(true);

		try {
			AnagraficaAgenziaVo anagraficaAgenziaVo = agenzieService.getAgenziaByCodiceDpeWeb(codDL);
			if (anagraficaAgenziaVo == null) {
				throw new Exception(String.format("Agenzia non presente: " + codDL));
			} 
			
		
			File fileOut = getFileAllegato(fileAllegato.getOriginalFilename(), codDL);
			result.setMessage(fileOut.getAbsolutePath());
			OutputStream os =  new FileOutputStream(fileOut);
			IOUtils.copy(fileAllegato.getInputStream(), new FileOutputStream(fileOut));
			MessaggioVo messaggioVo = getMessaggioVo(codDL,oggetto, testo,fileOut);
			messaggiService.saveBaseVo(messaggioVo);
			
		} catch (Exception e) {
			result.setCode(-1);
			result.setOk(false);
			result.setMessage(e.getMessage());
		}
		
		return result;
	}

	private MessaggioVo getMessaggioVo(Integer codDL, String oggetto, String testo, File fileAllegato) {
		MessaggioVo vo = new MessaggioVo();
		MessaggioPk pk = new MessaggioPk();
		pk.setCodFiegDl(codDL);
		pk.setDestinatarioA(0);
		pk.setDestinatarioB(0);
		pk.setDtMessaggio(new Timestamp(new Date().getTime()));
		pk.setTipoDestinatario(IGerivConstants.COD_EDICOLE_MULTIPLE);
		vo.setPk(pk);
		vo.setMessaggio(getTextMessaggio(oggetto, testo));
		vo.setStatoMessaggio(IGerivConstants.STATO_MESSAGGIO_INVIATO);
		vo.setTipoMessaggio(IGerivConstants.TIPO_MESSAGGIO_IMMEDIATO);
		vo.setAttachmentName1(fileAllegato.getAbsolutePath());
		
		vo.setCategoria(null);
		return vo;
	}

	private File getFileAllegato(String name, Integer codDL) {
		File result =  new File(String.format("%s%s%d%s%s", pathAllegati,File.separatorChar,codDL, File.separatorChar, name ));
		if (!result.getParentFile().exists()) {
			result.getParentFile().mkdirs();
		}
		return result;
		
		
	}
	
	private String getTextMessaggio(String oggetto, String testo) {
		if (oggetto==null || oggetto.trim().isEmpty()) {
			oggetto="ATTENZIONE";
		}
		if (testo==null || testo.trim().isEmpty()) {
			testo="Allegato presente";
		}
		return String.format(
			"<div style=\"text-align:center;\">" +
				"<font color=\"#CC0000\" size=\"12px\">" +
					"<p ><b>%s</b></p>" +
				"</font>" +
				"<font size=\"8px\" color=\"#000099\">" +
					"<p>%s</p>" +
				"</font>" +
			"</div>",
			oggetto, testo);
		
	}
	

}
