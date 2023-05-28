package org.Baloot.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Provider {
    private int id;
    private String name;
    private Date registryDate;

    private List<Commodity> myCommodities;
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegistryDate() {
        return registryDate.getAsString();
    }
    public Date getDate(){return registryDate;}
    public List<Commodity> getMyCommodities() {
        return myCommodities;
    }

    public String getImage() {
        return image;
    }
    public HashMap<String, String> getAttributes() {
        HashMap<String, String> attributes = new HashMap<>();

        attributes.put("id", String.valueOf(id));
        attributes.put("name", name);
        attributes.put("registryDate", registryDate.getAsSqlDate().toString());
        attributes.put("image", image);

        return attributes;
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
                    @JsonProperty("registryDate") String _registryDate,
                    @JsonProperty("image") String _image) {
        this.id = _id;
        this.name = _name;
        this.registryDate = new Date(_registryDate);
        myCommodities = new ArrayList<>();
        this.image = _image;
    }

    public void print() {
        System.out.println(this.id + " " + this.name + " " + this.registryDate);
    }
}


