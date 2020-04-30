package it.dpe.igeriv;


import it.dpe.igeriv.bo.account.AccountService;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.vo.UserVo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/serviceContext.xml","/test/applicationContext.xml","/test/securityContext.xml","/test/webserviceContext.xml"})
public class CreaPwds {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ReflectionSaltSource saltSource;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test 
	public void test() {
		IGerivMessageBundle.initialize();
		Map<String, String> map = buildDataMap();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			UserVo userVo = accountService.getEdicolaByCodice(entry.getKey());
			UserAbbonato user = accountService.buildUserDetails(entry.getKey(), userVo);
			String pwd = passwordEncoder.encodePassword(entry.getValue(), saltSource.getSalt(user));
			userVo.setPwd(pwd);
			userVo.setChangePassword(0);
			userVo.setPwdCriptata(1);
			accountService.saveBaseVo(userVo);
		}
	}

	private Map<String, String> buildDataMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("3340","PEVIANIWEB269");
		return map;
	}
	
}

