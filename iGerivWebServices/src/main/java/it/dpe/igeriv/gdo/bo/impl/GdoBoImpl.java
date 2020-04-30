package it.dpe.igeriv.gdo.bo.impl;

import it.dpe.igeriv.bo.ws.IGerivWSService;
import it.dpe.igeriv.dto.BollaDettaglioDto;
import it.dpe.igeriv.dto.FondoBollaDettaglioDto;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.gdo.bo.GdoBo;
import it.dpe.igeriv.gdo.dto.GdoBolProdottoDto;
import it.dpe.igeriv.gdo.dto.GdoBolReplyDto;
import it.dpe.igeriv.gdo.dto.GdoVenditaDto;
import it.dpe.igeriv.gdo.dto.GdoVenditaReplyDto;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.security.UserAbbonato;
import it.dpe.igeriv.util.DateUtilities;
import it.dpe.igeriv.util.IGerivConstants;
import it.dpe.igeriv.util.IGerivWSConstants;
import it.dpe.igeriv.vo.RichiestaDatiWSVo;
import it.dpe.service.mail.MailingListService;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implementazione dell'interfaccia BO per scambio dati GDO.
 * 
 * @author romanom
 * 
 */
@Component("GdoBo")
public class GdoBoImpl implements GdoBo {
	@Autowired
	private IGerivWSService iGerivBo;
	@Autowired
	private MailingListService mailingListService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ReflectionSaltSource saltSource;
	
	public IGerivWSService getiGerivBo() {
		return iGerivBo;
	}

	public void setiGerivBo(IGerivWSService iGerivBo) {
		this.iGerivBo = iGerivBo;
	}

	public MailingListService getMailingListService() {
		return mailingListService;
	}

	public void setMailingListService(MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}
	
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public ReflectionSaltSource getSaltSource() {
		return saltSource;
	}

	public void setSaltSource(ReflectionSaltSource saltSource) {
		this.saltSource = saltSource;
	}

	@Override
	public GdoBolReplyDto getDatiBollaGdo(UserAbbonato user, Date dataBolla) {
		GdoBolReplyDto dto = new GdoBolReplyDto();
		List<BollaDettaglioDto> dettaglioBolla = iGerivBo.getDettaglioBolla(user.getArrCodFiegDl(), user.getArrId(), new Timestamp(dataBolla.getTime()), user.isMultiDl());
		List<FondoBollaDettaglioDto> dettagliFondoBolla = iGerivBo.getDettagliFondoBolla(user.getArrCodFiegDl(), user.getArrId(), new Timestamp(dataBolla.getTime()), null, false, false, user.isMultiDl());
		List<GdoBolProdottoDto> listProdottiBolla = buildListProdottiDto(dettaglioBolla);
		List<GdoBolProdottoDto> listProdottiFondoBolla = buildListProdottiFondoBollaDto(dettagliFondoBolla);
		listProdottiBolla.addAll(listProdottiFondoBolla);
		dto.setListProdottiBolla(listProdottiBolla);
		return dto;
	}
	
	@Override
	public void validateRichiestaDati(UserAbbonato user, Date dataBolla) throws IGerivBusinessException {
		RichiestaDatiWSVo rd = iGerivBo.getRichiestaDatiWS(user.getCodEdicolaMaster(), new Timestamp(dataBolla.getTime()));
		if (rd == null) {
			rd = new RichiestaDatiWSVo();
			rd.setCodEdicola(user.getCodEdicolaMaster());
			rd.setDataBolla(new Timestamp(dataBolla.getTime()));
		}
		Integer count = (rd != null && rd.getCount() != null) ? rd.getCount() : 0;
		if (count.intValue() >= 4) {
			String format = MessageFormat.format(IGerivMessageBundle.get("igeriv.superato.numero.massimo.scarico.dati.bolla.ws"), user.getCodEdicolaMaster().toString(), DateUtilities.getTimestampAsString(dataBolla, DateUtilities.FORMATO_DATA));
			throw new IGerivBusinessException(format);
		}
		rd.setCount(++count);
		iGerivBo.saveBaseVo(rd);
	}

