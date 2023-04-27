package org.Baloot.Application;

import org.Baloot.Database.DataGetter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "Controller")
public class BalootApplication {
    public static void main(String[] args) {
        DataGetter dataGetter = new DataGetter();
        SpringApplication.run(BalootApplication.class, args);
    }
}
