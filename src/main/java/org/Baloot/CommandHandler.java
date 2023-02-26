package org.Baloot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CommandHandler {
    public List<User> users = new ArrayList<>();
    public List<Provider> providers = new ArrayList<>();
    public List<Commodity> commodities = new ArrayList<>();
    
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
