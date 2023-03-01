package org.Baloot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.Baloot.Exception.*;
public class CommandHandler {
    public List<User> users = new ArrayList<>();
    public List<Provider> providers = new ArrayList<>();
    public  List<Commodity> commodities = new ArrayList<>();
    private Parser parser = new Parser();

    public Commodity findByCommodityId(int commodityId) throws CommodityNotFoundException {
        for (Commodity commodity : commodities) {
            if (commodity.getId() == commodityId) {
                return commodity;
            }
        }
        throw new CommodityNotFoundException("Couldn't find commodity with the given Id!");
    }

    public User findByUsername(String username) throws UserNotFoundException {
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        throw new UserNotFoundException("Couldn't find user with the given Username!");
    }

    private void addToProviderCommodityList(Commodity commodity) throws ProviderNotFoundException {
        for(Provider provider : providers){
            if(Objects.equals(provider.getId(), commodity.getProviderId())){
                provider.addToCommodities(commodity);
                return;
            }
        }
        throw new ProviderNotFoundException("Couldn't find provider with the given Id!");
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
        switch (command[0]) {
            case "addUser" -> System.out.println(addUser(command[1]));
            case "addProvider" -> System.out.println(addProvider(command[1]));
            case "addCommodity" -> System.out.println(addCommodity(command[1]));
            case "getCommoditiesList" -> System.out.println(getCommoditiesList());
            case "rateCommodity" -> System.out.println(rateCommodity(parser.rateCommodityParser(command[1])));
            case "addToBuyList" -> System.out.println(addToUserBuyList(command[1]));
            case "removeFromBuyList" -> removeFromUserBuyList(command[1]);
            case "getCommodityById" -> System.out.println(getCommodityById(parser.getCommodityByIdParser(command[1])));
            case "getCommoditiesByCategory" -> System.out.println(getCommodityByCategory(parser.getCommodityByCategoryParser(command[1])));
            case "getBuyList" -> System.out.println(getBuyList(parser.getBuyListParser(command[1])));
            default -> {
            }
            //TODO Exception
        }
    }

    public String addUser(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        User user = parser.addUserParser(data);
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]+");
        Matcher matcher = pattern.matcher(user.getUsername());
        try {
            if (matcher.matches()) {
                User foundUser = findByUsername(user.getUsername());
                foundUser.modifyFields(user);
                return makeJsonFromString(true, "User modified successfully");
            } else {
                throw new InvalidUsernameException("Invalid Username!");
            }
        }
        catch (UserNotFoundException e){
            users.add(user);
            return makeJsonFromString(true, "User added successfully");
        }
        catch (InvalidUsernameException e) {
            return makeJsonFromString(false, e.getMessage());
        }
    }

    public String addProvider(String data) throws JsonProcessingException {
        Provider provider = parser.addProviderParser(data);
        providers.add(provider);
        return makeJsonFromString(true, "Provider added successfully");
    }

    public String addCommodity(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Commodity commodity = parser.addCommodityParser(data);
        try {
            addToProviderCommodityList(commodity);
            commodities.add(commodity);
            commodity.print();
            return makeJsonFromString(true, "User added successfully");
        }
        catch (ProviderNotFoundException e){
            return makeJsonFromString(false, e.getMessage());
        }
    }

    public String rateCommodity(ObjectNode node) throws JsonProcessingException {
        try {
            Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
            User userFound = findByUsername(node.get("username").asText());
            commodityFound.rateCommodity(node.get("username").asText(), node.get("score").asInt());
            return makeJsonFromString(true, "User rated commodity successfully");
        } catch (ExceptionHandler e) {
            return makeJsonFromString(false, e.getMessage());
        }
    }

    public String addToUserBuyList(String command) throws JsonProcessingException, ExceptionHandler {
        try {
            ObjectNode node = parser.modifyBuyListParser(command);
            Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());

            if (commodityFound.getInStock() == 0) {
                throw new OutOfStockException("Commodity out of stock");
            }
            commodityFound.decreaseInStock();
            User userFound = findByUsername(node.get("username").asText());
            userFound.addToBuyList(node.get("commodityId").asInt());
            return makeJsonFromString(true, "Commodity added to user buy list successfully");
        }
        catch (CommodityNotFoundException e) {
            return makeJsonFromString(false, e.getMessage());
        }
        catch (CommodityExistenceException e) {
            return makeJsonFromString(false, e.getMessage());
        }
        catch (UserNotFoundException e) {
            return makeJsonFromString(false, e.getMessage());
        }
        catch (OutOfStockException e) {
            return makeJsonFromString(false, e.getMessage());
        }
    }

    public void removeFromUserBuyList(String command) throws JsonProcessingException, ExceptionHandler {
        ObjectNode node = parser.modifyBuyListParser(command);
        Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
        User userFound = findByUsername(node.get("username").asText());
        userFound.removeFromBuyList(node.get("commodityId").asInt());
    }

    private String getCommoditiesList() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> commodityNodes = new ArrayList<>();
        for(Commodity commodity : commodities) {
            commodityNodes.add(commodity.toJson());
        }
        ObjectNode mainNode = mapper.createObjectNode();
        mainNode.putPOJO("commoditiesList", commodityNodes);
        String data = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mainNode);

        return makeJsonFromObjectNode(true, mainNode);
    }

    public String getCommodityById(int id) throws JsonProcessingException, ExceptionHandler {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Commodity commodity = findByCommodityId(id);
            ObjectNode commodityNode = commodity.toJson();
            commodityNode.remove("inStock");
            return makeJsonFromObjectNode(true, commodityNode);
        }
        catch (CommodityNotFoundException e) {
            return makeJsonFromString(false, e.getMessage());
        }
    }

    public String getCommodityByCategory(String category) throws JsonProcessingException {
        List<Commodity> foundedCommodities = findCommoditiesByCategory(category);

        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> commodityNodes = new ArrayList<>();
        for(Commodity commodity : foundedCommodities) {
            ObjectNode commodityNode = commodity.toJson();
            commodityNode.remove("inStock");
            commodityNodes.add(commodityNode);
        }
        ObjectNode mainNode = mapper.createObjectNode();
        mainNode.putPOJO("commoditiesListByCategory", commodityNodes);

        return makeJsonFromObjectNode(true, mainNode);
    }

    public String getBuyList(String username) throws JsonProcessingException, ExceptionHandler {
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = findByUsername(username);
            Set<Integer> buyListIds = user.getBuyList();
            List<ObjectNode> commodityNodes = new ArrayList<>();
            for (int commodityId : buyListIds) {
                Commodity commodity = findByCommodityId(commodityId);
                commodityNodes.add(commodity.toJson());
            }
            ObjectNode mainNode = mapper.createObjectNode();
            mainNode.putPOJO("buyList", commodityNodes);

            return makeJsonFromObjectNode(true, mainNode);
        }
        catch (UserNotFoundException e) {
            return makeJsonFromString(false, e.getMessage());
        }
    }

    public String makeJsonFromObjectNode(Boolean success, ObjectNode dataNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("success", success);
        node.put("data", dataNode);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }

    public String makeJsonFromString(Boolean success, String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("success", success);
        node.put("data", data);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }
}
