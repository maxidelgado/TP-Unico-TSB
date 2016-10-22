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

    private File[] archivos;
    private Database base;

    public Archivo(String[] paths, String nomDB) {
        archivos = new File[paths.length];

        for (int i = 0; i < archivos.length; i++) {
            archivos[i] = new File(paths[i]);
        }
        base = new Database(nomDB);
    }

    /**
     * Obtiene la cantidad de líneas del archivo. Usa un buffered reader y
     * avanzar linea por linea hasta que finaliza el archivo, incrementando un
     * contador por cada linea.
     *
     * @return El tamaño del archivo en líneas.
     */
    public int tamaño() {
        int lineas = 0;
        for (File archivo : archivos) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                while (null != br.readLine()) {
                    lineas++;
                }
            } catch (IOException ex) {
                System.err.println("Error al leer el archivo: " + ex.getMessage());
            }
        }

        return lineas;
    }

    /**
     * Muestra el contenido del archivo. Sólo sirve para verificar que lea
     * correctamente. Acumula las salidas en un StringBuilder
     *
     * @return Un mega string con el texto entero
     */
    public String mostrarLineas() {
        StringBuilder aux = new StringBuilder();
        for (File archivo : archivos) {
            try (Scanner in = new Scanner(archivo)) {
                while (in.hasNext()) {
                    aux.append(in.nextLine());
                    aux.append("\n");
                }
            } catch (IOException ex) {
                System.err.println("Error al leer el archivo: " + ex.getMessage());
            }
        }

        return aux.toString();
    }

    /**
     * Obtiene palabra por palabra y la muestra por salida estándar. Utiliza el
     * Scanner y un delimitador de caracteres. La expresión regular
     * [^A-Za-zñ'áéíóú] indica caracteres de a..z, A..Z, ñ, y las vocales con
     * tilde. No verifica palabras mal formadas, ni contempla apostrofes o
     * tildes
     */
    public void mostrarPalabras() {
        for (File archivo : archivos) {
            try (Scanner in = new Scanner(archivo, "CP1252")) {
                in.useDelimiter("[^A-Za-zñ'áéíóú]");
                while (in.hasNext()) {
                    String aux = in.next();
                    if (!aux.isEmpty()) {
                        System.out.println(aux);
                    }
                }
            } catch (IOException ex) {
                System.err.println("Error al leer el archivo: " + ex.getMessage());
            }
        }

    }

    /**
     * Obtiene palabra por palabra e incrementa un contador. Su funcionamiento
     * es el mismo que mostrarPalabras, solo que incrementa un contador.
     *
     * @return La cantidad de palabras bien formadas que tiene el archivo.
     */
    public int contarPalabras() {
        int cantidad = 0;
        for (File archivo : archivos) {
            try (Scanner in = new Scanner(archivo, "CP1252")) {
                in.useDelimiter("[^A-Za-zñ'áéíóú]");
                while (in.hasNext()) {
                    String aux = in.next();
                    if (!aux.isEmpty()) {
                        cantidad++;
                    }
                }
            } catch (IOException ex) {
                System.err.println("Error al leer el archivo: " + ex.getMessage());
            }
        }
        return cantidad;

    }

    /**
     * Obtiene palabra por palabra, la carga en un HashMap de conteo y
     * posteriormente guarda el resultado en la DB.
     */
    public void cargarDatabase() {
        for (File archivo : archivos) {
            int cantidad = 0;
            try (Scanner in = new Scanner(archivo, "CP1252")) {
                in.useDelimiter("[^A-Za-zñ'áéíóú]");
                while (in.hasNext()) {
                    String aux = in.next();
                    if (!aux.isEmpty()) {
                        Conteo.add(aux);
                    }
                }

            } catch (IOException ex) {
                System.err.println("Error al leer el archivo: " + ex.getMessage());
            }
        }
        for (HashMap.Entry<String, Integer> entry : Conteo.getHashMap().entrySet()) {
            base.cargar(entry.getKey(), entry.getValue());
        }
    }
}
