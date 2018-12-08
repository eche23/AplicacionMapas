package pem.tema4.vista;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import pem.tema4.AppMediador;
import pem.tema4.presentador.IPresentadorMapa;

public class VistaMapa extends FragmentActivity implements OnMapReadyCallback, IVistaMapa, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private Marker miUbicacion;
    private AppMediador appMediador;
    private IPresentadorMapa presentadorMapa;
    private int anchoPantalla, altoPantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        appMediador = (AppMediador)this.getApplication();
        appMediador.setVistaMapa(this);
        presentadorMapa = appMediador.getPresentadorMapa();
        miUbicacion = null;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // TODO Asociar al mapa los listener para manejar el toque del mapa y de los iconos
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        // TODO Solicitar al presentador que inice el GPS
        presentadorMapa.iniciarGps();
        // TODO Solicitar al presentador que actualice el mapa en el estado inicial
        presentadorMapa.actualizarMapa(AppMediador.ESTADO_INICIAL,null);
    }

    // Método actualizarMapa que recibe por parámetros un array de Object en el que se almacena
    // 0: la latitud de un marker
    // 1: la longitud de un marker
    // 2: el título del marker
    // ES UN MÉTODO @Override
    @Override
    public void actualizarMapa(Object datos) {
        // TODO Recuperar la latitud, longitud y titulo que se recibe desde parámetros y crear el objeto lugar
        Object[] objects = (Object[]) datos;
        double latitud = (double) objects[0];
        double longitud = (double) objects[1];
        String titulo = (String) objects[2];
        LatLng lugar = new LatLng(latitud, longitud);


        // Comprobar cómo se crean las marcas de Mi ubicación y de cualquier otro icono (waypoint)
        if (titulo.equals(("Mi ubicación"))) {
            // es el dispositivo del usuario (datos obtenidos por GPS)
            // se elimina la marca de la posición anterior del usuario, si existía
            if (miUbicacion != null) {
                miUbicacion.remove();
            }
            miUbicacion = mMap.addMarker(new MarkerOptions().position(lugar).title(titulo).
                    icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lugar, AppMediador.ZOOM));
        } else
            mMap.addMarker(new MarkerOptions().position(lugar).title(titulo));
    }

    // Método redefinido mostrarDialogo que muestra un diálogo en el mapa para que el usuario introduzca el título
    // de una marca. Por parámetros se recibe un array de Object en el que se almacena:
    // 0: la latitud de la marca
    // 1: la longitud de la marca
    // ES UN MÉTODO @Override
    @Override
    public void mostrarDialogo(Object datos) {
        Object[] info = (Object[]) datos;
        final double latitud = (double) info[0];
        final double longitud = (double) info[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introduzca el título de la marca");
        View vistaDialogo = this.getLayoutInflater().inflate(R.layout.dialogo_marca, null);
        final EditText titulo = (EditText) vistaDialogo.findViewById(R.id.titulo);
        builder.setView(vistaDialogo);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Eliminar el diálogo y crear un objeto array de Object con la información de la marca (latitud,
                // longitud y titulo) y pedir al presentador que actualice el mapa para el estado de agregar marca.
                dialog.dismiss();
                Object[] datos = new Object[3];
                datos[0] = latitud;
                datos[1] = longitud;
                datos[2] = titulo.getText().toString();
                presentadorMapa.actualizarMapa(AppMediador.ESTADO_AGREGAR_MARCA, datos);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    // Método redefinido mostrarInformacion que muestra, en el mapa, la latitud, longitud y título de una marca.
    // Por parámetros se recibe un array de Object en el que se almacena:
    // 0: la latitud de la marca
    // 1: la longitud de la marca
    // 2: el título de la marca
    // ES UN MÉTODO @Override
    @Override
    public void mostrarInformacion(Object informacion) {
        String[] datos = (String[])informacion;
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Información de la marca");
        // TODO Inflar el layout informacion_marca y actualizar los valores de la latitud, longitud y titulo
        // con los recibidos por parámetros. Actualizar el objeto builder con el layout inflado. Fijarse en
        // como se ha hecho en el método mostrarDialogo.
        View vistaDialogo = this.getLayoutInflater().inflate(R.layout.informacion_marca, null);
        TextView latitud = (TextView) vistaDialogo.findViewById(R.id.latitud);
        latitud.setText(latitud.getText()+" "+datos[0]);
        TextView longitud = (TextView) vistaDialogo.findViewById(R.id.longitud);
        longitud.setText(longitud.getText()+" "+datos[1]);
        TextView titulo = (TextView) vistaDialogo.findViewById(R.id.titulo);
        titulo.setText(titulo.getText()+" "+datos[2]);
        builder.setView(vistaDialogo);


        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Método redefinido mostrarMenu que muestra un diálogo con un menú en el mapa con las opciones: editar y borrar
    // sobre una marca
    // ES UN MÉTODO @Override
    @Override
    public void mostrarMenu(final Object marca) {
        Projection projection = mMap.getProjection();
        Marker temp = (Marker)marca;
        Point punto = projection.toScreenLocation(((Marker) marca).getPosition());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View vistaDialogo = this.getLayoutInflater().inflate(R.layout.menu_marca, null);
        builder.setView(vistaDialogo);
        ImageButton botonE = (ImageButton)vistaDialogo.findViewById(R.id.editar);
        ImageButton botonB = (ImageButton)vistaDialogo.findViewById(R.id.borrar);
        final AlertDialog dialog = builder.create();
        botonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                // TODO Solicitar al presentador que edite la marca
                presentadorMapa.editarMarca(marca);

            }
        });

        botonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                // TODO Solicitar al presentador que borre la marca (borrar marca es un estado de la aplicación que
                // implica la actualización del mapa.
                presentadorMapa.actualizarMapa(AppMediador.ESTADO_BORRAR_MARCA, marca);

            }
        });

        dialog.show();
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.copyFrom(dialog.getWindow().getAttributes());
        wmlp.width = 180;
        wmlp.x = punto.x - anchoPantalla/2;
        wmlp.y = punto.y - altoPantalla/2;
        if (punto.x > anchoPantalla/2)
            wmlp.x -= wmlp.width/2;
        else
            wmlp.x += wmlp.width/2;
        dialog.getWindow().setAttributes(wmlp);
    }

    // TODO Método redefinido borrarMarca que borra la marca dada por parámetros.

    @Override
    public void borrarMarca(Object marca) {
        Marker marker = (Marker) marca;
        marker.remove();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        appMediador.removePresentadorMapa();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        presentadorMapa.tratarToqueMapa(latLng.latitude, latLng.longitude);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        presentadorMapa.tratarToqueMarca(marker);
        return false;
    }
}
