package it.dpe.igeriv.filter;

import java.util.List;
import java.util.Map;

/**
 * @author mromano
 *
 */
public interface ListFilter<T> {
	
	/**
	 * @param o
	 */
	public void filter(List<T> list, Map<String, Object> params);
	
}
