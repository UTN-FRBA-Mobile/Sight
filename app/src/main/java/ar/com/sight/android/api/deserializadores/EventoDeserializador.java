package ar.com.sight.android.api.deserializadores;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ar.com.sight.android.api.modelos.Evento;

public class EventoDeserializador implements JsonDeserializer<Evento> {

    @Override
    public Evento deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        Evento evento = new Evento();

        if (jsonObject.get("id") == null || jsonObject.get("id").isJsonNull()) {
            evento.setId(-1);
        } else {
            evento.setId(jsonObject.get("id").getAsInt());
        }

        if (jsonObject.get("timestamp") != null && !jsonObject.get("timestamp").isJsonNull()) {
            evento.setTimestamp(jsonObject.get("timestamp").getAsString());
        }

        if (jsonObject.get("latitud") != null && !jsonObject.get("latitud").isJsonNull()) {
            evento.setLatitud(jsonObject.get("latitud").getAsDouble());
        }

        if (jsonObject.get("longitud") != null && !jsonObject.get("longitud").isJsonNull()) {
            evento.setLongitud(jsonObject.get("longitud").getAsDouble());
        }

        if (jsonObject.get("direccion") != null && !jsonObject.get("direccion").isJsonNull()) {
            evento.setDireccion(jsonObject.get("direccion").getAsString());
        }

        if (jsonObject.get("evento_estado") != null && !jsonObject.get("evento_estado").isJsonNull()) {
            evento.setEvento_estado_decripcion(jsonObject.get("evento_estado").getAsString());
        }

        if (jsonObject.get("evento_tipo") != null && !jsonObject.get("evento_tipo").isJsonNull()) {
            evento.setEvento_tipo_decripcion(jsonObject.get("evento_tipo").getAsString());
        }

        if (jsonObject.get("evento_tipo_id") != null && !jsonObject.get("evento_tipo_id").isJsonNull()) {
            evento.setEvento_tipo_id(jsonObject.get("evento_tipo_id").getAsInt());
        }

        if (jsonObject.get("evento_estado_id") != null && !jsonObject.get("evento_estado_id").isJsonNull()) {
            evento.setEvento_estado_id(jsonObject.get("evento_estado_id").getAsInt());
        }



        return evento;
    }
}


