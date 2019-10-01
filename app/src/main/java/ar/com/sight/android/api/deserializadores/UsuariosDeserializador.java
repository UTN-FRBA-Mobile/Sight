package ar.com.sight.android.api.deserializadores;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ar.com.sight.android.api.modelos.Usuario;

public class UsuariosDeserializador implements JsonDeserializer<ArrayList<Usuario>> {

    @Override
    public ArrayList<Usuario> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ArrayList<Usuario> usuarios = new ArrayList<>();

        JsonArray jArray = json.getAsJsonArray();

        UsuarioDeserializador deserializador = new UsuarioDeserializador();

        for (int i = 0; i < jArray.size(); i++) {
            JsonObject jsonObject = jArray.get(i).getAsJsonObject();
            Usuario usuario = deserializador.deserialize(jsonObject, typeOfT, context);
            usuarios.add(usuario);
        }

        return usuarios;
    }
}