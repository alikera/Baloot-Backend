package org.Baloot.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class DiscountCode {
    private static int count = 0;
    private int id;

    public int getId() {
        return id;
    }

    private String code;
    private double value;
    public DiscountCode(@JsonProperty("discountCode") String _code, @JsonProperty("discount") double _value) {
        code = _code;
        value = _value;
        count++;
        id = count;
    }

    public HashMap<String, String> getAttributes() {
        HashMap<String, String> attributes = new HashMap<>();

        attributes.put("id", String.valueOf(id));
        attributes.put("code", code);
        attributes.put("value", String.valueOf(value));

        return attributes;
    }
    public String getCode() {
        return code;
    }
    public double getValue() {
        return value;
    }
}
