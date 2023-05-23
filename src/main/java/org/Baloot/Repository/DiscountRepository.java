package org.Baloot.Repository;

import org.Baloot.Entities.DiscountCode;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DiscountRepository<T> extends Repository<T> {
    @Override
    public void createTable(Statement createTableStatement) throws SQLException {
        createTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS Discount(did BIGINT," +
                        " code CHAR(50)," +
                        " value CHAR(10))"
        );
    }

    @Override
    public String insertStatement(T entity) {
        DiscountCode discountCode = (DiscountCode) entity;
        return "INSERT INTO Discount(did,code,value)"
                + " VALUES('" + discountCode.getId() + "','" + discountCode.getCode() + "','" + discountCode.getDiscount() + "')"
                + "ON DUPLICATE KEY UPDATE did = did";
    }
}