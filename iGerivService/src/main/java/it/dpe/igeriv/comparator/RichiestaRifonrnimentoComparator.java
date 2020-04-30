package it.dpe.igeriv.comparator;

import it.dpe.igeriv.vo.StoricoCopertineVo;

import java.util.Comparator;

public class RichiestaRifonrnimentoComparator implements Comparator<StoricoCopertineVo> {

	@Override
	public int compare(StoricoCopertineVo o1, StoricoCopertineVo o2) {
		if (o1 != null && o2 != null) {
			return o1.getDataUscita().compareTo(o2.getDataUscita());
		}
		return 0;
	}

}
