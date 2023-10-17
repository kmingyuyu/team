package com.recipe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

@NoArgsConstructor
@Data
public class IamPortBuyInfoDto {
	private String code;
	private String message;
	private JSONObject response = new JSONObject();
}
