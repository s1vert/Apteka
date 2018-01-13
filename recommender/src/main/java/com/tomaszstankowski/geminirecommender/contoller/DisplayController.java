package com.tomaszstankowski.geminirecommender.contoller;

import com.tomaszstankowski.geminirecommender.model.Display;
import com.tomaszstankowski.geminirecommender.service.DisplayService;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/displays")
public class DisplayController {
    private final DisplayService service;

    public DisplayController(DisplayService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity addDisplay(@RequestBody Display display){
        try{
            return ResponseEntity.ok(service.save(display));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity getDisplays(){
        try{
            return ResponseEntity.ok(service.findAll());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recommendations")
    public ResponseEntity getRecommendations(@RequestParam long customerId){
        try{
            return ResponseEntity.ok(service.getRecommendations(customerId));
        }catch (NoSuchUserException e1){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("User with id " + customerId + " could not be found");
        }
        catch (Exception e2){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
