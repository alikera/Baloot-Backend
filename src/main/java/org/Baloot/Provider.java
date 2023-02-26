package org.Baloot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Provider {
    private int id;
    private String name;
    private String registryDate;

    private double averageRatingCommodities;
    public int getId() {
        return id;
    }

    private List<Commodity> myCommodities = new ArrayList<>();
    public void addToCommodities(Commodity commodity){
        myCommodities.add(commodity);

        averageRatingCommodities = myCommodities.stream()
                .mapToDouble(Commodity::getRating) // Extract the scores as an IntStream
                .average() // Calculate the average score
                .orElse(0.0);
        for(Commodity cc : myCommodities){
            cc.print();
        }
        System.out.println(averageRatingCommodities);
    }
    public Provider(@JsonProperty("id") int _id,
                    @JsonProperty("name") String _name,
                    @JsonProperty("registryDate") String _registryDate) {
        this.id = _id;
        this.name = _name;
        this.registryDate = _registryDate;
    }

    public void print() {
        System.out.println(this.id + " " + this.name + " " + this.registryDate);
    }
}


