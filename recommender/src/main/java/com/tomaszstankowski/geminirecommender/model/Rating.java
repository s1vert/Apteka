package com.tomaszstankowski.geminirecommender.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Rating {
    private final long customerId;
    private final long productId;
    private final short rate;

    @JsonCreator
    public Rating(@JsonProperty("customerId") long customerId,
                  @JsonProperty("productId") long productId,
                  @JsonProperty("rate") short rate){
        this.customerId = customerId;
        this.productId = productId;
        this.rate = rate;
    }
}
