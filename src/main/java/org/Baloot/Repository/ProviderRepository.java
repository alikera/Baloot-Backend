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
                "CREATE TABLE IF NOT EXISTS Provider(pid CHAR(50), name CHAR(100), date DATE, image CHAR(400)," +
                        "PRIMARY KEY (pid))"
        );
    }

    @Override
    public String insertStatement(T entity) {
        System.out.println("provder SQL");
        Provider provider = (Provider) entity;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            java.util.Date date = sdf.parse(provider.getDate().getAsString());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            return "INSERT INTO Provider(pid,name,date,image)"
                    + " VALUES('" + provider.getId() + "','" + provider.getName() + "','" + sqlDate + "','" + provider.getImage() + "')"
                    + "ON DUPLICATE KEY UPDATE pid = pid";

        }catch (ParseException e){

        }

        return null;
    }
}