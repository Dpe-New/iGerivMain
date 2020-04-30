package it.dpe.igeriv.util;

public class IGerivBatchConstants {

	public static final String CHANNEL_NAME_TEMPLATE_IGERIV_DL_RIFORNIMENTI = "jms{0}SendChannel";
	public static final String CHANNEL_NAME_TEMPLATE_DL_IGERIV_REPLY = "jmsReceive{0}ReplyChannel";
	public static final String CHANNEL_NAME_TEMPLATE_DL_IGERIV_REPLY_TIMEOUT = "jmsReceive{0}ReplyTimeoutChannel";
	public static final String REPLY_PROCESSOR_BEAN_ID_SUFFIX = "ReplyProcessor";
	public static final String IMPORTER_BEAN_ID_SUFFIX = "Importer";
	public static final String FILE_INFORIV_INPUT_PATTERN = ".+(\\.zip|\\.ZIP)$";
	public static final String FILE_INFORIV_PREFIX_INVIO_EDICOLA_DL = "ED-DL_";

}
