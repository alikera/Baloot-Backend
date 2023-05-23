package org.Baloot.Entities;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    public java.sql.Date getAsSqlDate(){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            java.util.Date date = sdf.parse(getAsString());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            return sqlDate;
        }catch (ParseException e){}
        return null;
    }

}
