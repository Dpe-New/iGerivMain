package it.dpe.igeriv.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.spring.properties.EncryptablePropertyPlaceholderConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class ExposableEncryptablePropertyPlaceholderConfigurer /*extends
	EncryptablePropertyPlaceholderConfigurer */{
	
	public ExposableEncryptablePropertyPlaceholderConfigurer(StringEncryptor stringEncryptor) {
		//super(stringEncryptor);
	}

	private Map<String, String> resolvedProps;

	/*@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		resolvedProps = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			resolvedProps.put(keyStr, parseStringValue(props.getProperty(keyStr), props, new HashSet()));
		}
	}*/

	public Map<String, String> getResolvedProps() {
		return Collections.unmodifiableMap(resolvedProps);
	}
}
