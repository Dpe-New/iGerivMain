package it.dpe.igeriv.bo.contabilita;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;
import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.EstrattoContoDettaglioDto;
import it.dpe.igeriv.dto.EstrattoContoDinamicoDto;
import it.dpe.igeriv.dto.EstrattoContoDto;
import it.dpe.igeriv.dto.FatturazioneDto;
import it.dpe.igeriv.dto.VendutoEstrattoContoDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.CausaleMovimentiEstrattoContoVo;
import it.dpe.igeriv.vo.EstrattoContoDinamicoVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaDettaglioVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaPdfVo;
import it.dpe.igeriv.vo.EstrattoContoEdicolaVo;
import it.dpe.igeriv.vo.FattureEdicolaPdfVo;
import it.dpe.igeriv.vo.MovimentiEstrattoContoClientiVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiProgressiviFatturazioneVo;
import it.dpe.igeriv.vo.pk.ProdottiNonEditorialiProgressiviFatturazionePk;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import ch.lambdaj.group.Group;

@Repository
class ContabilitaRepositoryImpl extends BaseRepositoryImpl implements ContabilitaRepository {
	
	@Autowired
	ContabilitaRepositoryImpl(BaseDao<EstrattoContoEdicolaVo> dao) {
		super(dao);
	}
	
