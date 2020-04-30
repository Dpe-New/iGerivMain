package it.dpe.igeriv.vo;

import java.util.Locale;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;



public class BasetTextVo extends BaseVo implements LocaleProvider {
	private static final long serialVersionUID = 1L;
	protected TextProvider textProvider;
	
	protected TextProvider getTextProvider() {
        if (textProvider == null) {
            TextProviderFactory tpf = new TextProviderFactory();
            textProvider = tpf.createInstance(getClass(), this);
        }
        return textProvider;
    }
	
	public Locale getLocale() {
        ActionContext ctx = ActionContext.getContext();
        return ctx!=null ? ctx.getLocale() : null;
    }
	public String getText(String textId) {
        return getTextProvider().getText(textId);
    }

}
