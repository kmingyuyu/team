package com.recipe.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RecipeCrawlerService {
    @Autowired


    public void crawlAndSaveRecipes() throws IOException {

        String url = "https://wtable.co.kr/recipes";
        Document doc = Jsoup.connect(url).get();
        Elements recipes = doc.select(".erZvWP");
        

        for (Element recipe : recipes) {
            String description = recipe.select(".LxJcT").text();
            String title = recipe.select(".hpYiJK").text();
            String imageUrl = recipe.select(".duQJWI").attr("src");
            


        }
    }
    
    
    
}