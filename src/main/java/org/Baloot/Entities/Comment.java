package org.Baloot.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;

public class Comment {
    HashMap<String, Integer> votes;
    private static int count = 0;
    private int id;
    private String userEmail;
    private String username;
    private int commodityId;
    private String text;
    private Date date;
    public Comment(@JsonProperty("userEmail") String _userEmail, @JsonProperty("commodityId") int _commodityId,
                   @JsonProperty("text") String _text, @JsonProperty("date") String _date) {
        userEmail = _userEmail;
        commodityId = _commodityId;
        text = _text;
        date = new Date(_date);
        votes = new HashMap<>();
        count++;
        id = count;
    }
    public int getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date.getAsString();
    }

    public java.sql.Date getAsSqlDate() {
        return date.getAsSqlDate();
    }
    public HashMap<String, String> getAttributes() {
        HashMap<String, String> attributes = new HashMap<>();

        attributes.put("id", String.valueOf(id));
        attributes.put("userEmail", userEmail);
        attributes.put("commodityId", String.valueOf(commodityId));
        attributes.put("text", text);
        attributes.put("date", date.getAsSqlDate().toString());

        return attributes;
    }


    public int voteComment(String username, int vote) {
        if (votes.containsKey(username)) {
            if (votes.get(username) == vote) {
                return 0;
            } else {
                votes.put(username, vote);
                return -1;
            }
        } else {
            votes.put(username, vote);
            return 1;
        }

    }

    public int getLikes() {
        int likesCounter = 0;
        for (int vote : votes.values()) {
            if (vote == 1) {
                likesCounter++;
            }
        }
        return likesCounter;
    }

    public int getDislikes() {
        int dislikesCounter = 0;
        for (int vote : votes.values()) {
            if (vote == -1) {
                dislikesCounter++;
            }
        }
        return dislikesCounter;
    }

    public void print() {
        System.out.println(this.userEmail + " " + this.commodityId + " " + this.text + " " + this.date);
    }
}
