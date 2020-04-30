package it.dpe.igeriv.bo.ws;

import it.dpe.igeriv.bo.BaseRepositoryImpl;
import it.dpe.igeriv.dao.BaseDao;
import it.dpe.igeriv.dto.EdicolaDto;
import it.dpe.igeriv.util.IGerivQueryContants;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.ClientCompatibilitaVersioniVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.RichiestaDatiWSVo;
import it.dpe.igeriv.vo.UtenteAgenziaVo;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Implementazione dell'interfaccia business WS per iGeriv. 
 * 
 * @author romanom
 *
 */
@Repository("IGerivWSBo")
class IGerivWSBoImpl extends BaseRepositoryImpl implements IGerivWSBo {
	
	@Autowired
	IGerivWSBoImpl(BaseDao<?> dao) {
		super(dao);
	}
	
	@Override
	public UtenteAgenziaVo getAgenziaByCodice(Integer codFiegDl) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_UTENTE_AGENZIA_BY_ID_JOINS, codFiegDl);
	}
	
	@Override
	public AbbinamentoEdicolaDlVo getAbbinamentoEdicolaDlVoByCodFiegDlCodRivDl(Integer codiceDl, Integer codiceRivendita) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_ABBINAMENTO_EDICOLA_DL, codiceRivendita, codiceDl);
	}
	
	@Override
	public Long getNextSeqVal(String seqClientiEdicola) {
		return getDao().getNextSeqVal(seqClientiEdicola);
	}
	
	@Override
	public DlGruppoModuliVo getDlGruppoModuliVo(Integer idGruppoModuli, Integer codDl) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DlGruppoModuliVo.class, "dlgmvo");
		criteria.add(Restrictions.eq("dlgmvo.pk.codGruppo", idGruppoModuli));
		criteria.add(Restrictions.eq("dlgmvo.pk.codDl", codDl));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public GruppoModuliVo getGruppoModuliByRole(String roleIgerivClienteEdicola) {
		return getDao().findUniqueResultByNamedQuery(IGerivQueryContants.QUERY_NAME_GET_GRUPPO_BY_ROLE_JOINS, roleIgerivClienteEdicola);
	}

	@Override
	public Timestamp getSysdate() {
		return getDao().getSysdate();
	}
	
	@Override
	public RichiestaDatiWSVo getRichiestaDatiWS(Integer codEdicola, Timestamp dataBolla) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RichiestaDatiWSVo.class, "rd");
		criteria.add(Restrictions.eq("rd.codEdicola", codEdicola));
		criteria.add(Restrictions.eq("rd.dataBolla", dataBolla));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
	@Override
	public List<EdicolaDto> getEdicoleGeomappateByCap(Integer capDa, Integer capA) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AbbinamentoEdicolaDlVo.class, "ab");
		criteria.createCriteria("ab.anagraficaEdicolaVo", "ae");
		criteria.add(Restrictions.isNull("ab.dtSospensioneEdicola"));
		criteria.add(Restrictions.and(Restrictions.gt("ae.latitudine", 0d),Restrictions.gt("ae.longitudine", 0d)));
		criteria.add(Restrictions.between("ae.capInt", capDa, capA));
		ProjectionList props = Projections.projectionList();
		props.add(Projections.property("ae.codEdicola"), "codEdicolaWeb");
		props.add(Projections.property("ae.ragioneSocialeEdicolaPrimaRiga"), "ragioneSociale1");
		props.add(Projections.property("ae.ragioneSocialeEdicolaSecondaRiga"), "ragioneSociale2");
		props.add(Projections.property("ae.indirizzoEdicolaPrimaRiga"), "indirizzo");
		props.add(Projections.property("ae.localitaEdicolaPrimaRiga"), "localita");
		props.add(Projections.property("ae.siglaProvincia"), "provincia");
		props.add(Projections.property("ae.latitudine"), "latitudine");
		props.add(Projections.property("ae.longitudine"), "longitudine");
		criteria.setProjection(props);
		criteria.setResultTransformer(Transformers.aliasToBean(EdicolaDto.class));
		return getDao().findObjectByDetachedCriteria(criteria);
	}
	
	@Override
	public ClientCompatibilitaVersioniVo getClientCompatibilitaVersione(String app, Float clientVersion) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ClientCompatibilitaVersioniVo.class, "co");
		criteria.add(Restrictions.eq("co.os", app).ignoreCase());
		criteria.add(Restrictions.le("co.clientVersionDa", clientVersion));
		criteria.add(Restrictions.ge("co.clientVersionA", clientVersion));
		return getDao().findUniqueResultByDetachedCriteria(criteria);
	}
	
}
