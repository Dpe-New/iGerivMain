package it.dpe.igeriv.web.extremecomponents;

import org.apache.commons.lang.StringUtils;

public class DpeExportViewUtils {
	private static String HTML_NON_BREAKABLE_SPACE = "&nbsp;";
	private static String XML_NON_BREAKABLE_SPACE = "&#160;";
	
	public static String parseXLS(String value) {
        if (StringUtils.isBlank(value))
            return "";
        
        value = replaceNonBreakingSpaces(value);

        return value;
    }

    public static String parsePDF(String value) {
        if (StringUtils.isBlank(value))
            return "";

        value = replaceNonBreakingSpaces(value);
        value = escapeChars(value);

        return value;
    }

    public static String parseCSV(String value) {
        if (StringUtils.isBlank(value))
            return "";

        value = replaceNonBreakingSpaces(value);

        return value;
    }

    public static String replaceNonBreakingSpaces(String value) {
        if (StringUtils.isBlank(value))
            return "";
       
        if (StringUtils.contains(value, "&nbsp;")) {
        	value = value.replaceAll(HTML_NON_BREAKABLE_SPACE, XML_NON_BREAKABLE_SPACE);
        }

        return value;
    }

    public static String escapeChars(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        
        String tmpString = value.replaceAll(XML_NON_BREAKABLE_SPACE, " ");
        if (StringUtils.contains(tmpString, "&")) {
        	tmpString = StringUtils.replace(value, "&", "&#38;");
        }

        if (StringUtils.contains(tmpString, ">")) {
        	tmpString = StringUtils.replace(value, ">", "&gt;");
        }

        if (StringUtils.contains(tmpString, "<")) {
        	tmpString = StringUtils.replace(value, "<", "&lt;");
        }
        tmpString = tmpString.replaceAll(" ", XML_NON_BREAKABLE_SPACE);
        
        return tmpString;
    }

}
