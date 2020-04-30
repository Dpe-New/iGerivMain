package it.dpe.igeriv.bo.note;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.vo.NoteRivenditaPerCpuVo;
import it.dpe.igeriv.vo.NoteRivenditaVo;
import it.dpe.igeriv.vo.UserVo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
class NoteRepositoryImpl extends BaseRepositoryImpl implements NoteRepository {
	
	@Autowired
	NoteRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}

	@Override
	public NoteRivenditaVo getNoteRivenditaVo(Integer idtn, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(NoteRivenditaVo.class, "nr");
		criteria.add(Restrictions.eq("nr.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("nr.pk.idtn", idtn));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public NoteRivenditaPerCpuVo getNoteRivenditaPerCpuVo(Integer cpu, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(NoteRivenditaPerCpuVo.class, "nr");
		criteria.add(Restrictions.eq("nr.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("nr.pk.cpu", cpu));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	
}
