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
        Archivo arc = new Archivo();

        arc.mostrarPalabras();
          System.out.println("Total de palabras: " + arc.contarPalabras());
        System.out.println("Total: " + arc.tamaño() + " lineas.");
      }
}
