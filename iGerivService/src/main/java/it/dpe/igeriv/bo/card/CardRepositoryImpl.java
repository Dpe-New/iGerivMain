package it.dpe.igeriv.bo.card;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.VenditaDettaglioDto;
import it.dpe.igeriv.exception.ConfirmRiassociareSmartCardEdicolaException;
import it.dpe.igeriv.exception.SmartCardEdicolaGiaAssociataException;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.ClienteEdicolaVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.IGerivCardVo;
import it.dpe.igeriv.vo.UserVo;
import it.dpe.igeriv.vo.pk.IGerivCardPk;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;

@Repository
class CardRepositoryImpl extends BaseRepositoryImpl implements CardRepository {
	private final MenuService menuService;
	private final String maxResultsPreferitiSmartEdicola;
	
	@Autowired
	CardRepositoryImpl(BaseDao<UserVo> dao, MenuService menuService, @Value("${igeriv.preferiti.smart.edicola.max.results}") String maxResultsPreferitiSmartEdicola) {
		super(dao);
		this.menuService = menuService;
		this.maxResultsPreferitiSmartEdicola = maxResultsPreferitiSmartEdicola;
	}
	
	@Override
	public IGerivCardVo getIGerivCardVo(String inputText, Integer idEdicola, Long codCliente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(IGerivCardVo.class, "ic");
		criteria.createCriteria("ic.cliente", "cli", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ic.pk.codEdicola", idEdicola));
		if (!Strings.isNullOrEmpty(inputText)) {
			criteria.add(Restrictions.eq("ic.pk.card", inputText));
		}
		if (codCliente != null) {
			criteria.add(Restrictions.eq("ic.codCliente", codCliente));
		}
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<VenditaDettaglioDto> getUltimiAcquistiIGerivCard(final String inputText, final Integer codEdicola, final Integer codFiegDl, final Integer giorni, final Long codCliente, final Timestamp dataStorico) {
		HibernateCallback<List<VenditaDettaglioDto>> action = new HibernateCallback<List<VenditaDettaglioDto>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<VenditaDettaglioDto> doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = IGerivQueryContants.SQL_QUERY_GET_PREFERITI;
				SQLQuery createSQLQuery = session.createSQLQuery(sql);
				createSQLQuery.setInteger("coddl", codFiegDl);
				createSQLQuery.setInteger("codEdicola", codEdicola);
				createSQLQuery.setLong("codCliente", codCliente);
				createSQLQuery.setInteger("giorni", giorni);
				createSQLQuery.setTimestamp("dataStorico", (dataStorico == null) ? new Timestamp(new Date(0L).getTime()) : dataStorico);
				createSQLQuery.setInteger("maxResults", new Integer(maxResultsPreferitiSmartEdicola));
				createSQLQuery.setResultTransformer( Transformers.aliasToBean(VenditaDettaglioDto.class));
				createSQLQuery.addScalar("titolo", StringType.INSTANCE);
				createSQLQuery.addScalar("sottoTitolo",StringType.INSTANCE);
				createSQLQuery.addScalar("numeroCopertina", StringType.INSTANCE);
				return createSQLQuery.list();
			}
		}; 
		return getDao().findByHibernateCallback(action);
	}
	
	@Override
	public List<VenditaDettaglioDto> getSuggerimentiVendita(String inputText, Integer idEdicola) {
		return null;
	}
	
	private void saveIGerivCard(String barcode, Integer codEdicola, Long codCliente) {
		if (codCliente == -1) {
			codCliente = new Long(barcode);
			ClienteEdicolaVo cliente = new ClienteEdicolaVo();
			cliente.setCodEdicola(codEdicola);
			cliente.setCodCliente(codCliente);
			GruppoModuliVo gruppoModuliVo = menuService.getGruppoModuliByRole(IGerivConstants.ROLE_IGERIV_CLIENTE_EDICOLA);
			cliente.setGruppoModuliVo(gruppoModuliVo);
			cliente.setNome(barcode);
			cliente.setPassword(barcode);
			cliente.setPwdCriptata(0);
			cliente.setChangePassword(1);
			saveBaseVo(cliente);
		} 
		IGerivCardVo vo = new IGerivCardVo();
		IGerivCardPk pk = new IGerivCardPk();
		pk.setCard(barcode);
		pk.setCodEdicola(codEdicola);
		vo.setPk(pk);
		vo.setCodCliente(codCliente);
		saveBaseVo(vo);
	}
	
	@Override
	public void associaIGerivCard(String barcode, Long codCliente, Integer codEdicola, boolean byPassClienteCheck) throws Exception {
		try {
			IGerivCardVo card = getIGerivCardVo(null, codEdicola, codCliente);
			if (!byPassClienteCheck) {
				if (card != null) {
					throw new ConfirmRiassociareSmartCardEdicolaException();
				}
			} else if (card != null) {
				deleteVo(card);
			}
			card = getIGerivCardVo(barcode, codEdicola, null);
			if (card != null && !byPassClienteCheck) {
				throw new SmartCardEdicolaGiaAssociataException();
			} else {
				saveIGerivCard(barcode, codEdicola, codCliente);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
}
