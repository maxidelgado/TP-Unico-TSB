/*
 * Clase encargada de leer un archivo de disco
 */
package soporte;

import java.io.*;
import java.util.Scanner;

/**
 *
 * @author rodrigo
 */
public class Archivo
{

    private File archivo;

    public Archivo()
      {
        archivo = new File("txt/asd.txt");
      }

    /**
     * Obtiene la cantidad de líneas del archivo. Usa un buffered reader y avanzar linea por linea hasta que finaliza el archivo, incrementando un contador por cada linea.
     */
    public int tamaño()
      {
        int lineas = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo)))
          {
            while (null != br.readLine())
              {
                lineas++;
              }
          } catch (IOException ex)
          {
            System.err.println("Error al leer el archivo: " + ex.getMessage());
          }
        return lineas;
      }

    /**
     * Muestra el contenido del archivo. Sólo sirve para verificar que lea correctamente. Acumula las salidas en un StringBuilder
     *
     * @return
     */
    public String mostrarLineas()
      {
        StringBuilder aux = new StringBuilder();
        try (Scanner in = new Scanner(archivo))
          {
            while (in.hasNext())
              {
                aux.append(in.nextLine());
                aux.append("\n");
              }
          } catch (IOException ex)
          {
            System.err.println("Error al leer el archivo: " + ex.getMessage());
          }
        return aux.toString();
      }
}
