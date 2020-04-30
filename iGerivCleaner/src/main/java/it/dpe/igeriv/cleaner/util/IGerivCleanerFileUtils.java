package it.dpe.igeriv.cleaner.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

import com.google.common.base.Joiner;

/**
 * Classe di utilities java.io.File
 * 
 * @author romanom
 * 
 */
public class IGerivCleanerFileUtils {

	/**
	 * Copia un File in un altro File
	 * 
	 * @param in
	 * @param out
	 * @throws IOException 
	 */
	public static void copyFile(File in, File out) throws IOException {
		try (FileInputStream srcStream = new FileInputStream(in); 
				FileOutputStream dstStream = new FileOutputStream(out)) {
			FileChannel channel = srcStream.getChannel();
			channel.transferTo(0, channel.size(), dstStream.getChannel());
		} catch (IOException e) {
			throw e;
		}
	}
	
	/** 
	 * Ridimensiona le immagini contenute nella cartella inImgDirResized
	 * creando le immagini ridotte nella cartella di output outImgDirResized.
	 * 
	 * @param File inImgDirResized Cartella di input
	 * @param File outImgDirResized Cartella di output
	 * @param String formatName Nome del formato del file di output (es. JPEG) 
	 * @param width Larghezza dell'immagine
	 * @param height Altezza dell'immagine 
	 * @throws IOException
	 */
	public static void resizeImageDir(File inImgDirResized, File outImgDirResized, String formatName, int width, int height) throws IOException {
		for (File file : inImgDirResized.listFiles()) {
			resizeImage(file, outImgDirResized, formatName, width, height);
		}
	}

	/**
	 * Ridimensiona l'immagine creando una ridotta nella cartella di output outImgDirResized.
	 * 
	 * @param File file Il file da ridimensionare
	 * @param File outImgDirResized Cartella di output
	 * @param String formatName Nome del formato del file di output (es. JPEG) 
	 * @param width Larghezza dell'immagine
	 * @param height Altezza dell'immagine 
	 * @throws IOException
	 */
	public static void resizeImage(File file, File outImgDirResized, String formatName, int width, int height) throws IOException {
		MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
		if (mimetypesFileTypeMap.getContentType(file).toLowerCase().contains("image")) {
			File newFile = new File(outImgDirResized.getAbsolutePath() + System.getProperty("file.separator") + file.getName());
			newFile.createNewFile();
			BufferedImage bufferedImage = ImageIO.read(file);
			BufferedImage createResizedCopy = createResizedCopy(bufferedImage, width, height);
			ImageIO.write(createResizedCopy, formatName, newFile);
		}
	}
	
	/**
	 * Cancella il  contenuto di una directory.
	 * 
	 * @param File path
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
	 * @param String text
	 * @param int width
	 * @param Graphics g
	 * @param Font pFont
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
	private static BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight) {
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaledBI.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}
	
	/**
	 * Crea un'immagine fittizia, un quadrato bianco con il titolo formattato.
	 * 
	 * @param String titolo Il titolo dell'immagine
	 * @param iconWidth Larghezza dell'immagine
	 * @param iconHeight Altezza dell'immagine 
	 * @param imgDir Directory dove salvare l'immagine
	 * @param numCharBreak Numero massimo di caratteri per linea all'intreno dell'immagine 
	 * @param fontWeight 
	 * @param fontSize 
	 * @return String
	 * @throws IOException
	 */
	public static String createFakeImage(String titolo, int iconWidth, int iconHeight, String imgDir, int numCharBreak, int fontWeight, int fontSize) throws IOException {
		BufferedImage img = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = img.createGraphics();
		String tit = buildTitolo(titolo);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, iconWidth, iconHeight);
		Font scaleFontToFit = new Font("sansserif", fontWeight, fontSize);
		g2.setFont(scaleFontToFit);
		g2.setPaint(Color.BLACK);
		drawTitleString(img, tit, g2, iconWidth, iconHeight, numCharBreak);
		File imgTmpDir = new File(imgDir);
		String titFileName = buildTitoloFileName(titolo);
		String imgName = titFileName + "_fake.jpg";
		File outputfile = new File(imgTmpDir.getAbsolutePath() + System.getProperty("file.separator") + imgName);
		ImageIO.write(img, "jpg", outputfile);
		return imgName;
	}
	
	/**
	 * Disegna il titolo nell'immagine bianca regolando font e dimensioni su quadrato iconWidth x iconHeight.
	 * 
	 * @param BufferedImage img
	 * @param String tit
	 * @param Graphics2D g2
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
		return titolo.toUpperCase().replaceAll("\\b\\s{2,}\\b", " ").replaceAll(IGerivCleanerConstants.EURO_SIGN_DECIMAL, IGerivCleanerConstants.EURO_SIGN).trim();
	}
	
	private static String buildTitoloFileName(String titolo) {
		return titolo.toUpperCase().replaceAll("\\b\\s{2,}\\b", " ").replaceAll(IGerivCleanerConstants.EURO_SIGN_DECIMAL, IGerivCleanerConstants.EURO_SIGN_TEXT).replaceAll("[^a-zA-Z0-9,.]", "").trim();
	}
	
	/**
	 * @param is
	 * @param file
	 * @throws IOException
	 */
	public static void buildFileFromInputStream(InputStream is, File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = is.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		is.close();
		out.flush();
		out.close();
	}
	
	public static boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }
	    return dir.delete();
	}
}
