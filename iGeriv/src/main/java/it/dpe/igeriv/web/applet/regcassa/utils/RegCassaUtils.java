package it.dpe.igeriv.web.applet.regcassa.utils;

import java.awt.Desktop;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;

import it.dpe.igeriv.web.applet.regcassa.worker.HttpDownloadWorker;

public class RegCassaUtils {
	private static final String TASKLIST = "tasklist";
	
	public static String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		String string = writer.toString();
		return string;
	}
	
	/**
	 * @param serviceName
	 * @return
	 * @throws IOException
	 */
	public static boolean isProcessRunning(String serviceName) throws IOException {
		if (OSValidator.isWindows()) {
			boolean winProcessRunning = isWindowsProcessRunning(serviceName);
			return winProcessRunning;
		} else {
			boolean unixProcessRunning = isUnixProcessRunning(serviceName.replaceAll(".exe", ""));
			return unixProcessRunning;
		}
	}
	
	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean executeProcess(File file) throws IOException {
		if (OSValidator.isWindows()) {
			boolean execWinProcess = execWinProcess(file);
			return execWinProcess;
		} else if (OSValidator.isUnix() || OSValidator.isMac() || OSValidator.isSolaris()) {
			boolean execUnixProcess = execUnixProcess(file);
			return execUnixProcess;
		} else {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(file);
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Copia un File in un altro File
	 * 
	 * @param in
	 * @param out
	 * @throws IOException 
	 */
	 @SuppressWarnings("resource")
	public static void copyFile(File in, File out) throws IOException {
		FileChannel sourceChannel = new FileInputStream(in).getChannel();   
        FileChannel destinationChannel = new FileOutputStream(out).getChannel();   
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);   
        sourceChannel.close();   
        destinationChannel.close();
	}

	/**
	 * @param str
	 * @param width
	 * @param padWithChar
	 * @param trimWhitespace
	 * @return
	 */
	public static String justifyLeft(String str, final int width, char padWithChar, boolean trimWhitespace) {
		str = str != null ? (trimWhitespace ? str.trim() : str) : "";
		int addChars = width - str.length();
		if (addChars < 0) {
			return str.subSequence(0, width).toString();
		}
		final StringBuilder sb = new StringBuilder();
		sb.append(str);
		while (addChars > 0) {
			sb.append(padWithChar);
			--addChars;
		}
		return sb.toString();
	}
	
	public static String getLineFeed() {
		if (OSValidator.isWindows()) {
			return RegCassaConstants.DOS_LINEFEED;
		} else {
			return RegCassaConstants.UNIX_LINEFEED;
		}
	}
	
	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static boolean execUnixProcess(File file) throws IOException {
		Runtime.getRuntime().exec(new String[] {"/usr/bin/open", file.getAbsolutePath()});
		return true;
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static boolean execWinProcess(File file) throws IOException {
		Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
		boolean winProcessRunning = isWindowsProcessRunning(file.getName().replaceAll(".exe", ""));
		return winProcessRunning;
	}

	/**
	 * Esegue il download del file usando http.
	 * @param window 
	 * 
	 * @param fileName 
	 * @return File
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException 
	 * @throws InterruptedException 
	 * @throws AuthenticationException 
	 */
	public static void httpDownload(String srcFileUri, File destFile, Window parentFrame) throws IOException, URISyntaxException, InterruptedException {
		final CountDownLatch startLatch = new CountDownLatch(1);
		HttpDownloadWorker worker = new HttpDownloadWorker(srcFileUri, destFile, parentFrame, startLatch);
		worker.execute();
		startLatch.await();
	}
	
	/**
	 * @param p
	 * @return
	 * @throws IOException 
	 */
	public static List<String> bufferProcessOutput(Process p) throws IOException {
		List<String> output = new ArrayList<String>();
		InputStreamReader is = null;
		BufferedReader br = null;
		try {
			is = new InputStreamReader(p.getInputStream());
			br = new BufferedReader(is);  
			String line;
		    while ((line = br.readLine()) != null) {
			    output.add(line.trim());
		    }
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return output;
	}
	
	/**
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static String getUsbSerialPortNumber() throws InterruptedException, IOException {
		Process p = Runtime.getRuntime().exec(System.getProperty(RegCassaConstants.JAVA_IO_TMP_DIR) + RegCassaConstants.DEVCON_EXE + " " + RegCassaConstants.DEVCON_FIND_USB_COM_PORTS_CMD);
		p.waitFor();
		List<String> bufferProcessOutput = bufferProcessOutput(p);
		if (bufferProcessOutput.size() > 0) {
			String line = bufferProcessOutput.get(0);
			if (line.toUpperCase().contains("USB SERIAL PORT")) {
				return (line.toUpperCase().substring(line.indexOf("(") + 1, line.indexOf(")")));
			}
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public static boolean is64bit()  
    {
        return (System.getenv("ProgramW6432") != null);
    }
	
	/**
	 * @param serviceName
	 * @return
	 * @throws IOException
	 */
	private static boolean isWindowsProcessRunning(String serviceName) throws IOException {
		Process p = Runtime.getRuntime().exec(TASKLIST);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains(serviceName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param serviceName
	 * @return
	 * @throws IOException
	 */
	private static boolean isUnixProcessRunning(String serviceName) throws IOException {
		String line = null;
		Process proc = Runtime.getRuntime().exec("ps -ef");
		InputStream stream = proc.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		while ((line = reader.readLine()) != null) {
			Pattern pattern = Pattern.compile(serviceName);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Toglie tutti i caratteri accentati e speciali
	 * 
	 * @param string
	 * @return
	 */
	public static String flattenToAscii(String string) {
	    char[] out = new char[string.length()];
	    string = Normalizer.normalize(string, Normalizer.Form.NFD);
	    int j = 0;
	    for (int i = 0; i < string.length(); i++) {
	        char c = string.charAt(i);
	        if (c <= '\u007F') out[j++] = c;
	    }
	    return new String(out);
	}
	
	/**
	 * Costruisce la descrizione dell'item dello scontrino 
	 * togliendo i caratteri non accettati dal reg. di cassa
	 * 
	 * @param string
	 * @return
	 */
	public static String buildDescrizioneItemScontrino(String string) {
		return flattenToAscii(string).replaceAll("'", "").replaceAll("\"", "").replaceAll("\\*", "").replaceAll("!", "").replaceAll("&", "").replaceAll("%", "").replaceAll("$", "").replaceAll("\\\\", "").replaceAll("/", "").trim();
	}
	
}
