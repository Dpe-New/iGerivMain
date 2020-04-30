package it.dpe.igeriv.bo.scolastica;


import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.messaggi.MessaggiService;
import it.dpe.igeriv.dto.OrdineLibriDto;
import it.dpe.igeriv.vo.OrdineLibriDettVo;


import it.dpe.igeriv.vo.OrdineLibriVo;
import it.dpe.igeriv.vo.OrdineLibriDettVo.ETrack;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ScolasticaService")
public class ScolasticaServiceImp extends BaseServiceImpl implements ScolasticaService{
	
	private final ScolasticaRepository scolasticaRepository;
	private final MessaggiService messaggiService;
	
	@Autowired
	ScolasticaServiceImp(ScolasticaRepository scolasticaRepository, MessaggiService messaggiService) {
		super(scolasticaRepository);
		this.scolasticaRepository = scolasticaRepository;
		this.messaggiService = messaggiService;
	}


	@Override
	public List<OrdineLibriDto> findListOrdiniLibri(Integer codFiegDl, Integer codEdicola, Long numeroOrdine, Long codCliente) {
		return scolasticaRepository.findListOrdiniLibri(codFiegDl, codEdicola, numeroOrdine, codCliente);
	}

	@Override
	public boolean deleteOrdineLibri(Long numeroOrdine) {
		return scolasticaRepository.deleteOrdineLibri(numeroOrdine);
	}

	@Override
	public boolean existOrdineAttivo(Integer codFiegDl, Integer codEdicola,Long codCliente) {
		return scolasticaRepository.existOrdineAttivo(codFiegDl, codEdicola, codCliente);
	}

	@Override
	public Long getOrdineAttivo(Integer codFiegDl, Integer codEdicola,Long codCliente) {
		return scolasticaRepository.getOrdineAttivo(codFiegDl, codEdicola, codCliente);
	}

	@Override
	public Long insertNewOrdineLibri(Integer codFiegDl, Integer codEdicola,Long codCliente) {
		return scolasticaRepository.insertNewOrdineLibri(codFiegDl, codEdicola, codCliente);
	}

	@Override
	public Long countLibriCarrello(Long numOrdine) {
		return scolasticaRepository.countLibriCarrello(numOrdine);
	}

	@Override
	public void addLibriCarrello(OrdineLibriDettVo dettaglioLibro) {
		scolasticaRepository.addLibriCarrello(dettaglioLibro);
	}
	
	@Override
	public List<OrdineLibriDettVo> getOrdiniWS(Integer codDl, Long numeroCollo) {
		List<OrdineLibriDettVo> result = new ArrayList<>(0);
		List<OrdineLibriVo> ordini = scolasticaRepository.findOrdiniLibri(codDl, null, null, null, numeroCollo);
		if (ordini!=null && !ordini.isEmpty()) {
			for (OrdineLibriVo vo : ordini) {
				if (vo.getListDettaglioOrdine()!=null) {
					result.addAll(vo.getListDettaglioOrdine());
				}
			}
		}
		if (result.isEmpty()) {
			// call ws txt
		}
		return result; 
		
	}

	@Override
	public BigDecimal sumPrezzoLibriCarrello(Long numOrdine) {
		return scolasticaRepository.sumPrezzoLibriCarrello(numOrdine);
	}


	@Override
	public OrdineLibriVo getDettaglioOrdineLibri(Long numeroOrdine) {
		return scolasticaRepository.getDettaglioOrdineLibri(numeroOrdine);
	}

	@Override
	public OrdineLibriVo getDettaglioOrdineLibri(Long numeroOrdine, ETrack track) {
		return scolasticaRepository.getDettaglioOrdineLibri(numeroOrdine,track);
	}
	
	@Override
	public boolean deleteLibroDaOrdine(Long numeroOrdine,Long seqOrdine, String sku) {
		return scolasticaRepository.deleteLibroDaOrdine(numeroOrdine,seqOrdine, sku);
	}


	@Override
	public boolean confermaOrdine(OrdineLibriVo ordine) {
		return scolasticaRepository.confermaOrdine(ordine);
	}


	@Override
	public OrdineLibriVo getOrdine(String numOrdineTxt) {
		return scolasticaRepository.getOrdine(numOrdineTxt);
	}

	@Override
	public OrdineLibriVo getOrdine(String numOrdineTxt,Long codCliente) {
		return scolasticaRepository.getOrdine(numOrdineTxt,codCliente);
	}

	@Override
	public OrdineLibriDettVo getDettaglioLibro(Long numeroOrdine, String sku) {
		return scolasticaRepository.getDettaglioLibro(numeroOrdine, sku);
	}


	@Override
	public OrdineLibriDettVo getDettaglioLibro(Long numeroOrdine, String sku, Long keynum) {
		return scolasticaRepository.getDettaglioLibro(numeroOrdine, sku, keynum);
	}
	

	@Override
	public List<OrdineLibriDettVo> getDettaglioOrdiniByNumCollo(Integer codDl,Long numeroCollo) {
		return scolasticaRepository.getDettaglioOrdiniByNumCollo(codDl,numeroCollo);
	}


