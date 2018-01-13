package com.tomaszstankowski.geminirecommender.service;

import com.tomaszstankowski.geminirecommender.model.Rating;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;


@Service
public class RatingService {
    private static final String dataFilePath = "data/ratings.csv";
    private UserBasedRecommender recommender;

    public RatingService(){
        try{
            createRecommender();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createRecommender() throws IOException, IllegalArgumentException, TasteException{
        DataModel model = new FileDataModel(new File(dataFilePath));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
    }

    public Rating save(Rating rating) throws IOException, TasteException{
            writeToFile(rating);
            if(recommender == null)
                createRecommender();
            else
                recommender.refresh(null);
            return rating;
    }

    private void writeToFile(Rating rating) throws IOException{
        String line = String.format("%d,%d,%f\n", rating.getCustomerId(), rating.getProductId(), rating.getRate());
        Path path = Paths.get(dataFilePath);
        path.toFile().getParentFile().mkdirs();
        Files.write(path, line.getBytes(), Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
    }

    public List<Rating> findRatings() throws IOException{
            return Files.readAllLines(Paths.get(dataFilePath)).stream()
                    .map(line -> {
                        try {
                            Scanner s = new Scanner(line);
                            s.useDelimiter(",");
                            long customerId = s.nextLong();
                            long productId = s.nextLong();
                            float rate = s.nextFloat();
                            return new Rating(customerId, productId, rate);
                        }catch (Exception e){
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }

    public List<RecommendedItem> getRecommendations(long customerId) throws TasteException, NullPointerException{
        return recommender.recommend(customerId, 5);
    }
}
