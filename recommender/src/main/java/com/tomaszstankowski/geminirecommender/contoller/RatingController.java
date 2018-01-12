package com.tomaszstankowski.geminirecommender.contoller;

import com.tomaszstankowski.geminirecommender.model.Rating;
import com.tomaszstankowski.geminirecommender.service.RatingService;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {
    private final RatingService service;

    public RatingController(RatingService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity addRating(@RequestBody Rating rating){
        Rating r = service.save(rating);
        if(r == null)
            return ResponseEntity.status(500).build();
        return ResponseEntity.ok(r);
    }

    @GetMapping
    public List<Rating> getRatings(){
        return service.findRatings();
    }

    @GetMapping("/recommendations")
    public List<RecommendedItem> getRecommendations(long customerId){
        return service.getRecommendations(customerId);
    }
}