	@Override
	public boolean existOrdiniInattivi(Integer codFiegDl, Integer codEdicola,
			Long codCliente) {
		return scolasticaRepository.existOrdiniInattivi(codFiegDl, codEdicola, codCliente);
	}


	@Override
	public boolean existOrdini(Integer codFiegDl, Integer codEdicola,
			Long codCliente) {
		return scolasticaRepository.existOrdini(codFiegDl, codEdicola, codCliente);
	}


	@Override
	public List<OrdineLibriDettVo> getDettaglioOrdiniInEdicolaByNumCollo(
			Integer codDl, Long numeroCollo) {
		return scolasticaRepository.getDettaglioOrdiniInEdicolaByNumCollo(codDl, numeroCollo);
	}


	@Override
	public List<Long> getDistinctClientiByNumCollo(Integer codDl,
			Long numeroCollo) {
		return scolasticaRepository.getDistinctClientiByNumCollo(codDl, numeroCollo);
	}
	
	@Override
	public List<OrdineLibriDettVo> getOrdiniFornitore(String ordine, ETrack track, Integer codEdicola) {
		return  scolasticaRepository.getOrdiniFornitore(ordine, track, codEdicola);
	}
	
	public int updateStatoOrdini(List<OrdineLibriDettVo> ordini, ETrack stato) {
		return  scolasticaRepository.updateStatoOrdini(ordini, stato);
	}


	@Override
	public List<OrdineLibriDto> findListOrdiniPerDettaglio(Integer codFiegDl,
			Integer codEdicola, Long numeroOrdine, Long codCliente) {
		return  scolasticaRepository.findListOrdiniPerDettaglio(codFiegDl, codEdicola, numeroOrdine, codCliente);
	}
	
	@Override
	public List<OrdineLibriDettVo> getOrdiniFornitore(Long[] ids) {
		return scolasticaRepository.getOrdiniFornitore(ids);
	}
	@Override
	public List<OrdineLibriDettVo> getOrdiniFornitore(Long cliente) {
		return scolasticaRepository.getOrdiniFornitore(cliente);
	}
	
	

	@Override
	public boolean existLibriDaConsegnare(Integer codDl, String numOrdineTxt) {
		return scolasticaRepository.existLibriDaConsegnare(codDl, numOrdineTxt);
	}


	@Override
	public boolean existTrackingOrdine(Integer codDl, String numOrdineTxt) {
		return scolasticaRepository.existTrackingOrdine(codDl, numOrdineTxt);
	}
	
	@Override
	public boolean existTrackingOrdineEdicola(Integer codDl,Integer codDpeWebEdicola, String numOrdineTxt) {
		return scolasticaRepository.existTrackingOrdineEdicola(codDl,codDpeWebEdicola, numOrdineTxt);
	}
	
	@Override
	public boolean existTrackingOrdineParzialmenteEvaso(Integer codDl,
			String numOrdineTxt) {
		return scolasticaRepository.existTrackingOrdineParzialmenteEvaso(codDl, numOrdineTxt);
	}


	@Override
	public List<Date> getDateDiConsegnaCliente(Integer codDl,
			String numOrdineTxt) {
		return scolasticaRepository.getDateDiConsegnaCliente(codDl, numOrdineTxt);
	}


	@Override
	public boolean existOrdiniDaConsegnareAlCliente(Integer codFiegDl,
			Integer codEdicola, Long codCliente) {
		return scolasticaRepository.existOrdiniDaConsegnareAlCliente(codFiegDl, codEdicola, codCliente);
	}


	@Override
	public boolean visualizzareOrdineCliente(Long numeroOrdine) {
		return scolasticaRepository.visualizzareOrdineCliente(numeroOrdine);
	}


	@Override
	public OrdineLibriDettVo getDettaglioLibroNotKeyNum(Long numeroOrdine, String sku) {
		return scolasticaRepository.getDettaglioLibroNotKeyNum(numeroOrdine,sku);
	}


	@Override
	public BigDecimal sumPrezzoCopertinatura(Long numOrdine) {
		return scolasticaRepository.sumPrezzoCopertinatura(numOrdine);
	}


	@Override
	public BigDecimal sumPrezzoTotale(Long numOrdine) {
		return scolasticaRepository.sumPrezzoTotale(numOrdine);
	}


	@Override
	public OrdineLibriDettVo getInformazioneCopertinaLibro(Long numeroOrdine, Long seqOrdine) {
		return scolasticaRepository.getInformazioneCopertinaLibro(numeroOrdine,seqOrdine);
	}
	@Override
	public OrdineLibriDettVo getInformazioneCopertinaUltimoLibroInserito(Long numeroOrdine) {
		return scolasticaRepository.getInformazioneCopertinaUltimoLibroInserito(numeroOrdine);
	}

	@Override
	public List<OrdineLibriVo> getListOrdiniCliente(Integer codFiegDl,Integer codEdicola, Long codCliente) {
		return scolasticaRepository.getListOrdiniCliente(codFiegDl,codEdicola,codCliente);
	}


	



	
	
}
