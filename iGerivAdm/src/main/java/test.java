import java.util.Date;
import java.util.List;

import it.dpe.igeriv.util.DateFatturazioneUtil;

public class test {

	public static void main(String[] args) {
		
		List<String> res = DateFatturazioneUtil.nextDate(new Date("01/01/2016"), 3, 3);
		for (String string : res) {
			System.out.println(string);
		}
		
	}
}
