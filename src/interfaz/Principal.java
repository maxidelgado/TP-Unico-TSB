package interfaz;

import soporte.*;

public class Principal
{

    public static void main(String[] args)
      {
        
        try
          {
            //Prueba p = new Prueba();
            Ventana v = new Ventana();
            v.setVisible(true);
          } catch (Exception ex)
          {
            System.out.println("Error: " + ex.getMessage());
          }
        
        String[] paths = {"txt/16082-8.txt", "txt/18166-8.txt", "txt/22975-8.txt", "txt/41575-8.txt"};
        
        //String[] paths = {"txt/asd.txt"};
        
        Archivo arc = new Archivo(paths, "Palabras");
        
        //arc.cargarDatabase();
        //arc.mostrarDatabase();
      }
    public static void reload(Ventana v)
      {
        v.dispose();
        v = new Ventana();
        v.setVisible(true);
      }
}
