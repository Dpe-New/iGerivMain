package it.dpe.igeriv.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateFatturazioneUtil {

	public DateFatturazioneUtil(){}
	
	/**
	 * 
	 * @param dateStart - Data ultima fatturazione
	 * @param addMonth - Periodo di fatturazione ( Mensile, Bimestrale, Trimestrale, Quadrimestrale)
	 * @param sizeListReturn - Lunghezza della lista calcolata
	 * @return List<String>
	 */
	public static List<String> nextDate(Date dateStart, int addMonth,int sizeListReturn){
		
		try {
			List<String> dtReturn = new ArrayList<String>();
			Calendar date = Calendar.getInstance();
			date.setTime(dateStart);
			for (int i = 0; i < sizeListReturn; i++) {
				//Aggiungo mesi alla data
				date.add(Calendar.MONTH, addMonth);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String strDate = sdf.format(date.getTime());
				dtReturn.add(strDate);	
			}
			return dtReturn;
		} catch (Exception e) {
			
			return null;
		}
	}
	
	
}
