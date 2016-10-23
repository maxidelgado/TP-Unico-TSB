package interfaz;

import soporte.*;

public class Principal
{

    public static void main(String[] args)
      {
        /*
        try
          {
            Prueba p = new Prueba();
            p.setVisible(true);
          } catch (InterruptedException ex)
          {
            System.out.println("Error: " + ex.getMessage());
          }
        */
        String[] paths = {"txt/16082-8.txt", "txt/18166-8.txt", "txt/22975-8.txt", "txt/41575-8.txt"};
        
        //String[] paths = {"txt/asd.txt"};
        
        Archivo arc = new Archivo(paths, "Palabras");
        
        arc.cargarDatabase();
      }
}
