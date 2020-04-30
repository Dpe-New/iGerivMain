package it.dpe.igeriv.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class RestConstants {
	public static final String ID = "RestConstants";
	private static Map nameToValueMap = createNameToValueMap();
	public static final int COD_TIPOLOGIA_MINICARD_VALORE = 99999999;
	
	/**
     * Puts all public static fields via introspection into the resulting Map.
     * Uses the name of the field as key to reference it's in the Map.
     *
     * @return a Map of field names to field values of
     *         all public static fields of this class
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
     */
	@SuppressWarnings("unchecked")
	private static Map createNameToValueMap() {
        Map result = new HashMap();
        Field[] publicFields = IGerivConstants.class.getFields();
        for (int i = 0; i < publicFields.length; i++) {
            Field field = publicFields[i];
            String name = field.getName();
            try {
                result.put(name, field.get(null));
            } catch (Exception e) {
                System.err.println("Initialization of Constants class failed!");
                e.printStackTrace(System.err);
            }
        }
        Class<?>[] classes = IGerivConstants.class.getClasses();
        for (Class clazz : classes) {
	        Field[] pf = clazz.getFields();
	        for (int i = 0; i < pf.length; i++) {
	            Field field = pf[i];
	            String name = field.getName();
	            try {
	                result.put(name, field.get(null));
	            } catch (Exception e) {
	                System.err.println("Initialization of Constants class failed!");
	                e.printStackTrace(System.err);
	            }
	        }
        }
        return result;
    }
	
	/**
     * Gets the Map of all public static fields.
     * The field name is used as key for the value of the field itself.
     *
     * @return the Map of all public static fields
     */
    public static Map getNameToValueMap() {
        return nameToValueMap;
    }
}
