package com.example.list_app.data.network.serializer;

import com.example.list_app.data.network.Status;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


public class ResourceStatusDeserializer implements JsonDeserializer<Status>{

    @Override
    public Status deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Status.parse(json.getAsString());
    }
}
