package controlador;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import soporte.*;

/**
 *
 * @author maxi
 */
public class ControladorDB {

    private Archivo archivo;
    private DB db;
    private HashMap<String, Integer> map_insert;
    private HashMap<Integer, Palabra> map_select;
    private File f;
    private Object[][] tabla;

    public ControladorDB() {
        db = new DB();
        map_select = new HashMap<>();
    }

    public ControladorDB(File file) {
        archivo = new Archivo(file);
        db = new DB();
        map_insert = archivo.leer();
        f = file;
    }

    public void guardar_libro() {
        db.open();
        db.insertar_HashMap(f.getName(), map_insert);
        Conteo.clear();
        db.close();
    }

    public Object[][] getPalabras() {
        db.open();
        map_select = db.getPalabras();
        tabla = new Object[map_select.size()][4];
        
        int i = 0;
        
        for (Entry<Integer, Palabra> entry : map_select.entrySet()) {
            tabla[i][0] = entry.getValue().getId();
            tabla[i][1] = entry.getValue().getPalabra();
            tabla[i][2] = entry.getValue().getCantidad();
            tabla[i][3] = entry.getValue().getLibros();
            i++;
        }
        db.close();
        return tabla;
    }

    public String tablaToString() {
        String res = "";
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[i].length; j++) {
                res += tabla[i][j] + "; ";
            }

            res += "\n";
        }
        return res;
    }

}
