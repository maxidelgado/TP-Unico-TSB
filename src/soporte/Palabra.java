/*
 *Clase sencilla que representa un objeto palabra.
 *Necesaria para poder mostrar la información completa en
 *la tabla de la ventana principal.
 */
package soporte;

/**
 *
 * @author Rodrigo Gomez
 */
public class Palabra
{
    private String palabra;
    private int id;
    private int cantidad;
    private String libros;

    public Palabra(int id, String palabra, int cantidad, String libros)
      {
        this.id = id;
        this.palabra = palabra;
        this.cantidad = cantidad;
        this.libros = libros;
      }

    public String getPalabra()
      {
        return palabra;
      }

    public void setPalabra(String palabra)
      {
        this.palabra = palabra;
      }

    public int getId()
      {
        return id;
      }

    public void setId(int id)
      {
        this.id = id;
      }

    public int getCantidad()
      {
        return cantidad;
      }

    public void setCantidad(int cantidad)
      {
        this.cantidad = cantidad;
      }

    public String getLibros() {
        return libros;
    }

    public void setLibros(String libros) {
        this.libros = libros;
    }

    @Override
    public String toString()
      {
        return "Word{" + "palabra=" + palabra + ", id=" + id + ", cantidad=" + cantidad + ", libros=" + libros + '}';
      }

}
