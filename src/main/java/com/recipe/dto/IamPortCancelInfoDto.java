package com.recipe.dto;

import com.nimbusds.jose.shaded.gson.JsonObject;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IamPortCancelInfoDto {
	
	private String code;
	private String message;
	private JsonObject response = new JsonObject();
}
