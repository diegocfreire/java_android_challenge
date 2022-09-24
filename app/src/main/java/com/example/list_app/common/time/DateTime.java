package com.example.list_app.common.time;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime extends Date{

    @NonNull
    private final String pattern;

    public DateTime() {
        super();
        this.pattern = DateTimeFormat.ISO8601_DATETIME;
    }

    public DateTime(long date) {
        super(date);
        this.pattern = DateTimeFormat.ISO8601_DATETIME;
    }

    public DateTime(@NonNull Date datetime){
        super(datetime.getTime());
        this.pattern = DateTimeFormat.ISO8601_DATETIME;
    }

    public DateTime(@NonNull Date datetime, @NonNull @DateTimeFormat String pattern){
        super(datetime.getTime());
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(this);
    }

    public String toString(@DateTimeFormat @NonNull String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(this);
    }

    public static DateTime parse(
            @NonNull String dateString,
            @DateTimeFormat @NonNull String pattern
    ) throws ParseException {
        //Sempre criar uma nova instância. DateFormat possui problemas para acesso concorrente em singleton
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return new DateTime(formatter.parse(dateString), pattern);
    }

}
