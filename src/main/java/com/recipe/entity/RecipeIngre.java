package com.recipe.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="ingredient")
@Getter
@Setter
@ToString
public class RecipeIngre {
	
	@Id
	@Column(name="ingredient_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; //아이디
	
	private String ingreMaterial; //재료량
	
	@Column(length = 1000) // Adjust the length as needed
	private String ingreName; //재료 이름

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipe_id")
	private Recipe recipe;
	
}
