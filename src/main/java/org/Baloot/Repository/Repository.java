package org.Baloot.Repository;

import java.sql.*;

public abstract class Repository<T> {
    public abstract void createTable(Statement createTableStatement) throws SQLException;
    public abstract String insertStatement(T entity);
    public abstract String selectOneStatement();
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

    public ResultSet selectOne(String uniqueFiled) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement prepStat = con.prepareStatement(this.selectOneStatement());
        prepStat.setString(1, uniqueFiled);
        ResultSet result = prepStat.executeQuery();
        try {
            result.close();
            prepStat.close();
            con.close();
            return result;
        } catch (Exception e) {
            result.close();
            prepStat.close();
            con.close();
            System.out.println("error in Repository.Select query.");
            e.printStackTrace();
            return null;
        }
    }

}