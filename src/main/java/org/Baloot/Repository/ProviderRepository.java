package org.Baloot.Repository;

import org.Baloot.Entities.Provider;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public String insertStatement(HashMap<String, String> values) {
        return "INSERT INTO Provider(pid,name,date,image)"
                + " VALUES('" + Integer.parseInt(values.get("id")) + "','" + values.get("name") + "','"
                + java.sql.Date.valueOf(values.get("registryDate")) + "','" + values.get("image") + "')"
                + "ON DUPLICATE KEY UPDATE pid = pid";
    }

    @Override
    public String selectOneStatement(String field) {
        return "SELECT * FROM Provider WHERE " + field + " = ?";
    }
    public String selectCommodities() {
        return "SELECT * FROM Commodity WHERE pid = ?";
    }
    @Override
    public List<String> getColNames() {
        List<String> colNames = new ArrayList<>();
        colNames.add("pid");
        colNames.add("name");
        colNames.add("date");
        colNames.add("image");
        return colNames;
    }
}