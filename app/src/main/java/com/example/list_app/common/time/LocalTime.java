package com.example.list_app.common.time;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalTime extends Date {

    @NonNull
    private String pattern;

    public LocalTime() {
        super();
        this.pattern = LocalTimeFormat.FULL_TIME;
    }

    public LocalTime(long time) {
        super(time);
        this.pattern = LocalTimeFormat.FULL_TIME;
    }

    public LocalTime(@NonNull Date time) {
        this(time.getTime());
        this.pattern = LocalTimeFormat.FULL_TIME;
    }

    public LocalTime(@NonNull Date time, @NonNull @LocalTimeFormat String pattern) {
        this(time.getTime());
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(this);
    }

    public String toString(@NonNull @LocalTimeFormat String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(this);
    }

    public static LocalTime parse(
            @NonNull String dateString, @NonNull @LocalTimeFormat String pattern
    ) throws ParseException {
        //Sempre criar uma nova instância. DateFormat possui problemas para acesso concorrente em singleton
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return new LocalTime(formatter.parse(dateString), pattern);
    }

}
