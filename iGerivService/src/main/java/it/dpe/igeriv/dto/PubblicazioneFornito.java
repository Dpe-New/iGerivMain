package it.dpe.igeriv.dto;

import java.sql.Timestamp;

/**
 * @author mromano
 *
 */
public interface PubblicazioneFornito {
	public Integer getCoddl();
	public Integer getEditore();
	public Boolean isEditoreComune();
	public Timestamp getDataUscita();
	public Integer getFornito();
	public String getCodiceInforeteNumeroCopertinaInforete();
	public String getCodicePubblicazioneeNumeroCopertina();
}
