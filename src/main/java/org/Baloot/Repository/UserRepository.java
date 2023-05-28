package org.Baloot.Repository;


import org.Baloot.Entities.User;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserRepository<T> extends Repository<T> {
    @Override
    public void createTable(Statement createTableStatement) throws SQLException {
        createTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS User(uid BIGINT PRIMARY KEY," +
                        " username CHAR(50)," +
                        " password CHAR(50)," +
                        " email CHAR(50)," +
                        " birth_date DATE," +
                        " address TEXT," +
                        " credit DOUBLE)"
        );
    }

    @Override
    public String insertStatement(HashMap<String, String> values) {
        return "INSERT INTO User(uid,username,password,email,birth_date,address,credit)"
                + " VALUES('" + values.get("uid") + "','" + values.get("username") + "','" + values.get("password") + "','" + values.get("email") +
                        "','" + java.sql.Date.valueOf(values.get("birth_date")) + "','" + values.get("address") + "','" + values.get("credit") + "')"
                + "ON DUPLICATE KEY UPDATE uid = uid";
    }

    @Override
    public String selectOneStatement() {
        return "SELECT * FROM User WHERE username = ?";
    }

    public void createWeakTable(Statement createWeakTableStatement, String tableName) throws SQLException {
        String statement = String.format("CREATE TABLE IF NOT EXISTS %s(id BIGINT PRIMARY KEY, uid BIGINT, cid BIGINT, quantity INT," +
                "FOREIGN KEY (uid) REFERENCES User(uid)," +
                "FOREIGN KEY (cid) REFERENCES Commodity(cid))", tableName);
        createWeakTableStatement.addBatch(statement);
    }

    public String increaseCreditStatement() {
        return "UPDATE User " +
                "SET credit = credit + ? " +
                "WHERE username = ?";
    }

    @Override
    public List<String> getColNames() {
        List<String> colNames = new ArrayList<>();
        colNames.add("uid");
        colNames.add("username");
        colNames.add("password");
        colNames.add("email");
        colNames.add("birth_date");
        colNames.add("address");
        colNames.add("credit");
        return colNames;
    }
}