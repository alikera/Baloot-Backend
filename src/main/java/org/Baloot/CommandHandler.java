//package org.Baloot;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import java.io.IOException;
//import org.Baloot.Exception.*;
//
//public class CommandHandler {
//    private Parser parser;
//    private Baloot baloot;
//    ObjectMapper mapper;
//
//    public CommandHandler() {
//        baloot = new Baloot();
//        parser = new Parser();
//        mapper = new ObjectMapper();
//    }
//
//    public void executeCommands(String[] command) throws IOException {
//        try {
//            switch (command[0]) {
//                case "addUser" -> {
//                    User user = parser.addUserParser(command[1]);
//                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.addUser(user)));
//                }
//                case "addProvider" -> {
//                    Provider provider = parser.addProviderParser(command[1]);
//                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.addProvider(provider)));
//                }
//                case "addCommodity" -> {
//                    Commodity commodity = parser.addCommodityParser(command[1]);
//                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.addCommodity(commodity)));
//                }
//                case "getCommoditiesList" -> System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.getCommoditiesList()));
//                case "rateCommodity" -> {
//                    ObjectNode node = parser.rateCommodityParser(command[1]);
//                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.rateCommodity(node)));
//                }
//                case "addToBuyList" -> {
//                    ObjectNode node = parser.modifyBuyListParser(command[1]);
//                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.addToUserBuyList(node)));
//                }
//                case "removeFromBuyList" -> {
//                    ObjectNode node = parser.modifyBuyListParser(command[1]);
//                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.removeFromUserBuyList(node)));
//                }
//                case "getCommodityById" -> {
//                    int id = parser.getCommodityByIdParser(command[1]);
//                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.getCommodityById(id)));
//                }
//                case "getCommoditiesByCategory" -> {
//                    String category = parser.getCommodityByCategoryParser(command[1]);
//                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.getCommodityByCategory(category)));
//                }
//                case "getBuyList" -> {
//                    String username = parser.getBuyListParser(command[1]);
//                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.getBuyList(username)));
//                }
//                default -> System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(baloot.makeJsonFromString(false, "Command not found")));
//            }
//        }
//        catch (ExceptionHandler e) {
//            System.out.println(baloot.makeJsonFromString(false, e.getMessage()));
//        }
//    }
//}
