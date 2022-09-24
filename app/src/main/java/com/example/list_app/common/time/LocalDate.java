package com.example.list_app.common.time;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalDate extends Date {

    @NonNull
    private String pattern;

    public LocalDate(){
        super();
        this.pattern = LocalDateFormat.SQLITE_DATE;
    }

    public LocalDate(long date) {
        super(date);
        this.pattern = LocalDateFormat.SQLITE_DATE;
    }

    public LocalDate(@NonNull Date date) {
        this(date.getTime());
        this.pattern = LocalDateFormat.SQLITE_DATE;
    }

    public LocalDate(@NonNull Date date, @LocalDateFormat @NonNull String pattern) {
        this(date.getTime());
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(this);
    }

    public String toString(@LocalDateFormat @NonNull String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return formatter.format(this);
    }

    public static LocalDate parse(
            @NonNull String dateString,
            @LocalDateFormat @NonNull String pattern
    ) throws ParseException{
        //Sempre criar uma nova instância. DateFormat possui problemas para acesso concorrente em singleton
        DateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        return new LocalDate(formatter.parse(dateString), pattern);
    }

}
