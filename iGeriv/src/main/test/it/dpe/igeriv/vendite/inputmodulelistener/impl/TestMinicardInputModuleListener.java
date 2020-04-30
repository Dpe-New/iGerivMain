package it.dpe.igeriv.vendite.inputmodulelistener.impl;

import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.vendite.inputmodulelistener.VenditeInputModuleListener;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/serviceContext.xml","classpath:META-INF/serviceContext.xml","/test/applicationContext.xml","/test/securityContext.xml","/test/webserviceContext.xml"})
public class TestMinicardInputModuleListener extends TestCase {
	
	@Autowired
	@Qualifier("SubscriptionMinicardInputModuleListener")
	private VenditeInputModuleListener minicardInputModuleListener;
	
	@Test
	public void testExecute() {
		IGerivMessageBundle.initialize();
		VenditeParamDto params = new VenditeParamDto();
		try {
			params.setIdEdicola(878);
            params.setCodFiegDl(177);
            params.setCodEdicolaDl(447);
			minicardInputModuleListener.execute(params);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
		assertNotNull(params.getToken());
		
	}
	
}
