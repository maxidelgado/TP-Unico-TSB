package soporte;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Clase para contar la cantidad de apariciones de una palabra en un libro a
 * través de un LinkedHashMap que permite mantener el orden de inserción. Esto
 * nos permite ordenar el HashMap.
 *
 * @author maxi
 */
public class Conteo {

    private static HashMap<String, Integer> hm = new LinkedHashMap<>();

    /**
     * Método que agrega palabras al HashMap y cuenta la cantidad de
     * apariciones.
     *
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
     *
     * @return
     */
    public static HashMap<String, Integer> getHashMap() {
        hm = MapUtil.sortByValue(hm);
        return hm;
    }

    /**
     * Método que limpia el HashMap
     */
    public static void clear() {
        hm.clear();
    }
}

/**
 * Esta clase contiene el método necesario para ordenar un LinkedHashMap, de
 * esta manera las inserciones en la DB se realizarán por órden alfabético
 *
 * @author maxi
 */
class MapUtil {

    public static <K extends Comparable, V extends Comparable<? super V>> HashMap<K, V>
            sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list
                = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<HashMap.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getKey()).compareTo(o2.getKey());
            }
        });

        HashMap<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
