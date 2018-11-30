package pem.tema4.vista;

public interface IVistaMapa {

    // TODO Método llamado actualizarMapa que modifica el mapa mostrado en la pantalla del terminal usando
    // el objeto pasado por parámetros
    void actualizarMapa(Object datos);


    // TODO Método llamado mostrarDialogo que muestra un diálogo en el mapa para que el usuario introduzca una información.
    // A través del parámetro datos se puede pasar datos que el diálogo necesite
    void mostrarDialogo(Object datos);

    // TODO Método llamado mostrarMenu que muestra un diálogo con un menú en el mapa para tratar una marca
    void mostrarMenu(Object marca);

    // TODO Método llamado mostrarInformacion que muestra una información en el mapa
    void mostrarInformacion(Object informacion);

    // TODO Método llamado borrarMarca que borra una marca seleccionada por el usuario


}
