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

    private static User addUserParser;
    public static void main(String args[]) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] seperatedLine = line.split(" ");
            String command = seperatedLine[0];
            System.out.println(command);
            switch (command) {
                case "adduser":

                    break;
                case "addProvider":
                    break;
                case "addCommodity":
                    break;
                default:
                    //TODO Exception
            }
        }
    }
}
