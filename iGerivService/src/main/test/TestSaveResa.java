import it.dpe.igeriv.bo.edicole.EdicoleService;
import it.dpe.igeriv.bo.prodotti.ProdottiService;
import it.dpe.igeriv.vo.ProdottiNonEditorialiResaDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiResaVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiResaDettaglioPk;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/serviceContext.xml",
		"file:src/main/resources/testApplicationContext.xml"
})
public class TestSaveResa extends TestCase {
	
	@Autowired
	private ProdottiService prodottiService;
	
	@Autowired
	private EdicoleService edicoleService;
	
	@Test 
	public void testSave() {
		ProdottiNonEditorialiResaVo resaProdottiVariEdicola = prodottiService.getResaProdottiVariEdicola(73l);
		List<ProdottiNonEditorialiResaDettaglioVo> dettagli = resaProdottiVariEdicola.getDettagli();
		for (ProdottiNonEditorialiResaDettaglioVo vo : dettagli) {
			System.out.println(vo.getPk());
		}
	}
	
}
