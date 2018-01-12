package com.tomaszstankowski.geminirecommender.contoller;

import com.tomaszstankowski.geminirecommender.model.Display;
import com.tomaszstankowski.geminirecommender.service.DisplayService;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
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
        Display d = service.save(display);
        if(d == null)
            return ResponseEntity.status(500).build();
        return ResponseEntity.ok(d);
    }

    @GetMapping
    public List<Display> getDisplays(){
        return service.findAll();
    }

    @GetMapping("/recommendations")
    public List<RecommendedItem> getRecommendations(@RequestParam long customerId){
        return service.getRecommendations(customerId);
    }
}
