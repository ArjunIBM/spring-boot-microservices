package com.everyhelpcounts.web.util;

import java.util.Date;

public class CommonUtil {

    public static Date convertFromSQLDateToJAVADate(java.sql.Timestamp sqlTimestamp) {

        // Convert SQL Timestamp to Java Date
        return (sqlTimestamp!=null) ? new Date(sqlTimestamp.getTime()) : null;
    }
}
