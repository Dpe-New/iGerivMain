package it.dpe.igeriv.vendite.inputmodulelistener.impl;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.on;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.dpe.igeriv.bo.livellamenti.LivellamentiService;
import it.dpe.igeriv.bo.pubblicazioni.PubblicazioniService;
import it.dpe.igeriv.dto.PubblicazioneDto;
import it.dpe.igeriv.dto.VenditeCardResultDto;
import it.dpe.igeriv.dto.VenditeParamDto;
import it.dpe.igeriv.enums.StatoRichiestaLivellamento;
import it.dpe.igeriv.enums.StatoRichiestaRifornimentoLivellamento;
import it.dpe.igeriv.exception.CodiceLivellamentoErratoException;
import it.dpe.igeriv.exception.IGerivBusinessException;
import it.dpe.igeriv.exception.RichiestaLivellamentoInesistenteException;
import it.dpe.igeriv.resources.IGerivMessageBundle;
import it.dpe.igeriv.vendite.inputmodulelistener.VenditeInputModuleListener;
import it.dpe.igeriv.vo.LivellamentiVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoLivellamentiVo;

/**
 * Classe per la gestione dell'evento di tipo ricerca pubblicazione per barcode.
 * 
 * @author romanom
 *
 */
@Component("ReteEdicoleInputModuleListener")
public class ReteEdicoleInputModuleListener implements VenditeInputModuleListener {
	private final PubblicazioniService pubblicazioniService;
	private final LivellamentiService<LivellamentiVo> livellamentiService;
    
    @Autowired
	ReteEdicoleInputModuleListener(PubblicazioniService pubblicazioniService, LivellamentiService<LivellamentiVo> livellamentiService) {
		this.pubblicazioniService = pubblicazioniService;
		this.livellamentiService = livellamentiService;
	}
	
    @Override
    public VenditeCardResultDto execute(VenditeParamDto params) throws IGerivBusinessException {
    	String barcode = params.getInputText();
    	Long codiceVenditaReteEdicole = new Long(params.getCodiceVenditaReteEdicole());
    	Integer[] arrCodDl = params.getArrCodFiegDl();
        Integer[] idEdicola = params.getArrIdEdicola();
        Integer gruppoSconto = params.getGruppoSconto();
        Boolean findCopieInContoDeposito = params.getFindCopieInContoDeposito();
        Timestamp dataStorico = params.getDataStorico();
        Boolean dlInforiv = params.getDlInforiv();
        Integer quantita = params.getQuantita();
        
        VenditeCardResultDto result = new VenditeCardResultDto();
        List<PubblicazioneDto> copertineByTitoloBarcode = pubblicazioniService.getCopertineByTitoloBarcodeCpu(arrCodDl, idEdicola, null, barcode, false, false, null, gruppoSconto, findCopieInContoDeposito, false, dataStorico, dlInforiv);
        if (!copertineByTitoloBarcode.isEmpty() && copertineByTitoloBarcode.size() == 1) {
        	PubblicazioneDto cop = copertineByTitoloBarcode.get(0);
			//RichiestaRifornimentoLivellamentiVo richiesta = livellamentiService.getRichiestaRifornimentoLivellamentiByEdicolaVenditrice(cop.getCoddl(), params.getIdEdicola(), cop.getIdtn());
			//RichiestaRifornimentoLivellamentiVo richiesta = livellamentiService.getRichiestaRifornimentoLivellamentiByCodEdicolaVenditrice(cop.getCoddl(),cop.getIdtn());
			
        	RichiestaRifornimentoLivellamentiVo richiesta = livellamentiService.getRichiestaRifornimentoLivellamentiByEdicolaRichiedente(cop.getCoddl(),codiceVenditaReteEdicole.intValue(),cop.getIdtn());
			
        	
			if (richiesta != null) {
				if (livellamentiService.isValidCodEdicola(richiesta.getIdRichiestaLivellamento(), codiceVenditaReteEdicole.intValue())) {  // (richiesta.getIdRichiestaLivellamento() 
					if(livellamentiService.isCheckQuantita(richiesta.getIdRichiestaLivellamento(), codiceVenditaReteEdicole.intValue(), params.getIdEdicola(), quantita)){
					
						//richiesta.setStato(StatoRichiestaRifornimentoLivellamento.DA_RITIRARE);
		        		//forEach(richiesta.getLivellamenti()).setStatoVendita(StatoRichiestaLivellamento.VENDUTO);
						//richiesta.setStato(StatoRichiestaRifornimentoLivellamento.RITIRATI);
						
						//List<LivellamentiVo> listLivellamenti = livellamentiService.getAllLivellamentiAccettatiByIdRichiestaLivellamento(richiesta.getIdRichiestaLivellamento());
						List<LivellamentiVo> listLivellamenti = livellamentiService.getLivellamentiInStatoAccettatoByIdRichiestaLivellamentoIdEdicola(richiesta.getIdRichiestaLivellamento(),idEdicola[0].intValue());
						
						forEach(listLivellamenti).setStatoVendita(StatoRichiestaLivellamento.VENDUTO);
						Timestamp sysdate = livellamentiService.getSysdate();
						forEach(listLivellamenti).setDataVendita(sysdate);
						
						List<Long> idRich = extract(listLivellamenti, on(LivellamentiVo.class).getRichiesta().getIdRichiestaLivellamento());
						List<RichiestaRifornimentoLivellamentiVo> listRichieste = livellamentiService.getRichiesteRifornimentoLivellamentiByIds(idRich);
						forEach(listRichieste).setStato(StatoRichiestaRifornimentoLivellamento.RITIRATI);
						
						livellamentiService.saveAccettazioneLivellamenti(listLivellamenti, null, listRichieste);
						
						//livellamentiService.saveVenditaLivellamenti(richiesta);
						result.setResult(cop);
					}else{
						throw new CodiceLivellamentoErratoException(IGerivMessageBundle.get("igeriv.livellamenti.error.quantita.errato"));
					}
				} else {
	        		throw new CodiceLivellamentoErratoException(IGerivMessageBundle.get("igeriv.livellamenti.error.codice.livellamento.errato"));
	        	}
        	} else {
        		throw new RichiestaLivellamentoInesistenteException(IGerivMessageBundle.get("igeriv.livellamenti.error.livellamento.inesistente"));
        	}
        } else {
        	throw new IGerivBusinessException(IGerivMessageBundle.get("label.barcode_input_module_listener.Pubblication_Not_Found"));
        }
        
		result.setType(VenditeCardResultDto.ResultType.VENDITA_RETE_EDICOLE);
        return result;
    }
    
}
