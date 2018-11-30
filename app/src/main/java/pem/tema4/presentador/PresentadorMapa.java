package pem.tema4.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.model.Marker;
import pem.tema4.AppMediador;
import pem.tema4.pem.tema4.modelo.DatoMarca;
import pem.tema4.pem.tema4.modelo.IModelo;
import pem.tema4.pem.tema4.modelo.Modelo;
import pem.tema4.vista.VistaMapa;

import java.util.ArrayList;

public class PresentadorMapa implements IPresentadorMapa {

    private AppMediador appMediador;
    private VistaMapa vistaMapa;
    private Marker marcaSeleccionada;
    private IModelo modelo;

    // TODO Crear un objeto receptor de avisos (BroadcastReceiver) que reciba notificaciones del modelo. Las notificaciones
    // a recibir son: AVISO_ESTADO_INICIAL, AVISO_AGREGAR_MARCA y AVISO_BORRAR_MARCA
    private BroadcastReceiver receptorAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppMediador.AVISO_ESTADO_INICIAL)){
                ArrayList<DatoMarca> datoMarcas = (ArrayList<DatoMarca>) intent.getSerializableExtra(AppMediador.CLAVE_MARCAS);
                for (int i=0; i <datoMarcas.size(); i++){
                    Object[] datos = new Object[3];
                    datos[0] = datoMarcas.get(i).getLatitud();
                    datos[1] = datoMarcas.get(i).getLongitud();
                    datos[2] = datoMarcas.get(i).getTitulo();
                    appMediador.getVistaMapa().actualizarMapa(datos);

                }
            } else if (intent.getAction().equals(AppMediador.AVISO_AGREGAR_MARCA)){
                Object[] datos = new Object[3];
                datos[0] = intent.getDoubleExtra(AppMediador.CLAVE_LATITUD, 0.0);
                datos[1] = intent.getDoubleExtra(AppMediador.CLAVE_LONGITUD, 0.0);
                datos[2] = intent.getSerializableExtra(AppMediador.CLAVE_TITULO);
                appMediador.getVistaMapa().actualizarMapa(datos);

            } else if (intent.getAction().equals(AppMediador.AVISO_BORRAR_MARCA)){

            }
            appMediador.unRegisterReceiver(this);
        }
    };

    // TODO Crear un objeto receptor de gps (BroadcastReceiver) que reciba notificaciones del modelo. La notificación
    // a recibir es: AVISO_LOCALIZACION_GPS
    private BroadcastReceiver receptorGps = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GPS)){
                Object[] datos = new Object[3];
                datos[0] = intent.getDoubleExtra(AppMediador.CLAVE_LATITUD, 0);
                datos[1] = intent.getDoubleExtra(AppMediador.CLAVE_LONGITUD,  0);
                datos[2] = "Mi ubicación";
                appMediador.getVistaMapa().actualizarMapa(datos);
                appMediador.unRegisterReceiver(this);
                appMediador.registerReceiver(this, AppMediador.AVISO_LOCALIZACION_GPS);
            }
        }
    };

    public PresentadorMapa() {
        appMediador = AppMediador.getInstance();
        vistaMapa = (VistaMapa) appMediador.getVistaMapa();
        modelo = Modelo.getInstance();
    }


    // TODO Método redefinido iniciarGps que obtiene la posición Gps del dispositivo

    @Override
    public void iniciarGps() {
        appMediador.registerReceiver(receptorGps, AppMediador.AVISO_LOCALIZACION_GPS);
        modelo.iniciarGps();
    }


    // TODO Método redefinido actualizarMapa que llama al método actualizarMapa de la vista VistaMapa
    // y que coloca un marker en el punto indicado por el modelo según el estado de la aplicación

    @Override
    public void actualizarMapa(int estado, Object datos) {
        switch (estado){
            case AppMediador.ESTADO_INICIAL:
                appMediador.registerReceiver(receptorAvisos, AppMediador.AVISO_ESTADO_INICIAL);
                modelo.actualizarMapas();
                break;
            case AppMediador.ESTADO_AGREGAR_MARCA:
                appMediador.registerReceiver(receptorAvisos, AppMediador.AVISO_AGREGAR_MARCA);
                modelo.agregarMarca(datos);
                break;
            case AppMediador.ESTADO_BORRAR_MARCA:
                appMediador.registerReceiver(receptorAvisos, AppMediador.AVISO_BORRAR_MARCA);
                modelo.borrarMarca(datos);
                break;
            default:
                break;
        }


    }

    // TODO Método redefinido tratarToqueMapa que pone una marca en el mapa en la latitud y longitud indicada y
    // que pide al usuario el título asociado a la marca

    @Override
    public void tratarToqueMapa(double latitud, double longitud) {
        Object[] datos = new Object[2];
        datos[0] = latitud;
        datos[1] = longitud;
        appMediador.getVistaMapa().mostrarDialogo(datos);
    }

    // TODO Método redefinido tratarToqueMarca que presenta un menú para tratar una marca seleccionada por el usuario

    @Override
    public void tratarTogueMarca(Object datos) {
        appMediador.getVistaMapa().mostrarMenu(datos);
    }

    // TODO Método redefinido editarMarca que edita la información de una marca dada seleccionada por el usuario


    @Override
    public void editarMarca(Object datos) {
        //TODO convertir a marker
        Marker marker = (Marker) datos;
        String[] marca = new String[3];
        marca[0] = String.valueOf(marker.getPosition().latitude);
        marca[1] = String.valueOf(marker.getPosition().longitude);
        marca[2] = marker.getTitle();
        appMediador.getVistaMapa().mostrarInformacion(marca);


    }
}
