package soporte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author maxi
 */
public class Database {

    private final String nombre;
    private Connection con;
    private int map_size;

    public Database() {
        map_size = 0;
        nombre = "Vocabulario";
        try {
            open();

            execute_sql("CREATE TABLE IF NOT EXISTS palabras"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "PALABRA TEXT NOT NULL,"
                    + "CANTIDAD INTEGER NOT NULL)");
            execute_sql("CREATE TABLE IF NOT EXISTS libros"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "LIBRO TEXT NOT NULL)");
            execute_sql("CREATE TABLE IF NOT EXISTS palabrasxlibro"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "ID_Palabra INTEGER NOT NULL,"
                    + "ID_Libro INTEGER NOT NULL)");

        } catch (SQLException ex) {
            System.err.println("Error en el constructor: " + ex.getMessage());
        } finally {
            close();
        }
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

    public void execute_sql(String sql) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.executeUpdate();
    }
    
    /**
     * Recibe el nombre de un libro y su correspondiente LinkedHashMap de conteo
     * de palabras.
     * Inserta el libro con su respectivo ID en una tabla, la palabra con su ID y
     * cantidad de apariciones en otra tabla y luego inserta el ID de la palabra
     * y el libro en el que fue hallada en otra tabla.
     * @param libro
     * @param map 
     */
    public void insert(String libro, HashMap<String, Integer> map) {
        try {

            int i = 0;
            /**
             * Las sentencias SQL están condicionadas para que no haya repetición.
             */
            String sql_contains = "SELECT * FROM palabras WHERE PALABRA = (?)";
            String sql_libro = "INSERT INTO libros (LIBRO)"
                + "SELECT * FROM (SELECT (?)) AS tmp "
                + "WHERE NOT EXISTS"
                + "(SELECT LIBRO FROM libros WHERE LIBRO = (?))"
                + "LIMIT 1;";
            String sql_palabra = "INSERT INTO palabras(ID, PALABRA, CANTIDAD) VALUES (?,?,?)";
            String sql_palabraxlibro = "INSERT INTO palabrasxlibro (ID_Palabra, ID_Libro) "
                    + "SELECT * FROM (SELECT palabras.ID as idpal, libros.ID as idlib "
                    + "FROM libros INNER JOIN palabras "
                    + "WHERE palabras.PALABRA = (?) AND libros.LIBRO = (?)) AS tmp "
                    + "WHERE NOT EXISTS"
                    + "(SELECT * FROM palabrasxlibro WHERE ID_Palabra = idpal "
                    + "AND ID_Libro = idlib) LIMIT 1;";
            String sql_update = "UPDATE palabras SET CANTIDAD = CANTIDAD + (?) WHERE PALABRA = (?);";

            PreparedStatement ps_libro = con.prepareStatement(sql_libro);
            PreparedStatement ps_palabra = con.prepareStatement(sql_palabra);
            PreparedStatement ps_palabraxlibro = con.prepareStatement(sql_palabraxlibro);
            PreparedStatement ps_update = con.prepareStatement(sql_update);
            PreparedStatement ps_contains = con.prepareStatement(sql_contains);
            
            ps_libro.setString(1, libro);
            ps_libro.setString(2, libro);
            ps_libro.executeUpdate();

            con.setAutoCommit(false);
            for (HashMap.Entry<String, Integer> entry : map.entrySet()) {
                String palabra = entry.getKey();
                int cantidad = entry.getValue();
                
                ps_contains.setString(1, palabra);
                ResultSet rs = ps_contains.executeQuery();
                
                ps_palabraxlibro.setString(1, palabra);
                ps_palabraxlibro.setString(2, libro);
                ps_palabraxlibro.addBatch();

                if (!rs.next()) {
                    ps_palabra.setString(2, palabra);
                    ps_palabra.setInt(3, cantidad);
                    ps_palabra.addBatch();
                    i++;

                } else {
                    ps_update.setInt(1, cantidad);
                    ps_update.setString(2, palabra);
                    ps_update.addBatch();
                    i++;
                }
                if (i % 3000 == 0 || i == map.size() - 1) {
                    ps_palabra.executeBatch();
                    ps_update.executeBatch();
                    ps_palabraxlibro.executeBatch();
                    i = 0;
                }
            }
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            System.err.println("No se pudo insertar HashMap en la DB: " + ex.getMessage());
        }
    }

        public ArrayList toWordArray()
      {
        ResultSet rs = null;
        ArrayList<Word> arr = new ArrayList<>();
        String sql = "SELECT palabras.ID, palabras.PALABRA, palabras.CANTIDAD, count(palabrasxlibro.ID_Libro) as TOTAL FROM palabrasxlibro INNER JOIN palabras,libros ON  palabras.ID = palabrasxlibro.ID_Palabra and libros.ID = palabrasxlibro.ID_Libro group by palabrasxlibro.ID_Palabra;";
        try
          {
            PreparedStatement ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next())
              {
                Word w;
                w = new Word(rs.getInt("ID"),rs.getString("PALABRA"),rs.getInt("CANTIDAD"), rs.getInt("TOTAL"));
                arr.add(w);
              }
          } catch (SQLException ex)
          {
            System.err.println("No se pudo ejecutar la consulta: " + ex.getMessage());
          }
        return arr;
      }
}
