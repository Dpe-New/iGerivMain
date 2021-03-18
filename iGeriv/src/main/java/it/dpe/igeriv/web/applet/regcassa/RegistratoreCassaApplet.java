package it.dpe.igeriv.web.applet.regcassa;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.security.crypto.codec.Base64;

import com.google.common.base.Strings;

import it.dpe.igeriv.web.applet.regcassa.detection.DetectionTest;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.DriverDirFinder;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.InstallDirFinder;
import it.dpe.igeriv.web.applet.regcassa.dirfinder.ScontriniDirFinder;
import it.dpe.igeriv.web.applet.regcassa.driverinstall.DriverInstall;
import it.dpe.igeriv.web.applet.regcassa.dto.RegCassaDatiDto;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaCommunicationException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaDownloadResourceException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaDriverInstallationException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaMissingActiveProcessException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaMissingDriverException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaMissingInstallationPathException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaMissingScontriniPathException;
import it.dpe.igeriv.web.applet.regcassa.exception.RegCassaNotConnectedException;
import it.dpe.igeriv.web.applet.regcassa.exception.UnsupportedOSException;
import it.dpe.igeriv.web.applet.regcassa.factory.DetectionTestFactory;
import it.dpe.igeriv.web.applet.regcassa.factory.DriverDirFinderFactory;
import it.dpe.igeriv.web.applet.regcassa.factory.DriverInstallFactory;
import it.dpe.igeriv.web.applet.regcassa.factory.FileWatcherFactory;
import it.dpe.igeriv.web.applet.regcassa.factory.FileWriterFactory;
import it.dpe.igeriv.web.applet.regcassa.factory.InitRegCassaFactory;
import it.dpe.igeriv.web.applet.regcassa.factory.InstallDirFinderFactory;
import it.dpe.igeriv.web.applet.regcassa.factory.ScontriniDirFinderFactory;
import it.dpe.igeriv.web.applet.regcassa.filewriter.FileWriter;
import it.dpe.igeriv.web.applet.regcassa.init.InitRegCassa;
import it.dpe.igeriv.web.applet.regcassa.utils.OSValidator;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaConstants;
import it.dpe.igeriv.web.applet.regcassa.utils.RegCassaUtils;
import it.dpe.igeriv.web.resources.ClientMessageBundle;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import netscape.javascript.JSObject;

