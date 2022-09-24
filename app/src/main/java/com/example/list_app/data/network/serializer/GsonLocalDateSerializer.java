package com.example.list_app.data.network.serializer;

import com.example.list_app.common.time.LocalDate;
import com.example.list_app.common.time.LocalDateFormat;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;


public class GsonLocalDateSerializer implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate>{

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return LocalDate.parse(json.getAsString(), LocalDateFormat.DATE2);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        String dateIso = src.toString(LocalDateFormat.ISO8601_DATETIME);
        return new JsonPrimitive(dateIso);
    }
}
