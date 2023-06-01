package org.Baloot.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;

public class Comment {
    private int likes;
    private int dislikes;
    private static int count = 0;
    private int tid;
    private String userEmail;
    private int commodityId;
    private String text;
    private Date date;

    public Comment(){

    }
    public Comment(@JsonProperty("userEmail") String _userEmail, @JsonProperty("commodityId") int _commodityId,
                   @JsonProperty("text") String _text, @JsonProperty("date") String _date) {
        userEmail = _userEmail;
        commodityId = _commodityId;
        text = _text;
        date = new Date(_date);
        count++;
        likes = 0;
        dislikes = 0;
        tid = count;
        System.out.println("ASASASASASSAS");
        System.out.println(count);
        System.out.println(text);
    }
    public int getId() {
        return tid;
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

    public int getDislikes() {
        return dislikes;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikesAndDislikes(List<HashMap<String, String>> _votes){
        likes = 0;
        dislikes = 0;
        for(HashMap<String, String> hashMap: _votes) {
            if(Integer.parseInt(hashMap.get("status")) == -1){
                dislikes += 1;
            } else if (Integer.parseInt(hashMap.get("status")) == 1) {
                likes += 1;
            }
        }
    }

    public HashMap<String, String> getAttributes() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("tid", String.valueOf(tid));
        attributes.put("userEmail", userEmail);
        attributes.put("commodityId", String.valueOf(commodityId));
        attributes.put("text", text);
        attributes.put("date", date.getAsSqlDate().toString());

        return attributes;
    }

}
