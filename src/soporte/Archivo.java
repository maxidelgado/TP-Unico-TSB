/*
 * Clase encargada de leer un archivo de disco
 * información sobre la expresión regular:
 * https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
 * Información sobre el la codificación de texto:
 * http://stackoverflow.com/questions/18901316/curly-quotes-causing-java-scanner-hasnextline-to-be-false-why
 * La codificación de los textos del ejemplo es ANSI, para que Scanner los lea
 * hay que indicárselo al momento de inicializarlo, Scanner(archivo, "Cp1252")
 * "ANSI" es CP1252
 * "Unicode" es UTF-16LE
 * "UTF-8" es... UTF-8
 */
package soporte;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author rodrigo
 */
public class Archivo {

    private File file;

    public Archivo(File file) {
        this.file = file;
    }

    /**
     * Lee el archivo utilizando expresiones regulares para determinar qué es
     * una palabra y qué no lo es.
     *
     * @return
     */
    public HashMap<String, Integer> leer() {
        try (Scanner in = new Scanner(file, "CP1252")) {
            in.useDelimiter("[^A-Za-zñáéíóú]");
            while (in.hasNext()) {
                String aux = in.next();
                aux = aux.toLowerCase();
                if (!aux.isEmpty()) {
                    Conteo.add(aux);
                }
            }
            return Conteo.getHashMap();
        } catch (FileNotFoundException ex) {
            System.err.println("Error al leer el archivo: " + ex.getMessage());
        }
        return null;
    }
}
