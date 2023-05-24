package org.Baloot.Repository;

import org.Baloot.Entities.Provider;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ProviderRepository<T> extends Repository<T> {
    @Override
    public void createTable(Statement createTableStatement) throws SQLException {
        createTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS Provider(pid BIGINT PRIMARY KEY," +
                        " name CHAR(100)," +
                        " date DATE," +
                        " image TEXT)"
        );
    }

    @Override
    public String insertStatement(T entity) {
        Provider provider = (Provider) entity;
        return "INSERT INTO Provider(pid,name,date,image)"
                + " VALUES('" + provider.getId() + "','" + provider.getName() + "','" + provider.getDate().getAsSqlDate() + "','" + provider.getImage() + "')"
                + "ON DUPLICATE KEY UPDATE pid = pid";
    }

    @Override
    public String selectOneStatement() {
        return "";
    }
}