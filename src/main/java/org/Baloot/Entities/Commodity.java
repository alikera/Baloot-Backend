package org.Baloot.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.Baloot.Exception.InvalidRatingException;
import java.util.*;

public class Commodity {
    HashMap<String, Double> Ratings;
    private int id;
    private String name;
    private int providerId;
    private double price;
    private Set<String> categories;
    private double rating;
    private int inStock;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public int getProviderId() {
        return providerId;
    }

    public int getInStock() {
        return inStock;
    }

    public double getRating() { return rating; }

    public int getCountOfRatings(){ return Ratings.size(); }
    public void decreaseInStock(){
        inStock--;
    }
    public void increaseInStock(){
        inStock++;
    }

    private double calculateAverageOfRatings(){
        double sum = 0.0;
        for (Double value : Ratings.values()) {
            sum += value;
        }

        return sum / Ratings.size();
    }
    public Commodity(@JsonProperty ("id") int _id, @JsonProperty ("name") String _name,
                     @JsonProperty ("providerId") int _providerId, @JsonProperty ("price") double _price,
                     @JsonProperty ("categories") List<String> _categories, @JsonProperty ("rating") double _rating,
                     @JsonProperty ("inStock") int _inStock) {
        id = _id;
        name = _name;
        providerId = _providerId;
        price = _price;
        categories = new HashSet<String>(_categories);
        rating = _rating;
        inStock = _inStock;
        Ratings = new HashMap<>();
    }
    public boolean isInCategoryGiven(String category){
        for(String cat : categories){
            if(cat.equalsIgnoreCase(category)){
                return true;
            }
        }
        return false;
    }
    public void rateCommodity(String username, int score) throws InvalidRatingException {
        if (score < 0 || score > 10) {
            throw new InvalidRatingException("Invalid Rating");
        }
        Ratings.put(username, (double) score);
        rating = calculateAverageOfRatings();
    }
    public void print() {
        System.out.println(this.id + " " + this.name + " " + this.providerId + " " + this.price + " " + this.rating + " " + this.inStock);
        for (String category : categories) {
            System.out.println(category);
        }
    }

    public ObjectNode toJson() {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode categoriesNode = mapper.valueToTree(categories);
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode arrayNode = factory.arrayNode();

        ObjectNode node = mapper.createObjectNode();
        node.put("id", this.id);
        node.put("name", this.name);
        node.put("providerId", this.providerId);
        node.put("price", this.price);
        node.put("categories", categoriesNode);
        node.put("rating", this.rating);
        node.put("inStock", this.inStock);
        return node;
    }
}
