/**
 * Clase para contar la cantidad de apariciones de cada palabra a través de un
 * HashMap
 */
package soporte;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author maxi
 */
public class Conteo {

    private static HashMap<String, Integer> hm = new HashMap<>();

    /**
     * Método que agrega palabras al HashMap y cuenta la cantidad de 
     * apariciones.
     * @param key 
     * @return 
     */
    public static void add(String key) {
        if (hm.containsKey(key)) {
            hm.replace(key, (hm.get(key) + 1));
        } else {
            hm.put(key, 1);
        }
    }
    
    /**
     * Método que devuelve el HashMap completo.
     * @return 
     */
    public static HashMap<String, Integer> getHashMap(){
        return hm;
    }
}
