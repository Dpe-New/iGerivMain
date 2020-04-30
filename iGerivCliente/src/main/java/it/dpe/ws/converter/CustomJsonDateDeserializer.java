package it.dpe.ws.converter;

import it.dpe.igeriv.util.DateUtilities;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * @author mromano
 *
 */
public class CustomJsonDateDeserializer extends JsonDeserializer<Date> {
	
	@Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
        String date = jsonparser.getText();
        try {
            return DateUtilities.parseDate(date, DateUtilities.FORMATO_DATA_YYYY_MM_DD);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
