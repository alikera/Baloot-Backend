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
    private List<Commodity> myCommodities = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegistryDate() {
        return registryDate;
    }

    public List<Commodity> getMyCommodities() {
        return myCommodities;
    }

    public void addToCommodities(Commodity commodity){
        myCommodities.add(commodity);

        averageRatingCommodities = myCommodities.stream()
                .mapToDouble(Commodity::getRating) // Extract the scores as an IntStream
                .average() // Calculate the average score
                .orElse(0.0);
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


