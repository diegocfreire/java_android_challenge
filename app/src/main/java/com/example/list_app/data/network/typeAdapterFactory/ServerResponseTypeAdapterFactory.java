package com.example.list_app.data.network.typeAdapterFactory;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ServerResponseTypeAdapterFactory implements TypeAdapterFactory{

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonElement jsonElement = elementAdapter.read(in);

                if(jsonElement.isJsonObject()){
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    if(jsonObject.has("result") && jsonObject.get("result").isJsonArray()){
                        JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                        jsonElement = jsonArray.get(0);

                        jsonElement = new JsonParser().parse(jsonElement.getAsString()).getAsJsonObject();
                    }
                }

                return delegate.fromJsonTree(jsonElement);
            }
        };
    }
}
