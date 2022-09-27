package com.example.list_app.data.network.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GsonDateDeserializer implements JsonDeserializer<Date> {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm";

    private static final String TIME_FORMAT = "HH:mm";

    public GsonDateDeserializer() {}

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context){
        try{
            return parseDate(json.getAsString());
        }catch (ParseException ex){
            return null;
        }
    }

    private Date parseDate(String dateString) throws ParseException{
        if((dateString != null) && (!dateString.isEmpty())){
            try{
                return tryParseDatetime(dateString);
            }catch (ParseException ex){
                try{
                    return tryParseDate(dateString);
                }catch (ParseException pe){
                    return tryParseTime(dateString);
                }
            }
        }else{
            return null;
        }
    }

    private Date tryParseDatetime(String datetimeString) throws ParseException{
        DateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault());
        return formatter.parse(datetimeString);
    }

    private Date tryParseDate(String dateString) throws ParseException{
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return formatter.parse(dateString);
    }

    private Date tryParseTime(String timeString) throws ParseException{
        DateFormat formatter = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        return formatter.parse(timeString);
    }
}
