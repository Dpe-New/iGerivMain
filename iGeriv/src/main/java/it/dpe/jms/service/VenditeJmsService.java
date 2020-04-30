package it.dpe.jms.service;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.project;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.sum;
import static org.hamcrest.Matchers.notNullValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import ch.lambdaj.group.Group;
import it.dpe.igeriv.util.JmsConstants;
import it.dpe.igeriv.vo.VenditaDettaglioVo;
import it.dpe.igeriv.vo.VenditaVo;
import it.dpe.jms.dto.VenditaJmsMessage;
import it.dpe.jms.dto.VenditeJmsMessage;
import it.dpe.jms.factory.JmsRequest;
import it.dpe.jms.factory.JmsRequestFactory;
import it.dpe.jms.send.SendGateway;
import it.dpe.service.mail.MailingListService;

@Component("VenditeJmsService")
public class VenditeJmsService extends BaseJmsService<VenditaJmsMessage> implements JmsService<VenditaVo>  {
	
	@Autowired
	VenditeJmsService(SendGateway<VenditaJmsMessage> sendGateway, MailingListService mailingListService) {
		super(sendGateway, mailingListService);
	}
	
	@Override
	public void send(VenditaVo vendita) {
		JmsRequest<VenditaJmsMessage> venditeJmsRequest = JmsRequestFactory.prepareVenditeJmsRequest(vendita.getMethodSignature());
		List<VenditaDettaglioVo> dettagliVendita = select(vendita.getListVenditaDettaglio(), notNullValue());
		if (dettagliVendita != null && !dettagliVendita.isEmpty()) {
			List<VenditeJmsMessage> list = project(dettagliVendita, VenditeJmsMessage.class, on(VenditaDettaglioVo.class).getIdtn(), on(VenditaDettaglioVo.class).getPk().getVenditaVo().getDataVendita(), on(VenditaDettaglioVo.class).getImportoTotale(), on(VenditaDettaglioVo.class).getQuantita(), on(VenditaDettaglioVo.class).getPrezzoCopertina(), on(VenditaDettaglioVo.class).getTitolo());
			List<VenditeJmsMessage> venditeList = buildVenditeList(list);
			String operation = !Strings.isNullOrEmpty(vendita.getMethodSignature()) && vendita.getMethodSignature().contains("save") ? JmsConstants.OPERATION_INSERT : !Strings.isNullOrEmpty(vendita.getMethodSignature()) && vendita.getMethodSignature().contains("update") ? JmsConstants.OPERATION_UPDATE : !Strings.isNullOrEmpty(vendita.getMethodSignature()) && vendita.getMethodSignature().contains("delete") ? JmsConstants.OPERATION_DELETE : null;
			if (!venditeList.isEmpty()) {
				VenditaJmsMessage messageVendita = new VenditaJmsMessage(operation, vendita.getCodFiegDl(), vendita.getCodEdicolaDl(), vendita.getCodVendita(), vendita.getCorrelationId(), venditeList);
				send(messageVendita, venditeJmsRequest);
			}
		}
	}

	/**
	 * Raggruppa le vendite per idtn e somma le quantita' e calcolo il prezzo totale
	 * 
	 * @param list
	 * @return
	 */
	private List<VenditeJmsMessage> buildVenditeList(List<VenditeJmsMessage> list) {
		List<VenditeJmsMessage> outList = new ArrayList<VenditeJmsMessage>();
		Group<VenditeJmsMessage> group = group(list, by(on(VenditeJmsMessage.class).getIdtn()));
		if (!group.subgroups().isEmpty()) {
			for (Group<VenditeJmsMessage> subgroup : group.subgroups()) {
				VenditeJmsMessage vjm = subgroup.findAll().get(0);
				Integer copie = sum(subgroup.findAll(), on(VenditeJmsMessage.class).getCopieProdotto());
				BigDecimal totaleImporto = sum(subgroup.findAll(), on(VenditeJmsMessage.class).getTotaleImporto());
				vjm.setCopieProdotto(copie);
				vjm.setTotaleImporto(totaleImporto);
				outList.add(vjm);
			}
		}
		return outList;
	}

}
