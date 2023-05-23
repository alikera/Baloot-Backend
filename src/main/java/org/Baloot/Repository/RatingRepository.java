//package org.Baloot.Repository;
//
//import org.Baloot.Entities.DiscountCode;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//public class RatingRepository<T> extends Repository<T> {
//    @Override
//    public void createTable(Statement createTableStatement) throws SQLException {
//        createTableStatement.addBatch(
//                "CREATE TABLE IF NOT EXISTS Rating(rid BIGINT," +
//                        " uid BIGINT," +
//                        " cid BIGINT)"
//        );
//    }
//
//    @Override
//    public String insertStatement(T entity) {
////        Rating rating = (Rating) entity;
////        return "INSERT INTO Rating(rid,uid,cid)"
////                + " VALUES('" + rating.getId() + "','" + rating.getUserId() + "','" + rating.getCommodityId() + "')"
////                + "ON DUPLICATE KEY UPDATE rid = rid";
//        return null;
//    }
//}