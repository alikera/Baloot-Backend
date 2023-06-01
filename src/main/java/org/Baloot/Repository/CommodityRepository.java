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
                        "inStock INT, " +
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
    public void createRatingTable(Statement createWeakTableStatement) throws SQLException {
        createWeakTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS Rating(" +
                        "cid BIGINT, " +
                        "username CHAR(100), " +
                        "rate INT, " +
                        "PRIMARY KEY (cid, username), " +
                        "FOREIGN KEY (cid) REFERENCES Commodity(cid))"
        );
    }
    @Override
    public String insertStatement(HashMap<String, String> values) {
        return "INSERT INTO Commodity(cid,name,pid,price,rating,inStock,image)"
                + " VALUES('" + Integer.parseInt(values.get("cid")) + "','" + values.get("name") + "','"
                + Integer.parseInt(values.get("pid")) + "','" + values.get("price") + "','"
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
    public String insertRatingStatement(HashMap<String, String> values) {
        return "INSERT INTO Rating(cid, username, rate)"
                + " VALUES(" + Integer.parseInt(values.get("cid")) + ", '"
                + values.get("username") + "', "
                + Integer.parseInt(values.get("rate")) + ")"
                + "ON DUPLICATE KEY UPDATE rate = " + Double.parseDouble(values.get("rate"));
    }
    public String updateRatingStatement() {
        return "UPDATE Commodity " +
                "SET  rating = ? " +
                "WHERE cid = ?";
    }
    public String modifyInStockStatement() {
        return "UPDATE Commodity " +
                "SET  inStock = inStock + ? " +
                "WHERE cid = ?";
    }

    public String getSuggestedCommoditiesStatement() {
        return """
                SELECT DISTINCT c2.cid, c2.name, c2.price, c2.rating, c2.in_stock, c2.image,
                       IF(c1.name = c3.name, 11, 0) + c2.rating AS score
                FROM Commodity c1
                         JOIN Category cat ON c1.cid = cat.cid
                         JOIN Category cat2 ON cat.name = cat2.name
                         JOIN Commodity c2 ON cat2.cid = c2.cid
                         LEFT JOIN Category cat3 ON c2.cid = cat3.cid
                         LEFT JOIN Commodity c3 ON cat3.cid = c1.cid
                WHERE c1.cid = ? AND c2.cid != ?
                ORDER BY score DESC
                LIMIT 4;
                """;
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
    public String selectRatingStatement() { return "SELECT rate FROM rating WHERE cid = ?"; }
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
        colNames.add("inStock");
        colNames.add("image");
        return colNames;
    }
}