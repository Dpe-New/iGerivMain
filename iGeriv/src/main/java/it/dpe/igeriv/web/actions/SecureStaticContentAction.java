package it.dpe.igeriv.web.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.util.IGerivConstants;

/**
 * Classe action che serve i contenuto statici che devono essere protetti da autenticazione.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Scope("prototype")
@Component("secureStaticContentAction")
public class SecureStaticContentAction extends RestrictedAccessBaseAction {
	private static final long serialVersionUID = 1L;
	private String fileName;
	private Integer type;
	private String folder;
	@Value("${img.path.dir}")
	private String imgPathDir;
	@Value("${edicole.resa.img.dir}")
	private String imgResaPathDir;
	@Value("${img.miniature.path.dir}")
	private String imgMiniaturaPathDir;
	@Value("${img.miniature.edicola.path.dir}")
	private String imgMiniaturaEdicolaPathDir;
	@Value("${img.miniature.edicola.prodotti.vari.path.dir}")
	private String imgMiniaturaProdottoNonEditorialeEdicolaPathDir;
	@Value("${img.prodotti.vari.dl.path.dir}")
	private String imgProdottiVariDlPathDir;
	@Value("${path.files.pdf}")
	private String pathFilesPdf;
	@Value("${path.fatture.edicole}")
	private String pathEstrattoContoEdicole;
	@Value("${path.estratto.conto.pdf}")
	private String pathFilesPdfEstrattoConto;
	
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
				case IGerivConstants.COD_TIPO_IMMAGINE_PDF_ESTRATTO_CONTO:
					if(getAuthUser() != null && getAuthUser().isMultiDl()){
						return getPathFilesPdfEstrattoConto() + System.getProperty("file.separator") + getAuthUser().getCodFiegDl() + System.getProperty("file.separator") + getAuthUser().getCodDpeWebEdicola();
					}else{
						return getPathFilesPdfEstrattoConto() + System.getProperty("file.separator") + getAuthUser().getCodFiegDlMaster() + System.getProperty("file.separator") + getAuthUser().getCodEdicolaMaster();
					}
				default: 
					return getImgPathDir();
			}
		} 
		return getImgPathDir();
	}

	public void setParameters(Map<String, String[]> parameters) {
		setFileName(parameters.get("fileName").toString());
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getImgPathDir() {
		return imgPathDir;
	}

	public void setImgPathDir(String imgPathDir) {
		this.imgPathDir = imgPathDir;
	}

	public String getImgResaPathDir() {
		return imgResaPathDir;
	}

	public void setImgResaPathDir(String imgResaPathDir) {
		this.imgResaPathDir = imgResaPathDir;
	}

	public String getImgMiniaturaPathDir() {
		return imgMiniaturaPathDir;
	}

	public void setImgMiniaturaPathDir(String imgMiniaturaPathDir) {
		this.imgMiniaturaPathDir = imgMiniaturaPathDir;
	}

	public String getImgMiniaturaEdicolaPathDir() {
		return imgMiniaturaEdicolaPathDir;
	}

	public void setImgMiniaturaEdicolaPathDir(String imgMiniaturaEdicolaPathDir) {
		this.imgMiniaturaEdicolaPathDir = imgMiniaturaEdicolaPathDir;
	}

	public String getImgMiniaturaProdottoNonEditorialeEdicolaPathDir() {
		return imgMiniaturaProdottoNonEditorialeEdicolaPathDir;
	}

	public void setImgMiniaturaProdottoNonEditorialeEdicolaPathDir(String imgMiniaturaProdottoNonEditorialeEdicolaPathDir) {
		this.imgMiniaturaProdottoNonEditorialeEdicolaPathDir = imgMiniaturaProdottoNonEditorialeEdicolaPathDir;
	}

	public String getImgProdottiVariDlPathDir() {
		return imgProdottiVariDlPathDir;
	}

	public void setImgProdottiVariDlPathDir(String imgProdottiVariDlPathDir) {
		this.imgProdottiVariDlPathDir = imgProdottiVariDlPathDir;
	}

	public String getPathFilesPdf() {
		return pathFilesPdf;
	}

	public void setPathFilesPdf(String pathFilesPdf) {
		this.pathFilesPdf = pathFilesPdf;
	}

	public String getPathEstrattoContoEdicole() {
		return pathEstrattoContoEdicole;
	}

	public void setPathEstrattoContoEdicole(String pathEstrattoContoEdicole) {
		this.pathEstrattoContoEdicole = pathEstrattoContoEdicole;
	}

	public String getPathFilesPdfEstrattoConto() {
		return pathFilesPdfEstrattoConto;
	}

	public void setPathFilesPdfEstrattoConto(String pathFilesPdfEstrattoConto) {
		this.pathFilesPdfEstrattoConto = pathFilesPdfEstrattoConto;
	}
	
}
