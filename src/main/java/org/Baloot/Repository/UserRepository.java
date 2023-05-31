package org.Baloot.Repository;


import org.Baloot.Entities.User;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserRepository<T> extends Repository<T> {
    @Override
    public void createTable(Statement createTableStatement) throws SQLException {
        createTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS User(username CHAR(50) PRIMARY KEY," +
                        " password CHAR(50)," +
                        " email CHAR(50)," +
                        " birth_date DATE," +
                        " address TEXT," +
                        " credit DOUBLE)"
        );
    }

    public void createWeakTable(Statement createWeakTableStatement, String tableName) throws SQLException {
        String statement = String.format("CREATE TABLE IF NOT EXISTS %s (id INT AUTO_INCREMENT PRIMARY KEY, username CHAR(50), cid BIGINT," +
                "FOREIGN KEY (username) REFERENCES User(username)," +
                "FOREIGN KEY (cid) REFERENCES Commodity(cid)) ", tableName);
        createWeakTableStatement.addBatch(statement);
    }

    public void createPurchasedListTable(Statement createWeakTableStatement) throws SQLException {
        String statement = "CREATE TABLE IF NOT EXISTS PurchasedList (username CHAR(50), cid BIGINT, quantity INT, buyTime TIMESTAMP, " +
                "PRIMARY KEY (username, cid, quantity, buyTime), " +
                "FOREIGN KEY (username) REFERENCES User(username), " +
                "FOREIGN KEY (cid) REFERENCES Commodity(cid)) ";
        createWeakTableStatement.addBatch(statement);
    }

    @Override
    public String insertStatement(HashMap<String, String> values) {
        return "INSERT INTO User(username,password,email,birth_date,address,credit)"
                + " VALUES('" + values.get("username") + "','" + values.get("password") + "','" + values.get("email") +
                "','" + java.sql.Date.valueOf(values.get("birthDate")) + "','" + values.get("address") + "','" + Double.parseDouble(values.get("credit")) + "')"
                + "ON DUPLICATE KEY UPDATE username = username";
    }

    public String insertBuyListStatement(HashMap<String, String> values) {
        return "INSERT INTO BuyList(username,cid)"
                + " VALUES('" + values.get("username") + "','" + Integer.parseInt(values.get("cid")) + "')";
    }

    public String removeFromBuyListStatement(HashMap<String, String> values) {
        return "DELETE FROM BuyList " +
                "WHERE username = '" + values.get("username") + "' AND cid = " + Integer.parseInt(values.get("cid"));
    }

    public String insertPurchasedListStatement(HashMap<String, String> values) {
        return "INSERT INTO PurchasedList(username, cid, quantity, buyTime)"
                + " VALUES('" + values.get("username") + "','"
                + Integer.parseInt(values.get("cid")) + "','"
                + Integer.parseInt(values.get("quantity")) + "','"
                + Timestamp.valueOf(values.get("buyTime")) + "')";
    }

    @Override
    public String selectOneStatement() {
        return "SELECT * FROM User WHERE username = ?";
    }

    public String selectPurchasedListStatement() {
        return null;
    }

    public String selectListStatement(String tableName) {
        return "SELECT c.*, b.quantity\n" +
                "FROM Commodity c\n" +
                "JOIN (\n" +
                "  SELECT cid, COUNT(*) AS quantity\n" +
                "  FROM " + tableName + "\n" +
                "  WHERE username = ?\n" +
                "  GROUP BY cid\n" +
                ") b ON c.cid = b.cid\n";
    }

    public String updateCreditStatement() {
        return "UPDATE User " +
                "SET credit = credit + ? " +
                "WHERE username = ?";
    }

    @Override
    public List<String> getColNames() {
        List<String> colNames = new ArrayList<>();
        colNames.add("username");
        colNames.add("password");
        colNames.add("email");
        colNames.add("birth_date");
        colNames.add("address");
        colNames.add("credit");
        return colNames;
    }
}