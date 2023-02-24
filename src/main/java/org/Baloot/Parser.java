package org.Baloot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class Parser {

    public static User addUserParser(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        String respData = mapper.writeValueAsString(data);
        User user = mapper.readValue(respData, User.class);
        user.print();
        return user;
    }

    public static void main(String args[]) throws JsonProcessingException {
        String data = "{“username”: “user1”, “password”: “1234”, “email”: “user@gmail.com”, “birthDate”:“1977-09-15”, “address”: “address1”, “credit”: 1500}";
        addUserParser(data);
    }
}
