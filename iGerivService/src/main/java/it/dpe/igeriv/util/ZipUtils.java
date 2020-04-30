package it.dpe.igeriv.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
	static final int BUFFER = 2048;

	/**
	 * Esegue lo zip dei file.
	 * 
	 * @param File file
	 * @return ZipOutputStream
	 * @throws IOException
	 */
	public static ZipOutputStream zip(File file) throws IOException {
		BufferedInputStream origin = null;
		FileOutputStream dest = new FileOutputStream(file);
		CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
		// out.setMethod(ZipOutputStream.DEFLATED);
		byte data[] = new byte[BUFFER];
		File f = new File(".");
		String files[] = f.list();
		for (int i = 0; i < files.length; i++) {
			FileInputStream fi = new FileInputStream(files[i]);
			origin = new BufferedInputStream(fi, BUFFER);
			ZipEntry entry = new ZipEntry(files[i]);
			out.putNextEntry(entry);
			int count;
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
		}
		out.close();
		return out;
	}

	/**
	 * Esegue l'unzip dei file contenuti nel file fileIn nella cartella dirOut.
	 * 
	 * @param File fileIn
	 * @param File dirOut
	 * @return BufferedOutputStream 
	 * @throws IOException
	 */
	public static BufferedOutputStream unzip(File fileIn, File dirOut) throws IOException {
		final int BUFFER = 2048;
		BufferedOutputStream dest = null;
		FileInputStream fis = new FileInputStream(fileIn);
		CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null) {
			int count;
			byte data[] = new byte[BUFFER];
			FileOutputStream fos = new FileOutputStream(dirOut.getAbsolutePath() + "/" + entry.getName());
			dest = new BufferedOutputStream(fos, BUFFER);
			while ((count = zis.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
		}
		zis.close();
		return dest;
	}

}
