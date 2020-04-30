package it.dpe.igeriv.bo.prodotti;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.selectDistinct;
import static org.hamcrest.Matchers.notNullValue;
import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.BollaResaProdottiVariDto;
import it.dpe.igeriv.dto.CategoriaDto;
import it.dpe.igeriv.dto.GiacenzaPneDto;
import it.dpe.igeriv.dto.ProdottoDto;
import it.dpe.igeriv.dto.ReportMagazzinoPneDto;
import it.dpe.igeriv.dto.RichiestaProdottoDto;
import it.dpe.igeriv.dto.SottoCategoriaDto;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.ProdottiNonEditorialiAliquotaIvaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiBollaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCategoriaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCausaliContabilitaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiCausaliMagazzinoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiDocumentiEmessiVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiGenericaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiGiacenzeVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiPrezziAcquistoVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaEdicolaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiSottoCategoriaVo;
import it.dpe.igeriv.vo.ProdottiNonEditorialiVo;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

@Repository
class ProdottiRepositoryImpl extends BaseRepositoryImpl implements ProdottiRepository {
	
	@Autowired
	ProdottiRepositoryImpl(BaseDao<ProdottiNonEditorialiVo> dao) {
		super(dao);
	}
	
	public void updateFakeImgMiniaturaName(Integer codFiegDl, Integer cpu, String nomeImmagine) {
		getDao().bulkUpdate(IGerivQueryContants.HQL_UPDATE_FAKE_IMAGE_NAME, new Object[]{nomeImmagine, codFiegDl, cpu});
	}
	
