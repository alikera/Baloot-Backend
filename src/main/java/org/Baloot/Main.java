package org.Baloot;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Baloot.Database.Database;
import org.Baloot.Exception.ExceptionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException, ExceptionHandler {
        Database db = new Database();

        DataGetter dataGetter = new DataGetter();
        dataGetter.getDataFromServer(db);

        Baloot baloot = new Baloot(db);

        RequestHandler rh = new RequestHandler(baloot);
        rh.getRequest();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        String line;
//        CommandHandler ch = new CommandHandler();
//        while ((line = reader.readLine()) != null) {
//            String[] seperatedLine = line.split(" ", 2);
//            ch.executeCommands(seperatedLine);
//        }
    }
}

