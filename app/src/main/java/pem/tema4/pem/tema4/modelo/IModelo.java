package pem.tema4.pem.tema4.modelo;

public interface IModelo {

    // TODO Método llamado iniciarGps que obtiene la posición Gps del dispositivo
    void iniciarGps();
    
    // TODO Método llamado actualizarMapa que actualiza el mapa con la información inicial
    void actualizarMapas();

    // TODO Método llamado agregarMarca que agrega una marca a la base de datos
    void agregarMarca(Object datos);

    // TODO Método llamado borrarMarca que borra una marca de la base de datos
    void borrarMarca(Object datos);
}