/**
 * Applet iGeriv per il Registratore di Cassa
 * 
 * @author mromano
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RegistratoreCassaApplet extends Applet {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_LOG_FILE_NAME = RegCassaConstants.REG_CASSA_APPLET_LOG;
	private static final int DEFAULT_LOG_FILE_SIZE = 1024 * 1024;
	public static final Integer OS_WINDOWS = 0;
	public static final Integer OS_UNIX_LINUX = 1;
	public static final Integer OS_MAC = 2;
	public static final Integer OS_SOLARIS = 3;
	private Logger logger = null;
	private JSObject window = null;
	private Frame parentFrame;
	private String scheme;
	private String serverName;
	private String serverPort;
	private String context;
	private String toSendDir;
	private String pathEmissioneScontrini;
	private String installationDir;
	private String driverDir;
	private String userRegCassaLocalDir;
	private String scontriniRegCassaLocalDir;
	private String fileNamePrefix;
	private String fileNameDigitLength;
	private String millsTaskInterval;
	private String millsTaskTimeout;
	private String codRegCassa;
	private String nomeProcesso;
	private String iniFileContent;
	private Boolean abilitaLog;
	private String mailFrom;
	private String mailTo;
	private String logFileName;
	private Integer logFileSize;
	private String smtpHost;
	private String smtpUser;
	private String smtpPwd;
	private String supportedOS;
	private String binaryUnixLinux;
	private String binaryMac;
	private String binarySolaris;
	private Boolean isScontrinoFileNameFixed;
	private String codEdicola;
	private String baseUrl;
	
	static {
	    MailcapCommandMap mcap = new MailcapCommandMap();
	    mcap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
	    mcap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
	    mcap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
	    mcap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed; x-java-fallback-entry=true");
	    mcap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
	    CommandMap.setDefaultCommandMap(mcap);
	}
	
	@Override
	public void init() {
		super.init();
		Logger.getLogger("foo");
		logger = Logger.getAnonymousLogger();
		window = JSObject.getWindow(this);
		parentFrame = getParentFrame(this);
		centerWindow(parentFrame);
		AccessController.doPrivileged(new PrivilegedAction() {
			@Override
			public Object run() {
				window.eval("ray.ajax();");
				try {
					initLogging();
					ClientMessageBundle.initialize();
					logPlatformInfo();
					setParameters();
				} catch (Throwable e) {
					logger.fine(RegCassaUtils.getStackTrace(e));
					if (abilitaLog) {
						window.eval("confirmSendErrorToDpe()");
					}
				}
				return null;
			}

			/**
			 * Logga le info sul sistema
			 * 
			 * @throws IOException 
			 * @throws InterruptedException 
			 */
			private void logPlatformInfo() throws IOException, InterruptedException {
				logger.fine("---------------------------- SYSTEM INFO ----------------------------");
				if (abilitaLog) {
					Properties systemProperties = System.getProperties();
					Enumeration enuProp = systemProperties.propertyNames();
					while (enuProp.hasMoreElements()) {
						String propertyName = (String) enuProp.nextElement();
						String propertyValue = systemProperties.getProperty(propertyName);
						logger.fine(propertyName + ": " + propertyValue);
					}
				}
				logger.fine("---------------------------------------------------------------------");
			}

			/**
			 * Inizializza il log
			 */
			private void initLogging() {
				try {
					abilitaLog = getParameter("abilitaLog") == null ? new Boolean(false) : Boolean.valueOf(getParameter("abilitaLog"));
					if (abilitaLog) {
						logFileName = getParameter("logFileName") == null ? DEFAULT_LOG_FILE_NAME : getParameter("logFileName");
						logFileSize = getParameter("logFileSize") == null ? DEFAULT_LOG_FILE_SIZE : new Integer(getParameter("logFileSize"));
						String pattern = System.getProperty(RegCassaConstants.JAVA_IO_TMP_DIR) + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + logFileName;
						FileHandler handler = new FileHandler(pattern, logFileSize, 1, false);
						handler.setFormatter(new SimpleFormatter());
						handler.setLevel(Level.ALL);
						logger.addHandler(handler);
						logger.setLevel(Level.ALL);
					}
					logger.fine("logging initialized");
				} catch (IOException e) {
					throw new IllegalStateException("Could not add file handler.", e);
				}
			}
		});
	}
	
	@Override
	public void start() {
		AccessController.doPrivileged(new PrivilegedAction() {
			@Override
			public Object run() {
				window.eval("ray.ajax();");
				try {
					checkSupportedOS();
					findRegCassaInstallationDir();
					findRegCassaScontrininDir();
					findRegCassaDriverDir();
					checkRegCassaActiveProcess();
					doCustomDetectionTest();
					doConfirmInitMessages();
				} catch (RegCassaMissingActiveProcessException e) {
					window.eval("alertMissingRegCassaActiveProcess()");
				} catch (RegCassaMissingScontriniPathException e) {
					window.eval("promptForRegCassaScontriniPath()");
				} catch (RegCassaMissingInstallationPathException e) {
					window.eval("promptForRegCassaLocalInstallPath()");
				} catch (RegCassaNotConnectedException e) {
					window.eval("alertRegCassaNotConnected()");
				} catch (RegCassaDownloadResourceException e) {
					window.eval("alertRegCassaDownloadResourceException()");
				} catch (UnsupportedOSException e) {
					window.eval("alertUnsupportedOS()");
				} catch (RegCassaMissingDriverException e) {
					window.eval("alertMissingDriver('" + e.getDriverName() + "')");
				} catch (Throwable e) {
					logger.fine(RegCassaUtils.getStackTrace(e));
					if (abilitaLog) {
						window.eval("confirmSendErrorToDpe()");
					}
				} finally {
					if (window != null) {
						window.eval("unBlock()");
					}
				}
				return null;
			}

			/**
			 * Verfica che il sistema operativo in uso sia tra quelli suppportati dal driver del reg. di cassa.
			 * Se il parametro <code>supportedOS</code> è nullo ed è in uso un sistema operativo diverso da Windows, lancio eccezione. 
			 * 
			 * @throws UnsupportedOSException
			 */
			private void checkSupportedOS() throws UnsupportedOSException {
				if (supportedOS != null && !supportedOS.equals("")) {
					String[] split = supportedOS.split(",");
					List<Integer> listSupportedOS = new ArrayList<Integer>();
					for (String os : split) {
						listSupportedOS.add(new Integer(os.trim()));
					}
					if (!listSupportedOS.contains(OSValidator.getCurrentOS())) {
						throw new UnsupportedOSException();
					}
				} else if ((supportedOS == null || supportedOS.equals("")) && !OSValidator.getCurrentOS().equals(OS_WINDOWS)) {
					throw new UnsupportedOSException();
				}
			}

			/**
			 * Cerca il percorso di installazione del reg. cassa e fa il
			 * prompt all'utente nel caso in cui non venga trovato
			 * 
			 * @throws Exception
			 */
			private void findRegCassaInstallationDir() throws Exception {
				logger.fine("Entered RegistratoreCassaApplet.findRegCassaInstallationDir()");
				InstallDirFinder finder = InstallDirFinderFactory.buildInstallDirFinder(new Integer(codRegCassa), logger);
				installationDir = finder.find(userRegCassaLocalDir, logger);
				if (installationDir.endsWith(System.getProperty(RegCassaConstants.FILE_SEPARATOR))) {
					installationDir = installationDir.substring(0, installationDir.lastIndexOf(System.getProperty(RegCassaConstants.FILE_SEPARATOR)));
				}
				logger.fine("installationDir=" + installationDir);
				logger.fine("Exiting RegistratoreCassaApplet.findRegCassaInstallationDir()");
			}
			
			/**
			 * Cerca il percorso scontrini del reg. cassa e fa il
			 * prompt all'utente nel caso in cui non venga trovato
			 * 
			 * @throws Exception
			 */
			private void findRegCassaScontrininDir() throws Exception {
				logger.fine("Entered RegistratoreCassaApplet.findRegCassaScontrininDir()");
				ScontriniDirFinder finder = ScontriniDirFinderFactory.buildScontriniDirFinder(new Integer(codRegCassa), logger, context);
				pathEmissioneScontrini = finder.find(scontriniRegCassaLocalDir, logger, baseUrl, parentFrame);
				if (pathEmissioneScontrini.endsWith(System.getProperty(RegCassaConstants.FILE_SEPARATOR)) || pathEmissioneScontrini.contains("*.")) {
					pathEmissioneScontrini = pathEmissioneScontrini.substring(0, pathEmissioneScontrini.lastIndexOf(System.getProperty(RegCassaConstants.FILE_SEPARATOR)));
				}
				logger.fine("pathEmissioneScontrini=" + pathEmissioneScontrini);
				logger.fine("Exiting RegistratoreCassaApplet.findRegCassaScontrininDir()");
			}
			
			/**
			 * Cerca il percorso dove risiede il .exe del driver del reg. cassa
			 * 
			 * @throws Exception
			 */
			private void findRegCassaDriverDir() throws Exception {
				logger.fine("Entered RegistratoreCassaApplet.findRegCassaDriverDir()");
				DriverDirFinder finder = DriverDirFinderFactory.buildDriverDirFinder(new Integer(codRegCassa), logger);
				driverDir = finder.find(installationDir, logger);
				if (driverDir.endsWith(System.getProperty(RegCassaConstants.FILE_SEPARATOR))) {
					driverDir = driverDir.substring(0, driverDir.lastIndexOf(System.getProperty(RegCassaConstants.FILE_SEPARATOR)));
				}
				logger.fine("driverDir=" + driverDir);
				logger.fine("Exiting RegistratoreCassaApplet.findRegCassaDriverDir()");
			}

			/**
			 * Verifica che il processo del reg. cassa che ascolta una cartella
			 * per emettere gli scontrini sia attivo e nel caso non lo sia prova
			 * ad avviarlo
			 * 
			 * @throws RegCassaMissingActiveProcessException
			 * @throws IOException
			 */
			private void checkRegCassaActiveProcess() throws RegCassaMissingActiveProcessException, IOException {
				logger.fine("Entered RegistratoreCassaApplet.checkRegCassaActiveProcess()");
				logger.fine("processo=" + (driverDir + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + nomeProcesso));
				boolean processExists = RegCassaUtils.isProcessRunning(nomeProcesso);
				if (!processExists) {
					processExists = RegCassaUtils.executeProcess(new File(driverDir + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + nomeProcesso));
					if (!processExists) {
						throw new RegCassaMissingActiveProcessException();
					}
				}
				logger.fine("Exiting RegistratoreCassaApplet.checkRegCassaActiveProcess()");
			}
			
			/**
			 * Esegue un test customizzato per tipo di reg. di cassa per
			 * verificarne il corretto funzionamento
			 * 
			 * @throws RegCassaCommunicationException
			 * @throws IOException
			 */
			private void doCustomDetectionTest() throws RegCassaCommunicationException, Exception {
				logger.fine("Entered RegistratoreCassaApplet.doCustomDetectionTest()");
				DetectionTest dt = DetectionTestFactory.buildDetectionTest(pathEmissioneScontrini, installationDir, new Integer(codRegCassa), window, new Long(millsTaskInterval), logger);
				dt.detect();
				logger.fine("Exiting RegistratoreCassaApplet.doCustomDetectionTest()");
			}
			
			/**
			 * Mostra un messaggio di conferma sulla configurazione del reg. di cassa 
			 */
			private void doConfirmInitMessages() {
				logger.fine("Entered RegistratoreCassaApplet.doConfirmInitMessages()");
				Integer type = new Integer(codRegCassa);
				if (type.equals(RegCassaConstants.MCT_FLASH) || type.equals(RegCassaConstants.MCT_SPOT)) {
					window.eval("showAlertRepartiRegCassa('" + ClientMessageBundle.get("igeriv.reg.cassa.flash.reparti.alert") + "')");
				} else if (type.equals(RegCassaConstants.DITRON)) {
					window.eval("showAlertRepartiRegCassa('" + ClientMessageBundle.get("igeriv.reg.cassa.ditron.reparti.alert") + "')");
				}
				logger.fine("Exiting RegistratoreCassaApplet.doConfirmInitMessages()");
			}

		});
	}
	
	/**
	 * Esegue l'inizializzaizone del registratore di cassa dopo che l'utente ha acconsentito 
	 * al messaggio che avvisa che saranno riconfigurati i reparti del reg. cassa.
	 */
	public void initRegCassa() {
		AccessController.doPrivileged(new PrivilegedAction() {
			@Override
			public Object run() {
				try {
					window.eval("ray.ajax();");
					initilizeRegCassa();
				} catch (RegCassaMissingActiveProcessException e) {
					window.eval("alertMissingRegCassaActiveProcess()");
				} catch (Throwable e) {
					logger.fine(RegCassaUtils.getStackTrace(e));
					if (abilitaLog) {
						window.eval("confirmSendErrorToDpe()");
					}
				} finally {
					if (window != null) {
						window.eval("unBlock()");
					}
				}
				return null;
			}
			
			/**
			 * Esegue la configurazione (opzionale) del registratore di cassa (es. reparti iva)
			 * customizzata per modello di reg. di cassa.
			 * 
			 * @throws IOException
			 */
			private void initilizeRegCassa() throws Exception {
				logger.fine("Entered RegistratoreCassaApplet.initilizeRegCassa()");
				InitRegCassa dt = InitRegCassaFactory.buildInitRegCassa(iniFileContent, pathEmissioneScontrini, installationDir, new Integer(codRegCassa), window, new Long(millsTaskInterval), logger);
				dt.init();
				logger.fine("Exiting RegistratoreCassaApplet.initilizeRegCassa()");
			}
		});
		
	}

	/**
	 * @param vendite
	 */
	public void emettiScontrino(final String vendite, final String aliquoteReparti, final String scontrinoACredito, final String tipoScontrino) {
		logger.fine("Entered RegistratoreCassaApplet.emettiScontrino() with params: vendite=" + vendite + ",aliquoteReparti=" + aliquoteReparti + ",scontrinoACredito=" + scontrinoACredito + ",tipoScontrino=" + tipoScontrino);
		AccessController.doPrivileged(new PrivilegedAction() {
			@Override
			public Object run() {
				logger.fine("Entered RegistratoreCassaApplet.emettiScontrino.AccessController.run()");
				try {
					Boolean bScontrinoACredito = new Boolean((Strings.isNullOrEmpty(scontrinoACredito)) ? "false" : scontrinoACredito);
					Map<Integer, String> mapAliquoteReparti = buildMapAliquoteReparti(aliquoteReparti);
					List<RegCassaDatiDto> list = buildRegCassaDatiList(vendite);
					String fileName = fileNamePrefix + getFileCounter() + ".TXT";
					logger.fine("fileName=" + fileName);
					File file = new File(pathEmissioneScontrini + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + fileName);
					writeFile(mapAliquoteReparti, list, file, bScontrinoACredito, FileWriter.Operation.EMISSIONE, FileWriter.TipoScontrino.values()[Strings.isNullOrEmpty(tipoScontrino) ? FileWriter.TipoScontrino.NORMALE.getValue() : Integer.parseInt(tipoScontrino)]);
					addFileMonitor(file);
				} catch (Throwable e) {
					logger.fine(RegCassaUtils.getStackTrace(e));
					if (abilitaLog) {
						window.eval("confirmSendErrorToDpe()");
					}
				}
				logger.fine("Exiting RegistratoreCassaApplet.emettiScontrino.AccessController.run()");
				return null;
			}

			/**
			 * Costruisce una lista di RegCassaDatiDto dalla Stringa vendite in formato JSON
			 * 
			 * @param String vendite 
			 * @return List<RegCassaDatiDto>
			 */
			private List<RegCassaDatiDto> buildRegCassaDatiList(String vendite) {
				JSONObject json = (JSONObject) JSONSerializer.toJSON(vendite);
				JSONArray jsonArray = json.getJSONArray("list");
				List<RegCassaDatiDto> list = new ArrayList<RegCassaDatiDto>();
				for (int i = 0; i < jsonArray.size(); i++) {
				    JSONObject row = jsonArray.getJSONObject(i);
				    RegCassaDatiDto dto = new RegCassaDatiDto();
					dto.setProgressivo(row.getString("progressivo"));
					dto.setProdottoNonEditoriale(row.getString("prodottoNonEditoriale"));
					dto.setCoddl(row.getString("coddl"));
					dto.setAliquota(row.get("aliquota") != null ? row.getString("aliquota") : "0");
					dto.setBarcode(row.getString("barcode"));
					dto.setGiacenzaIniziale(row.getString("giacenzaIniziale"));
					dto.setIdProdotto(row.getString("idProdotto"));
					dto.setIdtn(row.getString("idtn"));
					dto.setImportoFormat(row.getString("importoFormat"));
					dto.setNumeroCopertina(row.getString("numeroCopertina"));
					dto.setPrezzoCopertina(row.getString("prezzoCopertina"));
					dto.setPrezzoCopertinaFormat(row.getString("prezzoCopertinaFormat"));
					dto.setQuantita(row.getString("quantita"));
					dto.setSottoTitolo(RegCassaUtils.buildDescrizioneItemScontrino(row.getString("sottoTitolo")));
					dto.setTitolo(RegCassaUtils.buildDescrizioneItemScontrino(row.getString("titolo")));
					list.add(dto);
				}
				return list;
			}

			/**
			 * Costruisce la mappa aliquota : reparto.
			 * 
			 * @param String aliquoteReparti
			 * @return Map<Integer, String>
			 */
			private Map<Integer, String> buildMapAliquoteReparti(final String aliquoteReparti) {
				logger.fine("Entered RegistratoreCassaApplet.buildMapAliquoteReparti() with params: aliquoteReparti=" + aliquoteReparti);
				Map<Integer, String> mapRepartoAliquota = new HashMap<Integer, String>();
				String reps = aliquoteReparti.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\"", "");
				for (String s : reps.split(",")) {
					String[] aliqRep = s.split(":");
					mapRepartoAliquota.put(new Integer(aliqRep[0]), aliqRep[1]);
				}
				logger.fine("Exiting RegistratoreCassaApplet.buildMapAliquoteReparti()");
				return mapRepartoAliquota;
			}
			
			/**
			 * Ritorna l'ultimo progressivo del file scontrino.
			 * 
			 * @return String
			 */
			private String getFileCounter() {
				logger.fine("Entered RegistratoreCassaApplet.getFileCounter()");
				if (!isScontrinoFileNameFixed) {
					File fileExport = new File(pathEmissioneScontrini);
					File[] listFiles = fileExport.listFiles();
					int higher = 0;
					if (listFiles != null) {
						for (int i = 0; i < listFiles.length; i++) {
							String fileName = listFiles[i].getName();
							if (fileName.contains(fileNamePrefix)) {
								int counter = Integer.parseInt(fileName.substring(fileNamePrefix.length(), fileNamePrefix.length() + 10));
								if (counter > higher) {
									higher = counter;
								}
							}
						}
					}
					String format = String.format("%%0%dd", new Integer(fileNameDigitLength));
					String format2 = String.format(format, ++higher);
					logger.fine("Exiting RegistratoreCassaApplet.getFileCounter() with file counter=" + format2);
					return format2;
				}
				return "";
			}
		});
		logger.fine("Exiting RegistratoreCassaApplet.emettiScontrino()");
	}

	/**
	 * Aggiunge un timer che fa il monitor del file. Appena il file viene
	 * rinominato (in .OK o .KO) viene chiamata la callback che chiama un metodo
	 * javascript della pagina.
	 * 
	 * @param File
	 *            file
	 */
	private void addFileMonitor(File file) {
		logger.fine("Entered RegistratoreCassaApplet.addFileMonitor() with params: file=" + file);
		Timer timer = new Timer();
		TimerTask task = FileWatcherFactory.buildTimerTask(file, new Integer(codRegCassa), window, timer, Long.parseLong(millsTaskTimeout), logger);
		timer.schedule(task, new Date(), new Long(millsTaskInterval));
		logger.fine("Exiting RegistratoreCassaApplet.addFileMonitor()");
	}

	/**
	 * @param mapAliquoteReparti 
	 * @param vendite
	 * @param file
	 * @param operazione
	 * @throws IOException
	 */
	private void writeFile(Map<Integer, String> mapAliquoteReparti, List<RegCassaDatiDto> vendite, File file, Boolean scontrinoACredito, FileWriter.Operation operazione, FileWriter.TipoScontrino tipoScontrino) throws IOException {
		logger.fine("Entered RegistratoreCassaApplet.writeFile() with params: vendite=" + vendite + ",file=" + file + ",operazione=" + operazione);
		FileWriter fw = FileWriterFactory.buildFileWriter(new Integer(codRegCassa), file, logger);
		fw.write(mapAliquoteReparti, vendite, scontrinoACredito, operazione, tipoScontrino);
		logger.fine("Exiting RegistratoreCassaApplet.writeFile()");
	}
	
	/**
	 * Fa il download e installa il driver
	 */
	public void downloadAndExecuteDriver() {
		logger.fine("Entered RegistratoreCassaApplet.downloadAndExecuteDriver()");
		AccessController.doPrivileged(new PrivilegedAction() {
			@Override
			public Object run() {
				try {
					window.eval("ray.ajax()");
					DriverInstall di = DriverInstallFactory.buildDriverInstall(new Integer(codRegCassa), logger);
					di.install(logger, baseUrl, parentFrame);
					start();
				} catch (RegCassaDriverInstallationException e) {
					window.eval("alertRegCassaDownloadResourceException()");
				} catch (Throwable e) {
					logger.fine(RegCassaUtils.getStackTrace(e));
					window.eval("confirmSendErrorToDpe()");
				}
				return null;
			}
		});
		logger.fine("Exiting RegistratoreCassaApplet.downloadAndExecuteDriver()");
	}

	/**
	 * Invia una mail con il file log di errori
	 */
	public void sendLogFile() {
		logger.fine("Entered RegistratoreCassaApplet.sendLogFile()");
		AccessController.doPrivileged(new PrivilegedAction() {
			@Override
			public Object run() {
				try {
					File lastLogFile = getLastLogFile();
					logger.fine("lastLogFile=" + (lastLogFile != null ? lastLogFile.getName() : null));
				    Properties props = System.getProperties();
				    props.put("mail.transport.protocol", "smtp");
				    props.put("mail.smtp.host", new String(Base64.decode(smtpHost.getBytes())));
				    props.put("mail.smtp.auth", "true");
				    Authenticator auth = new SMTPAuthenticator();
				    Session session = Session.getInstance(props, auth);
				    MimeMessage message = new MimeMessage(session);
				    message.setFrom(new InternetAddress(mailFrom));
				    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
				    message.setSubject(MessageFormat.format(ClientMessageBundle.get("igeriv.mail.subject.errori.da.applet.reg.cassa"), codEdicola));
				    MimeBodyPart messageBodyPart = new MimeBodyPart();
				    messageBodyPart.setText(MessageFormat.format(ClientMessageBundle.get("igeriv.mail.body.errori.da.applet.reg.cassa"), lastLogFile.getName()));
				    Multipart multipart = new MimeMultipart();
				    multipart.addBodyPart(messageBodyPart);
				    messageBodyPart = new MimeBodyPart();
				    DataSource source = new FileDataSource(lastLogFile);
				    messageBodyPart.setDataHandler(new DataHandler(source));
				    messageBodyPart.setFileName(lastLogFile.getName());
				    multipart.addBodyPart(messageBodyPart);
				    message.setContent(multipart);
				    Transport.send( message );
				    logger.fine("Exiting RegistratoreCassaApplet.sendLogFile()");
				} catch (Throwable e) {
					logger.fine(RegCassaUtils.getStackTrace(e));
				}
				return null;
			}
			
			/**
			 * @author mromano
			 *
			 */
			final class SMTPAuthenticator extends Authenticator {
				public PasswordAuthentication getPasswordAuthentication() {
					logger.fine("Entered RegistratoreCassaApplet.SMTPAuthenticator.getPasswordAuthentication()");
					PasswordAuthentication passwordAuthentication = null;
					try {
						passwordAuthentication = new PasswordAuthentication(new String(Base64.decode(smtpUser.getBytes())), new String(Base64.decode(smtpPwd.getBytes())));
					} catch (Throwable e) {
						e.printStackTrace();
						logger.fine(RegCassaUtils.getStackTrace(e));
					}
					logger.fine("Exiting RegistratoreCassaApplet.SMTPAuthenticator.getPasswordAuthentication()");
					return passwordAuthentication;
		        }
		    }

			/**
			 * Ritorna il file di log più recente
			 * 
			 * @return File
			 */
			private File getLastLogFile() {
				File baseLogFile = new File(System.getProperty(RegCassaConstants.JAVA_IO_TMP_DIR) + System.getProperty(RegCassaConstants.FILE_SEPARATOR) + RegCassaConstants.REG_CASSA_APPLET_LOG);
				File lastLogFile = baseLogFile;
				File logFileDir = baseLogFile.getParentFile();
				if (logFileDir.isDirectory()) {
					FileFilter fileFilterFiles = new FileFilter() {
						@Override
						public boolean accept(File file) {
							return file != null && file.isFile() && file.getName().contains(RegCassaConstants.REG_CASSA_APPLET_LOG) && !file.getName().contains(".lck");
						}
					};
					File[] listFiles = logFileDir.listFiles(fileFilterFiles);
					Arrays.sort(listFiles, new Comparator() {
						@Override
						public int compare(Object f1, Object f2) {
							Long lastModified = ((File) f1).lastModified();
							Long lastModified2 = ((File) f2).lastModified();
							return lastModified2.compareTo(lastModified);
						}
					});
					lastLogFile = (listFiles != null && listFiles.length > 0) ? listFiles[0] : baseLogFile;
				}
				return lastLogFile;
			}
			
		});
	}

	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	private void setParameters() {
		logger.fine("Entered RegistratoreCassaApplet.setParameters()");
		scheme = Strings.isNullOrEmpty(getParameter("scheme")) ? "" : getParameter("scheme");
		serverName = Strings.isNullOrEmpty(getParameter("serverName")) ? "" : getParameter("serverName");
		serverPort = Strings.isNullOrEmpty(getParameter("serverPort")) ? "" : getParameter("serverPort");
		context = Strings.isNullOrEmpty(getParameter("context")) ? "" : getParameter("context");
		toSendDir = Strings.isNullOrEmpty(getParameter("toSendDir")) ? "" : getParameter("toSendDir");
		pathEmissioneScontrini = Strings.isNullOrEmpty(getParameter("pathEmissioneScontrini")) ? "" : getParameter("pathEmissioneScontrini");
		userRegCassaLocalDir = getParameter("userRegCassaLocalDir");
		scontriniRegCassaLocalDir = getParameter("scontriniRegCassaLocalDir");
		fileNamePrefix = getParameter("fileNamePrefix");
		fileNameDigitLength = getParameter("fileNameDigitLength");
		millsTaskInterval = getParameter("millsTaskInterval");
		millsTaskTimeout = getParameter("millsTaskTimeout");
		codRegCassa = getParameter("codRegCassa");
		nomeProcesso = getParameter("nomeProcesso");
		iniFileContent = getParameter("iniFileContent");
		mailFrom = getParameter("mailFrom");
		mailTo = getParameter("mailTo");
		smtpHost = getParameter("smtpHost");
		smtpUser = getParameter("smtpUser");
		smtpPwd = getParameter("smtpPwd");
		supportedOS = getParameter("supportedOS");
		binaryUnixLinux = getParameter("binaryUnixLinux");
		binaryMac = getParameter("binaryMac");
		binarySolaris = getParameter("binarySolaris");
		isScontrinoFileNameFixed = Strings.isNullOrEmpty(getParameter("isScontrinoFileNameFixed")) ? new Boolean(false) : Boolean.parseBoolean(getParameter("isScontrinoFileNameFixed"));
		codEdicola = getParameter("codEdicola");
		baseUrl = scheme + RegCassaConstants.COLON + RegCassaConstants.DOUBLE_SLASH + serverName + RegCassaConstants.COLON + serverPort;
		logger.fine("context=" + context);
		logger.fine("serverName=" + serverName);
		logger.fine("toSendDir=" + toSendDir);
		logger.fine("installationDir=" + installationDir);
		logger.fine("pathEmissioneScontrini=" + pathEmissioneScontrini);
		logger.fine("userRegCassaLocalDir=" + userRegCassaLocalDir);
		logger.fine("scontriniRegCassaLocalDir=" + scontriniRegCassaLocalDir);
		logger.fine("fileNamePrefix=" + fileNamePrefix);
		logger.fine("fileNameDigitLength=" + fileNameDigitLength);
		logger.fine("millsTaskInterval=" + millsTaskInterval);
		logger.fine("millsTaskTimeout=" + millsTaskTimeout);
		logger.fine("codRegCassa=" + codRegCassa);
		logger.fine("nomeProcesso=" + nomeProcesso);
		logger.fine("iniFileContent=" + iniFileContent);
		logger.fine("mailFrom=" + mailFrom);
		logger.fine("mailTo=" + mailTo);
		logger.fine("smtpHost=" + smtpHost);
		logger.fine("smtpUser=" + smtpUser);
		logger.fine("smtpPwd=" + smtpPwd);
		logger.fine("supportedOS=" + supportedOS);
		logger.fine("binaryUnixLinux=" + binaryUnixLinux);
		logger.fine("binaryMac=" + binaryMac);
		logger.fine("binarySolaris=" + binarySolaris);
		logger.fine("isScontrinoFileNameFixed=" + isScontrinoFileNameFixed.toString());
		logger.fine("codEdicola=" + codEdicola.toString());
		logger.fine("Exiting RegistratoreCassaApplet.setParameters()");
	}

	public void setUserRegCassaLocalDir(String userRegCassaLocalDir) {
		this.userRegCassaLocalDir = userRegCassaLocalDir;
	}

	public void setDefaultLocalDir(String pathEmissioneScontrini) {
		this.pathEmissioneScontrini = pathEmissioneScontrini;
	}

	public String getScontriniRegCassaLocalDir() {
		return scontriniRegCassaLocalDir;
	}

	public void setScontriniRegCassaLocalDir(String scontriniRegCassaLocalDir) {
		this.scontriniRegCassaLocalDir = scontriniRegCassaLocalDir;
	}
	
	private Frame getParentFrame(Component c) {
		Container cont = c.getParent();
		if (cont instanceof Frame) {
			return (Frame) cont;
		} else {
			return getParentFrame((Component) cont);
		}
	}
	
	private void centerWindow(Window frame) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) (dimension.getWidth() / 2);
	    int y = (int) (dimension.getHeight() / 2);
	    frame.setLocation(x, y);
	}
}
