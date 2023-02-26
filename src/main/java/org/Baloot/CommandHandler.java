package org.Baloot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.*;

public class CommandHandler {
    public List<User> users = new ArrayList<>();
    public List<Provider> providers = new ArrayList<>();
    public  List<Commodity> commodities = new ArrayList<>();

    private Commodity findByCommodityId(int commodityId){
        for (Commodity commodity : commodities) {
            if (commodity.getId() == commodityId) {
                return commodity;
            }
        }
        return null;
    }
    private User findByUsername(String username){
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        return null;
    }
    public void executeCommands(String[] command) throws IOException {
        Parser parser = new Parser();
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
                        commodityFound.rateMovie(node.get("username").asText(), node.get("score").asInt());
                    } else {
                        System.out.println("Commodity Not Found!");
                    }
                }
                catch (RuntimeException e){
                    System.out.println("Invalid Rating");
                }
                break;
            case "addToBuyList":
                break;
            case "removeFromBuyList":
                break;
            case "getCommodityById":
                break;
            case "getCommodityByCategory":
                break;
            case "getBuyList":
                break;
            default:
                //TODO Exception
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


}
