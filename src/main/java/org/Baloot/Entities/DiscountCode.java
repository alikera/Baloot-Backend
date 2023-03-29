package org.Baloot.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountCode {
    private String code;
    private double discount;
    public DiscountCode(@JsonProperty("discountCode") String _code, @JsonProperty("discount") double _discount) {
        code = _code;
        discount = _discount;
    }
    public String getCode() {
        return code;
    }
    public double getDiscount() {
        return discount;
    }
}
