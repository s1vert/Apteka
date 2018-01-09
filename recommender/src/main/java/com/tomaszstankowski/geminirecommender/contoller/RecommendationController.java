package com.tomaszstankowski.geminirecommender.contoller;

import com.tomaszstankowski.geminirecommender.service.RecommendationService;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {
    private final RecommendationService service;

    public RecommendationController(RecommendationService service){
        this.service = service;
    }

    @GetMapping
    public List<RecommendedItem> getRecommendations(@RequestParam("customerId") long customerId){
        return service.getRecommendations(customerId);
    }
}
