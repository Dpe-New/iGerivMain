package it.dpe.igeriv.web.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ParameterAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Classe action che esegue l'upload.
 * 
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Scope("prototype")
@Component("uploadAction")
public class UploadAction extends RestrictedAccessBaseAction implements ParameterAware {
	private static final long serialVersionUID = 1L;
	private String fileName;
	@Value("${upload.path.dir}")
	private String uploadPathDir;
	@Value("${edicole.upload.path.dir}")
	private String uploadRivenditaPathDir;
	private Integer codRivendita;

	public String execute() throws Exception {
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HttpServletResponse response = ServletActionContext.getResponse();
		String fileName = uploadPathDir + "/" + getAuthUser().getCodFiegDl() + "/" + getFileName();
		if (codRivendita != null) {
			fileName = uploadRivenditaPathDir + "/" + codRivendita + "/" + getFileName();
		}
		File file = new File(fileName);
		if (file != null && file.isFile()) {
			String mimeType = servletContext.getMimeType(fileName);
		    if (mimeType == null) {
		    	servletContext.log("Could not get MIME type of " + fileName);
		    	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		        return null;
		    }
		    
			// Set content type
			response.setContentType(mimeType);
			response.setHeader("Cache-Control","no-store"); 
			response.setHeader("Pragma","no-cache"); 
			response.setDateHeader ("Expires", 0); 
			
			// Set content size
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
		}
		return null;
	}

	public void setParameters(Map<String, String[]> parameters) {
		setFileName(parameters.get("fileName").toString());
	}

	public String getFileName() {
		if (fileName != null) {
			if (fileName.contains(".JPG")) {
				fileName = fileName.replace(".JPG", ".jpg");
			} else if (fileName.contains(".GIF")) {
				fileName = fileName.replace(".GIF", ".gif");
			} else if (fileName.contains(".PNG")) {
				fileName = fileName.replace(".PNG", ".png");
			} else if (fileName.contains(".TIF")) {
				fileName = fileName.replace(".TIF", ".tif");
			}
		}
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUploadPathDir() {
		return uploadPathDir;
	}

	public void setUploadPathDir(String uploadPathDir) {
		this.uploadPathDir = uploadPathDir;
	}

	public String getUploadRivenditaPathDir() {
		return uploadRivenditaPathDir;
	}

	public void setUploadRivenditaPathDir(String uploadRivenditaPathDir) {
		this.uploadRivenditaPathDir = uploadRivenditaPathDir;
	}

	public Integer getCodRivendita() {
		return codRivendita;
	}

	public void setCodRivendita(Integer codRivendita) {
		this.codRivendita = codRivendita;
	}
	
}
