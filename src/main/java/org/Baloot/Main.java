package org.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Exception.ExceptionHandler;
import org.Baloot.Services.DataGetter;
import org.Baloot.Services.RequestHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ExceptionHandler {
        Database db = new Database();

        DataGetter dataGetter = new DataGetter();
        dataGetter.getDataFromServer(db);

        Baloot baloot = new Baloot(db);
        RequestHandler rh = new RequestHandler(baloot);
        rh.getRequest();

    }
}

