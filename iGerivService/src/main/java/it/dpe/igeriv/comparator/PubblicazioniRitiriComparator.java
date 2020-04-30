package it.dpe.igeriv.comparator;

import it.dpe.igeriv.dto.PubblicazioneDto;

import java.util.Comparator;

/**
 * Ordina la lista di pubblicazioni.
 * 
 * @author romanom
 *
 */
public class PubblicazioniRitiriComparator implements Comparator<PubblicazioneDto> {
	
	@Override
	public int compare(PubblicazioneDto o1, PubblicazioneDto o2) {
		int i = 0;
		if (o1 != null && o2 != null) {
			if (o1.getDataVendita() != null && o2.getDataVendita() != null) {
				i = -o1.getDataVendita().compareTo(o2.getDataVendita());
			}
			if (i != 0) {
		    	return i;
		    }
			if (o1.getTitolo() != null && o2.getTitolo() != null) {
				i = o1.getTitolo().compareTo(o2.getTitolo());
			}
			if (i != 0) {
		    	return i;
		    }
		}
		return i;
	}
}