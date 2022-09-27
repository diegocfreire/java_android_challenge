package com.example.list_app.common.time;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        LocalDateFormat.SQLITE_DATE, LocalDateFormat.SQLITE_MONTH_YEAR,
        LocalDateFormat.DATE1, LocalDateFormat.DATE2, LocalDateFormat.ISO8601_DATETIME
})
public @interface LocalDateFormat {

    String DATE1 = "dd-MM-yyyy";

    String DATE2 = "dd/MM/yyyy";

    String SQLITE_DATE = "yyyy-MM-dd";

    String SQLITE_MONTH_YEAR = "yyyy-MM";

    String ISO8601_DATETIME = "yyyy-MM-dd'T'HH:mm:ss.SSS";

}
