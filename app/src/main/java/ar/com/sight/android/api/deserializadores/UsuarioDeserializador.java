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

public class UsuarioDeserializador implements JsonDeserializer<Usuario> {

    @Override
    public Usuario deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        Usuario usuario = new Usuario();

        if (jsonObject.get("id") == null || jsonObject.get("id").isJsonNull()) {
            usuario.setId(-1);
        } else {
            usuario.setId(jsonObject.get("id").getAsInt());
        }

        usuario.setApellido(jsonObject.get("apellido").getAsString());
        usuario.setNombre(jsonObject.get("nombre").getAsString());
        if (jsonObject.get("email") != null && !jsonObject.get("email").isJsonNull()) {
            usuario.setEmail(jsonObject.get("email").getAsString());
        }
        if (jsonObject.get("firebase") != null && !jsonObject.get("firebase").isJsonNull()) {
            usuario.setFirebase(jsonObject.get("firebase").getAsString());
        }
        return usuario;
    }
}