package it.dpe.igeriv.util;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

/**
 * Classe que implementa il truncate di un campo data nella clausola ORDER BY 
 * di una query di tipo Criteria.
 * 
 * @author romanom
 *
 */
public class ProjectionOrder extends Order {
	private static final long serialVersionUID = 1L;
	protected String propertyName;
	private String aggregate;
	private boolean ascending;
	
	public ProjectionOrder(String aggregate, String propertyName, boolean ascending) {
		super(propertyName, ascending);
		this.propertyName = propertyName;
		this.aggregate = aggregate;
		this.ascending = ascending;
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		StringBuffer stringBuffer = new StringBuffer();
		StringBuffer buffer = stringBuffer.append(aggregate).append("(").append(criteriaQuery.getColumn(criteria, propertyName));
		stringBuffer.append(") ");
		if (ascending) {
			stringBuffer.append("asc");
		} else {
			stringBuffer.append("desc");
		}
		return buffer.toString();
	}
	
}
