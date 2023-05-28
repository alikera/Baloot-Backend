package org.Baloot.Repository;

import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;

import java.sql.*;
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
                + " VALUES('" + Integer.parseInt(values.get("id")) + "','" + values.get("name") + "','"
                + Integer.parseInt(values.get("providerId")) + "','" + values.get("price") + "','"
                + Double.parseDouble(values.get("rating")) + "','" + Integer.parseInt(values.get("inStock"))
                +"','"+ values.get("image") + "')"
                + "ON DUPLICATE KEY UPDATE cid = cid";
    }

    public String insertCategory(HashMap<String, String> values) {
        // TODO: On Update Category?
        return "INSERT INTO Category(cid, name)"
                + " VALUES('" + values.get("cid") + "','" + values.get("name") + "')"
                + "ON DUPLICATE KEY UPDATE name = name";
    }

    @Override
    public String selectOneStatement() {
        return "SELECT * FROM Commodity WHERE cid = ?";
    }


    public List<String> getCategories(int cid) throws SQLException {
        String statement = "SELECT name " +
                "FROM category " +
                "WHERE cid = " + cid;
        Connection con = ConnectionPool.getConnection();
        PreparedStatement prepStat = con.prepareStatement(statement);
        ResultSet result = prepStat.executeQuery();
        List<String> categories = new ArrayList<>();
        while (result.next()) {
            categories.add(result.getString("name"));
        }
        result.close();
        prepStat.close();
        con.close();
        return categories;
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