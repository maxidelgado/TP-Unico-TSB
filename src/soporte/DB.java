package soporte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maxi
 */
public class DB {

    private final String nombre;
    private Connection con;
    private int batch;
    private int map_size;

    public DB() {
        batch = 0;
        map_size = 0;
        nombre = "Vocabulario";
        open();
        try {
            executeSql("CREATE TABLE IF NOT EXISTS palabras"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "PALABRA TEXT NOT NULL,"
                    + "CANTIDAD INTEGER NOT NULL)");
        } catch (SQLException ex) {
            System.err.println("No se pudo crear tabla palabras: " + ex.getMessage());
        }

        try {
            executeSql("CREATE TABLE IF NOT EXISTS libros"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "LIBRO TEXT NOT NULL)");
        } catch (SQLException ex) {
            System.err.println("No se pudo crear tabla libros: " + ex.getMessage());
        }

        try {
            executeSql("CREATE TABLE IF NOT EXISTS palabrasxlibro"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "ID_Palabra INTEGER NOT NULL,"
                    + "ID_Libro INTEGER NOT NULL)");
        } catch (SQLException ex) {
            System.err.println("No se pudo crear tabla palabrasxlibro: " + ex.getMessage());
        }
        close();
    }

    public void open() {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:" + nombre + ".db");
            if (con != null) {
                System.out.println("Conectado");
            }
        } catch (SQLException ex) {
            System.err.println("No se pudo conectar a la DB\n" + ex.getMessage());
        }
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException ex) {
            System.err.println("No se pudo cerrar la conexion a la DB\n" + ex.getMessage());
        }
    }

    public void executeSql(String sql) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
    }

    public ResultSet select(String sql) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            System.err.println("No se pudo ejecutar la consulta: " + ex.getMessage());
        }
        return rs;
    }

    private void update(String palabra, int c) {
        try {
            String sql = "SELECT * FROM palabras WHERE PALABRA = " + "'" + palabra + "'";
            ResultSet rs = select(sql);
            int cant = rs.getInt("CANTIDAD");
            int total = cant + c;
            System.out.println("palabra: " + palabra);
            System.out.println("c: " + c);
            System.out.println("cant: " + cant);
            System.out.println("total: " + total);
            sql = "UPDATE palabras SET CANTIDAD = " + total + " WHERE PALABRA = " + "'" + palabra + "'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.addBatch();
            if (batch % 3000 == 0 || batch == map_size) {
                ps.executeBatch();
            }
        } catch (SQLException ex) {
            System.err.println("No se pudo actualizar el valor en la DB: " + ex.getMessage());
        }
    }

    private void insertar_palabra(String palabra, int cantidad) {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO palabras(ID, PALABRA, CANTIDAD) VALUES (?,?,?)");
            if (!contains(palabra)) {
                ps.setString(2, palabra);
                ps.setInt(3, cantidad);
                ps.addBatch();
                if (batch % 3000 == 0 || batch == map_size) {
                    ps.executeBatch();
                }
            } else {
                update(palabra, cantidad);
            }
        } catch (SQLException ex) {
            System.err.println("No se pudo insertar la palabra: " + ex.getMessage());
        }
    }

    private void insertar_libro(String libro) {
        String sql = "INSERT INTO libros (LIBRO)"
                + "SELECT * FROM (SELECT '" + libro + "') AS tmp "
                + "WHERE NOT EXISTS"
                + "(SELECT LIBRO FROM libros WHERE LIBRO = '" + libro + "')"
                + "LIMIT 1;";
        try {
            executeSql(sql);
        } catch (SQLException ex) {
            System.err.println("No se pudo insertar libro: " + ex.getMessage());
        }
    }

    private void insertar_palabrasxlibro(int id_palabra, int id_libro) {
        String sql = "INSERT INTO palabrasxlibro (ID_Palabra, ID_Libro)"
                + "SELECT * FROM (SELECT " + id_palabra + ", " + id_libro + ") AS tmp "
                + "WHERE NOT EXISTS "
                + "(SELECT * FROM palabrasxlibro WHERE ID_Palabra = " + id_palabra + " AND ID_Libro = " + id_libro + ") "
                + "LIMIT 1;";
        try {
            executeSql(sql);
        } catch (SQLException ex) {
            System.err.println("No se pudo insertar palabrasxlibro: " + ex.getMessage());
        }
    }

    private boolean contains(String palabra) {
        String sql = "SELECT * FROM palabras WHERE PALABRA=" + "'" + palabra + "'";
        try {
            ResultSet rs = select(sql);
            return rs.next();
        } catch (SQLException ex) {
            System.err.println("No se pudo obtener elemento: " + ex.getMessage());
        }
        return false;
    }

    public int[] obtener_ids(String palabra, String libro) {
        String sql = "SELECT palabras.ID, libros.ID as ID_Libro "
                + "FROM libros "
                + "INNER JOIN palabras "
                + "WHERE palabras.PALABRA = '" + palabra + "' "
                + "AND libros.LIBRO = '" + libro + "'";

        ResultSet rs = select(sql);

        int id_palabra;
        int id_libro;
        int[] res = new int[2];
        try {
            id_palabra = rs.getInt("ID");
            id_libro = rs.getInt("ID_Libro");
            res[0] = id_palabra;
            res[1] = id_libro;
        } catch (SQLException ex) {
            System.err.println("No se pudo obtener los ids: " + ex.getMessage());
        }

        return res;
    }

    public void insertar_HashMap(String libro, HashMap<String, Integer> map) {
        try {
            con.setAutoCommit(false);
            insertar_libro(libro);
            for (HashMap.Entry<String, Integer> entry : map.entrySet()) {
                String palabra = entry.getKey();
                int cantidad = entry.getValue();

                insertar_palabra(palabra, cantidad);
                int[] ids = obtener_ids(palabra, libro);
                insertar_palabrasxlibro(ids[0], ids[1]);
            }
            con.commit();
        } catch (SQLException ex) {
            System.err.println("No se pudo insertar HashMap en la DB: " + ex.getMessage());
        }
    }

    public HashMap<Integer, Palabra> getPalabras() {
        ResultSet rs = null;
        Palabras p = new Palabras();
        String sql = "SELECT palabras.ID, palabras.PALABRA, palabras.CANTIDAD, libros.LIBRO "
                + "FROM palabrasxlibro "
                + "INNER JOIN palabras, libros "
                + "ON palabras.ID = palabrasxlibro.ID_Palabra";
        try {

            rs = select(sql);
            while (rs.next()) {
                int aux = rs.getInt("ID");
                Palabra w;
                w = new Palabra(rs.getInt("ID"), rs.getString("PALABRA"), rs.getInt("CANTIDAD"), rs.getString("LIBRO"));
                p.add_without_rep(w);
            }
            if (rs.next() == false) {
                Palabra w = new Palabra(0, "DB vacía", 0, "");
            }
        } catch (SQLException ex) {
            System.err.println("No se pudo ejecutar la consulta: " + ex.getMessage());
        }

        return p.getMap();
    }
}
