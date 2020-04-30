import it.dpe.igeriv.jms.out.SendGateway;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.util.SpringContextManager;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author mromano
 *
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"/testContext.xml"})
public class TestSendMessage {
	private final Logger log = Logger.getLogger(getClass());
	
	@Autowired 
	ApplicationContext context;
	
	@Autowired
	SendGateway sendGateway;
	
	@Autowired
	SchedulerFactoryBean prodottiExportCronTriggerScheduler;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		Properties systemProps = System.getProperties();
		/*systemProps.put(
				"javax.net.debug", "all"
		);*/
		systemProps.put(
				"javax.net.ssl.keyStore", "C:/apache-activemq-5.6.0/conf/igeriv-server.ks"
		);
		systemProps.put(
				"javax.net.ssl.keyStorePassword", "grvSslPwd2013"
		);
		systemProps.put(
				"javax.net.ssl.trustStore", "C:/apache-activemq-5.6.0/conf/shared.ks"
		);
		System.setProperties(systemProps);
	}
	
	@Test 
	public void test() throws Exception {
		SpringContextManager.setMockContext(context);
		IGerivMessageBundle.initialize();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.SECOND, 5);
	    String hours = "" + cal.get(Calendar.HOUR_OF_DAY);
	    String minutes = "" + cal.get(Calendar.MINUTE);
	    String seconds = "" + cal.get(Calendar.SECOND);
	    String croneExp = seconds + " " + minutes + " " + hours + " * * ? ";
	    Scheduler scheduler = (Scheduler) prodottiExportCronTriggerScheduler.getObject(); 
		//CronTriggerBean prodottiExportCronTrigger = (CronTriggerBean) scheduler.getTrigger("prodottiExportCronTrigger", "DEFAULT");
		//prodottiExportCronTrigger.setCronExpression(croneExp);
		//scheduler.rescheduleJob("prodottiExportCronTrigger", "DEFAULT", prodottiExportCronTrigger);
		prodottiExportCronTriggerScheduler.start();
		Thread.sleep(1000000);
	}
	
}
