package it.dpe.igeriv.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Joiner;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;

/**
 * Classe di utilities java.io.File
 * 
 * @author romanom
 * 
 */
public class FileUtils {

	/**
	 * Copia un File in un altro File
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copyFile(File in, File out) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;
		try {
			sourceChannel = new FileInputStream(in).getChannel();
			destinationChannel = new FileOutputStream(out).getChannel();
			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		} finally {
			if (sourceChannel != null) {
				sourceChannel.close();
			}
			if (destinationChannel != null) {
				destinationChannel.close();
			}
		}
	}

	/**
	 * Ridimensiona le immagini contenute nella cartella inImgDirResized creando
	 * le immagini ridotte nella cartella di output outImgDirResized.
	 * 
	 * @param File
	 *            inImgDirResized Cartella di input
	 * @param File
	 *            outImgDirResized Cartella di output
	 * @param String
	 *            formatName Nome del formato del file di output (es. JPEG)
	 * @param width
	 *            Larghezza dell'immagine, se null viene calcolata in base all'altezza
	 * @param height
	 *            Altezza dell'immagine, se null viene calcolata in base alla larghezza
	 * @throws IOException
	 */
	public static void resizeImageDir(File inImgDirResized, File outImgDirResized, int width, int height) throws IOException {
		for (File file : inImgDirResized.listFiles()) {
			if (file.isFile()) {
				resizeImage(file, outImgDirResized, width, height);
			}
		}
	}

	/**
	 * Ridimensiona l'immagine creando una ridotta nella cartella di output
	 * outImgDirResized.
	 * 
	 * @param File
	 *            file Il file da ridimensionare
	 * @param File
	 *            outImgDirResized Cartella di output
	 * @param width
	 *            Larghezza dell'immagine, se null viene calcolata in base all'altezza
	 * @param height
	 *            Altezza dell'immagine, se null viene calcolata in base alla larghezza
	 * @throws IOException
	 */
	public static void resizeImage(File file, File outImgDirResized, Integer width, Integer height) throws IOException {
		if (Files.probeContentType(Paths.get(file.getAbsolutePath())).toLowerCase().contains("image")) {
			BufferedImage bufferedImage = ImageIO.read(file);
			BufferedImage createResizedCopy = createResizedCopy(bufferedImage, width, height);
			ImageInputStream iis = bufferedImage != null ? ImageIO.createImageInputStream(bufferedImage) : null;
			Iterator<ImageReader> ir = iis != null ? ImageIO.getImageReaders(iis) : null;
			String imgFormatName = IGerivConstants.FORMAT_IMAGE_JPEG;
			if (ir != null && ir.hasNext()) {
				String formatName = ir.next().getFormatName();
				if (Collections.binarySearch(Arrays.asList(ImageIO.getWriterFormatNames()), formatName) >= 0) {
					imgFormatName = formatName;
				}
			}
			File newFile = new File(outImgDirResized.getAbsolutePath() + "/" + file.getName());
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			ImageIO.write(createResizedCopy, imgFormatName, newFile);
		}
	}

	/**
	 * Cancella il contenuto di una directory.
	 * 
	 * @param File
	 *            path
	 * @return
	 */
	public static void deleteDirectoryContent(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectoryContent(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
	}

	/**
	 * Ridimensiona il Font sulla larghezza
	 * 
	 * @param String
	 *            text
	 * @param int width
	 * @param Graphics
	 *            g
	 * @param Font
	 *            pFont
	 * @return Font
	 */
	public static Font scaleFontToFit(String text, int width, Graphics g, Font pFont) {
		float fontSize = pFont.getSize();
		float fWidth = g.getFontMetrics(pFont).stringWidth(text);
		if (fWidth <= width) {
			return pFont;
		}
		fontSize = ((float) width / fWidth) * fontSize;
		return pFont.deriveFont(fontSize);
	}

	/**
	 * Crea le immagini ridotte.
	 * 
	 * @param originalImage
	 * @param scaledWidth
	 * @param scaledHeight
	 * @return
	 */
	private static BufferedImage createResizedCopy(BufferedImage originalImage, Integer scaledWidth, Integer scaledHeight) {
		if (scaledHeight == null) {
			Double factor = new Double(originalImage.getWidth()) / new Double(scaledWidth);
			scaledHeight = new Double(originalImage.getHeight() / factor).intValue();
		} else if (scaledWidth == null) {
			Double factor = new Double(originalImage.getHeight()) / new Double(scaledHeight);
			scaledWidth = new Double(originalImage.getWidth() / factor).intValue(); 
		}
		Image scaledInstance = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_DEFAULT);
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaledBI.createGraphics();
		g.drawImage(scaledInstance, 0, 0, null);
		g.dispose();
		return scaledBI;
	}

