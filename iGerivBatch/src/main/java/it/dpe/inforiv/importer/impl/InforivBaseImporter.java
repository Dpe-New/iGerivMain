package it.dpe.inforiv.importer.impl;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoIdtnInforivVo;
import it.dpe.igeriv.vo.pk.AbbinamentiIdtnInforivPk;

public class InforivBaseImporter {

	/**
	 * @param apdto
	 * @param codFiegDl
	 * @param bo 
	 * @return
	 */
	protected AbbinamentoIdtnInforivVo buildIdtn(String idProdotto, Integer codFiegDl, IGerivBatchService bo) {
		AbbinamentoIdtnInforivVo abii = bo.getAbbinamentoIdtnInforiv(codFiegDl, idProdotto.trim());
		if (abii == null) {
			abii = new AbbinamentoIdtnInforivVo();
			AbbinamentiIdtnInforivPk pk = new AbbinamentiIdtnInforivPk();
			pk.setCodDl(codFiegDl);
			pk.setIdtnInforete(idProdotto.trim());
			abii.setPk(pk);
			abii.setIdtn(bo.getLastId(AbbinamentoIdtnInforivVo.class, "idtn", "pk.codDl", codFiegDl));
		}
		return abii;
	}

	// Vittorio 07/01/2019
	private Integer getIdtn(String idProdotto, Integer codFiegDl, IGerivBatchService bo) {
		Integer idtn = 0;
		if (codFiegDl.equals(IGerivConstants.MORANDINI_CODE)) {
			idtn = Integer.valueOf(idProdotto.trim());
		} else {
			idtn = bo.getLastId(AbbinamentoIdtnInforivVo.class, "idtn", "pk.codDl", codFiegDl);
		}
		return idtn;
	}
}
