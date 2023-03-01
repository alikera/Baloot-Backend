package org.Baloot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.Baloot.Exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Baloot {
    public List<User> users = new ArrayList<>();
    public List<Provider> providers = new ArrayList<>();
    public  List<Commodity> commodities = new ArrayList<>();

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
    public ObjectNode addUser(User user) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

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

    public ObjectNode addProvider(Provider provider) throws JsonProcessingException {
        providers.add(provider);
        return makeJsonFromString(true, "Provider added successfully");
    }

    public ObjectNode addCommodity(Commodity commodity) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            addToProviderCommodityList(commodity);
            commodities.add(commodity);
            return makeJsonFromString(true, "Commodity added successfully");
        }
        catch (ProviderNotFoundException e){
            return makeJsonFromString(false, e.getMessage());
        }
    }

    public ObjectNode rateCommodity(ObjectNode node) throws JsonProcessingException {
        try {
            Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
            User userFound = findByUsername(node.get("username").asText());
            commodityFound.rateCommodity(node.get("username").asText(), node.get("score").asInt());
            return makeJsonFromString(true, "User rated commodity successfully");
        } catch (ExceptionHandler e) {
            return makeJsonFromString(false, e.getMessage());
        }
    }

    public ObjectNode addToUserBuyList(ObjectNode node) throws JsonProcessingException, ExceptionHandler {
        try {
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

    public void removeFromUserBuyList(ObjectNode node) throws JsonProcessingException, ExceptionHandler {
        Commodity commodityFound = findByCommodityId(node.get("commodityId").asInt());
        User userFound = findByUsername(node.get("username").asText());
        userFound.removeFromBuyList(node.get("commodityId").asInt());
    }

    public ObjectNode getCommoditiesList() throws JsonProcessingException {
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

    public ObjectNode getCommodityById(int id) throws JsonProcessingException, ExceptionHandler {
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

    public ObjectNode getCommodityByCategory(String category) throws JsonProcessingException {
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

    public ObjectNode getBuyList(String username) throws JsonProcessingException, ExceptionHandler {
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

    public ObjectNode makeJsonFromObjectNode(Boolean success, ObjectNode dataNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("success", success);
        node.put("data", dataNode);
        return node;
    }

    public ObjectNode makeJsonFromString(Boolean success, String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("success", success);
        node.put("data", data);
        return node;
    }
}
