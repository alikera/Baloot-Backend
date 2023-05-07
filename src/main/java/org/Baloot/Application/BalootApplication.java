package org.Baloot.Application;

import org.Baloot.Baloot;
import org.Baloot.Database.DataGetter;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "Controller")
public class BalootApplication {
    public static void main(String[] args) throws UserNotFoundException, NegativeAmountException, CommodityExistenceException, CommodityNotFoundException, NotEnoughCreditException {
        DataGetter dataGetter = new DataGetter();
        SpringApplication.run(BalootApplication.class, args);
    }
}
