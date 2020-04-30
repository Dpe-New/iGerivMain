package it.dpe.igeriv.jms.error;

import org.springframework.integration.Message;
import org.springframework.integration.message.ErrorMessage;
import org.springframework.stereotype.Component;

/**
 * Convertitore di messaggi utilizzato nella ricezione dei messaggi
 * di errore da parte della classe <see>it.dpe.rtae.jms.error.RtaeJmsErrorMessageHandler</see>.
 * 
 * @author romanom
 *
 */
@Component("errorUnwrapper")
public class IGerivErrorUnwrapper {
	
	public Message<?> transform(ErrorMessage errorMessage) {
		//Message<?> failedMessage = ((MessagingException) errorMessage.getPayload()).getFailedMessage();
		return errorMessage;
	 }
}
