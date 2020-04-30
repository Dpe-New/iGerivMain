package it.dpe.igeriv.cleaner.resources;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * Classe di utilità che rende disponibili i testi contenuti nel file di language location (package_*.properties)
 * in qualisasi altra classe al di fuori delle actions di struts.
 * 
 * @author romanom
 *
 */
public class IGerivMessageBundle {
	private static final Logger log = Logger.getLogger(IGerivMessageBundle.class);
    private static PropertyResourceBundle languageBundle;

    public static void initialize() {
    	log.debug("IGerivMessageBundle.initialize start");
    	try {
	        languageBundle = (PropertyResourceBundle)
	                         ResourceBundle.getBundle("package",
	                                                  Locale.getDefault(),
	                                                  IGerivMessageBundle.class.getClassLoader());
	        log.debug("languageBundle=" + languageBundle.toString());
	        if (languageBundle == null) {
	        	setPackageIt();
	        }
	        log.debug("languageBundle=" + languageBundle.toString());
    	} catch (MissingResourceException e) {
    		setPackageIt();
    	}
    	log.debug("IGerivMessageBundle.initialize end");
    }

	private static void setPackageIt() {
		log.debug("IGerivMessageBundle.setPackageIt start");
		languageBundle = (PropertyResourceBundle)
	    ResourceBundle.getBundle("package",
	                             Locale.ITALY,
	                             IGerivMessageBundle.class.getClassLoader());
		log.debug("IGerivMessageBundle.setPackageIt end");
	}

    public static String get(String key) {
        return languageBundle.getString(key);
    }

    private IGerivMessageBundle() {}

}
