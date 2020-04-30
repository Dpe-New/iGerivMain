package it.dpe.igeriv.bo.inventario;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.InventarioDto;
import it.dpe.igeriv.dto.InventarioPresuntoDto;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.Types;
import it.dpe.igeriv.util.Types.ContoDepositoType;
import it.dpe.igeriv.vo.InventarioVo;
import it.dpe.igeriv.vo.StoricoCopertineVo;
import it.dpe.igeriv.vo.UserVo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

@Repository
class InventarioRepositoryImpl extends BaseRepositoryImpl implements InventarioRepository {
	
	@Autowired
	InventarioRepositoryImpl(BaseDao<UserVo> dao) {
		super(dao);
	}
	
	@Override
	public List<InventarioDto> getListInventarioDto(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(InventarioVo.class, "inv");
		criteria.add(Restrictions.eq("inv.codEdicola", codEdicola));
		criteria.addOrder(Order.desc("inv.dataInventario"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("inv.idInventario"), "idInventario");
		props.add(Projections.property("inv.dataInventario"), "dataInventario");
		criteria.setProjection(props); 
		criteria.setResultTransformer(Transformers.aliasToBean(InventarioDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public InventarioVo getInventarioVo(Integer codEdicola, Long idInventario) {
		DetachedCriteria criteria = DetachedCriteria.forClass(InventarioVo.class, "inv");
		criteria.createCriteria("inv.dettagli", "det", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("inv.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("inv.idInventario", idInventario));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public void deleteInventario(Long idInventario) {
		getDao().bulkUpdate("delete from InventarioDettaglioVo vo where vo.pk.idInventario = ?", new Object[]{idInventario});
		getDao().bulkUpdate("delete from InventarioVo vo where vo.idInventario = ?", new Object[]{idInventario});
	}
	
	@Override
	public BigDecimal getTotaleInventario(Long idInventario) {
		return getDao().findUniqueResultByQuery("select sum(vo.prezzoEdicola * vo.quantita) from InventarioDettaglioVo vo where vo.pk.idInventario = ?", idInventario);
	}
	
	@Override
	public List<InventarioPresuntoDto> getInventarioPresunto(final Integer[] coddl, final Integer[] codEdicola, final Date dataUscitaDa, final Date dataStorico, final Integer gruppoSconto, final ContoDepositoType filtroContoDeposito, final Types.TipoPubblicazioneType filtroTipoPubblicazione, final Boolean escludiPubblicazioniSenzaPrezzo) {
		HibernateCallback<List<InventarioPresuntoDto>> action = new HibernateCallback<List<InventarioPresuntoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<InventarioPresuntoDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(StoricoCopertineVo.class, "sc");
				criteria.createCriteria("sc.anagraficaPubblicazioniVo", "ap");
				criteria.createCriteria("sc.immagine", "im", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("ap.periodicitaVo", "per", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.in("sc.pk.codDl", coddl));
				criteria.add(Restrictions.or(Restrictions.in("sc.tipoRichiamoResa", new Integer[]{1,2}), Restrictions.isNull("sc.dataRichiamoResa")));
				String dataUscitaStr = DateUtilities.getTimestampAsString(DateUtilities.floorDay(dataUscitaDa), DateUtilities.FORMATO_DATA);
				criteria.add(Restrictions.sqlRestriction("datausc9607 >= to_date(?, '" + DateUtilities.FORMATO_DATA + "')", dataUscitaStr, StringType.INSTANCE));
				String meno15giorni = DateUtilities.getTimestampAsString(DateUtilities.floorDay(DateUtilities.togliGiorni(getSysdate(), 15)), DateUtilities.FORMATO_DATA);
				criteria.add(Restrictions.sqlRestriction("(dare9607 is null or dare9607 >= to_date(?, '" + DateUtilities.FORMATO_DATA + "'))", meno15giorni, StringType.INSTANCE));
				if (filtroTipoPubblicazione.equals(Types.TipoPubblicazioneType.ESCLUDI_PUBBLICAZIONI_SCADUTE)) {
					criteria.add(Restrictions.sqlRestriction("(resp9607 is null or resp9607 = 0)"));
				} else if (filtroTipoPubblicazione.equals(Types.TipoPubblicazioneType.SOLO_PUBBLICAZIONI_SCADUTE)) {
					criteria.add(Restrictions.sqlRestriction("resp9607 > 0"));
				}
				criteria.add(Restrictions.gt("sc.giacenza", 0l));
				criteria.add(Restrictions.ne("per.pk.periodicita", IGerivConstants.PERIODICITA_QUOTIDIANO));
				if (filtroContoDeposito != null && !filtroContoDeposito.equals(ContoDepositoType.TUTTO)) {
					if (filtroContoDeposito.equals(ContoDepositoType.ESCLUDI_CONTO_DEPOSITO)) {
						criteria.add(Restrictions.or(Restrictions.isNull("sc.quantitaCopieContoDeposito"), Restrictions.eq("sc.quantitaCopieContoDeposito", 0)));
					} else if (filtroContoDeposito.equals(ContoDepositoType.SOLO_CONTO_DEPOSITO)) {
						criteria.add(Restrictions.gt("sc.quantitaCopieContoDeposito", 0));
					}
				}
				if (escludiPubblicazioniSenzaPrezzo != null) {
					criteria.add(Restrictions.and(Restrictions.isNotNull("sc.prezzoCopertina"), Restrictions.ne("sc.prezzoCopertina", BigDecimal.ZERO)));
				}
				criteria.addOrder(Order.asc("ap.titolo"));
				ProjectionList props = Projections.projectionList();
				props.add(Projections.property("sc.codicePubblicazione"), "cpu");
				props.add(Projections.property("ap.titolo"), "titolo");
				props.add(Projections.property("sc.sottoTitolo"), "sottoTitolo"); 
				props.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertina");
				props.add(Projections.property("sc.prezzoEdicola"), "prezzoEdicola");
				props.add(Projections.property("sc.numeroCopertina"), "numeroCopertina");
				props.add(Projections.property("sc.dataUscita"), "dataUscita");
				props.add(Projections.property("sc.quantitaCopieContoDeposito"), "quantitaCopieContoDeposito");
				props.add(Projections.property("sc.giacenza"), "giacenzaSP");
				props.add(Projections.property("im.nome"), "nomeImmagine");
				criteria.setProjection(props);
				criteria.setResultTransformer(Transformers.aliasToBean(InventarioPresuntoDto.class));
				List<InventarioPresuntoDto> list = null;
				Integer codEdicola1 = codEdicola[0];
				Integer codEdicola2 = codEdicola.length > 1 ? codEdicola[1] : codEdicola1;
				session.enableFilter("GruppoScontoFilter").setParameter("gruppoSconto", gruppoSconto);
				session.enableFilter("FornitoFilter").setParameter("codEdicola", codEdicola1).setParameter("codEdicola2", codEdicola2); 
				session.enableFilter("StoricoFilter").setParameter("dataStorico", new Timestamp(dataStorico.getTime()));
				list = criteria.list();
				session.disableFilter("GruppoScontoFilter");
				session.disableFilter("FornitoFilter");
				session.disableFilter("StoricoFilter");
				return list;
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
}
