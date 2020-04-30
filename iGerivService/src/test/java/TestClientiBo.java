import it.dpe.igeriv.bo.agenzie.AgenzieService;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"file:src/main/resources/testApplicationContext.xml"
})
public class TestClientiBo extends TestCase {
	
	@BeforeClass
	public static void setUpTest() {
	}
	
	@Autowired
	private AgenzieService agenzieService;
	
	@Test 
	public void testGetAgenziaByCodiceDpeWeb() {
		AnagraficaAgenziaVo agenziaByCodiceDpeWeb = agenzieService.getAgenziaByCodiceDpeWeb(123123);
		System.out.println(agenziaByCodiceDpeWeb);
	}
	
}
