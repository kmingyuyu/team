package com.recipe.myPage.global.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyPageSerchDto {
	
	private String data = "";
	
	private LocalDate startTime = LocalDate.now().minusDays(30);
	
	private LocalDate endTime = LocalDate.now();
	
	private String searchBy;
	
	private String searchQuery = "";
	
}
 