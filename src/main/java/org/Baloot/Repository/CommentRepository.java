package org.Baloot.Repository;

import org.Baloot.Entities.Comment;
import org.Baloot.Entities.DiscountCode;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CommentRepository<T> extends Repository<T> {
    @Override
    public void createTable(Statement createTableStatement) throws SQLException {
        createTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS Comment(tid BIGINT," +
                        " uid CHAR(50)," +
                        " cid BIGINT," +
                        " text TEXT," +
                        " date DATE)"
        );
    }

    @Override
    public String insertStatement(T entity) {
        Comment comment = (Comment) entity;
        return "INSERT INTO Comment(tid,uid,cid,text,date)"
                + " VALUES('" + comment.getId() + "','" + comment.getUserEmail() + "','" + comment.getCommodityId() + "','" +
                                comment.getText() + "','" + comment.getAsSqlDate() + "')"
                + "ON DUPLICATE KEY UPDATE tid = tid";
    }
    @Override
    public String selectOneStatement() {
        return "";
    }

    @Override
    public List<String> getColNames() {
        List<String> colNames = new ArrayList<>();
        colNames.add("tid");
        colNames.add("uid");
        colNames.add("cid");
        colNames.add("text");
        colNames.add("date");
        return colNames;
    }
}