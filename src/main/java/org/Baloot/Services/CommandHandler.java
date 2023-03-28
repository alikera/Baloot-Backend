package org.Baloot.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;

public class CommandHandler {
    private Parser parser;
    private Baloot baloot;
    ObjectMapper mapper;

    public CommandHandler(Baloot _baloot) {
        baloot = _baloot;
        parser = new Parser();
        mapper = new ObjectMapper();
    }

    public void executeCommands(String[] command) throws IOException {
            try {
                switch (command[0]) {
                    case "addUser" -> addUserCommand(command[1]);
                    case "addProvider" -> addProviderCommand(command[1]);
                    case "addCommodity" -> addCommodityCommand(command[1]);
                    case "getCommoditiesList" -> getCommoditiesListCommand(command[0]);
                    case "rateCommodity" -> rateCommodityCommand(command[1]);
                    case "addToBuyList" -> addToUserBuyListCommand(command[1]);
                    case "removeFromBuyList" -> removeFromBuyListCommand(command[1]);
                    case "getCommodityById" -> getCommodityCommand(command[1]);
                    case "getCommoditiesByCategory" -> getCommoditiesByCategoryCommand(command[1]);
                    case "getBuyList" -> getBuyListCommand(command[1]);
//                default -> System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.makeJsonFromString(false, "Command not found")));
                }
            } catch (UserNotFoundException | CommodityNotFoundException | ProviderNotFoundException | InvalidRatingException | InvalidUsernameException |
                     OutOfStockException | CommodityExistenceException e) {
                printResponseMessage(false, e.getMessage());
            }
    }
    public void addUserCommand(String command) throws JsonProcessingException, InvalidUsernameException {
        User user = parser.addUserParser(command);
        baloot.userManager.addUser(user);
        printResponseMessage(true, "User added/modified successfully.");
    }
    public void addProviderCommand(String command) throws JsonProcessingException {
        Provider provider = parser.addProviderParser(command);
        baloot.addProvider(provider);
        printResponseMessage(true, "Provider added successfully.");
    }

    public void addCommodityCommand(String command) throws JsonProcessingException, ProviderNotFoundException {
        Commodity commodity = parser.addCommodityParser(command);
        baloot.commodityManager.addCommodity(commodity);
        printResponseMessage(true, "Commodity added successfully.");
    }

    public void getCommoditiesListCommand(String command) throws JsonProcessingException {
        ObjectNode commoditiesList = getCommoditiesList(baloot.getCommodities());
        printOutput(makeJsonFromObjectNode(true, commoditiesList));
    }
    public ObjectNode getCommoditiesList(List<Commodity> commodities) {
        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> commodityNodes = new ArrayList<>();
        for(Commodity commodity : commodities) {
            commodityNodes.add(commodity.toJson());
        }
        ObjectNode mainNode = mapper.createObjectNode();
        mainNode.putPOJO("commoditiesList", commodityNodes);

        return mainNode;
    }
    public void rateCommodityCommand(String command) throws JsonProcessingException, InvalidRatingException, UserNotFoundException, CommodityNotFoundException {
        ObjectNode node = parser.rateCommodityParser(command);
        baloot.commodityManager.rateCommodity(node.get("username").asText(), node.get("commodityId").asText(), node.get("score").asText());
        printResponseMessage(true, "User rated commodity successfully.");
    }

    public void addToUserBuyListCommand(String command) throws JsonProcessingException, UserNotFoundException, OutOfStockException, CommodityExistenceException, CommodityNotFoundException {
        ObjectNode node = parser.modifyBuyListParser(command);
        baloot.userManager.addCommodityToUserBuyList(node.get("username").asText(), node.get("commodityId").asText());
        printResponseMessage(true, "Commodity added to user buy list successfully.");
    }

    public void removeFromBuyListCommand(String command) throws JsonProcessingException, UserNotFoundException, CommodityExistenceException, CommodityNotFoundException {
        ObjectNode node = parser.modifyBuyListParser(command);
        baloot.userManager.removeCommodityFromUserBuyList(node.get("username").asText(), node.get("commodityId").asText());
        printResponseMessage(true, "Commodity removed from user buy list successfully");
    }

    public void getCommodityCommand(String command) throws JsonProcessingException, CommodityNotFoundException {
        int id = parser.getCommodityByIdParser(command);
        Commodity commodity = baloot.getCommodityById(id);
        printOutput(commodity);
    }
    public void getCommoditiesByCategoryCommand(String command) throws JsonProcessingException {
        String category = parser.getCommodityByCategoryParser(command);
        List<Commodity> commodities = baloot.commodityManager.getCommoditiesByCategory(category);
        ObjectNode commoditiesList = getCommoditiesList(commodities);
        printOutput(makeJsonFromObjectNode(true, commoditiesList));
    }
    public void getBuyListCommand(String command) throws JsonProcessingException, UserNotFoundException, CommodityNotFoundException {
        String username = parser.getBuyListParser(command);
        List<Commodity> commodities = baloot.userManager.getUserBuylist(username);
        ObjectNode commoditiesList = getCommoditiesList(commodities);
        printOutput(makeJsonFromObjectNode(true, commoditiesList));
    }
    public ObjectNode makeJsonFromString(Boolean success, String data) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("success", success);
        node.put("data", data);
        return node;
    }
    public ObjectNode makeJsonFromObjectNode(Boolean success, ObjectNode dataNode) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("success", success);
        node.put("data", dataNode);
        return node;
    }
    public void printResponseMessage(Boolean success, String message) throws JsonProcessingException {
        String output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(makeJsonFromString(success, message));
        System.out.println(output);
    }
    public void printOutput(Object outputObject) throws JsonProcessingException {
        String output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(outputObject);
        System.out.println(output);
    }
    public static void main(String[] args) throws IOException, ExceptionHandler {
        Database db = new Database();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        Baloot baloot = new Baloot(db);
        CommandHandler ch = new CommandHandler(baloot);
        while ((line = reader.readLine()) != null) {
            String[] seperatedLine = line.split(" ", 2);
            ch.executeCommands(seperatedLine);
        }
    }
}
