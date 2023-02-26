package org.Baloot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class CommandHandler {
    public List<User> users = new ArrayList<>();
    public List<Provider> providers = new ArrayList<>();
    public  List<Commodity> commodities = new ArrayList<>();
    private Parser parser;
    private Commodity findByCommodityId(int commodityId){
        for (Commodity commodity : commodities) {
            if (commodity.getId() == commodityId) {
                return commodity;
            }
        }
        System.out.println("Error: Couldn't find commodity with the given Id!");
        return null;
    }
    private User findByUsername(String username){
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        System.out.println("Error: Couldn't find user with the given Username!");
        return null;
    }

    private List<Commodity> findCommoditiesByCategory(String category){
        List<Commodity> foundedCommodities = new ArrayList<>();
        for (Commodity commodity : commodities) {
            Set<String> categories = commodity.getCategories();
            if (categories.contains(category)) {
                foundedCommodities.add(commodity);
            }
        }
        return foundedCommodities;
    }

    public void executeCommands(String[] command) throws IOException {
        parser = new Parser();
        System.out.println(command[0]);

        switch (command[0]) {
            case "addUser":
                User user = parser.addUserParser(command[1]);
                users.add(user);
                user.print();
                break;
            case "addProvider":
                Provider provider = parser.addProviderParser(command[1]);
                providers.add(provider);
                provider.print();
                break;
            case "addCommodity":
                Commodity commodity = parser.addCommodityParser(command[1]);
                commodities.add(commodity);
                commodity.print();
                break;
            case "getCommoditiesList":
                getCommoditiesList();
                break;
            case "rateCommodity":
                try {
                    ObjectNode node = parser.rateCommodityParser(command[1]);
                    if(node.get("score").asInt() < 0 || node.get("score").asInt() > 10) {
                        System.out.println("Invalid Rating");
                        return;
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
                    Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
                    User userFound = findByUsername(node.get("username").asText());

                    if (commodityFound != null && userFound != null) {
                        commodityFound.rateCommodity(node.get("username").asText(), node.get("score").asInt());
                    }
                }
                catch (RuntimeException e){
                    System.out.println("Invalid Rating");
                }
                break;
            case "addToBuyList":
                addToUserBuyList(command[1]);
                break;
            case "removeFromBuyList":
                removeFromUserBuyList(command[1]);
                break;
            case "getCommodityById":
                getCommodityById(parser.getCommodityByIdParser(command[1]));
                break;
            case "getCommoditiesByCategory":
                getCommodityByCategory(parser.getCommodityByCategoryParser(command[1]));
                break;
            case "getBuyList":
                break;
            default:
                //TODO Exception
        }
    }
    public void addToUserBuyList(String command) throws JsonProcessingException {
        ObjectNode node = parser.modifyBuyListParser(command);
        Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
        if (commodityFound != null) {
            if(commodityFound.getInStock() == 0) {
                System.out.println("Error: Commodity out of stock");
                return;
            }
            User userFound = findByUsername(node.get("username").asText());
            if(userFound != null) {
                userFound.addToBuyList(node.get("commodityId").asInt());
            }
        }
    }

    public void removeFromUserBuyList(String command) throws JsonProcessingException{
        ObjectNode node = parser.modifyBuyListParser(command);
        Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
        if (commodityFound != null) {
            User userFound = findByUsername(node.get("username").asText());
            if(userFound != null) {
                userFound.removeFromBuyList(node.get("commodityId").asInt());
            }
        }
    }
    private void getCommoditiesList() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> commodityNodes = new ArrayList<>();
        for(Commodity commodity : commodities) {
            commodityNodes.add(commodity.toJson());
        }
        ObjectNode mainNode = mapper.createObjectNode();
        mainNode.putPOJO("commoditiesList", commodityNodes);
        String jsonString = mapper.writeValueAsString(mainNode);

        ObjectNode newNode = mapper.createObjectNode();
        newNode.put("data", mapper.readTree(jsonString));

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(newNode));
    }

    public void getCommodityById(int id) throws JsonProcessingException {
        Commodity commodity = findByCommodityId(id);
        if (commodity != null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode commodityNode = commodity.toJson();
            String commodityInfo = mapper.writeValueAsString(commodityNode);
            System.out.println();
            ObjectNode mainNode = mapper.createObjectNode();
            mainNode.put("data", mapper.readTree(commodityInfo));
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mainNode));
        }
    }

    public void getCommodityByCategory(String category) throws JsonProcessingException {
        List<Commodity> foundedCommodities = findCommoditiesByCategory(category);

        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> commodityNodes = new ArrayList<>();
        for(Commodity commodity : foundedCommodities) {
            commodityNodes.add(commodity.toJson());
        }
        ObjectNode mainNode = mapper.createObjectNode();
        mainNode.putPOJO("commoditiesListByCategory", commodityNodes);
        String jsonString = mapper.writeValueAsString(mainNode);

        ObjectNode newNode = mapper.createObjectNode();
        newNode.put("data", mapper.readTree(jsonString));

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(newNode));
    }

}
