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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author rodrigo
 */
public class Database
{
    private String nombre;
    
    public Database(String name)
      {
        nombre = name;
        try
          {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" + nombre + ".db");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE Vocabulario(ID INT PRIMARY KEY NOT NULL, PALABRA TEXT NOT NULL)");

          } catch (SQLException | ClassNotFoundException ex)
          {
            System.err.println("No se pudo crear la base: " + ex.getMessage());
          }
      }

    public void cargar(int id, String palabra)
      {
        try
          {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" + nombre + ".db");
            PreparedStatement ps = con.prepareStatement("INSERT INTO Vocabulario(ID, PALABRA) VALUES (?,?)");
            
            ps.setInt(1, id);
            ps.setString(2, palabra);
            ps.executeUpdate();
            
          } catch (SQLException | ClassNotFoundException ex)
          {
            System.err.println("No se pudo crear la base: " + ex.getMessage());
          }
       
      }
}
