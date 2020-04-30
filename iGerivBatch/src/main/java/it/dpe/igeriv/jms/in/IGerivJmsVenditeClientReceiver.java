package it.dpe.igeriv.jms.in;

import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.bo.vendite.VenditeService;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.jms.receive.ReplyReceiver;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.StringUtility;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.jms.dto.VenditaDetailJmsMessage;
import it.dpe.jms.dto.VenditaJmsMessage;
import it.dpe.mail.MailingListService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * @author romanom
 * 
 */
@Component("IGerivJmsVenditeClientReceiver")
public class IGerivJmsVenditeClientReceiver implements ReplyReceiver<VenditaJmsMessage> {
	private final Logger log = Logger.getLogger(getClass());
	private final AccountService accountService;
	private final VenditeService venditeService;
	private final MailingListService mailingListService;
	
	@Autowired
	IGerivJmsVenditeClientReceiver(AccountService accountService, VenditeService venditeService, MailingListService mailingListService) {
		this.accountService = accountService;
		this.venditeService = venditeService;
		this.mailingListService = mailingListService;
	}

	@Override
	public void receive(Message<VenditaJmsMessage> message) {
		try {
			String crivw = null;
			try {
				if (message.getPayload() != null) {
					VenditaJmsMessage payload = message.getPayload();
					crivw = payload.getCrivw();
					List<VenditeParamDto> params = buildVenditeParamDto(payload.getDettagli());
					UserVo utente = accountService.getEdicolaByCodice(payload.getCrivw());
					UserAbbonato user = accountService.buildUserDetails(payload.getCrivw(), utente);
					venditeService.chiudiConto(params, null, null, payload.getImportoTotale(), null, "false", "true", null, null, null, user.getCodEdicolaMaster(), user.getCodFiegDlMaster(), user.getCodUtente(), user.getCodEdicolaDl(), user.getArrId(), user.isMultiDl(), user.getArrCodFiegDl(), user.getId());
				}
	    	} catch (Exception e) {
				log.error(MessageFormat.format(IGerivMessageBundle.get("error.ricezione.vendite.client"), crivw), e);
	    		String subject = MessageFormat.format(IGerivMessageBundle.get("error.ricezione.vendite.client.msg.subject"), crivw);
				String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.ricezione.vendite.client.msg.body"), (Strings.isNullOrEmpty(e.getMessage()) ? "" : e.getMessage()), StringUtility.getStackTrace(e));
				mailingListService.sendEmail(null, subject, emailMsg, true);
	    	}
		} catch (Throwable e) {
			String subject = IGerivMessageBundle.get("error.jms.fatal.unexpected.subject");
			String emailMsg = MessageFormat.format(IGerivMessageBundle.get("error.jms.fatal.unexpected.body"), (e.getMessage() == null ? "" : e.getMessage()), StringUtility.getStackTrace(e));
			mailingListService.sendEmail(null, subject, emailMsg, true);
		}
	}

	/**
	 * @param dettagli
	 * @return
	 */
	private List<VenditeParamDto> buildVenditeParamDto(List<VenditaDetailJmsMessage> dettagli) {
		List<VenditeParamDto> list = new ArrayList<VenditeParamDto>();
		for (VenditaDetailJmsMessage det : dettagli) {
			VenditeParamDto dto = new VenditeParamDto();
			dto.setProdottoNonEditoriale(false);
			dto.setProgressivo(det.getProgressivo());
			dto.setIdtn(det.getIdtn());
			dto.setCodFiegDlPubblicazione(det.getCodFiegDlPubblicazione());
			dto.setImporto(det.getImporto());
			dto.setTitolo(det.getTitolo());
			dto.setSottoTitolo(det.getSottoTitolo());
			dto.setNumeroCopertina(det.getNumeroCopertina());
			dto.setQuantita(det.getQuantita());
			dto.setProdottoNonEditoriale(false);
			list.add(dto);
		}
		return list;
	}
	
}
