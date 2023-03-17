package org.Baloot.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Provider {
    private int id;
    private String name;
    private Date registryDate;

    private List<Commodity> myCommodities = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegistryDate() {
        return registryDate.getAsString();
    }

    public List<Commodity> getMyCommodities() {
        return myCommodities;
    }

    public double getAverageRatingCommodities() {
        return myCommodities.stream()
                .mapToDouble(Commodity::getRating) // Extract the scores as an IntStream
                .average() // Calculate the average score
                .orElse(0.0);
    }

    public void addToCommodities(Commodity commodity){
        myCommodities.add(commodity);
    }
    public Provider(@JsonProperty("id") int _id,
                    @JsonProperty("name") String _name,
                    @JsonProperty("registryDate") String _registryDate) {
        this.id = _id;
        this.name = _name;
        this.registryDate = new Date(_registryDate);
    }

    public void print() {
        System.out.println(this.id + " " + this.name + " " + this.registryDate);
    }
}


