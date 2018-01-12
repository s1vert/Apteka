package com.tomaszstankowski.geminirecommender.service;

import com.tomaszstankowski.geminirecommender.model.Display;
import com.tomaszstankowski.geminirecommender.model.Rating;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class DisplayService {
    private ItemBasedRecommender recommender;
    private static final String dataFilePath = "data/displays.csv";

    public DisplayService(){
        try {
            createRecommender();
        }catch (Exception e){}
    }

    private void createRecommender() throws IOException, IllegalArgumentException{
        DataModel model = new FileDataModel(new File(dataFilePath));
        ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(model);
        recommender = new GenericItemBasedRecommender(model, itemSimilarity);
    }

    public Display save(Display display){
        try {
            if (recommender == null) {
                writeToFile(display);
                createRecommender();
            }else {
                boolean exists = false;
                try {
                    exists = recommender.getDataModel().getItemIDsFromUser(display.getCustomerId()).contains(display.getProductId());
                } catch (TasteException e) {
                    //does not exist
                }
                if(!exists){
                    writeToFile(display);
                    createRecommender();
                }
            }
            return display;
        }catch (Exception e){ return null; }
    }

    public List<Display> findAll(){
        try {
            return Files.readAllLines(Paths.get(dataFilePath)).stream()
                    .map(line -> {
                        Scanner s = new Scanner(line);
                        s.useDelimiter(",");
                        long customerId = s.nextLong();
                        long productId = s.nextLong();
                        return new Display(customerId, productId);
                    })
                    .collect(Collectors.toList());
        }catch (IOException e){ return new ArrayList<>(); }
    }

    public List<RecommendedItem> getRecommendations(long customerId){
        try{
            return recommender.recommend(customerId, 5);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    private void writeToFile(Display display) throws IOException{
        String line = String.format("%d,%d\n", display.getCustomerId(), display.getProductId());
        final Path path = Paths.get(dataFilePath);
        path.toFile().getParentFile().mkdirs();
        Files.write(path, line.getBytes(), Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
    }
}
