package org.Baloot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Commodity {

    private int id;
    private String name;
    private String providerId;
    private double price;
    private String categories;
    private double rating;
    private int inStock;

    public Commodity(@JsonProperty ("id") int _id, @JsonProperty ("name") String _name,
                     @JsonProperty ("providerId") String _providerId, @JsonProperty ("price") double _price,
                     @JsonProperty ("categories") String _categories, @JsonProperty ("rating") double _rating,
                     @JsonProperty ("inStock") int _inStock) {
        id = _id;
        name = _name;
        providerId = _providerId;
        price = _price;
        categories = _categories;
        rating = _rating;
        inStock = _inStock;
    }

    public void print() {
        System.out.println(this.id + " " + this.name + " " + this.providerId + " " + this.price + " " + this.categories
        + " " + this.rating + " " + this.inStock);
    }
}
