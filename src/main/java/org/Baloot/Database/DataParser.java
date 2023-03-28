package org.Baloot.Database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Entities.User;
import org.Baloot.Exception.InvalidRatingException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {

    public User addUserParser(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(data, User.class);
        return user;
    }

    public Provider addProviderParser(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Provider provider = mapper.readValue(data, Provider.class);
        return provider;
    }

    public Commodity addCommodityParser(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        Commodity commodity = mapper.readValue(data, Commodity.class);
        return commodity;
    }

    public ObjectNode rateCommodityParser(String data) throws JsonProcessingException, InvalidRatingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(data, new TypeReference<>() {});
        ObjectNode node = objectMapper.createObjectNode();

        String username = (String) map.get("username");
        String commodityIdGiven = map.get("commodityId").toString();
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(commodityIdGiven);
        if(!matcher.matches()){
            throw new InvalidRatingException("Invalid Rating");
        }
        int commodityId = (int) map.get("commodityId");
        int score;
        try {
            score = (int) map.get("score");
            node.put("score", score);
            node.put("username", username);
            node.put("commodityId", commodityId);
        }
        catch (RuntimeException e){
            throw new InvalidRatingException("Invalid Rating");
        }

        return node;
    }

    public int getCommodityByIdParser(String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(data, new TypeReference<>() {});
        int id = (int) map.get("id");

        return id;
    }
    public ObjectNode modifyBuyListParser(String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(data, new TypeReference<>() {});
        ObjectNode node = objectMapper.createObjectNode();
        String username = (String) map.get("username");
        int commodityId = (int) map.get("commodityId");
        node.put("username", username);
        node.put("commodityId", commodityId);
        return node;
    }
    public String getCommodityByCategoryParser(String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(data, new TypeReference<>() {});
        String category = (String) map.get("category");

        return category;
    }

    public String getBuyListParser(String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(data, new TypeReference<>() {});
        String username = (String) map.get("username");

        return username;
    }

    public void main(String args[]) throws JsonProcessingException {
        String data = "{“username”: “user1”, “password”: “1234”, “email”: “user@gmail.com”, “birthDate”:“1977-09-15”, “address”: “address1”, “credit”: 1500}";
        addUserParser(data);
    }

    public User[] getUsersParser(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        User[] user = mapper.readValue(data, User[].class);
        return user;
    }
}
