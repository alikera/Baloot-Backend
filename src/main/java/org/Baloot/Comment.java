package org.Baloot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.Baloot.Exception.InvalidRatingException;
import java.util.*;

public class Comment {

    private String userEmail;
    private int commodityId;
    private String text;
    private String date;

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
        return date;
    }

    public Comment(@JsonProperty ("userEmail") String _userEmail, @JsonProperty ("commodityId") int _commodityId,
                     @JsonProperty ("text") String _text, @JsonProperty ("date") String _date) {
        userEmail = _userEmail;
        commodityId = _commodityId;
        text = _text;
        date = _date;
    }

    public void print() {
        System.out.println(this.userEmail + " " + this.commodityId + " " + this.text + " " + this.date);
    }

    public ObjectNode toJson() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = mapper.createObjectNode();
        node.put("userEmail", this.userEmail);
        node.put("commodityId", this.commodityId);
        node.put("text", this.text);
        node.put("date", this.date);
        return node;
    }
}
