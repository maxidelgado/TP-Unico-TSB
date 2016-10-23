/*
 * Material de consulta
 * Comandos basicos
 * https://www.sqlite.org/sessions/sqlite.html
 * Sentencia bien formada
 * http://www.sqlitetutorial.net/sqlite-java/insert/
 * http://stackoverflow.com/questions/5774713/trouble-inserting-data-in-a-sqlite-databae
 * https://www.mkyong.com/jdbc/jdbc-preparestatement-example-insert-a-record/
 * 
 */
package soporte;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rodrigo
 */
public class Database {

    private final String nombre;
    private Connection con;

    public Database(String nom) {
        nombre = nom;
        createTable();
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

    public void createTable() {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Vocabulario"
                    + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "PALABRA TEXT NOT NULL, "
                    + "CANTIDAD INTEGER NOT NULL)");
        } catch (SQLException ex) {
            System.err.println("No se pudo crear la base: " + ex.getMessage());
        }
    }

    public void insert(HashMap<String, Integer> map) {
        int i = 0;

        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Vocabulario(ID, PALABRA, CANTIDAD) VALUES (?,?,?)");
            con.setAutoCommit(false);
            for (HashMap.Entry<String, Integer> entry : map.entrySet()) {

                if (!contains(entry.getKey())) {
                    ps.setString(2, entry.getKey());
                    ps.setInt(3, entry.getValue());
                    ps.addBatch();
                } else {
                    update(entry.getKey(), entry.getValue());
                }

                i++;

                if (i % 3000 == 0 || i == map.size()) {
                    ps.executeBatch();
                }
            }
            con.commit();
        } catch (SQLException ex) {
            System.err.println("No se pudo insertar en la DB: " + ex.getMessage());
        } 
    }

    public ResultSet select(String palabra) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Vocabulario WHERE PALABRA=" + "'" + palabra + "'");
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            System.out.println("No se pudo ejecutar la consulta: " + ex.getMessage());
        } 
        return rs;
    }

    public boolean contains(String palabra) {
        try {
            ResultSet rs = select(palabra);
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void update(String palabra, int c) {
        int cant = 0;
        try {
            ResultSet rs = select(palabra);
            cant = rs.getInt("CANTIDAD");
            int total = cant + c;

            PreparedStatement ps = con.prepareStatement("UPDATE Vocabulario SET CANTIDAD=" + total + " WHERE PALABRA=" + "'" + palabra + "'");
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("No se pudo actualizar el valor en la DB: " + ex.getMessage());
        } 
    }

}
