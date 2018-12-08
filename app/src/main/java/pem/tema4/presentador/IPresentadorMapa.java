package pem.tema4.presentador;

public interface IPresentadorMapa {

    // TODO Método llamado iniciarGps que obtiene la posición Gps del dispositivo
    void iniciarGps();

    // TODO Método llamado actualizarMapa que actualiza el mapa presente en la pantalla con los datos según el estado
    // de la aplicación
    void actualizarMapa(int estado, Object datos);

    // TODO Método llamado tratarToqueMapa que trata un toque en el mapa en la latitud y longitud indicada
    void tratarToqueMapa(double latitud, double longitud);

    // TODO Método llamado tratarToqueMarca que trata un toque en una marca dada
    void tratarToqueMarca(Object datos);

    // TODO Método llamado editarMarca que edita la información de una marca dada
    void editarMarca(Object datos);

}

