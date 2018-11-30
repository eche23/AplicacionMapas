package pem.tema4.pem.tema4.modelo.bajo_nivel;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import pem.tema4.AppMediador;

public class ServicioLocalizacion extends Service implements LocationListener {

    // Distancia mínima en metros entre actualizaciones
    private static final long DISTANCIA_MINIMA_ENTRE_ACTUALIZACIONES = 0; // 0 metros, poner la que se quiera
    // Tiempo mínimo en milisegundos entre actualizaciones
    private static final long TIEMPO_MINIMO_ENTRE_ACTUALIZACIONES = 0;  // 0 minuto, poner la que se quiera
    boolean gpsEstaHabilitado = false;
    protected LocationManager locationManager;
    private AppMediador appMediador;

    @Override
    public void onCreate() {
        appMediador = AppMediador.getInstance();
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        gpsEstaHabilitado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsEstaHabilitado) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    TIEMPO_MINIMO_ENTRE_ACTUALIZACIONES,
                    DISTANCIA_MINIMA_ENTRE_ACTUALIZACIONES, this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Enviar al presentador, la latitud y longitud obtenida por el GPS.
        Bundle extras = new Bundle();
        extras.putDouble(AppMediador.CLAVE_LATITUD,location.getLatitude());
        extras.putDouble(AppMediador.CLAVE_LONGITUD,location.getLongitude());
        appMediador.sendBroadcast(AppMediador.AVISO_LOCALIZACION_GPS, extras);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
