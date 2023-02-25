package org.Baloot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class Parser {

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

    public void main(String args[]) throws JsonProcessingException {
        String data = "{“username”: “user1”, “password”: “1234”, “email”: “user@gmail.com”, “birthDate”:“1977-09-15”, “address”: “address1”, “credit”: 1500}";
        addUserParser(data);
    }
}
