package com.tomaszstankowski.geminirecommender.contoller;

import com.tomaszstankowski.geminirecommender.model.Rating;
import com.tomaszstankowski.geminirecommender.service.RatingService;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ratings")
public class RatingController {
    private final RatingService service;

    public RatingController(RatingService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity addRating(@RequestBody Rating rating){
        try {
            return ResponseEntity.ok(service.save(rating));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity getRatings(){
        try {
            return ResponseEntity.ok(service.findRatings());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recommendations")
    public ResponseEntity getRecommendations(long customerId){
        try {
            return ResponseEntity.ok(service.getRecommendations(customerId));
        } catch (NoSuchUserException e1){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("User with id " + customerId + " could not be found");
        }catch (Exception e2){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
