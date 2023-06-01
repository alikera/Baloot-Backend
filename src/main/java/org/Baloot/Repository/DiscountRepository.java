package org.Baloot.Repository;

import org.Baloot.Entities.DiscountCode;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiscountRepository<T> extends Repository<T> {
    @Override
    public void createTable(Statement createTableStatement) throws SQLException {
        createTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS Discount(code CHAR(50), " +
                        "value DOUBLE, " +
                        "PRIMARY KEY (code))"
        );
    }
    public void createWeakTable(Statement createTableStatement) throws SQLException {
        createTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS UsedCode(" +
                        "uid CHAR(50), " +
                        "code CHAR(50), " +
                        "PRIMARY KEY (uid, code))"
        );
    }
    @Override
    public String insertStatement(HashMap<String, String> values) {
        return "INSERT INTO Discount(code,value) " +
                "VALUES('" + values.get("code") + "' ," + Double.parseDouble(values.get("value")) + ") " +
                "ON DUPLICATE KEY UPDATE code = code";
    }
    public String insertUsedCodeStatement(HashMap<String, String> values) {
        return "INSERT INTO UsedCode(uid,code) " +
                "VALUES('" + Integer.parseInt(values.get("uid")) + "' ,'" + Integer.parseInt(values.get("code")) + ")";
    }
    public String findDiscountCodeStatement(String username, String code) {
        return "SELECT * FROM UsedCode WHERE uid = ? AND code = ?";
    }
    @Override
    public String selectOneStatement() {
        return "SELECT * FROM discount WHERE code = ?";
    }

    @Override
    public List<String> getColNames() {
        List<String> colNames = new ArrayList<>();
        colNames.add("code");
        colNames.add("value");
        return colNames;
    }
}