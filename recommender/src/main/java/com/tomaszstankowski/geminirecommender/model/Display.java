package com.tomaszstankowski.geminirecommender.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Display {
    private final long customerId;
    private final long productId;

    @JsonCreator
    public Display(@JsonProperty("customerId") long customerId,
                   @JsonProperty("productId") long productId){
        this.customerId = customerId;
        this.productId = productId;
    }
}
