package it.dpe.igeriv.web.applet.scaricoDatiDl;

import it.dpe.igeriv.web.applet.scaricoDatiDl.exception.UnreadableFileOnServerException;
import it.dpe.igeriv.web.resources.ClientMessageBundle;

import java.applet.Applet;
import java.awt.Container;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Scanner;

import javax.swing.ProgressMonitorInputStream;

import netscape.javascript.JSObject;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamAdapter;
import org.apache.commons.net.io.Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class SendToFtpApplet extends Applet {
	private static final long serialVersionUID = 1L;
	private String ftpUser;
	private String ftpPwd;
	private String ftpDir;
	private String ftpServerUrl;
	private String ftpPort;
	private String fileName;
	private String url;
	private String sessionId;  
	private JSObject window = null;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void init() {
		window = JSObject.getWindow(this);
		AccessController.doPrivileged(new PrivilegedAction() {
			@Override
			public Object run() {
				ClientMessageBundle.initialize();
				setParameters();
				return null;
			}
		});
	}
	
	@Override
	public void start() {
		
	}
	
	@Override
	public void stop() {
		super.stop();
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testConnection() throws Exception {
		AccessController.doPrivileged(new PrivilegedAction() {
			@Override
			public Object run() {
				try {
					FTPClient ftpClient = null;
			    	try {
			    		ftpClient = ftpConnect();
			    	} finally {
			    		disconnect(ftpClient);
			    	}
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void esporta(final String fileName, final String tipo) throws Exception {
		this.fileName = fileName;
		AccessController.doPrivileged(new PrivilegedAction() {
			@Override
			public Object run() {
				try {
					File file = httpDownload();
					String firstLine = readFirstLine(file);
					if (firstLine != null && !firstLine.equals("") && firstLine.contains("<html>")) {
						throw new UnreadableFileOnServerException();
					}
					sendFileToFtp(file);
					window.eval("esportazioneFileCompletata('" + fileName + "','" + tipo + "')");
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
				return null;
			}

			private String readFirstLine(File file) throws FileNotFoundException {
				Scanner scan = new Scanner(file);
				if (scan.hasNextLine()) {
					String line = scan.nextLine();
					return line;
				}
				return "";
			}
		});
	}
	
	private void setParameters() {
		ftpUser = getParameter("ftpUser");
		ftpPwd = getParameter("ftpPwd");
		ftpDir = getParameter("ftpDir");
		ftpServerUrl = getParameter("ftpServerUrl");
		ftpPort = getParameter("ftpPort");
		fileName = getParameter("fileName");
		sessionId = getParameter("sessionId");
		url = getParameter("url");
	}
	
	/**
	 * Esegue il download del file usando http.
	 * Usato come metodo de fallback nel caso in cui 
	 * fallisca il download via ftp.
	 * 
	 * @return File
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException 
	 * @throws AuthenticationException 
	 */
	private File httpDownload() throws ClientProtocolException, IOException, URISyntaxException, AuthenticationException {
		File result = new File(System.getProperty("java.io.tmpdir") + "\\" + fileName);
		DefaultHttpClient httpclient = new DefaultHttpClient(); 
		HttpGet httpget = new HttpGet(new URI(url + "/" + fileName));
		
		//setCredentials(httpclient);
		
		HttpResponse response = httpclient.execute(httpget/*, localContext*/);  
		HttpEntity entity = response.getEntity();  
		if (entity != null) {  	
			ProgressMonitorInputStream  stO =
	            new ProgressMonitorInputStream(findParentFrame(), ClientMessageBundle.get("igeriv.message.attendere.download"), entity.getContent());
	    	stO.getProgressMonitor().setMaximum((int)entity.getContentLength());
	    	OutputStream stD = new FileOutputStream(result);
	        Util.copyStream(
	                stO,
	                stD,
	                0,
	                entity.getContentLength(),
	                new CopyStreamAdapter() {
	                    public void bytesTransferred(long totalBytesTransferred,
	                            int bytesTransferred,
	                            long streamSize) {
	                    }
	        });
	        stD.flush();
	        stD.close();
		}  
		return result;
	}

	private void sendFileToFtp(File file) throws IOException {
		FTPClient ftpClient = null;
    	try {
    		ftpClient = ftpConnect();
    		ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
    		ftpClient.storeFile(fileName, new FileInputStream(file));
    	} finally {
    		disconnect(ftpClient);
    	}
	}
	
	/**
     * Esegue la connessione FTP.
     * 
     * @param String userName
     * @param String password
     * @param String ftpPathOut
     * @return FTPClient
     * @throws IOException
     */
    private FTPClient ftpConnect() throws IOException {
		FTPClient ftpClient = new FTPClient();
        try {
        	ftpClient.connect(ftpServerUrl, new Integer(ftpPort));
	        ftpClient.setSoTimeout(10 * 1000);
	        ftpClient.login(ftpUser, ftpPwd);
	        ftpClient.setDataTimeout(60 * 1000);
	        ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
            ftpClient.changeWorkingDirectory(ftpDir);
        }
        catch (IOException ioe) {
            disconnect(ftpClient);
            throw ioe;
        }
        return ftpClient;
    }
    
    /**
	 * @param ftpClient
	 */
	private static void disconnect(FTPClient ftpClient) {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            }
            catch (IOException ioe) {}
        }
    }
	
	private Frame findParentFrame() {
		Container c = this;
		while (c != null) {
			if (c instanceof Frame)
				return (Frame) c;

			c = c.getParent();
		}
		return (Frame) null;
	}
	
	@SuppressWarnings("unused")
	private void setCredentials(DefaultHttpClient httpclient) {
		BasicHttpContext localContext = new BasicHttpContext();
		BasicScheme basicAuth = new BasicScheme();
		localContext.setAttribute("preemptive-auth", basicAuth);
		httpclient.addRequestInterceptor(new PreemptiveAuthInterceptor(), 0);
		httpclient.getCookieStore().addCookie(new BasicClientCookie("jsessionid", sessionId));
	}
	
	static class PreemptiveAuthInterceptor implements HttpRequestInterceptor {
	    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
	        AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
	        if (authState.getAuthScheme() == null) {
	            AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
	            CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
	            HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
	            credsProvider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort(), "DL_Realm"),
	    				new UsernamePasswordCredentials("214", "aaaaa"));
	            if (authScheme != null) {
	                Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort(), "DL_Realm"));
	                if (creds == null) {
	                    throw new HttpException("No credentials for preemptive authentication");
	                }
	                authState.setAuthScheme(authScheme);
	                authState.setCredentials(creds);
	            }
	        }
	    }
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
