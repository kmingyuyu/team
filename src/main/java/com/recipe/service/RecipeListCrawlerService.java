package com.recipe.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recipe.entity.RecipeOrder;
import com.recipe.entity.Recipe;
import com.recipe.repository.DetailRecipeRopository;
import com.recipe.repository.IngredientRepository;
import com.recipe.repository.RecipeListRepository;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeListCrawlerService {
    @Autowired
    private final RecipeListRepository recipeListRepository;
    private final IngredientRepository ingredientRepository;
    private final DetailRecipeRopository detailRecipeRopository;
    private int count = 1;

    public void crawlAndSaveRecipes() throws IOException {
        String url = "https://wtable.co.kr/recipes";
        Document doc = Jsoup.connect(url).get();
        Elements recipeElements = doc.select("a:has(.erZvWP)");
        
        for (Element recipeLink : recipeElements) {
            String detailUrl = recipeLink.attr("href");



            // Now, you can crawl and save the detail page
            crawlAndSaveDetailPage(detailUrl);
        }
    }

    private void crawlAndSaveDetailPage(String detailUrl) throws IOException {
        // Check if the URL is relative and construct the full URL
        if (!detailUrl.startsWith("http://") && !detailUrl.startsWith("https://")) {
            detailUrl = "https://wtable.co.kr" + detailUrl;
        }

        Document detailDoc = Jsoup.connect(detailUrl).get();

       
        String description = detailDoc.select(".IdQIJ").text();
        String basic = detailDoc.select(".fCbbYE").text();
        Elements recipeImgs = detailDoc.select(".ihCzrN img");
        
        for (Element img : recipeImgs) {
        
        	
//        	String recipeImg =  img.attr("src");
//        	RecipeOrder detailRecipe = new RecipeOrder();
//        	detailRecipe.setRecipes(Integer.toString(count));
//        	detailRecipe.setRecipeImg(recipeImg);
//        	detailRecipeRopository.save(detailRecipe);
        	
        }
        count++;
        	
        	
        
        
        
        String title = detailDoc.select(".kIVrZW").text();
        String imageUrl = detailDoc.select("img").attr("src");

        
        System.out.println(basic);
        Recipe recipeListEntity = new Recipe();
//        RecipeIngre ingredient = new RecipeIngre();
        
       
        
        
        
        
        recipeListEntity.setImageUrl(imageUrl);
//        ingredientRepository.save(ingredient);
        recipeListRepository.save(recipeListEntity);

    }
}
