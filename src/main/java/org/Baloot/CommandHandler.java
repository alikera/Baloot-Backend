package org.Baloot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.Baloot.Exception.*;
public class CommandHandler {
    private Parser parser;
    private Baloot baloot;
    public CommandHandler() {
        baloot = new Baloot();
        parser = new Parser();
    }

    public void executeCommands(String[] command) throws IOException, ExceptionHandler {
        try {
            switch (command[0]) {
                case "addUser" -> {
                    User user = parser.addUserParser(command[1]);
                    System.out.println(baloot.addUser(user));
                }
                case "addProvider" -> {
                    Provider provider = parser.addProviderParser(command[1]);
                    System.out.println(baloot.addProvider(provider));
                }
                case "addCommodity" -> {
                    Commodity commodity = parser.addCommodityParser(command[1]);
                    System.out.println(baloot.addCommodity(commodity));
                }
                case "getCommoditiesList" -> System.out.println(baloot.getCommoditiesList());
                case "rateCommodity" -> {
                    ObjectNode node = parser.rateCommodityParser(command[1]);
                    System.out.println(baloot.rateCommodity(node));
                }
                case "addToBuyList" -> {
                    ObjectNode node = parser.modifyBuyListParser(command[1]);
                    System.out.println(baloot.addToUserBuyList(node));
                }
                case "removeFromBuyList" -> {
                    ObjectNode node = parser.modifyBuyListParser(command[1]);
                    baloot.removeFromUserBuyList(node);
                }
                case "getCommodityById" -> {
                    int id = parser.getCommodityByIdParser(command[1]);
                    System.out.println(baloot.getCommodityById(id));
                }
                case "getCommoditiesByCategory" -> {
                    String category = parser.getCommodityByCategoryParser(command[1]);
                    System.out.println(baloot.getCommodityByCategory(category));
                }
                case "getBuyList" -> {
                    String username = parser.getBuyListParser(command[1]);
                    System.out.println(baloot.getBuyList(username));
                }
                default -> System.out.println(baloot.makeJsonFromString(false, "Command not found"));
            }
        }
        catch (ExceptionHandler e) {
            System.out.println(baloot.makeJsonFromString(false, e.getMessage()));
        }
    }
}
