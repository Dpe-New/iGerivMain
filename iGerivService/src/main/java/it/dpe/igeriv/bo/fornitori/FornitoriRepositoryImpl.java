package it.dpe.igeriv.bo.fornitori;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.ProdottiNonEditorialiFornitoreVo;
import it.dpe.igeriv.vo.UserVo;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
class FornitoriRepositoryImpl extends BaseRepositoryImpl implements FornitoriRepository {
	
	@Autowired
	FornitoriRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}
	
	@Override 
	public void deleteFornitore(Integer codFornitore, Integer codEdicola) {
		getDao().bulkUpdate("delete from ProdottiNonEditorialiFornitoreVo vo where vo.pk.codFornitore = ? and vo.pk.codEdicola = ?", new Object[]{codFornitore, codEdicola});
	}
	
	@Override
	public List<ProdottiNonEditorialiFornitoreVo> getFornitoriEdicola(Integer codEdicola, String ragioneSociale, String piva, boolean excludeDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiFornitoreVo.class, "pneg");
		criteria.createCriteria("pneg.edicola", "ed");
		criteria.createCriteria("pneg.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("pneg.localita", "loc", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("pneg.provincia", "pro", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ed.codDpeWebEdicola", codEdicola));
		if (excludeDl) {
			criteria.add(Restrictions.ge("pneg.pk.codFornitore", IGerivConstants.COD_INIZIO_FORNITORI_NON_DL));
		}
		if (ragioneSociale != null && !ragioneSociale.equals("")) {
			criteria.add(Restrictions.ilike("pneg.nome", ragioneSociale, MatchMode.START));
		}
		if (piva != null && !piva.equals("")) {
			criteria.add(Restrictions.eq("pneg.piva", piva));
		}
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public ProdottiNonEditorialiFornitoreVo getFornitore(Integer codEdicola, Integer codFornitore) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiFornitoreVo.class, "pneg");
		criteria.createCriteria("pneg.edicola", "ed");
		criteria.createCriteria("pneg.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("pneg.localita", "loc", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("pneg.provincia", "pro", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ed.codDpeWebEdicola", codEdicola));
		criteria.add(Restrictions.eq("pneg.pk.codFornitore", codFornitore));
		criteria.add(Restrictions.eq("pneg.pk.codEdicola", codEdicola));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
}
