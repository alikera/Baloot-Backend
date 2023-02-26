package org.Baloot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler {
    public List<User> users = new ArrayList<>();
    public List<Provider> providers = new ArrayList<>();
    public  List<Commodity> commodities = new ArrayList<>();
    private Parser parser;
    private Commodity findByCommodityId(int commodityId) throws CommodityNotFoundException {
        for (Commodity commodity : commodities) {
            if (commodity.getId() == commodityId) {
                return commodity;
            }
        }
        throw new CommodityNotFoundException("Error: Couldn't find commodity with the given Id!");
    }
    private User findByUsername(String username) throws UserNotFoundException {
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        throw new UserNotFoundException("Error: Couldn't find user with the given Username!");
    }

    private void processAddUser(User user){
        for (User current : users) {
            if (Objects.equals(current.getUsername(), user.getUsername())) {
                current.modifyFields(user);
                return;
            }
        }

        users.add(user);
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

    public void executeCommands(String[] command) throws IOException, ExceptionHandler {
        parser = new Parser();
        System.out.println(command[0]);

        switch (command[0]) {
            case "addUser" -> {
                User user = parser.addUserParser(command[1]);
                Pattern pattern = Pattern.compile("[0-9a-zA-Z]+");
                Matcher matcher = pattern.matcher(user.getUsername());
                if(matcher.matches()) {
                    processAddUser(user);
                }
                else{
                    System.out.println("eeeeeeeee");

                }
                System.out.println(users.size());
                for (User current : users) {
                    current.print();
                }
            }
            case "addProvider" -> {
                Provider provider = parser.addProviderParser(command[1]);
                providers.add(provider);
                provider.print();
            }
            case "addCommodity" -> {
                Commodity commodity = parser.addCommodityParser(command[1]);
                commodities.add(commodity);
                commodity.print();
            }
            case "getCommoditiesList" -> getCommoditiesList();
            case "rateCommodity" -> {
                try {
                    ObjectNode node = parser.rateCommodityParser(command[1]);
                    if (node.get("score").asInt() < 0 || node.get("score").asInt() > 10) {
                        throw new InvalidRatingException("Error: Invalid Score");
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
                    Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
                    User userFound = findByUsername(node.get("username").asText());

                    commodityFound.rateCommodity(node.get("username").asText(), node.get("score").asInt());
                } catch (ExceptionHandler e) {
                    System.out.println(e.getMessage());
                }
            }
            case "addToBuyList" -> addToUserBuyList(command[1]);
            case "removeFromBuyList" -> removeFromUserBuyList(command[1]);
            case "getCommodityById" -> getCommodityById(parser.getCommodityByIdParser(command[1]));
            case "getCommoditiesByCategory" -> getCommodityByCategory(parser.getCommodityByCategoryParser(command[1]));
            case "getBuyList" -> getBuyList(parser.getBuyListParser(command[1]));
            default -> {
            }
            //TODO Exception
        }
    }
    public void addToUserBuyList(String command) throws JsonProcessingException, ExceptionHandler {
        ObjectNode node = parser.modifyBuyListParser(command);
        Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
        if (commodityFound.getInStock() == 0) {
            System.out.println("Error: Commodity out of stock");
            return;
        }
        User userFound = findByUsername(node.get("username").asText());
        userFound.addToBuyList(node.get("commodityId").asInt());
    }

    public void removeFromUserBuyList(String command) throws JsonProcessingException, ExceptionHandler {
        ObjectNode node = parser.modifyBuyListParser(command);
        Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
        User userFound = findByUsername(node.get("username").asText());
        userFound.removeFromBuyList(node.get("commodityId").asInt());
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

    public void getCommodityById(int id) throws JsonProcessingException, ExceptionHandler {
        Commodity commodity = findByCommodityId(id);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commodityNode = commodity.toJson();
        String commodityInfo = mapper.writeValueAsString(commodityNode);
        System.out.println();
        ObjectNode mainNode = mapper.createObjectNode();
        mainNode.put("data", mapper.readTree(commodityInfo));
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mainNode));
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

    public void getBuyList(String username) throws JsonProcessingException, ExceptionHandler {
        User user = findByUsername(username);
        Set<Integer> buyListIds = user.getBuyList();

        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> commodityNodes = new ArrayList<>();
        for (int commodityId : buyListIds) {
            Commodity commodity = findByCommodityId(commodityId);
            commodityNodes.add(commodity.toJson());
        }

        ObjectNode mainNode = mapper.createObjectNode();
        mainNode.putPOJO("buyList", commodityNodes);
        String jsonString = mapper.writeValueAsString(mainNode);

        ObjectNode newNode = mapper.createObjectNode();
        newNode.put("data", mapper.readTree(jsonString));

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(newNode));
    }

}
