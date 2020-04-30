package it.dpe.igeriv.bo.messaggi;


import it.dpe.igeriv.bo.BaseService;
import it.dpe.igeriv.dto.AvvisoMessaggioDto;
import it.dpe.igeriv.dto.ConfermaLetturaMessaggioDto;
import it.dpe.igeriv.dto.EmailRivenditaDto;
import it.dpe.igeriv.dto.GiroDto;
import it.dpe.igeriv.dto.GiroTipoDto;
import it.dpe.igeriv.dto.MessaggioDpeDto;
import it.dpe.igeriv.dto.MessaggioDto;
import it.dpe.igeriv.vo.MessaggiBollaVo;
import it.dpe.igeriv.vo.MessaggioClienteVo;
import it.dpe.igeriv.vo.MessaggioDpeVo;
import it.dpe.igeriv.vo.MessaggioIdtnVo;
import it.dpe.igeriv.vo.MessaggioVo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Interfaccia messaggi
 * 
 * @author mromano
 *
 */
public interface MessaggiService extends BaseService {
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param dtBolla
	 * @param tipo
	 * @return
	 */
	public List<MessaggiBollaVo> getMessaggiBolla(Integer[] codFiegDl, Integer[] codEdicolaDl, Timestamp dtBolla, String tipo);
	
	/**
	 * @param codFiegDl
	 * @param codEdicolaDl
	 * @param dtBolla
	 * @param tipo
	 * @return
	 */
	public List<MessaggiBollaVo> getMessaggiBollaEdicola(Integer codFiegDl, Integer codEdicolaDl, Timestamp dtBolla, String tipo);
	
	/**
	 * @param <T>
	 * @param codDl
	 * @param codEdicolaDl
	 * @param confermaLettura 
	 * @param codEdicola 
	 * @param dtMessaggio
	 * @return
	 */
	public List<MessaggioDto> getMessaggiRivendite(Integer[] codDl, Timestamp dtMessaggioDa, Timestamp dtMessaggioA, Integer[] codEdicola, boolean showMessaggioLetto);
	
	/**
	 * @param <T>
	 * @param codFiegDl
	 * @param codEdicola
	 * @return
	 */
	public List<ConfermaLetturaMessaggioDto> getConfermeLettura(Integer codEdicola, Integer codFiegDl, Timestamp dtMessaggio, Integer tipoDestinatario, Integer destinatarioA, Integer destinatarioB);
	
	/**
	 * @param codFiegDl
	 * @param codFiegDl2
	 * @param dtMessaggio
	 * @param showConfermaLettura 
	 * @return
	 */
	public MessaggioDto getMessaggioRivendita(Integer codEdicola, Integer codFiegDl, Timestamp dtMessaggio, Integer tipoDestinatario, Integer destinatarioA, Integer destinatarioB);
	
	/**
	 * @param codFiegDl
	 * @param dtMessaggio
	 * @param tipoDestinatario
	 * @param destinatarioA
	 * @param destinatarioB
	 * @return
	 */
	public MessaggioVo getMessaggioRivenditaVo(Integer codFiegDl, Timestamp dtMessaggio, Integer tipoDestinatario, Integer destinatarioA, Integer destinatarioB);
	
	/**
	 * @param codEdicola
	 * @param codDl
	 * @param dtAttivazioneEdicola 
	 * @return
	 */
	public AvvisoMessaggioDto getPkMessaggioNonLetto(Integer codEdicola, Integer codDl, List<Integer> giroZonaTipo, List<Integer> giriZone, Integer tipoMessaggio, Timestamp dtAttivazioneEdicola);
	
	/**
	 * @param codRivendita
	 * @param dataDa
	 * @param dataA
	 * @return
	 */
	public List<EmailRivenditaDto> getEmailInviatiDaRivendita(Integer codRivendita, Timestamp dataDa, Timestamp dataA);

	/**
	 * @param idEmailRivendita
	 * @return
	 */
	public EmailRivenditaDto getMessaggioInviatoDaRivendita(Integer idEmailRivendita);

	/**
	 * @param codEdicola
	 * @param giroTipo
	 * @return
	 */
	public List<GiroDto> getGiri(Integer codEdicola, Integer giroTipo);
	
	/**
	 * @param codEdicola
	 * @param giroTipo
	 * @return
	 */
	public List<GiroDto> getZone(Integer codEdicola, Integer zonaTipo);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param codice
	 * @param desc
	 * @return
	 */
	public List<GiroTipoDto> getGiriTipo(Integer codFiegDl, Integer codEdicola, Integer codice, String desc);

	/**
	 * @param codFiegDl
	 * @param codEdicola
	 * @param codice
	 * @param desc
	 * @return
	 */
	public List<GiroTipoDto> getZoneTipo(Integer codFiegDl, Integer codEdicola, Integer codice, String desc);
	
	/**
	 * @param dataDa
	 */
	public List<MessaggioDpeDto> getMessaggiDpe(Timestamp dataDa, Timestamp dataA, Boolean abilitati);
	
	/**
	 * @param codMessaggio
	 * @return
	 */
	public MessaggioDpeDto getMessaggioDpe(Long codMessaggio);

	/**
	 * @param codice
	 */
	public MessaggioDpeVo getMessaggioDpeVo(Long codice);
	
	/**
	 * @return
	 */
	public Boolean hasMessaggiDpe();
	
	/**
	 * @param messaggio
	 */
	public void saveMessaggioDpe(MessaggioDpeDto messaggio);
	
	/**
	 * @param messaggio
	 */
	public void deleteMessaggioDpe(MessaggioDpeDto messaggio);
	
	/**
	 * @param messaggio
	 */
	public void deleteMessaggioRivendita(MessaggioDto messaggio);
	
	/**
	 * @param messaggio
	 */
	public void saveMessaggioRivenditaDpe(MessaggioDto messaggio);

	/**
	 * @param codDl
	 * @param idtn
	 * @return
	 */
	public MessaggioIdtnVo getMessaggioIdtnVo(Integer codDl, Integer idtn);

	/**
	 * @param idMessaggioIdtn
	 * @return
	 */
	public MessaggioIdtnVo getMessaggioIdtnVo(Integer idMessaggioIdtn);

	/**
	 * @param codFiegDl
	 * @param dtMessaggio
	 * @return
	 */
	public Timestamp getMaxSecondsDateMessaggio(Integer codFiegDl, Timestamp dtMessaggio);
	
	/**
	 * @param codMessaggio
	 * @return
	 */
	public MessaggioClienteVo getMessaggioCliente(Long codMessaggio);
	
	/**
	 * @param codEdicola
	 * @param dataDa
	 * @param dataA
	 * @return
	 */
	public List<MessaggioClienteVo> getMessaggiCliente(Integer codEdicola, Date dataDa, Date dataA, String nome, String cognome, String codFiscale, String piva);
	
	/**
	 * @param codEdicola
	 * @return
	 */
	public MessaggioVo getMessaggioRilevamenti(Integer codFiegDl, Integer codEdicola);
	
}
