/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soporte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author maxi
 */
public class Palabras {
    private ArrayList<Palabra> lista;
    private HashMap<Integer, Palabra> map;

    public Palabras() {
        lista = new ArrayList<>();
        map = new HashMap<>();
    }
    
    public void add_without_rep(Palabra p){
        if(!map.containsKey(p.getId())){
            map.put(p.getId(), p);
        } else {
            String aux = map.get(p.getId()).getLibros() + ", " + p.getLibros();
            map.replace(p.getId(), new Palabra(p.getId(), p.getPalabra(), p.getCantidad(), aux));
        }
    }

    public HashMap<Integer, Palabra> getMap() {
        return map;
    }
    
    @Override
    public String toString(){
        String res = "";
        for (Map.Entry<Integer, Palabra> entry : map.entrySet()) {
            res += entry.getValue() + "\n";
        }
        return res;
    }
}
