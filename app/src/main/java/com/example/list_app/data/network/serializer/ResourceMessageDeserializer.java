package com.example.list_app.data.network.serializer;


import com.example.list_app.data.network.Message;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


public class ResourceMessageDeserializer implements JsonDeserializer<Message>{

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Message.make(json.getAsString());
    }
}
