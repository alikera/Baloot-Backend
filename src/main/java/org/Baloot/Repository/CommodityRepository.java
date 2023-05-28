package org.Baloot.Repository;

import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommodityRepository<T> extends Repository<T> {
    @Override
    public void createTable(Statement createTableStatement) throws SQLException {
        createTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS Commodity(" +
                        "cid BIGINT PRIMARY KEY, " +
                        "name CHAR(100), " +
                        "pid BIGINT, " +
                        "price DOUBLE, " +
                        "rating DOUBLE, " +
                        "in_stock INT, " +
                        "image TEXT," +
                        "FOREIGN KEY (pid) REFERENCES Provider(pid))"
        );
    }

    @Override
    public String insertStatement(T entity) {
        Commodity commodity = (Commodity) entity;
        return "INSERT INTO Commodity(cid,name,pid,price,rating,in_stock,image)"
                + " VALUES('" + commodity.getId() + "','" + commodity.getName() + "','" + commodity.getProviderId() + "','" + commodity.getPrice() + "','" +
                                commodity.getRating() + "','" + commodity.getInStock() +"','"+ commodity.getImage() + "')"
                + "ON DUPLICATE KEY UPDATE cid = cid";
    }

    @Override
    public String selectOneStatement() {
        return "";
    }

    @Override
    public List<String> getColNames() {
        List<String> colNames = new ArrayList<>();
        colNames.add("cid");
        colNames.add("name");
        colNames.add("pid");
        colNames.add("price");
        colNames.add("rating");
        colNames.add("in_stock");
        colNames.add("image");
        return colNames;
    }
}