package it.dpe.igeriv.bo.inforiv;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.vo.AbbinamentoTipoMovimentoFondoBollaInforivVo;
import it.dpe.igeriv.vo.BollaDettaglioVo;
import it.dpe.igeriv.vo.BollaResaDettaglioVo;
import it.dpe.igeriv.vo.BollaResaFuoriVoceVo;
import it.dpe.igeriv.vo.BollaResaRiassuntoVo;
import it.dpe.igeriv.vo.BollaResaRichiamoPersonalizzatoVo;
import it.dpe.igeriv.vo.BollaRiassuntoVo;
import it.dpe.igeriv.vo.PrenotazioneVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.VenditaDettaglioVo;
import it.dpe.inforiv.dto.output.InforivMancanzeEccedenzeDto;
import it.dpe.inforiv.dto.output.InforivResaDichiarataDto;
import it.dpe.inforiv.dto.output.InforivTotaleBollaConsegnaAccertataDto;
import it.dpe.inforiv.dto.output.InforivTotaleBollaResaEdicolaDto;
import it.dpe.inforiv.dto.output.InforivVariazioniServizioDto;
import it.dpe.inforiv.dto.output.InforivVenditeDto;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

/**
 * Implementazione dell'interfaccia BO per l'esportazione dati inforiv. 
 * 
 * @author romanom
 * 
 */
@SuppressWarnings("unchecked")
@Repository("InforivExportBo")
class InforivExportBoImpl extends BaseRepositoryImpl implements InforivExportBo {
	
	@Autowired
	InforivExportBoImpl(BaseDao<?> dao) {
		super(dao);
	}
	
