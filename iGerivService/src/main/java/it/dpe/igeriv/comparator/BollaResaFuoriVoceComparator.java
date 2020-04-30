package it.dpe.igeriv.comparator;

import it.dpe.igeriv.vo.BollaResa;

import java.util.Comparator;

import org.springframework.stereotype.Component;

/**
 * @author Marcello Romano (marcello74@gmail.com)
 * 
 */
@Component("BollaResaFuoriVoceComparator")
public class BollaResaFuoriVoceComparator<T extends BollaResa> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		if (o1 != null && o2 != null && o1.getNumeroPubblicazione() != null && o2.getNumeroPubblicazione() != null) {
			return - o1.getNumeroPubblicazione().trim().compareTo(o2.getNumeroPubblicazione().trim());
		}
		return 0;
	}

}
