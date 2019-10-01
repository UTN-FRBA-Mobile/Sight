package ar.com.sight.android.api.modelos;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String firebase;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirebase() {
        return firebase;
    }

    public void setFirebase(String firebase) {
        this.firebase = firebase;
    }
}