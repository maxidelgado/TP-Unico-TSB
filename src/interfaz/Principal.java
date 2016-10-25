package interfaz;

import soporte.*;

public class Principal
{

    public static void main(String[] args)
      {
        try
          {
            Ventana v = new Ventana();
            v.setVisible(true);
          } catch (Exception ex)
          {
            System.out.println("Error: " + ex.getMessage());
          }
      }
}
