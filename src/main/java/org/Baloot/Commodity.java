package org.Baloot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;

public class Commodity {
    HashMap<String, Integer> Ratings;

    private int id;
    private String name;
    private String providerId;
    private double price;
    private String categories;
    private double rating;
    private int inStock;

    public int getId() {
        return id;
    }

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

        Ratings = new HashMap<>();
    }

    public void rateMovie(String username, int score) {
        Ratings.put(username, score);
        Ratings.forEach((key, value) -> System.out.println(key + " " + value));
    }
    public void print() {
        System.out.println(this.id + " " + this.name + " " + this.providerId + " " + this.price + " " + this.categories
        + " " + this.rating + " " + this.inStock);
    }

    public ObjectNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("id", this.id);
        node.put("name", this.name);
        node.put("providerId", this.providerId);
        node.put("price", this.price);
        node.put("categories", this.categories);
        node.put("rating", this.rating);
        node.put("inStock", this.inStock);
        return node;
    }
}
