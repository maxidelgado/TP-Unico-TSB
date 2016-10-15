package interfaz;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Principal {
    public static void main(String[] args) {
        try {
            Prueba p = new Prueba();
            p.setVisible(true);
        } catch (InterruptedException ex) {
            System.out.println("Error: "+ex.getMessage());
        }
    }
}
