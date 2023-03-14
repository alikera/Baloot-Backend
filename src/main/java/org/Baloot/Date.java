package org.Baloot;

import java.text.DecimalFormat;

public class Date {
    public int day;
    public int month;
    public int year;

    public Date(String registryDate) {
        String[] parts = registryDate.split("-");
        year = Integer.parseInt(parts[0]);
        month = Integer.parseInt(parts[1]);
        day = Integer.parseInt(parts[2]);

    }

    public String getAsString() {
        String _day = new DecimalFormat("00").format(day);
        String _month = new DecimalFormat("00").format(month);
        return Integer.toString(year) + '/' + _month + '/' + _day;
    }
}
