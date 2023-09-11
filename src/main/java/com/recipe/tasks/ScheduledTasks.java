package com.recipe.tasks;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.recipe.service.RecipeCrawlerService;
import com.recipe.service.RecipeListCrawlerService;

@Component
public class ScheduledTasks {
	
	
	
    @Autowired
    private RecipeListCrawlerService crawlerListService;
    
    @Autowired
    private RecipeCrawlerService crawlerService;
    
    
    
//   
//    @Scheduled(fixedRate = 3600000) // 1시간 마다 업데이트
//    public void crawlAndSaveScheduled() {
//    	
//        try {
//
//            crawlerListService.crawlAndSaveRecipes();
//            crawlerService.crawlAndSaveRecipes();
//        } catch (IOException e) {
//            // 예외 처리 및 로그 출력
//            e.printStackTrace();
//        }
//    }
}

