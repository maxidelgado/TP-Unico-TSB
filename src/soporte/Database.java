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

/**
 *
 * @author rodrigo
 */
public class Database {

    private String nombre;

    public Database(String name) {
        nombre = name;
        try {
            File f = new File(nombre);
            if (!f.exists()) {
                Class.forName("org.sqlite.JDBC");
                Connection con = DriverManager.getConnection("jdbc:sqlite:" + nombre + ".db");
                Statement stmt = con.createStatement();
                stmt.executeUpdate("CREATE TABLE Vocabulario(ID INTEGER PRIMARY KEY AUTOINCREMENT, PALABRA TEXT NOT NULL, CANTIDAD INTEGER NOT NULL)");
            } else {
                System.err.println("La DB ya existe.");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("No se pudo crear la base: " + ex.getMessage());
        }
    }

    public void cargar(HashMap<String, Integer> map) {
        int i = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" + nombre + ".db");
            PreparedStatement ps = con.prepareStatement("INSERT INTO Vocabulario(ID, PALABRA, CANTIDAD) VALUES (?,?,?)");

            con.setAutoCommit(false);
            for (HashMap.Entry<String, Integer> entry : map.entrySet()) {
                //System.out.println(entry.getKey()+": "+entry.getValue());
                ps.setString(2, entry.getKey());
                ps.setInt(3, entry.getValue());
                ps.addBatch();

                i++;

                if (i % 3000 == 0 || i == map.size()) {
                    System.out.println("item: " + i);
                    ps.executeBatch();
                }
            }
            con.commit();
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println("No se pudo crear la base: " + ex.getMessage());
        }

    }
}