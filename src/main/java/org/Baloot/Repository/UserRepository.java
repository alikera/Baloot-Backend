package org.Baloot.Repository;


import org.Baloot.Entities.User;

import java.sql.SQLException;
import java.sql.Statement;

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
    public String insertStatement(T entity) {
        System.out.println("ensfjhroihgqipuhgrliRUH");

        User user = (User) entity;
        return "INSERT INTO User(uid,username,password,email,birth_date,address,credit)"
                + " VALUES('" + user.getId() + "','" + user.getUsername() + "','" + user.getPassword() + "','" + user.getEmail() +
                        "','" + user.getDate().getAsSqlDate() + "','" + user.getAddress() + "','" + user.getCredit()+ "')"
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
}