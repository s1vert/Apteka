package com.tomaszstankowski.geminirecommender.service;

import com.tomaszstankowski.geminirecommender.model.Rating;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
public class RatingService {
    private final String dataFilePath;

    public RatingService(@Qualifier("dataFilePath") String dataFilePath){
        this.dataFilePath = dataFilePath;
    }

    public Rating save(Rating rating){
        String line = String.format("%d,%d,%d\n", rating.getCustomerId(), rating.getProductId(), rating.getRate());
        try {
            final Path path = Paths.get(dataFilePath);
            path.toFile().getParentFile().mkdirs();
            Files.write(path, line.getBytes(), Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
            return rating;
        } catch (final IOException ioe) {
            return null;
        }
    }

    public List<Rating> findRatings(){
        List<Rating> ratings = new ArrayList<>();
        try {
            ratings = Files.readAllLines(Paths.get(dataFilePath)).stream()
                    .map(line -> {
                        Scanner s = new Scanner(line);
                        s.useDelimiter(",");
                        long customerId = s.nextLong();
                        long productId = s.nextLong();
                        short rate = s.nextShort();
                        return new Rating(customerId, productId, rate);
                    })
                    .collect(Collectors.toList());
        }catch (final IOException e){
        }
        return ratings;
    }
}
