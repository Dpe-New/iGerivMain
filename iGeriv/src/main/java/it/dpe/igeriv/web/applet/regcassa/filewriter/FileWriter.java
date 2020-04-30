package it.dpe.igeriv.web.applet.regcassa.filewriter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import it.dpe.igeriv.web.applet.regcassa.dto.RegCassaDatiDto;

/**
 * @author mromano
 * 
 */
public abstract class FileWriter {
	
	public enum Operation {
		EMISSIONE(0), STORNO(1);
		private int value;

		private Operation(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	};
	
	public enum TipoScontrino {
		NORMALE(0), DETTAGLIATO(1);
		private int value;

		private TipoScontrino(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	};

	public abstract void write(Map<Integer, String> mapAliquoteReparti, List<RegCassaDatiDto> vendite, Boolean scontrinoACredito, FileWriter.Operation operazione, FileWriter.TipoScontrino tipoScontrino) throws IOException;

}
