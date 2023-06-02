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
                "CREATE TABLE IF NOT EXISTS Vote(" +
                        "userEmail CHAR(50), " +
                        "tid BIGINT, " +
                        "status INT," +
                        "PRIMARY KEY (userEmail, tid))"
        );
    }
    @Override
    public String insertStatement(HashMap<String, String> values) {
        return "INSERT INTO Comment(tid,userEmail,commodityId,text,date)"
                + " VALUES('" + values.get("tid") + "','" + values.get("userEmail") + "','" + values.get("commodityId") + "','" +
                                values.get("text") + "','" + java.sql.Date.valueOf(values.get("date")) + "')"
                + "ON DUPLICATE KEY UPDATE tid = tid";
    }
    public String selectMaxStatement(){ return "SELECT MAX(tid) AS max_tid FROM comment"; }
    public String insertVoteComment(HashMap<String, String> values) {
        return "INSERT INTO Vote(userEmail,tid,status) " +
                "VALUES('" + values.get("userEmail") + "' ," + Integer.parseInt(values.get("tid")) + "," + Integer.parseInt(values.get("status")) + ")"
                + "ON DUPLICATE KEY UPDATE status = " + Integer.parseInt(values.get("status"));
    }
    @Override
    public String selectOneStatement(String field) {
        return "SELECT * FROM Comment WHERE " + field + " = ?";
    }
    public String selectVoteStatement() {
        return "SELECT * FROM Vote WHERE tid = ?";
    }
    public String selectPrevVoteStatement() {
        return "SELECT * FROM Vote WHERE tid = ? AND userEmail = ?";
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