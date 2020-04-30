package it.dpe.igeriv.web.extremecomponents;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.fop.apps.Driver;
import org.apache.fop.apps.Options;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.fop.apps.FopFactory;
//import org.apache.fop.apps.MimeConstants;
import org.extremecomponents.table.core.Preferences;
import org.extremecomponents.table.filter.PdfViewResolver;
import org.xml.sax.InputSource;

import com.icafe4j.image.ImageColorType;
import com.icafe4j.image.ImageParam;
import com.icafe4j.image.options.TIFFOptions;
import com.icafe4j.image.quant.DitherMatrix;
import com.icafe4j.image.quant.DitherMethod;
import com.icafe4j.image.tiff.TIFFTweaker;
import com.icafe4j.image.tiff.TiffFieldEnum.Compression;
import com.icafe4j.io.FileCacheRandomAccessOutputStream;
import com.icafe4j.io.RandomAccessOutputStream;



public class DpePdfViewResolver extends PdfViewResolver {

	private Logger log = null;
	private final static String USERCONFIG_LOCATION = "exportPdf.userconfigLocation";
	private static final String TRANSFORMER_FACTORY_CLASS = "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl";

	
	
	@Override
	public void resolveView(ServletRequest request, ServletResponse response, Preferences preferences, Object viewData)throws Exception {
		
		InputStream is = new ByteArrayInputStream(((String) viewData).getBytes("UTF-8")); 
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
        Driver driver = new Driver(new InputSource(is), out); 
 
        String userconfigLocation = preferences.getPreference(USERCONFIG_LOCATION); 
        if (userconfigLocation != null) { 
            InputStream input = this.getClass().getResourceAsStream(userconfigLocation); 
            if (input != null) { 
                new Options(input); 
            } 
        } 
        driver.setRenderer(Driver.RENDER_PDF); 
        driver.run(); 
        
        byte[] contents = out.toByteArray(); 
 
        InputStream input_stream = new ByteArrayInputStream(out.toByteArray());
		PDDocument pddoc = PDDocument.load(input_stream);
        BufferedImage[] images = new BufferedImage[pddoc.getNumberOfPages()];
        for (int i = 0; i < images.length; i++) {
            PDPage page = (PDPage) pddoc.getDocumentCatalog().getAllPages().get(i);
            BufferedImage image;
            try {
                image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 300); 
                images[i] = image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        
        //FileOutputStream fos = new FileOutputStream("C:\\Temp\\esempio_convert.tiff");
        ByteArrayOutputStream out_fos = new ByteArrayOutputStream(); 
        RandomAccessOutputStream rout = new FileCacheRandomAccessOutputStream(out_fos);
        ImageParam.ImageParamBuilder builder = ImageParam.getBuilder();
        ImageParam[] param = new ImageParam[1];
        TIFFOptions tiffOptions = new TIFFOptions();
        tiffOptions.setTiffCompression(Compression.CCITTFAX4);
        builder.imageOptions(tiffOptions);
        builder.colorType(ImageColorType.GRAY_SCALE).ditherMatrix(DitherMatrix.getBayer8x8Diag()).applyDither(true).ditherMethod(DitherMethod.BAYER);
        param[0] = builder.build();
        TIFFTweaker.writeMultipageTIFF(rout, param, images);
        rout.close();
        out_fos.close();
        
        byte[] contents_2 = out_fos.toByteArray(); 
        
        response.setContentLength(contents_2.length); 
        response.getOutputStream().write(contents_2); 
		
	}
	
	
}
