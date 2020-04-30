package it.dpe.igeriv.web.captcha;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.code.jcaptcha4struts2.core.PluginConstants;
import com.google.code.jcaptcha4struts2.core.validation.JCaptchaValidator;
import com.octo.captcha.service.CaptchaServiceException;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Intercepter which automatically validates JCaptcha responses.
 * <p>
 * <b>Note :</b> Actions intercepted by this intercepter are <b>required</b> to implement
 * {@link ValidationAware} or should be a subclass of a class which implements it (such as
 * {@code ActionSupport}).
 * 
 * @author Yohan Liyanage
 * @since 2.0
 * @version 2.0
 */
public class JCaptchaValidationIntercepter extends AbstractInterceptor {

    private static final long serialVersionUID = 3410862000200098945L;
    private static final Log LOG = LogFactory.getLog(JCaptchaValidationIntercepter.class);

    /**
     * Intercepts invocation and adds a field error if captcha validation fails.
     * <p>
     * Note that intercepting an action which does not implement {@link ValidationAware} or an
     * action invocation which does not contain captcha information will result in an exception.
     * 
     * @param invocation
     *            ActionInvocation
     * 
     * @return action forward string
     * 
     * @throws Exception
     *             if thrown by action invocation chain
     * @throws IllegalArgumentException
     *             if the target action does not implement {@link ValidationAware}.
     */
    public String intercept(ActionInvocation invocation) throws Exception, IllegalArgumentException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("JCaptchaValidationInterceptor intercepting...");
        }

        if (!(invocation.getAction() instanceof ValidationAware)) {
            // Action does not implement ValidationAware, which is a requirement
            // if JCaptchaValidationIntercepter is being used to intercept an action.

            LOG.error("JCaptchaValidationInterceptor intercepted action which does "
                    + "not implement ValidationAware");

            throw new IllegalArgumentException("Action does not implement ValidationAware");
        }

        String response =
                ServletActionContext.getRequest().getParameter(PluginConstants.J_CAPTCHA_RESPONSE);
        
        // Validate and add field error if fails
        try {
	        if (response != null && !JCaptchaValidator.validate()) {
	            
	            if (LOG.isDebugEnabled()) {
	                LOG.debug("JCaptchaValidationInterceptor validation failed");
	            }
	            // Get Action Reference
	            ValidationAware action = (ValidationAware) invocation.getAction();
	            
	            String validationMessage = "Entered string does not match with image";
	            if (action instanceof ActionSupport) {
	            	ActionSupport actionSupp = (ActionSupport) action;
	            	validationMessage = actionSupp.getText("gp.captcha.error");
	            }
	            
				action.addFieldError(PluginConstants.J_CAPTCHA_RESPONSE, validationMessage);
	            return Action.INPUT;
	        }
        } catch (CaptchaServiceException e) {
        	// Get Action Reference
            ValidationAware action = (ValidationAware) invocation.getAction();
            
            String validationMessage = "Entered string does not match with image";
            if (action instanceof ActionSupport) {
            	ActionSupport actionSupp = (ActionSupport) action;
            	validationMessage = actionSupp.getText("gp.captcha.error");
            }
            
			action.addFieldError(PluginConstants.J_CAPTCHA_RESPONSE, validationMessage);
            return Action.INPUT;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("JCaptchaValidationInterceptor validation completed successfully.");
        }

        // Continue invocation
        return invocation.invoke();
    }
   
}
