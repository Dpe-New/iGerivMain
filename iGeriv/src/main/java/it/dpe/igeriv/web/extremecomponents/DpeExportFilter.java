package it.dpe.igeriv.web.extremecomponents;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.core.Preferences;
import org.extremecomponents.table.core.TableConstants;
import org.extremecomponents.table.core.TableModelUtils;
import org.extremecomponents.table.core.TableProperties;
import org.extremecomponents.table.filter.ExportFilter;
import org.extremecomponents.table.filter.ExportFilterUtils;
import org.extremecomponents.util.MimeUtils;

/**
 * Filtro customizzado per l'esportazione in pdf, xls o altro della 
 * tabella generata dalla tag lib estremecomponents.
 * 
 * Se presente l'attributo "downloadToken" (campo hidden nel form della pagina chiamante) 
 * genera un cookie per notificare il javascript della pagina dell'apertura della dialog di download.
 * 
 * @author romanom
 *
 */
public class DpeExportFilter extends ExportFilter {
	
	@Override
	protected void handleExport(HttpServletRequest request, HttpServletResponse response, Context context) {
		
		if (request.getParameter("downloadToken") != null && !request.getParameter("downloadToken").toString().equals("")) {
			response.addCookie(new Cookie("fileDownloadToken", request.getParameter("downloadToken").toString()));
		} else if (request.getParameter("downloadToken1") != null && !request.getParameter("downloadToken1").toString().equals("")) {
			response.addCookie(new Cookie("fileDownloadToken", request.getParameter("downloadToken1").toString()));
		} else if (request.getParameter("downloadToken2") != null && !request.getParameter("downloadToken2").toString().equals("")) {
			response.addCookie(new Cookie("fileDownloadToken", request.getParameter("downloadToken2").toString()));
		} 
		
		// 07/03/2017 vine richiamato il metodo in base alla tipologia di file da generare
		if(request.getParameter("type_return") != null && request.getParameter("type_return").toString().equals("TIFF")){
			this.AbstractExportFilter_handleExport(request, response, context);
		}else {
			
			String fileName = ExportFilterUtils.getExportFileName(context);
        	String mimeType = MimeUtils.getFileMimeType(fileName);
        	//APERTURA PDF DAL BROWSER
        	response.setContentType("application/pdf");
        	response.setHeader("Cache-Control","no-store"); 
    		response.setHeader("Pragma","no-cache"); 
    		response.setHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
			
			
			super.handleExport(request, response, context);
		}
		
		
	}
	
	private void AbstractExportFilter_handleExport(HttpServletRequest request, HttpServletResponse response, Context context) {
        try {
        	String fileName = ExportFilterUtils.getExportFileName(context);
        	String mimeType = MimeUtils.getFileMimeType(fileName);
        	//image/tiff
        	response.setContentType("image/tiff");
        	response.setHeader("Cache-Control","no-store"); 
    		response.setHeader("Pragma","no-cache"); 
    		response.setHeader("Content-Disposition","attachment; filename=\""+fileName+".tiff\"");
    		response.setDateHeader ("Expires", 0); 

        	
        	
        	
            Object viewData = request.getAttribute(TableConstants.VIEW_DATA);
            if (viewData != null) {
                Preferences preferences = new TableProperties();
                preferences.init(null, TableModelUtils.getPreferencesLocation(context));
                String viewResolver = (String) request.getAttribute(TableConstants.VIEW_RESOLVER);
                Class classDefinition = Class.forName(viewResolver);
                
//				DpePdfViewResolver handleExportViewResolver = (DpePdfViewResolver) classDefinition.newInstance();
//              ViewResolver handleExportViewResolver = (ViewResolver) classDefinition.newInstance();
//                handleExportViewResolver.resolveView(request, response, preferences, viewData);
                
                
                
                DpePdfViewResolver handleExportViewResolver = new DpePdfViewResolver();
                handleExportViewResolver.resolveView(request, response, preferences, viewData);
                
                
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	
	
	
}
