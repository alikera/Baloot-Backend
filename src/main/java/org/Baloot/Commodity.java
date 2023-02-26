package org.Baloot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.util.*;

public class Commodity {
    HashMap<String, Integer> Ratings;


    private int id;
    private String name;
    private String providerId;
    private double price;
    private Set<String> categories;
    private double rating;
    private int inStock;

    public int getId() {
        return id;
    }
    public Set<String> getCategories() {
        return categories;
    }

    public int getInStock() {
        return inStock;
    }

    public Commodity(@JsonProperty ("id") int _id, @JsonProperty ("name") String _name,
                     @JsonProperty ("providerId") String _providerId, @JsonProperty ("price") double _price,
                     @JsonProperty ("categories") String _categories, @JsonProperty ("rating") double _rating,
                     @JsonProperty ("inStock") int _inStock) {
        id = _id;
        name = _name;
        providerId = _providerId;
        price = _price;

        String[] seperatedCategories = _categories.substring(1, _categories.length() - 1).split(", ");
        categories = new HashSet<>(Arrays.asList(seperatedCategories));

        rating = _rating;
        inStock = _inStock;
        Ratings = new HashMap<>();
    }

    public void rateCommodity(String username, int score) {
        Ratings.put(username, score);
        Ratings.forEach((key, value) -> System.out.println(key + " " + value));
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
