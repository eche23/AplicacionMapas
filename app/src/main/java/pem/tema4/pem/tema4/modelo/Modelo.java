package pem.tema4.pem.tema4.modelo;

import android.os.Bundle;

import pem.tema4.AppMediador;
import pem.tema4.pem.tema4.modelo.bajo_nivel.ServicioLocalizacion;

import java.util.ArrayList;

public class Modelo implements IModelo {

    private static Modelo singleton = null;
    private AppMediador appMediador;
    private TablaMarcasAdaptadorBD adaptadorBD;

    private Modelo() {
        appMediador = AppMediador.getInstance();
        adaptadorBD = new TablaMarcasAdaptadorBD(appMediador.getApplicationContext());
    }

    public static Modelo getInstance() {
        if (singleton == null)
            singleton = new Modelo();
        return singleton;
    }


    // TODO Método redefinido iniciarGps que obtiene la posición Gps del dispositivo

    @Override
    public void iniciarGps() {
        appMediador.launchService(ServicioLocalizacion.class, null);
    }

    // TODO Método redefinido actualizarMapa que obtiene los datos para actualiza el mapa presente en la pantalla
    // según el estado de la aplicación

    @Override
    public void actualizarMapas() {
        adaptadorBD = adaptadorBD.abrir();
        Bundle extras = null;
        if (adaptadorBD != null) {
            ArrayList<DatoMarca> datoMarcas = adaptadorBD.obtenerRegistros();
            extras = new Bundle();
            extras.putSerializable(AppMediador.CLAVE_MARCAS, datoMarcas);
        }
        adaptadorBD.cerrar();
        appMediador.sendBroadcast(AppMediador.AVISO_ESTADO_INICIAL, extras);
    }


    // TODO Método redefinido agregarMarca que agrega una marca a la base de datos. El método recibe por parámetros un
    // array de Object en el que se almacena:
    // 0: la latitud de la marca
    // 1: la longitud de la marca
    // 2: el título de la marca

    @Override
    public void agregarMarca(Object datos) {
        adaptadorBD = adaptadorBD.abrir();
        Bundle extras = null;
        if (adaptadorBD != null) {
            Object[] marca = (Object[]) datos;
            double latitud = (double) marca[0];
            double longitud = (double) marca[1];
            String titulo = (String) marca[2];
            adaptadorBD.crearRegistro(latitud, longitud, titulo);

            extras = new Bundle();
            extras.putDouble(AppMediador.CLAVE_LATITUD, latitud);
            extras.putDouble(AppMediador.CLAVE_LONGITUD, longitud);
            extras.putString(AppMediador.CLAVE_TITULO, titulo);

        }
        adaptadorBD.cerrar();
        appMediador.sendBroadcast(AppMediador.AVISO_AGREGAR_MARCA, extras);

    }

    // TODO Método redefinido borrarMarca que borra una marca de la base de datos

    @Override
    public void borrarMarca(Object datos) {
        adaptadorBD = adaptadorBD.abrir();
        Bundle extras = null;
        if (adaptadorBD !=null){
            Object[] datosMarca = (Object[]) datos;
            double latitud = (double) datosMarca[0];
            double longitud = (double) datosMarca[1];
            String titulo = (String) datosMarca[2];
            adaptadorBD.borrarRegistro(latitud, longitud, titulo);
            extras = new Bundle();


        }
        adaptadorBD.cerrar();
        appMediador.sendBroadcast(AppMediador.AVISO_BORRAR_MARCA, extras);
    }

}
