package it.dpe.igeriv.web.extremecomponents;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;

import it.dpe.igeriv.dto.VisitableDto;
import it.dpe.igeriv.dto.VisitorDto;
import it.dpe.igeriv.util.DPEWebContants;
import it.dpe.igeriv.web.extremecomponents.visitor.RowInterceptorVisitor;

/**
 * @author romanom
 *
 */
public class DpeRowInterceptorAttributeHref implements RowInterceptor {
	
	public void modifyRowAttributes(TableModel model, Row row) { 
		VisitableDto currentRowBean = (VisitableDto) model.getCurrentRowBean();
		
		String onclick = row.getOnclick();
		if (onclick != null && !onclick.equals(DPEWebContants.BLANK) && onclick.contains("(") && onclick.contains(")")) {
	    	try {
				List<String> paramList = null;
				String jsFunctionName = onclick.substring(0, onclick.indexOf("("));
				String jsParams = onclick.substring(onclick.indexOf("(") + 1, onclick.indexOf(")"));
				paramList = new ArrayList<String>(); 
				StringTokenizer st = new StringTokenizer(jsParams,",");
				if (currentRowBean != null) {
					while (st.hasMoreTokens()) {
						String token = st.nextToken();
						String prop = BeanUtils.getProperty(currentRowBean, token);
						if (prop != null && !prop.equals(DPEWebContants.BLANK)) {
							String property = prop.replaceAll("'", "\\\\'").replaceAll(" ", "&nbsp;");
							paramList.add("'" + property + "'");
						}
					}
					if (!paramList.isEmpty()) {
						String params = paramList.toString().substring(1, (paramList.toString().length() - 1));
						onclick = jsFunctionName + "(" + params + ")";
						row.setOnclick(onclick);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		VisitorDto visitor = new RowInterceptorVisitor(model, row);
		currentRowBean.accept(visitor);
		
		String href = (row.getAttribute("href") != null) ? (String)row.getAttribute("href") : DPEWebContants.BLANK;
		if (!href.equals(DPEWebContants.BLANK)) {
			StringBuffer sbHrefParams = new StringBuffer();
			StringBuffer sbHrefValues = new StringBuffer();
			String hrefParams = href.substring(href.indexOf("?") + 1);
			StringTokenizer st = new StringTokenizer(hrefParams,"&");
			int count = 0;
			while (st.hasMoreTokens()) {
				String field = st.nextToken().trim();
				StringTokenizer st1 = new StringTokenizer(field,"=");
				while (st1.hasMoreTokens()) {
					String token = st1.nextToken().trim();
					if ((count % 2) != 0) {
						sbHrefParams.append("=");
						String property = token;
						try {
							property = BeanUtils.getProperty(currentRowBean, token);
						} catch (Exception e) {
							//throw new RuntimeException(e);
						}
						sbHrefParams.append(property);
						sbHrefValues.append(property);
					} else {
						if (count > 0) {
							sbHrefParams.append("&");
						}
						sbHrefParams.append(token);
					}
					count++;
				}
			}
			String firstPartHref = href.substring(0, href.indexOf("?") + 1);
			row.addAttribute("href1", firstPartHref + sbHrefParams.toString());
		}
    }

	@Override
	public void addRowAttributes(TableModel tableModel, Row row) {
		
	} 

}
