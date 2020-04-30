package it.dpe.igeriv.vo.pk;

import java.sql.Timestamp;

public interface IBollaDettaglioPk {

	Integer getCodFiegDl();

	void setCodFiegDl(Integer codFiegDl);

	Integer getCodEdicola();

	void setCodEdicola(Integer codEdicola);

	Timestamp getDtBolla();

	void setDtBolla(Timestamp dtBolla);

	String getTipoBolla();

	void setTipoBolla(String tipoBolla);

	Integer getPosizioneRiga();

	void setPosizioneRiga(Integer posizioneRiga);

	String toString();

}