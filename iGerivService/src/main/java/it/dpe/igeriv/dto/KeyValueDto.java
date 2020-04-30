package it.dpe.igeriv.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class KeyValueDto extends BaseDto {
	private static final long serialVersionUID = 1L;
	private String key;
	private Integer keyInt;
	private String value;
	
	public KeyValueDto() {}
	
	public KeyValueDto(String key, Integer keyInt, String value) {
		super();
		this.key = key;
		this.keyInt = keyInt;
		this.value = value;
	}

	
	
}
