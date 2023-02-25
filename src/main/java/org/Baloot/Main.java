package org.Baloot;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.Baloot.CommandHandler.executeCommands;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        Parser parser = new Parser();
        while ((line = reader.readLine()) != null) {
            String[] seperatedLine = line.split(" ", 2);
            System.out.println(seperatedLine[1]);
            executeCommands(seperatedLine);
        }
    }
}