	/**
	 * @param pubblicazioniInBolla
	 * @return
	 */
	private List<GdoBolProdottoDto> buildListProdottiDto(List<BollaDettaglioDto> pubblicazioniInBolla) {
		List<GdoBolProdottoDto> list = new ArrayList<GdoBolProdottoDto>();
		SimpleDateFormat sdf = new SimpleDateFormat(IGerivWSConstants.DATA_RESPONSE_WS_FORMAT);
		for (BollaDettaglioDto dto : pubblicazioniInBolla) {
			GdoBolProdottoDto gdo = new GdoBolProdottoDto();
			gdo.setBarcode(dto.getBarcode());
			gdo.setDescrizione(dto.getTitolo() + dto.getSottoTitolo());
			gdo.setIdtn(dto.getIdtn());
			gdo.setCpu(dto.getCodicePubblicazione());
			gdo.setCodiceInizio(dto.getCodInizioQuotidiano());
			if (dto.getDataUscita() != null) {
				gdo.setDataUscita(sdf.format(dto.getDataUscita()));
			}
			gdo.setNumeroCopertina(dto.getNumeroPubblicazione());
			gdo.setPrezzoAcquisto(dto.getPrezzoNetto());
			gdo.setPrezzoLordo(dto.getPrezzoLordo());
			gdo.setSottotitolo(dto.getSottoTitolo());
			gdo.setTitolo(dto.getTitolo());
			gdo.setTipoPubblicazione(dto.getPeriodicitaPk() != null && dto.getPeriodicitaPk().getPeriodicita() != null ? (dto.getPeriodicitaPk().getPeriodicita().equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO) ? IGerivWSConstants.QUOTIDIANO : IGerivWSConstants.PERIODICO) : null);
			gdo.setQuantitaConsegnata(dto.getQuantitaConsegnata());
			list.add(gdo);
		}
		return list;
	}
	
	/**
	 * @param pubblicazioniInBolla
	 * @return
	 */
	private List<GdoBolProdottoDto> buildListProdottiFondoBollaDto(List<FondoBollaDettaglioDto> pubblicazioniInBolla) {
		List<GdoBolProdottoDto> list = new ArrayList<GdoBolProdottoDto>();
		SimpleDateFormat sdf = new SimpleDateFormat(IGerivWSConstants.DATA_RESPONSE_WS_FORMAT);
		for (FondoBollaDettaglioDto dto : pubblicazioniInBolla) {
			GdoBolProdottoDto gdo = new GdoBolProdottoDto();
			gdo.setBarcode(dto.getBarcode());
			gdo.setDescrizione(dto.getTitolo() + dto.getSottoTitolo());
			gdo.setIdtn(dto.getIdtn());
			gdo.setCpu(dto.getCodicePubblicazione());
			gdo.setCodiceInizio(dto.getCodInizioQuotidiano());
			if (dto.getDataUscita() != null) {
				gdo.setDataUscita(sdf.format(dto.getDataUscita()));
			}
			gdo.setNumeroCopertina(dto.getNumeroPubblicazione());
			gdo.setPrezzoAcquisto(dto.getPrezzoNetto());
			gdo.setPrezzoLordo(dto.getPrezzoLordo());
			gdo.setSottotitolo(dto.getSottoTitolo());
			gdo.setTitolo(dto.getTitolo());
			gdo.setTipoPubblicazione(dto.getPeriodicitaPk() != null && dto.getPeriodicitaPk().getPeriodicita() != null ? (dto.getPeriodicitaPk().getPeriodicita().equals(IGerivConstants.COD_PERIODICITA_QUOTIDIANO) ? IGerivWSConstants.QUOTIDIANO : IGerivWSConstants.PERIODICO) : null);
			gdo.setQuantitaConsegnata(dto.getQuantitaConsegnata());
			list.add(gdo);
		}
		return list;
	}


	@Override
	public GdoVenditaReplyDto importDatiVenditeGdo(UserAbbonato user, List<GdoVenditaDto> list) {
		GdoVenditaReplyDto reply = new GdoVenditaReplyDto();
		List<String> warnings = new ArrayList<String>();
		try {
			if (list != null && !list.isEmpty()) {
				iGerivBo.importDatiVenditeGdo(user, list, warnings);
			}
			reply.setListMessages(warnings.toArray(new String[warnings.size()]));
			if (!warnings.isEmpty()) {
				throw new IGerivBusinessException();
			}
			reply.setState(IGerivConstants.STATO_IMPORTAZIONE_DATI_VENDITE_GDO_CONCLUSO_SUCCESS);
		} catch (IGerivBusinessException e) {
			reply.setState(IGerivConstants.STATO_IMPORTAZIONE_DATI_VENDITE_GDO_CONCLUSO_WARNING);
		}
		return reply;
	}
	
}