	@Override
	public List<EstrattoContoEdicolaDettaglioVo> getListEstrattoContoDettaglio(Integer codFiegDl, Integer codEdicola, Timestamp dtEstrattoConto) {
		return getDao().findByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ESTRATTO_DETTAGLIO_CONTO_EDICOLA, codFiegDl, codEdicola, dtEstrattoConto);
	}
	
	@Override
	public List<VendutoEstrattoContoDto> getListVendutoEstrattoConto(Integer[] codFiegDl, Integer codEdicola[], Timestamp dataDa, Timestamp dataA) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaDettaglioVo.class, "ecdvo");
		criteria.add(Restrictions.in("ecdvo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.in("ecdvo.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.between("ecdvo.pk.dataEstrattoConto", dataDa, dataA));
		criteria.add(Restrictions.in("ecdvo.tipoMovimento", new Object[]{IGerivConstants.COD_TIPO_MOVIMENTO_FORNITURA_QUOTIDIANI,IGerivConstants.COD_TIPO_MOVIMENTO_RESA_QUOTIDIANI,IGerivConstants.COD_TIPO_MOVIMENTO_FORNITURA_PERIODICI,IGerivConstants.COD_TIPO_MOVIMENTO_RESA_PERIODICI}));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.groupProperty("ecdvo.pk.dataEstrattoConto"), "dataEstrattoConto");
		properties.add(Projections.groupProperty("ecdvo.tipoMovimento"), "tipoMovimento");
		properties.add(Projections.sum("ecdvo.importoDare"), "importoDare");
		properties.add(Projections.sum("ecdvo.importoAvere"), "importoAvere");
		criteria.setProjection(properties);
		criteria.addOrder(Order.desc("ecdvo.pk.dataEstrattoConto")).addOrder(Order.desc("ecdvo.tipoMovimento"));
		criteria.setResultTransformer(Transformers.aliasToBean(EstrattoContoDettaglioDto.class));
		List<EstrattoContoDettaglioDto> listEstrattoContoDettaglio = getDao().findObjectByDetachedCriteria(criteria);
		List<VendutoEstrattoContoDto> retList = buildVendutoEstrattoContoList(listEstrattoContoDettaglio);
		return retList;
	}

	@Override
	public EstrattoContoDto getEstrattoConto(final Integer codFiegDl, final Integer codEdicola, final Timestamp dtEstrattoConto) {
		HibernateCallback<EstrattoContoDto> action = new HibernateCallback<EstrattoContoDto>() {
			@Override
			public EstrattoContoDto doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ESTRATTO_CONTO_BY_PK);
				query.setInteger("codFiegDl", codFiegDl);
				query.setInteger("codEdicola", codEdicola);
				query.setTimestamp("dataEstrattoConto", dtEstrattoConto);
				query.setResultTransformer(Transformers.aliasToBean(EstrattoContoDto.class));
				return (EstrattoContoDto) query.uniqueResult();
			}
		}; 
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public List<EstrattoContoDto> getListDateEstrattoConto(final Integer codFiegDl, final Integer codEdicola, final java.sql.Date dataInizioEstrattoContoPdf) {
		HibernateCallback<List<EstrattoContoDto>> action = new HibernateCallback<List<EstrattoContoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<EstrattoContoDto> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.getNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ESTRATTO_CONTO_EDICOLA_DATE);
				query.setInteger("codFiegDl", codFiegDl);
				query.setInteger("codEdicola", codEdicola);
				query.setDate("dataInizioEstrattoContoPdf", dataInizioEstrattoContoPdf);
				query.setResultTransformer(Transformers.aliasToBean(EstrattoContoDto.class));
				List<EstrattoContoDto> list = query.list();
				if (list != null && !list.isEmpty()) {
					forEach(list).setTipo(IGerivConstants.COD_TIPO_ESTRATTO_CONTO_DATI);
				}
				return list;
			}
		}; 
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<EstrattoContoDto> getListDateEstrattoContoPdf(Integer codFiegDl, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaPdfVo.class, "ec");
		criteria.add(Restrictions.eq("ec.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ec.pk.codEdicola", codEdicola));
		criteria.addOrder(Order.desc("ec.pk.dataEstrattoConto"));
		ProjectionList properties = Projections.projectionList(); 
		properties.add(Projections.property("ec.pk.dataEstrattoConto"), "dataEstrattoConto");
		criteria.setProjection(properties); 
		criteria.setResultTransformer(Transformers.aliasToBean(EstrattoContoDto.class));
		List<EstrattoContoDto> list = getDao().findObjectByDetachedCriteria(criteria);
		if (list != null && !list.isEmpty()) {
			forEach(list).setTipo(IGerivConstants.COD_TIPO_ESTRATTO_CONTO_PDF);
		}
		return list;
	}
	
	
	@Override
	public List<FatturazioneDto> getFatturazioneEdicole(final Integer codDlInt, final Date dtFatturazione, final Integer trimestre) {
		HibernateCallback<List<FatturazioneDto>> action = new HibernateCallback<List<FatturazioneDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<FatturazioneDto> doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = null;
				
				if(codDlInt.intValue() == IGerivConstants.CHIMINELLI_CODE_1.intValue() || codDlInt.intValue() == IGerivConstants.CHIMINELLI_CODE_2.intValue()){
					//CHIMINELLI
					if (trimestre.equals(IGerivConstants.COD_TRIMETRE_PRECEDENTE)) {
						sql = IGerivQueryContants.SQL_QUERY_GET_FATTURAZIONE_EDICOLE_TRIMESTRE_PRECEDENTE_CHIMINELLI;
					} else {
						sql = IGerivQueryContants.SQL_QUERY_GET_FATTURAZIONE_EDICOLE_TRIMESTRE_SUCCESSIVO_CHIMINELLI;
					}
				}else{
					
					if (trimestre.equals(IGerivConstants.COD_TRIMETRE_PRECEDENTE)) {
						sql = IGerivQueryContants.SQL_QUERY_GET_FATTURAZIONE_EDICOLE_TRIMESTRE_PRECEDENTE;
					} else {
						sql = IGerivQueryContants.SQL_QUERY_GET_FATTURAZIONE_EDICOLE_TRIMESTRE_SUCCESSIVO;
					}
				}
				SQLQuery createSQLQuery = session.createSQLQuery(sql);
				createSQLQuery.setTimestamp("dtFatturazione", dtFatturazione);
				createSQLQuery.setInteger("coddl", codDlInt);
				createSQLQuery.setResultTransformer( Transformers.aliasToBean(FatturazioneDto.class));
				createSQLQuery.addScalar("coddl", IntegerType.INSTANCE);
				createSQLQuery.addScalar("nomeDl", StringType.INSTANCE);
				createSQLQuery.addScalar("dtAttivazioneEdicola",TimestampType.INSTANCE);
				createSQLQuery.addScalar("codEdicolaDl", IntegerType.INSTANCE);
				createSQLQuery.addScalar("nomeEdicola", StringType.INSTANCE);
				createSQLQuery.addScalar("giorni", IntegerType.INSTANCE);
				createSQLQuery.addScalar("dtSospensioneEdicola",TimestampType.INSTANCE);
				createSQLQuery.addScalar("roleName", StringType.INSTANCE);
				createSQLQuery.addScalar("importoiGeriv", FloatType.INSTANCE);
				createSQLQuery.addScalar("importoiGerivPlus", FloatType.INSTANCE);
				createSQLQuery.addScalar("importoiGerivPromo", FloatType.INSTANCE);
				createSQLQuery.addScalar("edicolaTest", BooleanType.INSTANCE);
				return createSQLQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public EstrattoContoEdicolaVo getEstrattoContoEdicolaVo(Integer codFiegDl, Integer codEdicolaWeb, Date dataEstrattoConto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaVo.class, "ecevo");
		criteria.createCriteria("ecevo.dettagli", "det");
		criteria.add(Restrictions.eq("ecevo.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ecevo.pk.codEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("ecevo.pk.dataEstrattoConto", dataEstrattoConto));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public EstrattoContoEdicolaDettaglioVo getEstrattoContoEdicolaDettaglioVo(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto, int progressivo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaDettaglioVo.class, "eced");
		criteria.add(Restrictions.eq("eced.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("eced.pk.codEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("eced.pk.dataEstrattoConto", dataEstrattoConto));
		criteria.add(Restrictions.eq("eced.pk.progressivo", progressivo));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public Integer getLastProgressivoEstrattoConto(Integer codFiegDl, Integer codEdicolaWeb, java.sql.Date dataEstrattoConto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaDettaglioVo.class, "eced");
		criteria.add(Restrictions.eq("eced.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("eced.pk.codEdicola", codEdicolaWeb));
		criteria.add(Restrictions.eq("eced.pk.dataEstrattoConto", dataEstrattoConto));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.max("eced.pk.progressivo"));
		criteria.setProjection(props);
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	
	
	/**
	 * Ritorna una lista di VendutoEstrattoContoDto estraendo i dati dalla lista listEstrattoContoDettaglio.
	 * 
	 * @param List<EstrattoContoDettaglioDto> listEstrattoContoDettaglio
	 * @return List<VendutoEstrattoContoDto>
	 */
	private List<VendutoEstrattoContoDto> buildVendutoEstrattoContoList(List<EstrattoContoDettaglioDto> listEstrattoContoDettaglio) {
		Group<EstrattoContoDettaglioDto> group = group(listEstrattoContoDettaglio, by(on(EstrattoContoDettaglioDto.class).getDataEstrattoConto()));
		List<VendutoEstrattoContoDto> retList = new ArrayList<VendutoEstrattoContoDto>();
		for (Group<EstrattoContoDettaglioDto> subgroup : group.subgroups()) {
			VendutoEstrattoContoDto vecdto = new VendutoEstrattoContoDto();
			List<EstrattoContoDettaglioDto> findAll = subgroup.findAll();
			vecdto.setDataEstrattoConto(findAll.get(0).getDataEstrattoConto());
			for (EstrattoContoDettaglioDto dto : findAll) {
				switch(dto.getTipoMovimento()) {
					case IGerivConstants.COD_TIPO_MOVIMENTO_FORNITURA_QUOTIDIANI:
						vecdto.setFornitoQuotidiani(dto.getImportoDare().subtract(dto.getImportoAvere()));
						break;
					case IGerivConstants.COD_TIPO_MOVIMENTO_RESA_QUOTIDIANI:
						vecdto.setResoQuotidiani(dto.getImportoAvere().subtract(dto.getImportoDare()));
						break;
					case IGerivConstants.COD_TIPO_MOVIMENTO_FORNITURA_PERIODICI:
						vecdto.setFornitoPeriodici(dto.getImportoDare().subtract(dto.getImportoAvere()));
						break;
					case IGerivConstants.COD_TIPO_MOVIMENTO_RESA_PERIODICI:
						vecdto.setResoPeriodici(dto.getImportoAvere().subtract(dto.getImportoDare()));
						break;
				}
			}
			retList.add(vecdto);
		}
		return retList;
	}
	
	@Override
	public CausaleMovimentiEstrattoContoVo getCausaleMovimentoEstrattoConto(Integer codCausale) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CausaleMovimentiEstrattoContoVo.class, "ccvo");
		criteria.add(Restrictions.eq("ccvo.codice", codCausale));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public boolean existsEstrattoContoChiuso(Long codCliente, Timestamp dataA) {
		DetachedCriteria criteria = DetachedCriteria.forClass(MovimentiEstrattoContoClientiVo.class, "mvec");
		criteria.createCriteria("mvec.cliente", "cli");
		criteria.add(Restrictions.eq("cli.codCliente", codCliente));
		criteria.add(Restrictions.eq("mvec.dataEstrattoConto", dataA));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("mvec.idMovimento"));
		criteria.setProjection(props);
		Long codMov = getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (codMov == null) ? false : true;
	}
	
	@Override
	public ProdottiNonEditorialiProgressiviFatturazioneVo getNextProgressivoFatturazioneVo(final Integer codEdicolaMaster, final Integer tipoDocumento, final java.util.Date data) {
		HibernateCallback<ProdottiNonEditorialiProgressiviFatturazioneVo> action = new HibernateCallback<ProdottiNonEditorialiProgressiviFatturazioneVo>() {
			@Override
			public ProdottiNonEditorialiProgressiviFatturazioneVo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiProgressiviFatturazioneVo.class, "prog");
				criteria.add(Restrictions.eq("prog.pk.codEdicola", codEdicolaMaster));
				if (tipoDocumento != null) {
					criteria.add(Restrictions.eq("prog.pk.tipoDocumento", tipoDocumento));
				}
				criteria.add(Restrictions.eq("prog.pk.data", data));
				criteria.addOrder(Order.desc("prog.progressivo"));
				criteria.setMaxResults(1);
				Object uniqueResult = criteria.uniqueResult();
				if (uniqueResult == null) {
					uniqueResult = insertFirstProgressivoFatturazione(codEdicolaMaster, tipoDocumento, data);
				} 
				return (ProdottiNonEditorialiProgressiviFatturazioneVo) uniqueResult;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public Long getNextProgressivoFatturazione(Integer codEdicolaMaster, Integer tipoDocumento, java.util.Date data) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiProgressiviFatturazioneVo.class, "prog");
		criteria.add(Restrictions.eq("prog.pk.codEdicola", codEdicolaMaster));
		if (tipoDocumento != null) {
			criteria.add(Restrictions.eq("prog.pk.tipoDocumento", tipoDocumento));
		}
		criteria.add(Restrictions.eq("prog.pk.data", data));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.max("prog.progressivo"));
		criteria.setProjection(props);
		Long prog = getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		if (prog == null) {
			insertFirstProgressivoFatturazione(codEdicolaMaster, tipoDocumento, data);
		}
		return (prog == null ? 1l : ++prog);
	}

	/**
	 * @param codEdicolaMaster
	 * @param tipoDocumento
	 * @param data
	 * @return
	 */
	public ProdottiNonEditorialiProgressiviFatturazioneVo insertFirstProgressivoFatturazione(Integer codEdicolaMaster, Integer tipoDocumento, java.util.Date data) {
		ProdottiNonEditorialiProgressiviFatturazioneVo vo = new ProdottiNonEditorialiProgressiviFatturazioneVo();
		ProdottiNonEditorialiProgressiviFatturazionePk pk = new ProdottiNonEditorialiProgressiviFatturazionePk();
		pk.setCodEdicola(codEdicolaMaster);
		pk.setData(new java.sql.Date(data.getTime()));
		pk.setTipoDocumento(tipoDocumento);
		vo.setPk(pk);
		vo.setDataUltMovimentazione(getSysdate());
		vo.setProgressivo(0l);
		saveBaseVo(vo);
		return vo;
	}

	@Override
	public void setNextProgressivoFatturazione(Integer codEdicola, Integer tipoDocumento, Date data, Long progressivo) {
		getDao().bulkUpdate("update ProdottiNonEditorialiProgressiviFatturazioneVo vo set vo.progressivo = ? where vo.pk.codEdicola = ? and vo.pk.tipoDocumento = ? and vo.pk.data = ?",  new Object[]{progressivo, codEdicola, tipoDocumento, data});
	}
	
	@Override
	public List<EstrattoContoDinamicoDto> getEstrattoContoDinamico(Integer codFiegDl, Integer codEdicolaWeb) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoDinamicoVo.class, "ecdvo");
		criteria.add(Restrictions.eq("ecdvo.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ecdvo.codEdicola", codEdicolaWeb));
		//criteria.addOrder(Order.asc("ecdvo.dataMovimento")).addOrder(Order.asc("ecdvo.causale"));
		criteria.addOrder(Order.asc("ecdvo.progressivo"));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("ecdvo.dataMovimento"), "dataMovimento");
		props.add(Projections.property("ecdvo.descrizione"), "descrizione");
		props.add(Projections.property("ecdvo.importoDare"), "importoDare");
		props.add(Projections.property("ecdvo.importoAvere"), "importoAvere");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(EstrattoContoDinamicoDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EstrattoContoEdicolaDettaglioVo> getEstrattoContoEdicolaDettaglioVoFinoAllaData(Integer codDl, Integer codEdicola, Timestamp dataECFinale) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaDettaglioVo.class, "eced");
		criteria.add(Restrictions.eq("eced.pk.codFiegDl", codDl));
		criteria.add(Restrictions.eq("eced.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.le("eced.pk.dataEstrattoConto", dataECFinale));
		
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EstrattoContoEdicolaVo> getEstrattoContoEdicolaVoFinoAllaData(Integer codDl, Integer codEdicola, Timestamp dataECFinale) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaVo.class, "ece");
		criteria.add(Restrictions.eq("ece.pk.codFiegDl", codDl));
		criteria.add(Restrictions.eq("ece.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.le("ece.pk.dataEstrattoConto", dataECFinale));
		
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<FattureEdicolaPdfVo> getFattureEdicolaPdfVoFinoAllaData(Integer codDl, Integer codEdicola, Timestamp dataFatturaFinale) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FattureEdicolaPdfVo.class, "ecep");
		criteria.add(Restrictions.eq("ecep.pk.codFiegDl", codDl));
		criteria.add(Restrictions.eq("ecep.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.le("ecep.pk.dataFattura", dataFatturaFinale));
		
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EstrattoContoEdicolaPdfVo> getEstrattoContoEdicolaPdfVoFinoAllaData(Integer codDl, Integer codEdicola, Timestamp dataECFinale) {
		DetachedCriteria criteria = DetachedCriteria.forClass(EstrattoContoEdicolaPdfVo.class, "ecep");
		criteria.add(Restrictions.eq("ecep.pk.codFiegDl", codDl));
		criteria.add(Restrictions.eq("ecep.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.le("ecep.pk.dataEstrattoConto", dataECFinale));
		
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<FattureEdicolaPdfVo> getFattureDLdallaData(Integer codFiegDl, Integer codEdicola, Date from) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FattureEdicolaPdfVo.class, "ft");
		criteria.add(Restrictions.eq("ft.pk.codFiegDl", codFiegDl));
		criteria.add(Restrictions.eq("ft.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.ge("ft.pk.dataFattura", from));
		
		return getDao().findByDetachedCriteria(criteria);
	}

	
}
