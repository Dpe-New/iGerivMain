package it.dpe.igeriv.visitor;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.igeriv.vo.MessaggioClienteVo;
import it.dpe.igeriv.vo.VenditaVo;
import it.dpe.igeriv.vo.VisitorVo;
import it.dpe.jms.service.JmsService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component("IntegrationVisitor")
public class IntegrationVisitor implements VisitorVo {
	private final Logger log = Logger.getLogger(getClass());
	private final JmsService<VenditaVo> jmsService;
	private final MBeanServerConnection mBeanServerConnection;

	@Autowired
	IntegrationVisitor(JmsService<VenditaVo> jmsService, MBeanServerConnection mBeanServerConnection) {
		this.jmsService = jmsService;
		this.mBeanServerConnection = mBeanServerConnection;
	}

	@Override
	public void visit(VenditaVo vo) {
		String queueName = null;
		try {
			queueName = SpringContextManager.getService("venditeSendDestination") != null ? SpringContextManager.getService("venditeSendDestination").toString().replaceFirst("queue://", "") : null;
			if (queueName != null && getConsumerCount(queueName) > 0l) {
				jmsService.send(vo);
			}
		} catch (NoSuchBeanDefinitionException e) {}
	}

	/**
	 * Ritorna il numero di consumers di una queue
	 * 
	 * @param queueName Nome fisico della queue (es. igeriv.vendite.177.queue)
	 * @return
	 */
	public Long getConsumerCount(String queueName) {
		Long consumerCount = 0l;
		try {
			ObjectName objectNameRequest = new ObjectName("org.apache.activemq:BrokerName=igeriv-broker,Type=Queue,Destination=" + queueName);
			Object attribute = objectNameRequest != null ? mBeanServerConnection.getAttribute(objectNameRequest, "ConsumerCount") : null;
			consumerCount = attribute != null ? new Long(attribute.toString()) : 0l;
		} catch (IOException e) {
			// logga soltanto se la exception non e' causata dal servizio ActiveMQ non disponibile
			if (e.getCause() == null || !(e.getCause() instanceof ServiceUnavailableException)) {
				log.error("IOException in IntegrationVisitor.getConsumerCount()", e);
			}
		} catch (Exception e) {
			log.error("Exception in IntegrationVisitor.getConsumerCount()", e);
		}
		return consumerCount;
	}

	@Override
	public void visit(Object dto) {

	}

	@Override
	public void visit(MessaggioClienteVo dto) {

	}

}
