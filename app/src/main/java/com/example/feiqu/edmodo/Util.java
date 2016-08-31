package com.example.feiqu.edmodo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by feiqu on 8/30/16.
 */
public class Util {
    public static String convertDate(String dateStr) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            String newDateStr = new SimpleDateFormat("MMM dd, yyyy").format(date);
            return newDateStr;
        } catch (ParseException e) {
            return dateStr;
        }
    }
}