	/**
	 * Crea un'immagine fittizia, un quadrato bianco con il titolo formattato.
	 * 
	 * @param String
	 *            titolo Il titolo dell'immagine
	 * @param iconWidth
	 *            Larghezza dell'immagine
	 * @param iconHeight
	 *            Altezza dell'immagine
	 * @param imgDir
	 *            Directory dove salvare l'immagine
	 * @param numCharBreak
	 *            Numero massimo di caratteri per linea all'intreno
	 *            dell'immagine
	 * @param fontWeight
	 * @param fontSize
	 * @return String
	 * @throws IOException
	 */
	public static String createFakeImage(String titolo, int iconWidth, int iconHeight, String imgDir, int numCharBreak, int fontWeight, int fontSize) throws IOException {
		BufferedImage img = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		String tit = buildTitolo(StringUtility.stripHtml(titolo));
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, iconWidth, iconHeight);
		Font scaleFontToFit = new Font("sansserif", fontWeight, fontSize);
		g2.setFont(scaleFontToFit);
		g2.setPaint(Color.BLACK);
		drawTitleString(img, tit, g2, iconWidth, iconHeight, numCharBreak);
		File imgTmpDir = new File(imgDir);
		String titFileName = buildTitoloFileName(titolo);
		String imgName = titFileName + "_fake.jpg";
		File outputfile = new File(imgTmpDir.getAbsolutePath() + "/" + imgName);
		ImageIO.write(img, "jpg", outputfile);
		return imgName;
	}

	/**
	 * Disegna il titolo nell'immagine bianca regolando font e dimensioni su
	 * quadrato iconWidth x iconHeight.
	 * 
	 * @param BufferedImage
	 *            img
	 * @param String
	 *            tit
	 * @param Graphics2D
	 *            g2
	 * @param iconWidth
	 * @param iconHeight
	 * @param numCharBreak
	 */
	private static void drawTitleString(BufferedImage img, String tit, Graphics2D g2, int iconWidth, int iconHeight, int numCharBreak) {
		FontMetrics fm = img.getGraphics().getFontMetrics();
		int msg_width = fm.stringWidth(tit);
		if (msg_width > iconWidth) {
			List<String> listTit = new ArrayList<String>();
			if (msg_width > iconWidth) {
				listTit = Arrays.asList(tit.replaceAll(" ", "\\|").replaceAll(" ", "").split("\\|"));
				StringBuffer sb = new StringBuffer(Joiner.on("|").join(listTit));
				if (listTit.size() > 1) {
					for (int i = 0; i < listTit.size(); i++) {
						String t = listTit.get(i);
						if (fm.stringWidth(t) > iconWidth) {
							String[] split = t.split("(?<=\\G.{" + numCharBreak + "})");
							String str = Arrays.asList(split).toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
							sb.replace(sb.indexOf(t), (sb.indexOf(t) + t.length()), str);
						}
					}
					listTit = Arrays.asList(sb.toString().split("\\|"));
				} else {
					listTit = Arrays.asList(tit.split("(?<=\\G.{" + numCharBreak + "})"));
				}
			}
			int yPos = (iconHeight / listTit.size());
			for (int i = 0; i < listTit.size(); i++) {
				int msg_x = ((img.getWidth() - fm.stringWidth(listTit.get(i))) / 2) + 1;
				int msg_y = ((i + 1) * yPos) - 2;
				g2.drawString(listTit.get(i), msg_x, msg_y);
			}
		} else {
			int ascent = fm.getMaxAscent();
			int descent = fm.getMaxDescent();
			int msg_x = (int) (img.getWidth() / 2.5 - msg_width / 2);
			int msg_y = img.getHeight() / 2 - descent / 2 + ascent / 2;
			g2.drawString(tit, msg_x, msg_y);
		}
	}

	private static String buildTitolo(String titolo) {
		return titolo.toUpperCase().replaceAll("\\b\\s{2,}\\b", " ").replaceAll(IGerivConstants.EURO_SIGN_DECIMAL, IGerivConstants.EURO_SIGN).trim();
	}

	private static String buildTitoloFileName(String titolo) {
		return titolo.toUpperCase().replaceAll("\\b\\s{2,}\\b", " ").replaceAll(IGerivConstants.EURO_SIGN_DECIMAL, IGerivConstants.EURO_SIGN_TEXT).replaceAll("[^a-zA-Z0-9,.]", "").trim();
	}

	/**
	 * @param is
	 * @param file
	 * @throws IOException
	 */
	public static void buildFileFromInputStream(InputStream is, File file) throws IOException {
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = is.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		} finally {
			if (is != null) {
				is.close();
			}
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
		}
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromFile(File file) throws IOException {
		byte[] bytes = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			long length = file.length();
			if (length > Integer.MAX_VALUE) {
				// File is too large
			}
			bytes = new byte[(int) length];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return bytes;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void splitPDFByBookmarks(String pdf, String outputFolder) throws NumberFormatException, DocumentException, IOException {
		PdfReader reader = new PdfReader(pdf);
		// List of bookmarks: each bookmark is a map with values for title,
		// page, etc
		List<Map> bookmarks = SimpleBookmark.getBookmark(reader);
		for (int i = 0; i < bookmarks.size(); i++) {
			Map bm = bookmarks.get(i);
			Map nextBM = i == bookmarks.size() - 1 ? null : bookmarks.get(i + 1);
			// In my case I needed to split the title string
			String title = ((String) bm.get("Title")).split(" ")[2];
			String startPage = ((String) bm.get("Page")).split(" ")[0];
			String startPageNextBM = nextBM == null ? "" + (reader.getNumberOfPages() + 1) : ((String) nextBM.get("Page")).split(" ")[0];
			extractBookmarkToPDF(reader, Integer.valueOf(startPage), Integer.valueOf(startPageNextBM), title + ".pdf", outputFolder);
		}
	}

	private static void extractBookmarkToPDF(PdfReader reader, int pageFrom, int pageTo, String outputName, String outputFolder) throws DocumentException, IOException {
		Document document = null;
		OutputStream os = null;
		try {
			document = new Document();
			os = new FileOutputStream(outputFolder + outputName);
			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data
			PdfImportedPage page;
			while (pageFrom < pageTo) {
				document.newPage();
				page = writer.getImportedPage(reader, pageFrom);
				cb.addTemplate(page, 0, 0);
				pageFrom++;
			}
		} finally {
			if (document != null && document.isOpen()) {
				document.close();
				document = null;
			}
			try {
				if (os != null) {
					os.flush();
					os.close();
					os = null;
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	public static void unzipEntry(File fileOrg, ZipFile zipfile, ZipEntry entry, File outputDir, Boolean appendNameZipFile) throws IOException {
		File file = new File(outputDir, entry.getName());
        if (entry.isDirectory()) {
            if (!file.mkdirs()) {
            	throw new RuntimeException("Can not create dir " + file);
            }
            return;
        }
        if (!file.getParentFile().exists()) {
        	file.getParentFile().mkdirs();
        }
        File dstFile = appendNameZipFile ? new File(outputDir, fileOrg.getName().toLowerCase().replaceAll("\\.zip", "")+"_"+entry.getName()) : file;
        BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dstFile));
        try {
            IOUtils.copy(inputStream, outputStream);
        } finally {
        	if (outputStream != null) {
        		outputStream.close();
        		outputStream = null;
        	}
        	if (inputStream != null) {
        		inputStream.close();
        		inputStream = null;
        	}
        }
    }
	
	/**
	 * Testa se il file e' aperto da un altro processo
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isFileLocked(File file) {
		try {
		    org.apache.commons.io.FileUtils.touch(file);
		    return false;
		} catch (IOException e) {
			return true;
		}
	}
	
	/**
	 * La data dell'ultima modifica viene verificata se è antecedente alla data 
	 * odierna meno il numDays passato.
	 * 
	 */
	public static boolean isDeleteFile_verificationDateFileLastModified(File file, int numDays){
		boolean resAttribute = false;
		Path p = Paths.get(file.getAbsolutePath());
	    BasicFileAttributes view;
		try {
			view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
			Date sysdate = new Date();
			Date dateFile = new Date(file.lastModified());
			sysdate = DateUtilities.togliGiorni(sysdate, numDays);
			
			if(dateFile.before(sysdate)){
				resAttribute = true;
			}else{
				resAttribute = false; 
			}
		} catch (IOException e) {
			return false;
		}
	    
		return resAttribute;
	}
	
	
	
}
