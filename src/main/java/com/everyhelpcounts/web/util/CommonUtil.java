package com.everyhelpcounts.web.util;

import java.util.Date;

public class CommonUtil {

    public static Date convertFromSQLDateToJAVADate(java.sql.Date sqlDate){
        return (sqlDate!=null) ? new Date(sqlDate.getTime()) : null;
    }
}
