package it.dpe.igeriv.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import lombok.Getter;
import lombok.Setter;

@Component("filterListWrappper")
public class FilterListWrappper extends GenericFilterBean {
	private List<GenericFilterBean> filterList = new ArrayList<GenericFilterBean>();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		FilterChain filterChain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest arg0, ServletResponse arg1) throws IOException, ServletException {
				chain.doFilter(arg0, arg1);
			}
		};
		Vector<FilterChain> filterChains = new Vector<FilterChain>();
		filterChains.add(filterChain);
		for (final GenericFilterBean filter : filterList) {
			final FilterChain lastChain = filterChains.lastElement();
			FilterChain loopChain = new FilterChain() {
				@Override
				public void doFilter(ServletRequest arg0, ServletResponse arg1) throws IOException, ServletException {
					filter.doFilter(arg0, arg1, lastChain);
				}
			};
			filterChains.add(loopChain);
		}
		filterChains.lastElement().doFilter(request, response);
	}
	public void setFilterList(List<GenericFilterBean> filterList) {
		this.filterList = filterList;
	}
	
	public List<GenericFilterBean> getFilterList() {
		return filterList;
	}

}
