package org.Baloot.Repository;

import org.Baloot.Entities.DiscountCode;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
    public String insertStatement(T entity) {
        DiscountCode discountCode = (DiscountCode) entity;
        return "INSERT INTO Discount(code,value) " +
                "VALUES('" + discountCode.getCode() + "' ," + discountCode.getDiscount() + ") " +
                "ON DUPLICATE KEY UPDATE code = code";
    }

    @Override
    public String selectOneStatement() {
        return "SELECT * FROM discount WHERE code = ?";
    }
}