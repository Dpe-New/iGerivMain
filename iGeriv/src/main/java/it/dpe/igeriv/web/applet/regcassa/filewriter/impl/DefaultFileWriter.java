package it.dpe.igeriv.web.applet.regcassa.filewriter.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import it.dpe.igeriv.web.applet.regcassa.dto.RegCassaDatiDto;
import it.dpe.igeriv.web.applet.regcassa.filewriter.FileWriter;

public class DefaultFileWriter extends FileWriter {

	@Override
	public void write(Map<Integer, String> mapAliquoteReparti, List<RegCassaDatiDto> vendite, Boolean scontrinoACredito, FileWriter.Operation operazione, FileWriter.TipoScontrino tipoScontrino) throws IOException {

	}

}
