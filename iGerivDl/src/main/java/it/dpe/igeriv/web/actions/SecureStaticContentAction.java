package it.dpe.igeriv.web.actions;

import it.dpe.igeriv.util.IGerivConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action che serve i contenuto statici che devono essere protetti da autenticazione.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Getter
@Setter
@Scope("prototype")
@Component("secureStaticContentAction")
public class SecureStaticContentAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private String fileName;
	private Integer type;
	private String folder;
	private final String imgPathDir;
	private final String imgResaPathDir;
	private final String imgMiniaturaPathDir;
	private final String imgMiniaturaEdicolaPathDir;
	private final String imgMiniaturaProdottoNonEditorialeEdicolaPathDir;
	private final String imgProdottiVariDlPathDir;
	private final String pathFilesPdf;
	private final String pathEstrattoContoEdicole;
	
	public SecureStaticContentAction() {
		this(null,null,null,null,null,null,null,null);
	}
	
	@Autowired
	public SecureStaticContentAction(@Value("${img.path.dir}") String imgPathDir, @Value("${edicole.resa.img.dir}") String imgResaPathDir, @Value("${img.miniature.path.dir}") String imgMiniaturaPathDir, @Value("${img.miniature.edicola.path.dir}") String imgMiniaturaEdicolaPathDir, @Value("${img.miniature.edicola.prodotti.vari.path.dir}") String imgMiniaturaProdottoNonEditorialeEdicolaPathDir, @Value("${img.prodotti.vari.dl.path.dir}") String imgProdottiVariDlPathDir, @Value("${path.files.pdf}") String pathFilesPdf, @Value("${path.fatture.edicole}") String pathEstrattoContoEdicole) {
		this.imgPathDir = imgPathDir;
		this.imgResaPathDir = imgResaPathDir;
		this.imgMiniaturaPathDir = imgMiniaturaPathDir;
		this.imgMiniaturaEdicolaPathDir = imgMiniaturaEdicolaPathDir;
		this.imgMiniaturaProdottoNonEditorialeEdicolaPathDir = imgMiniaturaProdottoNonEditorialeEdicolaPathDir;
		this.imgProdottiVariDlPathDir = imgProdottiVariDlPathDir;
		this.pathFilesPdf = pathFilesPdf;
		this.pathEstrattoContoEdicole = pathEstrattoContoEdicole;
	}
	
	public String execute() throws Exception {
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HttpServletResponse response = ServletActionContext.getResponse();
		String path = getImgPath() + System.getProperty("file.separator") + getFileName(); 

		String mimeType = servletContext.getMimeType(path);
	    if (mimeType == null) {
	    	servletContext.log("Could not get MIME type of " + path);
	    	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        return null;
	    }
	    
		// Set content type
		response.setContentType(mimeType);
		response.setHeader("Cache-Control","no-store"); 
		response.setHeader("Pragma","no-cache"); 
		if (getType() != null && getFileName() != null && (getType().equals(IGerivConstants.COD_TIPO_IMMAGINE_PDF) || getType().equals(IGerivConstants.COD_TIPO_IMMAGINE_PDF_FATTURE))) {
			response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
		}
		response.setDateHeader ("Expires", 0); 
		
		// Set content size
		File file = new File(path);
		response.setContentLength((int) file.length());

		// Open the file and output streams
		FileInputStream in = new FileInputStream(file);
		OutputStream out = response.getOutputStream();

		// Copy the contents of the file to the output stream
		byte[] buf = new byte[1024];
		int count = 0;
		while ((count = in.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		out.flush();
		out.close();
		in.close();
		return null;
	}

	private String getImgPath() {
		if (getType() != null) {
			switch (getType().intValue()) {				
				case IGerivConstants.COD_TIPO_IMMAGINE_RESA:
					return getImgResaPathDir() + "/" + getFolder();
				case IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA:
					return getImgMiniaturaPathDir();
				case IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA_EDICOLA:
					return getImgMiniaturaEdicolaPathDir();
				case IGerivConstants.COD_TIPO_IMMAGINE_MINIATURA_PNE_EDICOLA:
					return getImgMiniaturaProdottoNonEditorialeEdicolaPathDir();
				case IGerivConstants.COD_TIPO_IMMAGINE_PRODOTTI_VARI_DL:
					return getImgProdottiVariDlPathDir();
				case IGerivConstants.COD_TIPO_IMMAGINE_PDF:
					return getPathFilesPdf();
				case IGerivConstants.COD_TIPO_IMMAGINE_PDF_FATTURE:
					return getPathEstrattoContoEdicole() + System.getProperty("file.separator") + getAuthUser().getCodFiegDlMaster() + System.getProperty("file.separator") + getAuthUser().getCodEdicolaMaster();
				default: 
					return getImgPathDir();
			}
		} 
		return getImgPathDir();
	}

	public void setParameters(Map<String, String[]> parameters) {
		setFileName(parameters.get("fileName").toString());
	}

}
