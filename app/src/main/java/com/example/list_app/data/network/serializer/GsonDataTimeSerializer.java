package com.example.list_app.data.network.serializer;

import com.example.list_app.common.time.DateTime;
import com.example.list_app.common.time.DateTimeFormat;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;

public class GsonDataTimeSerializer implements JsonSerializer<DateTime>, JsonDeserializer<DateTime>{

    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try{
            return DateTime.parse(json.toString(), DateTimeFormat.ISO8601_DATETIME);
        }catch (ParseException e){
            return null;
        }
    }

    @Override
    public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
        String time = src.toString( DateTimeFormat.ISO8601_DATETIME);
        return new JsonPrimitive(time);
    }
}
