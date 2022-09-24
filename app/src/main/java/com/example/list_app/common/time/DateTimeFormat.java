package com.example.list_app.common.time;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@StringDef({DateTimeFormat.SQLITE_DATETIME, DateTimeFormat.ISO8601_DATETIME, DateTimeFormat.DATETIME})
public @interface DateTimeFormat {

    String SQLITE_DATETIME = "yyyy-MM-dd HH:mm:ss";

    String ISO8601_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";

    String DATETIME = "dd-MM-yyyy HH:mm";
}
