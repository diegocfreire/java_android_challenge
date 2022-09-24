package com.example.list_app.data.network.serializer;

import com.example.list_app.common.time.LocalTime;
import com.example.list_app.common.time.LocalTimeFormat;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;

public class GsonLocalTimeSerializer implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime>{

    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try{
            return LocalTime.parse(json.toString(), LocalTimeFormat.FULL_TIME);
        }catch (ParseException e){
            return null;
        }
    }

    @Override
    public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
        String time = src.toString();
        return new JsonPrimitive(time);
    }
}
