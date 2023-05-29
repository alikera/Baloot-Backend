package org.Baloot.Repository;

import org.Baloot.Entities.User;
import org.Baloot.Exception.UserNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Repository<T> {
    public abstract void createTable(Statement createTableStatement) throws SQLException;

    public abstract String insertStatement(HashMap<String, String> values);
    public abstract String selectOneStatement();
    public abstract List<String> getColNames();

    public void insert(String insertStatement) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(insertStatement);
        try {
            st.execute();
            st.close();
            con.close();
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.insert query.");
            e.printStackTrace();
        }
    }

    public List<HashMap<String, String>> select(List<Object> mustFill, List<String> colNames, String statement) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement prepStat = con.prepareStatement(statement);
        List<HashMap<String, String>> selectedRows = new ArrayList<>();
        try {
            for (int i = 0; i < mustFill.size(); i++) {
                if (mustFill.get(i) instanceof String) {
                    prepStat.setString(i + 1, (String) mustFill.get(i));
                } else if (mustFill.get(i) instanceof Integer) {
                    prepStat.setInt(i + 1, (Integer) mustFill.get(i));
                } else {
                    throw new IllegalArgumentException("Unsupported data type for uniqueCol");
                }
            }
            ResultSet result = prepStat.executeQuery();
            while (result.next()) {
                HashMap<String, String> values = new HashMap<>();
                for (String colName : colNames) {
                    values.put(colName, result.getString(colName));
                }
                selectedRows.add(values);
            }
            result.close();
            prepStat.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            prepStat.close();
            con.close();
        }
        return selectedRows;
    }

}