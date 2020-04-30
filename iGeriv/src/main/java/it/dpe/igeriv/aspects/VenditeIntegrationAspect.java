package it.dpe.igeriv.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import it.dpe.igeriv.vo.VisitableVo;
import it.dpe.igeriv.vo.VisitorVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Aspect
@Order(10)
public class VenditeIntegrationAspect {
	private final Logger log = Logger.getLogger(getClass());
	private final VisitorVo visitorVo;
	
	@Autowired
	VenditeIntegrationAspect(VisitorVo visitorVo) {
		this.visitorVo = visitorVo;
	}
	
	@Around("(execution(* it.dpe..*VenditeService*.chiudiConto*(..))) || execution(* it.dpe..*VenditeService*.deleteVenditaVo*(..)))")
	public Object doCall(ProceedingJoinPoint pjp) throws Throwable {
		Signature signature = pjp.getSignature();
		Object[] args = pjp.getArgs();
	    Object retVal = pjp.proceed();
	    VisitableVo v = (retVal != null && retVal instanceof VisitableVo ? (VisitableVo) retVal : (args != null && args.length > 0 && args[0] instanceof VisitableVo) ? (VisitableVo) args[0] : null);
	    if (v != null) {
	    	v.setMethodSignature(signature.getName());
	    	((VisitableVo) v).accept(visitorVo);
	    }
	    return retVal;
	}
	
}
