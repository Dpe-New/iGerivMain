package it.dpe.igeriv.bo.agenzie;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.AnagraficaAgenziaDto;
import it.dpe.igeriv.dto.EmailDlDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.BaseVo;
import it.dpe.igeriv.vo.DlVo;
import it.dpe.igeriv.vo.EmailDlVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.UtenteAgenziaVo;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

@Repository
class AgenzieRepositoryImpl extends BaseRepositoryImpl implements AgenzieRepository {
	
	public static final String SEQ_CODDLWEB = "SEQUENZA_9107";
	
	@Autowired
	AgenzieRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}
	
	@Override
	public AnagraficaAgenziaVo getAgenziaByCodice(Integer codFiegDl) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_AGENZIE_BY_ID, codFiegDl);
	}
	
	@Override
	public AnagraficaAgenziaVo getAgenziaByEmail(String email) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ANAGRAFICA_AGENZIE_BY_EMAIL_JOINS, email);
	}
	
	@Override
	public UtenteAgenziaVo getAgenziaByCodiceLogin(Integer codUtente) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_UTENTE_AGENZIA_BY_ID_JOINS, codUtente);
	}
	
	@Override
	public List<AnagraficaAgenziaVo> getAgenzie() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaAgenziaVo.class, "aavo");
		criteria.addOrder(Order.asc("aavo.ragioneSocialeDlPrimaRiga"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<AnagraficaAgenziaVo> getAgenzieConFatturazione() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaAgenziaVo.class, "aavo");
		criteria.add(Restrictions.disjunction().add(Restrictions.isNotNull("aavo.importoMensileDaFatturareDl")).add(Restrictions.isNotNull("aavo.importoMensileDaFatturareDlLite")).add(Restrictions.isNotNull("aavo.importoMensileDaFatturareDliGerivPlus")));
		criteria.addOrder(Order.asc("aavo.ragioneSocialeDlPrimaRiga"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<AnagraficaAgenziaVo> getAgenzieInforiv() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaAgenziaVo.class, "aavo");
		criteria.add(Restrictions.eq("aavo.dlInforiv", true));
		criteria.addOrder(Order.asc("aavo.ragioneSocialeDlPrimaRiga"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<DlVo> getListAgenzie() {
		DetachedCriteria criteria = DetachedCriteria.forClass(DlVo.class, "ag");
		criteria.addOrder(Order.asc("ag.nome"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<AnagraficaAgenziaDto> getListAgenzieDpe() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaAgenziaVo.class, "ag");
		criteria.add(Restrictions.eq("ag.dlInforiv", false));
		criteria.add(Restrictions.isNotNull("ag.ragioneSocialeDlPrimaRiga"));
		criteria.add(Restrictions.ne("ag.codFiegDl", IGerivConstants.COD_DPE_INT));
		criteria.add(Restrictions.ne("ag.codFiegDl", IGerivConstants.CDL_CODE));
		criteria.addOrder(Order.asc("ag.ragioneSocialeDlPrimaRiga"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("ag.codFiegDl"), "codFiegDl");
		props.add(Projections.property("ag.ragioneSocialeDlPrimaRiga"), "ragioneSocialeDlPrimaRiga");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(AnagraficaAgenziaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public String getPasswordDl(Integer codFiegDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaAgenziaVo.class, "aa");
		criteria.add(Restrictions.eq("aa.codFiegDl", codFiegDl));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("aa.passwordDl"));
		criteria.setProjection(props);
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public void updateDataDownload(final Integer codDl, final String fileName, final Integer tipo) {
		HibernateCallback<Integer> action = new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				Query query = arg0.createQuery("update DlRichiamoRifornimentoInvioVo vo set vo.dataDownload = :now where vo.pk.codDl = :codDl and vo.pk.tipo = :tipo and vo.nomeFile = :nomeFile");
				query.setTimestamp("now", getDao().getSysdate());
				query.setInteger("codDl", codDl);
				query.setInteger("tipo", tipo);
				query.setString("nomeFile", fileName);
				return query.executeUpdate();
			}
		};
		getDao().executeByHibernateCallback(action);
	}
	
	@Override
	public List<EmailDlDto> getEmailsDl(Integer codFiegDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EmailDlVo.class, "em");
		criteria.createCriteria("em.anagraficaAgenziaVo", "aa");
		criteria.add(Restrictions.eq("aa.codFiegDl", codFiegDl));
		criteria.addOrder(Order.asc("em.codEmailVo"));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("em.codEmailVo"), "codEmailVo");
		properties.add(Projections.property("em.reparto"), "reparto"); 
		properties.add(Projections.property("em.nome"), "nome");
		properties.add(Projections.property("aa.codFiegDl"), "codFiegDl"); 
		properties.add(Projections.property("em.email"), "email");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(EmailDlDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public AnagraficaAgenziaVo getAgenziaByCodiceDpeWeb(Integer codDpeWebDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaAgenziaVo.class, "ag");
		criteria.add(Restrictions.eq("ag.codDpeWebDl", codDpeWebDl));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	public Long getNewCodDlWeb()
	{
		return getDao().getNextSeqVal(SEQ_CODDLWEB);
	}

	

	@Override
	public List<AnagraficaAgenziaVo> getAgenzieModalitaLocaleFtpInforiv() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaAgenziaVo.class, "aavo");
		criteria.add(Restrictions.eq("aavo.dlInforiv", true));
		criteria.add(Restrictions.eq("aavo.nuovaModalitaftp", new Integer(1)));
		criteria.addOrder(Order.asc("aavo.ragioneSocialeDlPrimaRiga"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<AnagraficaAgenziaVo> getAgenzieModalitaInforiv() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaAgenziaVo.class, "aavo");
		criteria.add(Restrictions.eq("aavo.dlInforiv", true));
		criteria.add(Restrictions.eq("aavo.nuovaModalitaftp", new Integer(2)));
		criteria.addOrder(Order.asc("aavo.ragioneSocialeDlPrimaRiga"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public Boolean isMod2Inforiv(Integer codDl){
		DetachedCriteria criteria = DetachedCriteria.forClass(AnagraficaAgenziaVo.class, "aavo");
		criteria.add(Restrictions.eq("aavo.codFiegDl", codDl));
		criteria.add(Restrictions.eq("aavo.dlInforiv", true));
		criteria.add(Restrictions.eq("aavo.nuovaModalitaftp", new Integer(2)));
		criteria.addOrder(Order.asc("aavo.ragioneSocialeDlPrimaRiga"));
		return (getDao().findByDetachedCriteria(criteria).size()>0)?true:false;
	}
}
