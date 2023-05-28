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

    @Override
    public String insertStatement(HashMap<String, String> values) {
        return "INSERT INTO Discount(code,value) " +
                "VALUES('" + values.get("code") + "' ," + values.get("value") + ") " +
                "ON DUPLICATE KEY UPDATE code = code";
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