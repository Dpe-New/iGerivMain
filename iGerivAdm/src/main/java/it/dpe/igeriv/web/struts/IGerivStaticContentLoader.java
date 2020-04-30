package it.dpe.igeriv.web.struts;

import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.SpringContextManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.DefaultStaticContentLoader;

/**
 * @deprecated 
 * Questa classe non viene più utilizzata perchè il controllo della cache è eseguito da Apache Httpd
 * Come definito nel web.xml struts risponde solo alle richieste che terminano per .action, escludendo quindi i contenuti statici
 * 
 * Questa classe estende una classe del framework struts2 per definire 
 * le risorse statiche del sito. Queste dovranno essere scaricate dal browser soltanto 
 * allo scadere di giorni N. <code>IGerivConstants.STATIC_CONTENT_EXPIRE_DAYS</code>
 * dalla data in cui sono state scaricate la prima volta. 
 * L'uso di questa classe da parte del framework è configurato nel 
 * file struts.xml nella chiave "struts.staticContentLoader".
 * 
 * @author romanom
 *
 */
public class IGerivStaticContentLoader extends DefaultStaticContentLoader {
	private final Logger log = Logger.getLogger(getClass());
	
	@Override
	public boolean canHandle(String resourcePath) {
		return serveStatic && (resourcePath.startsWith("/struts") || resourcePath.startsWith("/static") || isIgerivStaticResource(resourcePath));
	}
	
	@Override
	protected String getAdditionalPackages() {
		return super.getAdditionalPackages() + " css js  img html pdf";
	}
	
	@Override
	protected URL findResource(String path) throws IOException {
		if (isIgerivStaticResource(path)) {
			return SpringContextManager.getSpringContext().getResource(path).getURL();
		} 
		return super.findResource(path);
	}
	
	protected File findResourceAsFile(String path) throws IOException {
		if (isIgerivStaticResource(path)) {
			return SpringContextManager.getSpringContext().getResource(path).getFile();
		} 
		return null;
	}
	
	@Override
	protected String buildPath(String name, String packagePrefix)
			throws UnsupportedEncodingException {
		if (isIgerivStaticResource(name)) {
			return name;
		}
		return super.buildPath(name, packagePrefix);
	}
	
	@Override
	protected String cleanupPath(String path) {
		if (isIgerivStaticResource(path)) {
			return path;
		}
		return super.cleanupPath(path);
	}
	
	@Override
	protected void process(InputStream is, String path, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (is != null) {
        	File resource = findResourceAsFile(path);
            
        	Calendar cal = Calendar.getInstance();
            long ifModifiedSince = 0;
            try {
                ifModifiedSince = request.getDateHeader("If-Modified-Since");
            } catch (Exception e) {
            	log.warn("Invalid If-Modified-Since header value: '"
                        + request.getHeader("If-Modified-Since") + "', ignoring");
            }
            long lastModifiedMillis = resource.lastModified();
            long now = cal.getTimeInMillis();
            cal.add(Calendar.MINUTE, IGerivConstants.STATIC_CONTENT_EXPIRE_DAYS);
            long expires = cal.getTimeInMillis();

            if (ifModifiedSince > 0 && ifModifiedSince > lastModifiedMillis) {
                // not modified, content is not sent - only basic
                // headers and status SC_NOT_MODIFIED
                response.setDateHeader("Expires", expires);
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                is.close();
                return;
            }

            // set the content-type header
            String contentType = getContentType(path);
            if (contentType != null) {
                response.setContentType(contentType);
            }

            if (serveStaticBrowserCache) {
                // set heading information for caching static content
                response.setDateHeader("Date", now);
                response.setDateHeader("Expires", expires);
                response.setDateHeader("Retry-After", expires);
                response.setHeader("Cache-Control", "public");
                response.setDateHeader("Last-Modified", now);
            } else {
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "-1");
            }

            try {
                copy(is, response.getOutputStream());
            } finally {
                is.close();
            }
            return;
        }
    }
	
	private boolean isIgerivStaticResource(String path) {
		if (path != null && (path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/img") || path.startsWith("/html") || path.startsWith("/pdf"))) {
			return true;
		}
		return false;
	}
}
