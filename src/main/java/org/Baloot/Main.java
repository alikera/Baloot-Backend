package org.Baloot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
//        Parser parser = new Parser();
        CommandHandler ch = new CommandHandler();
        while ((line = reader.readLine()) != null) {
            String[] seperatedLine = line.split(" ", 2);
//            System.out.println(seperatedLine[1]);
            ch.executeCommands(seperatedLine);
        }
    }
}

