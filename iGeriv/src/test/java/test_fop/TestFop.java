package test_fop;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;

public class TestFop {

	public static void main(String[] args) throws Exception {
		System.out.println("START");
		Source xsltSrc = new StreamSource(new File("C:\\Temp\\Model.xsl"));
		System.out.println("START 1 ");
        //Fop fop = FopFactory.newInstance().newFop(MimeConstants.MIME_TIFF, new FileOutputStream("C:\\Temp\\Model.tiff"));
		System.out.println("START 2 ");

        Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSrc);
		System.out.println("START 3 ");

        //transformer.transform(xsltSrc, new SAXResult(fop.getDefaultHandler()));

		
	}
}
