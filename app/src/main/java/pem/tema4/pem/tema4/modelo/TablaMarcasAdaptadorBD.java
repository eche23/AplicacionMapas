package pem.tema4.pem.tema4.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TablaMarcasAdaptadorBD {

    // las tablas de la base de datos deben usar como nombre de la clave primaria _id
    public static final String COLUMNA_ID = "_id";
    public static final String COLUMNA_LATITUD = "latitud";
    public static final String COLUMNA_LONGITUD = "longitud";
    public static final String COLUMNA_TITULO = "titulo";

    private static final String NOMBRE_BD = "MisMarcas.db";
    private static final String NOMBRE_TABLA = "Marcas";
    private static final int VERSION_BD = 1;
    private SQLiteDatabase db;
    private Context contexto;
    private DataBaseHelper dataBaseHelper;

    private static class DataBaseHelper extends SQLiteOpenHelper {

        DataBaseHelper(Context contexto) {
            super(contexto, NOMBRE_BD, null, VERSION_BD);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql =  "CREATE TABLE IF NOT EXISTS "+NOMBRE_TABLA+" ( " + COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMNA_LATITUD + " DOUBLE NOT NULL, " + COLUMNA_LONGITUD + " DOUBLE NOT NULL, " + COLUMNA_TITULO +
                    " TEXT NOT NULL );";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int verionAnterior, int nuevaVersion) {
            String sql = "DROP TABLE IF EXISTS " + NOMBRE_TABLA+";";
            db.execSQL(sql);
            onCreate(db);
        }
    }

    public TablaMarcasAdaptadorBD(Context contexto) {
        this.contexto = contexto;
    }

    public TablaMarcasAdaptadorBD abrir() {
        dataBaseHelper = new DataBaseHelper(contexto);
        db = dataBaseHelper.getWritableDatabase();
        if (db == null)
            return null;
        if (estaVacia()) {
            crearRegistro(28.070900,-15.454546,"Edif. Telecomunicación");
        }
        return this;
    }

    public void cerrar() {
        dataBaseHelper.close();
        db.close();
    }

    private boolean estaVacia() {
        String sql = "SELECT * FROM "+ NOMBRE_TABLA+";";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() == 0)
            return true;
        return false;
    }

    public long crearRegistro(double latitud, double longitud, String titulo) {
        ContentValues valores = new ContentValues();
        valores.put(COLUMNA_LATITUD, latitud);
        valores.put(COLUMNA_LONGITUD, longitud);
        valores.put(COLUMNA_TITULO, titulo);
        return db.insert(NOMBRE_TABLA, null, valores);
    }

    public long borrarRegistro(double latitud, double longitud, String titulo) {
        // TODO Crear la cláusula WHERE que compruebe si la latitud, longitud y titulo están almacenados en un registro
        // único de la base de datos (para luego eliminarlo). Tener en cuenta que tanto la latitud como la longitud son
        // de tipo double, y por tanto, hay que comprobarlas usando un valor epsilon (=1e-3). Fijarse en la página
        // https://www.techonthenet.com/sqlite/between.php donde se explica la condición BETWEEN.
        String clausula = COLUMNA_LATITUD + " BETWEEN " + (latitud-1e-3) + " AND " + (latitud+1e-3) + " AND "
                + COLUMNA_LONGITUD + " BETWEEN " + (longitud-1e-3) + " AND " + (longitud+1e-3) +
                " AND " + COLUMNA_TITULO + " = '" + titulo + "'";
        return db.delete(NOMBRE_TABLA, clausula, null);
    }

    public ArrayList<DatoMarca> obtenerRegistros() {
        String sql = "SELECT * FROM "+ NOMBRE_TABLA+";";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<DatoMarca> lista = new ArrayList<DatoMarca>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            double latitud = cursor.getDouble(1);
            double longitud = cursor.getDouble(2);
            String titulo = cursor.getString(3);
            lista.add(new DatoMarca(latitud, longitud, titulo));
        }
        cursor.close();
        return lista;
    }




}
