package org.Baloot.Repository;

import org.Baloot.Entities.Comment;
import org.Baloot.Entities.DiscountCode;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentRepository<T> extends Repository<T> {
    @Override
    public void createTable(Statement createTableStatement) throws SQLException {
        createTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS Comment(" +
                        " tid BIGINT," +
                        " userEmail CHAR(50)," +
                        " commodityId BIGINT," +
                        " text CHAR(255)," +
                        " date DATE," +
                        " PRIMARY KEY (tid)" +
                        ");"
        );
    }
    public void createWeakTable(Statement createTableStatement) throws SQLException {
        createTableStatement.addBatch(
                "CREATE TABLE IF NOT EXISTS Votes(" +
                        "userEmail CHAR(50), " +
                        "tid BIGINT, " +
                        "status INT," +
                        "PRIMARY KEY (userEmail, tid, status))"
        );
    }
    @Override
    public String insertStatement(HashMap<String, String> values) {
        return "INSERT INTO Comment(tid,userEmail,commodityId,text,date)"
                + " VALUES('" + values.get("tid") + "','" + values.get("userEmail") + "','" + values.get("commodityId") + "','" +
                                values.get("text") + "','" + java.sql.Date.valueOf(values.get("date")) + "')"
                + "ON DUPLICATE KEY UPDATE text = text";
    }
    public String insertVoteComment(HashMap<String, String> values) {
        return "INSERT INTO Vote(userEmail,tid,status) " +
                "VALUES('" + values.get("userEmail") + "' ,'" + values.get("tid") + values.get("status") + "')";
    }
    @Override
    public String selectOneStatement() {
        return "SELECT * FROM Comment WHERE commodityId = ?";
    }
    public String selectVoteStatement() {
        return "SELECT * FROM Vote WHERE userEmail = ? AND tid = ?";
    }
    @Override
    public List<String> getColNames() {
        List<String> colNames = new ArrayList<>();
        colNames.add("tid");
        colNames.add("userEmail");
        colNames.add("commodityId");
        colNames.add("text");
        colNames.add("date");
        return colNames;
    }
}