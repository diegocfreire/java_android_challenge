package com.example.list_app.common.time;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({LocalTimeFormat.FULL_TIME, LocalTimeFormat.HOUR_MINUTE})
public @interface LocalTimeFormat {

    String FULL_TIME = "HH:mm:ss";

    String HOUR_MINUTE = "HH:mm";

}
