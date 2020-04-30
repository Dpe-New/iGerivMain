package it.dpe.inforiv.exporter.impl;

import it.dpe.igeriv.bo.batch.IGerivBatchService;
import it.dpe.igeriv.bo.inforiv.InforivExportService;
import it.dpe.igeriv.vo.AbbinamentoEdicolaDlVo;
import it.dpe.igeriv.vo.RichiestaRifornimentoVo;
import it.dpe.igeriv.vo.pk.RichiestaRifornimentoPk;
import it.dpe.inforiv.dto.input.InforivDto;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

public class InforivRichiestaRifornimentoExporterTest {
	private Mockery mockingContext;
	private InforivRichiestaRifornimentoExporter systemUnderTest; 
	private IGerivBatchService iGerivBo;
	private InforivExportService exportBo;
	private List<RichiestaRifornimentoVo> testListRichieste;
	private AbbinamentoEdicolaDlVo edicolaTest;
	private FixedFormatManager fixedFormatManagerTest;
	
	@Before
	public void Setup() {
		mockingContext = new JUnit4Mockery();
		systemUnderTest = new InforivRichiestaRifornimentoExporter();
		systemUnderTest.setExportPathDir("C:/igeriv/edicole/inforiv/out");
		iGerivBo = mockingContext.mock(IGerivBatchService.class);
		exportBo = mockingContext.mock(InforivExportService.class);
		fixedFormatManagerTest = mockingContext.mock(FixedFormatManager.class);
		systemUnderTest.setBo(iGerivBo);
		systemUnderTest.setExportBo(exportBo);
		buildTestListRichieste();
		buildEdicolaTest();
	}

	@Test
	public void exportData() {
		mockingContext.checking(new Expectations(){{
			one(exportBo).getRichiesteRifornimenti(with(any(Integer.class)));
			will(returnValue(testListRichieste));
			one(iGerivBo).getNextSeqVal(with(any(String.class)));
			will(returnValue(1l));
			one(iGerivBo).getAbbinamentoEdicolaDlVoByCodEdicolaWeb(with(any(Integer.class)));
			will(returnValue(edicolaTest));
			allowing(fixedFormatManagerTest).export(with(any(InforivDto.class)));
			will(returnValue(""));
		}});
		systemUnderTest.exportData("B", new Timestamp(new Date().getTime()), 1, new HashMap<Integer, File>(), new HashMap<Integer, Map<String, String>>(), fixedFormatManagerTest,null);
	}
	
	private void buildEdicolaTest() {
		edicolaTest = new AbbinamentoEdicolaDlVo();
		edicolaTest.setCodEdicolaDl(456);
		edicolaTest.setHostFtp("10.1.1.3");
		edicolaTest.setUserFtp("test");
		edicolaTest.setPwdFtp("test");
	}

	private void buildTestListRichieste() {
		testListRichieste = new ArrayList<RichiestaRifornimentoVo>();
		RichiestaRifornimentoVo vo = new RichiestaRifornimentoVo();
		RichiestaRifornimentoPk pk = new RichiestaRifornimentoPk();
		pk.setCodEdicola(1538);
		pk.setCodFiegDl(213);
		pk.setDataOrdine(new Timestamp(new Date().getTime()));
		pk.setIdtn(123456);
		vo.setPk(pk);
		vo.setFornito(1l);
		vo.setQuantitaRichiesta(5);
		vo.setIdtnTrascodifica("12324");
		vo.setNoteVendita("test");
		testListRichieste.add(vo);
	}
	
}	
