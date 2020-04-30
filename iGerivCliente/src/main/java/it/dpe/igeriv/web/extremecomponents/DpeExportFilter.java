package it.dpe.igeriv.web.extremecomponents;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.filter.ExportFilter;

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
	protected void handleExport(HttpServletRequest request,
			HttpServletResponse response, Context context) {
		if (request.getParameter("downloadToken") != null && !request.getParameter("downloadToken").toString().equals("")) {
			response.addCookie(new Cookie("fileDownloadToken", request.getParameter("downloadToken").toString()));
		} else if (request.getParameter("downloadToken1") != null && !request.getParameter("downloadToken1").toString().equals("")) {
			response.addCookie(new Cookie("fileDownloadToken", request.getParameter("downloadToken1").toString()));
		} else if (request.getParameter("downloadToken2") != null && !request.getParameter("downloadToken2").toString().equals("")) {
			response.addCookie(new Cookie("fileDownloadToken", request.getParameter("downloadToken2").toString()));
		}
		super.handleExport(request, response, context);
	}
}
