package org.Baloot.Repository;

import org.Baloot.Entities.User;
import org.Baloot.Exception.UserNotFoundException;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public abstract class Repository<T> {
    public abstract void createTable(Statement createTableStatement) throws SQLException;
    public abstract String insertStatement(T entity);
    public abstract String selectOneStatement();
    public abstract List<String> getColNames();
    public void insert(T entity) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(this.insertStatement(entity));
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

    public HashMap<String, String> selectOne(String uniqueCol) throws SQLException {
        String statement = this.selectOneStatement();
        List<String> colNames = this.getColNames();
        Connection con = ConnectionPool.getConnection();
        PreparedStatement prepStat = con.prepareStatement(statement);

        HashMap<String, String> values = new HashMap<>();
        try {
            prepStat.setString(1, uniqueCol);
            ResultSet result = prepStat.executeQuery();
            int index = 0;
            while (result.next()) {
                values.put(colNames.get(index), result.getString(colNames.get(index)));
            }
            result.close();
            prepStat.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            prepStat.close();
            con.close();
        }
        return values;
    }
}