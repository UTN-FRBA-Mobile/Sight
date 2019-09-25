package rsa.sight.api.modelos;

public class Evento {
    private int id;
    private String timestamp;
    private int evento_estado_id;
    private String evento_estado_decripcion;
    private int evento_tipo_id;
    private String evento_tipo_decripcion;
    private double latitud;
    private double longitud;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String nombre) {
        this.timestamp = nombre;
    }

    public int getEvento_estado_id() {
        return evento_estado_id;
    }

    public void setEvento_estado_id(int evento_estado_id) {
        this.evento_estado_id = evento_estado_id;
    }

    public int getEvento_tipo_id() {
        return evento_tipo_id;
    }

    public void setEvento_tipo_id(int evento_tipo_id) {
        this.evento_tipo_id= evento_tipo_id;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud= latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud= longitud;
    }

    public String getEvento_estado_decripcion() {
        return evento_estado_decripcion;
    }

    public void setEvento_estado_decripcion(String evento_estado_decripcion) {
        this.evento_estado_decripcion = evento_estado_decripcion;
    }

    public String getEvento_tipo_decripcion() {
        return evento_tipo_decripcion;
    }

    public void setEvento_tipo_decripcion(String evento_tipo_decripcion) {
        this.evento_tipo_decripcion = evento_tipo_decripcion;
    }
}