	@Override
	public List<ProdottoDto> getProdotti(final Integer codFornitore, final Integer codEdicola, final Long codContabileCliente, final String codiceEsterno, 
			final String descrizione, final Long categoria, final Long sottocategoria, final String barcode, final Float prezzo) {
		HibernateCallback<List<ProdottoDto>> action = new HibernateCallback<List<ProdottoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ProdottoDto> doInHibernate(Session session) throws HibernateException, SQLException { 
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiPrezziAcquistoVo.class, "pnepa");
				criteria.createCriteria("pnepa.prodotto", "pdto");
				criteria.createCriteria("pnepa.fornitore", "forn");
				criteria.createCriteria("pnepa.edicola", "ed");
				criteria.createCriteria("pdto.categoria", "cat", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("pdto.sottocategoria", "scat", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("pdto.prodottoDl", true));
				if (codEdicola != null) {
					criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
				}
				if (codFornitore != null) {
					criteria.add(Restrictions.eq("forn.pk.codFornitore", codFornitore));
				}
				if (!Strings.isNullOrEmpty(codiceEsterno)) {
					criteria.add(Restrictions.eq("pdto.codProdottoEsterno", codiceEsterno));
				}
				if (descrizione != null && !descrizione.trim().equals("")) {
					criteria.add(Restrictions.ilike("pdto.descrizioneProdottoA", descrizione, MatchMode.ANYWHERE));
				}
				if (categoria != null) {
					criteria.add(Restrictions.eq("pdto.codCategoria", categoria));
				}
				if (sottocategoria != null) {
					criteria.add(Restrictions.eq("pdto.codSottoCategoria", sottocategoria));
				}
				if (barcode != null && !barcode.trim().equals("")) {
					criteria.add(Restrictions.eq("pdto.barcode", barcode));
				}
				if (prezzo != null) {
					criteria.add(Restrictions.eq("pdto.prezzo", prezzo));
				}
				criteria.add(Restrictions.or(Restrictions.isNull("pdto.obsoleto"), Restrictions.eq("pdto.obsoleto", false)));
				ProjectionList properties = Projections.projectionList();
				properties.add(Projections.property("pdto.codProdottoInterno"), "codProdottoInterno");
				properties.add(Projections.property("pdto.codProdottoEsterno"), "codProdottoEsterno");
				properties.add(Projections.property("pdto.descrizioneProdottoA"), "descrizione");
				properties.add(Projections.property("cat.descrizione"), "categoria");
				properties.add(Projections.property("scat.descrizione"), "sottocategoria");
				properties.add(Projections.property("pdto.barcode"), "barcode");
				properties.add(Projections.property("pdto.nomeImmagine"), "immagine");
				properties.add(Projections.property("pdto.aliquota"), "aliquota");
				properties.add(Projections.property("pnepa.ultimoPrezzoAcquisto"), "ultimoPrezzoAcquisto");
				properties.add(Projections.property("pnepa.pk.codiceProdottoFornitore"), "codiceProdottoFornitore");
				criteria.setProjection(properties);
				criteria.setResultTransformer(Transformers.aliasToBean(ProdottoDto.class));
				List<ProdottoDto> list = criteria.list();
				
				String sql = IGerivQueryContants.SQL_QUERY_GET_PREZZO;
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				for (ProdottoDto dto : list) {
					sqlQuery.setLong("codProdotto", dto.getCodProdottoInterno());
					sqlQuery.setLong("codContabileCliente", codContabileCliente);
					sqlQuery.addScalar("prezzo", FloatType.INSTANCE);
					Float prezzoResult = (Float) sqlQuery.uniqueResult();
					dto.setPrezzo(prezzoResult);
					dto.setGiacenza(getGiacenzaProdottoDl(dto.getCodiceProdottoFornitore(), codFornitore));
				}
				
				return list;
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public Integer getGiacenzaProdottoDl(String codiceProdottoFornitore, Integer codFornitore) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiGiacenzeVo.class, "giac");
		criteria.add(Restrictions.eq("giac.pk.codDl", codFornitore));
		criteria.add(Restrictions.eq("giac.pk.codProdottoFornitore", codiceProdottoFornitore));
		criteria.add(Restrictions.eq("giac.pk.numeroMagazzino", IGerivConstants.COD_MAGAZZINO_INTERNO));
		ProjectionList properties = Projections.projectionList();
		properties.add(Projections.property("giac.giacenza"));
		criteria.setProjection(properties);
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<RichiestaProdottoDto> getRichiesteProdotti(final Integer codFornitore, final Integer codEdicolaDl, final String descrizione, 
			final String stato, final Timestamp dataDa, final Timestamp dataA) {
		HibernateCallback<List<RichiestaProdottoDto>> action = new HibernateCallback<List<RichiestaProdottoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<RichiestaProdottoDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo.class, "pdto");
				criteria.createCriteria("pdto.prodotto", "prod");
				criteria.createCriteria("pdto.richiesteRifornimento", "rr");
				criteria.createCriteria("rr.fornitore", "forn");
				criteria.createCriteria("rr.edicola", "ed");
				criteria.add(Restrictions.eq("ed.codEdicolaDl", codEdicolaDl));
				if (codFornitore != null) {
					criteria.add(Restrictions.eq("forn.pk.codFornitore", codFornitore));
				}
				if (descrizione != null && !descrizione.equals("")) {
					criteria.add(Restrictions.ilike("prod.descrizioneProdottoA", descrizione, MatchMode.ANYWHERE));
				}
				if (stato != null && !stato.equals("")) {
					criteria.add(Restrictions.eq("pdto.stato", stato));
				}
				if (dataDa != null && dataA != null) {
					criteria.add(Restrictions.between("rr.dataRichiesta", dataDa, dataA));
				}
				ProjectionList properties = Projections.projectionList();
				properties.add(Projections.property("prod.descrizioneProdottoA"), "descrizioneProdotto");
				properties.add(Projections.property("pdto.quatitaRichiesta"), "quatitaRichiesta");
				properties.add(Projections.property("pdto.quatitaEvasa"), "quatitaEvasa");
				properties.add(Projections.property("rr.dataRichiesta"), "dataRichiesta");
				properties.add(Projections.property("pdto.stato"), "stato");
				properties.add(Projections.property("pdto.rispostaDl"), "rispostaDl");
				properties.add(Projections.property("pdto.note"), "note");
				criteria.setProjection(properties);
				criteria.setResultTransformer(Transformers.aliasToBean(RichiestaProdottoDto.class));
				List<RichiestaProdottoDto> list = criteria.list();
				return list;
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<ProdottiNonEditorialiCategoriaEdicolaVo> getCategorie(Integer codFiegDl, Integer codEdicola) {
		DetachedCriteria criteria = null;
		List<ProdottiNonEditorialiCategoriaEdicolaVo> categorie = null;
		if (codFiegDl != null && codEdicola != null) {
			criteria = DetachedCriteria.forClass(ProdottiNonEditorialiPrezziAcquistoVo.class, "pnepa");
			criteria.createCriteria("pnepa.fornitore", "fo");
			criteria.createCriteria("pnepa.edicola", "ed");
			criteria.createCriteria("pnepa.prodotto", "prod");
			criteria.createCriteria("prod.categoria", "cat");
			criteria.add(Restrictions.eq("fo.pk.codFornitore", codFiegDl));
			criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
			List<ProdottiNonEditorialiPrezziAcquistoVo> prodotti = getDao().findByDetachedCriteria(criteria);
			Collection<ProdottiNonEditorialiCategoriaEdicolaVo> categorieCol = selectDistinct(extract(prodotti, on(ProdottiNonEditorialiPrezziAcquistoVo.class).getProdotto().getCategoria()));
			categorie = new ArrayList<ProdottiNonEditorialiCategoriaEdicolaVo>(categorieCol);
		} else {
			criteria = DetachedCriteria.forClass(ProdottiNonEditorialiCategoriaEdicolaVo.class, "pnepa");
			categorie = getDao().findByDetachedCriteria(criteria);
		}
		return categorie;
	}
	
	@Override
	public List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> getSottocategorieProdottiDl(Integer codFiegDl, Integer codEdicola, Long categoria) {
		DetachedCriteria criteria = null;
		List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> sottocategorie = null;
		if (codFiegDl != null && codEdicola != null) {
			criteria = DetachedCriteria.forClass(ProdottiNonEditorialiPrezziAcquistoVo.class, "pnepa");
			criteria.createCriteria("pnepa.fornitore", "fo");
			criteria.createCriteria("pnepa.edicola", "ed");
			criteria.createCriteria("pnepa.prodotto", "prod");
			criteria.createCriteria("prod.categoria", "cat");
			criteria.createCriteria("prod.sottocategoria", "scat");
			criteria.add(Restrictions.eq("fo.pk.codFornitore", codFiegDl));
			criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
			criteria.add(Restrictions.eq("prod.codCategoria", categoria));
			List<ProdottiNonEditorialiPrezziAcquistoVo> prodotti = getDao().findByDetachedCriteria(criteria);
			Collection<ProdottiNonEditorialiSottoCategoriaEdicolaVo> sottocategorieCol = selectDistinct(extract(prodotti, on(ProdottiNonEditorialiPrezziAcquistoVo.class).getProdotto().getSottocategoria()));
			sottocategorie = new ArrayList<ProdottiNonEditorialiSottoCategoriaEdicolaVo>(sottocategorieCol);
		} 
		return sottocategorie;
	}
	
	@Override
	public RichiestaProdottoDto getRichiestaRifornimentoProdotto(final Long codice) {
		HibernateCallback<RichiestaProdottoDto> action = new HibernateCallback<RichiestaProdottoDto>() {
			@Override
			public RichiestaProdottoDto doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo.class, "svo");
				criteria.createCriteria("svo.richiesteRifornimento", "rr");
				criteria.createCriteria("svo.prodotto", "pr");
				criteria.add(Restrictions.eq("svo.pk.codProdottoInterno", codice));
				ProjectionList properties = Projections.projectionList();
				properties.add(Projections.property("pr.descrizioneProdottoA"), "descrizioneProdotto");
				properties.add(Projections.property("svo.pk.codRichiestaRifornimento"), "codRichiestaRifornimento");
				properties.add(Projections.property("svo.pk.codProdottoInterno"), "codProdottoInterno");
				properties.add(Projections.property("svo.quatitaRichiesta"), "quatitaRichiestaTmp");
				properties.add(Projections.property("svo.quatitaEvasa"), "quatitaEvasa");
				properties.add(Projections.property("rr.dataRichiesta"), "dataRichiesta");
				properties.add(Projections.property("rr.dataInvioRichiestaDl"), "dataInvioRichiestaDl");
				properties.add(Projections.property("svo.stato"), "stato");
				properties.add(Projections.property("svo.rispostaDl"), "rispostaDl");
				properties.add(Projections.property("svo.note"), "note");
				criteria.addOrder(Order.desc("rr.dataRichiesta"));
				criteria.setProjection(properties);
				criteria.setResultTransformer(Transformers.aliasToBean(RichiestaProdottoDto.class));
				criteria.setMaxResults(1);
				Object uniqueResult = criteria.uniqueResult();
				return uniqueResult != null ? (RichiestaProdottoDto) uniqueResult : null;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo getProdottiRichiesteRifornimentoDettaglioVo(Long codice, Long codRichiestaRifornimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiRichiesteRifornimentoDettaglioVo.class, "svo");
		criteria.createCriteria("svo.richiesteRifornimento");
		criteria.add(Restrictions.eq("svo.pk.codProdottoInterno", codice));
		criteria.add(Restrictions.eq("svo.pk.codRichiestaRifornimento", codRichiestaRifornimento));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<CategoriaDto> getCategoriePne(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiCategoriaEdicolaVo.class, "pnecat");
		criteria.add(Restrictions.eq("pnecat.pk.codEdicola", codEdicola));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("pnecat.pk.codCategoria"), "codCategoria");
		props.add(Projections.property("pnecat.descrizione"), "descrizione");
		props.add(Projections.property("pnecat.immagine"), "immagine");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(CategoriaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<SottoCategoriaDto> getSottocategoriePne(Long categoria, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiSottoCategoriaEdicolaVo.class, "pnecat");
		criteria.add(Restrictions.eq("pnecat.pk.codEdicola", codEdicola));
		if (categoria != null) {
			criteria.add(Restrictions.eq("pnecat.pk.codCategoria", categoria));
		}
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("pnecat.pk.codCategoria"), "codCategoria");
		props.add(Projections.property("pnecat.pk.codSottoCategoria"), "codSottoCategoria");
		props.add(Projections.property("pnecat.descrizione"), "descrizione");
		props.add(Projections.property("pnecat.immagine"), "immagine");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(SottoCategoriaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottoDto> getProdottiNonEditoriali(Long categoria, Long sottocategoria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiGenericaVo.class, "pne");
		if (categoria != null) {
			criteria.add(Restrictions.eq("pne.codCategoria", categoria));
		}
		if (sottocategoria != null) {
			criteria.add(Restrictions.eq("pne.codSottoCategoria", sottocategoria));
		}
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("pne.codProdottoInterno"), "codProdottoInterno");
		props.add(Projections.property("pne.codProdottoEsterno"), "codProdottoEsterno");
		props.add(Projections.property("pne.descrizioneProdottoA"), "descrizione");
		props.add(Projections.property("pne.descrizioneProdottoB"), "descrizioneB");
		props.add(Projections.property("pne.barcode"), "barcode");
		props.add(Projections.property("pne.unitaMisura"), "unitaMisura");
		props.add(Projections.property("pne.aliquota"), "aliquota");
		props.add(Projections.property("pne.unitaMinimaIncremento"), "unitaMinimaIncremento");
		props.add(Projections.property("pne.nomeImmagine"), "nomeImmagine");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(ProdottoDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottoDto> getProdottoNonEditorialeByBarcode(final Integer codEdicola, final String barcode, final Integer codFornitore, final Boolean soloProdottiForniti) {
		HibernateCallback<List<ProdottoDto>> action = new HibernateCallback<List<ProdottoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ProdottoDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiPrezziAcquistoVo.class, "prea");
				criteria.createCriteria("prea.prodotto", "prod", JoinType.RIGHT_OUTER_JOIN);
				criteria.createCriteria("prod.edicola");
				criteria.createCriteria("prod.prezzi", "pre", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("prod.edicola.codEdicola", codEdicola));
				
				//17-01-2018 Ricerca barcode - Gestione del prodotto obsolete 
				criteria.add(Restrictions.or(Restrictions.isNull("prod.obsoleto"), Restrictions.eq("prod.obsoleto", false)));
				
				
				if (codFornitore != null) {
					criteria.add(Restrictions.eq("prea.codiceFornitore", codFornitore));
				}
				if (soloProdottiForniti != null && soloProdottiForniti) {
					DetachedCriteria subCriteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "bc");
					subCriteria.createCriteria("bc.prodotto", "prod1");
					subCriteria.createCriteria("prod1.edicola", "edi");
					subCriteria.createCriteria("bc.causale", "cau");
					subCriteria.add(Property.forName("edi.codEdicola").eqProperty("prea.pk.codEdicola"));
					subCriteria.add(Property.forName("prod1.codProdottoInterno").eqProperty("prod.codProdottoInterno"));
					subCriteria.add(Property.forName("cau.codiceCausale").eq(IGerivConstants.CODICE_CAUSALE_ACQUISTO));
					subCriteria.add(Restrictions.or(Property.forName("bc.deleted").isNull(), Property.forName("bc.deleted").eq(false)));
					criteria.add(Subqueries.exists(subCriteria.setProjection(Projections.property("bc.pk.idDocumento"))));
				}
				criteria.add(Restrictions.eq("prod.barcode", barcode));
				ProjectionList props = Projections.projectionList(); 
				props.add(Projections.distinct(Projections.projectionList().add(Projections.property("prod.codProdottoInterno"), "codProdottoInterno")));
				props.add(Projections.property("prod.descrizioneProdottoA"), "descrizione");
				props.add(Projections.property("prod.barcode"), "barcode");
				props.add(Projections.property("prod.aliquota"), "aliquota");
				props.add(Projections.property("prod.codiceProdottoFornitore"), "codiceProdottoFornitore");
				props.add(Projections.property("prod.ultimoPrezzoAcquisto"), "ultimoPrezzoAcquisto");
				props.add(Projections.property("prod.prodottoDl"), "prodottoDl");
				props.add(Projections.property("prod.percentualeResaSuDistribuito"), "percentualeResaSuDistribuito");
				props.add(Projections.property("prod.giacenzaProdotto"), "giacenzaProdotto");
				criteria.setProjection(props);
				criteria.setResultTransformer(Transformers.aliasToBean(ProdottoDto.class));
				return criteria.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public ProdottiNonEditorialiVo getProdottoNonEditorialeVo(Long codiceProdottoInterno) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiVo.class, "pne");
		criteria.add(Restrictions.eq("pne.codProdottoInterno", codiceProdottoInterno));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottiNonEditorialiGenericaVo> getProdottiNonEditoriali(Long categoria, Long sottocategoria, Integer codEdicolaProdottiDaEscudere) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiGenericaVo.class, "pne");
		criteria.createCriteria("pne.categoria", "cat");
		criteria.createCriteria("pne.sottocategoria", "scat");
		if (categoria != null) {
			criteria.add(Restrictions.eq("pne.codCategoria", categoria));
		}
		if (sottocategoria != null) {
			criteria.add(Restrictions.eq("pne.codSottoCategoria", sottocategoria));
		}
		if (codEdicolaProdottiDaEscudere != null) {
			DetachedCriteria subCriteria = DetachedCriteria.forClass(ProdottiNonEditorialiVo.class, "pnevo");
			subCriteria.add(Property.forName("pnevo.edicola.codEdicola").eq(codEdicolaProdottiDaEscudere));
			subCriteria.add(Property.forName("pnevo.codProdottoInterno").eqProperty("pne.codProdottoInterno"));
			criteria.add(Subqueries.notExists(subCriteria.setProjection(Projections.property("pnevo.codProdottoInterno"))));
		}
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicola(final Long categoria, final Long sottocategoria, final Integer codEdicola) {
		HibernateCallback<List<ProdottiNonEditorialiVo>> action = new HibernateCallback<List<ProdottiNonEditorialiVo>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ProdottiNonEditorialiVo> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiVo.class, "pne");
				criteria.createCriteria("pne.categoria", "cat");
				criteria.createCriteria("pne.sottocategoria", "scat");
				criteria.createCriteria("pne.edicola", "ed");
				criteria.createCriteria("pne.prezzi", "prezzi", JoinType.LEFT_OUTER_JOIN);
				if (codEdicola != null) {
					criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
				}
				if (categoria != null) {
					criteria.add(Restrictions.eq("pne.codCategoria", categoria));
				}
				if (sottocategoria != null) {
					criteria.add(Restrictions.eq("pne.codSottoCategoria", sottocategoria));
				}
				criteria.addOrder(Order.asc("pne.posizione"));
				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				List<ProdottiNonEditorialiVo> list = criteria.list();
				/*for (ProdottiNonEditorialiVo vo : list) {
					vo.getPrezzo();
					vo.getGiacenzaProdotto();
					vo.getGiacenzaInizialeProdotto();
				}*/
				return list;
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<ProdottoDto> getProdottiNonEditorialiEdicolaDto(final Integer codEdicola) {
		HibernateCallback<List<ProdottoDto>> action = new HibernateCallback<List<ProdottoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ProdottoDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiVo.class, "pne");
				criteria.createCriteria("pne.categoria", "cat");
				criteria.createCriteria("pne.sottocategoria", "scat");
				criteria.createCriteria("pne.edicola", "ed");
				criteria.createCriteria("pne.prezzi", "prezzi", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
				criteria.addOrder(Order.asc("pne.posizione"));
				ProjectionList props = Projections.projectionList(); 
				props.add(Projections.distinct(Projections.projectionList().add(Projections.property("pne.codProdottoInterno"), "codProdottoInterno")));
				props.add(Projections.property("pne.codProdottoEsterno"), "codProdottoEsterno");
				props.add(Projections.property("pne.descrizioneProdottoA"), "descrizione");
				props.add(Projections.property("pne.descrizioneProdottoB"), "descrizioneB");
				props.add(Projections.property("pne.codCategoria"), "codCategoria");
				props.add(Projections.property("pne.codSottoCategoria"), "codSubCategoria");
				props.add(Projections.property("pne.barcode"), "barcode");
				props.add(Projections.property("pne.unitaMisura"), "unitaMisura");
				props.add(Projections.property("pne.aliquota"), "aliquota");
				props.add(Projections.property("pne.unitaMinimaIncremento"), "unitaMinimaIncremento");
				props.add(Projections.property("pne.nomeImmagine"), "nomeImmagine");
				props.add(Projections.property("pne.ultimoPrezzoAcquisto"), "ultimoPrezzoAcquisto");
				props.add(Projections.property("pne.posizione"), "posizione");
				props.add(Projections.property("pne.giacenzaProdotto"), "giacenzaProdotto");
				props.add(Projections.property("pne.prezzo"), "prezzo");
				props.add(Projections.property("pne.giacenzaInizialeProdotto"), "giacenzaInizialeProdotto");
				props.add(Projections.property("pne.prodottoDl"), "prodottoDl");
				props.add(Projections.property("pne.escludiDalleVendite"), "escludiDalleVendite");
				//GIFT CARD EPIPOLI
				props.add(Projections.property("pne.isProdottoDigitale"), "isProdottoDigitale");
				props.add(Projections.property("pne.codiceFornitore"), "codiceFornitore");
				
				criteria.setProjection(props);
				criteria.setResultTransformer(Transformers.aliasToBean(ProdottoDto.class));
				return criteria.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<ProdottoDto> getProdottiVariByDescrizioneBarcode(final Integer codEdicola, final String descrizione, final String barcode) {
		HibernateCallback<List<ProdottoDto>> action = new HibernateCallback<List<ProdottoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ProdottoDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiVo.class, "pne");
				criteria.createCriteria("pne.edicola", "ed");
				criteria.createCriteria("pne.prezzi", "prezzi", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
				if (!Strings.isNullOrEmpty(descrizione)) {
//					criteria.add(Restrictions.eq("pne.barcode", barcode));
//					criteria.add(Restrictions.or(Restrictions.ilike("pne.descrizioneProdottoA", descrizione, MatchMode.ANYWHERE)));
					//criteria.add(Restrictions.ilike("pne.descrizioneProdottoA", descrizione, MatchMode.ANYWHERE));
					
					
					Disjunction or = Restrictions.disjunction();
					or.add(Restrictions.ilike("pne.barcode", descrizione));
					or.add(Restrictions.ilike("pne.descrizioneProdottoA", descrizione, MatchMode.ANYWHERE));
					criteria.add(or);
					
				}
				if (!Strings.isNullOrEmpty(barcode)) {
					criteria.add(Restrictions.eq("pne.barcode", barcode));
				}
				criteria.addOrder(Order.asc("pne.descrizioneProdottoA"));
				ProjectionList props = Projections.projectionList(); 
				props.add(Projections.distinct(Projections.projectionList().add(Projections.property("pne.codProdottoInterno"), "codProdottoInterno")));
				props.add(Projections.property("pne.codProdottoEsterno"), "codProdottoEsterno");
				props.add(Projections.property("pne.descrizioneProdottoA"), "descrizione");
				props.add(Projections.property("pne.descrizioneProdottoB"), "descrizioneB");
				props.add(Projections.property("pne.codCategoria"), "codCategoria");
				props.add(Projections.property("pne.codSottoCategoria"), "codSubCategoria");
				props.add(Projections.property("pne.barcode"), "barcode");
				props.add(Projections.property("pne.unitaMisura"), "unitaMisura");
				props.add(Projections.property("pne.aliquota"), "aliquota");
				props.add(Projections.property("pne.unitaMinimaIncremento"), "unitaMinimaIncremento");
				props.add(Projections.property("pne.nomeImmagine"), "nomeImmagine");
				props.add(Projections.property("pne.ultimoPrezzoAcquisto"), "ultimoPrezzoAcquisto");
				props.add(Projections.property("pne.posizione"), "posizione");
				props.add(Projections.property("pne.prezzo"), "prezzo");
				criteria.setProjection(props);
				criteria.setResultTransformer(Transformers.aliasToBean(ProdottoDto.class));
				return criteria.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public ProdottiNonEditorialiGenericaVo getProdottiNonEditorialiGenericaVo(Long codProdottoInterno) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiGenericaVo.class, "pneg");
		criteria.add(Restrictions.eq("pneg.codProdottoInterno", codProdottoInterno));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottiNonEditorialiSottoCategoriaEdicolaVo> getProdottiNonEditorialiSottoCategorieEdicolaVo(Long categoria, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiSottoCategoriaEdicolaVo.class, "pne");
		criteria.createCriteria("pne.edicola", "ed");
		if (codEdicola != null) {
			criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
		}
		if (categoria != null) {
			criteria.add(Restrictions.eq("pne.pk.codCategoria", categoria));
		}
		criteria.addOrder(Order.asc("pne.posizione"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public ProdottiNonEditorialiVo getProdottiNonEditorialiVo(final Long codProdotto, final Integer codEdicola) {
		HibernateCallback<ProdottiNonEditorialiVo> action = new HibernateCallback<ProdottiNonEditorialiVo>() {
			@Override
			public ProdottiNonEditorialiVo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiVo.class, "pneg");
				criteria.createCriteria("pneg.prezzi", "prezzi", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("pneg.categoria", "cat");
				criteria.createCriteria("pneg.sottocategoria", "scat");
				criteria.createCriteria("pneg.edicola", "edi");
				criteria.add(Restrictions.eq("pneg.codProdottoInterno", codProdotto));
				criteria.add(Restrictions.eq("edi.codEdicola", codEdicola));
				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				return (ProdottiNonEditorialiVo) criteria.uniqueResult();
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public void deletePrezziProdottoEdicola(Integer codEdicola, Long codProdottoInterno) {
		getDao().bulkUpdate("delete from ProdottiNonEditorialiPrezzoVenditaVo vo where vo.pk.codEdicola = ? and vo.pk.codProdottoInterno = ?", new Object[]{codEdicola, codProdottoInterno});
	}
	
	@Override
	public void deleteProdottiNonEditorialiPrezziAcquisto(Integer codEdicola, List<Long> listCodPro) {
		StringBuffer query = new StringBuffer("delete from ProdottiNonEditorialiPrezziAcquistoVo vo where vo.pk.codEdicola = ? and vo.pk.codProdotto in (");
		List<String> listParams = new ArrayList<String>();
		for (int i = 0, n = listCodPro.size(); i < n; i++) {
			listParams.add("?");
		}
		query.append(Joiner.on(",").join(listParams));
		query.append(")");
		List<Object> listParamVals = new ArrayList<Object>();
		listParamVals.add(codEdicola);
		listParamVals.addAll(listCodPro);
		getDao().bulkUpdate(query.toString(), listParamVals.toArray());
	}
	
	@Override
	public ProdottiNonEditorialiBollaVo getBolleProdottiVariEdicola(final Integer codEdicola, final Long idDocumento) {
		HibernateCallback<ProdottiNonEditorialiBollaVo> action = new HibernateCallback<ProdottiNonEditorialiBollaVo>() {
			@Override
			public ProdottiNonEditorialiBollaVo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiBollaVo.class, "pneb");
				criteria.createCriteria("pneb.dettagli", "det", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("pneb.causale", "cau", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("pneb.fornitore", "fo", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("fo.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("fo.localita", "lo", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("fo.provincia", "pv", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("pneb.edicola", "ed");
				criteria.add(Restrictions.eq("pneb.deleted", false));
				criteria.add(Restrictions.eq("pneb.idDocumento", idDocumento));
				if (codEdicola != null) {
					criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
				}
				session.enableFilter("NotDeletedFilter");
				Object res = criteria.uniqueResult();
				session.disableFilter("NotDeletedFilter");
				ProdottiNonEditorialiBollaVo uniqueResult = null;
				if (res != null) {
					uniqueResult = (ProdottiNonEditorialiBollaVo) res;
					List<ProdottiNonEditorialiBollaDettaglioVo> dettagli = select(uniqueResult.getDettagli(), notNullValue());
					if (dettagli != null && !dettagli.isEmpty()) {
						for (ProdottiNonEditorialiBollaDettaglioVo vo : ((List<ProdottiNonEditorialiBollaDettaglioVo>) dettagli)) {
							Hibernate.initialize(vo.getProdotto());
							Hibernate.initialize(vo.getCodiceProdottoFornitore());
						}
					}
				}
				return uniqueResult;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public ProdottiNonEditorialiBollaVo getBolleProdottiVariEdicola(Long idDocumento) {
		return getBolleProdottiVariEdicola(null, idDocumento);
	}
	
	@Override
	public List<ProdottiNonEditorialiCausaliMagazzinoVo> getCausali() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiCausaliMagazzinoVo.class, "cau");
		criteria.addOrder(Order.asc("cau.codiceCausale"));
		return getDao().findByDetachedCriteria(criteria);
	} 
	
	@Override
	public ProdottiNonEditorialiCausaliMagazzinoVo getCausaleMagazzino(Integer codiceCausale) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiCausaliMagazzinoVo.class, "pneb");
		criteria.add(Restrictions.eq("pneb.codiceCausale", codiceCausale));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezziAcquisto(final Integer codEdicola, final Integer codFornitore, final Boolean soloProdottiForniti, final String idProdottoFornitore, final boolean getGiacenza) {
		HibernateCallback<List<ProdottiNonEditorialiPrezziAcquistoVo>> action = new HibernateCallback<List<ProdottiNonEditorialiPrezziAcquistoVo>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ProdottiNonEditorialiPrezziAcquistoVo> doInHibernate(Session session) throws HibernateException, SQLException { 
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiPrezziAcquistoVo.class, "pnevo");
				criteria.createCriteria("pnevo.prodotto", "prod");
				criteria.createCriteria("pnevo.fornitore", "forn");
				criteria.createCriteria("prod.prezzi", "pre", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("pnevo.pk.codEdicola", codEdicola));
				if (codFornitore != null) {
					criteria.add(Restrictions.eq("forn.pk.codFornitore", codFornitore));
					criteria.add(Restrictions.eq("forn.pk.codEdicola", codEdicola));
				}
				if (soloProdottiForniti != null && soloProdottiForniti) {
					DetachedCriteria subCriteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "bc");
					subCriteria.createCriteria("bc.prodotto", "prod1");
					subCriteria.createCriteria("prod1.edicola", "edi");
					subCriteria.createCriteria("bc.causale", "cau");
					subCriteria.add(Property.forName("edi.codEdicola").eqProperty("pnevo.pk.codEdicola"));
					subCriteria.add(Property.forName("prod1.codProdottoInterno").eqProperty("prod.codProdottoInterno"));
					subCriteria.add(Property.forName("cau.codiceCausale").eq(IGerivConstants.CODICE_CAUSALE_ACQUISTO));
					subCriteria.add(Restrictions.or(Property.forName("bc.deleted").isNull(), Property.forName("bc.deleted").eq(false)));
					criteria.add(Subqueries.exists(subCriteria.setProjection(Projections.property("bc.pk.idDocumento"))));
				}
				criteria.add(Restrictions.ilike("pnevo.pk.codiceProdottoFornitore", idProdottoFornitore, MatchMode.START));
				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				List<ProdottiNonEditorialiPrezziAcquistoVo> list = criteria.list();
				if (getGiacenza) {
					for (ProdottiNonEditorialiPrezziAcquistoVo vo : list) {
						Hibernate.initialize(vo.getProdotto().getGiacenzaProdotto());
					}
				}
				return list;
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezziAcquisto(Integer codEdicola, Integer codFornitore, Boolean soloProdottiForniti, String idProdottoFornitore) {
		return getProdottiNonEditorialiPrezziAcquisto(codEdicola, codFornitore, soloProdottiForniti, idProdottoFornitore, false);
	}
	
	@Override
	public List<ProdottoDto> getProdottiNonEditorialiEdicolaByDescrizione(final String descrizione, final Integer codEdicola, final Integer codFornitore, final Boolean soloProdottiForniti) {
		HibernateCallback<List<ProdottoDto>> action = new HibernateCallback<List<ProdottoDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ProdottoDto> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiPrezziAcquistoVo.class, "prea");
				criteria.createCriteria("prea.prodotto", "prod", JoinType.RIGHT_OUTER_JOIN);
				criteria.createCriteria("prod.edicola", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("prod.prezzi", "pre", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("prod.edicola.codEdicola", codEdicola));
				if (codFornitore != null) {
					criteria.add(Restrictions.eq("prea.codiceFornitore", codFornitore));
				}
				if (soloProdottiForniti != null && soloProdottiForniti) {
					DetachedCriteria subCriteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "bc");
					subCriteria.createCriteria("bc.prodotto", "prod1");
					subCriteria.createCriteria("prod1.edicola", "edi");
					subCriteria.createCriteria("bc.causale", "cau");
					subCriteria.add(Property.forName("edi.codEdicola").eqProperty("prea.pk.codEdicola"));
					subCriteria.add(Property.forName("prod1.codProdottoInterno").eqProperty("prod.codProdottoInterno"));
					subCriteria.add(Property.forName("cau.codiceCausale").eq(IGerivConstants.CODICE_CAUSALE_ACQUISTO));
					subCriteria.add(Restrictions.or(Property.forName("bc.deleted").isNull(), Property.forName("bc.deleted").eq(false)));
					criteria.add(Subqueries.exists(subCriteria.setProjection(Projections.property("bc.pk.idDocumento"))));
				}
				criteria.add(Restrictions.ilike("prod.descrizioneProdottoA", descrizione, MatchMode.ANYWHERE));
				ProjectionList props = Projections.projectionList(); 
				props.add(Projections.distinct(Projections.projectionList().add(Projections.property("prod.codProdottoInterno"), "codProdottoInterno")));
				props.add(Projections.property("prod.descrizioneProdottoA"), "descrizione");
				props.add(Projections.property("prod.barcode"), "barcode");
				props.add(Projections.property("prod.aliquota"), "aliquota");
				props.add(Projections.property("prod.codiceProdottoFornitore"), "codiceProdottoFornitore");
				props.add(Projections.property("prod.ultimoPrezzoAcquisto"), "ultimoPrezzoAcquisto");
				props.add(Projections.property("prod.prodottoDl"), "prodottoDl");
				props.add(Projections.property("prod.percentualeResaSuDistribuito"), "percentualeResaSuDistribuito");
				props.add(Projections.property("prod.giacenzaProdotto"), "giacenzaProdotto");
				criteria.setProjection(props);
				criteria.setResultTransformer(Transformers.aliasToBean(ProdottoDto.class));
				return criteria.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicolaByCodOrDescr(Integer codEdicola, Long codProdotto, String descrizione) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiVo.class, "prod");
		criteria.createCriteria("prod.edicola", "edi", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("edi.codEdicola", codEdicola));
		if (codProdotto != null) {
			criteria.add(Restrictions.eq("prod.codProdottoInterno", codProdotto));
		}
		if (descrizione != null) {
			criteria.add(Restrictions.ilike("prod.descrizioneProdottoA", descrizione, MatchMode.ANYWHERE));
		}
		criteria.addOrder(Order.asc("prod.descrizioneProdottoA"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottiNonEditorialiVo> getProdottiNonEditorialiEdicolaByCodEdicolaOrDescr(Integer codEdicola, String descrizione) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiVo.class, "prod");
		criteria.createCriteria("prod.edicola", "edi", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("edi.codEdicola", codEdicola));
		if (descrizione != null) {
			criteria.add(Restrictions.or(Restrictions.ilike("prod.descrizioneProdottoA", descrizione, MatchMode.ANYWHERE), Restrictions.ilike("prod.codProdottoEsterno", descrizione, MatchMode.ANYWHERE)));
		}
		criteria.addOrder(Order.asc("prod.descrizioneProdottoA"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public void deleteBollaProdottiVari(Long idDocumento) {
		getDao().bulkUpdate("delete from ProdottiNonEditorialiBollaDettaglioVo vo where vo.pk.idDocumento = ?", new Object[]{idDocumento});
		getDao().bulkUpdate("delete from ProdottiNonEditorialiBollaVo vo where vo.idDocumento = ?", new Object[]{idDocumento});
	}
	
	@Override
	public ProdottiNonEditorialiPrezziAcquistoVo getProdottiNonEditorialiPrezzoAcquisto(Integer codEdicola, Long codProdotto, String codiceProdottoFornitore) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiPrezziAcquistoVo.class, "prod");
		criteria.createCriteria("prod.fornitore");
		criteria.add(Restrictions.eq("prod.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("prod.pk.codProdotto", codProdotto));
		if (codiceProdottoFornitore != null) {
			criteria.add(Restrictions.eq("prod.pk.codiceProdottoFornitore", codiceProdottoFornitore));
		}
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public ProdottiNonEditorialiPrezziAcquistoVo getProdottoNonEditorialiPrezzoAcquistoByCodFornitore(Integer codEdicola, Integer codiceFornitore, String codiceProdottoFornitore) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiPrezziAcquistoVo.class, "prod");
		criteria.createCriteria("prod.fornitore", "fo");
		criteria.add(Restrictions.eq("prod.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("prod.pk.codiceProdottoFornitore", codiceProdottoFornitore));
		criteria.add(Restrictions.eq("fo.pk.codFornitore", codiceFornitore));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezzoAcquistoNonUsatiInBolla(Integer codEdicola, Integer codFornitore) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiPrezziAcquistoVo.class, "pa");
		criteria.createCriteria("pa.fornitore", "forn");
		criteria.createCriteria("pa.prodotto", "prod");
		criteria.add(Restrictions.eq("pa.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("forn.pk.codFornitore", codFornitore));
		criteria.add(Restrictions.ne("prod.prodottoDl", true));
		DetachedCriteria subCriteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "pnevo");
		subCriteria.createCriteria("pnevo.prodotto", "prod");
		subCriteria.createCriteria("prod.edicola", "edi");
		subCriteria.createCriteria("pnevo.causale", "cau");
		subCriteria.add(Property.forName("edi.codEdicola").eqProperty("pa.pk.codEdicola"));
		subCriteria.add(Property.forName("prod.codProdottoInterno").eqProperty("pa.pk.codProdotto"));
		subCriteria.add(Property.forName("cau.codiceCausale").eq(IGerivConstants.CODICE_CAUSALE_ACQUISTO));
		subCriteria.add(Property.forName("pnevo.deleted").eq(false));
		criteria.add(Subqueries.notExists(subCriteria.setProjection(Projections.property("pnevo.pk.idDocumento"))));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottiNonEditorialiPrezziAcquistoVo> getProdottiNonEditorialiPrezziAcquistoByDescrizione(Integer codEdicola, String descrizione) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiPrezziAcquistoVo.class, "prod");
		criteria.createCriteria("prod.prodotto", "prodotto");
		criteria.add(Restrictions.eq("prod.pk.codEdicola", codEdicola));
		criteria.add(Restrictions.ilike("prodotto.descrizioneProdottoA", descrizione, MatchMode.START));
		return getDao().findByDetachedCriteria(criteria);
	}
	

	@Override
	public ProdottiNonEditorialiCategoriaVo getProdottiNonEditorialiCategoriaVo(Long codCategoria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiCategoriaVo.class, "pneg");
		criteria.add(Restrictions.eq("pneg.codCategoria", codCategoria));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public ProdottiNonEditorialiSottoCategoriaVo getProdottiNonEditorialiSottoCategoriaVo(Long codCategoria, Long codSottoCategoria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiSottoCategoriaVo.class, "pneg");
		criteria.add(Restrictions.eq("pneg.pk.codCategoria", codCategoria));
		criteria.add(Restrictions.eq("pneg.pk.codSottoCategoria", codSottoCategoria));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottiNonEditorialiBollaVo> getBollaProdottiVariEdicolaByFornitore(Integer codEdicola, Integer codFornitore) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaVo.class, "pneb");
		criteria.createCriteria("pneb.edicola", "ed");
		criteria.createCriteria("pneb.fornitore", "fo");
		criteria.add(Restrictions.eq("pneb.deleted", false));
		criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("fo.pk.codFornitore", codFornitore));
		return getDao().findByDetachedCriteria(criteria);
	};
	
	@Override
	public List<ProdottiNonEditorialiBollaVo> getBolleProdottiVariEdicola(Integer codEdicola, Integer codFornitore, Integer codiceCausale, Timestamp dataDocumento, String numeroDocumento, boolean excludeDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaVo.class, "pneb");
		criteria.createCriteria("pneb.edicola", "ed");
		criteria.add(Restrictions.eq("pneb.deleted", false));
		criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
		if (codFornitore != null) {
			criteria.createCriteria("pneb.fornitore", "fo");
			criteria.add(Restrictions.eq("fo.pk.codFornitore", codFornitore));
		}
		if (codiceCausale != null) {
			criteria.createCriteria("pneb.causale", "cau", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.eq("cau.codiceCausale", codiceCausale));
		}
		if (dataDocumento != null) {
			criteria.add(Restrictions.eq("pneb.dataDocumento", dataDocumento));
		}
		if (numeroDocumento != null && !numeroDocumento.equals("")) {
			criteria.add(Restrictions.eq("pneb.numeroDocumento", numeroDocumento));
		}
		if (excludeDl) {
			if (codFornitore == null) {
				criteria.createCriteria("pneb.fornitore", "fo");
			}
			criteria.add(Restrictions.ge("fo.pk.codFornitore", IGerivConstants.COD_INIZIO_FORNITORI_NON_DL));
		}
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public void deleteDettagliBollaProdotto(Integer codEdicola, Long codProdottoInterno) {
		getDao().bulkUpdate("delete from ProdottiNonEditorialiBollaDettaglioVo vo where cpro9546 = ? and crivw9546 = ?", new Object[]{codProdottoInterno, codEdicola});
	}
	
	@Override
	public void deletePrezziAcquistoProdotto(Integer codEdicola, Long codProdottoInterno) {
		getDao().bulkUpdate("delete from ProdottiNonEditorialiPrezziAcquistoVo vo where vo.pk.codProdotto = ? and vo.pk.codEdicola = ?", new Object[]{codProdottoInterno, codEdicola});
	}
	
	@Override
	public ProdottiNonEditorialiCategoriaEdicolaVo getProdottiNonEditorialiCategoriaEdicolaVo(Long codCategoria, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiCategoriaEdicolaVo.class, "cat");
		criteria.add(Restrictions.eq("cat.pk.codCategoria", codCategoria));
		criteria.add(Restrictions.eq("cat.pk.codEdicola", codEdicola));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public ProdottiNonEditorialiSottoCategoriaEdicolaVo getProdottiNonEditorialiSottoCategoriaEdicolaVo(Long codCategoria, Long codSottoCategoria, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiSottoCategoriaEdicolaVo.class, "cat");
		criteria.add(Restrictions.eq("cat.pk.codCategoria", codCategoria));
		criteria.add(Restrictions.eq("cat.pk.codSottoCategoria", codSottoCategoria));
		criteria.add(Restrictions.eq("cat.pk.codEdicola", codEdicola));
		criteria.addOrder(Order.asc("cat.posizione"));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottiNonEditorialiCategoriaEdicolaVo> getCategorieProdottiNonEditorialiEdicola(Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiCategoriaEdicolaVo.class, "cat");
		criteria.add(Restrictions.eq("cat.pk.codEdicola", codEdicola));
		criteria.addOrder(Order.asc("cat.posizione"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ReportMagazzinoPneDto> getReportMagazzinoPne(final Integer codRivendita, final Timestamp dataIniziale, final Timestamp dataFinale, final List<Integer> codContoList, final List<Integer> codCategoriaList, final List<Integer> codCausaleList, final Integer codProdotto) {
		HibernateCallback<List<ReportMagazzinoPneDto>> action = new HibernateCallback<List<ReportMagazzinoPneDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ReportMagazzinoPneDto> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer query = new StringBuffer();
				query.append("select cpro9506 as codProdotto, cat9506 as codCategoria, scat9506 as codSottocategoria, datar9545 as dataRegistrazione, cforn9545 as conto, nomea9500 as fornitore, desc9506 as descrizioneProdotto, sum(qt9546) as quantitaProdotto, prezzo9546 as prezzoProdotto, sum(qt9546) * prezzo9546 as importo, des9550 as causale ");
				query.append("from tbl_9545 inner join tbl_9546 on (id9545 = id9546) inner join tbl_9506 on (cpro9546 = cpro9506) left outer join tbl_9521 on (cat9506 = cat9521) left outer join tbl_9523 on (cat9506 = cat9523 and crivw9523 = crivw9545) inner join tbl_9550 on (caus9546 = cau9550) left outer join tbl_9500 on (crivw9545 = crivw9500 and cforn9545 = cforn9500) ");
				query.append("where crivw9545 = :codRivendita and trunc(datar9545) between :dataIniziale and :dataFinale ");
				if (!codContoList.isEmpty()) {
					query.append("and cforn9545 in (");
					for (int i = 0, n = codContoList.size(); i < n; i++) {
						query.append(String.format(":cforn%d, ", i));
					}
					if (!codCausaleList.isEmpty() && codCausaleList.contains(IGerivConstants.CODICE_CAUSALE_VENDITA)) {
						query.append(IGerivConstants.COD_CLIENTE_ANONIMO + ",");
					}
					query.append("-1) ");
				}
				if (!codCategoriaList.isEmpty()) {
					query.append("and (cat9521 in (");
					for (int i = 0, n = codCategoriaList.size(); i < n; i++) {
						query.append(String.format(":cat%d, ", i));
					}
					query.append("-1) or cat9523 in(");
					for (int i = 0, n = codCategoriaList.size(); i < n; i++) {
						query.append(String.format(":cat%d, ", i));
					}
					query.append("-1)) ");
				}
				if (!codCausaleList.isEmpty()) {
					query.append("and caus9546 in (");
					for (int i = 0, n = codCausaleList.size(); i < n; i++) {
						query.append(String.format(":caus%d, ", i));
					}
					query.append("-1) ");
				}
				if (codProdotto != null) {
					query.append(" and cpro9546 = :codProdotto ");
				}
				query.append("group by cpro9506, cat9506, scat9506, datar9545, cforn9545, nomea9500, desc9506, prezzo9546, des9550 ");
				query.append("order by datar9545 desc, desc9506");
				
				SQLQuery sqlQuery = session.createSQLQuery(query.toString());
				sqlQuery.setInteger("codRivendita", codRivendita);
				sqlQuery.setTimestamp("dataIniziale", dataIniziale);
				sqlQuery.setTimestamp("dataFinale", dataFinale);
				if (!codContoList.isEmpty()) {
					for (int i = 0, n = codContoList.size(); i < n; i++) {
						Integer codConto = codContoList.get(i);
						sqlQuery.setInteger(String.format("cforn%d", i), codConto);
					}
				}
				if (!codCategoriaList.isEmpty()) {
					for (int i = 0, n = codCategoriaList.size(); i < n; i++) {
						Integer codCategoria = codCategoriaList.get(i);
						sqlQuery.setInteger(String.format("cat%d", i), codCategoria);
					}
				}
				if (!codCausaleList.isEmpty()) {
					for (int i = 0, n = codCausaleList.size(); i < n; i++) {
						Integer codCausale = codCausaleList.get(i);
						sqlQuery.setInteger(String.format("caus%d", i), codCausale);
					}
				}
				if (codProdotto!= null) {
					sqlQuery.setInteger("codProdotto", codProdotto);
				}
				sqlQuery.setResultTransformer(Transformers.aliasToBean(ReportMagazzinoPneDto.class));
				sqlQuery.addScalar("codProdotto", LongType.INSTANCE);
				sqlQuery.addScalar("codCategoria", LongType.INSTANCE);
				sqlQuery.addScalar("codSottocategoria", LongType.INSTANCE);
				sqlQuery.addScalar("dataRegistrazione", TimestampType.INSTANCE);
				sqlQuery.addScalar("descrizioneProdotto", StringType.INSTANCE);
				sqlQuery.addScalar("quantitaProdotto", IntegerType.INSTANCE);
				sqlQuery.addScalar("prezzoProdotto", FloatType.INSTANCE);
				sqlQuery.addScalar("importo", FloatType.INSTANCE);
				sqlQuery.addScalar("conto", IntegerType.INSTANCE);
				sqlQuery.addScalar("fornitore", StringType.INSTANCE);
				sqlQuery.addScalar("causale", StringType.INSTANCE);
				List<ReportMagazzinoPneDto> list = sqlQuery.list();
				if (!codCausaleList.isEmpty() && codCausaleList.contains(IGerivConstants.CODICE_CAUSALE_ACQUISTO)) {
					for (ReportMagazzinoPneDto dto : list) {
						List<GiacenzaPneDto> listGiac = getGiacenzaPne(codRivendita, dto.getCodCategoria(), dto.getCodSottocategoria(), dto.getCodProdotto());
						if (!listGiac.isEmpty()) {
							dto.setGiacenzaProdotto(listGiac.get(0).getGiacenza());
						}
					}
				}
				return list;
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<GiacenzaPneDto> getGiacenzaPne(final Integer codEdicola, final Long codCategoria, final Long codSottoCategoria, final Long codProdotto) {
		HibernateCallback<List<GiacenzaPneDto>> action = new HibernateCallback<List<GiacenzaPneDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<GiacenzaPneDto> doInHibernate(Session session) throws HibernateException, SQLException {
				String query = IGerivQueryContants.SQL_QUERY_GET_GIACENZA_PRODOTTI_NON_EDITORIALI;
				//CDL RICHIESTA - AGGIUNGERE PREZZO UNITARIO E TOTALE PER OGNI RIGA ESCLUDENDO VALORI NEGATIVI
				//String query = IGerivQueryContants.SQL_QUERY_GET_GIACENZA_PRODOTTI_NON_EDITORIALI_PREZZO_UNITARIO_TOTALE;
				//CDL RICHIESTA 19/02/2016 è stata ripristinata la precedente query perchè la modifica effettuata non era adeguata.
				
				StringBuffer whereClause = new StringBuffer();
				if (codCategoria != null) {
					whereClause.append("and cat9506 = :codCategoria ");
					if (codSottoCategoria != null) {
						whereClause.append("and scat9506 = :codSottoCategoria ");
					}
				}
				if (codProdotto != null) {
					whereClause.append("and cpro9506 = :codProdotto ");
				}
				query = query.replace(":where", whereClause.toString());
				SQLQuery createSQLQuery = session.createSQLQuery(query);
				createSQLQuery.setInteger("codEdicola", codEdicola);
				if (codCategoria != null) {
					createSQLQuery.setLong("codCategoria", codCategoria);
					if (codSottoCategoria != null) {
						createSQLQuery.setLong("codSottoCategoria", codSottoCategoria);
					}
				}
				if (codProdotto != null) {
					createSQLQuery.setLong("codProdotto", codProdotto);
				}
				createSQLQuery.setResultTransformer( Transformers.aliasToBean(GiacenzaPneDto.class));
				createSQLQuery.addScalar("descrizione", StringType.INSTANCE);
				createSQLQuery.addScalar("giacenza", IntegerType.INSTANCE);
				//createSQLQuery.addScalar("prezzounitario", FloatType.INSTANCE);
				//createSQLQuery.addScalar("prezzototale", FloatType.INSTANCE);
			
				
				return createSQLQuery.list();
			}
		};
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public void updateCategoryPosition(Integer posizione, Long idCat, Integer codEdicola) {
		getDao().bulkUpdate("update ProdottiNonEditorialiCategoriaEdicolaVo vo set vo.posizione = ? where vo.pk.codCategoria = ? and vo.pk.codEdicola = ?", new Object[]{posizione, idCat, codEdicola});
	}
	
	@Override
	public void updateSubCategoryPosition(Integer posizione, Long idCat, Long idSCat, Integer codEdicola) {
		getDao().bulkUpdate("update ProdottiNonEditorialiSottoCategoriaEdicolaVo vo set vo.posizione = ? where vo.pk.codCategoria = ? and vo.pk.codSottoCategoria = ? and vo.pk.codEdicola = ?", new Object[]{posizione, idCat, idSCat, codEdicola});
	}
	
	@Override
	public void updateSubCategoryPosition(Integer posizione, Long idProd) {
		getDao().bulkUpdate("update ProdottiNonEditorialiVo vo set vo.posizione = ? where vo.codProdottoInterno = ?", new Object[]{posizione, idProd});
	}
	
	@Override
	public boolean codiceProdottoEdicolaExists(String codiceProdotto, Integer codEdicola) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiVo.class, "pne");
		criteria.createCriteria("pne.edicola", "ed");
		criteria.add(Restrictions.eq("pne.codProdottoEsterno", codiceProdotto));
		criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("pne.codProdottoInterno"));
		criteria.setProjection(props);
		Long codProd = getDao().findUniqueResultObjectByDetachedCriteria(criteria);
		return (codProd == null) ? false : true;
	}
	
	@Override
	public void deleteProdottiNonEditorialiBollaDettaglio(Long idDocumento) {
		getDao().bulkUpdate("delete from ProdottiNonEditorialiBollaDettaglioVo vo where vo.pk.idDocumento = ?", new Object[]{idDocumento});
	}
	
	@Override
	public void deleteBolleProdottiVariEdicolaBeforeDate(Integer codEdicola, Date date) {
		getDao().bulkUpdate("delete from ProdottiNonEditorialiBollaDettaglioVo vo where vo.pk.idDocumento in (select vo1.idDocumento from ProdottiNonEditorialiBollaVo vo1 where vo1.dataDocumento < ? and vo1.edicola.codEdicola = ?)", new Object[]{new Timestamp(date.getTime()), codEdicola});
		getDao().bulkUpdate("delete from ProdottiNonEditorialiBollaVo vo where vo.dataDocumento < ? and vo.edicola.codEdicola = ?", new Object[]{new Timestamp(date.getTime()), codEdicola});
	}
	
	@Override
	public List<ProdottiNonEditorialiAliquotaIvaVo> getListAliquoteIva() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiAliquotaIvaVo.class, "aivo");
		criteria.addOrder(Order.asc("aivo.aliquota"));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public List<ProdottiNonEditorialiCausaliContabilitaVo> getCausaliIva(Integer aliquota) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiCausaliContabilitaVo.class, "ccvo");
		criteria.add(Restrictions.eq("ccvo.pk.tipoCausale", IGerivConstants.COD_TIPO_CAUSALI_IVA));
		criteria.add(Restrictions.eq("ccvo.tipoDocumento", IGerivConstants.COD_FATTURA_VENDITA));
		criteria.add(Restrictions.eq("ccvo.tipoIva", (aliquota != null && aliquota > 0) ? IGerivConstants.COD_TIPO_IVA_DETRAIBILE : IGerivConstants.COD_TIPO_IVA_ESENTE));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public ProdottiNonEditorialiCausaliContabilitaVo getCausaleContabilita(Integer tipoCausale, Integer codiceCausale) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiCausaliContabilitaVo.class, "ccvo");
		criteria.add(Restrictions.eq("ccvo.pk.tipoCausale", tipoCausale));
		criteria.add(Restrictions.eq("ccvo.pk.codiceCausale", codiceCausale));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public ProdottiNonEditorialiVo getFirstProdottoNonEditorialeEdicola(final Long codCategoria, final Long codSottoCategoria, final Integer codEdicola) {
		HibernateCallback<ProdottiNonEditorialiVo> action = new HibernateCallback<ProdottiNonEditorialiVo>() {
			@Override
			public ProdottiNonEditorialiVo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiVo.class, "pne");
				criteria.createCriteria("pne.edicola", "ed");
				criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
				if (codCategoria != null) {
					criteria.add(Restrictions.eq("pne.codCategoria", codCategoria));
				}
				if (codSottoCategoria != null) {
					criteria.add(Restrictions.eq("pne.codSottoCategoria", codSottoCategoria));
				}
				criteria.setMaxResults(1);
				Object uniqueResult = criteria.uniqueResult();
				return (uniqueResult != null) ? (ProdottiNonEditorialiVo) uniqueResult : null;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public ProdottiNonEditorialiSottoCategoriaEdicolaVo getFirstSottocategoriaNonEditorialeEdicola(final Long codCategoria, final Integer codEdicola) {
		HibernateCallback<ProdottiNonEditorialiSottoCategoriaEdicolaVo> action = new HibernateCallback<ProdottiNonEditorialiSottoCategoriaEdicolaVo>() {
			@Override
			public ProdottiNonEditorialiSottoCategoriaEdicolaVo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiSottoCategoriaEdicolaVo.class, "pne");
				criteria.createCriteria("pne.edicola", "ed");
				criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
				criteria.add(Restrictions.eq("pne.pk.codCategoria", codCategoria));
				criteria.setMaxResults(1);
				Object uniqueResult = criteria.uniqueResult();
				return (uniqueResult != null) ? (ProdottiNonEditorialiSottoCategoriaEdicolaVo) uniqueResult : null;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public List<ProdottiNonEditorialiBollaVo> getReseProdottiVariEdicola(Integer codEdicola, Integer codFornitore, Timestamp dataDocumento, String numeroDocumento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaVo.class, "pner");
		criteria.createCriteria("pner.edicola", "ed");
		criteria.createCriteria("pner.fornitore", "fo");
		criteria.createCriteria("pner.causale", "ca");
		criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("pner.codiceFornitore", codFornitore));
		if (codFornitore != null) {
			criteria.add(Restrictions.eq("pner.codiceFornitore", codFornitore));
		}
		if (dataDocumento != null) {
			criteria.add(Restrictions.eq("pner.dataDocumento", dataDocumento));
		}
		if (numeroDocumento != null) {
			criteria.add(Restrictions.eq("pner.numeroDocumento", numeroDocumento));
		}
		criteria.add(Restrictions.eq("ca.codiceCausale", IGerivConstants.CODICE_CAUSALE_RESA));
		return getDao().findByDetachedCriteria(criteria);
	}
	
	@Override
	public ProdottiNonEditorialiBollaVo getResaProdottiVariEdicola(final Long idDocumento) {
		HibernateCallback<ProdottiNonEditorialiBollaVo> action = new HibernateCallback<ProdottiNonEditorialiBollaVo>() {
			@Override
			public ProdottiNonEditorialiBollaVo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(ProdottiNonEditorialiBollaVo.class, "pner");
				criteria.createCriteria("pner.dettagli", "det");
				criteria.createCriteria("pner.edicola", "ed");
				criteria.createCriteria("pner.fornitore", "fo");
				criteria.createCriteria("fo.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("fo.localita", "lo", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("fo.provincia", "pv", JoinType.LEFT_OUTER_JOIN);
				criteria.createCriteria("pner.documentoResa", "doc", JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("pner.idDocumento", idDocumento));
				Object result = criteria.uniqueResult();
				ProdottiNonEditorialiBollaVo uniqueResult = result != null ? (ProdottiNonEditorialiBollaVo) result : null;
				if (uniqueResult != null && uniqueResult.getDettagli() != null && !uniqueResult.getDettagli().isEmpty()) {
					Hibernate.initialize(uniqueResult.getDettagli());
					for (ProdottiNonEditorialiBollaDettaglioVo vo : uniqueResult.getDettagli()) {
						Hibernate.initialize(vo.getProdotto().getGiacenzaProdotto());
					}
				}
				return uniqueResult;
			}
		};
		return getDao().findUniqueResultByHibernateCallback(action);
	}
	
	@Override
	public Timestamp getLastDataBollaResa(Integer codFornitore, Integer codEdicola, Long codResaExclude) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaVo.class, "pner");
		criteria.createCriteria("pner.edicola", "ed");
		criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("pner.codiceFornitore", codFornitore));
		//05/01/2018 Modifica per CDL errore in fase di salvataggio perchè recuperava tutti i doc
		criteria.add(Restrictions.eq("pner.indicatoreEmessoRicevuto", new Integer("1")));
		
		if (codResaExclude != null) {
			criteria.add(Restrictions.ne("pner.idDocumento", codResaExclude));
		}
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.max("pner.dataDocumento"));
		criteria.setProjection(props);
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public Long getQuantitaResa(Integer codEdicola, Long codProdotto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "pner");
		criteria.createCriteria("pner.bolla", "re");
		criteria.createCriteria("re.edicola", "ed");
		criteria.createCriteria("pner.prodotto", "pr");
		criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("pr.codProdottoInterno", codProdotto));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.sum("pner.quantita"));
		criteria.setProjection(props);
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public Long getQuantitaDistribuita(Integer codEdicola, Long codProdotto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "pne");
		criteria.createCriteria("pne.bolla", "bol");
		criteria.createCriteria("bol.edicola", "ed");
		criteria.createCriteria("pne.prodotto", "prod");
		criteria.add(Restrictions.eq("prod.codProdottoInterno", codProdotto));
		criteria.add(Restrictions.eq("ed.codEdicola", codEdicola));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.sum("pne.quantita"));
		criteria.setProjection(props);
		return getDao().findUniqueResultObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public void deleteBollaResaProdottiVari(Long codResa) {
		getDao().bulkUpdate("delete from ProdottiNonEditorialiBollaDettaglioVo vo where vo.pk.idDocumento = ?", new Object[]{codResa});
		getDao().bulkUpdate("delete from ProdottiNonEditorialiBollaVo vo where vo.idDocumento = ?", new Object[]{codResa});
	}
	
	@Override
	public List<BollaResaProdottiVariDto> getDettagliBollaResaProdottiVariDto(Long idResa) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiBollaDettaglioVo.class, "prde");
		criteria.createCriteria("prde.bolla", "re");
		criteria.createCriteria("re.fornitore", "fo");
		criteria.createCriteria("fo.tipoLocalita", "tl", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("fo.localita", "lo", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("fo.provincia", "pv", JoinType.LEFT_OUTER_JOIN);
		criteria.createCriteria("prde.prodotto", "pr");
		criteria.add(Restrictions.eq("prde.pk.idDocumento", idResa));
		ProjectionList props = Projections.projectionList(); 
		props.add(Projections.property("prde.pk.idDocumento"), "idDocumento");
		props.add(Projections.property("prde.quantita"), "quantitaResa");
		props.add(Projections.property("prde.prezzo"), "prezzo");
		props.add(Projections.property("re.numeroDocumento"), "numeroDocumento");
		props.add(Projections.property("re.dataDocumento"), "dataDocumento");
		props.add(Projections.property("pr.descrizioneProdottoA"), "descrizioneProdotto");
		props.add(Projections.property("prde.codiceProdottoFornitore"), "codiceProdottoFornitore");
		props.add(Projections.property("fo.pk.codFornitore"), "codFornitore");
		props.add(Projections.property("fo.nome"), "nomeFornitore");
		props.add(Projections.property("tl.descrizione"), "tipoLocalita");
		props.add(Projections.property("fo.indirizzo"), "indirizzo");
		props.add(Projections.property("fo.numeroCivico"), "numeroCivico");
		props.add(Projections.property("fo.estensione"), "estensione");
		props.add(Projections.property("lo.descrizione"), "localita");
		props.add(Projections.property("pv.descrizione"), "provincia");
		props.add(Projections.property("fo.cap"), "cap");
		props.add(Projections.property("fo.piva"), "piva");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(BollaResaProdottiVariDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public ProdottiNonEditorialiDocumentiEmessiVo getProdottiNonEditorialiDocumentiEmessi(Long codForn, Long idDoc) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdottiNonEditorialiDocumentiEmessiVo.class, "pnedoc");
		criteria.add(Restrictions.eq("pnedoc.idDocumento", idDoc));
		criteria.add(Restrictions.eq("pnedoc.codFornitore", codForn));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
}
