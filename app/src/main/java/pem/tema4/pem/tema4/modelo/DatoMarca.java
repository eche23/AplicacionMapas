package pem.tema4.pem.tema4.modelo;

import java.io.Serializable;

public class DatoMarca implements Serializable {
    private double latitud, longitud;
    private String titulo;

    public DatoMarca(double latitud, double longitud, String titulo) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.titulo = titulo;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getTitulo() {
        return titulo;
    }
}
