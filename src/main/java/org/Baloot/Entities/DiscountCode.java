package org.Baloot.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountCode {
    private static int count = 0;
    private int id;

    public int getId() {
        return id;
    }

    private String code;
    private double discount;
    public DiscountCode(@JsonProperty("discountCode") String _code, @JsonProperty("discount") double _discount) {
        code = _code;
        discount = _discount;
        count++;
        id = count;
    }
    public String getCode() {
        return code;
    }
    public double getDiscount() {
        return discount;
    }
}
