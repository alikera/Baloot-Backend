package org.Baloot.Repository;

import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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

    public void createWeakTable(Statement createWeakTableStatement) throws SQLException {
        createWeakTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS Category(" +
                        "cid BIGINT, " +
                        "name CHAR(100), " +
                        "PRIMARY KEY (cid, name), " +
                        "FOREIGN KEY (cid) REFERENCES Commodity(cid))"
        );
    }

    @Override
    public String insertStatement(HashMap<String, String> values) {
        return "INSERT INTO Commodity(cid,name,pid,price,rating,in_stock,image)"
                + " VALUES('" + Integer.parseInt(values.get("cid")) + "','" + values.get("name") + "','"
                + Integer.parseInt(values.get("pid")) + "','" + values.get("price") + "','"
                + Double.parseDouble(values.get("rating")) + "','" + Integer.parseInt(values.get("in_stock"))
                +"','"+ values.get("image") + "')"
                + "ON DUPLICATE KEY UPDATE cid = cid";
    }

    public String insertWeak(HashMap<String, String> values) {
        // TODO: On Update Category?
        return "INSERT INTO Category(cid, name)"
                + " VALUES('" + values.get("cid") + "','" + values.get("name") + "')";
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