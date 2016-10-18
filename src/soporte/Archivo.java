/*
 * Clase encargada de leer un archivo de disco
 */
package soporte;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

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

    /**
     * Obtiene palabra por palabra y la muestra por salida estándar. Utiliza el Scanner y un delimitador de caracteres, solo acepta A..Z y a..Z TODO: cuando lee algo como h0la, separa 'h' y 'la' como dos palabras independientes. una manera de resolver las palabras mal formadas, es quitando el delimitador y comprobando if (!aux.isEmpty() && Pattern.matches("[a-zA-Z]+", aux)) contra una expresión regular y un patrón Este mismo método se puede utilizar para devolver una colección de palabras individuales.
     */
    public void mostrarPalabras()
      {

        try (Scanner in = new Scanner(archivo))
          {
            in.useDelimiter("[^A-Za-z]");
            while (in.hasNext())
              {
                String aux = in.next();
                if (!aux.isEmpty())
                  {
                    System.out.println(aux);
                  }
              }
          } catch (IOException ex)
          {
            System.err.println("Error al leer el archivo: " + ex.getMessage());
          }
      }

    /**
     * Obtiene palabra por palabra e incrementa un contador. TODO: el mismo error que el método mostrar palabras
     *
     * @return La cantidad de palabras bien formadas que tiene el archivo.
     */
    public int contarPalabras()
      {
        int cantidad = 0;
        try (Scanner in = new Scanner(archivo))
          {
            in.useDelimiter("[^A-Za-z]");
            while (in.hasNext())
              {
                String aux = in.next();
                if (!aux.isEmpty())
                  {
                    cantidad++;
                  }
              }
          } catch (IOException ex)
          {
            System.err.println("Error al leer el archivo: " + ex.getMessage());
          }
        return cantidad;
      }
}
