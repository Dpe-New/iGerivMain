package it.dpe.igeriv.bo.agenzie;


import it.dpe.igeriv.bo.BaseServiceImpl;
import it.dpe.igeriv.bo.menu.MenuService;
import it.dpe.igeriv.dto.AnagraficaAgenziaDto;
import it.dpe.igeriv.dto.EmailDlDto;
import it.dpe.igeriv.vo.AnagraficaAgenziaVo;
import it.dpe.igeriv.vo.DlGruppoModuliVo;
import it.dpe.igeriv.vo.DlVo;
import it.dpe.igeriv.vo.GruppoModuliVo;
import it.dpe.igeriv.vo.LimitiPeriodicitaVo;
import it.dpe.igeriv.vo.UtenteAgenziaVo;
import it.dpe.igeriv.vo.pk.DlGruppoModuliPk;
import it.dpe.igeriv.vo.pk.LimitiPeriodicitaPk;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AgenzieService")
class AgenzieServiceImpl extends BaseServiceImpl implements AgenzieService {
	private final AgenzieRepository repository;
	private final MenuService menuService;
	
	@Autowired
	AgenzieServiceImpl(AgenzieRepository repository, MenuService menuService) {
		super(repository);
		this.repository = repository;
		this.menuService = menuService;
	}

	@Override
	public AnagraficaAgenziaVo getAgenziaByCodice(Integer codFiegDl) {
		return repository.getAgenziaByCodice(codFiegDl);
	}

	@Override
	public AnagraficaAgenziaVo getAgenziaByEmail(String email) {
		return repository.getAgenziaByEmail(email);
	}

	@Override
	public UtenteAgenziaVo getAgenziaByCodiceLogin(Integer codUtente) {
		return repository.getAgenziaByCodiceLogin(codUtente);
	}

	@Override
	public List<AnagraficaAgenziaVo> getAgenzie() {
		return repository.getAgenzie();
	}

	@Override
	public List<AnagraficaAgenziaVo> getAgenzieConFatturazione() {
		return repository.getAgenzieConFatturazione();
	}

	@Override
	public List<AnagraficaAgenziaVo> getAgenzieInforiv() {
		return repository.getAgenzieInforiv();
	}

	@Override
	public List<DlVo> getListAgenzie() {
		return repository.getListAgenzie();
	}

	@Override
	public List<AnagraficaAgenziaDto> getListAgenzieDpe() {
		return repository.getListAgenzieDpe();
	}
	
	@Override
	public String getPasswordDl(Integer codFiegDl) {
		return repository.getPasswordDl(codFiegDl);
	}
	
	@Override
	public void updateDataDownload(Integer codDl, String fileName, Integer tipo) {
		repository.updateDataDownload(codDl, fileName, tipo);
	}
	
	@Override
	public List<EmailDlDto> getEmailsDl(Integer codFiegDl) {
		return repository.getEmailsDl(codFiegDl);
	}
	
	@Override
	public AnagraficaAgenziaVo getAgenziaByCodiceDpeWeb(Integer codDpeWebDl) {
		return repository.getAgenziaByCodiceDpeWeb(codDpeWebDl);
	}
	
	@Override
	public void insertNewAgenzia(AnagraficaAgenziaVo agenzia) {
		List<DlGruppoModuliVo> listGruppiDl = new ArrayList<>();
		for (GruppoModuliVo gruppo : menuService.getGruppoModuli()) {
			DlGruppoModuliVo dlg = new DlGruppoModuliVo();
			DlGruppoModuliPk pk = new DlGruppoModuliPk();
			pk.setCodDl(agenzia.getCodFiegDl());
			pk.setCodGruppo(gruppo.getId());
			dlg.setPk(pk);
			listGruppiDl.add(dlg);
		}
		List<LimitiPeriodicitaVo> limitiPeriodicita = buildLimitiPeriodicita(agenzia.getCodFiegDl());
		saveBaseVo(agenzia);
		saveVoList(listGruppiDl);
		saveVoList(limitiPeriodicita);
	}
	
	/**
	 * Costruisce i limite delle periodicita
	 * @param Integer codFiegDl
	 * @return List<LimitiPeriodicitaVo>
	 */
	private List<LimitiPeriodicitaVo> buildLimitiPeriodicita(Integer codFiegDl) {
		List<LimitiPeriodicitaVo> list = new ArrayList<LimitiPeriodicitaVo>();
		for (int i = 0; i <= 10; i++) {
			LimitiPeriodicitaVo vo = new LimitiPeriodicitaVo();
			LimitiPeriodicitaPk pk = new LimitiPeriodicitaPk();
			pk.setCodFiegDl(codFiegDl);
			pk.setCodPeriodicita(i);
			vo.setNumMaxCopertineVecchieResa(5);
			vo.setNumMaxStoriaCopertine(5);
			vo.setPermetteResaNumeroNuovo("S");
			vo.setMaxStatisticaVisualizzare(12);
			vo.setPermettePubblicazioniInContoDeposito("N");
			vo.setGiorniValiditaRichiesteRifornimento(7);
			vo.setPk(pk);
			list.add(vo);
		}
		return list;
	}
	
	public Long getNewCodDlWeb()
	{
		return repository.getNewCodDlWeb();
	}

	@Override
	public List<AnagraficaAgenziaVo> getAgenzieModalitaLocaleFtpInforiv() {
		return repository.getAgenzieModalitaLocaleFtpInforiv();
	}
	
	@Override
	public List<AnagraficaAgenziaVo> getAgenzieModalitaInforiv(){
		return repository.getAgenzieModalitaInforiv();
	}
	
	@Override
	public Boolean isMod2Inforiv(Integer codDl){
		return repository.isMod2Inforiv(codDl);
	}
}
