package it.dpe.igeriv.web.applet.regcassa.worker;

import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingWorker;

import org.apache.commons.net.io.CopyStreamAdapter;
import org.apache.commons.net.io.Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import it.dpe.igeriv.web.resources.ClientMessageBundle;

/**
 * @author mromano
 *
 */
public class HttpDownloadWorker extends SwingWorker<Void, Void> {
	private final String srcFileUri;
	private final File destFile;
	private final Window parentFrame;
	private final CountDownLatch startLatch;
	
	public HttpDownloadWorker(String srcFileUri, File destFile, Window parentFrame, CountDownLatch startLatch) {
		super();
		this.srcFileUri = srcFileUri;
		this.destFile = destFile;
		this.parentFrame = parentFrame;
		this.startLatch = startLatch;
	}

	@Override
	protected Void doInBackground() throws URISyntaxException, ClientProtocolException, IOException {
		DefaultHttpClient httpclient = new DefaultHttpClient(); 
		HttpGet httpget = new HttpGet(new URI(srcFileUri));
		HttpResponse response = httpclient.execute(httpget);  
		HttpEntity entity = response.getEntity();  
		if (entity != null) {  	
	    	OutputStream stD = new FileOutputStream(destFile);
	    	try {
		        InputStream content = entity.getContent();
		        if (parentFrame != null) {
		        	content = new ProgressMonitorInputStream(parentFrame, ClientMessageBundle.get("igeriv.message.attendere.download"), content);
		        	((ProgressMonitorInputStream) content).getProgressMonitor().setMaximum((int)entity.getContentLength());
		        }
				Util.copyStream(
		        		content,
		                stD,
		                0,
		                entity.getContentLength(),
		                new CopyStreamAdapter() {
		                    public void bytesTransferred(long totalBytesTransferred,
		                            int bytesTransferred,
		                            long streamSize) {
		                    }
		        });
	    	} catch (Throwable e) {
	    		if (stD != null) {
	    			try {
	    				stD.flush();
	    				stD.close();
	    			}
	    			catch (IOException e1) { }
	    		}
	    		if (destFile != null && destFile.isFile() && destFile.exists()) {
	    			destFile.delete();
	    		}
	    		throw e;
	    	} finally {
	    		if (stD != null) {
	    			try {
	    				stD.flush();
	    				stD.close();
	    			}
	    			catch (IOException e) { }
	    		}
	    	}
		}  
		return null;
	}
	
	@Override
	protected void done() {
		super.done();
		startLatch.countDown();
	}

}
