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
        User user = Baloot.getBaloot().getUserByUsername("amir");
        Baloot.getBaloot().userManager.addCredit("amir", "40000");
        user.addToBuyList(1);
        user.addToBuyList(2);
        Baloot.getBaloot().userManager.finalizePayment("amir", "", 0, null);
        user.addToBuyList(3);
        user.addToBuyList(4);
        user.addToBuyList(5);
        SpringApplication.run(BalootApplication.class, args);
    }
}
