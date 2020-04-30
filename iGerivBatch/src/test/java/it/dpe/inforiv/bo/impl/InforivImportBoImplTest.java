package it.dpe.inforiv.bo.impl;

import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.util.SpringContextManager;
import it.dpe.inforiv.bo.InforivImportBo;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class InforivImportBoImplTest {
	
	@Before
	public void Setup() {
		String path = "C:/aplic/iGeriv/WebContent/WEB-INF/config/";
		ApplicationContext context = new FileSystemXmlApplicationContext(new String[]{
				path + "applicationContext.xml",
				path + "beansContext.xml",
				path + "customBeansContext.xml",
				path + "securityContext.xml",
				path + "jmsContext.xml",
				path + "integrationContext.xml",
				path + "webserviceContext.xml"
				});
		SpringContextManager.setMockContext(context);
	}
	
	@Test
	public void importEdicolaInforiv() {
		InforivImportBo bo = (InforivImportBo) SpringContextManager.getService("InforivImportBo");
		EdicolaDto edicola = new EdicolaDto();
		edicola.setCap("20100");
		edicola.setCodDl(999);
		edicola.setCodEdicolaDl(88);
		edicola.setCodFiscale("d676dfs76dfd");
		edicola.setEmail("marcello.romano@dpe.it");
		edicola.setFax("33333");
		edicola.setFtpPwd("test");
		edicola.setFtpUser("test");
		edicola.setIndirizzo("via Vallarsa 10");
		edicola.setLocalita("Milano");
		edicola.setPiva("34242342");
		edicola.setTelefono("6456546546");
		edicola.setRagioneSociale1("test");
		edicola.setProvincia("Mi");
		try {
			bo.importEdicolaInforiv(edicola);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
