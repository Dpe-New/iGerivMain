package it.dpe.igeriv.web.captcha;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.code.jcaptcha4struts2.core.actions.JCaptchaImageAction;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

/**
 * Classe che implementa il risultato struts di tipo Captcha Image.
 *  
 * @author romanom
 *
 */
public class CaptchaImageResult implements Result {

    private static final Log LOG = LogFactory.getLog(CaptchaImageResult.class);
    private static final long serialVersionUID = -595342817673304030L;

    /**
     * Action Execution Result. This will write the image bytes to response stream.
     * 
     * @param invocation
     *            ActionInvocation
     * @throws IOException
     *             if an IOException occurs while writing the image to output stream.
     * @throws IllegalArgumentException
     *             if the action invocation was done by an action which is not the
     *             {@link JCaptchaImageAction}.
     */
    public void execute(ActionInvocation invocation) throws IOException, IllegalArgumentException {

        // Check if the invoked action was JCaptchaImageAction
        if (!(invocation.getAction() instanceof JCaptchaImageAction)) {
            throw new IllegalArgumentException(
                    "CaptchaImageResult expects JCaptchaImageAction as Action Invocation");
        }

        JCaptchaImageAction action = (JCaptchaImageAction) invocation.getAction();
        HttpServletResponse response = ServletActionContext.getResponse();

        // Read captcha image bytes
        byte[] image = action.getCaptchaImage();

        // Send response
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        response.setContentLength(image.length);
        try {
            response.getOutputStream().write(image);
            response.getOutputStream().flush();
        } catch (IOException e) {
            LOG.error("IOException while writing image response for action : " + e.getMessage(), e);
            throw e;
        }
    }

}

