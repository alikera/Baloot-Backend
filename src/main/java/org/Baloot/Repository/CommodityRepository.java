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

    public String selectCategories(){ return "SELECT name FROM category WHERE cid = ?"; }
    public String selectCidFromCategories() { return "SELECT * FROM category WHERE category = ?"; }
    public String selectCommodities(String tableName, String entity) {
        return "SELECT *\n" +
                "FROM (SELECT c.*, w.name AS name_2\n" +
                "FROM Commodity c\n" +
                "JOIN " +tableName+ " w ON c."+entity+ "= w."+entity+") AS joined_table\n" +
                "WHERE joined_table.name_2 LIKE ?";
    }
    public String selectCommoditiesByProvider(String entity) {
        return "SELECT *\n" +
                "FROM (SELECT c.*, p.name AS provider_name\n" +
                "FROM Commodity c\n" +
                "JOIN Provider p ON p.pid = c.pid) AS joined_table\n" +
                "WHERE joined_table." + entity + " LIKE ?";
    }
    public List<String> extractValues(List<HashMap<String, String>> hashMapList) {
        List<String> valuesList = new ArrayList<>();

        for (HashMap<String, String> hashMap : hashMapList) {
            valuesList.addAll(hashMap.values());
        }

        return valuesList;
    }

    public List<String> getCategories(int cid) throws SQLException {
        List<String> colNames = new ArrayList<>();
        colNames.add("name");
        String selectStatement = selectCategories();
        List<HashMap<String, String>> categoryRow = select(new ArrayList<Object>() {{ add(cid); }},
                colNames,
                selectStatement);

        return extractValues(categoryRow);
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