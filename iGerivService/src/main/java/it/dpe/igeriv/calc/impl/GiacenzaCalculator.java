package it.dpe.igeriv.calc.impl;

import it.dpe.igeriv.calc.Calculator;
import it.dpe.igeriv.dto.BaseDto;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author romanom
 * 
 */
@Component("GiacenzaCalculator")
public class GiacenzaCalculator implements Calculator {
	private final Logger log = Logger.getLogger(getClass());
	
	@Override
	public Number getResult(BaseDto dto) {
		Number result = null;
		try {
			int fornitoInt = BeanUtils.getProperty(dto, "fornito") != null ? new Integer(BeanUtils.getProperty(dto, "fornito").toString()) : 0;
			int resoInt = BeanUtils.getProperty(dto, "reso") != null ? new Integer(BeanUtils.getProperty(dto, "reso").toString()) : 0;
			//int resoInt = BeanUtils.getProperty(dto, "resoRiscontrato") != null ? new Integer(BeanUtils.getProperty(dto, "resoRiscontrato").toString()) : 0;
			int venditeInt = BeanUtils.getProperty(dto, "vendite") != null ? new Integer(BeanUtils.getProperty(dto, "vendite").toString()) : 0;
			int respinto = BeanUtils.getProperty(dto, "respinto") != null ? new Integer(BeanUtils.getProperty(dto, "respinto").toString()) : 0;
			result = (fornitoInt - resoInt - venditeInt) + Math.min(resoInt, respinto);
		} catch (Exception e) {
			log.error("Errore fatale in GiacenzaCalculator", e);
		}
		return result;
	}

}
