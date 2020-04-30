package it.dpe.igeriv.web.applet.regcassa;

import java.util.HashMap;
import java.util.Map;

import org.fest.swing.applet.AppletViewer;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.launcher.AppletLauncher;
import org.jmock.Mockery;
import org.junit.Test;

import junit.framework.TestCase;

public class TestApplet extends TestCase {
	private Mockery mockery;
	private FrameFixture applet;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mockery = new Mockery();
	}

	@Test
	public void testApplet() {

		Map<String, String> parameters = new HashMap<>();
		parameters.put("type", "application/x-java-applet;version=1.7");
		parameters.put("codebase", "/igeriv/applet/signed");
		parameters.put("scheme", "http");
		parameters.put("serverName", "localhost");
		parameters.put("serverPort", "8080");
		parameters.put("context", "/igeriv");
		parameters.put("scriptable", "true");
		parameters.put("toSendDir", "");
		parameters.put("userRegCassaLocalDir", null);
		parameters.put("scontriniRegCassaLocalDir", null);
		parameters.put("fileNamePrefix", "scontrino_");
		parameters.put("fileNameDigitLength", "10");
		parameters.put("codRegCassa", "10");
		parameters.put("millsTaskTimeout", "20000");
		parameters.put("nomeProcesso", "SoEcrCom.exe");
		parameters.put("iniFileContent",
						"PROG NOPRINT<br/>PIVA NR=1, ALIQ=10.00<br/>PIVA NR=2, ALIQ=22.00<br/>PIVA NR=3, ALIQ=0.00<br/>PIVA NR=4, ALIQ=4.00<br/>FINEPROG<br/>PROG NOPRINT<br/>PREP NR=1, DES='rep1 10%                ', IVA=1<br/>PREP NR=2, DES='rep2 22%                ', IVA=2<br/>PREP NR=3, DES='rep3 es                 ', IVA=3<br/>PREP NR=4, DES='rep4 4%                 ', IVA=4<br/>PREP NR=5, DES='rep5 es74               ', IVA=3<br/>FINEPROG");
		parameters.put("abilitaLog", "true");
		parameters.put("mailFrom", "noreply@geriv.it");
		parameters.put("mailTo", "marcello.romano@dpe.it");
		parameters.put("logFileName", "reg_cassa_applet.log");
		parameters.put("logFileSize", "1048576");
		parameters.put("smtpHost", "out.impresasemplice.it");
		parameters.put("smtpUser", "noreply@geriv.it");
		parameters.put("smtpPwd", "noreply2011");
		parameters.put("supportedOS", "0");
		parameters.put("binaryUnixLinux", "xditron");
		parameters.put("binaryMac", "xditron");
		parameters.put("binarySolaris", "xditron");
		parameters.put("codEdicola", "949");
		parameters.put("millsTaskInterval", "50000");

		RegistratoreCassaApplet registratoreCassaApplet = new RegistratoreCassaApplet();
		
		//registratoreCassaApplet.setJSObject(new IGerivJSObject());
		
		AppletViewer applet = AppletLauncher
				.applet(registratoreCassaApplet)
				.withParameters(parameters).start();
		
		String vendite = "{list:[{\"progressivo\":1,\"argomento\":null,\"barcode\":null,\"codEdicolaCorrezioneBarcode\":null,\"codFineQuotidiano\":0,\"codInizioQuotidiano\":0,\"codMotivoRespinto\":null,\"coddl\":45,\"codiceInforete\":0,\"codiceInforeteNumeroCopertinaInforete\":\"0\",\"codicePubblicazione\":1846,\"codicePubblicazioneeNumeroCopertina\":\"184641402  \",\"contoDeposito\":null,\"created\":\"2014-02-05T16:02:39\",\"crivw\":null,\"dataECFattura\":null,\"dataEstrattoConto\":null,\"dataFattura\":null,\"dataRichiamoResa\":null,\"dataUscita\":\"2014-01-31T00:00:00\",\"dataUscitaFormat\":\"31/01/2014\",\"dataVendita\":null,\"dirAlias\":null,\"fornito\":0,\"fornitoBolla\":null,\"fornitoFondoBolla\":null,\"fornitoRifornimenti\":null,\"fornitoSP\":null,\"fornitoStorico\":null,\"giacenza\":0,\"giacenzaCalculator\":null,\"giacenzaIniziale\":-5,\"giacenzaSP\":null,\"giancezaPressoDl\":2,\"giorniValiditaRichiesteRifornimento\":7,\"idProdotto\":null,\"idtn\":944597,\"imgMiniaturaName\":null,\"immagine\":null,\"importo\":0,\"importoFormat\":\"4,50\",\"importoTotale\":null,\"isContoDeposito\":\"No\",\"isScaduta\":\"No\",\"mapCiqCfq\":null,\"maxStatisticaVisualizzare\":12,\"note\":null,\"noteByCpu\":null,\"numCopertinePrecedentiPerRifornimenti\":2,\"numGiorniDaDataUscitaPerRichiestaRifornimento\":24,\"numeroCopertina\":\"41402  \",\"numeroCopertinaInforete\":\"\",\"pagato\":null,\"periodicita\":null,\"periodicitaInt\":5,\"periodicitaPk\":null,\"periodicitaPkObj\":null,\"periodicitaStr\":\"1|5\",\"pk\":null,\"prezzo\":null,\"prezzoCopertina\":4.5,\"prezzoCopertinaFormat\":\"4,50\",\"prezzoCopertinaVendita\":4.5,\"prezzoEdicola\":null,\"prezzoEdicolaFormat\":\"\",\"prodottoDl\":false,\"prodottoNonEditoriale\":false,\"provenienzaConto\":0,\"puoRichiedereRifornimenti\":false,\"quantita\":1,\"quantitaCopieContoDeposito\":0,\"reso\":null,\"resoBolla\":null,\"resoFuoriVoce\":null,\"resoRichiamoPersonalizzato\":null,\"resoRiscontrato\":0,\"resoStorico\":null,\"respinto\":0,\"richiedereRifornimenti\":false,\"sottoTitolo\":\"spiderman-let. casta\",\"tipo\":1,\"tipoRichiamoResa\":null,\"tipoRitiro\":\"In Edicola\",\"titolo\":\"CIAK\",\"updated\":\"2014-02-05T16:02:39\",\"vendite\":null,\"venduto\":0},"
				+ "{\"progressivo\":2,\"prodottoNonEditoriale\":\"true\",\"coddl\":0,\"idtn\":0,\"numeroCopertina\":\"\",\"quantita\":\"1\",\"idProdotto\":\"100250\",\"titolo\":\"PROVA ALIMENTARI\",\"sottoTitolo\":\"\",\"prezzoCopertina\":\"1.1\",\"prezzoCopertinaFormat\":\"1,10\",\"barcode\":\"123\",\"importoFormat\":\"1,10\",\"giacenzaIniziale\":-11,\"aliquota\":\"22\"}]}";
		String arrAliquoteReparti= "{\"0\":\"R3\",\"4\":\"R4\",\"10\":\"R1\",\"22\":\"R6\"}";
		registratoreCassaApplet.emettiScontrino(vendite, arrAliquoteReparti, "false", "0");
		
	}

}
