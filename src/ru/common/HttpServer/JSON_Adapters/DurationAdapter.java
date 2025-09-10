package ru.common.HttpServer.JSON_Adapters;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.Duration;

public class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {

    @Override
    public JsonElement serialize(Duration value, Type typeOfValue, JsonSerializationContext context) {
        return new JsonPrimitive(value.toMinutes());
    }

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return Duration.ofMinutes(json.getAsLong());
    }
}
