package it.dpe.igeriv.web.extremecomponents;

import java.util.Iterator;

import javax.servlet.jsp.JspException;

import org.extremecomponents.util.ExceptionUtils;

import it.dpe.igeriv.dto.EstrattoContoDinamicoDto;

public class DpeEstrattoContoTableTag extends DpeTableTag {
	private static final long serialVersionUID = 1L;
	private Iterator<EstrattoContoDinamicoDto> iterator;
	
	@SuppressWarnings("unchecked")
	@Override
	public int doAfterBody() throws JspException {
		try {
            if (iterator == null) {
                model.execute();
				iterator = model.getCollectionOfBeans().iterator();
            }
            if (iterator != null && iterator.hasNext()) {
                Object bean = iterator.next();
                model.setCurrentRowBean(bean);
                return EVAL_BODY_AGAIN;
            }
        } catch (Exception e) {
            throw new JspException("TableTag.doAfterBody() Problem: " + ExceptionUtils.formatStackTrace(e));
        }

        return SKIP_BODY;
	}
	
	@Override
	public void doFinally() {
        iterator = null;
        super.doFinally();
    }
	
}
