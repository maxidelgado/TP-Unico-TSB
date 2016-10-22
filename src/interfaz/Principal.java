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
        String[] paths = {"txt/asd.txt"};
        
        Archivo arc = new Archivo(paths, "Palabras");
        
        arc.cargarDatabase();
      }
}
