package it.dpe.igeriv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String titolo;
	private String descrizione;
	private String url;
	private String actionName;
	private Integer idModuloPadre;
	private Integer posizioneItem;
	private Integer posizioneMenu;
	private Boolean moduloPadre;
	private Integer livello;
	private boolean attivo;
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			this.getId().equals(((MenuDto) obj).getId());
		}
		return false;
	}

}
