package ar.com.sight.android.api.deserializadores;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ar.com.sight.android.api.modelos.Evento;

public class EventosDeserializador implements JsonDeserializer<ArrayList<Evento>> {

    @Override
    public ArrayList<Evento> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ArrayList<Evento> eventos = new ArrayList<>();

        JsonArray jArray = json.getAsJsonArray();

        EventoDeserializador deserializador = new EventoDeserializador();

        for (int i = 0; i < jArray.size(); i++) {
            JsonObject jsonObject = jArray.get(i).getAsJsonObject();
            Evento evento= deserializador.deserialize(jsonObject, typeOfT, context);
            eventos.add(evento);
        }

        return eventos;
    }
}