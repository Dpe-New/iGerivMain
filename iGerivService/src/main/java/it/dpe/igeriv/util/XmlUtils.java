package it.dpe.igeriv.util;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

@SuppressWarnings("rawtypes")
public class XmlUtils {
	
	/**
	 * Esegue la conversione da xml a oggetto.
	 * 
	 * @param Class cls
	 * @param File file
	 * @return Object
	 * @throws JAXBException
	 */
	public static Object unmarshall(Class cls, File file) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(cls);
		Unmarshaller m = context.createUnmarshaller();
		return m.unmarshal(file);
	}
	
	/**
	 * Esegue la conversione da oggetto a xml.
	 * 
	 * @param Object obj
	 * @param Class cls
	 * @param File f
	 * @param boolean formatOutput
	 * @throws JAXBException
	 */
	public static void marshall(Object obj, Class cls, File file, boolean formatOutput) throws JAXBException {
		Marshaller m = marshall(cls, formatOutput);
		m.setProperty("jaxb.encoding", "ISO-8859-1");
		m.marshal(obj, file);
	}

	/**
	 * Esegue la conversione da oggetto a xml.
	 * 
	 * @param Object obj
	 * @param Class cls
	 * @param OutputStream os
	 * @param boolean formatOutput
	 * @throws JAXBException
	 */
	public static void marshall(Object obj, Class cls, OutputStream os, boolean formatOutput) throws PropertyException, JAXBException {
		Marshaller m = marshall(cls, formatOutput);
		m.marshal(obj, os);
	}
	
	/**
	 * Esegue la conversione da oggetto a xml.
	 * 
	 * @param Object obj
	 * @param Class cls
	 * @param Writer wr
	 * @param boolean formatOutput
	 * @throws JAXBException
	 */
	public static void marshall(Object obj, Class cls, Writer wr, boolean formatOutput) throws PropertyException, JAXBException {
		Marshaller m = marshall(cls, formatOutput);
		m.marshal(obj, wr);
	}
	
	/**
	 * @param cls
	 * @param formatOutput
	 * @return
	 * @throws JAXBException
	 * @throws PropertyException
	 */
	private static Marshaller marshall(Class cls, boolean formatOutput) throws JAXBException, PropertyException {
		JAXBContext context = JAXBContext.newInstance(cls);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatOutput);
		return m;
	}
	
}
