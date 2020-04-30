package it.dpe.igeriv.web.resources;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Classe di utilità che rende disponibili i testi contenuti nel file di language location (package_*.properties).
 * Utlizzato nella Applet.
 * 
 * @author romanom
 *
 */
public class ClientMessageBundle {
	
    private static PropertyResourceBundle languageBundle;

    public static void initialize() {
        languageBundle = (PropertyResourceBundle)
                         ResourceBundle.getBundle("package_client",
                        		 Locale.ITALY,
                                                  ClientMessageBundle.class.getClassLoader());
        if (languageBundle == null) {
        	languageBundle = (PropertyResourceBundle)
            ResourceBundle.getBundle("package_client",
                                     Locale.ITALY,
                                     ClientMessageBundle.class.getClassLoader());
        }
    }

    public static String get(String key) {
        return languageBundle.getString(key);
    }

    private ClientMessageBundle() {}

}
