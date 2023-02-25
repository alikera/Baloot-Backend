package org.Baloot;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CommandHandler {
    public static Set<User> users = new HashSet<User>();
    public static Set<Provider> providers = new HashSet<Provider>();
    public static Set<Commodity> commodities = new HashSet<Commodity>();

    public static void executeCommands(String[] command) throws IOException {
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
}
