package it.dpe.igeriv.comparator;

import it.dpe.igeriv.vo.MenuModuloVo;

import java.util.Comparator;

/**
 * Ordina la lista dei moduli.
 * 
 * @author romanom
 *
 */
public class ModuliComparator implements Comparator<MenuModuloVo> {
	
	@Override
	public int compare(MenuModuloVo o1, MenuModuloVo o2) {
		int i = 0;
		if (o1 != null && o2 != null) {
			if (o1.getPosizioneMenu() != null && o2.getPosizioneMenu() != null) {
				i = o1.getPosizioneMenu().compareTo(o2.getPosizioneMenu());
			}
			if (i != 0) {
		    	return i;
		    }
			if (o1.getIdModuloPadre() != null && o2.getIdModuloPadre() != null) {
				i = o1.getIdModuloPadre().compareTo(o2.getIdModuloPadre());
			}
			if (i != 0) {
		    	return i;
		    }
			if (o1.getPosizioneItem() != null && o2.getPosizioneItem() != null) {
				i = o1.getPosizioneItem().compareTo(o2.getPosizioneItem());
			}
			if (i != 0) {
		    	return i;
		    }
			}
		return i;
	}
}