	@Override
	public List<InforivMancanzeEccedenzeDto> getBollaDettaglioConDifferenze(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaDettaglioVo.class, "ce");
		criteria.add(Restrictions.eq("ce.pk.codFiegDl", codFiegDl));
		if (codEdicola != null) {
			criteria.add(Restrictions.eq("ce.pk.codEdicola", codEdicola));
		}
		if (dataBolla != null) {
			criteria.add(Restrictions.eq("ce.pk.dtBolla", dataBolla));
		}
		if (tipoBolla != null) {
			criteria.add(Restrictions.eq("ce.pk.tipoBolla", tipoBolla));
		}
		criteria.add(Restrictions.ne("ce.differenze", IGerivConstants.COD_RICHIEDERE_DIFFERENZA_DL_NO));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("ce.pk.codFiegDl"), "codFiegDl");
		props.add(Projections.property("ce.pk.codEdicola"), "codEdicola");
		props.add(Projections.property("ce.pk.dtBolla"), "dataBolla");
		props.add(Projections.property("ce.pk.tipoBolla"), "tipoBolla");
		props.add(Projections.property("ce.differenze"), "differenze");
		props.add(Projections.property("ce.idtnTrascodifica"), "idtnTrascodifica");
		props.add(Projections.property("ce.quantitaConsegnata"), "fornito");
		props.add(Projections.property("ce.note"), "note");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(InforivMancanzeEccedenzeDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}

	@Override
	public List<InforivTotaleBollaConsegnaAccertataDto> getBolleRiassuntoDaTrasmettereDl(Integer codFiegDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaRiassuntoVo.class, "ce");
		criteria.add(Restrictions.eq("ce.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ce.bollaTrasmessaDl", IGerivConstants.INDICATORE_BOLLA_DA_TRASMETTERE));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("ce.pk.codFiegDl"), "codFiegDl");
		props.add(Projections.property("ce.pk.codEdicola"), "codEdicola");
		props.add(Projections.property("ce.pk.dtBolla"), "dataBolla");
		props.add(Projections.property("ce.pk.tipoBolla"), "tipoBolla");
		props.add(Projections.property("ce.totaleCopieConsegnate"), "totaleCopieConsegnate");
		props.add(Projections.property("ce.totaleDifferenze"), "totaleDifferenze");
		props.add(Projections.property("ce.valoreBolla"), "totaleBollaConsegnaBd");
		//props.add(Projections.property("ce.valoreFondoBolla"), "valoreFondoBollaBd");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(InforivTotaleBollaConsegnaAccertataDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<InforivResaDichiarataDto> getBollaResaDettaglio(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaDettaglioVo.class, "ce");
		criteria.createCriteria("ce.storicoCopertineVo", "sc");
		criteria.createCriteria("ce.bollaResa", "br");
		criteria.createCriteria("br.numeroRichiamo", "nr", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ce.pk.codFiegDl", codFiegDl));
		if (codEdicola != null) {
			criteria.add(Restrictions.eq("ce.pk.codEdicola", codEdicola));
		}
		if (dataBolla != null) {
			criteria.add(Restrictions.eq("ce.pk.dtBolla", dataBolla));
		}
		if (tipoBolla != null) {
			criteria.add(Restrictions.eq("ce.pk.tipoBolla", tipoBolla));
		}
		criteria.add(Restrictions.gt("ce.reso", 0));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("ce.pk.codFiegDl"), "codFiegDl");
		props.add(Projections.property("ce.pk.codEdicola"), "codEdicola");
		props.add(Projections.property("ce.pk.dtBolla"), "dataBolla");
		props.add(Projections.property("ce.pk.tipoBolla"), "tipoBolla");
		props.add(Projections.property("nr.pk.tipoRichiamoResa"), "causaliResa");
		props.add(Projections.property("nr.descrizione"), "descrizioneRichiamoResa");
		props.add(Projections.property("br.riga"), "numeroRiga");
		props.add(Projections.property("ce.idtnTrascodifica"), "idtnTrascodifica");
		props.add(Projections.property("ce.reso"), "copie");
		props.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertinaBd");
		props.add(Projections.property("ce.prezzoNetto"), "prezzoNettoBd");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(InforivResaDichiarataDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<InforivResaDichiarataDto> getBollaResaDettaglioFuoriVoce(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaFuoriVoceVo.class, "ce");
		criteria.createCriteria("ce.storicoCopertineVo", "sc");
		criteria.add(Restrictions.eq("ce.pk.codFiegDl", codFiegDl));
		if (codEdicola != null) {
			criteria.add(Restrictions.eq("ce.pk.codEdicola", codEdicola));
		}
		if (dataBolla != null) {
			criteria.add(Restrictions.eq("ce.pk.dtBolla", dataBolla));
		}
		if (tipoBolla != null) {
			criteria.add(Restrictions.eq("ce.pk.tipoBolla", tipoBolla));
		}
		criteria.add(Restrictions.gt("ce.reso", 0));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("ce.pk.codFiegDl"), "codFiegDl");
		props.add(Projections.property("ce.pk.codEdicola"), "codEdicola");
		props.add(Projections.property("ce.pk.dtBolla"), "dataBolla");
		props.add(Projections.property("ce.pk.tipoBolla"), "tipoBolla");
		props.add(Projections.property("ce.idtnTrascodifica"), "idtnTrascodifica");
		props.add(Projections.property("ce.reso"), "copie");
		props.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertinaBd");
		props.add(Projections.property("ce.prezzoNetto"), "prezzoNettoBd");
		props.add(Projections.property("ce.resoInContoDeposito"), "resoInContoDeposito");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(InforivResaDichiarataDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}

	@Override
	public List<InforivResaDichiarataDto> getBollaResaDettaglioRichiamoPersonalizzato(Integer codFiegDl, Integer codEdicola, Date dataBolla, String tipoBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaRichiamoPersonalizzatoVo.class, "ce");
		criteria.createCriteria("ce.storicoCopertineVo", "sc");
		criteria.add(Restrictions.eq("ce.pk.codFiegDl", codFiegDl));
		if (codEdicola != null) {
			criteria.add(Restrictions.eq("ce.pk.codEdicola", codEdicola));
		}
		if (dataBolla != null) {
			criteria.add(Restrictions.eq("ce.pk.dtBolla", dataBolla));
		}
		if (tipoBolla != null) {
			criteria.add(Restrictions.eq("ce.pk.tipoBolla", tipoBolla));
		}
		criteria.add(Restrictions.gt("ce.reso", 0));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("ce.pk.codFiegDl"), "codFiegDl");
		props.add(Projections.property("ce.pk.codEdicola"), "codEdicola");
		props.add(Projections.property("ce.pk.dtBolla"), "dataBolla");
		props.add(Projections.property("ce.pk.tipoBolla"), "tipoBolla");
		props.add(Projections.property("ce.idtnTrascodifica"), "idtnTrascodifica");
		props.add(Projections.property("ce.reso"), "copie");
		props.add(Projections.property("sc.prezzoCopertina"), "prezzoCopertinaBd");
		props.add(Projections.property("ce.prezzoNetto"), "prezzoNettoBd");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(InforivResaDichiarataDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<InforivTotaleBollaResaEdicolaDto> getBolleResaRiassuntoDaTrasmettereDl(Integer codFiegDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BollaResaRiassuntoVo.class, "ce");
		criteria.add(Restrictions.eq("ce.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ce.bollaTrasmessaDl", IGerivConstants.INDICATORE_BOLLA_DA_TRASMETTERE));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("ce.pk.codFiegDl"), "codFiegDl");
		props.add(Projections.property("ce.pk.codEdicola"), "codEdicola");
		props.add(Projections.property("ce.pk.dtBolla"), "dataBolla");
		props.add(Projections.property("ce.pk.tipoBolla"), "tipoBolla");
		props.add(Projections.property("ce.totaleCopieBollaResaDettaglio"), "totaleCopieBollaResaDettaglio");
		props.add(Projections.property("ce.totaleBollaResaDettaglio"), "totaleBollaResaDettaglio");
		props.add(Projections.property("ce.totaleCopieBollaFuoriResa"), "totaleCopieBollaFuoriResa");
		props.add(Projections.property("ce.totaleBollaFuoriResa"), "totaleBollaFuoriResa");
		props.add(Projections.property("ce.totaleCopieBollaResaDimenticata"), "totaleCopieBollaResaDimenticata");
		props.add(Projections.property("ce.totaleCopieBollaResaDettaglio"), "totaleCopieBollaResaDettaglio");
		props.add(Projections.property("ce.totaleBollaResaDimenticata"), "totaleBollaResaDimenticata");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(InforivTotaleBollaResaEdicolaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<InforivVenditeDto> getVenditeDaTrasmettereDl(Integer codFiegDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(VenditaDettaglioVo.class, "ce");
		criteria.createCriteria("ce.venditaVo", "ve");
		criteria.add(Restrictions.eq("ce.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ve.trasferitaGestionale", IGerivConstants.INDICATORE_RECORD_NON_TRASMESSO_DL));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("ce.codFiegDl"), "codFiegDl");
		props.add(Projections.property("ce.codEdicola"), "codEdicola");
		props.add(Projections.property("ve.codVendita"), "codVendita");
		props.add(Projections.property("ve.dataVendita"), "dataVendita");
		props.add(Projections.property("ce.importoTotale"), "importoTotale");
		props.add(Projections.property("ce.idtnTrascodifica"), "idtnTrascodifica");
		props.add(Projections.property("ce.quantita"), "copieVendute");
		props.add(Projections.property("ce.prezzoCopertina"), "prezzoCopertina");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(InforivVenditeDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	/**
	 * @param codFiegDl
	 * @return
	 */
	public List<RichiestaRifornimentoVo> getRichiesteRifornimenti(final Integer codFiegDl) {
		HibernateCallback<List<RichiestaRifornimentoVo>> action = new HibernateCallback<List<RichiestaRifornimentoVo>>() {
			@Override
			public List<RichiestaRifornimentoVo> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(RichiestaRifornimentoVo.class, "ce");
				criteria.add(Restrictions.eq("ce.pk.codFiegDl", codFiegDl));
				criteria.add(Restrictions.isNull("ce.stato"));
				Hibernate.initialize("ce.idtnTrascodifica");
				return criteria.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<InforivVariazioniServizioDto> getVariazioni(Integer codFiegDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PrenotazioneVo.class, "ce");
		criteria.createCriteria("ce.pubblicazione", "pub");
		criteria.add(Restrictions.eq("ce.pk.codDl", codFiegDl));
		criteria.add(Restrictions.eq("ce.indicatoreTrasmessoDl", IGerivConstants.INDICATORE_RECORD_NON_TRASFERITO));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("ce.pk.codDl"), "codFiegDl");
		props.add(Projections.property("ce.pk.codEdicola"), "codEdicola");
		props.add(Projections.property("ce.pk.codicePubblicazione"), "codiceTestata");
		props.add(Projections.property("pub.titolo"), "titolo");
		props.add(Projections.property("ce.quantitaRichiesta"), "copieRichieste");
		props.add(Projections.property("ce.motivoRichiesta"), "note");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(InforivVariazioniServizioDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public void updateBolleRiassuntoTrasmesseDl(Integer codFiegDl) {
		getDao().bulkUpdate("update BollaRiassuntoVo vo set vo.bollaTrasmessaDl = ?, vo.dtTrasmissione = ? where vo.pk.codFiegDl = ? and vo.bollaTrasmessaDl = ?", new Object[]{IGerivConstants.INDICATORE_BOLLA_TRASMESSA, new Date(), codFiegDl, IGerivConstants.INDICATORE_BOLLA_DA_TRASMETTERE});
	}
	
	@Override
	public void updateBolleResaRiassuntoTrasmesseDl(Integer codFiegDl) {
		getDao().bulkUpdate("update BollaResaRiassuntoVo vo set vo.bollaTrasmessaDl = ?, vo.dtTrasmissione = ? where vo.pk.codFiegDl = ? and vo.bollaTrasmessaDl = ?", new Object[]{IGerivConstants.INDICATORE_BOLLA_TRASMESSA, new Date(), codFiegDl, IGerivConstants.INDICATORE_BOLLA_DA_TRASMETTERE});
	}
	
	@Override
	public void updateBollaResaRiassuntoTrasmesseDl(Integer codFiegDl) {
		getDao().bulkUpdate("update BollaResaRiassuntoVo vo set vo.bollaTrasmessaDl = ?, vo.dtTrasmissione = ? where vo.pk.codFiegDl = ? and vo.bollaTrasmessaDl = ?", new Object[]{IGerivConstants.INDICATORE_BOLLA_TRASMESSA, new Date(), codFiegDl, IGerivConstants.INDICATORE_BOLLA_DA_TRASMETTERE});
	}
	
	@Override
	public void updateVenditeTrasmesse(Integer codFiegDl) {
		getDao().bulkUpdate("update VenditaVo vo set vo.trasferitaGestionale = ?, vo.dataTrasfGestionale = ? where vo.codFiegDl = ? and vo.trasferitaGestionale = ?", new Object[]{IGerivConstants.INDICATORE_RECORD_TRASFERITO, new Date(), codFiegDl, IGerivConstants.INDICATORE_RECORD_NON_TRASFERITO});
	}
	
	@Override
	public void updateRichiesteRifornimenti(Integer codFiegDl) {
		getDao().bulkUpdate("update RichiestaRifornimentoVo vo set vo.ordine = ? where vo.codFiegDl = ? and vo.trasferitaGestionale = ?", new Object[]{IGerivConstants.INDICATORE_RECORD_TRASFERITO, new Date(), codFiegDl, IGerivConstants.INDICATORE_RECORD_NON_TRASFERITO});
	}
	
	@Override
	public void updateVariazioni(Integer codFiegDl) {
		getDao().bulkUpdate("update PrenotazioneVo vo set vo.indicatoreTrasmessoDl = ?, vo.dataUltimaTrasmissioneDl = ? where vo.pk.codDl = ? and vo.indicatoreTrasmessoDl = ?", new Object[]{IGerivConstants.INDICATORE_RECORD_TRASFERITO, new Date(), codFiegDl, IGerivConstants.INDICATORE_RECORD_NON_TRASFERITO});
	}
	
	@Override
	public AbbinamentoTipoMovimentoFondoBollaInforivVo getAbbinamentoTipoMovimentoFondoBollaInforiv(Integer tipoMovimentoInforiv) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoTipoMovimentoFondoBollaInforivVo.class, "tmvo");
		criteria.add(Restrictions.eq("tmvo.pk.tipoRecordFondoBollaInforiv", tipoMovimentoInforiv));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
}
